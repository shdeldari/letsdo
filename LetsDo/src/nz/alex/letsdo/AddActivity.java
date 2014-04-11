package nz.alex.letsdo;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

public class AddActivity extends Activity implements OnItemSelectedListener{

	private TaskSource taskSource;
	
	ArrayAdapter<String> adapterCategory;
	ArrayAdapter<String> adapterAssignee;
	String customValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		setupActionBar();

		taskSource = TaskSource.GetInstance(this);
		taskSource.open();

		customValue = getString(R.string.customValue);

		Spinner spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
		adapterCategory = new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_spinner_item, taskSource.getCategoryList());
		adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterCategory.add(customValue);
		spinnerCategory.setAdapter(adapterCategory);
		spinnerCategory.setOnItemSelectedListener(this);
		
		Spinner spinnerAssignee = (Spinner) findViewById(R.id.spinnerAssignee);
		adapterAssignee = new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_spinner_item, taskSource.getAssigneeList());
		adapterAssignee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterAssignee.add(customValue);
		spinnerAssignee.setAdapter(adapterAssignee);
		spinnerAssignee.setOnItemSelectedListener(this);
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
		getMenuInflater().inflate(R.menu.add, menu);
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

	public void onClick (View view) {
		TaskModel aTask = new TaskModel(((EditText)findViewById(R.id.taskTitle)).getText().toString(), ((Spinner)findViewById(R.id.spinnerCategory)).getSelectedItem().toString(),
				((Spinner)findViewById(R.id.spinnerAssignee)).getSelectedItem().toString(), ((EditText)findViewById(R.id.taskDescription)).getText().toString());

		taskSource.addTask(aTask);

		Context context = getApplicationContext();
		CharSequence text = aTask.toString();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();	
		this.setResult(1);
		finish();
	}

	public void onStop(){
		taskSource.close();
		super.onStop();
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
			final Dialog dialog = new Dialog(AddActivity.this);
			dialog.setContentView(R.layout.new_value);
			dialog.setTitle(getString(R.string.addValueTitle));
			
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
