package lekt09_services;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * @author Jacob Nordfalk
 */
public class BenytService extends Activity implements OnClickListener {

  Button knap1, knap2, knap3, knap4, knap5;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    TextView tv = new TextView(this);
    tv.setText("Når en service er startet vil systemet prøve at holde programmet "
            + "i hukommelsen.\n"
            + "Hvis det programmet alligevel ryger ud vil systemet genstarte processen "
            + "og reinstantiere servicen igen hurtigst muligt.\n"
            + "Hvis ens program SKAL blive i hukommelsen skal det starte en forgrundsservice, "
            + "der er knyttet til en notifikation som brugeren kan se.\n"
            + "Nederste knap kalder System.exit() for at simulere at processen afsluttes, "
            + "hvorefter systemet straks vil genstarte processen og reinstantiere servicen.");
    tl.addView(tv);

    knap1 = new Button(this);
    knap1.setText("Start service");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Stop service");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("Start forgrundsservice");
    tl.addView(knap3);

    knap4 = new Button(this);
    knap4.setText("Stop forgrundsservice");
    tl.addView(knap4);

    knap5 = new Button(this);
    knap5.setText("Stop proces");
    tl.addView(knap5);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
    knap4.setOnClickListener(this);
    knap5.setOnClickListener(this);
  }

  public void onClick(View klikketPå) {

    if (klikketPå == knap1) {
      Intent i = new Intent(this, BaggrundsService.class);
      i.putExtra("nøgle", "en værdi fra BenytService");
      startService(i);
    } else if (klikketPå == knap2) {
      stopService(new Intent(this, BaggrundsService.class));
    } else if (klikketPå == knap3) {
      startService(new Intent(this, ForgrundsService.class));
    } else if (klikketPå == knap4) {
      stopService(new Intent(this, ForgrundsService.class));
    } else if (klikketPå == knap5) {
      System.exit(0);
    }
  }
}
