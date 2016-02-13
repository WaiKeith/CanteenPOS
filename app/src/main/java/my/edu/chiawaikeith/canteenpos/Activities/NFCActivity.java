package my.edu.chiawaikeith.canteenpos.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import my.edu.chiawaikeith.canteenpos.Domains.Transactions;
import my.edu.chiawaikeith.canteenpos.R;

public class NFCActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback{

    private TextView aText;
    private Toolbar toolBar;
    private String front,behind;
    private Transactions transaction = new Transactions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        toolBar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.title_nfc_activity);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //aText = (TextView) findViewById(R.id.textview_acc_id);
        aText = (TextView) findViewById(R.id.textview_transac_id);

        transaction = (Transactions) getIntent().getSerializableExtra(FoodCarts.KEY_TRANSAC);

        initValues();

        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            //mEditText.setText("Sorry this device does not have NFC.");
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Sorry, this device does not have NFC.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);
    }

    private void initValues(){
        front = String.valueOf(transaction.getAcc_id());
        behind = String.valueOf(transaction.getTransac_id());
        aText.setText(front + "." + behind);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nfc, menu);
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
    public NdefMessage createNdefMessage(NfcEvent event) {
        String message = aText.getText().toString();
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }
}
