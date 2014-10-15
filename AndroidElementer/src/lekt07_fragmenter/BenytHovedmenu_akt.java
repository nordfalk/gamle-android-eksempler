package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class BenytHovedmenu_akt extends Activity {

  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_ACTION_BAR);
    setContentView(R.layout.lekt07_fragmenter);

    if (savedInstanceState == null) {
      Hovedmenu_frag fragment = new Hovedmenu_frag();
      getFragmentManager().beginTransaction()
          .add(R.id.fragmentindhold, fragment)
          .commit();
    }

    // Man kan trykke på app-ikonet i øverste venstre hjørne
    // (og det betyder at brugeren vil navigere op i hierakiet)
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      // brugeren vil navigere op i hierakiet - afslut aktiviteten
      Toast.makeText(this, "OP blev trykket, afslutter", Toast.LENGTH_LONG).show();
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }


}



