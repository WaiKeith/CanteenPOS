package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import my.edu.chiawaikeith.canteenpos.Adapters.FoodAdapter;
import my.edu.chiawaikeith.canteenpos.Domains.Foods;
import my.edu.chiawaikeith.canteenpos.Domains.Transactions;
import my.edu.chiawaikeith.canteenpos.Fab;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;

public class FoodDetails extends BaseActivity implements View.OnClickListener {

    Calendar calendar;
    private int food_id;
    private Toolbar toolBar;
    private JSONArray mJsonArray;
    private MaterialSheetFab materialSheetFab;
    public Foods food = new Foods();
    private CircleImageView foodPic;
    Transactions transaction = new Transactions();
    public Integer acc_id,newTransac_id;
    private TextView stallID,foodID,foodName,foodCategory,price,quantity;

    final static String INSERT_URL = "http://dinpos.comlu.com/OrderLines/insert_order_line.php";
    final static String GET_URL = "http://dinpos.comlu.com/Transactions/get_transaction.php";
//    final static String GETFOOD_URL = "http://dinpos.comlu.com/Foods/retrieve_foods.php";
    final static String KEY_ACCOUNT_ID = "acc_id";
    final static String KEY_TRANSAC_ID = "transac_id";
    final static String KEY_FOOD_ID = "food_id";
    final static String KEY_ITEMQTY = "item_qty";
    final static String KEY_SINGLE_PRICE = "single_price";
    final static String KEY_ORDER_DATETIME = "order_date_time";
//    final static String KEY_FOOD_IMAGE_PATH = "food_image_path";
    final static String KEY_TOTAL_GST = "total_gst";
    final static String KEY_ORDER_STATUS = "order_status";
    final static String KEY_PAYMENT_AMOUNT = "payment_amount";
    final static String KEY_FOOD_NAME = "food_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        toolBar = (Toolbar)findViewById(R.id.toolbar);
        foodID = (TextView)findViewById(R.id.food_id);
        stallID = (TextView)findViewById(R.id.stall_id);
        foodCategory = (TextView)findViewById(R.id.food_category);
        foodName = (TextView)findViewById(R.id.food_name);
        price = (TextView)findViewById(R.id.food_price);
        quantity = (TextView)findViewById(R.id.qty);
        foodPic = (CircleImageView)findViewById(R.id.image_food);

        setSupportActionBar(toolBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.title_food_details);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fab fab = (Fab)findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.cardview_light_background);
        int fabColor = getResources().getColor(R.color.accentColor);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet item click listeners
        findViewById(R.id.fab_sheet_item_add).setOnClickListener(this);
        food = (Foods) getIntent().getSerializableExtra(FoodAdapter.KEY_FOOD);
        initValues();
        getTransaction();
        //getFood();

    }

    public void initValues() {
        acc_id = new BaseActivity().getLoginDetail(this).getAcc_id();
        foodID.setText(String.valueOf(food.getFood_id()));
        stallID.setText(String.valueOf(food.getStall_id()));
        foodName.setText(food.getFood_name());
        foodCategory.setText(food.food_category);
        price.setText(String.valueOf(food.getFood_price()));
        if(food.getFood_image_path() != ""){
            ImageLoader.getInstance().displayImage(food.getFood_image_path(), foodPic, options);}
    }

    public void getTransaction(){
        new getTransaction().execute();
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
            convertJson(json);
            extractJsonData();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<>();
            data.put(KEY_ACCOUNT_ID,  String.valueOf(acc_id));

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
                //transaction = new Transactions();

                transaction.setTransac_id(jsonObject.getInt(KEY_TRANSAC_ID));
                transaction.setAcc_id(jsonObject.getInt(KEY_ACCOUNT_ID));
                transaction.setPayment_amount(jsonObject.getDouble(KEY_PAYMENT_AMOUNT));
                transaction.setTotal_gst(jsonObject.getDouble(KEY_TOTAL_GST));
                transaction.setOrder_date_time(mySqlDateTimeFormat.parse(jsonObject.getString(KEY_ORDER_DATETIME)));
                transaction.setOrder_status(jsonObject.getString(KEY_ORDER_STATUS));
                newTransac_id = transaction.getTransac_id();


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

//    public void getFood(){
//        new getFood().execute();
//    }
//
//    public class getFood extends AsyncTask<String, Void, String> {
//        ProgressDialog loading;
//        RequestHandler rh = new RequestHandler();
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //loading = ProgressDialog.show(getActivity(), "Uploading...", null, true, true);
//        }
//
//        @Override
//        protected void onPostExecute(String json) {
//            super.onPostExecute(json);
//            convertJson1(json);
//            extractJsonData1();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            HashMap<String, String> data = new HashMap<>();
//            data.put(KEY_FOOD_ID,  String.valueOf(food.getFood_id()));
//
//            return rh.sendPostRequest(GETFOOD_URL, data);
//        }
//    }
//
//    private void convertJson1(String json) {
//        try {
//            JSONObject jsonObject = new JSONObject(json);
//            mJsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void extractJsonData1() {
//
//        for (int i = 0; i < mJsonArray.length(); i++) {
//            try {
//                JSONObject jsonObject = mJsonArray.getJSONObject(i);
//                //transaction = new Transactions();
//
//                food.setFood_id((jsonObject.getInt(KEY_FOOD_ID)));
//                food.setFood_name(jsonObject.getString(KEY_FOOD_NAME));
//                food.setFood_image_path(jsonObject.getString(KEY_FOOD_IMAGE_PATH));
//                Log.d("foodpath",food.getFood_image_path());
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            loadFoodImage();
//        }
//
//    }
//
//    private void loadFoodImage() {
//        if(food.getFood_image_path() != ""){
//            ImageLoader.getInstance().displayImage(food.getFood_image_path(), foodPic, options);}
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_details, menu);
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
        Intent intent = new Intent(this, FoodCarts.class);
        switch (v.getId()) {
            case R.id.fab_sheet_item_add:

//                intent.putExtra(KEY_FOOD, food);
//                startActivity(intent);

                new MaterialDialog.Builder(this)
                        .title(R.string.pickQty)
                        .items(R.array.quantity)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int qty, CharSequence text) {
                                Intent intent = new Intent(view.getContext(), FoodList.class);
                                switch (qty) {
                                    case 1: // edit society event
                                        new insertOrderLine().execute(
                                                String.valueOf(newTransac_id),
                                                foodID.getText().toString(),
                                                String.valueOf(qty),
                                                price.getText().toString(),
                                                foodName.getText().toString());
                                        startActivity(intent);
                                        break;
                                    case 2: // delete society event
                                        new insertOrderLine().execute(
                                                String.valueOf(newTransac_id),
                                                foodID.getText().toString(),
                                                String.valueOf(qty),
                                                price.getText().toString(),
                                                foodName.getText().toString());
                                        startActivity(intent);
                                        break;
                                    case 3: // report
                                        new insertOrderLine().execute(
                                                String.valueOf(newTransac_id),
                                                foodID.getText().toString(),
                                                String.valueOf(qty),
                                                price.getText().toString(),
                                                foodName.getText().toString());
                                        startActivity(intent);
                                        break;
                                    case 4: // report
                                        new insertOrderLine().execute(
                                                String.valueOf(newTransac_id),
                                                foodID.getText().toString(),
                                                String.valueOf(qty),
                                                price.getText().toString(),
                                                foodName.getText().toString());
                                        startActivity(intent);
                                        break;
                                    case 5: // report
                                        new insertOrderLine().execute(
                                                String.valueOf(newTransac_id),
                                                foodID.getText().toString(),
                                                String.valueOf(qty),
                                                price.getText().toString(),
                                                foodName.getText().toString());
                                        startActivity(intent);
                                        break;
                                    case 6: // report
                                        new insertOrderLine().execute(
                                                String.valueOf(newTransac_id),
                                                foodID.getText().toString(),
                                                String.valueOf(qty),
                                                price.getText().toString(),
                                                foodName.getText().toString());
                                        startActivity(intent);
                                        break;
                                    case 7: // report
                                        new insertOrderLine().execute(
                                                String.valueOf(newTransac_id),
                                                foodID.getText().toString(),
                                                String.valueOf(qty),
                                                price.getText().toString(),
                                                foodName.getText().toString());
                                        startActivity(intent);
                                        break;
                                    case 8: // report
                                        new insertOrderLine().execute(
                                                String.valueOf(newTransac_id),
                                                foodID.getText().toString(),
                                                String.valueOf(qty),
                                                price.getText().toString(),
                                                foodName.getText().toString());
                                        startActivity(intent);
                                        break;
                                    case 9: // report
                                        new insertOrderLine().execute(
                                                String.valueOf(newTransac_id),
                                                foodID.getText().toString(),
                                                String.valueOf(qty),
                                                price.getText().toString(),
                                                foodName.getText().toString());
                                        startActivity(intent);
                                        break;
                                    case 10: // report
                                        new insertOrderLine().execute(
                                                String.valueOf(newTransac_id),
                                                foodID.getText().toString(),
                                                String.valueOf(qty),
                                                price.getText().toString(),
                                                foodName.getText().toString());
                                        startActivity(intent);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
                break;
        }
    }

    public final class insertOrderLine extends AsyncTask<String, Void, String> {

        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
//        Integer transacID;
//
//        public insertOrderLine(Integer transac_ID) {
//            this.transacID = transac_ID;
//        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(FoodDetails.this, "Adding...", null, true, true);
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
            data.put(KEY_FOOD_ID, params[1]);
            data.put(KEY_ITEMQTY, params[2]);
            data.put(KEY_SINGLE_PRICE, params[3]);
            data.put(KEY_FOOD_NAME, params[4]);

            return rh.sendPostRequest(INSERT_URL, data);
        }
    }
}
