package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Adapters.FoodAdapter;
import my.edu.chiawaikeith.canteenpos.Domains.Foods;
import my.edu.chiawaikeith.canteenpos.Domains.Transactions;
import my.edu.chiawaikeith.canteenpos.Fab;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;

public class FoodList extends BaseActivity implements View.OnClickListener {
    private String mText,sText,bText,status="Success";
    private Double a,b,g,s1,s2;
    private Double gst = 0.06,tPrice=0.00,totalgst=0.00;
    private Integer acc_id,transac_id,newTransac_id,f;
    private Date orderDate;
    Calendar calendar;
    Transactions transaction = new Transactions();
    private RecyclerView recyclerView;
    private JSONArray mJsonArray;
    private ArrayList<Foods> foodList = new ArrayList<>();
    private MaterialSheetFab materialSheetFab;

    final static String GETFOOD_URL = "http://dinpos.comlu.com/Foods/retrieve_foods.php";
    final static String START_URL = "http://dinpos.comlu.com/Transactions/start_transaction.php";
    final static String GET_URL = "http://dinpos.comlu.com/Transactions/get_transaction.php";
    final static String KEY_ACCOUNT_ID = "acc_id";
    final static String KEY_TRANSAC_ID = "transac_id";
    final static String KEY_FOOD_ID = "food_id";
    final static String KEY_PAYMENT_AMOUNT = "payment_amount";
    final static String KEY_ORDER_DATETIME = "order_date_time";
    final static String KEY_TOTAL_GST = "total_gst";
    final static String KEY_ORDER_STATUS = "order_status";
    final static String KEY_ITEMQTY = "item_qty";
    final static String KEY_STALL_ID = "stall_id";
    final static String KEY_FOOD_NAME = "food_name";
    final static String KEY_FOOD_CATEGORY = "food_category";
    final static String KEY_FOOD_PRICE = "food_price";
    final static String KEY_FOOD_GSTPRICE = "food_gst_price";
    final static String KEY_FOOD_IMAGE = "food_image_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        recyclerView = (RecyclerView)findViewById(R.id.food_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Fab fab = (Fab)findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.cardview_light_background);
        int fabColor = getResources().getColor(R.color.accentColor);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet item click listeners
        findViewById(R.id.fab_sheet_item_view).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_confirm).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_start).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_next).setOnClickListener(this);


        initValues();
        loadFoods();
        getTransaction();
        beginTransaction();
    }

    public void startView() {
        FoodAdapter foodAdapter;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new FoodAdapter(this, foodList, R.layout.view_food_row);
        recyclerView.setAdapter(foodAdapter);
    }

    public void loadFoods() {
        new getFood().execute();
    }

    public class getFood extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
//        Integer acc_ID;
//
//        private getReminder(Integer accountID){
//            this.acc_ID = accountID;
//        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(getActivity(), "Loading...", "Please Wait...", true, true);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            //Log.d("FoodList", json);
            //loading.dismiss();
            convertJson(json);
            extractJsonData();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            return rh.sendPostRequest(GETFOOD_URL, data);
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
                Foods foods = new Foods();

                try {
                    foods.setFood_id((jsonObject.getInt(KEY_FOOD_ID)));
                    foods.setStall_id((jsonObject.getInt(KEY_STALL_ID)));
                    foods.setFood_name(jsonObject.getString(KEY_FOOD_NAME));
                    foods.setFood_category(jsonObject.getString(KEY_FOOD_CATEGORY));
                    foods.setFood_price(jsonObject.getDouble(KEY_FOOD_PRICE));
                    foods.setFood_gst_price(jsonObject.getDouble(KEY_FOOD_GSTPRICE));
                    foods.setFood_image_path(jsonObject.getString(KEY_FOOD_IMAGE));

                    foodList.add(foods);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if(mJsonArray.length() > 0) {
            startView();
        }else {
            shortToast(this,"No records found.");}
    }

    private void initValues() {
        acc_id = new BaseActivity().getLoginDetail(this).getAcc_id();
    }


    public void getTransaction(){
        new getTransaction().execute();
    }

    private void beginTransaction(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Begin Transaction");
        builder.setMessage("Confirm to start transaction?");
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();

                transac_id = transaction.getTransac_id();

                Log.d("tranid", String.valueOf(transac_id));
                newTransac_id = transac_id + 1;
                transaction.setTransac_id(newTransac_id);

                Log.d("transac_id2", String.valueOf(transac_id));

                new newTransaction().execute(
                        String.valueOf(newTransac_id),
                        String.valueOf(acc_id)
                );
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public class getTransaction extends AsyncTask<String, Void, String> {
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
            convertJson1(json);
            extractJsonData1();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<>();
            data.put(KEY_ACCOUNT_ID,  String.valueOf(acc_id));

            return rh.sendPostRequest(GET_URL, data);
        }
    }

    private void convertJson1(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            mJsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void extractJsonData1() {

        for (int i = 0; i < mJsonArray.length(); i++) {
            try {
                JSONObject jsonObject = mJsonArray.getJSONObject(i);
                //Transactions transactions = new Transactions();

                transaction.setTransac_id(jsonObject.getInt(KEY_TRANSAC_ID));
                transaction.setAcc_id(jsonObject.getInt(KEY_ACCOUNT_ID));
                transaction.setPayment_amount(jsonObject.getDouble(KEY_PAYMENT_AMOUNT));
                transaction.setTotal_gst(jsonObject.getDouble(KEY_TOTAL_GST));
                transaction.setOrder_date_time(mySqlDateTimeFormat.parse(jsonObject.getString(KEY_ORDER_DATETIME)));
                transaction.setOrder_status(jsonObject.getString(KEY_ORDER_STATUS));


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    public class newTransaction extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
//        Transactions transac;
//
//        public newTransaction(Transactions transactions){
//            this.transac = transactions;
//        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(FoodList.this, "Uploading...", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<>();

            data.put(KEY_TRANSAC_ID, params[0]);
            data.put(KEY_ACCOUNT_ID, params[1]);

            return rh.sendPostRequest(START_URL, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_list, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_sheet_item_view:
//                tPrice = s1 + s2;
//                totalPrice.setText(tPrice.toString());
//
//                totalgst = tPrice * gst;
//                totalGST.setText(totalgst.toString());

                Intent intent = new Intent(this, FoodCarts.class);
                intent.putExtra(KEY_TRANSAC_ID, newTransac_id);
                startActivity(intent);
                break;
        }
    }
}
