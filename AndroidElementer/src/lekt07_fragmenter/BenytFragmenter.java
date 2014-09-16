package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
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


  // Bemærk, fragmenter i indre klasser SKAL erklæres static
  public static class StartFragment extends Fragment implements View.OnClickListener {
    Button knap1, knap2, knap3;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
      View rod = i.inflate(R.layout.tre_knapper, container, false);

      rod.findViewById(R.id.ikon).setVisibility(View.GONE);
      knap1 = (Button) rod.findViewById(R.id.knap1);
      knap1.setText("MitFragment");
      knap2 = (Button) rod.findViewById(R.id.knap2);
      knap2.setText("TekstDialogFragment som dialog");
      knap3 = (Button) rod.findViewById(R.id.knap3);
      knap3.setText("TekstDialogFragment som fragment");

      knap1.setOnClickListener(this);
      knap2.setOnClickListener(this);
      knap3.setOnClickListener(this);

      // Kilde: LiveButton-eksempel på DevBytes kanalen på developer.android.com
      final DecelerateInterpolator sDecelerator = new DecelerateInterpolator();
      final OvershootInterpolator sOvershooter = new OvershootInterpolator(10f);
      //knap2.animate().setDuration(2000);
      knap2.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
          if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
            knap2.animate().setInterpolator(sDecelerator).
                scaleX(.7f).scaleY(.7f);
          } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
            knap2.animate().setInterpolator(sOvershooter).
                scaleX(1f).scaleY(1f);
          }
          return false;
        }
      });
      knap3.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
          if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
            knap3.animate().setInterpolator(new MinInterpolator()).
                scaleX(2f).scaleY(2f);
          }
          return false;
        }
      });

      return rod;
    }


    public void onClick(View v) {
      System.out.println("Der blev trykket på knap " + v.getContentDescription());

      if (v == knap1) {
        getFragmentManager().beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
            .replace(android.R.id.content, new BenytFragment.MitFragment())
            .addToBackStack(null)
            .commit();
        ;
      } else if (v == knap2) {
        // DialogFragment har mulighed for at vises som en dialog
        new TekstDialogFragment().show(getFragmentManager(), "dialog");
      } else if (v == knap3) {
        // ... eller som et fragment
        getFragmentManager().beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(android.R.id.content, new TekstDialogFragment())
            .addToBackStack(null)
            .commit();
      }

    }
  }


  // Bemærk, fragmenter i indre klasser SKAL erklæres static
  public static class TekstDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

      TextView rod = new TextView(getActivity());
      rod.setText("Dette er Fragment2 - tryk tilbage");
      return rod;
    }
  }

}



class MinInterpolator implements Interpolator {
  private final float mTension = 10;

  /**
   * Maps a value representing the elapsed fraction of an animation to a value that represents
   * the interpolated fraction. This interpolated value is then multiplied by the change in
   * value of an animation to derive the animated value at the current elapsed animation time.
   *
   * @param t A value between 0 and 1.0 indicating our current point
   *        in the animation where 0 represents the start and 1.0 represents
   *        the end
   * @return The interpolation value. This value can be more than 1.0 for
   *         interpolators which overshoot their targets, or less than 0 for
   *         interpolators that undershoot their targets.
   */
  public float getInterpolation(float t) {
    // _o(t) = t * t * ((tension + 1) * t + tension)
    // o(t) = _o(t - 1) + 1
    //t -= 1.0f;
    //return t * t * ((mTension + 1) * t + mTension) + 1.0f;
    return t * (1-t) * mTension;
  }
}
