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

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.communication.IOnBarClickedListener;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Activities.BaseActivity;
import my.edu.chiawaikeith.canteenpos.Domains.Foods;
import my.edu.chiawaikeith.canteenpos.Domains.OrderLines;
import my.edu.chiawaikeith.canteenpos.Domains.Reports;
import my.edu.chiawaikeith.canteenpos.Domains.Transactions;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;


public class ChartFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final static String KEY_FOOD_ID = "O.food_id";
    private final static String KEY_ITEM_QTY = "O.item_qty";
    private final static String KEY_FOOD_CATEGORY = "F.food_category";
    private final static String KEY_PAYMENT_AMOUNT = "payment_amount";
    private final static String KEY_ORDER_DATETIME = "order_date_time";
    private static final String REPORT1_URL = "http://canteenpos.comxa.com/Reports/reportv1.php";
    private static final String REPORT2_URL = "http://canteenpos.comxa.com/Reports/reportv2.php";
    private JSONArray jsonArray;
    OrderLines orderLines = new OrderLines();
    Reports categoryRecord,expenseRecord;
    Transactions transactions = new Transactions();
    Foods foods = new Foods();
    private String mParam1;
    private String mParam2;
    private int a=35,b=25,c=35,d=5,acc_id;
    public int rice=0,noodle=0;
    public double day1=0,day2=0,day3=0,day4=0,day5=0,day6=0,day7=0;
    PieChart pieChart;
    BarChart barChart;

    private OnFragmentInteractionListener mListener;

    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ChartFragment() {
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
        getPieChart();
        getBarChart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chart, container, false);

        barChart = (BarChart) view.findViewById(R.id.barchart);

        barChart.setOnBarClickedListener(new IOnBarClickedListener() {
            @Override
            public void onBarClicked(int _Position) {
                Log.d("BarChart", "Position: " + _Position);
            }
        });

        pieChart = (PieChart)view. findViewById(R.id.piechart);
        return view;
    }

    public void initValues(){
        acc_id = new BaseFragment().getLoginDetail(getActivity()).getAcc_id();
    }

    public void getPieChart() {
        new getPieChart(acc_id).execute();
    }

    // this one is get json
    public class getPieChart extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
        Integer acc_ID;

        public getPieChart(Integer accountId) {
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
            Log.d("ChartFragment", json);
            convertJson(json);
            extractJsonData();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("acc_id", String.valueOf(acc_ID));
            return rh.sendPostRequest(REPORT1_URL, data);
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
        categoryRecord = new Reports();
        int total1=0,total2=0;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                orderLines.setItem_qty(jsonObject.getInt(KEY_ITEM_QTY));
                orderLines.setFood_id(jsonObject.getInt(KEY_FOOD_ID));
                foods.setFood_category(jsonObject.getString(KEY_FOOD_CATEGORY));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String foodCategory;
            int riceQty=0,noodleQty=0;
            foodCategory = foods.getFood_category();
            switch (foodCategory){
                case "rice":
                    riceQty = orderLines.getItem_qty();
                    Log.d("total1",String.valueOf(categoryRecord.getRice()));
                    break;

                case "noodle":
                    noodleQty = orderLines.getItem_qty();
                    break;
            }

            total1 += riceQty;
            categoryRecord.setRice(total1);
            total2 += noodleQty;
            categoryRecord.setNoodle(total2);

        }

        if(jsonArray.length() > 0) {
            startPieChart();
        }else {
            shortToast(getActivity(),"No records analysis.");
        }
    }

    private void startPieChart(){
        rice = categoryRecord.getRice();
        Log.d("rice",String.valueOf(rice));
        noodle = categoryRecord.getNoodle();

        pieChart.addPieSlice(new PieModel("Rice", rice, Color.parseColor("#FE6DA8")));
        pieChart.addPieSlice(new PieModel("Noodle", noodle, Color.parseColor("#56B7F1")));
        //mPieChart.addPieSlice(new PieModel("Chinese Food", c, Color.parseColor("#CDA67F")));
        //mPieChart.addPieSlice(new PieModel("Mamak", d, Color.parseColor("#FED70E")));

        pieChart.startAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();
        barChart.startAnimation();
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
            Log.d("ChartFragment", Json);
            convertJson2(Json);
            extractJsonData2();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("acc_id", String.valueOf(acc_ID));
            return rh.sendPostRequest(REPORT2_URL, data);
        }
    }

    // parse JSON data into JSON array
    private void convertJson2(String Json) {
        try {
            JSONObject jsonObject = new JSONObject(Json);
            jsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
            Log.d("length2",String.valueOf(jsonArray.length()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void extractJsonData2() {
        expenseRecord = new Reports();
        double total1=0,total2=0,total3=0,total4=0,total5=0,total6=0,total7=0;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                transactions.setPayment_amount(jsonObject.getDouble(KEY_PAYMENT_AMOUNT));
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
            try {
                date = dfDate.parse(dfDate.format(transactions.getOrder_date_time()));
                Log.d("day",dfDate.format(date));
                today = dfDate.parse(dfDate.format(cal.getTime()));//Returns 15/10/2012
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            int diffInDays = (int) ((today.getTime() - date.getTime())/ (1000 * 60 * 60 * 24));

            //int days=0;
            double d1=0,d2=0,d3=0,d4=0,d5=0,d6=0,d7=0;
            switch (diffInDays){
                case 1:
                    d1 = transactions.getPayment_amount();
                    break;

                case 2:
                    d2 = transactions.getPayment_amount();
                    break;

                case 3:
                    d3 = transactions.getPayment_amount();
                    break;

                case 4:
                    d4 = transactions.getPayment_amount();
                    break;

                case 5:
                    d5 = transactions.getPayment_amount();
                    break;

                case 6:
                    d6 = transactions.getPayment_amount();
                    break;

                case 7:
                    d7 = transactions.getPayment_amount();
                    break;
            }

            total1 += d1;
            expenseRecord.setDay1(total1);
            total2 += d2;
            expenseRecord.setDay2(total2);
            total3 += d3;
            expenseRecord.setDay3(total3);
            total4 += d4;
            expenseRecord.setDay4(total4);
            total5 += d5;
            expenseRecord.setDay5(total5);
            total6 += d6;
            expenseRecord.setDay6(total6);
            total7 += d7;
            expenseRecord.setDay7(total7);

        }

        if(jsonArray.length() > 0) {
            startBarChart();
        }else {
            shortToast(getActivity(),"No records analysis.");
        }
    }

    private void startBarChart(){
        day1 = expenseRecord.getDay1();
        float f1 = (float)day1;
        Log.d("rice",String.valueOf(rice));
        day2 = expenseRecord.getDay2();
        float f2 = (float)day2;
        day3 = expenseRecord.getDay3();
        float f3 = (float)day3;
        day4 = expenseRecord.getDay4();
        float f4 = (float)day4;
        day5 = expenseRecord.getDay5();
        float f5 = (float)day5;
        day6 = expenseRecord.getDay6();
        float f6 = (float)day6;
        day7 = expenseRecord.getDay7();
        float f7 = (float)day7;

        barChart.addBar(new BarModel(f7, 0xFF123456));
        barChart.addBar(new BarModel(f6, 0xFF343456));
        barChart.addBar(new BarModel(f5, 0xFF563456));
        barChart.addBar(new BarModel(f4, 0xFF873F56));
        barChart.addBar(new BarModel(f3, 0xFF56B7F1));
        barChart.addBar(new BarModel(f2, 0xFF56B7F1));
        barChart.addBar(new BarModel(f1, 0xFF56B7F1));

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
