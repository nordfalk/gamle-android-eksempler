package lekt01_views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;
import lekt02_aktiviteter.Indstillinger_akt;

public class BenytMenuer extends AppCompatActivity {
  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    textView = new TextView(this);
    textView.setText("Dette eksempel viser hvordan menuer virker\n");
    textView.append("Tryk på knapperne i topbjælken (Actionbar/Toolbar)\n");
    setContentView(textView);
  }

  /**
   * Kaldes én gang for generelt at forberede menuen
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    textView.append("\nonCreateOptionsMenu");
    menu.add(Menu.NONE, 101, Menu.NONE, "javabog.dk");
    menu.add(Menu.NONE, 102, Menu.NONE, "Kør bil").setIcon(R.drawable.bil).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    menu.add(Menu.NONE, 103, Menu.NONE, "Indstillinger").setIcon(android.R.drawable.ic_menu_preferences).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    menu.add(Menu.NONE, 104, Menu.NONE, "Afslut").setIcon(android.R.drawable.ic_menu_close_clear_cancel).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    // ofte vil man i stedet lægge menupunkterne ud i en XML-fil og pakke den ud således
    // getMenuInflater().inflate(R.menu.lekt03_benytmenuer2, menu);
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
}
