package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dk.nordfalk.android.elementer.R;

/**
 * Created by j on 30-09-14.
 */ // Bemærk, fragmenter i indre klasser SKAL erklæres static
public class Hovedmenu_frag extends Fragment implements View.OnClickListener {
  private Button knap1, knap2, knap3;
  private ViewGroup rod;

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
    rod = (ViewGroup) i.inflate(R.layout.lekt01_tre_knapper, container, false);
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
              // Animationer - bemærk:
              // MED  kompativilitetsbibliotek SKAL de være af typen R.anim (ikke R.animator),
              // UDEN kompatibilitetsbibliotek SKAL de være af typen R.animator (ikke R.anim).
              .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
              // uden kompativilitetsbibliotek ville det være
              // .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
              .replace(R.id.fragmentindhold, new BenytDialoger_frag())
              .addToBackStack(null)
              .commit();
    } else if (v == knap3) {
      getFragmentManager().beginTransaction()
              .replace(R.id.fragmentindhold, new Animation_frag())
              .addToBackStack(null).commit();
    }

  }
}
