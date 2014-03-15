package nz.alex.letsdo;

import java.util.ArrayList;
import java.util.List;
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
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	protected TaskSource taskSource;
	protected Context context;

	protected List<TaskModel> values;
	protected List<Integer> keys;

	public final static String EXTRA_MESSAGE = "nz.alex.letsdo.MESSAGE";
	protected ListView list;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				String toast;
				// right to left swipe
				int position = list.pointToPosition((int)e1.getX(), (int) e1.getY());
				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					toast = "Left Swipe: " + position;
					Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
					taskSource.openTask(keys.get(position).toString());
				}  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					toast = "Right Swipe: " + position;
					Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
					taskSource.closeTask(keys.get(position).toString());
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		// use the SimpleCursorAdapter to show the
		// elements in a ListView
		list = (ListView)findViewById(R.id.listView1);
		BasicListAdapter adapter = new BasicListAdapter(this, R.layout.list_item, values);
		list.setAdapter(adapter);
		//    	list.setOnItemClickListener(itemClickListener);
		list.setOnItemLongClickListener(itemLongClickListener);
		list.setOnTouchListener(gestureListener);
	}

	@Override
	protected void onResume(){
		super.onResume();

		taskSource.open();

		values = new ArrayList<TaskModel>(taskSource.getAllTasks().values());
		keys = new ArrayList<Integer>(taskSource.getAllTasks().keySet());

		// use the SimpleCursorAdapter to show the
		// elements in a ListView
		BasicListAdapter adapter = new BasicListAdapter(this,R.layout.list_item, values);
		list.setAdapter(adapter);
		//		list.refreshDrawableState();
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

			//			taskSource.deleteTask(keys.get(position).toString());
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
}