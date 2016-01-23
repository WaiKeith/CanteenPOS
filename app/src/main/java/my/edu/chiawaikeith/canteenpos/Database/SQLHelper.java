package my.edu.chiawaikeith.canteenpos.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import my.edu.chiawaikeith.canteenpos.Database.Contracts.AccountContract.AccountRecord;
import my.edu.chiawaikeith.canteenpos.Database.Contracts.EventCon.Event;
import my.edu.chiawaikeith.canteenpos.Database.Contracts.OrderCon.Order;

/**
 * Created by win77 on 13/12/2015.
 */
public class SQLHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "users.db";

    private static final String SQL_CREATE_ENTRIES =
    "CREATE TABLE "      +
        AccountRecord.ACCOUNT_TABLE      + "(" +
        AccountRecord.COLUMN_ACC_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        AccountRecord.COLUMN_CUST_ID     + " TEXT," +
        AccountRecord.COLUMN_ACC_PASSWORD + " TEXT," +
        AccountRecord.COLUMN_ACC_SECURITYCODE   + " TEXT," +
        AccountRecord.COLUMN_PROFILE_IMAGE_PATH      + " TEXT," +
        AccountRecord.COLUMN_ACC_BALANCE   + " TEXT," +
        AccountRecord.COLUMN_REGISTER_DATE   + " TEXT)";
//                    User.COLUMN_BMI      + " TEXT," +
//                    User.COLUMN_BALANCE + " TEXT," +
//                    User.COLUMN_REGISTERDATE + " TEXT)";

    private static final String SQL_CREATE_ENTRIES2 =
    "CREATE TABLE " + Event.TABLE_NAME + "(" + Event._id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
    Event.COLUMN_DATE + " TEXT," +
    Event.COLUMN_TIME + " TEXT, " +
    Event.COLUMN_DESC + " TEXT)";

    private static final String SQL_CREATE_ENTRIES4 =
            "CREATE TABLE " + Order.TABLE_NAME + "(" + Order._id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Order.COLUMN_NUM + " TEXT)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AccountRecord.ACCOUNT_TABLE;

    private static final String SQL_DELETE_ENTRIES2 =
            "DROP TABLE IF EXISTS " + Event.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES4 =
            "DROP TABLE IF EXISTS " + Order.TABLE_NAME;


    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES2);
        db.execSQL(SQL_CREATE_ENTRIES4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// This database is only a cache for online data, so its upgrade
// policy is to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES2);
        db.execSQL(SQL_DELETE_ENTRIES4);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}