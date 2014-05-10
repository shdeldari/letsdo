package nz.alex.letsdo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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
	
	private TaskSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	private Task cursorToTask(Cursor cursor){
		TaskModel model = new TaskModel(cursor.getString(TaskColumns.TITLE.ordinal()), 
				cursor.getString(TaskColumns.CATEGORY.ordinal()), 
				cursor.getString(TaskColumns.ASSIGNEE.ordinal()), 
				cursor.getString(TaskColumns.DESCRIPTION.ordinal()), 
				cursor.getString(TaskColumns.DATEDUE.ordinal()), 
				TaskStatus.valueOf(cursor.getString(TaskColumns.STATUS.ordinal())));
		Task task = new Task(cursor.getInt(TaskColumns.ID.ordinal()), model);
		return task;
	}

	private Set<String> getAllCategories(){
		Set<String> columnSet = new HashSet<String>();
		for (Task task: getAllTasks())
			columnSet.add(task.getCategory());
		return columnSet;	
	}
	
	private Set<String> getAllAssignees(){
		Set<String> columnSet = new HashSet<String>();
		for (Task task: getAllTasks())
			columnSet.add(task.getAssignee());
		return columnSet;	
	}

	public static TaskSource GetInstance(Context context){
		if (Instance == null)
			return (Instance = new TaskSource(context));
		return Instance;
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

	public void changeTask(int taskId, TaskModel aTask){
		ContentValues values = new ContentValues();
		values.put(TaskColumns.TITLE.name(), aTask.title);
		values.put(TaskColumns.CATEGORY.name(), aTask.category);
		values.put(TaskColumns.ASSIGNEE.name(), aTask.assignee);
		values.put(TaskColumns.DESCRIPTION.name(), aTask.description);
		values.put(TaskColumns.DATEDUE.name(), aTask.dateDue);
		values.put(TaskColumns.STATUS.name(), aTask.status.name());
		
		values.put(TaskColumns.DATEMODIFIED.name(), dateFormat.format(new Date()));
		
		String where = TaskColumns.ID.name() + " = " + Integer.toString(taskId);
		
		database.update(SQLiteHelper.TABLE_TASKS, values, where, null);
	}

	public void closeTask(int taskId){
		ContentValues values = new ContentValues();
		values.put(TaskColumns.STATUS.name(), TaskStatus.CLOSED.name());
		
		values.put(TaskColumns.DATEMODIFIED.name(), dateFormat.format(new Date()));
		
		String where = TaskColumns.ID.name() + " = " + Integer.toString(taskId);
		
		database.update(SQLiteHelper.TABLE_TASKS, values, where, null);
	}

	public void openTask(int taskId){
		ContentValues values = new ContentValues();
		values.put(TaskColumns.STATUS.name(), TaskStatus.OPENED.name());
		
		values.put(TaskColumns.DATEMODIFIED.name(), dateFormat.format(new Date()));

		String where = TaskColumns.ID.name() + " = " + Integer.toString(taskId);
		
		database.update(SQLiteHelper.TABLE_TASKS, values, where, null);
	}

	public void deleteTask(int taskId){
		String where = TaskColumns.ID.name() + " = " + Integer.toString(taskId);
		database.delete(SQLiteHelper.TABLE_TASKS, where, null);
	}

	public void deleteTasks(ArrayList<Integer> taskIdList){
		for (int taskId: taskIdList)
			deleteTask(taskId);
	}

	public TaskModel findTask(int taskId) throws Exception{
		String where = TaskColumns.ID.name() + " = " + Integer.toString(taskId);
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
	
	public ArrayList<Task> getTasksOrderedBy(String column){
		ArrayList<Task> tasksArray = new ArrayList<Task>();
		Cursor cursor = database.query(SQLiteHelper.TABLE_TASKS, dbHelper.allColumns, null, null, null, null, column +" DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			tasksArray.add(cursorToTask(cursor));
			cursor.moveToNext();
		}

		cursor.close();
		return tasksArray;
	}

	public ArrayList<Task> getAllTasks(){
		return getTasksOrderedBy(TaskColumns.DATECREATED.name());	
	}

	public List<String> getCategoryList(){
		Set<String> columnSet = getAllCategories();
		List<String> columnList = new ArrayList<String>();
		for (String category: columnSet)
			columnList.add(category);
		return columnList;	
	}

	public List<String> getAssigneeList(){
		Set<String> columnSet = getAllAssignees();
		List<String> columnList = new ArrayList<String>();
		for (String assignee: columnSet)
			columnList.add(assignee);
		return columnList;	
	}

	public int getLen(){
		Cursor cursor = database.query(SQLiteHelper.TABLE_TASKS, dbHelper.allColumns, null, null, null, null, null);
		return cursor.getCount();
	}
}