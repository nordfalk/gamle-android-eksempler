package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class BenytHovedmenu_akt extends AppCompatActivity {

  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_ACTION_BAR);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt07_fragmenter);

    if (savedInstanceState == null) {
      Hovedmenu_frag fragment = new Hovedmenu_frag();
      getFragmentManager().beginTransaction()
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
      // brugeren vil navigere op i hierakiet - afslut aktiviteten
      Toast.makeText(this, "OP blev trykket, afslutter "
          +getFragmentManager().getBackStackEntryCount()
          +" niveauer nede i hierakiet", Toast.LENGTH_SHORT).show();
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }


}



