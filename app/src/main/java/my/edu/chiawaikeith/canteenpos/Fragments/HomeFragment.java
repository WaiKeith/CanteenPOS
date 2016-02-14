package my.edu.chiawaikeith.canteenpos.Fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.communication.IOnBarClickedListener;
import org.eazegraph.lib.models.BarModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Activities.BaseActivity;
import my.edu.chiawaikeith.canteenpos.Domains.Accounts;
import my.edu.chiawaikeith.canteenpos.Domains.Reports;
import my.edu.chiawaikeith.canteenpos.Domains.Transactions;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;

public class HomeFragment extends BaseFragment implements View.OnClickListener{
    private int a=35,b=25,c=35,d=5,acc_id;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final static String KEY_CUST_ID = "cust_id";
    final static String KEY_USER_NAME = "user_name";
    final static String KEY_ACCBALANCE = "acc_balance";
    final static String KEY_REGISTERDATE = "register_date";
    private final static String KEY_PAYMENT_AMOUNT = "payment_amount";
    private final static String KEY_TOTAL_GST = "total_gst";
    private final static String KEY_ORDER_DATETIME = "order_date_time";
    private static final String REPORT_URL = "http://dinpos.comlu.com/Reports/report3.php";
    private static final String RETRIEVEACC_URL = "http://dinpos.comlu.com/Accounts/Students/retrieve_account.php";

    Accounts account = new Accounts();
    private String mParam1;
    private String mParam2;
    private TextView userName,accountBalance,accStatus;
    public double todayExpense=0,GST=0;
    BarChart barChart;
    Reports expenseRecord;
    Transactions transactions = new Transactions();
    JSONArray jsonArray;
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
        getBarChart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        //custID = (TextView)view.findViewById(R.id.cust_Id);
        userName = (TextView)view.findViewById(R.id.user_Name);
        accountBalance = (TextView)view.findViewById(R.id.acc_Balance);
        accStatus = (TextView)view.findViewById(R.id.acc_status);

//        pieChart.addPieSlice(new PieModel("Fast Food", a, Color.parseColor("#FE6DA8")));
//        pieChart.addPieSlice(new PieModel("Malay Food", b, Color.parseColor("#56B7F1")));
//        pieChart.addPieSlice(new PieModel("Chinese Food", c, Color.parseColor("#CDA67F")));
//        pieChart.addPieSlice(new PieModel("Mamak", d, Color.parseColor("#FED70E")));

        barChart = (BarChart) view.findViewById(R.id.barchart);

        barChart.setOnBarClickedListener(new IOnBarClickedListener() {
            @Override
            public void onBarClicked(int _Position) {
                Log.d("BarChart", "Position: " + _Position);
            }
        });
        return view;
    }

    public void initValues(){
        acc_id = new BaseFragment().getLoginDetail(getActivity()).getAcc_id();
        Log.d("id",String.valueOf(acc_id));
    }

    public void loadAccount() {
        new getAccount(acc_id).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){



        }

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
            jsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
            Log.d("length",String.valueOf(jsonArray.length()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void extractJsonData() {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

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
        //custID.setText(account.getCust_id());
        accountBalance.setText("RM " + String.valueOf(account.getAcc_balance()));
        if(account.getAcc_balance() <= 1.00){
            accStatus.setText("Not allow to order");
            //shortToast(getActivity(),"Reason: Minimum account balance is 1.00 and above!");
        }else {
            accStatus.setText("Able to order");
        }
    }

    public void getBarChart() {
        new getBarChart(acc_id).execute();
    }

    // this one is get json
    public class getBarChart extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
        Integer acc_ID;

        public getBarChart(Integer accountId) {
            this.acc_ID = accountId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(getActivity(), "Loading...", "Please Wait...", true, true);
        }


        @Override
        protected void onPostExecute(String Json) {
            super.onPostExecute(Json);
            //loading.dismiss();
            convertJson1(Json);
            extractJsonData1();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("acc_id", String.valueOf(acc_ID));
            return rh.sendPostRequest(REPORT_URL, data);
        }
    }

    // parse JSON data into JSON array
    private void convertJson1(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
            Log.d("array",String.valueOf(jsonArray));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void extractJsonData1() {
        expenseRecord = new Reports();
        Date orderDateTime;
        double todayTotal=0,todayGst=0;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                transactions.setPayment_amount(jsonObject.getDouble(KEY_PAYMENT_AMOUNT));
                transactions.setTotal_gst(jsonObject.getDouble(KEY_TOTAL_GST));
                transactions.setOrder_date_time(mySqlDateTimeFormat.parse(jsonObject.getString(KEY_ORDER_DATETIME)));
                Log.d("date",mySqlDateTimeFormat.format(transactions.getOrder_date_time()));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy/MM/dd");
            java.util.Date today = null;
            java.util.Date date = null;
            Calendar cal = Calendar.getInstance();

            //orderDateTime = transactions.getOrder_date_time();
            if(transactions.getOrder_date_time() != null) {
                try {
                    date = dfDate.parse(dfDate.format(transactions.getOrder_date_time()));
                    today = dfDate.parse(dfDate.format(cal.getTime()));//Returns 15/10/2012
                    Log.d("day", dfDate.format(today));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                int diffInDays = (int) ((today.getTime() - date.getTime())/ (1000 * 60 * 60 * 24));

                //int days=0;
                double total = 0,gst = 0;
                if(diffInDays == 0){
                    total = transactions.getPayment_amount();
                    gst = transactions.getTotal_gst();
                }

                todayTotal += total;
                todayGst += gst;
                expenseRecord.setTodayTotal(todayTotal);
                expenseRecord.setTodayGst(todayGst);

            }else {
                shortToast(getActivity(),"No transaction found!");
            }
        }

        if(jsonArray.length() > 0) {
            startBarChart();
        }else {
            shortToast(getActivity(),"No records analysis.");
        }
    }

    private void startBarChart(){
        todayExpense = expenseRecord.getTodayTotal();
        float f1 = (float)todayExpense;
        GST = expenseRecord.getTodayGst();
        float f2 = (float)GST;

        barChart.addBar(new BarModel(f1, 0xFF123456));
        barChart.addBar(new BarModel(f2, 0xFF343456));

        barChart.startAnimation();
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
