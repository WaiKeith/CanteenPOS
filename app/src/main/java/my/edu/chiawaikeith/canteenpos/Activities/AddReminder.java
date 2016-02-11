package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;

import java.util.Calendar;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Domains.Reminders;
import my.edu.chiawaikeith.canteenpos.Fab;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;

public class AddReminder extends BaseActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, View.OnTouchListener,View.OnClickListener{

    final static String INSERT_URL = "http://dinpos.comlu.com/Reminders/insert_reminder.php";
    final static String KEY_ACCOUNT_ID = "acc_id";
    final static String KEY_TITLE = "reminder_title";
    final static String KEY_DESC = "reminder_desc";
    final static String KEY_DATE = "reminder_date";
    final static String KEY_TIME = "reminder_time";

    private Reminders reminder = new Reminders();
    private ScheduleClient scheduleClient;
    private EditText editDesc,timeTextView,dateTextView,editTitle;
    private int acc_id;
    public Calendar calendar;
    private MaterialSheetFab materialSheetFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        timeTextView = (EditText)findViewById(R.id.time_textview);
        timeTextView.setOnTouchListener(this);
        dateTextView = (EditText)findViewById(R.id.date_textview);
        dateTextView.setOnTouchListener(this);
        editDesc = (EditText)findViewById(R.id.editDesc);
        editTitle = (EditText)findViewById(R.id.editTitle);

        Fab fab = (Fab) findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.cardview_light_background);
        int fabColor = getResources().getColor(R.color.accentColor);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet item click listeners
        findViewById(R.id.fab_sheet_item_add).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_check).setOnClickListener(this);

        initValues();
    }

    private void initValues() {
        // set the default date and time
        calendar = Calendar.getInstance();
        //  calendar.set(Calendar.HOUR_OF_DAY);
        calendar.set(Calendar.MINUTE, 0);

        // initiate current date and time into views
        dateTextView.setHint("Date");
        timeTextView.setHint("Time");

        acc_id = new BaseActivity().getLoginDetail(this).getAcc_id();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        timeTextView.setText(timeFormat.format(calendar.getTime()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        dateTextView.setText(mySqlDateFormat.format(calendar.getTime()));

    }

    public boolean onTouch(View view, MotionEvent event) {
        switch (view.getId()) {
            case R.id.date_textview:

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    new DatePickerDialog(this, this, calendar.get(Calendar.YEAR)
                            , calendar.get(Calendar.MONTH)
                            , calendar.get(Calendar.DAY_OF_MONTH)).show();
                    //dateTextView.setText(mySqlDateFormat.format(calendar.getTime()));
                }
                break;
            case R.id.time_textview:

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    new TimePickerDialog(this, this, calendar.get(Calendar.HOUR_OF_DAY)
                            , calendar.get(Calendar.MINUTE), true).show();
                    //timeTextView.setText(timeFormat.format(calendar.getTime()));
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_sheet_item_add:
                Intent intent = new Intent(this,ReminderList.class);
//                Reminders eventRecord = new Reminders();
//                eventRecord.date = dateTextView.getText().toString();
//                eventRecord.time = timeTextView.getText().toString();
//                eventRecord.desc = editDesc.getText().toString();

                new insertReminder().execute(
                        String.valueOf(acc_id),
                        editTitle.getText().toString(),
                        editDesc.getText().toString(),
                        dateTextView.getText().toString(),
                        timeTextView.getText().toString());
                Log.d("date", dateTextView.getText().toString());
                Log.d("time",timeTextView.getText().toString());

//                EventDataSource eventDataSource = new EventDataSource(getActivity());
//                eventDataSource.insertEvent(eventRecord);
                startActivity(intent);

                break;

//            case R.id.btnSelect:
//                Calendar c = Calendar.getInstance();
//                c.set(Year,month,day,hr,min);
//
//                // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
//                scheduleClient.setAlarmForNotification(c);
//                // Notify the user what they just did
//                Toast.makeText(getActivity(), "Notification set for: " + day + "/" + (month + 1) + "/" + Year
//                        + " ," + hr + ":" + min, Toast.LENGTH_SHORT).show();
//                break;

            case R.id.fab_sheet_item_check:
                Intent intent2 = new Intent(this,ReminderList.class);
                startActivity(intent2);
                break;
        }
    }

    public class insertReminder extends AsyncTask<String, Void, String> {

        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
//        Integer acc_id;
//
//        public insertReminder(Integer accountId) {
//            this.acc_id = accountId;
//        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(AddReminder.this, "Uploading...", null, true, true);
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

            data.put(KEY_ACCOUNT_ID, params[0]);
            data.put(KEY_TITLE, params[1]);
            data.put(KEY_DESC, params[2]);
            data.put(KEY_DATE, params[3]);
            data.put(KEY_TIME, params[4]);

            return rh.sendPostRequest(INSERT_URL, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
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
}
