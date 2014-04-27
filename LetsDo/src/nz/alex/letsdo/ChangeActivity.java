package nz.alex.letsdo;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

@SuppressLint("CutPasteId")

public class ChangeActivity extends Activity implements OnItemSelectedListener{

	protected TaskSource taskSource;

	protected ArrayAdapter<String> adapterCategory;
	protected ArrayAdapter<String> adapterAssignee;

	protected String customValue;

	protected int taskId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		taskId = intent.getIntExtra(MainActivity.EXTRA_MESSAGE,-1);

		setContentView(R.layout.activity_change);
		// Show the Up button in the action bar.
		setupActionBar();

		taskSource = TaskSource.GetInstance(this);
		taskSource.open();

		customValue = getString(R.string.customValue);

		Spinner spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
		adapterCategory = new ArrayAdapter<String>(ChangeActivity.this, android.R.layout.simple_spinner_item, taskSource.getCategoryList());
		adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterCategory.add(customValue);
		spinnerCategory.setAdapter(adapterCategory);
		spinnerCategory.setOnItemSelectedListener(this);

		Spinner spinnerAssignee = (Spinner) findViewById(R.id.spinnerAssignee);
		adapterAssignee = new ArrayAdapter<String>(ChangeActivity.this, android.R.layout.simple_spinner_item, taskSource.getAssigneeList());
		adapterAssignee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterAssignee.add(customValue);
		spinnerAssignee.setAdapter(adapterAssignee);
		spinnerAssignee.setOnItemSelectedListener(this);

		if (taskId == -1){
			setTitle(getString(R.string.title_activity_add));
		}
		else{
			try{
				TaskModel aTask = taskSource.findTask(taskId);

				EditText editText = (EditText)findViewById(R.id.taskTitle);
				editText.setText(aTask.title);
				
				Spinner spinner = (Spinner) findViewById(R.id.spinnerCategory);
				spinner.setSelection(adapterCategory.getPosition(aTask.category));

				spinner = (Spinner) findViewById(R.id.spinnerAssignee);
				spinner.setSelection(adapterAssignee.getPosition(aTask.assignee));

				editText = (EditText)findViewById(R.id.taskDescription);
				editText.setText(aTask.description);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View view){
		switch (view.getId()){
		case R.id.saveTask:
			String taskTitle = ((EditText)findViewById(R.id.taskTitle)).getText().toString();
			if (taskTitle.length() == 0){
				Toast.makeText(this, "Give it a title!", Toast.LENGTH_SHORT).show();
				break;
			}
			TaskModel aTask = new TaskModel(((EditText)findViewById(R.id.taskTitle)).getText().toString(), ((Spinner)findViewById(R.id.spinnerCategory)).getSelectedItem().toString(),
					((Spinner)findViewById(R.id.spinnerAssignee)).getSelectedItem().toString(), ((EditText)findViewById(R.id.taskDescription)).getText().toString());
			if (taskId < 0)
				taskSource.addTask(aTask);
			else
				taskSource.changeTask(taskId, aTask);
			taskSource.close();
			finish();
			break;
		case R.id.delTask:
			if (taskId >= 0)
				taskSource.deleteTask(taskId);
		case R.id.discardTask:
			taskSource.close();
			finish();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		ArrayAdapter<String> adapter = null;

		Spinner spinner = (Spinner) parent;
		int spinnerId = spinner.getId();

		if (spinnerId == R.id.spinnerCategory){
			adapter = adapterCategory;
		}
		else if (spinnerId == R.id.spinnerAssignee){
			adapter = adapterAssignee;
		}
		else 
			return;

		final ArrayAdapter<String> finalAdapter = adapter;

		if (spinner.getSelectedItem().toString() == customValue){
			final Dialog dialog = new Dialog(ChangeActivity.this);
			dialog.setContentView(R.layout.new_value);

			if (spinnerId == R.id.spinnerCategory)
				dialog.setTitle(getString(R.string.addCategoryTitle));
			else if (spinnerId == R.id.spinnerAssignee)
				dialog.setTitle(getString(R.string.addAssigneeTitle));

			Button addCategory = (Button) dialog.findViewById(R.id.addValue);
			addCategory.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					EditText newVal = (EditText) dialog.findViewById(R.id.newValue);
					finalAdapter.remove(customValue);
					finalAdapter.add(newVal.getText().toString());
					finalAdapter.add(customValue);
					finalAdapter.notifyDataSetChanged();
					dialog.dismiss();
				}
			});

			dialog.show();	
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
}
