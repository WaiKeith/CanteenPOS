package my.edu.chiawaikeith.canteenpos.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Activities.BaseActivity;
import my.edu.chiawaikeith.canteenpos.Domains.OrderLines;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;


public class ChartFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final static String KEY_FOOD_ID = "O.food_id";
    private final static String KEY_TOTAL_QTY = "O.total_qty";
    private static final String REPORT_URL = "http://canteenpos.comxa.com/Reports/report.php";
    private JSONArray jsonArray;
    OrderLines orderLines;

    private String mParam1;
    private String mParam2;
    private int a=35,b=25,c=35,d=5,acc_id;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chart, container, false);

        PieChart mPieChart = (PieChart)view. findViewById(R.id.piechart);

        mPieChart.addPieSlice(new PieModel("Fast Food", a, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Malay Food", b, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("Chinese Food", c, Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("Mamak", d, Color.parseColor("#FED70E")));

        getChart();
        mPieChart.startAnimation();
        return view;
    }

    public void initValues(){
        acc_id = new BaseFragment().getLoginDetail(getActivity()).getAcc_id();
    }

    public void getChart() {
        new getChart(acc_id).execute();
    }

    // this one is get json
    public class getChart extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
        Integer acc_ID;

        public getChart(Integer accountId) {
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
            return rh.sendPostRequest(REPORT_URL, data);
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

                orderLines.setTotal_qty(jsonObject.getInt(KEY_TOTAL_QTY));
                orderLines.setFood_id(jsonObject.getInt(KEY_FOOD_ID));

                //Log.d("Account image path", jsonObject.getString(KEY_PROFILE_IMAGE_PATH));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //loadA();

        }
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
