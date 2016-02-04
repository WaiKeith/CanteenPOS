package my.edu.chiawaikeith.canteenpos.Fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Activities.BaseActivity;
import my.edu.chiawaikeith.canteenpos.Adapters.HistoryAdapter;
import my.edu.chiawaikeith.canteenpos.Domains.Transactions;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;

public class HistoryFragment extends BaseFragment implements RecyclerView.RecyclerListener,View.OnClickListener{
    private static final String RETRIEVEHISTORY_URL = "http://dinpos.comlu.com/Transactions/retrieve_transaction.php";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private final static String KEY_TRANSAC_ID = "transac_id";
    private final static String KEY_ACC_ID = "acc_id";
    private final static String KEY_PAYMENT_AMOUNT = "payment_amount";
    private final static String KEY_TOTAL_GST = "total_gst";
    private final static String KEY_ORDER_DATETIME = "order_date_time";
    private final static String KEY_ORDER_STATUS = "order_status";

    HistoryAdapter historyAdapter;
    private OnFragmentInteractionListener mListener;
    private RecyclerView rv;
    private int acc_id;
    private JSONArray mJsonArray;
    private ArrayList<Transactions> transactionsList = new ArrayList<>();

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFragment() {
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
        loadHistory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        rv = (RecyclerView)v.findViewById(R.id.history_recycler);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return v;
    }

    public void initValues(){
        acc_id = new BaseFragment().getLoginDetail(getActivity()).getAcc_id();
    }

    public void startView() {
        HistoryAdapter historyAdapter;
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyAdapter = new HistoryAdapter(getActivity(), transactionsList, R.layout.view_order_row);
        rv.setAdapter(historyAdapter);

    }

    public void loadHistory() {
        new getHistory().execute();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttonClear:
                historyAdapter.clearAdapter();
        }

    }

    public class getHistory extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
//        Integer acc_ID;
//
//        public getHistory(Integer accountId) {
//            this.acc_ID = accountId;
//        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(getActivity(), "Loading...", "Please Wait...", true, true);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            Log.d("orders", json);
            //loading.dismiss();
            convertJson(json);
            extractJsonData();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            Log.d("id",String.valueOf(acc_id));
            data.put(KEY_ACC_ID, String.valueOf(acc_id));
            return rh.sendPostRequest(RETRIEVEHISTORY_URL, data);
        }
    }

    // parse JSON data into JSON array
    private void convertJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            mJsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void extractJsonData() {

        for (int i = 0; i < mJsonArray.length(); i++) {
            try {

                JSONObject jsonObject = mJsonArray.getJSONObject(i);
                Transactions transactions = new Transactions();

                try {
                    transactions.setTransac_id((jsonObject.getInt(KEY_TRANSAC_ID)));
                    transactions.setAcc_id((jsonObject.getInt(KEY_ACC_ID)));
                    transactions.setPayment_amount(Double.parseDouble(jsonObject.getString(KEY_PAYMENT_AMOUNT)));
                    transactions.setTotal_gst(Double.parseDouble(jsonObject.getString(KEY_TOTAL_GST)));
                    transactions.setOrder_date_time(mySqlDateTimeFormat.parse(jsonObject.getString(KEY_ORDER_DATETIME)));
                    transactions.setOrder_status(jsonObject.getString(KEY_ORDER_STATUS));

                    transactionsList.add(transactions);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if(mJsonArray.length() > 0) {
            startView();
        }else {
            shortToast(getActivity(),"No records found.");
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
