package nz.alex.letsdo.tools;

import java.util.List;
import java.util.Map;

import nz.alex.letsdo.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	 
    private Activity context;
    private Map<String, List<String>> tasks;
    private List<String> groups;
 
    public ExpandableListAdapter(Activity context, List<String> groups,
            Map<String, List<String>> tasks) {
        this.context = context;
        this.tasks = tasks;
        this.groups = groups;
    }
 
    public Object getChild(int groupPosition, int childPosition) {
        return tasks.get(groups.get(groupPosition)).get(childPosition);
    }
 
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        final String task = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();
 
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }
 
        TextView item = (TextView) convertView.findViewById(R.id.task);
 
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
 
        item.setText(task);
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
}