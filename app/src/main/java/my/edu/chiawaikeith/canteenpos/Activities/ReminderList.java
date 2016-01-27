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

import my.edu.chiawaikeith.canteenpos.Adapters.ReminderAdapter;
import my.edu.chiawaikeith.canteenpos.Domains.Reminders;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;


public class ReminderList extends BaseActivity implements View.OnClickListener{

    private static final String RETRIEVEREMINDER_URL = "http://canteenpos.comxa.com/Reminders/retrieve_reminder.php";
    private final static String KEY_REMINDER_ID = "reminder_id";
    private final static String KEY_ACC_ID = "acc_id";
    private final static String KEY_REMINDER_TITLE = "reminder_title";
    private final static String KEY_REMINDER_DESC = "reminder_desc";
    private final static String KEY_REMINDER_DATE = "reminder_date";
    private final static String KEY_REMINDER_TIME = "reminder_time";

    private RecyclerView recyclerView;
    private int acc_id;
    private JSONArray mJsonArray;
    private ArrayList<Reminders> reminderList = new ArrayList<>();
    private Toolbar toolBar;
    ReminderAdapter reminderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);

        toolBar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.title_reminder_list);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.reminder_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initValues();
        loadReminder();
    }

    public void initValues(){
        acc_id = new BaseActivity().getLoginDetail(this).getAcc_id();
    }

    public void startView() {
        ReminderAdapter reminderAdapter;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderAdapter = new ReminderAdapter(ReminderList.this, reminderList, R.layout.view_reminder_row);
        recyclerView.setAdapter(reminderAdapter);
    }

    public void loadReminder() {
        new getReminder().execute();
    }

    public class getReminder extends AsyncTask<String, Void, String> {
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
            Log.d("ReminderList", json);
            //loading.dismiss();
            convertJson(json);
            extractJsonData();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put(KEY_ACC_ID, String.valueOf(acc_id));
            return rh.sendPostRequest(RETRIEVEREMINDER_URL, data);
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
                Reminders reminders = new Reminders();

                try {
                    reminders.setReminder_id((jsonObject.getInt(KEY_REMINDER_ID)));
                    reminders.setAcc_id((jsonObject.getInt(KEY_ACC_ID)));
                    reminders.setReminder_title(jsonObject.getString(KEY_REMINDER_TITLE));
                    reminders.setReminder_desc(jsonObject.getString(KEY_REMINDER_DESC));
                    reminders.setReminder_date(mySqlDateFormat.parse(jsonObject.getString(KEY_REMINDER_DATE)));
                    reminders.setReminder_time(jsonObject.getString(KEY_REMINDER_TIME));

                    reminderList.add(reminders);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_history, menu);
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
            case R.id.buttonClear:
                reminderAdapter.clearAdapter();
        }
    }
}
