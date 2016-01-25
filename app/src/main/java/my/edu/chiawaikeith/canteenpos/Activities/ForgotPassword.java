package my.edu.chiawaikeith.canteenpos.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import my.edu.chiawaikeith.canteenpos.R;

public class ForgotPassword extends BaseActivity implements View.OnClickListener {

        private Toolbar toolBar;
        private String mText;
        private String spinnerSelected = "Pet name";
        private String userID = "",email = "";
        private Button btnProceed,btnReset;

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

        btnProceed = (Button)findViewById(R.id.buttonProceed);
        btnReset = (Button)findViewById(R.id.buttonReset);

       // Spinner spinnerPassword = (Spinner) findViewById(R.id.spinnerQues);
//        spinnerPassword.setOnItemSelectedListener(this);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//        R.array.ques, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerPassword.setAdapter(adapter);
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

    public void confirmAns(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        EditText editText1 = (EditText) findViewById(R.id.editID);

        userID = editText1.getText().toString();

        if (userID.isEmpty() || email.isEmpty()) {
        Toast.makeText(getApplicationContext(), "Please fill in all required fields!", Toast.LENGTH_LONG).show();
        } else {

        startActivity(intent);
        }
     }

public void reset(View view){
        EditText editText1 = (EditText) findViewById(R.id.editID);
        EditText editText2 = (EditText) findViewById(R.id.editEmail);
        //EditText editText3 = (EditText) findViewById(R.id.editPW);

        editText1.setText(null);
        editText2.setText(null);
        //editText3.setText(null);
        }

    @Override
    public void onClick(View v) {


    }
}
