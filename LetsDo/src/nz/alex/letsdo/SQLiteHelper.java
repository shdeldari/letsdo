package nz.alex.letsdo;

import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

enum TaskColumns{
	ID, TITLE, CATEGORY, ASSIGNEE, DESCRIPTION, DATEDUE, DATECREATED, DATEMODIFIED, STATUS;
}

public class SQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_TASKS = "tasks";
	private static final String DATABASE_NAME = "letsdotasks.db";
	private static final int DATABASE_VERSION = 1;

	public String[] allColumns;

	public SQLiteHelper(Context aContext) {
		super(aContext, DATABASE_NAME, null, DATABASE_VERSION);

		allColumns = new String[TaskColumns.values().length];

		for (TaskColumns column: TaskColumns.values()){
			allColumns[column.ordinal()] = column.name();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// Database creation sql statement
		String DATABASE_CREATE = "create table " + TABLE_TASKS + "(";
		for (TaskColumns column: TaskColumns.values()){
			DATABASE_CREATE += column.name();
			switch (column){
			case ID:
				DATABASE_CREATE += " integer primary key autoincrement";
				break;
			case DESCRIPTION:
			case DATEDUE:
				DATABASE_CREATE += " text";
				break;
			default:
				DATABASE_CREATE += " text not null";	
			}
			if (column.ordinal() < TaskColumns.values().length - 1)
				DATABASE_CREATE += ", ";
		}
		
		DATABASE_CREATE += ");";

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