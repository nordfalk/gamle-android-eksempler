package lekt03_diverse;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import dk.nordfalk.android.elementer.R;
import lekt02_aktiviteter.Indstillinger_akt;

public class BenytMenuer2 extends Activity {
  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    textView = new TextView(this);
    textView.setText("Dette eksempel viser hvordan menuer virker i Android 2, og hvordan ActionBar fungerer i Android 4\n");
    textView.append("Tryk på menu-knappen (F2 i emulatoren)\n");
    setContentView(textView);
  }

  /**
   * Kaldes én gang for at forberede menuen/ActionBar
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu); // tilføj systemets standardmenuer
    textView.append("\nonCreateOptionsMenu " + menu);
    // oftes vil man lægge menupunkterne ud i en XML-fil og pakke den ud således
    getMenuInflater().inflate(R.menu.lekt03_benytmenuer2, menu);
    return true;
  }

  /**
   * Kaldes hvis brugeren vælger noget i menuen
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    textView.append("\nonOptionsItemSelected(" + item.getTitle());
    if (item.getItemId() == R.id.javabog) {

      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://javabog.dk"));
      startActivity(intent);

    } else if (item.getItemId() == R.id.bil) {

      MediaPlayer.create(this, R.raw.dyt).start();

    } else if (item.getItemId() == R.id.indstillinger) {

      Intent intent = new Intent(this, Indstillinger_akt.class);
      startActivity(intent);

    } else if (item.getItemId() == R.id.afslut) {

      finish();

    } else {
      // Ikke håndteret - send kaldet videre til standardhåntering
      return super.onOptionsItemSelected(item);
    }
    return true;
  }

  /**
   * Kaldes når der trykkes på en fysisk knap (incl MENU)
   */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    textView.append("\nonKeyDown " + keyCode);
    return super.onKeyDown(keyCode, event);
  }
}
