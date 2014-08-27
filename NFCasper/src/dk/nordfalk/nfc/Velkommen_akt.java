package dk.nordfalk.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.Arrays;

public class Velkommen_akt extends Activity
{
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private TextView tv;
    private int mCount = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        tv = new TextView(this);
        ScrollView sv = new ScrollView(this);
        sv.addView(tv);
        setContentView(sv);

        tv.setText("Velkommen til Caspers fanTAGstiske program!\n");
        tv.append("Få nu knoglen hen til et tag...\n");

        mAdapter = NfcAdapter.getDefaultAdapter(this);


        mPendingIntent = PendingIntent.getActivity(
          this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

    }



    @Override
    public void onResume() {
        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
        tv.append("\nintent: " + intent+"\n");
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        tv.append("tag: " + tagFromIntent+"\n");
        tv.append("tag Tech: " + Arrays.toString(tagFromIntent.getTechList())+"\n");
        tv.append("tag ID: " + Arrays.toString(tagFromIntent.getId())+"\n");

        NdefMessage[] msgs;

        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
          if (rawMsgs != null) {
              msgs = new NdefMessage[rawMsgs.length];
              for (int i = 0; i < rawMsgs.length; i++) {
                  msgs[i] = (NdefMessage) rawMsgs[i];
                  tv.append("msgs"+i+" -> "+new String(msgs[i].toByteArray())+"\n");
                  for (NdefRecord r : msgs[i].getRecords()) {
                    tv.append("r ID="+new String(r.getId())+"\n");
                    tv.append("r ID="+new String(r.getType())+"\n");
                    tv.append("r PL="+new String(r.getPayload())+"\n");
                    tv.append("r PL="+Arrays.toString(r.getPayload())+"\n");
                  }
              }
          } else {
              tv.append("Ingen rawMsgs\n");
          }

    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
        //throw new RuntimeException("onPause not implemented to fix build");
    }


}
