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

		database.insert(SQLiteHelper.TABLE_TASKS, null, values);
	}

	public void deleteTask(TaskModel aTask){

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
