package lekt01_views;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;
import lekt02_aktiviteter.Indstillinger_akt;

public class BenytMenuer extends Activity {
  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    textView = new TextView(this);
    textView.setText("Dette eksempel viser hvordan menuer virker\n");
    textView.append("Tryk på menu-knappen (F2 i emulatoren)\n");
    setContentView(textView);
  }

  /**
   * Kaldes én gang for generelt at forberede menuen
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    textView.append("\nonCreateOptionsMenu");
    menu.add(Menu.NONE, 101, Menu.NONE, "javabog.dk");
    menu.add(Menu.NONE, 102, Menu.NONE, "En bil").setIcon(R.drawable.bil);
    menu.add(Menu.NONE, 103, Menu.NONE, "Indstillinger").setIcon(android.R.drawable.ic_menu_preferences);
    menu.add(Menu.NONE, 104, Menu.NONE, "Afslut").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
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

      Toast.makeText(this, "Denne knap er ikke implementeret endnu", Toast.LENGTH_LONG).show();

    } else if (item.getItemId() == 103) {

      Intent intent = new Intent(this, Indstillinger_akt.class);
      startActivity(intent);

    } else if (item.getItemId() == 104) {

      Toast.makeText(this, "Bemærk at det er generelt ikke er nødvendigt med afslut-knapper\n"
          + "Da brugeren godt ved hvad TILBAGE-knappen gør", Toast.LENGTH_LONG).show();
      finish();

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
