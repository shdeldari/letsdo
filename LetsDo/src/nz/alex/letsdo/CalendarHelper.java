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

	private static CalendarHelper _instance = null;

	private static final String[] EVENT_PROJECTION = new String[] {
		Calendars._ID,                           // 0
		Calendars.ACCOUNT_NAME,                  // 1
		Calendars.CALENDAR_DISPLAY_NAME,         // 2
		Calendars.OWNER_ACCOUNT                  // 3
	};

	private static final int PROJECTION_ID_INDEX = 0;
	//	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	//	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	//	private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

	public static CalendarHelper getInstance(Context context){
		if (_instance == null)
			return (_instance = new CalendarHelper(context));
		return _instance; 
	}

	public int queryCalendars(){
		int calendarIndex = -1;

		Uri uri = Calendars.CONTENT_URI;   
		cursor = contentResolver.query(uri, EVENT_PROJECTION, null, null, null);
		
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
	}

	public void SetCalendarId(long calId){
		calendarId = calId;
	}

	public boolean UploadTasks(ArrayList<Task> tasks){
		if (calendarId != -1){
			for (Task task: tasks){
				Calendar beginTime = Calendar.getInstance();
				beginTime.set(2025, 6, 24, 13, 0);
				long startMillis = beginTime.getTimeInMillis();
				Calendar endTime = Calendar.getInstance();
				endTime.set(2025, 6, 24, 14, 0);
				long endMillis = endTime.getTimeInMillis();

				ContentValues values = new ContentValues();
				values.put(Events.DTSTART, startMillis);
				values.put(Events.DTEND, endMillis);
				values.put(Events.TITLE, task.toString());
				Interpreter interpreter = new Interpreter();
				values.put(Events.DESCRIPTION, interpreter.compile(task));
				
				values.put(Events.CALENDAR_ID, calendarId);
				String timeZone = TimeZone.getDefault().getID();
				values.put(Events.EVENT_TIMEZONE, timeZone);
				values.put(Events.AVAILABILITY, Events.AVAILABILITY_FREE);
				//values.put(Events.RRULE, "FREQ=WEEKLY;WKST=SU;BYDAY=SU");				
				Uri uri = contentResolver.insert(Events.CONTENT_URI, values);
				// get the event ID that is the last element in the Uri
				long eventID = Long.parseLong(uri.getLastPathSegment());
			}
			return true;
		}
		return false;
	}
	
}