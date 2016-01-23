package my.edu.chiawaikeith.canteenpos.Database.Contracts;

import android.provider.BaseColumns;

/**
 * Created by win77 on 17/12/2015.
 */
public class EventCon {
    public EventCon(){}
    public static class Event implements BaseColumns {
    public static final String TABLE_NAME ="itemEvent";
    public static final String _id = "id";
    public static final String COLUMN_DATE ="date";
    public static final String COLUMN_TIME ="time";
    public static final String COLUMN_DESC ="desc";}
}
