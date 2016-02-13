package my.edu.chiawaikeith.canteenpos.Activities;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import my.edu.chiawaikeith.canteenpos.R;

public class NFCDisplayActivity extends AppCompatActivity {
    private TextView mTextView,bTextView,nTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcdisplay);
        mTextView = (TextView) findViewById(R.id.text_view_AccID);
        nTextView = (TextView) findViewById(R.id.text_view_TransacID);
        bTextView = (TextView)findViewById(R.id.text_view_msg);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nfcdisplay, menu);
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
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            bTextView.setText(new String(message.getRecords()[0].getPayload()));
            getMessage();
        } else
            bTextView.setText("Waiting for NDEF Message");
    }

    public void getMessage(){
        String msg = bTextView.getText().toString();
        String[] split = msg.split(".");
        String firstSubString = split[0];
        String secondSubString = split[1];

        mTextView.setText(firstSubString);
        nTextView.setText(secondSubString);
    }
}
