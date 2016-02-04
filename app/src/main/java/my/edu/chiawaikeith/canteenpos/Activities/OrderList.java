package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Adapters.HistoryAdapter;
import my.edu.chiawaikeith.canteenpos.Domains.Transactions;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;


public class OrderList extends BaseActivity implements View.OnClickListener {
    private static final String RETRIEVEHISTORY_URL = "http://dinpos.comlu.com/Transactions/retrieve_transaction.php";
    private final static String KEY_TRANSAC_ID = "transac_id";
    private final static String KEY_ACC_ID = "acc_id";
    private final static String KEY_PAYMENT_AMOUNT = "payment_amount";
    private final static String KEY_TOTAL_GST = "total_gst";
    private final static String KEY_ORDER_DATETIME = "order_date_time";
    private final static String KEY_ORDER_STATUS = "order_status";

    private RecyclerView recyclerView;
    private int acc_id;
    private JSONArray mJsonArray;
    private ArrayList<Transactions> transactionsList = new ArrayList<>();
    private Toolbar toolBar;
    HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        toolBar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.title_order_list);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.history_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initValues();
        loadHistory();
    }

    public void initValues(){
        acc_id = new BaseActivity().getLoginDetail(this).getAcc_id();
    }

    public void startView() {
        HistoryAdapter historyAdapter;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new HistoryAdapter(OrderList.this, transactionsList, R.layout.view_order_row);
        recyclerView.setAdapter(historyAdapter);
    }

    public void loadHistory() {
        new getHistory().execute();
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
            Log.d("OrderList", json);
            //loading.dismiss();
            convertJson(json);
            extractJsonData();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
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
        shortToast(this,"No records found.");}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_order_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonClear:
                historyAdapter.clearAdapter();
        }
    }
}
