package my.edu.chiawaikeith.canteenpos.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import my.edu.chiawaikeith.canteenpos.Domains.Accounts;
import my.edu.chiawaikeith.canteenpos.Domains.OfflineLogin;
import my.edu.chiawaikeith.canteenpos.Fragments.AboutUsFragment;
import my.edu.chiawaikeith.canteenpos.Fragments.HistoryFragment;
import my.edu.chiawaikeith.canteenpos.Fragments.HomeFragment;
import my.edu.chiawaikeith.canteenpos.Fragments.OrderFragment;
import my.edu.chiawaikeith.canteenpos.Fragments.ProfileFragment;
import my.edu.chiawaikeith.canteenpos.Fragments.ReminderFragment;
import my.edu.chiawaikeith.canteenpos.R;

import static my.edu.chiawaikeith.canteenpos.R.id.frame;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigation;
    ImageView profilePic;
    private Accounts account;
    private OfflineLogin offlineLogin;
    TextView textUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textUsername = (TextView)findViewById(R.id.name);


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
                        getSupportActionBar().setTitle(R.string.reminders_fragment);
                        //Fragment reminderFragment = new ReminderFragment();
                        //fragmentTransaction.replace(R.id.frame, reminderFragment);
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

        initValue();

        //calling sync state is necessay or else your hamburger icon wont show up
        mActionBarDrawerToggle.syncState();
    }

    private void initValue() {
        //textUsername.setText(new BaseActivity().getLoginDetail(this).getUser_name());
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


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.image_profile:
//                //case R.id.text_name:
//                OfflineLogin offlineLogin = getLoginDetail();
//                if (offlineLogin != null) {
//                    Log.d("MainActivity","a");
//                    // pass news data to view college news activity
//                    ProfileFragment profileFragment = new ProfileFragment();
////                    details.setArguments(getIntent().getExtras());
////                    getSupportFragmentManager().beginTransaction().add(
////                            android.R.id.content, details).commit();
//                    Bundle bundle = new Bundle();
//                    accID = String.valueOf(offlineLogin.getAcc_id());
//                    bundle.putString("KEY_ACCOUNT_ID",accID);
//
//                    profileFragment.setArguments(bundle);
////                    Intent intent = new Intent(this, ProfileFragment.class);
////                    intent.putExtra();
////                    startActivity(intent);
//                } else {
//                    drawerLayout.closeDrawers();
//                    break;
//                }
//                drawerLayout.closeDrawers();
//                break;
//        }
//    }
}