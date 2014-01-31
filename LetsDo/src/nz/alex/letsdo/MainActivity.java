package nz.alex.letsdo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

class Task{
	protected String title, category, assignee, description;
	public Task(String tTitle, String tCategory, String tAssignee, String tDescription){
		setTask(tTitle, tCategory, tAssignee, tDescription);
	}
	public void setTask(String tTitle, String tCategory, String tAssignee, String tDescription){
		title = tTitle;
		category = tCategory;
		assignee = tAssignee;
		description = tDescription;
	}
	public String getTitle(){
		return title;
	}
}

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick (View view) {
		Task aTask = new Task(((EditText)findViewById(R.id.taskTitle)).getText().toString(), ((EditText)findViewById(R.id.taskCategory)).getText().toString(),
				((EditText)findViewById(R.id.taskAssignee)).getText().toString(), ((EditText)findViewById(R.id.taskDescription)).getText().toString());
		Toast.makeText(this, aTask.getTitle(),
			Toast.LENGTH_LONG).show();
	} 
}