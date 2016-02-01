package my.edu.chiawaikeith.canteenpos.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;

import java.util.Calendar;
import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Activities.BaseActivity;
import my.edu.chiawaikeith.canteenpos.Activities.ReminderList;
import my.edu.chiawaikeith.canteenpos.Activities.ScheduleClient;
import my.edu.chiawaikeith.canteenpos.Domains.Reminders;
import my.edu.chiawaikeith.canteenpos.Fab;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;


public class ReminderFragment extends BaseFragment implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, View.OnTouchListener,View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    final static String INSERT_URL = "http://canteenpos.comxa.com/Reminders/insert_reminder.php";
    final static String KEY_ACCOUNT_ID = "acc_id";
    final static String KEY_TITLE = "reminder_title";
    final static String KEY_DESC = "reminder_desc";
    final static String KEY_DATE = "reminder_date";
    final static String KEY_TIME = "reminder_time";

    private Reminders reminder = new Reminders();
    private ScheduleClient scheduleClient;
    private EditText editDesc,timeTextView,dateTextView,editTitle;
    private FloatingActionButton fabAdd,fabChk;
    private int day;
    private int month;
    private int Year;
    private int hr;
    private int min;
    private int acc_id;
    public Calendar calendar;
    private MaterialSheetFab materialSheetFab;

    private OnFragmentInteractionListener mListener;

    public static ReminderFragment newInstance(String param1, String param2) {
        ReminderFragment fragment = new ReminderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        timeTextView = (EditText)view.findViewById(R.id.time_textview);
        timeTextView.setOnTouchListener(this);
        dateTextView = (EditText)view.findViewById(R.id.date_textview);
        dateTextView.setOnTouchListener(this);
        editDesc = (EditText)view.findViewById(R.id.editDesc);
        editTitle = (EditText)view.findViewById(R.id.editTitle);

        scheduleClient = new ScheduleClient(getActivity());
        scheduleClient.doBindService();

        Fab fab = (Fab) view.findViewById(R.id.fab);
        View sheetView = view.findViewById(R.id.fab_sheet);
        View overlay = view.findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.cardview_light_background);
        int fabColor = getResources().getColor(R.color.accentColor);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet item click listeners
        view.findViewById(R.id.fab_sheet_item_add).setOnClickListener(this);
        view.findViewById(R.id.fab_sheet_item_check).setOnClickListener(this);

        initValues();
        return view;
    }

    private void initValues() {
        // set the default date and time
        calendar = Calendar.getInstance();
        //  calendar.set(Calendar.HOUR_OF_DAY);
        calendar.set(Calendar.MINUTE, 0);

        // initiate current date and time into views
        dateTextView.setHint("Date");
        timeTextView.setHint("Time");

        acc_id = new BaseActivity().getLoginDetail(getActivity()).getAcc_id();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        String time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
//
//        hr = hourOfDay;
//        min = minute;
//        timeTextView.setText(time);

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        timeTextView.setText(timeFormat.format(calendar.getTime()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1) + "/" + String.valueOf(year);
//
//        //final Calendar calendar = Calendar.getInstance();
//        Year = year;
//        month = monthOfYear;
//        day = dayOfMonth;
//        dateTextView.setText(date);

        calendar.set(year, monthOfYear, dayOfMonth);
        dateTextView.setText(mySqlDateFormat.format(calendar.getTime()));

    }

    public void onDateSelectedButtonClick(View v){
        // Create a new calendar set to the date chosen
        // we set the time to midnight (i.e. the first minute of that day)
        Calendar c = Calendar.getInstance();
        c.set(Year,month,day,hr,min);

        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
        scheduleClient.setAlarmForNotification(c);
        // Notify the user what they just did
        Toast.makeText(getActivity(), "Notification set for: " + day + "/" + (month + 1) + "/" + Year
                + " ," + hr + ":" + min, Toast.LENGTH_SHORT).show();
    }

    public boolean onTouch(View view, MotionEvent event) {
        switch (view.getId()) {
            case R.id.date_textview:

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR)
                            , calendar.get(Calendar.MONTH)
                            , calendar.get(Calendar.DAY_OF_MONTH)).show();
                    //dateTextView.setText(mySqlDateFormat.format(calendar.getTime()));
                }
                break;
            case R.id.time_textview:

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    new TimePickerDialog(getActivity(), this, calendar.get(Calendar.HOUR_OF_DAY)
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
    public void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_sheet_item_add:
                Intent intent = new Intent(getActivity(),ReminderList.class);
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
                Log.d("date",dateTextView.getText().toString());
                Log.d("time",timeTextView.getText().toString());

//                EventDataSource eventDataSource = new EventDataSource(getActivity());
//                eventDataSource.insertEvent(eventRecord);
                startActivity(intent);

                break;

            case R.id.btnSelect:
                Calendar c = Calendar.getInstance();
                c.set(Year,month,day,hr,min);

                // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
                scheduleClient.setAlarmForNotification(c);
                // Notify the user what they just did
                Toast.makeText(getActivity(), "Notification set for: " + day + "/" + (month + 1) + "/" + Year
                        + " ," + hr + ":" + min, Toast.LENGTH_SHORT).show();
                break;

            case R.id.fab_sheet_item_check:
                Intent intent2 = new Intent(getActivity(),ReminderList.class);
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

            data.put(KEY_ACCOUNT_ID, params[0]);
            data.put(KEY_TITLE, params[1]);
            data.put(KEY_DESC, params[2]);
            data.put(KEY_DATE, params[3]);
            data.put(KEY_TIME, params[4]);

            return rh.sendPostRequest(INSERT_URL, data);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
