package lekt03_diverse;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import lekt02_aktiviteter.Indstillinger_akt;
import lekt03_net.ByvejrAktivitet;

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
   * Kaldes én gang for generelt at forberede menuen
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu); // tilføj systemets standardmenuer
    textView.append("\nonCreateOptionsMenu " + menu);
    menu.add(Menu.NONE, 101, Menu.NONE, "javabog.dk");
    MenuItem mi1 = menu.add(Menu.NONE, 102, Menu.NONE, "Vejrudsigt").setIcon(android.R.drawable.ic_menu_compass);
    MenuItem mi2 = menu.add(Menu.NONE, 103, Menu.NONE, "Indstillinger").setIcon(android.R.drawable.ic_menu_preferences);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      // Vis i ActionBar på Android 4
      mi1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
      mi2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }
    menu.add(Menu.NONE, 104, Menu.NONE, "Afslut").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    // ofte vil man lægge menupunkterne ud i en XML-fil og pakke den ud således
    // getMenuInflater().inflate(R.menu.benytmenuer, menu);
    return true;
  }

  /**
   * Kaldes hver gang menuen skal vises
   */
  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    textView.append("\nonPrepareOptionsMenu " + menu);
    super.onPrepareOptionsMenu(menu); // forbered systemets standardmenuer
    return true;
  }

  /**
   * Kaldes hvis brugeren vælger noget i menuen
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    textView.append("\nonOptionsItemSelected(" + item.getTitle());
    if (item.getItemId() == 101) {

      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://javabog.dk"));
      startActivity(intent);

    } else if (item.getItemId() == 102) {

      Intent intent = new Intent(this, ByvejrAktivitet.class);
      startActivity(intent);

    } else if (item.getItemId() == 103) {

      Intent intent = new Intent(this, Indstillinger_akt.class);
      startActivity(intent);

    } else if (item.getItemId() == 104) {

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
