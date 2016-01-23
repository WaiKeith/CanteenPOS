package my.edu.chiawaikeith.canteenpos.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import my.edu.chiawaikeith.canteenpos.Domains.Accounts;
import my.edu.chiawaikeith.canteenpos.R;

public class ForgotPassword extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

        private Toolbar toolBar;
        String mText;
        private String spinnerSelected = "Pet name";
        private String userID = "";
        private String answer = "";

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        toolBar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.title_forgot_password);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinnerPassword = (Spinner) findViewById(R.id.spinnerQues);
        spinnerPassword.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.ques, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPassword.setAdapter(adapter);

        Intent intent = getIntent();
    }


@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerSelected = parent.getItemAtPosition(position).toString();
        mText = spinnerSelected;
        }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        }

    public void confirmAns(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        EditText editText1 = (EditText) findViewById(R.id.editID);
        EditText editText2 = (EditText) findViewById(R.id.editAnswer);

        userID = editText1.getText().toString();
        answer = editText2.getText().toString();
        //passwordagn = editText3.getText().toString();


        if (userID.isEmpty() || answer.isEmpty()) {
        Toast.makeText(getApplicationContext(), "Please fill in all required fields!", Toast.LENGTH_LONG).show();
        } else {

        Accounts userRecord = new Accounts();

        //UserDataSource userDataSource = new UserDataSource(this);
        //userDataSource.insertUser(userRecord);

        //intent.putExtras(bundle);
        //startActivity(getIntent());
        startActivity(intent);
        }
     }

public void reset(View view){
        EditText editText1 = (EditText) findViewById(R.id.editID);
        EditText editText2 = (EditText) findViewById(R.id.editAnswer);
        //EditText editText3 = (EditText) findViewById(R.id.editPW);

        editText1.setText(null);
        editText2.setText(null);
        //editText3.setText(null);
        }
}
