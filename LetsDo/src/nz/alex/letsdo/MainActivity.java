package nz.alex.letsdo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

public class MainActivity extends Activity {
	private static final String DEBUG_TAG = "LetsDo.MainActivity";
	// Projection array. Creating indices for this array instead of doing
	// dynamic lookups improves performance.
	public static final String[] EVENT_PROJECTION = new String[] {
		Calendars._ID,                           // 0
		Calendars.ACCOUNT_NAME,                  // 1
		Calendars.CALENDAR_DISPLAY_NAME,         // 2
		Calendars.OWNER_ACCOUNT                  // 3
	};

	// The indices for the projection array above.
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

	protected enum ActivityMode {
		LIST,
		DELETE;
	}

	protected enum GroupMode {
		GROUPED_BY_ASSIGNEE,
		GROUPED_BY_CATEGORY;
	}

	protected TaskSource taskSource;
	protected Context context;
	protected ArrayList<Task> tasks;

	public final static String EXTRA_MESSAGE = "nz.alex.letsdo.MESSAGE";
	protected Switch filterSw;
	protected boolean DELETE_MODE = false;
	protected ArrayList<View> deleteBtn = new ArrayList<View>();


	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	///-----
	List<String> childList;
	protected Map<String, List<Task>> groupedTaskList;
	protected List<String> groupList;
	protected ExpandableListView expListView;
	protected ExpandableListAdapter expListAdapter;
	//-------

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
					return false;
				}
				// right to left swipe
				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					OnListSwipeLeft((int)e1.getX(), (int) e1.getY());
				}  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					OnListSwipeRight((int)e1.getX(), (int) e1.getY());
				}
			} catch (Exception e) {
				System.out.println("exception: "+ e.getMessage());
			}
			return false;
		}
	}

	public void onFilterClick(View view){
		updateList();
	}

	@Override
	public void onBackPressed() {
		if(DELETE_MODE){
			for (int i = 0; i < deleteBtn.size(); i++) {
				deleteBtn.get(i).setVisibility(View.INVISIBLE);
			}
			DELETE_MODE = false;
			deleteBtn.clear();
		}else{
			super.onBackPressed();
		}
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
		expListView.setOnChildClickListener(childClickListener);
		expListView.setOnItemLongClickListener(itemLongClickListener);
		expListView.setOnTouchListener(gestureListener);
	}

	private void setupLayout() {
		// Filter button
		filterSw = (Switch) findViewById(R.id.switch1);
		filterSw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				onFilterClick(arg0);
			}
		});
		// Gesture detection
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};
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

	public void onClick (View view) {
		taskSource.close();

		Intent intent = new Intent(this, ChangeActivity.class);
		intent.putExtra(EXTRA_MESSAGE, -1);
		startActivity(intent);
	} 

	public OnChildClickListener childClickListener = new OnChildClickListener()  {

		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			final Task selected = (Task) expListAdapter.getChild(
					groupPosition, childPosition);
			//taskSource.close();
			Intent intent = new Intent(getApplicationContext(), ChangeActivity.class);
			intent.putExtra(EXTRA_MESSAGE, selected.getId());
			startActivity(intent);
			return true;
		}
	};
	public OnGroupClickListener GroupClickListener = new OnGroupClickListener(){
		@Override
		public boolean onGroupClick(ExpandableListView arg0, View arg1,
				int arg2, long arg3) {
			Toast.makeText(getBaseContext(),  " " +arg2 + ":"+arg3, Toast.LENGTH_LONG)
			.show();
			return true;
		}

	};
	public OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			taskSource.close();


			Intent intent = new Intent(getApplicationContext(), ChangeActivity.class);
			intent.putExtra(EXTRA_MESSAGE, tasks.get(position).getId());
			startActivity(intent);
		}
	};

	public OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int group_position, long child_position) {
			DELETE_MODE = true;
			arg1.findViewById(R.id.chkBox).setVisibility(View.VISIBLE);;
			deleteBtn.add(arg1.findViewById(R.id.chkBox));
			return true;
		}
	};

	protected void OnListSwipeLeft(int x, int y){
		long packedPosition = expListView.getExpandableListPosition(expListView.pointToPosition(x, y));
		int groupPosition=0;
		int childPosition=-1;
		int positionType = ExpandableListView.getPackedPositionType(packedPosition);
		if( positionType != ExpandableListView.PACKED_POSITION_TYPE_NULL ){		      
			groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
			if(positionType == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
				childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
			}
		}else{
			Log.d("FooLabel", "positionType was NULL - header/footer?");
		}
		if(childPosition>=0){
			Task aTask = groupedTaskList.get(groupList.get(groupPosition)).get(childPosition);
			aTask.setStatus(TaskStatus.OPENED);
			taskSource.openTask(aTask.getId());

			expListAdapter.notifyDataSetChanged();
		}
	}

	protected void OnListSwipeRight(int x, int y){
		long packedPosition = expListView.getExpandableListPosition(expListView.pointToPosition(x, y));
		int groupPosition=0;
		int childPosition=-1;
		int positionType = ExpandableListView.getPackedPositionType(packedPosition);
		if( positionType != ExpandableListView.PACKED_POSITION_TYPE_NULL ){		      
			groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
			if(positionType == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
				childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
			}
		}else{
			Log.d("FooLabel", "positionType was NULL - header/footer?");
		}
		if(childPosition>=0){
			Task aTask = groupedTaskList.get(groupList.get(groupPosition)).get(childPosition);
			aTask.setStatus(TaskStatus.CLOSED);
			taskSource.closeTask(aTask.getId());

			expListAdapter.notifyDataSetChanged();
		}
	}

	private Map<String, List<Task>> createCollection(GroupMode mode) {
		groupedTaskList = new LinkedHashMap<String, List<Task>>();
		ArrayList<Task> tasks = TaskSource.GetInstance(context).getAllTasks();
		groupList = new ArrayList<String>();

		if (mode == GroupMode.GROUPED_BY_ASSIGNEE)
			groupList = TaskSource.GetInstance(context).getAssigneeList();
		else 
			groupList = TaskSource.GetInstance(context).getCategoryList();

		for (String groupName : groupList) {
			ArrayList<Task> openedTasks = new ArrayList<Task>();
			ArrayList<Task> closedTasks = new ArrayList<Task>();
			for (Task task: tasks) 
				if (mode == GroupMode.GROUPED_BY_ASSIGNEE){
					if(task.getAssignee().equalsIgnoreCase(groupName)){
						if (task.isOpen())
							openedTasks.add(task);
						else
							closedTasks.add(task);
					}
				}
				else{
					if(task.getCategory().equalsIgnoreCase(groupName)){
						if (task.isOpen())
							openedTasks.add(task);
						else
							closedTasks.add(task);
					}
				}
			openedTasks.addAll(closedTasks);
			groupedTaskList.put(groupName.trim(), openedTasks);
		}
		return groupedTaskList;
	}

	protected void updateList(){
		if (filterSw.isChecked())
			groupedTaskList = createCollection(GroupMode.GROUPED_BY_ASSIGNEE);
		else 
			groupedTaskList = createCollection(GroupMode.GROUPED_BY_CATEGORY);
		
		expListAdapter = new ExpandableListAdapter(this, groupList, groupedTaskList, taskSource);
		expListView.setAdapter(expListAdapter);
		expListView.refreshDrawableState();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_settings){
			// Run query
			ContentResolver cr = getContentResolver();
			Uri uri = Calendars.CONTENT_URI;   
			Cursor cur = cr.query(uri, EVENT_PROJECTION, null, null, null);

/*			long calID = 0;
			while (cur.moveToNext()){
				calID = cur.getLong(PROJECTION_ID_INDEX);
				break;
			}
*/			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select a Calendar to Sync")
			.setSingleChoiceItems(cur, -1, Calendars.CALENDAR_DISPLAY_NAME, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Log.d(DEBUG_TAG, "selected item is " + which);
				}
				
				
			})
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Log.d(DEBUG_TAG, "AlertDialog's OK pressed");
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Log.d(DEBUG_TAG, "AlertDialog's Cancel pressed");
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
			
		return super.onOptionsItemSelected(item);
	}
}