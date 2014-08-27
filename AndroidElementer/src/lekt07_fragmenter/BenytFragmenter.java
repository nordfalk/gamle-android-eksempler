package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BenytFragmenter extends Activity {

  private TextView statustekst;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_ACTION_BAR);
    FrameLayout fragmentcontainer = new FrameLayout(this);
    fragmentcontainer.setId(android.R.id.content);

    statustekst = new TextView(this);
    LinearLayout ll = new LinearLayout(this);
    ll.addView(statustekst);
    ll.addView(fragmentcontainer);
    setContentView(ll);

    if (savedInstanceState == null) {
      StartFragment fragment = new StartFragment();
      getFragmentManager().beginTransaction()
          .add(android.R.id.content, fragment)
          .commit();
    }

    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == android.R.id.home) {
      Toast.makeText(this, "OP blev trykket, afslutter", Toast.LENGTH_LONG).show();
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }


  private void skiftTilFragment1() {
    Toast.makeText(this, "Viser BenytFragment.MitFragment - tryk tilbage", Toast.LENGTH_LONG).show();
    getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new BenytFragment.MitFragment())
        .addToBackStack(null)
        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        .commit();
    ;
  }


  private void skiftTilFragment2() {
    Toast.makeText(this, "Viser DialogFragment", Toast.LENGTH_LONG).show();
    getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new Fragment2())
        .addToBackStack(null)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .commit();
    ;
  }


  // Bemærk, fragmenter SKAL erklæres static
  public static class StartFragment extends Fragment implements View.OnClickListener {
    Button knap1, knap2, knap3;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
      View rod = i.inflate(R.layout.tre_knapper, container, false);

      knap1 = (Button) rod.findViewById(R.id.knap1);
      knap1.setText("Vis BenytFragment.MitFragment");
      knap2 = (Button) rod.findViewById(R.id.knap2);
      knap3 = (Button) rod.findViewById(R.id.knap3);
      knap3.setText("Luk aktivitet.\n(dårlig opførsel - bruger har jo tilbage-knap)");

      knap1.setOnClickListener(this);
      knap2.setOnClickListener(this);
      knap3.setOnClickListener(this);

      return rod;
    }


    public void onClick(View v) {
      System.out.println("Der blev trykket på knap " + v.getContentDescription());

      BenytFragmenter benytFragmenter = (BenytFragmenter) getActivity();

      // Vis et tal der skifter så vi kan se hver gang der trykkes
      long etTal = System.currentTimeMillis();

      if (v == knap1) {
        benytFragmenter.skiftTilFragment1();
      } else if (v == knap2) {
        benytFragmenter.skiftTilFragment2();

      } else if (v == knap3) {
        benytFragmenter.finish();
      }

    }
  }


  // Bemærk, fragmenter SKAL erklæres static
  public static class Fragment2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
      TextView rod = new TextView(getActivity());
      rod.setText("Dette er Fragment2 - tryk tilbage");
      return rod;
    }
  }

}
