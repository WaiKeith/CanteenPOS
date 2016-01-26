package my.edu.chiawaikeith.canteenpos.Fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import my.edu.chiawaikeith.canteenpos.R;


public class ChartFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String REPORT_URL = "http://canteenpos.comxa.com/Reports/report.php";

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

        mPieChart.startAnimation();
        return view;
    }

    public void initValues(){
        acc_id = new BaseFragment().getLoginDetail(getActivity()).getAcc_id();
    }

//    public void loadInfo() {
//        new getInfo(acc_id).execute();
//    }
//
//    // this one is get json
//    public class getInfo extends AsyncTask<String, Void, String> {
//        ProgressDialog loading;
//        RequestHandler rh = new RequestHandler();
//        Integer acc_ID;
//
//        public getInfo(Integer accountId) {
//            this.acc_ID = accountId;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //loading = ProgressDialog.show(getActivity(), "Loading...", "Please Wait...", true, true);
//        }
//
//
//        @Override
//        protected void onPostExecute(String json) {
//            super.onPostExecute(json);
//            //loading.dismiss();
//            Log.d("ProfileFragment", json);
//            convertJson(json);
//            extractJsonData();
//
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            HashMap<String, String> data = new HashMap<>();
//            data.put("acc_id", String.valueOf(acc_ID));
//            return rh.sendPostRequest(RETRIEVEINFO_URL, data);
//        }
//    }
//
//    // parse JSON data into JSON array
//    private void convertJson(String json) {
//        try {
//            JSONObject jsonObject = new JSONObject(json);
//            mJsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
//            Log.d("length",String.valueOf(mJsonArray.length()));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void extractJsonData() {
//
//        for (int i = 0; i < mJsonArray.length(); i++) {
//            try {
//                JSONObject jsonObject = mJsonArray.getJSONObject(i);
//
//                student.setStud_name(jsonObject.getString(KEY_STUD_NAME));
//                student.setStud_course(jsonObject.getString(KEY_COURSE));
//                student.setStud_email(jsonObject.getString(KEY_EMAIL));
//                account.setCust_id((jsonObject.getString(KEY_CUST_ID)));
//                account.setUser_name((jsonObject.getString(KEY_USER_NAME)));
//                account.setAcc_balance(Double.parseDouble(jsonObject.getString(KEY_ACCBALANCE)));
//                account.setRegister_date(mySqlDateTimeFormat.parse(jsonObject.getString(KEY_REGISTERDATE)));
//                account.setProfile_image_path(jsonObject.getString(KEY_PROFILE_IMAGE_PATH));
//                Log.d("Account image path", jsonObject.getString(KEY_PROFILE_IMAGE_PATH));
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            loadInfoView();
//
//        }
//    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
