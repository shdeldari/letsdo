package nz.alex.letsdo;

import java.util.ArrayList;
import java.util.List;

import nz.alex.letsdo.tools.BasicListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MultipleSelectorActivity extends Activity{
	protected TaskSource taskSource;
	protected List<TaskModel> values;
	protected List<Integer> keys;
	
	//on ALL check-box click
	public void onChkBoxClick(View view){
		//implement method to select all tasks in the current list
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_selection);
		taskSource = TaskSource.GetInstance(this);
		taskSource.open();

		if (taskSource.getLen() > 0){
			values = new ArrayList<TaskModel>(taskSource.getAllTasks().values());
			keys = new ArrayList<Integer>(taskSource.getAllTasks().keySet());
			// use the SimpleCursorAdapter to show the
			// elements in a ListView
			ListView list = (ListView)findViewById(R.id.listView2);
			BasicListAdapter adapter = new BasicListAdapter(this, R.layout.list_item, values, true);
			list.setAdapter(adapter);
			
		}
	}

}
