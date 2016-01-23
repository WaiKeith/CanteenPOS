package my.edu.chiawaikeith.canteenpos.Database.Contracts;

import android.provider.BaseColumns;

/**
 * Created by win77 on 15/12/2015.
 */
public class AccountContract {
    public AccountContract() {
    }

    public static class AccountRecord implements BaseColumns {
        public final static String ACCOUNT_TABLE = "account";
        public final static String COLUMN_ACC_ID = "acc_id";
        public final static String COLUMN_CUST_ID = "cust_id";
        public final static String COLUMN_USER_NAME = "user_name";
        public final static String COLUMN_ACC_PASSWORD = "acc_password";
        public final static String COLUMN_ACC_SECURITYCODE = "acc_security_code";
        public final static String COLUMN_PROFILE_IMAGE_PATH = "profile_image_path";
        public final static String COLUMN_ACC_BALANCE = "acc_balance";
        public final static String COLUMN_REGISTER_DATE = "register_date";


    }
}
