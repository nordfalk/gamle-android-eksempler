package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;

import dk.nordfalk.android.elementer.R;

/**
* Created by j on 30-09-14.
*/ // Bemærk, fragmenter i indre klasser SKAL erklæres static
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class StartFragment extends Fragment implements View.OnClickListener {
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
          .replace(R.id.indhold, new MitFragment())
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
          .replace(R.id.indhold, new TekstDialogFragment())
          .addToBackStack(null)
          .commit();
    }

  }
}
