
package nz.alex.letsdo;

import java.util.ArrayList;
import java.util.List;

import nz.alex.letsdo.BasicListAdapter.ViewHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;


public class MultipleSelectorActivity extends Activity{
	protected TaskSource taskSource;
	protected List<TaskModel> values;
	protected List<Integer> keys;
	protected BasicListAdapter adapter;
	protected ListView list;
	protected CheckBox chkbox;
	
	//on ALL check-box click
	public void onChkBoxClick(View view){
		adapter= new BasicListAdapter(this, R.layout.list_item, values, chkbox.isChecked());
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter);
		list.refreshDrawableState();
	}
	
	public void onDeleteClick(View view){
		ArrayList<Integer> selectedToDelete = adapter.getSelected();
		System.out.println("to delete : "+selectedToDelete.size());
		keys = new ArrayList(taskSource.getAllTasks().keySet());
		System.out.println("keys to delete : "+keys.size());
//		for (int i = 0; i < selectedToDelete.size(); i++) {
//			TaskSource.GetInstance(getBaseContext()).deleteTask((keys.get(selectedToDelete.get(i).intValue())).toString() );
//		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_selection);
		setLayout();
		taskSource = TaskSource.GetInstance(this);
		taskSource.open();

		if (taskSource.getLen() > 0){
			values = new ArrayList<TaskModel>(taskSource.getAllTasks().values());
			keys = new ArrayList<Integer>(taskSource.getAllTasks().keySet());
			// use the SimpleCursorAdapter to show the
			// elements in a ListView
			adapter = new BasicListAdapter(this, R.layout.list_item, values, false);
			list.setAdapter(adapter);
			
		}
	}

	private void setLayout() {
		chkbox = (CheckBox)findViewById(R.id.selectAll);
		list = (ListView)findViewById(R.id.listView2);
	}
	
	static class ViewHolder {    	
        TextView text;
        CheckBox chkbox;
    }
}