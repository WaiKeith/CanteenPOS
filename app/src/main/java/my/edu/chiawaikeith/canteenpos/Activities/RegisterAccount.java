package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Domains.Accounts;
import my.edu.chiawaikeith.canteenpos.Domains.OfflineLogin;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;


public class RegisterAccount extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    final static String INSERT_URL = "http://canteenpos.comxa.com/Accounts/Students/create_account.php";

    final static String KEY_ACCOUNT_ID = "acc_id";
    final static String KEY_CUSTOMER_ID = "cust_id";
    final static String KEY_USERNAME = "user_name";
    final static String KEY_PASSWORD = "acc_password";
    final static String KEY_SECURITY_CODE = "acc_security_code";
    final static String KEY_PROFILEIMAGE_PATH = "profile_image_path";
    final static String KEY_ACCOUNT_BALANCE = "acc_balance";
    final static String KEY_REGISTER_DATE = "register_date";

    Date registerDate;
    private String custID = "";
    private String username = "";
    private String password="";
    Button btnRegister;

    private ArrayList<Accounts> accountItems;
    private int layout;
    private Context context;
    Toolbar toolBar;
    private OfflineLogin offlineLogin;

//    private JSONArray mJsonArray;
//    private ArrayList<Accounts> accountList = new ArrayList<>();

    EditText editCustId;
    EditText editUserName,editPw;

    public RegisterAccount() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolBar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.title_register_account);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        try {
//            // Create the Mobile Service Client instance, using the provided
//            // Mobile Service URL and key
//            mClient = new MobileServiceClient(
//                    "https://androidcanteenpos.azure-mobile.net/",
//                    "hoOMoroPlSmQBPMzdUODawrWbJqmVP19",
//                    this).withFilter(new ProgressFilter());
//
//            //Get the Mobile Service Table instance to use
//            accTable = mClient.getTable(Accounts.class);
//        } catch (MalformedURLException e) {
//            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
//        }


        editCustId = (EditText) findViewById(R.id.custId);
        editUserName = (EditText) findViewById(R.id.editUsername);
        editPw = (EditText) findViewById(R.id.accPassword);
        //editCode = (EditText)findViewById(R.id.editSecurityCode);
        //editImage = (EditText)findViewById(R.id.editImagePath);
        //editbalance = (EditText)findViewById(R.id.editBalance);
        //editdate = (EditText)findViewById(R.id.editDate);

        final Intent intent1 = new Intent(this, LoginActivity.class);
        btnRegister = (Button)findViewById(R.id.button_sign_up);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar;
                calendar = Calendar.getInstance();
                //accID = editAccId.getText().toString();
                custID = editCustId.getText().toString();
                username = editUserName.getText().toString();
                password = editPw.getText().toString();
                registerDate = calendar.getTime();
//                code = editCode.getText().toString();
//                path = editImage.getText().toString();
//                balance = editbalance.getText().toString();
//                date = editdate.getText().toString();

                if (custID.isEmpty() || username.isEmpty() || password.isEmpty() ) {
                    Toast.makeText(getApplicationContext(), "Please fill in all required fields!", Toast.LENGTH_LONG).show();
                } else {

//                    final Accounts userRecord = new Accounts();
//
//                    userRecord.setCustId(name);
//                    userRecord.setaPw(password);
//                    userRecord.setmComplete(false);
//                    accTable.insert(userRecord);
                    new createAccount().execute(
                            //editAccId.getText().toString(),
                            editCustId.getText().toString(),
                            editUserName.getText().toString(),
                            editPw.getText().toString(),
                            registerDate.toString());
//                            editCode.getText().toString(),
//                            editImage.getText().toString(),
//                            editbalance.getText().toString(),
//                            editdate.getText().toString());


                    startActivity(intent1);

                }
            }
        });


        //EditText editText1 = (EditText) findViewById(R.id.editName);
        //EditText editText2 = (EditText) findViewById(R.id.editPassword);
        //mProgressBar = (ProgressBar)findViewById(R.id.loadingProgressBar);
        //accountItems = new ArrayList<Accounts>();

        //refreshItemsFromTable();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class createAccount extends AsyncTask<String, Void, String> {

        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(RegisterAccount.this, "Uploading...", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<>();

            //data.put(KEY_ACCOUNT_ID, params[0]);
            data.put(KEY_CUSTOMER_ID, params[0]);
            data.put(KEY_USERNAME, params[1]);
            data.put(KEY_PASSWORD, params[2]);
            //data.put(KEY_SECURITY_CODE, params[4]);
            //data.put(KEY_PROFILEIMAGE_PATH, params[5]);
            //data.put(KEY_ACCOUNT_BALANCE, params[6]);
            data.put(KEY_REGISTER_DATE, params[3]);
            //data.put(KEY_LOGIN_ID, params[0]);

            return rh.sendPostRequest(INSERT_URL, data);
        }
    }

    public void reset(View view){
        EditText editText1 = (EditText) findViewById(R.id.editName);
        EditText editText2 = (EditText) findViewById(R.id.editPassword);
        //EditText editText3 = (EditText) findViewById(R.id.editPW);

        editText1.setText(null);
        editText2.setText(null);
        //editText3.setText(null);
    }

//    public void saveLoginDetail(OfflineLogin offlineLogin) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        SharedPreferences.Editor prefsEditor = prefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(offlineLogin);
//        prefsEditor.putString(OBJECT_OFFLINE_LOGIN, json);
//        prefsEditor.commit();
//    }

//    private void refreshItemsFromTable() {
//
////		TODO Uncomment the the following code when using a mobile service
////		// Get the items that weren't marked as completed and add them in the adapter
//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//                    final MobileServiceList<Accounts> result = accTable.where().field("complete").eq(false).execute().get();
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            //userAdapter.clear();
//
//                            for (Accounts item : result) {
//                                //userAdapter.add(item);
//                            }
//                        }
//                    });
//                } catch (Exception exception) {
//                    createAndShowDialog(exception, "Error");
//                }
//                return null;
//            }
//        }.execute();
//    }
//
//    private void createAndShowDialog(Exception exception, String title) {
//        createAndShowDialog(exception.toString(), title);
//    }

    @Override
    public void onClick(View v) {

    }


//    private class ProgressFilter implements ServiceFilter {
//
//        @Override
//        public ListenableFuture<ServiceFilterResponse> handleRequest(
//                ServiceFilterRequest request, NextServiceFilterCallback next) {
//
//            runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
//                }
//            });
//
//            SettableFuture<ServiceFilterResponse> result = SettableFuture.create();
//            try {
//                ServiceFilterResponse response = next.onNext(request).get();
//                result.set(response);
//            } catch (Exception exc) {
//                result.setException(exc);
//            }
//
//            dismissProgressBar();
//            return result;
//        }
//
//        private void dismissProgressBar() {
//            runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
//                }
//            });
//        }
//    }

//    private void createAndShowDialog(String message, String title) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setMessage(message);
//        builder.setTitle(title);
//        builder.create().show();
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
