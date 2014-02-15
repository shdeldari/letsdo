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
		values.put(dbHelper.COLUMN_TITLE, aTask.title);
		values.put(dbHelper.COLUMN_CATEGORY, aTask.category);
		values.put(dbHelper.COLUMN_ASSIGNEE, aTask.assignee);
		values.put(dbHelper.COLUMN_DESCRIPTION, aTask.description);
		values.put(dbHelper.COLUMN_DATEDUE, aTask.dateDue);

		values.put(dbHelper.COLUMN_DATECREATED, dateFormat.format(new Date()));
		values.put(dbHelper.COLUMN_DATEMODIFIED, dateFormat.format(new Date()));

		database.insert(SQLiteHelper.TABLE_TASKS, null, values);
	}

	public void changeTask(Integer rowID, TaskModel aTask){
		ContentValues values = new ContentValues();
		values.put(dbHelper.COLUMN_TITLE, aTask.title);
		values.put(dbHelper.COLUMN_CATEGORY, aTask.category);
		values.put(dbHelper.COLUMN_ASSIGNEE, aTask.assignee);
		values.put(dbHelper.COLUMN_DESCRIPTION, aTask.description);
		values.put(dbHelper.COLUMN_DATEDUE, aTask.dateDue);
		
		values.put(dbHelper.COLUMN_DATEMODIFIED, dateFormat.format(new Date()));
		
		String where = "_id=" + rowID;
		
		database.update(SQLiteHelper.TABLE_TASKS, values, where, null);
	}

	public void deleteTask(TaskModel aTask){

	}

	public TaskModel findTask(String rowID) throws Exception{
		String where = "_id = " + rowID;
		Cursor cursor = database.query(SQLiteHelper.TABLE_TASKS, dbHelper.allColumns,  where, null, null, null, null);
		if (cursor.getCount() <= 0)
			throw new Exception("Task not found!");
		cursor.moveToFirst();
		return new TaskModel(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
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
		return getTasksOrderedBy(dbHelper.COLUMN_DATEMODIFIED);	
	}
	
	private TaskModel cursorToTask(Cursor cursor){
		TaskModel task = new TaskModel(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
		return task;
	}
	
	public int getLen(){
		Cursor cursor = database.query(SQLiteHelper.TABLE_TASKS, dbHelper.allColumns, null, null, null, null, null);
		return cursor.getCount();
	}
}