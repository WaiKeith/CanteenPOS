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
import android.widget.CheckBox;
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
    final static String INSERT_URL = "http://dinpos.comlu.com/Accounts/Students/create_account.php";

    final static String KEY_ACCOUNT_ID = "acc_id";
    final static String KEY_CUSTOMER_ID = "cust_id";
    final static String KEY_USERNAME = "user_name";
    final static String KEY_PASSWORD = "acc_password";
    final static String KEY_SECURITY_CODE = "acc_security_code";
    final static String KEY_PROFILEIMAGE_PATH = "profile_image_path";
    final static String KEY_ACCOUNT_BALANCE = "acc_balance";
    final static String KEY_REGISTER_DATE = "register_date";

    Date registerDate;
    private String custID = "",username = "",password="";
    private Button btnRegister;

    private ArrayList<Accounts> accountItems;
    private int layout;
    private Context context;
    private Toolbar toolBar;
    private CheckBox checkTerm;
    private OfflineLogin offlineLogin;

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

        editCustId = (EditText) findViewById(R.id.custId);
        editUserName = (EditText) findViewById(R.id.editUsername);
        editPw = (EditText) findViewById(R.id.accPassword);
        checkTerm = (CheckBox)findViewById(R.id.checkbox_sign_up_terms);

        final Intent intent1 = new Intent(this, LoginActivity.class);
        btnRegister = (Button)findViewById(R.id.button_sign_up);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar;
                calendar = Calendar.getInstance();

                custID = editCustId.getText().toString();
                username = editUserName.getText().toString();
                password = editPw.getText().toString();
                registerDate = calendar.getTime();

                if (custID.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all required fields!", Toast.LENGTH_LONG).show();
                } else if (checkTerm.isChecked() == false){
                    Toast.makeText(getApplicationContext(), "Please agree to our term & conditions", Toast.LENGTH_LONG).show();
                } else {

                    new createAccount().execute(
                            editCustId.getText().toString(),
                            editUserName.getText().toString(),
                            editPw.getText().toString(),
                            registerDate.toString());

                    startActivity(intent1);

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

            data.put(KEY_CUSTOMER_ID, params[0]);
            data.put(KEY_USERNAME, params[1]);
            data.put(KEY_PASSWORD, params[2]);
            data.put(KEY_REGISTER_DATE, params[3]);

            return rh.sendPostRequest(INSERT_URL, data);
        }
    }

    public void reset(View view){
        EditText editText1 = (EditText) findViewById(R.id.editName);
        EditText editText2 = (EditText) findViewById(R.id.editPassword);

        editText1.setText(null);
        editText2.setText(null);
        checkTerm.setChecked(false);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
