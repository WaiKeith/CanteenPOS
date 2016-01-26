package my.edu.chiawaikeith.canteenpos.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Database.Contracts.AccountContract.AccountRecord;
import my.edu.chiawaikeith.canteenpos.Domains.OfflineLogin;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;


public class LoginActivity extends BaseActivity{

    Button btnLogin;
    EditText editTextUsername;
    EditText editTextPassword;
    private String userName = "";
    private String password = "";
    ImageView a;

    private static final String LOGIN_URL = "http://canteenpos.comxa.com/Accounts/Students/login_account.php";
    final static String KEY_USERNAME = "user_name";
    final static String KEY_PASSWORD = "acc_password";
    private static final String KEY_URL = "Url";

    public SimpleDateFormat mySqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private OfflineLogin offlineLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        btnLogin = (Button) findViewById(R.id.button_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();

                if (userName.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all required fields!", Toast.LENGTH_LONG).show();
                } else {
                    new Login(userName, password).execute();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void register(View view) {
        Intent intent = new Intent(this, RegisterAccount.class);
        startActivity(intent);

    }

    public void forgotPassword(View view)
    {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    public class Login extends AsyncTask<String, Void, String> {
        String user_name, acc_password;
        RequestHandler rh = new RequestHandler();

        public Login(String username, String password) {
            this.user_name = username;
            this.acc_password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            shortToast(LoginActivity.this, json);
            extractJsonData(json);

           switch (offlineLogin.getResponse()){
                case RESPONSE_404:
                    // account not found
                    shortToast(LoginActivity.this,"Account not found!");
                    break;

                case RESPONSE_SUCCESS:
                    // login success, save login state and direct to main screen
                    shortToast(LoginActivity.this,"Login success!");
                    saveLoginDetail(offlineLogin,LoginActivity.this);

                    // direct to main screen and finish current activity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                    break;
                case RESPONSE_PASSWORD_INCORRECT:
                    // password incorrect
                    shortToast(LoginActivity.this,"Password or username incorrect!");
                    break;

            }
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put(KEY_USERNAME, user_name);
            data.put(KEY_PASSWORD, acc_password);
            return rh.sendPostRequest(LOGIN_URL, data);
        }
    }

    private void extractJsonData(String json) {


        try {

            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            offlineLogin = new OfflineLogin();


            // These fields may not null
            offlineLogin.setAcc_id(jsonObject.getInt(AccountRecord.COLUMN_ACC_ID));
            offlineLogin.setCust_id(jsonObject.getString(AccountRecord.COLUMN_CUST_ID));
            offlineLogin.setUser_name(jsonObject.getString(AccountRecord.COLUMN_USER_NAME));
            offlineLogin.setAcc_password(jsonObject.getString(AccountRecord.COLUMN_ACC_PASSWORD));
            offlineLogin.setAcc_security_code(jsonObject.getString(AccountRecord.COLUMN_ACC_SECURITYCODE));
            offlineLogin.setProfile_image_path(jsonObject.getString(AccountRecord.COLUMN_PROFILE_IMAGE_PATH));
            offlineLogin.setAcc_balance(jsonObject.getDouble(AccountRecord.COLUMN_ACC_BALANCE));
            offlineLogin.setRegister_date(mySqlDateFormat.parse(jsonObject.getString(AccountRecord.COLUMN_REGISTER_DATE)));

            offlineLogin.setResponse((jsonObject.getInt(KEY_RESPONSE)));
            Log.d("LoginActivity", "response : " + jsonObject.getInt("response")+"");


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void shortToast(Context a, String message) {
        Toast.makeText(a, message, Toast.LENGTH_SHORT).show();
    }

    public void longToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
