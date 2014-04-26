
package nz.alex.letsdo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;


public class MultipleSelectorActivity extends Activity{
	protected TaskSource taskSource;
	protected ArrayList<Task> tasks;
	protected ExpandableListView expListView;
	protected ExpandableListAdapter expListAdapter;
	protected ListView list;
	protected CheckBox chkbox;
	
	//on ALL check-box click
	public void onChkBoxClick(View view){
//		expListAdapter= new ExpandableListAdapter(this, groupList, createCollection(groupList));
//		expListAdapter.notifyDataSetChanged();
//		expListView.setAdapter(expListAdapter);
//		expListView.refreshDrawableState();
	}
	
	public void onDeleteClick(View view){
//		ArrayList<Integer> selectedToDelete = adapter.getSelected();
//		System.out.println("to delete : "+selectedToDelete.size());
//		tasks = taskSource.getAllTasks();
//		System.out.println("tasks to delete : "+tasks.size());
////		for (int i = 0; i < selectedToDelete.size(); i++) {
////			TaskSource.GetInstance(getBaseContext()).deleteTask((keys.get(selectedToDelete.get(i).intValue())).toString() );
////		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_selection);
		setLayout();
		taskSource = TaskSource.GetInstance(this);
		taskSource.open();

		if (taskSource.getLen() > 0){
			tasks = taskSource.getAllTasks();

			expListView = (ExpandableListView) findViewById(R.id.listView1);
			expListView.setAdapter(expListAdapter);
		}
	}

	private void setLayout() {
		//chkbox = (CheckBox)findViewById(R.id.selectAll);
		expListView = (ExpandableListView)findViewById(R.id.listView1);
	}
	
	static class ViewHolder {    	
        TextView text;
        CheckBox chkbox;
    }
	protected void updateList(){
//    	groupList = createGroupList(); 
//		allTaskList = createCollection(groupList);
//		expListAdapter = new ExpandableListAdapter(this, groupList, createCollection(groupList));
//		expListView.setAdapter(expListAdapter);
//		expListView.refreshDrawableState();
    }
}