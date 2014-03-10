package nz.alex.letsdo;

import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_TASKS = "tasks";
	private static final String DATABASE_NAME = "letsdotasks.db";
	private static final int DATABASE_VERSION = 1;
	
	public final String COLUMN_ID = "_id";
	public final String COLUMN_TITLE;
	public final String COLUMN_CATEGORY;
	public final String COLUMN_ASSIGNEE;
	public final String COLUMN_DESCRIPTION;
	public final String COLUMN_DATEDUE;
	public final String COLUMN_DATECREATED;
	public final String COLUMN_DATEMODIFIED;
	public final String COLUMN_STATUS;
	
	public String[] allColumns;

	public SQLiteHelper(Context aContext) {
		super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
		allColumns = new String[9];
		allColumns[0] = COLUMN_ID;
		allColumns[1] = COLUMN_TITLE = aContext.getString(R.string.taskTitle);
		allColumns[2] = COLUMN_CATEGORY = aContext.getString(R.string.taskCategory);
		allColumns[3] = COLUMN_ASSIGNEE = aContext.getString(R.string.taskAssignee);
		allColumns[4] = COLUMN_DESCRIPTION = aContext.getString(R.string.taskDescription);
		allColumns[5] = COLUMN_DATEDUE = aContext.getString(R.string.taskDateDue);
		allColumns[6] = COLUMN_DATECREATED = aContext.getString(R.string.taskDateCreated);
		allColumns[7] = COLUMN_DATEMODIFIED = aContext.getString(R.string.taskDateModified);
		allColumns[8] = COLUMN_STATUS = aContext.getString(R.string.taskStatus);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		// Database creation sql statement
		String DATABASE_CREATE = "create table " + TABLE_TASKS + "(" 
				+ COLUMN_ID	+ " integer primary key autoincrement, " 
				+ COLUMN_TITLE + " text not null, " 
				+ COLUMN_CATEGORY + " text not null, " 
				+ COLUMN_ASSIGNEE + " text not null, " 
				+ COLUMN_DESCRIPTION + " text, "
				+ COLUMN_DATEDUE + " text, "
				+ COLUMN_STATUS + " integer, "
				
				+ COLUMN_DATECREATED + " text not null, "
				+ COLUMN_DATEMODIFIED + " text not null);";

		database.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteHelper.class.getName(),
		"Upgrading database from version " + oldVersion + " to "
		+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
	    onCreate(db);
	}
}