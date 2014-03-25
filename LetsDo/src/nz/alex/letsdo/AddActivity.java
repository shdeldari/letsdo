package nz.alex.letsdo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class AddActivity extends Activity {

	private TaskSource taskSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		// Show the Up button in the action bar.
		setupActionBar();
		taskSource = TaskSource.GetInstance(this);
		taskSource.open();
		
		Spinner spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
		taskSource.getColumnList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_spinner_item, taskSource.getColumnList());
		spinnerCategory.setAdapter(adapter);
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
				((EditText)findViewById(R.id.taskAssignee)).getText().toString(), ((EditText)findViewById(R.id.taskDescription)).getText().toString());

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
}
