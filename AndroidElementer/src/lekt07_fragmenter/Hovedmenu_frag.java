package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dk.nordfalk.android.elementer.R;

/**
* Created by j on 30-09-14.
*/ // Bemærk, fragmenter i indre klasser SKAL erklæres static
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class Hovedmenu_frag extends Fragment implements View.OnClickListener {
  private Button knap1, knap2, knap3;
  private ViewGroup rod;

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
    rod = (ViewGroup) i.inflate(R.layout.tre_knapper, container, false);
    rod.findViewById(R.id.ikon).setVisibility(View.GONE);

    knap1 = (Button) rod.findViewById(R.id.knap1);
    knap1.setText("MitFragment_frag");

    knap2 = (Button) rod.findViewById(R.id.knap2);
    knap2.setText("BenytDialoger_frag");

    knap3 = (Button) rod.findViewById(R.id.knap3);
    knap3.setText("Animation_frag");

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);

    return rod;
  }


  public void onClick(View v) {

    if (v == knap1) {
      getFragmentManager().beginTransaction()
          .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
          .replace(R.id.fragmentindhold, new MitFragment_frag())
          .addToBackStack(null)
          .commit();
    } else if (v == knap2) {
      getFragmentManager().beginTransaction()
          // Animationer - bemærk, skal være af type R.animator (ikke R.anim), og at
          // kompatibilitetsbiblioteket kræver typen R.anim (ikke R.animator).
          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
          .replace(R.id.fragmentindhold, new BenytDialoger_frag())
          .addToBackStack(null)
          .commit();
    } else if (v == knap3) {
      final Animation_frag animationFrag = new Animation_frag();
      FragmentTransaction t = getFragmentManager().beginTransaction()
          .replace(R.id.fragmentindhold, animationFrag)
          .addToBackStack(null);

      if (Build.VERSION.SDK_INT>=19) {
        // Brug Scene animation
        animationFrag.animerFraLayout = rod;
        /*
        Scene scene = new Scene(rod, vx);
69	        AutoTransition tr = new AutoTransition();
70	        tr.addListener(new Transition.TransitionListener() {
71	          @Override
72	          public void onTransitionStart(Transition transition) {
73	          }
74
75	          @Override
76	          public void onTransitionEnd(Transition transition) {
77	            getFragmentManager().beginTransaction()
78	                .replace(R.id.fragmentindhold, af)
79	                .addToBackStack(null)
80	                .commit();
81	          }
82
83	          @Override
84	          public void onTransitionCancel(Transition transition) {
85
86	          }
87
88	          @Override
89	          public void onTransitionPause(Transition transition) {
90
91	          }
92
93	          @Override
94	          public void onTransitionResume(Transition transition) {
95
96	          }
97	        });
98	        TransitionManager.go(scene, tr);
         */

      } else {
        // Brug almindelig animation
        t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
      }
      t.commit();
    }

  }
}
