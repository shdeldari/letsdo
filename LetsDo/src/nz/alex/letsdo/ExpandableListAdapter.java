package nz.alex.letsdo;

import java.util.List;
import java.util.Map;

import nz.alex.letsdo.MainActivity.GroupMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity context;
	private Map<String, List<Task>> tasks;
	private List<String> groups;
	boolean selector = false;
	private TaskSource taskSource;
	private GroupMode mode;

	public ExpandableListAdapter(Activity context, List<String> groups, Map<String, List<Task>> tasks, TaskSource taskSource) {
		this.context = context;
		this.tasks = tasks;
		this.groups = groups;
		this.taskSource = taskSource;
	}
	public ExpandableListAdapter(Activity context, GroupMode mode, TaskSource taskSource) {
		this.context = context;
		this.taskSource = taskSource;
		this.mode = mode;
		swapDataset(mode);
	}

	public Object getChild(int groupPosition, int childPosition) {
		return tasks.get(groups.get(groupPosition)).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final Task task = (Task) getChild(groupPosition, childPosition);
		//System.out.println("getchildView-"+groupPosition+":"+childPosition+"-"+task.toString() );
		LayoutInflater inflater = context.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.child_item, null);
		}

		TextView item = (TextView) convertView.findViewById(R.id.task);
		if (task.getStatus() == TaskStatus.CLOSED)
			item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		else
			item.setPaintFlags(item.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

		ImageButton chk = (ImageButton) convertView.findViewById(R.id.chkBox);
		chk.setOnClickListener(new CustomClickListener(groupPosition, childPosition));

		//        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
		//        delete.setOnClickListener(new OnClickListener() {
		// 
		//            public void onClick(View v) {
		//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
		//                builder.setMessage("Do you want to remove?");
		//                builder.setCancelable(false);
		//                builder.setPositiveButton("Yes",
		//                        new DialogInterface.OnClickListener() {
		//                            public void onClick(DialogInterface dialog, int id) {
		//                                List<String> child =
		//                                    tasks.get(groups.get(groupPosition));
		//                                child.remove(childPosition);
		//                                notifyDataSetChanged();
		//                            }
		//                        });
		//                builder.setNegativeButton("No",
		//                        new DialogInterface.OnClickListener() {
		//                            public void onClick(DialogInterface dialog, int id) {
		//                                dialog.cancel();
		//                            }
		//                        });
		//                AlertDialog alertDialog = builder.create();
		//                alertDialog.show();
		//            }
		//        });

		item.setText(task.toString());
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return tasks.get(groups.get(groupPosition)).size();
	}

	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String groupName = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.group_item,
					null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.group);
		item.setTypeface(null, Typeface.BOLD);
		item.setText(groupName);

		ImageButton chk = (ImageButton) convertView.findViewById(R.id.chkBox);
		chk.setOnClickListener(new CustomClickListener(groupPosition, -1));

		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void swapDataset(List<String> groups, Map<String, List<Task>> tasks){
		this.groups = groups;
		this.tasks = tasks;
		notifyDataSetChanged();
	}
	
	public void swapDataset(GroupMode mode){
		if(mode == GroupMode.GROUPED_BY_ASSIGNEE)
			this.groups = TaskSource.GetInstance(context).getAssigneeList();
		else
			this.groups = TaskSource.GetInstance(context).getCategoryList();
		this.tasks = TaskSource.GetInstance(context).getGroupedTasks(mode);
		notifyDataSetChanged();
	}

	public class CustomClickListener implements OnClickListener {
		private int groupPosition, childPosition;
		private boolean isChild;
		public CustomClickListener(int groupPosition, int childPosition){
			this.groupPosition = groupPosition;
			this.childPosition = childPosition;
			if(childPosition>=0)
				isChild=true;
			else 
				isChild=false;	
		}

		public void onClick(View v) { 
			Toast.makeText(context, "delete task!", Toast.LENGTH_SHORT).show();
			if(isChild){
				taskSource.deleteTask(((Task)getChild(groupPosition, childPosition)).getId());
			}
			else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Delete").
				setMessage("Are you sure to delete group ("+ groups.get(groupPosition) +")?").
				setCancelable(false).
				setPositiveButton("YES",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						int groupSize = getChildrenCount(groupPosition);
						for (int i = 0; i < groupSize; i++) {
							taskSource.deleteTask(((Task)getChild(groupPosition, i)).getId());
						}
						dialog.cancel();
					}
				})
				.setNegativeButton("NO,NO!",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();

			}
			swapDataset(mode);
//			notifyDataSetChanged();
		}
	}
}