package nz.alex.letsdo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TaskSource {

	private SQLiteHelper dbHelper = null;
	private SQLiteDatabase database = null;
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()); 
	
	private static TaskSource Instance = null;
	
	public static TaskSource GetInstance(Context context){
		if (Instance == null)
			return (Instance = new TaskSource(context));
		return Instance;
	}
	
	private TaskSource(Context context) {
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
		values.put(TaskColumns.TITLE.name(), aTask.title);
		values.put(TaskColumns.CATEGORY.name(), aTask.category);
		values.put(TaskColumns.ASSIGNEE.name(), aTask.assignee);
		values.put(TaskColumns.DESCRIPTION.name(), aTask.description);
		values.put(TaskColumns.DATEDUE.name(), aTask.dateDue);
		values.put(TaskColumns.STATUS.name(), aTask.status.name());

		values.put(TaskColumns.DATECREATED.name(), dateFormat.format(new Date()));
		values.put(TaskColumns.DATEMODIFIED.name(), dateFormat.format(new Date()));

		database.insert(SQLiteHelper.TABLE_TASKS, null, values);
	}

	public void changeTask(String rowID, TaskModel aTask){
		ContentValues values = new ContentValues();
		values.put(TaskColumns.TITLE.name(), aTask.title);
		values.put(TaskColumns.CATEGORY.name(), aTask.category);
		values.put(TaskColumns.ASSIGNEE.name(), aTask.assignee);
		values.put(TaskColumns.DESCRIPTION.name(), aTask.description);
		values.put(TaskColumns.DATEDUE.name(), aTask.dateDue);
		values.put(TaskColumns.STATUS.name(), aTask.status.name());
		
		values.put(TaskColumns.DATEMODIFIED.name(), dateFormat.format(new Date()));
		
		String where = TaskColumns.ID.name() + " = " + rowID;
		
		database.update(SQLiteHelper.TABLE_TASKS, values, where, null);
	}

	public void deleteTask(TaskModel aTask){

	}

	public TaskModel findTask(String rowID) throws Exception{
		String where = TaskColumns.ID.name() + " = " + rowID;
		Cursor cursor = database.query(SQLiteHelper.TABLE_TASKS, dbHelper.allColumns,  where, null, null, null, null);
		if (cursor.getCount() <= 0)
			throw new Exception("Task not found!");
		cursor.moveToFirst();
		return new TaskModel(cursor.getString(TaskColumns.TITLE.ordinal()), 
				cursor.getString(TaskColumns.CATEGORY.ordinal()), 
				cursor.getString(TaskColumns.ASSIGNEE.ordinal()), 
				cursor.getString(TaskColumns.DESCRIPTION.ordinal()), 
				cursor.getString(TaskColumns.DATEDUE.ordinal()),
				TaskStatus.valueOf(cursor.getString(TaskColumns.STATUS.ordinal())));
	}
	
	public Hashtable<Integer, TaskModel> getTasksOrderedBy(String column){
		Hashtable<Integer, TaskModel> tasksTable = new Hashtable<Integer, TaskModel>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_TASKS, dbHelper.allColumns, null, null, null, null, column);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			tasksTable.put(cursor.getInt(0), cursorToTask(cursor));
			cursor.moveToNext();
		}

		cursor.close();
		return tasksTable;
	}
	
	public Hashtable<Integer, TaskModel> getAllTasks(){
		return getTasksOrderedBy(TaskColumns.DATEMODIFIED.name());	
	}
	
	private TaskModel cursorToTask(Cursor cursor){
		TaskModel task = new TaskModel(cursor.getString(TaskColumns.TITLE.ordinal()), 
				cursor.getString(TaskColumns.CATEGORY.ordinal()), 
				cursor.getString(TaskColumns.ASSIGNEE.ordinal()), 
				cursor.getString(TaskColumns.DESCRIPTION.ordinal()), 
				cursor.getString(TaskColumns.DATEDUE.ordinal()), 
				TaskStatus.valueOf(cursor.getString(TaskColumns.STATUS.ordinal())));
		return task;
	}

	public int getLen(){
		Cursor cursor = database.query(SQLiteHelper.TABLE_TASKS, dbHelper.allColumns, null, null, null, null, null);
		return cursor.getCount();
	}
}