package nz.alex.letsdo;	

import java.util.ArrayList;
import java.util.List;

import nz.alex.letsdo.R;
import nz.alex.letsdo.TaskModel;
import nz.alex.letsdo.TaskStatus;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class BasicListAdapter extends ArrayAdapter<TaskModel>{
	private LayoutInflater mInflater;
	List<TaskModel> values;
	ArrayList <ViewHolder> views;
	protected String activityName;
	boolean selector;
	
	static class ViewHolder {    	
        TextView text;
        CheckBox chkbox;
    }

	public BasicListAdapter(Context context, int resource, List<TaskModel> values, boolean selector) {
		super(context, resource, values);
		mInflater = LayoutInflater.from(context);
        this.values = values;
        activityName = context.getClass().getSimpleName();
        this.selector = selector;
        views = new ArrayList<BasicListAdapter.ViewHolder>();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
		ViewHolder holder = new ViewHolder();

		// When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder.text = (TextView) convertView.findViewById(R.id.taskTxt);
            holder.chkbox = (CheckBox) convertView.findViewById(R.id.chkBox);
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.text.setText(values.get(position).toString());
        if (values.get(position).getStatus() == TaskStatus.CLOSED)
        	holder.text.setPaintFlags(holder.text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
        	holder.text.setPaintFlags(holder.text.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        
        if(activityName.equals("MainActivity")) 
        	holder.chkbox.setVisibility(View.INVISIBLE);	
        else {
        	holder.chkbox.setVisibility(View.VISIBLE);
        	if(selector)
        		holder.chkbox.setChecked(true);
        }
        views.add(holder);
        convertView.setTag(holder);
        return convertView;
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public TaskModel getItem(int arg0) {
		return values.get(arg0);
	}

	public ArrayList<Integer> getSelected() {
		ArrayList<Integer> selected = new ArrayList<Integer>();
		for (int i = 0; i < values.size(); i++) {
			if(views.get(i).chkbox.isChecked())
				selected.add(i);
		}
		return selected;
	}
}
