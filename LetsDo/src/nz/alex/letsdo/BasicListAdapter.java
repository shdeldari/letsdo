package nz.alex.letsdo;	

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class BasicListAdapter extends ArrayAdapter<Task>{
	private LayoutInflater mInflater;
	List<Task> tasks;
	ArrayList <ViewHolder> views;
	protected String activityName;
	boolean selector;
	protected Context context;
	
	static class ViewHolder {    	
        TextView text;
        CheckBox chkbox;
    }

	public BasicListAdapter(Context context, int resource, List<Task> tasks, boolean selector) {
		super(context, resource, tasks);
		mInflater = LayoutInflater.from(context);
        this.tasks = tasks;
        activityName = context.getClass().getSimpleName();
        this.selector = selector;
        views = new ArrayList<BasicListAdapter.ViewHolder>();
        this.context = context;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
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
            holder.chkbox.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					System.out.println("position"+ position + "chkbox");
					Toast.makeText(context, position, Toast.LENGTH_LONG).show();
				}
			});
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.text.setText(tasks.get(position).toString());
        if (tasks.get(position).getStatus() == TaskStatus.CLOSED)
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
		return tasks.size();
	}

	@Override
	public Task getItem(int arg0) {
		return tasks.get(arg0);
	}

	public ArrayList<Integer> getSelected() {
		ArrayList<Integer> selected = new ArrayList<Integer>();
		for (int i = 0; i < tasks.size(); i++) {
			if(views.get(i).chkbox.isChecked()){
				selected.add(i);
				System.out.println("del"+tasks.get(i).toString());
			}
		}
		return selected;
	}
}
