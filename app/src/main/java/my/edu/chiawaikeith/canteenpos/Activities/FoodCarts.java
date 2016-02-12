package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import my.edu.chiawaikeith.canteenpos.Adapters.FoodAdapter;
import my.edu.chiawaikeith.canteenpos.Adapters.OrderLinesAdapter;
import my.edu.chiawaikeith.canteenpos.Domains.Foods;
import my.edu.chiawaikeith.canteenpos.Domains.OrderLines;
import my.edu.chiawaikeith.canteenpos.Domains.Transactions;
import my.edu.chiawaikeith.canteenpos.Fab;
import my.edu.chiawaikeith.canteenpos.Fragments.OrderFragment;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;

public class FoodCarts extends BaseActivity implements View.OnClickListener {
    private String qty1,qty2,qty3,qty4,eText,status="Success";
    private Double a1,a2,b1,b2,c1,c2,d1,d2,s1,s2,s3,s4,aText,cText,bText,dText;
    private Double gst = 0.06,tPrice=0.00,totalgst=0.00;
    public int acc_id,transacID;
    private Date orderDate;
    //private Spinner spinner1,spinner2,spinner3,spinner4;
    NfcAdapter nfcAdapter;
    FoodAdapter foodAdapter;
    private TextView price1,price2,price3,price4,sub1,sub2,sub3,sub4;
    private TextView totalPrice,totalGST,foodID1,foodID2,foodID3,foodID4;
    //private Button addCart1,addCart2,addCart3,addCart4;
    private Toolbar toolBar;

    Calendar calendar;
    private ArrayList<OrderLines> orderList = new ArrayList<>();
    private Transactions transaction = new Transactions();
    OrderLines totalRecord;
    //OrderLines orderLine = new OrderLines();
    private Foods food = new Foods();
    public static final String KEY_FOOD = "food";
    private RecyclerView recyclerView;
    private JSONArray mJsonArray;
    private ArrayList<Foods> foodList = new ArrayList<>();
    private MaterialSheetFab materialSheetFab;
    boolean mAndroidBeamAvailable  = false;

    final static String INSERT_URL = "http://dinpos.comlu.com/Transactions/insert_transaction.php";
    final static String INSERT_URL2 = "http://dinpos.comlu.com/OrderLines/insert_order_line.php";
    final static String INSERT_URL3 = "http://dinpos.comlu.com/Transactions/start_transaction.php";
    final static String GETORDERLINES_URL = "http://dinpos.comlu.com/OrderLines/retrieve_order_line.php";
    //final static String GETFOOD_URL = "http://canteenpos.comxa.com/Foods/retrieve_foods.php";
    final static String KEY_ACCOUNT_ID = "acc_id";
    final static String KEY_TRANSAC_ID = "transac_id";
    final static String KEY_FOOD_ID = "food_id";
    final static String KEY_PAYMENT_AMOUNT = "payment_amount";
    final static String KEY_ORDER_DATETIME = "order_date_time";
    final static String KEY_TOTAL_GST = "total_gst";
    final static String KEY_ORDER_STATUS = "order_status";
    final static String KEY_ITEMQTY = "item_qty";
    final static String KEY_ORDERLINE_ID = "order_line_id";
    final static String KEY_FOOD_NAME = "food_name";
    final static String KEY_FOOD_CATEGORY = "food_category";
    final static String KEY_FOOD_PRICE = "food_price";
    final static String KEY_SINGLE_PRICE = "single_price";
    final static String KEY_FOOD_IMAGE = "food_image_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_carts);

//        spinner1 = (Spinner)findViewById(R.id.spinnerQty1);
//        spinner2 = (Spinner)findViewById(R.id.spinnerQty2);
//        spinner3 = (Spinner)findViewById(R.id.spinnerQty3);
//        spinner4 = (Spinner)findViewById(R.id.spinnerQty4);

//        Integer[] values =
//                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//        ArrayAdapter<Integer> Qtyadapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, values);
//        Qtyadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        spinner1.setAdapter(Qtyadapter);
//        spinner2.setAdapter(Qtyadapter);
//        spinner3.setAdapter(Qtyadapter);
//        spinner4.setAdapter(Qtyadapter);

        //cal = (Button)view.findViewById(R.id.btnCal);
        //cal.setOnClickListener(this);

//        addCart1 = (Button)findViewById(R.id.btnAddcart1);
//        addCart1.setOnClickListener(this);
//
//        addCart2 = (Button)findViewById(R.id.btnAddcart2);
//        addCart2.setOnClickListener(this);
//
//        addCart2 = (Button)findViewById(R.id.btnAddcart3);
//        addCart2.setOnClickListener(this);
//
//        addCart2 = (Button)findViewById(R.id.btnAddcart4);
//        addCart2.setOnClickListener(this);

        toolBar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.title_item_cart);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        price1 = (TextView)findViewById(R.id.singlePrice);
//        price2 = (TextView)findViewById(R.id.singlePrice2);
//        price3 = (TextView)findViewById(R.id.singlePrice3);
//        price4 = (TextView)findViewById(R.id.singlePrice4);

        totalPrice = (TextView)findViewById(R.id.textviewTotalPrice);
        totalPrice.setText(tPrice.toString());

        totalGST = (TextView)findViewById(R.id.textviewGSTPrice);
        totalGST.setText(totalgst.toString());

//        sub1 = (TextView)findViewById(R.id.subtotal1);
        //s1 = Integer.parseInt(sub1.getText().toString());
//        sub2 = (TextView)findViewById(R.id.subtotal2);
//        //s2 = Integer.parseInt(sub2.getText().toString());
//        sub3 = (TextView)findViewById(R.id.subtotal3);
//        sub4 = (TextView)findViewById(R.id.subtotal4);


//        foodID1 = (TextView)findViewById(R.id.tvFoodID);
//        foodID2 = (TextView)findViewById(R.id.tvFoodID2);
//        foodID3 = (TextView)findViewById(R.id.tvFoodID3);
//        foodID4 = (TextView)findViewById(R.id.tvFoodID4);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Fab fab = (Fab)findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.cardview_light_background);
        int fabColor = getResources().getColor(R.color.accentColor);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet item click listeners
        //findViewById(R.id.fab_sheet_item_start).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_calculate).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_confirm).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_next).setOnClickListener(this);

        food = (Foods) getIntent().getSerializableExtra(FoodAdapter.KEY_FOOD);
        transaction = (Transactions) getIntent().getSerializableExtra(OrderFragment.KEY_TRANSAC);


        recyclerView = (RecyclerView) findViewById(R.id.order_line_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initValues();
        getOrderLines();
//        getTransaction();
//        beginTransaction();
    }

    private void startView() {
//        tvTransacID.setText(String.valueOf(transaction.getTransac_id()));
//        tvPayment.setText(String.valueOf(transaction.getPayment_amount()));
//        tvTotalGST.setText(String.valueOf(transaction.getTotal_gst()));
//        tvDate.setText(mySqlDateTimeFormat.format(transaction.getOrder_date_time()));
//        tvStatus.setText(transaction.getOrder_status());

        OrderLinesAdapter orderLinesAdapter;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderLinesAdapter = new OrderLinesAdapter(FoodCarts.this, orderList, R.layout.view_cart_line);
        recyclerView.setAdapter(orderLinesAdapter);
    }

    private void initValues() {
        acc_id = new BaseActivity().getLoginDetail(this).getAcc_id();
        transacID = transaction.getTransac_id();
    }

    @Override
    public void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableForegroundDispatchSystem();
    }

//    public void getTransaction(){
//        new getTransaction().execute();
//    }
//
//    private void beginTransaction(){
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Begin Transaction");
//        builder.setMessage("Confirm to start transaction?");
//        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
//
//                transac_id = transaction.getTransac_id();
//
//                Log.d("tranid", String.valueOf(transac_id));
//                newTransac_id = transac_id + 1;
//                transaction.setTransac_id(newTransac_id);
//
//                Log.d("transac_id2", String.valueOf(transac_id));
//
//                new newTransaction().execute(
//                        String.valueOf(newTransac_id),
//                        String.valueOf(acc_id)
//                );
//                dialog.dismiss();
//            }
//        });
//        builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_carts, menu);
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
        switch (v.getId()){
            case R.id.fab_sheet_item_next:
                Intent intent1 = new Intent(this, NFCActivity.class);
                startActivity(intent1);
                break;

            case R.id.fab_sheet_item_calculate:
                totalPrice.setText(String.valueOf(totalRecord.getTotal_price()));

                totalgst = totalRecord.getTotal_price()
                        * gst;
                totalGST.setText(totalgst.toString());
                break;

//            case R.id.fab_sheet_item_start:
//                transac_id = transaction.getTransac_id();
//
//                Log.d("tranid",String.valueOf(transac_id));
//                newTransac_id = transac_id + 1;
//                transaction.setTransac_id(newTransac_id);
//
//                Log.d("transac_id2",String.valueOf(transac_id));
//
//                new newTransaction().execute(
//                        String.valueOf(newTransac_id),
//                        String.valueOf(acc_id)
//                );
//                break;

            case R.id.fab_sheet_item_confirm:
                Intent intent2 = new Intent(this, TransactionList.class);


                calendar = Calendar.getInstance();
                orderDate = calendar.getTime();

                new insertOrder().execute(
                        String.valueOf(transacID),
                        String.valueOf(acc_id),
                        totalPrice.getText().toString(),
                        totalGST.getText().toString(),
                        orderDate.toString(),
                        status.toString());

                //Log.d("transacid3",String.valueOf(transaction.getTransac_id()));

                //transaction.setTransac_id(transac_id);

                startActivity(intent2);
                break;

//            case R.id.btnAddcart1:
//                Log.d("newid",String.valueOf(newTransac_id));
//
//                new insertOrderLine().execute(
//                        String.valueOf(newTransac_id),
//                        foodID1.getText().toString(),
//                        qty1.toString());
//                break;
//
//            case R.id.btnAddcart2:
//                new insertOrderLine().execute(
//                        String.valueOf(newTransac_id),
//                        foodID2.getText().toString(),
//                        qty2.toString());
//                break;
//
//            case R.id.btnAddcart3:
//                new insertOrderLine().execute(
//                        String.valueOf(newTransac_id),
//                        foodID3.getText().toString(),
//                        qty3.toString());
//                break;
//
//            case R.id.btnAddcart4:
//                new insertOrderLine().execute(
//                        String.valueOf(newTransac_id),
//                        foodID4.getText().toString(),
//                        qty4.toString());
//                break;
        }
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
            loading = ProgressDialog.show(FoodCarts.this, "Retrieving...", null, true, true);
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
            data.put(KEY_TRANSAC_ID, String.valueOf(transacID));
            //Log.d("transacidfoodcart",String.valueOf(transacID));

            return rh.sendPostRequest(GETORDERLINES_URL, data);
        }
    }

    private void convertJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            mJsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
            //Log.d("foodcartarray", String.valueOf(mJsonArray));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void extractJsonData() {
        totalRecord = new OrderLines();
        double totalPrice=0,subTotal=0,qty=0,singlePrice=0;
        for (int i = 0; i < mJsonArray.length(); i++) {
            OrderLines orderLine = new OrderLines();
            try {
                JSONObject jsonObject = mJsonArray.getJSONObject(i);

                Foods food = new Foods();

                try{
                    orderLine.setTransac_id(jsonObject.getInt(KEY_TRANSAC_ID));
                    orderLine.setFood_id(jsonObject.getInt(KEY_FOOD_ID));
                    orderLine.setOrder_line_id(jsonObject.getInt(KEY_ORDERLINE_ID));
                    orderLine.setItem_qty(jsonObject.getInt(KEY_ITEMQTY));
                    orderLine.setSingle_price(jsonObject.getDouble(KEY_SINGLE_PRICE));

                    orderList.add(orderLine);
                    //foodList.add(food);
                    //Log.d("foodname", food.getFood_name());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            singlePrice = orderLine.getSingle_price();
            qty = Double.parseDouble(String.valueOf(orderLine.getItem_qty()));
            subTotal = singlePrice * qty;
            totalPrice += subTotal;
            totalRecord.setTotal_price(totalPrice);

        }
        if(mJsonArray.length() > 0) {
            startView();
        }
    }

    protected void onNewIntent(Intent newIntent) {
        //super.onNewIntent(newIntent);

        if (newIntent.hasExtra(NfcAdapter.EXTRA_TAG)){
            Toast.makeText(this, "nfcIntent!", Toast.LENGTH_SHORT).show();

            Tag tag = newIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            NdefMessage ndefMessage = createNdefMessage(tPrice.toString());

            writeNdefMessage(tag, ndefMessage);
        }
    }

//    public class getTransaction extends AsyncTask<String, Void, String> {
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
//            convertJson(json);
//            extractJsonData();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            HashMap<String, String> data = new HashMap<>();
//            data.put(KEY_ACCOUNT_ID,  String.valueOf(acc_id));
//
//            return rh.sendPostRequest(GET_URL, data);
//        }
//    }
//
//    private void convertJson(String json) {
//        try {
//            JSONObject jsonObject = new JSONObject(json);
//            mJsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
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
//                //Transactions transactions = new Transactions();
//
//                transaction.setTransac_id(jsonObject.getInt(KEY_TRANSAC_ID));
//                transaction.setAcc_id(jsonObject.getInt(KEY_ACCOUNT_ID));
//                transaction.setPayment_amount(jsonObject.getDouble(KEY_PAYMENT_AMOUNT));
//                transaction.setTotal_gst(jsonObject.getDouble(KEY_TOTAL_GST));
//                transaction.setOrder_date_time(mySqlDateTimeFormat.parse(jsonObject.getString(KEY_ORDER_DATETIME)));
//                transaction.setOrder_status(jsonObject.getString(KEY_ORDER_STATUS));
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    public class newTransaction extends AsyncTask<String, Void, String> {
//        ProgressDialog loading;
//        RequestHandler rh = new RequestHandler();
////        Transactions transac;
////
////        public newTransaction(Transactions transactions){
////            this.transac = transactions;
////        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loading = ProgressDialog.show(FoodCarts.this, "Uploading...", null, true, true);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            loading.dismiss();
//            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            HashMap<String, String> data = new HashMap<>();
//
//            data.put(KEY_TRANSAC_ID, params[0]);
//            data.put(KEY_ACCOUNT_ID, params[1]);
//
//            return rh.sendPostRequest(INSERT_URL3, data);
//        }
//    }

    public class insertOrder extends AsyncTask<String, Void, String> {

        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
//        Integer acc_id;
//
//        public insertOrder(Integer accountId) {
//            this.acc_id = accountId;
//        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(FoodCarts.this, "Uploading...", null, true, true);
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
            data.put(KEY_PAYMENT_AMOUNT, params[2]);
            data.put(KEY_TOTAL_GST, params[3]);
            data.put(KEY_ORDER_DATETIME, params[4]);
            data.put(KEY_ORDER_STATUS, params[5]);

            return rh.sendPostRequest(INSERT_URL, data);
        }
    }

//    public final class insertOrderLine extends AsyncTask<String, Void, String> {
//
//        ProgressDialog loading;
//        RequestHandler rh = new RequestHandler();
////        Integer transacID;
////
////        public insertOrderLine(Integer transac_ID) {
////            this.transacID = transac_ID;
////        }
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loading = ProgressDialog.show(FoodCarts.this, "Uploading...", null, true, true);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            loading.dismiss();
//            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            HashMap<String, String> data = new HashMap<>();
//
//            data.put(KEY_TRANSAC_ID, params[0]);
//            data.put(KEY_FOOD_ID, params[1]);
//            data.put(KEY_ITEMQTY, params[2]);
//
//            return rh.sendPostRequest(INSERT_URL2, data);
//        }
//    }

//    public class MyOnItemSelectedListener1 implements AdapterView.OnItemSelectedListener {
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            qty1 = parent.getItemAtPosition(position).toString();
//            a1 = Double.parseDouble(qty1);
//            aText = Double.parseDouble(price1.getText().toString());
//            s1 = aText * a1;
//            Log.d("sub",s1.toString());
//            sub1.setText(s1.toString());
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    }

    private void enableForegroundDispatchSystem(){
        Intent intent2 = new Intent(this,FoodCarts.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent2, 0);
        IntentFilter[] intentFilters = new IntentFilter[] {};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters,null );

    }

    private void disableForegroundDispatchSystem(){ nfcAdapter.disableForegroundDispatch(this);}

    private void formatTag(Tag tag, NdefMessage ndefMessage){
        try{
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null){
                Toast.makeText(this, "Tag is not NdefFormattable!", Toast.LENGTH_SHORT).show();
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Tag written", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Log.e("formatTag", e.getMessage());
        }
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage){
        try{
            if(tag == null){
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);
            if (ndef == null){
                formatTag(tag, ndefMessage);
            }
            else {
                ndef.connect();

                if (!ndef.isWritable()){
                    Toast.makeText(this, "Tag is not writable", Toast.LENGTH_SHORT).show();
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
            }
        }catch(Exception e){
            Log.e("writeNdefMessage", e.getMessage());
        }
    }

    private NdefRecord createTextRecord(String content){
        try{
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");
            final byte[] text = content.getBytes("UTF-8");
            ////final byte[] text2 = content.getBytes("UTF-8");

//            final byte[] text = aText.getBytes();
//            final byte[] text2 = bText.getBytes();

            final int languageSize = language.length;
            final int textLength = text.length;
            ////final int text2Length = text2.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0 , languageSize);
            payload.write(text, 0, textLength);
            ////payload.write(text2, 1, text2Length);
            ////payload.write(language, 1 , languageSize);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

//            NdefRecord textRecord1 = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
//                    aText.getBytes(), new byte[]{}, text);
//            NdefRecord textRecord2 = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
//                    bText.getBytes(), new byte[]{}, text2);

        }catch(Exception e){
            Log.e("createTextRecord", e.getMessage());
        }

        return null;
    }

    private NdefMessage createNdefMessage(String content){
        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        return ndefMessage;
    }
}
