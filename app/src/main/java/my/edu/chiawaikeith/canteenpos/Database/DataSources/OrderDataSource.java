package my.edu.chiawaikeith.canteenpos.Database.DataSources;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.edu.chiawaikeith.canteenpos.Database.Contracts.OrderCon.Order;
import my.edu.chiawaikeith.canteenpos.Database.SQLHelper;
import my.edu.chiawaikeith.canteenpos.Domains.Transactions;

/**
 * Created by win77 on 16/12/2015.
 */
public class OrderDataSource {
    private SQLiteDatabase database;
    private SQLHelper dbHelper;

    //public UserRecord userRecord;
    //private String[] allColumn = {
    //User.COLUMN_NAME,
    //User.COLUMN_PASSWORD
    //};
    public OrderDataSource(Context context) {
        dbHelper = new SQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

//    public int insertOrder(Transactions orderRecord){
//        ContentValues values = new ContentValues();
//        values.put(Order.COLUMN_NUM, orderRecord.getNumber());
//
//        database = dbHelper.getWritableDatabase();
//        long order_id = database.insert(Order.TABLE_NAME, null, values);
//        database.close();
//        return (int) order_id;
//    }

    public ArrayList<HashMap<String, String>> getAllOrders() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM itemOrder";

        ArrayList<HashMap<String, String>> getAllOrders = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> orderlist = new HashMap<>();
                orderlist.put("id", cursor.getString(cursor.getColumnIndex(String.valueOf(Order._id))));
                orderlist.put("number", cursor.getString(cursor.getColumnIndex(String.valueOf(Order.COLUMN_NUM))));
                getAllOrders.add(orderlist);
                //getAllOrders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return getAllOrders;
    }

    public Transactions getOrderById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery2 =  "SELECT * FROM itemOrder where id = ? ";
        //User._id + "," +
        //User.COLUMN_NAME + "," +
        //User.COLUMN_PASSWORD + "," +
        //User.COLUMN_GENDER + "," +
        //User.COLUMN_AGE + "," +
        //User.COLUMN_WEIGHT + "," +
        //User.COLUMN_HEIGHT + "," +
        // User.COLUMN_BMI + "," +
        // " FROM " + User.TABLE_NAME
        //+ " WHERE " +
        //User._id + "=?";// It's a good practice to use parameter ?, instead of concatenate string
        //String selectQuery2 = "SELECT * FROM user WHERE _id = " + User._id;

        int iCount =0;
        Transactions orderRecord = new Transactions();

        Cursor cursor = db.rawQuery(selectQuery2, new String[] { String.valueOf(Id) } );

//        if (cursor.moveToFirst()) {
//            do {
//                orderRecord.order_ID = cursor.getInt(cursor.getColumnIndex(String.valueOf(Order._id)));
//                orderRecord.number  = cursor.getInt(cursor.getColumnIndex(String.valueOf(Order.COLUMN_NUM)));
//                //eventRecord.time  = cursor.getString(cursor.getColumnIndex(Event.COLUMN_TIME));
//                //eventRecord.desc = cursor.getString(cursor.getColumnIndex(Event.COLUMN_DESC));
//
//            } while (cursor.moveToNext());
//        }

        cursor.close();
        db.close();
        return orderRecord;
    }


    public List<Transactions> getAllOrder(int Id) {
        List<Transactions> record = new ArrayList<Transactions>();
        String selectQuery3 =  "SELECT * FROM itemOrder where id = ? ";

        Cursor cursor = database.rawQuery(selectQuery3, new String[] { String.valueOf(Id) });
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                try {
//                    Transactions orders = new Transactions();
//                    orders.setOrder_ID(cursor.getInt(0));
//                    orders.setNumber(cursor.getInt(1));
//
//                    Log.d("Track", cursor.getString(10));
//                    record.add(orders);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            } while (cursor.moveToNext());
//        }
        cursor.close();
        return record;
    }


    public void deleteOrder(int order_id) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        database.delete(Order.TABLE_NAME, Order._id + "= ?", new String[] { String.valueOf(order_id) });
        database.close(); // Closing database connection
    }

}
