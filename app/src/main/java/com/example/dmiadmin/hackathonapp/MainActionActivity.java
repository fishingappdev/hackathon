package com.example.dmiadmin.hackathonapp;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dmiadmin.hackathonapp.model.Employee;
import com.example.dmiadmin.hackathonapp.util.DatabaseHelper;

import java.io.IOException;
import java.nio.charset.Charset;

public class MainActionActivity extends AppCompatActivity implements View.OnClickListener{
    NfcAdapter mNfcAdapter;
    private Button mAddBtn,mRemoveBtn,mAllocateBtn,mDeallocateBtn,mGeofenceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_action);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mAddBtn=(Button)findViewById(R.id.add_device_button);
        mRemoveBtn=(Button)findViewById(R.id.remove_device_button);
        mAllocateBtn=(Button)findViewById(R.id.allocate_device_button);
        mDeallocateBtn=(Button)findViewById(R.id.deallocate_device_button);
        mGeofenceBtn=(Button)findViewById(R.id.geofence_device_button);

        mAddBtn.setOnClickListener(this);
        mRemoveBtn.setOnClickListener(this);
        mAllocateBtn.setOnClickListener(this);
        mDeallocateBtn.setOnClickListener(this);
        mGeofenceBtn.setOnClickListener(this);

       com.example.dmiadmin.hackathonapp.util.DatabaseHelper db = new DatabaseHelper(this);

        /**
         * CRUD Operations
         * */
        // Inserting employee
        Log.d("Insert: ", "Inserting ..");
        db.addEmployee(new Employee("EO167DMP", "Swati Singh"));
        db.addEmployee(new Employee("EO139DMP", "Karan Singh"));
        db.addEmployee(new Employee("EO275DMP","Sarthak Srivastava"));
        db.addEmployee(new Employee("EO268DMP","Siddharth Dixit"));
        db.addEmployee(new Employee("EO220DMP","Nitish Srivastava"));



        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //method to handle your intent
        handleTag(getIntent());
    }

    @Override
    public void onResume() {
        super.onResume();

        final Intent intent = new Intent(this.getApplicationContext(), this.getClass());
        final PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent, 0);

        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        mNfcAdapter.disableForegroundDispatch(this);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleTag(intent);
    }

    private void handleTag(Intent intent) {
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {

            NfcA ndef = NfcA.get(tag);
            writeTag(MainActionActivity.this, tag, "Hello");
            ndef.getTag();

            try {
                Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                if (messages != null) {
                    NdefMessage[] ndefMessages = new NdefMessage[messages.length];
                    for (int i = 0; i < messages.length; i++) {

                        ndefMessages[i] = (NdefMessage) messages[i];
                    }
                    NdefRecord record = ndefMessages[0].getRecords()[0];

                    byte[] payload = record.getPayload();
                    String text = new String(payload);

                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Cannot Read From Tag.", Toast.LENGTH_LONG).show();
            }
        }
    }


    public static boolean writeTag(Context context, Tag tag, String data) {
        // Record to launch Play Store if app is not installed
        NdefRecord appRecord = NdefRecord.createApplicationRecord(context.getPackageName());

        // Record with actual data we care about
        NdefRecord relayRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                new String("application/" + context.getPackageName())
                        .getBytes(Charset.forName("US-ASCII")),
                null, data.getBytes());

        // Complete NDEF message with both records
        NdefMessage message = new NdefMessage(new NdefRecord[] {relayRecord, appRecord});

        try {
            // If the tag is already formatted, just write the message to it
            Ndef ndef = Ndef.get(tag);
            if(ndef != null) {
                ndef.connect();

                // Make sure the tag is writable
                if(!ndef.isWritable()) {
                    return false;
                }

                // Check if there's enough space on the tag for the message
                int size = message.toByteArray().length;
                if(ndef.getMaxSize() < size) {
                    return false;
                }

                try {
                    // Write the data to the tag
                    ndef.writeNdefMessage(message);
                    return true;
                } catch (TagLostException tle) {
                    return false;
                } catch (FormatException fe) {
                    return false;
                }
                // If the tag is not formatted, format it with the message
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if(format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        return true;
                    } catch (TagLostException tle) {
                        return false;
                    } catch (IOException ioe) {
                        return false;
                    } catch (FormatException fe) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch(Exception e) {
        }

        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_device_button:
                startActivity(new Intent(MainActionActivity.this,AddDeviceActivity.class));
                break;
        }
    }
}
