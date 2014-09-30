package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class BenytFragmenter extends Activity {

  private TextView statustekst;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_ACTION_BAR);
    statustekst = new TextView(this);
    statustekst.setText("Dette er statusteksten");

    FrameLayout fragmentcontainer = new FrameLayout(this);
    fragmentcontainer.setId(R.id.indhold);

    LinearLayout ll = new LinearLayout(this);
    ll.setOrientation(LinearLayout.VERTICAL);
    ll.addView(statustekst);
    ll.addView(fragmentcontainer);
    setContentView(ll);

    if (savedInstanceState == null) {
      StartFragment fragment = new StartFragment();
      getFragmentManager().beginTransaction()
          .add(R.id.indhold, fragment)
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



