package nz.alex.letsdo;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private TaskSource taskSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		taskSource = new TaskSource(this);
		taskSource.open();

		if (taskSource.getLen() > 0){
			List<TaskModel> values = new ArrayList<TaskModel>(taskSource.getAllTasks().values());

			// use the SimpleCursorAdapter to show the
			// elements in a ListView
			ListView list = (ListView)findViewById(R.id.listView1);
			ArrayAdapter<TaskModel> adapter = new ArrayAdapter<TaskModel>(this,
					android.R.layout.simple_list_item_1, values);
			list.setAdapter(adapter);	
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick (View view) {
		Intent intent = new Intent(this, AddActivity.class);
		startActivity(intent);
	} 
}