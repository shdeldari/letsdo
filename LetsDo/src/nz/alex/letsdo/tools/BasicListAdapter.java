package nz.alex.letsdo.tools;

import java.util.List;
import nz.alex.letsdo.R;
import nz.alex.letsdo.TaskModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class BasicListAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	List<TaskModel> values;
	boolean selector;
	
	public BasicListAdapter(Context context, int resource, List<TaskModel> values, boolean selector) {
		super();
		mInflater = LayoutInflater.from(context);
        this.values = values;
        this.selector = selector;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("getView");
		// A ViewHolder keeps references to children views to avoid unneccessary calls
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
        if(selector) holder.chkbox.setVisibility(0);	else holder.chkbox.setVisibility(4);
        return convertView;
	}

	static class ViewHolder {    	
        TextView text;
        CheckBox chkbox;
    }

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Object getItem(int arg0) {
		return values.get(arg0).toString();
	}

	@Override
	public long getItemId(int arg0) {
		
		return 0;
	}
}
