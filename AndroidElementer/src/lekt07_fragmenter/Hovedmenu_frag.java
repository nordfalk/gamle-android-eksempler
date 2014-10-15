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
import android.widget.ImageView;

import dk.nordfalk.android.elementer.R;

/**
* Created by j on 30-09-14.
*/ // Bemærk, fragmenter i indre klasser SKAL erklæres static
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class Hovedmenu_frag extends Fragment implements View.OnClickListener {
  private Button knap1, knap2, knap3;
  private ImageView ikon;

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
    View rod = i.inflate(R.layout.tre_knapper, container, false);

    ikon = (ImageView) rod.findViewById(R.id.ikon);
    ikon.setImageResource(android.R.drawable.ic_media_play);

    knap1 = (Button) rod.findViewById(R.id.knap1);
    knap1.setText("MitFragment_frag");
    knap1.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play, 0, 0, 0);

    knap2 = (Button) rod.findViewById(R.id.knap2);
    knap2.setText("TekstDialog_frag\nsom dialog");
    knap3 = (Button) rod.findViewById(R.id.knap3);
    knap3.setText("TekstDialog_frag\nsom fragment");

    ikon.setOnClickListener(this);
    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);

    return rod;
  }


  public void onClick(View v) {
    System.out.println("Der blev trykket på knap " + v.getContentDescription());

    if (v == ikon) {
      getFragmentManager().beginTransaction()
          .replace(R.id.fragmentindhold, new Animation_frag())
          .addToBackStack(null)
          .commit();
    } else if (v == knap1) {
      getFragmentManager().beginTransaction()
          .replace(R.id.fragmentindhold, new MitFragment_frag())
          .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
          .addToBackStack(null)
          .commit();
    } else if (v == knap2) {
      // DialogFragment har mulighed for at vises som en dialog
      new TekstDialog_frag().show(getFragmentManager(), "dialog");
    } else if (v == knap3) {
      // ... eller som et fragment
      getFragmentManager().beginTransaction()
          // Animationer - bemærk, skal være af type R.animator (ikke R.anim), og at
          // kompatibilitetsbiblioteket kræver typen R.anim (ikke R.animator).
          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
          .replace(R.id.fragmentindhold, new TekstDialog_frag())
          .addToBackStack(null)
          .commit();
    }

  }
}
