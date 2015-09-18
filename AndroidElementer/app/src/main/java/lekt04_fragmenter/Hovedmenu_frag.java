package lekt04_fragmenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dk.nordfalk.android.elementer.R;
import lekt02_aktiviteter.*;

public class Hovedmenu_frag extends Fragment implements View.OnClickListener {
  Button hjaelpKnap, indstillingerKnap, spilKnap;

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
    View rod = i.inflate(R.layout.lekt01_tre_knapper, container, false);

    hjaelpKnap = (Button) rod.findViewById(R.id.knap1);
    hjaelpKnap.setText("Hjælp");

    indstillingerKnap = (Button) rod.findViewById(R.id.knap2);
    indstillingerKnap.setText("Indstillinger");

    spilKnap = (Button) rod.findViewById(R.id.knap3);
    spilKnap.setText("Spil");

    hjaelpKnap.setOnClickListener(this);
    indstillingerKnap.setOnClickListener(this);
    spilKnap.setOnClickListener(this);

    return rod;
  }


  public void onClick(View v) {
    if (v == hjaelpKnap) {

      getFragmentManager().beginTransaction()
              .replace(R.id.fragmentindhold, new Hjaelp_frag())
              .addToBackStack(null)
              .commit();

    } else if (v == indstillingerKnap) {

      // Der er ikke lavet et fragment for indstillinger, så her bruger vi aktiviteten
      Intent i = new Intent(getActivity(), Indstillinger_akt.class);
      startActivity(i);

    } else if (v == spilKnap) {

      Spillet_frag fragment = new Spillet_frag();
      Bundle argumenter = new Bundle(); // Overfør data til fragmentet
      argumenter.putString("velkomst", "\n\nHalløj fra Hovedmenu_frag!\n");
      fragment.setArguments(argumenter);

      getFragmentManager().beginTransaction()
              .replace(R.id.fragmentindhold, fragment)
              .addToBackStack(null)
              .commit();
    }
  }
}
