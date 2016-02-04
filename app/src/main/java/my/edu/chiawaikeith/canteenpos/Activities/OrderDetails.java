package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.software.shell.fab.ActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Adapters.HistoryAdapter;
import my.edu.chiawaikeith.canteenpos.Adapters.OrderLinesAdapter;
import my.edu.chiawaikeith.canteenpos.Domains.Foods;
import my.edu.chiawaikeith.canteenpos.Domains.OrderLines;
import my.edu.chiawaikeith.canteenpos.Domains.Transactions;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;


public class OrderDetails extends BaseActivity implements View.OnClickListener {

    TextView tvTransacID,tvPayment,tvTotalGST,tvDate,tvStatus;
    private Toolbar toolBar;
    private RecyclerView recyclerView;
    Context context;
    JSONArray mJsonArray;
    private ActionButton actionButton;
    private ArrayList<OrderLines> orderList = new ArrayList<>();
    private ArrayList<Foods> foodList = new ArrayList<>();
    Transactions transaction = new Transactions();
    final static String KEY_TRANSAC_ID = "transac_id";
    final static String KEY_FOOD_ID = "food_id";
    final static String KEY_ORDERLINE_ID = "order_line_id";
    final static String KEY_ITEM_QTY = "item_qty";
    final static String KEY_FOOD_NAME = "F.food_name";
    final static String GET_URL = "http://dinpos.comlu.com/OrderLines/retrieve_order_line.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        tvTransacID = (TextView) findViewById(R.id.transacID);
        tvPayment = (TextView)findViewById(R.id.paymentAmt);
        tvTotalGST = (TextView)findViewById(R.id.totalGST);
        tvDate = (TextView)findViewById(R.id.orderDate);
        tvStatus = (TextView)findViewById(R.id.orderStatus);
        toolBar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.title_order_details);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.order_line_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transaction = (Transactions) getIntent().getSerializableExtra(HistoryAdapter.KEY_HISTORY);

        getOrderLines();
        startView();

    }

    public void getOrderLines(){
        new getOrderLines().execute();
    }

    public class getOrderLines extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(getActivity(), "Uploading...", null, true, true);
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
            data.put(KEY_TRANSAC_ID,  String.valueOf(transaction.getTransac_id()));

            return rh.sendPostRequest(GET_URL, data);
        }
    }

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
                OrderLines orderLine = new OrderLines();
                Foods food = new Foods();

            try{
                orderLine.setTransac_id(jsonObject.getInt(KEY_TRANSAC_ID));
                orderLine.setFood_id(jsonObject.getInt(KEY_FOOD_ID));
                orderLine.setOrder_line_id(jsonObject.getInt(KEY_ORDERLINE_ID));
                orderLine.setItem_qty(jsonObject.getInt(KEY_ITEM_QTY));
                //food.setFood_name(jsonObject.getString(KEY_FOOD_NAME));

                orderList.add(orderLine);
                //foodList.add(food);
                //Log.d("foodname", food.getFood_name());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if(mJsonArray.length() > 0) {
            startView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_details, menu);
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

    private void startView() {
        tvTransacID.setText(String.valueOf(transaction.getTransac_id()));
        tvPayment.setText(String.valueOf(transaction.getPayment_amount()));
        tvTotalGST.setText(String.valueOf(transaction.getTotal_gst()));
        tvDate.setText(mySqlDateTimeFormat.format(transaction.getOrder_date_time()));
        tvStatus.setText(transaction.getOrder_status());

        OrderLinesAdapter orderLinesAdapter;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderLinesAdapter = new OrderLinesAdapter(OrderDetails.this, orderList, R.layout.view_orderline_row);
        recyclerView.setAdapter(orderLinesAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}
