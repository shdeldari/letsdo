package nz.alex.letsdo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class ChangeActivity extends Activity {

	protected TaskSource taskSource;
	
	protected String rowID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		rowID = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

		setContentView(R.layout.activity_change);
		// Show the Up button in the action bar.
		setupActionBar();

		taskSource = TaskSource.GetInstance(this);
		taskSource.open();

		try{
			TaskModel aTask = taskSource.findTask(rowID);

			EditText editText = (EditText)findViewById(R.id.taskTitle);
			editText.setText(aTask.title);

			editText = (EditText)findViewById(R.id.taskAssignee);
			editText.setText(aTask.assignee);

			editText = (EditText)findViewById(R.id.taskCategory);
			editText.setText(aTask.category);

			editText = (EditText)findViewById(R.id.taskDescription);
			editText.setText(aTask.description);
		}catch (Exception e){
			e.printStackTrace();
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
			TaskModel aTask = new TaskModel(((EditText)findViewById(R.id.taskTitle)).getText().toString(), ((EditText)findViewById(R.id.taskCategory)).getText().toString(),
					((EditText)findViewById(R.id.taskAssignee)).getText().toString(), ((EditText)findViewById(R.id.taskDescription)).getText().toString());
			taskSource.changeTask(rowID, aTask);
		case R.id.discardTask:
			taskSource.close();
			finish();
			break;
		}
	}
}
