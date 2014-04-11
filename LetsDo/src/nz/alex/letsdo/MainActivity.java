package nz.alex.letsdo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nz.alex.letsdo.tools.ExpandableListAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	protected TaskSource taskSource;
	protected Context context;

	protected List<TaskModel> values;
	protected List<Integer> keys;

	public final static String EXTRA_MESSAGE = "nz.alex.letsdo.MESSAGE";
	protected ListView list;

	BasicListAdapter adapter;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	
	///-----
	List<String> groupList;
    List<String> childList;
    Map<String, List<String>> laptopCollection;
    ExpandableListView expListView;
    //-------

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					OnListSwipeLeft((int)e1.getX(), (int) e1.getY());
				}  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					OnListSwipeRight((int)e1.getX(), (int) e1.getY());
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	}
	
	public void onFilterClick(View view){
		System.out.println("filter click!");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("before");
		setContentView(R.layout.activity_main);
		System.out.println("after");
		
		taskSource = TaskSource.GetInstance(this);
		taskSource.open();

		values = new ArrayList<TaskModel>(taskSource.getAllTasks().values());
		keys = new ArrayList<Integer>(taskSource.getAllTasks().keySet());

		// Gesture detection
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		list = (ListView)findViewById(R.id.listView1);
		BasicListAdapter adapter = new BasicListAdapter(this, R.layout.list_item, values, false);
		list.setAdapter(adapter);
		list.setOnItemClickListener(itemClickListener);
		list.setOnItemLongClickListener(itemLongClickListener);
		list.setOnTouchListener(gestureListener);
		
		
//		createGroupList();
//		 
//        createCollection();
// 
//        expListView = (ExpandableListView) findViewById(R.id.listView1);
//        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
//                this, groupList, laptopCollection);
//        expListView.setAdapter(expListAdapter);
//        expListView.setOnChildClickListener(new OnChildClickListener() {
//        	 
//            public boolean onChildClick(ExpandableListView parent, View v,
//                    int groupPosition, int childPosition, long id) {
//                final String selected = (String) expListAdapter.getChild(
//                        groupPosition, childPosition);
//                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
//                        .show();
// 
//                return true;
//            }
//        });
//        expListView.setOnItemClickListener(itemClickListener);
//        expListView.setOnItemLongClickListener(itemLongClickListener);
//        expListView.setOnTouchListener(gestureListener);
	}

	@Override
	protected void onResume(){
		super.onResume();

		taskSource.open();

		values = new ArrayList<TaskModel>(taskSource.getAllTasks().values());
		keys = new ArrayList<Integer>(taskSource.getAllTasks().keySet());

		BasicListAdapter adapter = new BasicListAdapter(this,R.layout.list_item, values, false);
		list.setAdapter(adapter);
		
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
			startActivity(intent);
		}
	};

	public OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			taskSource.close();

			Intent intent = new Intent(getApplicationContext(), MultipleSelectorActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return true;
		}
	};

	protected void OnListSwipeLeft(int x, int y){
		int position = list.pointToPosition(x, y);

		values.get(position).setStatus(TaskStatus.OPENED);
		taskSource.openTask(keys.get(position).toString());

		adapter = (BasicListAdapter) list.getAdapter();
		adapter.notifyDataSetChanged();		
	}

	protected void OnListSwipeRight(int x, int y){
		int position = list.pointToPosition(x, y);

		values.get(position).setStatus(TaskStatus.CLOSED);
		taskSource.closeTask(keys.get(position).toString());

		adapter = (BasicListAdapter) list.getAdapter();
		adapter.notifyDataSetChanged();
	}
	
    private void createGroupList() {
        groupList = new ArrayList<String>();
        //TODO
        //groupList = getCategories();
        //or
        //groupList = getAssignees();
        groupList.add("HP");
        groupList.add("Dell");
        groupList.add("Lenovo");
        groupList.add("Sony");
        groupList.add("HCL");
        groupList.add("Samsung");
    }
 
    private void createCollection() {
        // preparing laptops collection(child)
    	//TODO
    	//getTasks(byCategroy)
    	//getTasks(byAssignee)
        String[] hpModels = { "HP Pavilion G6-2014TX", "ProBook HP 4540",
                "HP Envy 4-1025TX" };
        String[] hclModels = { "HCL S2101", "HCL L2102", "HCL V2002" };
        String[] lenovoModels = { "IdeaPad Z Series", "Essential G Series",
                "ThinkPad X Series", "Ideapad Z Series" };
        String[] sonyModels = { "VAIO E Series", "VAIO Z Series",
                "VAIO S Series", "VAIO YB Series" };
        String[] dellModels = { "Inspiron", "Vostro", "XPS" };
        String[] samsungModels = { "NP Series", "Series 5", "SF Series" };
 
        laptopCollection = new LinkedHashMap<String, List<String>>();
 
        for (String laptop : groupList) {
            if (laptop.equals("HP")) {
                loadChild(hpModels);
            } else if (laptop.equals("Dell"))
                loadChild(dellModels);
            else if (laptop.equals("Sony"))
                loadChild(sonyModels);
            else if (laptop.equals("HCL"))
                loadChild(hclModels);
            else if (laptop.equals("Samsung"))
                loadChild(samsungModels);
            else
                loadChild(lenovoModels);
 
            laptopCollection.put(laptop, childList);
        }
    }
 
    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
            childList.add(model);
    }
}