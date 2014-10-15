package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Fragment;
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
*/
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class Animation_frag extends Fragment {
  private Button knap1, knap2, knap3;

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
    View rod = i.inflate(R.layout.tre_knapper, container, false);
    rod.findViewById(R.id.ikon).setVisibility(View.GONE);
    knap1 = (Button) rod.findViewById(R.id.knap1);
    knap1.setText("Animation");
    knap2 = (Button) rod.findViewById(R.id.knap2);
    knap2.setText("Plastisk knap");
    knap3 = (Button) rod.findViewById(R.id.knap3);
    knap3.setText("Sprængfuld af energi");

    knap1.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View arg0, MotionEvent arg1) {
        if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
          knap1.animate().translationY(200);
        } else if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
          // Flyt knappen
          knap1.setX(arg1.getRawX());
        } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
          knap1.animate().translationY(0).translationX(0)
              .setInterpolator(new OvershootInterpolator())
              .setDuration(500);
        }
        return false;
      }
    });

    // Kilde: LiveButton-eksempel på DevBytes kanalen på developer.android.com
    final DecelerateInterpolator sDecelerator = new DecelerateInterpolator();
    final OvershootInterpolator sOvershooter = new OvershootInterpolator(10f);
    knap2.animate().setDuration(300);
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
          knap3.animate().setInterpolator(new AnimationInterpolator()).
              scaleX(2f).scaleY(2f);
        }
        return false;
      }
    });

    return rod;
  }
}
