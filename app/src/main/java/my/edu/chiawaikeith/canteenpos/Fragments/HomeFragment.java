package my.edu.chiawaikeith.canteenpos.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Activities.BaseActivity;
import my.edu.chiawaikeith.canteenpos.Domains.Accounts;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;

public class HomeFragment extends BaseFragment {
    private int a=35,b=25,c=35,d=5,acc_id;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final static String KEY_CUST_ID = "cust_id";
    final static String KEY_USER_NAME = "user_name";
    final static String KEY_ACCBALANCE = "acc_balance";
    final static String KEY_REGISTERDATE = "register_date";
    private static final String RETRIEVEACC_URL = "http://canteenpos.comxa.com/Accounts/Students/retrieve_account.php";

    Accounts account = new Accounts();
    private String mParam1;
    private String mParam2;
    private TextView custID,userName,accountBalance,registerDate;
    JSONArray mJsonArray;
    public SimpleDateFormat mySqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

    private OnFragmentInteractionListener mListener;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        initValues();
        loadAccount();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        PieChart mPieChart = (PieChart)view. findViewById(R.id.piechart);
        custID = (TextView)view.findViewById(R.id.cust_Id);
        userName = (TextView)view.findViewById(R.id.user_Name);
        accountBalance = (TextView)view.findViewById(R.id.acc_Balance);
        registerDate = (TextView)view.findViewById(R.id.register_Date);

        mPieChart.addPieSlice(new PieModel("Fast Food", a, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Malay Food", b, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("Chinese Food", c, Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("Mamak", d, Color.parseColor("#FED70E")));

        //getChart();
        mPieChart.startAnimation();
        return view;
    }

    public void initValues(){
        acc_id = new BaseFragment().getLoginDetail(getActivity()).getAcc_id();
        Log.d("id",String.valueOf(acc_id));
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
            Log.d("HomeFragment", json);
            convertJson(json);
            extractJsonData();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("acc_id", String.valueOf(acc_ID));
            return rh.sendPostRequest(RETRIEVEACC_URL, data);
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

                account.setCust_id((jsonObject.getString(KEY_CUST_ID)));
                account.setUser_name((jsonObject.getString(KEY_USER_NAME)));
                account.setAcc_balance(Double.parseDouble(jsonObject.getString(KEY_ACCBALANCE)));
                account.setRegister_date(mySqlDateTimeFormat.parse(jsonObject.getString(KEY_REGISTERDATE)));

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            loadAccountView();

        }
    }

    private void loadAccountView() {
        userName.setText(account.getUser_name());
        Log.d("name",account.getUser_name());
        custID.setText(account.getCust_id());
        accountBalance.setText(String.valueOf(account.getAcc_balance()));
        registerDate.setText(mySqlDateFormat.format(account.getRegister_date()));
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
