package nz.alex.letsdo;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	 
    private Activity context;
    private Map<String, List<Task>> tasks;
    private List<String> groups;
    boolean selector = false;
    private String activityName;
    
 
    public ExpandableListAdapter(Activity context, List<String> groups, Map<String, List<Task>> laptopCollection) {
        this.context = context;
        this.tasks = laptopCollection;
        this.groups = groups;
        //this.selector = selector;
        activityName = context.getClass().getSimpleName();
    }
 
    public Object getChild(int groupPosition, int childPosition) {
    	//System.out.println("getchild - "+ groupPosition+":"+childPosition);
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

        CheckBox chk = (CheckBox) convertView.findViewById(R.id.chkBox);
        if(activityName.equals("MainActivity")) 
        	chk.setVisibility(View.INVISIBLE);	
        else {
        	chk.setVisibility(View.VISIBLE);
        }
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
        return convertView;
    }
 
    public boolean hasStableIds() {
        return true;
    }
 
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    public void multipleSelectableMode(){
    	
    }
}