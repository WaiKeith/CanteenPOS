package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import my.edu.chiawaikeith.canteenpos.Domains.Accounts;
import my.edu.chiawaikeith.canteenpos.Domains.Students;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;

public class ProfileDetails extends BaseActivity {
    Context context;
    private FloatingActionButton fabEdit;
    final static String KEY_CUST_ID = "A.cust_id";
    final static String KEY_USER_NAME = "A.user_name";
    final static String KEY_ACCBALANCE = "A.acc_balance";
    final static String KEY_REGISTERDATE = "A.register_date";
    final static String KEY_PROFILE_IMAGE_PATH = "A.profile_image_path";
    final static String KEY_STUD_NAME = "S.stud_name";
    final static String KEY_COURSE = "S.stud_course";
    final static String KEY_EMAIL = "S.stud_email";
    final static String KEY_STUD_IC_NO = "S.stud_ic_no";
    final static String KEY_STUD_DOB = "S.stud_DOB";
    final static String KEY_STUD_CONTACT_NO = "S.stud_contact_no";

    private TextView profileName,custID,userName,accountBalance,registerDate,studName,studCourse,studEmail;
    private TextView icNo,dateBorn,contactNo;
    private int acc_id;
    private CircleImageView profilePic;
    JSONArray mJsonArray;
    public SimpleDateFormat mySqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Accounts account = new Accounts();
    public Students student = new Students();
    public final static String KEY_ACCOUNT = "account";
    private static final String RETRIEVEINFO_URL = "http://dinpos.comlu.com/Accounts/Students/retrieve_info.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        fabEdit = (FloatingActionButton)findViewById(R.id.fab_edit);
        profileName = (TextView)findViewById(R.id.profile_name);
        custID = (TextView)findViewById(R.id.cust_Id);
        userName = (TextView)findViewById(R.id.user_Name);
        accountBalance = (TextView)findViewById(R.id.acc_Balance);
        registerDate = (TextView)findViewById(R.id.register_Date);
        studName = (TextView)findViewById(R.id.stud_name);
        studCourse = (TextView)findViewById(R.id.courseStudy);
        studEmail = (TextView)findViewById(R.id.studemail);
        profilePic = (CircleImageView)findViewById(R.id.image_profile);
        icNo = (TextView)findViewById(R.id.ic_no);
        dateBorn = (TextView)findViewById(R.id.date_born);
        contactNo = (TextView)findViewById(R.id.contact_no);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileDetails.this, EditProfile.class);
                intent.putExtra(KEY_ACCOUNT, account);

                if(account.getProfile_image_path() == null)
                    Log.d("Account image path", "null");
                else

                    Log.d("Account image path", account.getProfile_image_path());
                startActivity(intent);
            }
        });

        initValues();
        loadInfo();
    }

    public void initValues(){
        acc_id = new BaseActivity().getLoginDetail(this).getAcc_id();
        account.setAcc_id(acc_id);
    }

    public void loadInfo() {
        new getInfo(acc_id).execute();
    }

    // this one is get json
    public class getInfo extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
        Integer acc_ID;

        public getInfo(Integer accountId) {
            this.acc_ID = accountId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(getActivity(), "Loading...", "Please Wait...", true, true);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            //loading.dismiss();
            Log.d("ProfileFragment", json);
            convertJson(json);
            extractJsonData();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("acc_id", String.valueOf(acc_ID));
            return rh.sendPostRequest(RETRIEVEINFO_URL, data);
        }
    }

    // parse JSON data into JSON array
    private void convertJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            mJsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
            Log.d("length",String.valueOf(mJsonArray.length()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void extractJsonData() {

        for (int i = 0; i < mJsonArray.length(); i++) {
            try {
                JSONObject jsonObject = mJsonArray.getJSONObject(i);

                student.setStud_name(jsonObject.getString(KEY_STUD_NAME));
                student.setStud_ic_no(jsonObject.getString(KEY_STUD_IC_NO));
                student.setStud_DOB(jsonObject.getString(KEY_STUD_DOB));
                student.setStud_contact_no(jsonObject.getString(KEY_STUD_CONTACT_NO));
                student.setStud_course(jsonObject.getString(KEY_COURSE));
                student.setStud_email(jsonObject.getString(KEY_EMAIL));
                account.setCust_id((jsonObject.getString(KEY_CUST_ID)));
                account.setUser_name((jsonObject.getString(KEY_USER_NAME)));
                account.setAcc_balance(jsonObject.getDouble(KEY_ACCBALANCE));
                account.setRegister_date(mySqlDateTimeFormat.parse(jsonObject.getString(KEY_REGISTERDATE)));
                account.setProfile_image_path(jsonObject.getString(KEY_PROFILE_IMAGE_PATH));
                Log.d("Account image path", jsonObject.getString(KEY_PROFILE_IMAGE_PATH));

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            loadInfoView();

        }
    }

    private void loadInfoView() {
        profileName.setText(account.getUser_name());
        userName.setText(account.getUser_name());
        custID.setText(account.getCust_id());
        accountBalance.setText("RM " + String.valueOf(account.getAcc_balance()));
        registerDate.setText(mySqlDateFormat.format(account.getRegister_date()));
        studName.setText(student.getStud_name());
        studCourse.setText(student.getStud_course());
        studEmail.setText(student.getStud_email());
        icNo.setText(student.getStud_ic_no());
        dateBorn.setText(student.getStud_DOB());
        contactNo.setText(student.getStud_contact_no());
        if(account.getProfile_image_path() != ""){
            ImageLoader.getInstance().displayImage(account.getProfile_image_path(), profilePic, options);}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_details, menu);
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
}
