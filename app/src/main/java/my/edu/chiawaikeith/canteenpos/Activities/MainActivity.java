package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import my.edu.chiawaikeith.canteenpos.Domains.Accounts;
import my.edu.chiawaikeith.canteenpos.Domains.OfflineLogin;
import my.edu.chiawaikeith.canteenpos.Fragments.AboutUsFragment;
import my.edu.chiawaikeith.canteenpos.Fragments.ChartFragment;
import my.edu.chiawaikeith.canteenpos.Fragments.HistoryFragment;
import my.edu.chiawaikeith.canteenpos.Fragments.HomeFragment;
import my.edu.chiawaikeith.canteenpos.Fragments.OrderFragment;
import my.edu.chiawaikeith.canteenpos.Fragments.ProfileFragment;
import my.edu.chiawaikeith.canteenpos.Fragments.ReminderFragment;
import my.edu.chiawaikeith.canteenpos.R;
import my.edu.chiawaikeith.canteenpos.RequestHandler;

import static my.edu.chiawaikeith.canteenpos.R.id.frame;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigation;
    private ImageView profilePic;
    Accounts account = new Accounts();
    private OfflineLogin offlineLogin;
    private TextView textUsername;
    private int acc_id;
    private JSONArray jsonArray;
    final static String KEY_USER_NAME = "user_name";
    final static String KEY_PROFILE_IMAGE_PATH = "profile_image_path";
    private static final String RETRIEVEACC_URL = "http://canteenpos.comxa.com/Accounts/Students/retrieve_account.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HomeFragment fragmentHome = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(frame, fragmentHome).commit();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

        navigation = (NavigationView) findViewById(R.id.navigation_view);
        navigation.setCheckedItem(R.id.navigation_item_1);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        getSupportActionBar().setTitle(R.string.app_name);
                        HomeFragment fragmentHome = new HomeFragment();
                        fragmentTransaction.replace(frame, fragmentHome);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_item_2:
                        getSupportActionBar().setTitle(R.string.user_profile_fragment);
                        ProfileFragment profileFragment = new ProfileFragment();
                        fragmentTransaction.replace(frame, profileFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.navigation_item_3:
                        getSupportActionBar().setTitle(R.string.orders_fragment);
                        OrderFragment orderFragment = new OrderFragment();
                        fragmentTransaction.replace(frame, orderFragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_item_4:
                        getSupportActionBar().setTitle(R.string.orders_history_fragment);
                        HistoryFragment historyFragment = new HistoryFragment();
                        fragmentTransaction.replace(frame, historyFragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_item_5:
                        getSupportActionBar().setTitle(R.string.reminders_fragment);
                        ReminderFragment reminderFragment = new ReminderFragment();
                        fragmentTransaction.replace(frame, reminderFragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_item_6:
                        getSupportActionBar().setTitle(R.string.charts_fragment);
                        ChartFragment chartFragment = new ChartFragment();
                        fragmentTransaction.replace(R.id.frame, chartFragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_item_7:
                        getSupportActionBar().setTitle(R.string.about_us_fragment);
                        AboutUsFragment aboutUsFragment = new AboutUsFragment();
                        fragmentTransaction.replace(frame, aboutUsFragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_item_8:
                        removeLoginDetail();
                        goToLogin();
                }
                return false;
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(mActionBarDrawerToggle);

        View headerView = navigation.inflateHeaderView(R.layout.navigation_header);

        textUsername = (TextView) headerView.findViewById(R.id.name);
        profilePic = (ImageView) headerView.findViewById(R.id.image_profile);
        textUsername.setOnClickListener(this);
        profilePic.setOnClickListener(this);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        initValues();
        loadAccount();

        //calling sync state is necessay or else your hamburger icon wont show up
        mActionBarDrawerToggle.syncState();
    }

    private void initValues() {
        acc_id = new BaseActivity().getLoginDetail(this).getAcc_id();
        textUsername.setText(new BaseActivity().getLoginDetail(this).getUser_name());
        if(new BaseActivity().getLoginDetail(this).getProfile_image_path() != ""){
            ImageLoader.getInstance().displayImage(new BaseActivity().getLoginDetail(this).getProfile_image_path(), profilePic, options);}
    }

    public void loadAccount() {
        new getAccount(acc_id).execute();
    }

    // this one is get json
    public class getAccount extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();
        Integer acc_ID;

        public getAccount(Integer accountId) {
            this.acc_ID = accountId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(getActivity(), "Loading...", "Please Wait...", true, true);
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            //loading.dismiss();
            Log.d("MainActivity", json);
            convertJson(json);
            extractJsonData();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("acc_id", String.valueOf(acc_ID));
            return rh.sendPostRequest(RETRIEVEACC_URL, data);
        }
    }

    // parse JSON data into JSON array
    private void convertJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
            Log.d("length",String.valueOf(jsonArray.length()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void extractJsonData() {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                account.setUser_name((jsonObject.getString(KEY_USER_NAME)));
                Log.d("name",account.getUser_name());
                account.setProfile_image_path(jsonObject.getString(KEY_PROFILE_IMAGE_PATH));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadAccountView();

        }
    }

    private void loadAccountView() {
        textUsername.setText(account.getUser_name());
        if(account.getProfile_image_path() != ""){
            ImageLoader.getInstance().displayImage(account.getProfile_image_path(), profilePic, options);}
    }

    private void goToLogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_profile:
            case R.id.name:
                OfflineLogin offlineLogin = getLoginDetail(MainActivity.this);
                FragmentTransaction fragmentTransaction1 =
                        getSupportFragmentManager().beginTransaction();
                if(offlineLogin != null) {
                    // pass news data to view college news activity

                    ProfileFragment profileFragment = new ProfileFragment();
                    fragmentTransaction1.replace(frame, profileFragment);
                    fragmentTransaction1.commit();
                } else {
                    drawerLayout.closeDrawers();
                    break;
                }


        }
    }
}