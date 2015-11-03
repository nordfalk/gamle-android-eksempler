package lekt09_recievers;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import lekt09_services.MinIntentService;

/**
 * @author Jacob Nordfalk
 */
public class BenytAlarmer extends Activity implements OnClickListener {

  Button knap1, knap2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    TextView tv = new TextView(this);
    tv.setText("Eksempel på brug af AlarmManager til regelmæssigt at blive vækket");
    tl.addView(tv);

    knap1 = new Button(this);
    knap1.setText("Start regelmæssig alarm");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Stop regelmæssig alarm");
    tl.addView(knap2);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
  }

  public void onClick(View klikketPå) {

    if (klikketPå == knap1) {
      AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
      PendingIntent pi = PendingIntent.getService(this, 0, new Intent(this, MinIntentService.class), 0);
      am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000, 60000, pi);
    } else if (klikketPå == knap2) {
      AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
      PendingIntent pi = PendingIntent.getService(this, 0, new Intent(this, MinIntentService.class), 0);
      am.cancel(pi);
    }
  }


  public static class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent i) {
      System.out.println("onReceive" + ctx + ":\n" + i);
      // I/System.out( 2870): Intent { act=android.intent.action.PACKAGE_REMOVED dat=package:dk.nordfalk.teoriproeve.ce flg=0x10000000 (has extras) }
      Toast.makeText(ctx, "onReceive" + ctx + ":\n" + i, Toast.LENGTH_LONG).show();

    }
  }

}
