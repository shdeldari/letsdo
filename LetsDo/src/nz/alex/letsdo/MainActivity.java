package nz.alex.letsdo;

import java.util.ArrayList;
import java.util.List;

import nz.alex.letsdo.tools.BasicListAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	protected TaskSource taskSource;
	protected Context context;
	
	protected List<TaskModel> values;
	protected List<Integer> keys;

	public final static String EXTRA_MESSAGE = "nz.alex.letsdo.MESSAGE";
	protected ListView list;
	protected int ADD_REQ = 1;
	
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
			list = (ListView)findViewById(R.id.listView1);
			BasicListAdapter adapter = new BasicListAdapter(this, R.layout.list_item, values, false);
			list.setAdapter(adapter);
			list.setOnItemClickListener(itemClickListener);
			list.setOnItemLongClickListener(itemLongClickListener);
		}
		this.context = this.getApplicationContext();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		taskSource.open();
		values = new ArrayList<TaskModel>(taskSource.getAllTasks().values());
		keys = new ArrayList<Integer>(taskSource.getAllTasks().keySet());
		// use the SimpleCursorAdapter to show the
		// elements in a ListView
		BasicListAdapter adapter = new BasicListAdapter(this,R.layout.list_item, values, false);
		list.setAdapter(adapter);
		list.refreshDrawableState();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		System.out.println("back to main page2");
		list.refreshDrawableState();
//		this.historyProfs = this.db.getHistory(-1);
//	    this.listAdapter.setData(this.historyProfs);
//	    this.listAdapter.notifyDataSetChanged();
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
	
	public OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			taskSource.close();

			Intent intent = new Intent(getApplicationContext(), ChangeActivity.class);
			intent.putExtra(EXTRA_MESSAGE, keys.get(position).toString());
			startActivityForResult(intent, ADD_REQ);
		}
	};
	public OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			System.out.println("multiple selection");
			Intent intent = new Intent(context, MultipleSelectorActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return true;
		}
	};
		
	

}