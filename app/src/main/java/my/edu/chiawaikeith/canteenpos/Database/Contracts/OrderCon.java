package my.edu.chiawaikeith.canteenpos.Database.Contracts;

import android.provider.BaseColumns;

/**
 * Created by win77 on 13/12/2015.
 */
public class OrderCon{
    public OrderCon(){}
    public static class Order implements BaseColumns {
        public static final String TABLE_NAME ="itemOrder";
        public static final String _id = "id";
        public static final String COLUMN_NUM ="number";
}}
