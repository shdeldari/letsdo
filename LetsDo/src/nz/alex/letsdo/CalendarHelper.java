package nz.alex.letsdo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;

public class CalendarHelper {

	private long calendarId = -1;

	private ContentResolver contentResolver = null;

	private Cursor cursor = null;

	Interpreter interpreter = new Interpreter();
	
	private static CalendarHelper _instance = null;

	private static String APP_TAG = "LetsDo";

	private static final String[] CALENDARS_PROJECTION = new String[] {
		Calendars._ID,                           // 0
		Calendars.CALENDAR_DISPLAY_NAME,         // 2
	};

	private static final int PROJECTION_ID_INDEX = 0;

	private static final String[] EVENTS_PROJECTION = new String[] {
		Events.TITLE,			// 0
		Events.DESCRIPTION		// 1
	};
	
	private static final int PROJECTION_TITLE_INDEX = 0;
	private static final int PROJECTION_DESCRIPTION_INDEX = 1;

	long startMillis, endMillis;
	
	public static CalendarHelper getInstance(Context context){
		if (_instance == null)
			return (_instance = new CalendarHelper(context));
		return _instance; 
	}

	public int queryCalendars(){
		int calendarIndex = -1;

		Uri uri = Calendars.CONTENT_URI;   
		cursor = contentResolver.query(uri, CALENDARS_PROJECTION, null, null, null);
		
		if (calendarId != -1){
			while(cursor.moveToNext()){
				calendarIndex++;
				if (cursor.getLong(PROJECTION_ID_INDEX) == calendarId){
					cursor.moveToFirst();
					break;
				}
			}
		}

		return calendarIndex;
	}

	public Cursor getCursor(){
		return cursor;
	}
	
	public long getCalendarIdAtPosition(int position){
		cursor.moveToPosition(position);
		return cursor.getLong(PROJECTION_ID_INDEX);
	}

	private CalendarHelper(Context context) {
		contentResolver = context.getContentResolver();
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(2025, 6, 24, 13, 0);
		startMillis = beginTime.getTimeInMillis();
		Calendar endTime = Calendar.getInstance();
		endTime.set(2025, 6, 24, 14, 0);
		endMillis = endTime.getTimeInMillis();
	}

	public void SetCalendarId(long calId){
		calendarId = calId;
	}

	public boolean UploadTasks(ArrayList<Task> tasks){
		if (calendarId != -1){
			for (Task task: tasks){
				ContentValues values = new ContentValues();
				values.put(Events.CALENDAR_ID, calendarId);
				
				String timeZone = TimeZone.getDefault().getID();
				values.put(Events.EVENT_TIMEZONE, timeZone);
				values.put(Events.DTSTART, startMillis);
				values.put(Events.DTEND, endMillis);
				
				values.put(Events.TITLE, task.toString());
				
				values.put(Events.EVENT_LOCATION, APP_TAG);				
				
				values.put(Events.AVAILABILITY, Events.AVAILABILITY_FREE);
				
				values.put(Events.DESCRIPTION, interpreter.compile(task));
				
				contentResolver.insert(Events.CONTENT_URI, values);
			}
			return true;
		}
		return false;
	}
	
	public ArrayList<TaskModel> downloadTasks(){
		if (calendarId != -1){
			String selection = "((" + Events.DTSTART + " = ?) AND (" 
                    + Events.DTEND + " = ?) AND ("
                    + Events.EVENT_LOCATION + " = ?))";
			
			String[] selectionArgs = new String[] {String.valueOf(startMillis), String.valueOf(endMillis), APP_TAG};
			
			cursor = contentResolver.query(Events.CONTENT_URI, EVENTS_PROJECTION, selection, selectionArgs, "ASC");
			
			ArrayList<TaskModel> taskModels = new ArrayList<TaskModel>();
			while (cursor.moveToNext()){
				TaskModel taskModel = interpreter.deCompile(cursor.getString(PROJECTION_DESCRIPTION_INDEX));
				taskModel.title = cursor.getString(PROJECTION_TITLE_INDEX);
				taskModels.add(taskModel);
			}
			return taskModels;
		}
		return null;
	}
}