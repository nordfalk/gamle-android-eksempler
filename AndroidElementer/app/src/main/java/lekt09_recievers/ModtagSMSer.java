package lekt09_recievers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

class SMSReciever extends BroadcastReceiver {

  @Override
  public void onReceive(Context ctx, Intent intent) {
    System.out.println("intent.toURI() = " + intent.toURI());
    Toast.makeText(ctx, intent.toURI(), Toast.LENGTH_LONG).show();
    Bundle data = intent.getExtras();
    if (data != null) {
      Object pdus[] = ((Object[]) data.get("pdus"));
      for (Object pdu : pdus) {
        SmsMessage part = SmsMessage.createFromPdu((byte[]) pdu);
        Toast.makeText(ctx, "SMS fra " + part.getDisplayOriginatingAddress(), Toast.LENGTH_SHORT).show();
        Toast.makeText(ctx, part.getDisplayMessageBody(), Toast.LENGTH_LONG).show();

        Intent i = new Intent(ctx, ModtagSMSer.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
      }
    }
  }
}

/**
 * @author Jacob Nordfalk
 */
public class ModtagSMSer extends Activity implements OnClickListener {

  static SMSReciever reciever;
  Button registrer, afregistrer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    TextView tv = new TextView(this);
    tv.setText("Broadcastreciever der opdager når der modages SMSer.\nDu kan sende SMSer til emulatoren ved at starte DDMS og vælge fanen 'Emulator Control'.");
    tl.addView(tv);

    registrer = new Button(this);
    registrer.setText("Registrer reciever");
    tl.addView(registrer);

    afregistrer = new Button(this);
    afregistrer.setText("Afregistrer reciever");
    tl.addView(afregistrer);

    setContentView(tl);

    registrer.setOnClickListener(this);
    afregistrer.setOnClickListener(this);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    if (hvadBlevDerKlikketPå == registrer && reciever == null) {
      reciever = new SMSReciever();
      IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
      getApplicationContext().registerReceiver(reciever, filter);
      Toast.makeText(this, "Send nu en SMS til telefonen", Toast.LENGTH_SHORT).show();
    } else if (hvadBlevDerKlikketPå == afregistrer && reciever != null) {
      getApplicationContext().unregisterReceiver(reciever);
      reciever = null;
      Toast.makeText(this, "Afregistreret", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(this, "Recieveren er allerede " + (reciever == null ? "afregistreret" : "registreret"), Toast.LENGTH_SHORT).show();
    }
  }
}
