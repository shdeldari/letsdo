package nz.alex.letsdo;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	protected TaskSource taskSource;

	protected List<TaskModel> values;
	protected List<Integer> keys;

	public final static String EXTRA_MESSAGE = "nz.alex.letsdo.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		taskSource = TaskSource.GetInstance(this);
		taskSource.open();

		if (taskSource.getLen() > 0){
			values = new ArrayList<TaskModel>(taskSource.getAllTasks().values());
			keys = new ArrayList<Integer>(taskSource.getAllTasks().keySet());
			// use the SimpleCursorAdapter to show the
			// elements in a ListView
			ListView list = (ListView)findViewById(R.id.listView1);
			ArrayAdapter<TaskModel> adapter = new ArrayAdapter<TaskModel>(this,
					android.R.layout.simple_list_item_1, values);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener(){
				@Override public void onItemClick(AdapterView<?> parent, View view, int position, long id){
					taskSource.close();

					Intent intent = new Intent(getApplicationContext(), ChangeActivity.class);
					intent.putExtra(EXTRA_MESSAGE, keys.get(position).toString());
					startActivity(intent);
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick (View view) {
		taskSource.close();

		Intent intent = new Intent(this, AddActivity.class);
		startActivity(intent);
	} 

}