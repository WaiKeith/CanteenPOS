package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Domains.Accounts;
import my.edu.chiawaikeith.canteenpos.Domains.Students;
import my.edu.chiawaikeith.canteenpos.Mail;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;

public class ForgotPassword extends BaseActivity implements View.OnClickListener {

        private JSONArray jsonArray;
        Accounts account = new Accounts();
        Students student = new Students();
        private Toolbar toolBar;
        private TextView aText;
        private Button btnProceed,btnRetrieve;
        private EditText editText1,editText2;
        private String userID = "",email = "";
        final static String VERIFY_URL = "http://canteenpos.comxa.com/Accounts/Students/get_password.php";

        final static String KEY_EMAIL = "stud_email";
        final static String KEY_CUSTOMER_ID = "cust_id";
        final static String KEY_ACC_PW = "acc_password";

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        toolBar = (Toolbar)findViewById(R.id.toolbar);
        aText = (TextView)findViewById(R.id.textviewHidden);
        editText1 = (EditText) findViewById(R.id.editID);
        editText2 = (EditText)findViewById(R.id.editEmail);
        btnProceed = (Button)findViewById(R.id.buttonProceed);
        btnProceed.setOnClickListener(this);
        btnRetrieve = (Button)findViewById(R.id.buttonRetrieve);
        btnRetrieve.setOnClickListener(this);

        setSupportActionBar(toolBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.title_forgot_password);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttonProceed:
                Intent intent = new Intent(this, LoginActivity.class);

                userID = editText1.getText().toString();
                email = editText2.getText().toString();

                if (userID.isEmpty() || email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all required fields!", Toast.LENGTH_LONG).show();
                } else {
                    new getPassword().execute(
                            userID.toString(),
                            email.toString());

                    //startActivity(intent);
                }

            case R.id.buttonReset:
//                editText1.setText(null);
//                editText2.setText(null);

                final Mail m = new Mail("keith_513345@hotmail.com", "chia513345");
                new AsyncTask<Void, Void, Void>() {
                    @Override public Void doInBackground(Void... arg) {
                        String[] toArr = {email,email};
                        m.setTo(toArr);
                        m.setFrom("keith_513345@hotmail.com");
                        m.setSubject("Password Recovery");
                        m.setBody(aText.getText().toString());

                        Log.d("here","here");
                        try {
                            if(m.send()) {
                                Toast.makeText(ForgotPassword.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                            } else {
                                //Toast.makeText(ForgotPassword.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                            }
                        } catch(Exception e) {
                            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                            Log.e("MailApp", "Could not send email", e);
                        }
                        return null;
                    }
                }.execute();
        }
    }

    public class getPassword extends AsyncTask<String, Void, String> {

        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(ForgotPassword.this, "Verifying...", null, true, true);
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            convertJson(json);
            extractJsonData();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<>();
            data.put(KEY_CUSTOMER_ID, params[0]);
            data.put(KEY_EMAIL, params[1]);

            return rh.sendPostRequest(VERIFY_URL, data);
        }
    }

    private void convertJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
            Log.d("length", String.valueOf(jsonArray.length()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void extractJsonData() {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                account.setAcc_password(jsonObject.getString(KEY_ACC_PW));
                //student.setStud_email(jsonObject.getString(KEY_EMAIL));
                Log.d("pw", account.getAcc_password());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        loadPassword();

        }

    private void loadPassword() {
        aText.setText(account.getAcc_password());
        email = editText2.getText().toString();
    }
}
