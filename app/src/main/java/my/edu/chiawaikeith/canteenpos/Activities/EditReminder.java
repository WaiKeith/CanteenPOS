package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.software.shell.fab.ActionButton;

import java.util.Calendar;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Adapters.ReminderAdapter;
import my.edu.chiawaikeith.canteenpos.Domains.Reminders;
import my.edu.chiawaikeith.canteenpos.Fab;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;


public class EditReminder extends BaseActivity implements View.OnClickListener
                                         ,TimePickerDialog.OnTimeSetListener,
                                        DatePickerDialog.OnDateSetListener, View.OnTouchListener{

    final static String UPDATE_URL = "http://canteenpos.comxa.com/Reminders/update_reminder.php";
    final static String DELETE_URL = "http://canteenpos.comxa.com/Reminders/delete_reminder.php";
    final static String KEY_REMINDER_ID = "reminder_id";
    final static String KEY_ACCOUNT_ID = "acc_id";
    final static String KEY_TITLE = "reminder_title";
    final static String KEY_DESC = "reminder_desc";
    final static String KEY_DATE = "reminder_date";
    final static String KEY_TIME = "reminder_time";

    private EditText editTextDate,editTextTime,editTextDesc,editTextTitle;
    private Toolbar toolBar;
    private Integer acc_id,reminder_id;

    Context context;
    private ActionButton actionButton;
    private MaterialSheetFab materialSheetFab;
    Button buttonFlatSave,buttonFlatDelete;
    private Reminders reminder = new Reminders();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        toolBar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.title_edit_reminder);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        buttonFlatSave = (Button) findViewById(R.id.buttonflatSave);
//        buttonFlatDelete = (Button) findViewById(R.id.buttonflatDelete);

        editTextDate = (EditText) findViewById(R.id.date_textview);
        editTextDate.setOnTouchListener(this);
        editTextTime = (EditText) findViewById(R.id.time_textview);
        editTextTime.setOnTouchListener(this);
        editTextDesc = (EditText)findViewById(R.id.editDesc);
        editTextTitle = (EditText)findViewById(R.id.editTitle);
//        buttonFlatSave.setOnClickListener(this);
//        buttonFlatDelete.setOnClickListener(this);

        Fab fab = (Fab)findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.cardview_light_background);
        int fabColor = getResources().getColor(R.color.accentColor);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet item click listeners
        findViewById(R.id.fab_sheet_item_save).setOnClickListener(this);
        findViewById(R.id.fab_sheet_item_delete).setOnClickListener(this);

        reminder = (Reminders) getIntent().getSerializableExtra(ReminderAdapter.KEY_REMINDER);
        initValues();
    }

    public void initValues() {

        editTextTitle.setText(reminder.getReminder_title());
        editTextDesc.setText(reminder.getReminder_desc());

        calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);

        // initiate current date and time into views
        editTextDate.setHint(mySqlDateFormat.format(reminder.getReminder_date()));
        editTextTime.setHint(reminder.getReminder_time());

        acc_id = reminder.getAcc_id();
        reminder_id = reminder.getReminder_id();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        editTextTime.setText(timeFormat.format(calendar.getTime()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        editTextDate.setText(mySqlDateFormat.format(calendar.getTime()));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_event, menu);
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
        Intent intent = new Intent(this, ReminderList.class);

        switch (v.getId()) {
            case R.id.fab_sheet_item_save:

                new updateReminder().execute(
                        String.valueOf(reminder_id),
                        String.valueOf(acc_id),
                        editTextTitle.getText().toString(),
                        editTextDesc.getText().toString(),
                        editTextDate.getText().toString(),
                        editTextTime.getText().toString());

                startActivity(intent);

                break;

            case R.id.fab_sheet_item_delete:
                new deleteReminder().execute(
                    String.valueOf(reminder_id));

                startActivity(intent);

                break;
        }
    }

    public class updateReminder extends AsyncTask<String, Void, String> {

        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
//        Reminders reminder;
//
//        public updateReminder(Reminders reminder) {
//            this.reminder = reminder;
//        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(EditReminder.this, "Uploading...", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(EditReminder.this.getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<>();

            data.put(KEY_REMINDER_ID, params[0]);
            data.put(KEY_ACCOUNT_ID, params[1]);
            data.put(KEY_TITLE, params[2]);
            data.put(KEY_DESC, params[3]);
            data.put(KEY_DATE, params[4]);
            data.put(KEY_TIME, params[5]);

            return rh.sendPostRequest(UPDATE_URL, data);
        }
    }


    public class deleteReminder extends AsyncTask<String, Void, String> {

        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(ViewLostAndFoundActivity.this, "Deleting...", null, true, true);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //loading.dismiss();

            longToast(s);
            finish();
        }

        @Override
        protected String doInBackground(String... params) {

            HashMap<String,String> data = new HashMap<>();

            data.put(KEY_REMINDER_ID, params[0]);

            return rh.sendPostRequest(DELETE_URL, data);

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (view.getId()) {
            case R.id.date_textview:

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    new DatePickerDialog(this, this, calendar.get(Calendar.YEAR)
                            , calendar.get(Calendar.MONTH)
                            , calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                break;
            case R.id.time_textview:

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    new TimePickerDialog(this, this, calendar.get(Calendar.HOUR_OF_DAY)
                            , calendar.get(Calendar.MINUTE), true).show();
                }
                break;
            default:
                break;
        }
        return false;
    }
}
