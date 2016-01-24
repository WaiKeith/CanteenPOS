package my.edu.chiawaikeith.canteenpos.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.software.shell.fab.ActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Activities.BaseActivity;
import my.edu.chiawaikeith.canteenpos.Activities.EditProfile;
import my.edu.chiawaikeith.canteenpos.Domains.Accounts;
import my.edu.chiawaikeith.canteenpos.Domains.Students;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;

public class ProfileFragment extends BaseFragment {
    Context context;
    private ActionButton actionButton;
    final static String KEY_CUST_ID = "A.cust_id";
    final static String KEY_USER_NAME = "A.user_name";
    final static String KEY_ACCBALANCE = "A.acc_balance";
    final static String KEY_REGISTERDATE = "A.register_date";
    final static String KEY_PROFILE_IMAGE_PATH = "A.profile_image_path";
    final static String KEY_STUD_NAME = "S.stud_name";
    final static String KEY_COURSE = "S.stud_course";
    final static String KEY_EMAIL = "S.stud_email";
    final static String KEY_STUD_ID = "S.stud_id";

    private TextView profileName,custID,userName,accountBalance,registerDate,studName,studCourse,studEmail;

    private int acc_id;
    JSONArray mJsonArray;
    public SimpleDateFormat mySqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

    private ArrayList<Accounts> accountList = new ArrayList<>();
    public Accounts account = new Accounts();
    public Students student = new Students();

    private static final String RETRIEVESTUD_URL = "http://canteenpos.comxa.com/Accounts/Students/retrieve_student.php";
    private static final String RETRIEVEACC_URL = "http://canteenpos.comxa.com/Accounts/Students/retrieve_account.php";
    private static final String RETRIEVEINFO_URL = "http://canteenpos.comxa.com/Accounts/Students/retrieve_info.php";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        initValues();
        loadAccount();
        //loadStudent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        actionButton = (ActionButton)view.findViewById(R.id.action_button);
        profileName = (TextView)view.findViewById(R.id.profile_name);
        actionButton.setImageResource(R.drawable.ic_edit);
        custID = (TextView)view.findViewById(R.id.cust_Id);
        userName = (TextView)view.findViewById(R.id.user_Name);
        accountBalance = (TextView)view.findViewById(R.id.acc_Balance);
        registerDate = (TextView)view.findViewById(R.id.register_Date);
        studName = (TextView)view.findViewById(R.id.stud_name);
        studCourse = (TextView)view.findViewById(R.id.courseStudy);
        studEmail = (TextView)view.findViewById(R.id.studemail);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                intent.putExtra(KEY_ACCOUNT, account);

                if(account.getProfile_image_path() == null)
                    Log.d("Account image path","null");
                else

                    Log.d("Account image path", account.getProfile_image_path());
                startActivity(intent);
            }
        });

        return view;
    }

    public void initValues(){
        acc_id = new BaseActivity().getLoginDetail(getActivity()).getAcc_id();
        account.setAcc_id(acc_id);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    private void loadAccount() {
//        new getAccount(RETRIEVEACC_URL).execute();
//    }

//    @Override
//    public void onRefresh() {
//        SharedPreferences accountList = getActivity().getSharedPreferences("accountList", 0);
//        acc_id = accountList.getInt("id", 0);
//        AccountDataSource accountDataSource = new AccountDataSource(getActivity());
//        Accounts accRecord = accountDataSource.getUserDetailsByID(acc_id);
//        cust_ID = accRecord.cust_id;
//        user_name =accRecord.user_name;
//        acc_balance = accRecord.acc_balance;
//        register_Date = accRecord.register_date;
////        user_gender = userRecord.gender;
////        user_age = userRecord.age;
////        user_weight = userRecord.weight;
////        user_height = userRecord.height;
//
//        textViewName.setText(user_name);
//
//    }

    private void loadStudent() {
        new getStudent().execute();
    }

    public class getStudent extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(getActivity(), "Loading...", "Please Wait...", true, true);
        }


        @Override
        protected void onPostExecute(String json2) {
            super.onPostExecute(json2);
            //loading.dismiss();
            Log.d("ProfileFragment", json2);
            convertjson(json2);
            extractjsonData();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put(KEY_STUD_ID, account.getCust_id());
            Log.d("studid", String.valueOf(account.getCust_id()));
            return rh.sendPostRequest(RETRIEVESTUD_URL, data);
        }
    }

    // parse JSON data into JSON array
    private void convertjson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            mJsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void extractjsonData() {

        for (int i = 0; i < mJsonArray.length(); i++) {
            try {

                JSONObject jsonObject2 = mJsonArray.getJSONObject(i);


                student.setStud_name((jsonObject2.getString(KEY_STUD_NAME)));
                student.setStud_course((jsonObject2.getString(KEY_COURSE)));
                student.setStud_email(jsonObject2.getString(KEY_EMAIL));

                Log.d("Profile", String.valueOf(mJsonArray.length()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadStudentView();

        }
    }

    public void loadAccount() {
        new getAccount(acc_id).execute();
    }

    // this one is get json
    public class getAccount extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
        Integer acc_ID;

        public getAccount(Integer accountId) {
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
                student.setStud_course(jsonObject.getString(KEY_COURSE));
                student.setStud_email(jsonObject.getString(KEY_EMAIL));
                account.setCust_id((jsonObject.getString(KEY_CUST_ID)));
                account.setUser_name((jsonObject.getString(KEY_USER_NAME)));
                account.setAcc_balance(Double.parseDouble(jsonObject.getString(KEY_ACCBALANCE)));
                account.setRegister_date(mySqlDateTimeFormat.parse(jsonObject.getString(KEY_REGISTERDATE)));
                account.setProfile_image_path(jsonObject.getString(KEY_PROFILE_IMAGE_PATH));
                Log.d("Account image path", jsonObject.getString(KEY_PROFILE_IMAGE_PATH));

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            loadAccountView();

        }
    }

    private void loadAccountView() {
        profileName.setText(account.getUser_name());
        userName.setText(account.getUser_name());
        custID.setText(account.getCust_id());
        accountBalance.setText(String.valueOf(account.getAcc_balance()));
        registerDate.setText(mySqlDateFormat.format(account.getRegister_date()));
        studName.setText(student.getStud_name());
        studCourse.setText(student.getStud_course());
        studEmail.setText(student.getStud_email());
    }

    private void loadStudentView() {
        studName.setText(student.getStud_name());
        studCourse.setText(student.getStud_course());
        studEmail.setText(student.getStud_email());
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
