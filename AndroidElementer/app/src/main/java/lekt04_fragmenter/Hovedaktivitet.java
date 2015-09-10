package lekt04_fragmenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class Hovedaktivitet extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_ACTION_BAR);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt07_fragmenter);

    if (savedInstanceState == null) {
      Fragment fragment = new Splash_frag();
      getSupportFragmentManager().beginTransaction()
              .add(R.id.fragmentindhold, fragment)  // tom container i layout
              .commit();
    }

    // Man kan trykke på app-ikonet i øverste venstre hjørne
    // (og det betyder at brugeren vil navigere op i hierakiet)
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      // brugeren vil navigere op i hierakiet
      if (getSupportFragmentManager().getBackStackEntryCount() == 0)
        finish(); // ikke flere fragmenter - afslut aktiviteten
      else
        getSupportFragmentManager().popBackStack(); // gå en tilbage i fragmentbunken
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
