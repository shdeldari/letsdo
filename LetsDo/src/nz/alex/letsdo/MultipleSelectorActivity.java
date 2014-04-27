package nz.alex.letsdo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.Switch;

public class MultipleSelectorActivity extends Activity {
	protected TaskSource taskSource;
	protected Context context;

	protected ArrayList<Task> tasks;

	public final static String EXTRA_MESSAGE = "nz.alex.letsdo.MESSAGE";
	protected Switch filterSw;
	
	///-----
    List<String> childList;
    protected Map<String, List<Task>> allTaskList;
    protected List<String> groupList;
    protected ExpandableListView expListView;
    protected ExpandableListAdapter expListAdapter;
    //-------

	
	public void onFilterClick(View view){
		updateList();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupLayout();
		
		taskSource = TaskSource.GetInstance(this);
		taskSource.open();
		tasks = taskSource.getAllTasks();
			
		updateList();
       
	}

	private void setupLayout() {
		// Filter button
		filterSw = (Switch) findViewById(R.id.switch1);
		filterSw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				onFilterClick(arg0);
			}
		});
		
		expListView = (ExpandableListView) findViewById(R.id.listView1);
	}

	@Override
	protected void onResume(){
		super.onResume();
		taskSource.open();
		tasks = taskSource.getAllTasks();
		updateList();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		System.out.println("back to main page2");
		updateList();
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

	
    private List<String> createGroupList() {
    	List<String> groupList = new ArrayList<String>();
        if(filterSw.isChecked())
        	groupList = TaskSource.GetInstance(context).getAssigneeList();
        else 
        	groupList = TaskSource.GetInstance(context).getCategoryList();
		return groupList;
    }
 
    private Map<String, List<Task>> createCollection(List<String> groupList) {
    	allTaskList = new LinkedHashMap<String, List<Task>>();
    	if(filterSw.isChecked()){
    		ArrayList<Task> tasks = TaskSource.GetInstance(context).getTasksOrderedBy(TaskColumns.ASSIGNEE.name());
    		for (String g : groupList) {
    			ArrayList<Task> s = new ArrayList<Task>();
    			for (Task t: tasks) 
					if(t.getAssignee().equalsIgnoreCase(g))
						s.add(t);
    			allTaskList.put(g.trim(), s);
			}
    	}
    	else{
    		ArrayList<Task> tasks = TaskSource.GetInstance(context).getTasksOrderedBy(TaskColumns.CATEGORY.name());
    		for (String g : groupList) {
    			ArrayList<Task> s = new ArrayList<Task>();
    			for (Task t: tasks) 
    				if(t.getCategory().equalsIgnoreCase(g))
    					s.add(t);
    			allTaskList.put(g.trim(), s);
			}
    	}
    	return allTaskList;
    }
    
    protected void updateList(){
    	groupList = createGroupList(); 
		allTaskList = createCollection(groupList);
		expListAdapter = new ExpandableListAdapter(this, groupList, createCollection(groupList));
		expListView.setAdapter(expListAdapter);
		expListView.refreshDrawableState();
    }
}