package nz.alex.letsdo;

import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	private Context context;
	
	public final String TABLE_TASKS = "tasks";
	public final String COLUMN_ID = "_id";
	
	public final String COLUMN_TITLE = context.getString(R.string.taskTitle);
	public final String COLUMN_CATEGORY = context.getString(R.string.taskCategory);
	public final String COLUMN_ASSIGNEE = context.getString(R.string.taskAssignee);
	public final String COLUMN_DESCRIPTION = context.getString(R.string.taskDescription);
	public final String COLUMN_DATEDUE = context.getString(R.string.taskDateDue);

	public final String COLUMN_DATECREATED = context.getString(R.string.taskDateCreated);
	public final String COLUMN_DATEMODIFIED = context.getString(R.string.taskDateModified);
	
	private static final String DATABASE_NAME = "letsdotasks.db";
	private static final int DATABASE_VERSION = 1;
	
	public SQLiteHelper(Context aContext) {
		super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
		context = aContext;
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