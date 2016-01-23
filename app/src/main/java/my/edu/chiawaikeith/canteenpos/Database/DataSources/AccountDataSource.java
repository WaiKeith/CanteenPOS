package my.edu.chiawaikeith.canteenpos.Database.DataSources;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;

import my.edu.chiawaikeith.canteenpos.Database.Contracts.AccountContract.AccountRecord;
import my.edu.chiawaikeith.canteenpos.Database.SQLHelper;
import my.edu.chiawaikeith.canteenpos.Domains.OfflineLogin;

/**
 * Created by win77 on 15/12/2015.
 */
public class AccountDataSource {
    public SimpleDateFormat mySqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private SQLiteDatabase database;
    private SQLHelper dbHelper;

    public AccountDataSource(Context context) {
        dbHelper = new SQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private String[] allColumn = {
            AccountRecord.COLUMN_ACC_ID,
            AccountRecord.COLUMN_CUST_ID,
            AccountRecord.COLUMN_USER_NAME,
            AccountRecord.COLUMN_ACC_PASSWORD,
            AccountRecord.COLUMN_ACC_SECURITYCODE,
            AccountRecord.COLUMN_PROFILE_IMAGE_PATH,
            AccountRecord.COLUMN_ACC_BALANCE,
            AccountRecord.COLUMN_REGISTER_DATE
    };

    public void insertAccount(OfflineLogin offlineLogin) {

        ContentValues values = new ContentValues();
        values.put(AccountRecord.COLUMN_ACC_ID, offlineLogin.getAcc_id());
        values.put(AccountRecord.COLUMN_USER_NAME, offlineLogin.getUser_name());

        database = dbHelper.getWritableDatabase();
        database.insert(AccountRecord.ACCOUNT_TABLE, null, values);
        database.close();
    }

//    public ArrayList<HashMap<String, String>> getAllUsers() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        String selectQuery = "SELECT * FROM user";
//
//        ArrayList<HashMap<String, String>> getAllUsers = new ArrayList<>();
//
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                HashMap<String, String> user = new HashMap<>();
//                user.put("id", cursor.getString(cursor.getColumnIndex(User._id)));
//                user.put("name", cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)));
//                getAllUsers.add(user);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return getAllUsers;
//    }
//
//    public Accounts getUserDetails(String username, String password) {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        Accounts userRecord = new Accounts();
//        String selectQuery2 =  "SELECT  " +
//                User._id + "," +
//                User.COLUMN_NAME + "," +
//                User.COLUMN_PASSWORD +
////                User.COLUMN_GENDER + "," +
////                User.COLUMN_AGE + "," +
////                User.COLUMN_WEIGHT + "," +
////                User.COLUMN_HEIGHT + "," +
////                User.COLUMN_BMI  +
//
//                //User.COLUMN_BALANCE + "," +
//                //User.COLUMN_REGISTERDATE +
//                " FROM " + User.TABLE_NAME
//                + " WHERE " +
//                User.COLUMN_NAME + "=?" + " AND " + User.COLUMN_PASSWORD + "=?" + ";";
//        Cursor cursor = db.rawQuery(selectQuery2, new String[] {username,password});
//        if (cursor.moveToFirst()) {
//            do {
//                //userRecord.user_ID = cursor.getInt(cursor.getColumnIndex(User._id));
//                //userRecord.name = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME));
//                //userRecord.pw = cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD));
////                userRecord.gender = cursor.getString(cursor.getColumnIndex(User.COLUMN_GENDER));
////                userRecord.age = cursor.getString(cursor.getColumnIndex(User.COLUMN_AGE));
////                userRecord.weight = cursor.getString(cursor.getColumnIndex(User.COLUMN_WEIGHT));
////                userRecord.height = cursor.getString(cursor.getColumnIndex(User.COLUMN_HEIGHT));
////                userRecord.bmi = cursor.getString(cursor.getColumnIndex(User.COLUMN_BMI));
//                //userRecord.balance = cursor.getDouble(cursor.getColumnIndex(User.COLUMN_BALANCE));
//                //userRecord.registerDate = cursor.getString(cursor.getColumnIndex(User.COLUMN_REGISTERDATE));
//
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        db.close();
//        return userRecord;
//    }
//
//    public Accounts getUserDetailsByID(int id) {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        Accounts accRecord = new Accounts();
//        String selectQuery2 = "SELECT  " +
//                AccountRecord.COLUMN_ACC_ID + "," +
//                AccountRecord.COLUMN_CUST_ID + "," +
//                AccountRecord.COLUMN_USER_NAME + "," +
//                AccountRecord.COLUMN_ACC_BALANCE + "," +
//                AccountRecord.COLUMN_REGISTER_DATE +
////                User.COLUMN_WEIGHT + "," +
////                User.COLUMN_HEIGHT + "," +
////                User.COLUMN_BMI  +
//                //User.COLUMN_BALANCE + "," +
//                //User.COLUMN_REGISTERDATE +
//                " FROM " + AccountRecord.ACCOUNT_TABLE
//                + " WHERE " +
//                AccountRecord.COLUMN_ACC_ID + "=?" + ";";
//        Cursor cursor = db.rawQuery(selectQuery2, new String[]{String.valueOf(id)});
//        if (cursor.moveToFirst()) {
//            do {
//                accRecord.acc_id = cursor.getInt(cursor.getColumnIndex(AccountRecord.COLUMN_ACC_ID));
//                accRecord.cust_id = cursor.getString(cursor.getColumnIndex(AccountRecord.COLUMN_CUST_ID));
//                accRecord.acc_balance = cursor.getDouble(cursor.getColumnIndex(AccountRecord.COLUMN_ACC_BALANCE));
//                accRecord.register_date = cursor.getString(cursor.getColumnIndex(AccountRecord.COLUMN_REGISTER_DATE));
////                userRecord.age = cursor.getString(cursor.getColumnIndex(User.COLUMN_AGE));
////                userRecord.weight = cursor.getString(cursor.getColumnIndex(User.COLUMN_WEIGHT));
////                userRecord.height = cursor.getString(cursor.getColumnIndex(User.COLUMN_HEIGHT));
////                userRecord.bmi = cursor.getString(cursor.getColumnIndex(User.COLUMN_BMI));
//                //userRecord.balance = cursor.getDouble(cursor.getColumnIndex(User.COLUMN_BALANCE));
//                //userRecord.registerDate = cursor.getString(cursor.getColumnIndex(User.COLUMN_REGISTERDATE));
//
//            } while (cursor.moveToNext());
//
//    }
//
//        cursor.close();
//        db.close();
//        return accRecord;
//    }
//
//    public void delete(int user_id) {
//
//        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        // It's a good practice to use parameter ?, instead of concatenate string
//        database.delete(User.TABLE_NAME, User._id + "= ?", new String[]{String.valueOf(user_id)});
//        database.close(); // Closing database connection
//    }
//
//    public void update(Accounts userRecord) {
//
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        //values.put(User.COLUMN_NAME, userRecord.name);
//        //values.put(User.COLUMN_PASSWORD, userRecord.pw);
////        values.put(User.COLUMN_GENDER, userRecord.gender);
////        values.put(User.COLUMN_AGE, userRecord.age);
////        values.put(User.COLUMN_WEIGHT, userRecord.weight);
////        values.put(User.COLUMN_HEIGHT, userRecord.height);
//
//
//        // It's a good practice to use parameter ?, instead of concatenate string
//        //db.update(User.TABLE_NAME, values, User._id + "= ?", new String[]{String.valueOf(userRecord.user_ID)});
//        db.close(); // Closing database connection
//    }
}
