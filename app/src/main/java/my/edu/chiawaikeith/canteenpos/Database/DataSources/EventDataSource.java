package my.edu.chiawaikeith.canteenpos.Database.DataSources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Database.SQLHelper;
import my.edu.chiawaikeith.canteenpos.Domains.Reminders;
import my.edu.chiawaikeith.canteenpos.Database.Contracts.EventCon.Event;

/**
 * Created by win77 on 17/12/2015.
 */
public class EventDataSource {
    private SQLiteDatabase database;
    private SQLHelper dbHelper;

    public EventDataSource(Context context) {
        dbHelper = new SQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

//    public int insertEvent(Reminders eventRecord){
//        ContentValues values = new ContentValues();
//        values.put(Event.COLUMN_DATE, eventRecord.date);
//        values.put(Event.COLUMN_TIME, eventRecord.time);
//        values.put(Event.COLUMN_DESC, eventRecord.desc);
//
//        database = dbHelper.getWritableDatabase();
//        long event_id = database.insert(Event.TABLE_NAME, null, values);
//        database.close();
//        return (int) event_id;
//    }

    public ArrayList<HashMap<String, String>> getAllEvents(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM itemEvent";

        ArrayList<HashMap<String, String>> getAllEvents = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> eventList = new HashMap<>();
                eventList.put("id", cursor.getString(cursor.getColumnIndex(String.valueOf(Event._id))));
                eventList.put("desc", cursor.getString(cursor.getColumnIndex(Event.COLUMN_DESC)));
                getAllEvents.add(eventList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return getAllEvents;
    }

    public Reminders getEventById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery2 =  "SELECT * FROM itemEvent where id = ? ";

        int iCount =0;
        Reminders eventRecord = new Reminders();

        Cursor cursor = db.rawQuery(selectQuery2, new String[] { String.valueOf(Id) } );

//        if (cursor.moveToFirst()) {
//            do {
//                eventRecord.event_ID = cursor.getInt(cursor.getColumnIndex(String.valueOf(Event._id)));
//                eventRecord.date  = cursor.getString(cursor.getColumnIndex(Event.COLUMN_DATE));
//                eventRecord.time  = cursor.getString(cursor.getColumnIndex(Event.COLUMN_TIME));
//                eventRecord.desc = cursor.getString(cursor.getColumnIndex(Event.COLUMN_DESC));
//
//            } while (cursor.moveToNext());
//        }

        cursor.close();
        db.close();
        return eventRecord;
    }

    public void deleteEvent(int event_id) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        database.delete(Event.TABLE_NAME, Event._id + "= ?", new String[] { String.valueOf(event_id) });
        database.close(); // Closing database connection
    }

    public void updateEvent(Reminders eventRecord) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

//        values.put(Event.COLUMN_DATE, eventRecord.date);
//        values.put(Event.COLUMN_TIME,eventRecord.time);
//        values.put(Event.COLUMN_DESC,eventRecord.desc);

        //db.update(Event.TABLE_NAME, values, Event._id + "= ?", new String[] { String.valueOf(eventRecord.event_ID) });
        db.close();
    }
}
