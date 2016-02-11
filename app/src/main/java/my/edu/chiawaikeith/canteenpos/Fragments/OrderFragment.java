package my.edu.chiawaikeith.canteenpos.Fragments;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import my.edu.chiawaikeith.canteenpos.Activities.BaseActivity;
import my.edu.chiawaikeith.canteenpos.Activities.NFCActivity;
import my.edu.chiawaikeith.canteenpos.Activities.TransactionList;
import my.edu.chiawaikeith.canteenpos.Adapters.FoodAdapter;
import my.edu.chiawaikeith.canteenpos.Domains.Foods;
import my.edu.chiawaikeith.canteenpos.Domains.Transactions;
import my.edu.chiawaikeith.canteenpos.Fab;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;


public class OrderFragment extends BaseFragment implements View.OnClickListener {
    private String mText,sText,bText,status="Success";
    private Double a,b,g,s1,s2;
    private Double gst = 0.06,tPrice=0.00,totalgst=0.00;
    private Integer acc_id,transac_id,newTransac_id,f;
    private Date orderDate;
    private Spinner spinnerQuantity;
    NfcAdapter nfcAdapter;
    FoodAdapter foodAdapter;
    private TextView price,totalPrice,qty,aPrice,sub1,sub2,totalGST,foodID,foodID2;
    private Button write,cal,order,start,addCart1,addCart2,next;
    private ImageView d,e;
    Calendar calendar;
    Transactions transaction = new Transactions();
    private RecyclerView recyclerView;
    private JSONArray mJsonArray;
    private ArrayList<Foods> foodList = new ArrayList<>();
    private MaterialSheetFab materialSheetFab;
    boolean mAndroidBeamAvailable  = false;

    final static String INSERT_URL1 = "http://canteenpos.comxa.com/Transactions/insert_transaction.php";
    final static String INSERT_URL2 = "http://canteenpos.comxa.com/OrderLines/insert_order_line.php";
    final static String INSERT_URL3 = "http://canteenpos.comxa.com/Transactions/start_transaction.php";
    final static String GET_URL = "http://canteenpos.comxa.com/Transactions/get_transaction.php";
    final static String GETFOOD_URL = "http://dinpos.comlu.com/Foods/retrieve_foods.php";
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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OrderFragment() {
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
        loadFoods();
        //getTransaction();
        //beginTransaction();
    }

    public void startView() {
        FoodAdapter foodAdapter;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        foodAdapter = new FoodAdapter(getActivity(), foodList, R.layout.view_food_row);
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
        protected void onPostExecute(String json1) {
            super.onPostExecute(json1);
            Log.d("ReminderList", json1);
            //loading.dismiss();
            convertJson1(json1);
            extractJsonData1();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            return rh.sendPostRequest(GETFOOD_URL, data);
        }
    }

    // parse JSON data into JSON array
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
            shortToast(getActivity(),"No records found.");}
    }

    private void initValues() {
        acc_id = new BaseActivity().getLoginDetail(getActivity()).getAcc_id();
    }

    public void getTransaction(){
        new getTransaction().execute();
    }

    private void beginTransaction(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Begin Transaction");
        builder.setMessage("Confirm to start transaction?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_LONG).show();

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
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

//        spinnerQuantity = (Spinner)view.findViewById(R.id.spinnerQty);
//        //spinnerqty = (Spinner)view.findViewById(R.id.spinnerQty1);
//
//        Integer[] values =
//                {1, 2, 3, 4, 5};
//        ArrayAdapter<Integer> Qtyadapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, values);
//        Qtyadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        spinnerQuantity.setAdapter(Qtyadapter);
//
//        d = (ImageView)view.findViewById(R.id.imagePlus);
//        d.setOnClickListener(this);
//        e = (ImageView)view.findViewById(R.id.imageMinus);
//        e.setOnClickListener(this);
//
//        write = (Button)view.findViewById(R.id.btnWrite);
//        write.setOnClickListener(this);
//
//        //cal = (Button)view.findViewById(R.id.btnCal);
//        //cal.setOnClickListener(this);
//
//        addCart1 = (Button)view.findViewById(R.id.btnAddcart1);
//        addCart1.setOnClickListener(this);
//
//        addCart2 = (Button)view.findViewById(R.id.btnAddcart2);
//        addCart2.setOnClickListener(this);
//
////        order = (Button)view.findViewById(R.id.btnConfirm);
////        order.setOnClickListener(this);
//
//        //start = (Button)view.findViewById(R.id.btnStart);
//        //start.setOnClickListener(this);
//
//        price = (TextView)view.findViewById(R.id.tvPrice);
//        sText = price.getText().toString();
//
//        totalPrice = (TextView)view.findViewById(R.id.textviewTotalPrice);
//        totalPrice.setText(tPrice.toString());
//
//        totalGST = (TextView)view.findViewById(R.id.textviewGSTPrice);
//        totalGST.setText(totalgst.toString());
//
//        sub1 = (TextView)view.findViewById(R.id.subtotal1);
//        //s1 = Integer.parseInt(sub1.getText().toString());
//        sub2 = (TextView)view.findViewById(R.id.subtotal2);
//        //s2 = Integer.parseInt(sub2.getText().toString());
//
//        aPrice = (TextView)view.findViewById(R.id.singlePrice);
//        g = Double.parseDouble(aPrice.getText().toString());
//
//        qty = (TextView)view.findViewById(R.id.textQty);
//        f = Integer.parseInt(qty.getText().toString());
//
//        foodID = (TextView)view.findViewById(R.id.tvFoodID);
//        foodID2 = (TextView)view.findViewById(R.id.textView9);
//
//        mText = spinnerQuantity.getSelectedItem().toString();

        nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
//        next = (Button)view.findViewById(R.id.btnNextPage);
//        next.setOnClickListener(this);

        Fab fab = (Fab) view.findViewById(R.id.fab);
        View sheetView = view.findViewById(R.id.fab_sheet);
        View overlay = view.findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.cardview_light_background);
        int fabColor = getResources().getColor(R.color.accentColor);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet item click listeners
        view.findViewById(R.id.fab_sheet_item_calculate).setOnClickListener(this);
        view.findViewById(R.id.fab_sheet_item_confirm).setOnClickListener(this);
        view.findViewById(R.id.fab_sheet_item_start).setOnClickListener(this);
        view.findViewById(R.id.fab_sheet_item_next).setOnClickListener(this);

        recyclerView = (RecyclerView)view. findViewById(R.id.food_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }

    @Override
    public void onResume() {
        super.onResume();
        //spinnerQuantity.setOnItemSelectedListener(new MyOnItemSelectedListener());

        enableForegroundDispatchSystem();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.imagePlus:
//                f++;
//                qty.setText(f.toString());
//                s1 = f * g;
//                sub1.setText(s1.toString());
//
//                break;
//
//            case R.id.imageMinus:
//                f--;
//                qty.setText(f.toString());
//                s1 = f * g;
//                sub1.setText(s1.toString());
//
//                break;

//            case R.id.btnCal:
//                tPrice = s1 + s2;
//                totalPrice.setText(tPrice.toString());
//
//                totalgst = tPrice * gst;
//                totalGST.setText(totalgst.toString());
//                break;

//            case R.id.btnConfirm:
//                Intent intent = new Intent(getActivity(), OrderList.class);
//
//
//                calendar = Calendar.getInstance();
//                orderDate = calendar.getTime();
//
//                new insertOrder().execute(
//                        String.valueOf(transaction.getTransac_id()),
//                        String.valueOf(acc_id),
//                        totalPrice.getText().toString(),
//                        totalGST.getText().toString(),
//                        orderDate.toString(),
//                        status.toString());
//
//                //Log.d("transacid3",String.valueOf(transaction.getTransac_id()));
//
//                //transaction.setTransac_id(transac_id);
//
//                startActivity(intent);
//                break;

//            case R.id.btnAddcart1:
//                Log.d("newid",String.valueOf(newTransac_id));
//
//                new insertOrderLine().execute(
//                        String.valueOf(newTransac_id),
//                        foodID.getText().toString(),
//                        qty.getText().toString());
//                break;
//
//            case R.id.btnAddcart2:
//                new insertOrderLine().execute(
//                        String.valueOf(newTransac_id),
//                        foodID2.getText().toString(),
//                        mText.toString());
//                break;

//            case R.id.btnStart:
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

            case R.id.fab_sheet_item_next:
                Intent intent1 = new Intent(getActivity(), NFCActivity.class);
                startActivity(intent1);
                break;

            case R.id.fab_sheet_item_calculate:
                tPrice = s1 + s2;
                totalPrice.setText(tPrice.toString());

                totalgst = tPrice * gst;
                totalGST.setText(totalgst.toString());
                break;

            case R.id.fab_sheet_item_start:
                transac_id = transaction.getTransac_id();

                Log.d("tranid",String.valueOf(transac_id));
                newTransac_id = transac_id + 1;
                transaction.setTransac_id(newTransac_id);

                Log.d("transac_id2",String.valueOf(transac_id));

                new newTransaction().execute(
                        String.valueOf(newTransac_id),
                        String.valueOf(acc_id)
                );
                break;

            case R.id.fab_sheet_item_confirm:
                Intent intent2 = new Intent(getActivity(), TransactionList.class);


                calendar = Calendar.getInstance();
                orderDate = calendar.getTime();

                new insertOrder().execute(
                        String.valueOf(transaction.getTransac_id()),
                        String.valueOf(acc_id),
                        totalPrice.getText().toString(),
                        totalGST.getText().toString(),
                        orderDate.toString(),
                        status.toString());

                //Log.d("transacid3",String.valueOf(transaction.getTransac_id()));

                //transaction.setTransac_id(transac_id);

                startActivity(intent2);
                break;

        }

//        s1 = f * g;
//        sub1.setText(s1.toString());
    }

    protected void onNewIntent(Intent newIntent) {
        //super.onNewIntent(newIntent);

        if (newIntent.hasExtra(NfcAdapter.EXTRA_TAG)){
            Toast.makeText(getActivity(), "nfcIntent!", Toast.LENGTH_SHORT).show();

            Tag tag = newIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            NdefMessage ndefMessage = createNdefMessage(tPrice.toString());

            writeNdefMessage(tag, ndefMessage);
        }
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
            loading = ProgressDialog.show(getActivity(), "Uploading...", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<>();

            data.put(KEY_TRANSAC_ID, params[0]);
            data.put(KEY_ACCOUNT_ID, params[1]);

            return rh.sendPostRequest(INSERT_URL3, data);
        }
    }

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
            loading = ProgressDialog.show(getActivity(), "Uploading...", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();
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

            return rh.sendPostRequest(INSERT_URL1, data);
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
            loading = ProgressDialog.show(getActivity(), "Uploading...", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<>();

            data.put(KEY_TRANSAC_ID, params[0]);
            data.put(KEY_FOOD_ID, params[1]);
            data.put(KEY_ITEMQTY, params[2]);

            return rh.sendPostRequest(INSERT_URL2, data);
        }
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mText = parent.getItemAtPosition(position).toString();
            b = Double.parseDouble(sText);
            a = Double.parseDouble(mText);

            s2 = b * a;
            bText = s2.toString();
            sub2.setText(bText);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void enableForegroundDispatchSystem(){
        Intent intent2 = new Intent(getActivity(),OrderFragment.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent2, 0);
        IntentFilter[] intentFilters = new IntentFilter[] {};
        nfcAdapter.enableForegroundDispatch(getActivity(), pendingIntent, intentFilters,null );

    }

    private void disableForegroundDispatchSystem(){ nfcAdapter.disableForegroundDispatch(getActivity());}

    private void formatTag(Tag tag, NdefMessage ndefMessage){
        try{
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null){
                Toast.makeText(getActivity(), "Tag is not NdefFormattable!", Toast.LENGTH_SHORT).show();
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(getActivity(), "Tag written", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Log.e("formatTag", e.getMessage());
        }
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage){
        try{
            if(tag == null){
                Toast.makeText(getActivity(), "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);
            if (ndef == null){
                formatTag(tag, ndefMessage);
            }
            else {
                ndef.connect();

                if (!ndef.isWritable()){
                    Toast.makeText(getActivity(), "Tag is not writable", Toast.LENGTH_SHORT).show();
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }
}
