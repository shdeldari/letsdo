package nz.alex.letsdo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TaskSource {

	private SQLiteHelper dbHelper;
	private SQLiteDatabase database;

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()); 

	public TaskSource(Context context) {
		// TODO Auto-generated constructor stub
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void addTask(TaskModel aTask){
	    ContentValues values = new ContentValues();
	    values.put(dbHelper.COLUMN_TITLE, aTask.title);
	    values.put(dbHelper.COLUMN_CATEGORY, aTask.category);
	    values.put(dbHelper.COLUMN_ASSIGNEE, aTask.assignee);
	    values.put(dbHelper.COLUMN_DESCRIPTION, aTask.description);
	    values.put(dbHelper.COLUMN_DATEDUE, aTask.dateDue);
	   
	    values.put(dbHelper.COLUMN_DATECREATED, dateFormat.format(new Date()));
	    values.put(dbHelper.COLUMN_DATEMODIFIED, dateFormat.format(new Date()));
	    
	    database.insert(dbHelper.TABLE_TASKS, null, values);
	}

	public void deleteTask(TaskModel aTask){

	}

	public List<TaskModel> getTasksOrderedBy(String column){
		List<TaskModel> tasksList = new ArrayList<TaskModel>();

		// TODO Build the list of the tasks here

		return tasksList;
	}
}
