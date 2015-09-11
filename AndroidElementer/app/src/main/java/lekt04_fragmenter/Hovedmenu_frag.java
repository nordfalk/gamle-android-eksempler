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
  private Button spilKnap, indstillingerKnap, hjaelpKnap;

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
    View rod = i.inflate(R.layout.lekt01_tre_knapper, container, false);

    spilKnap = (Button) rod.findViewById(R.id.knap1);
    spilKnap.setText("Spil");

    indstillingerKnap = (Button) rod.findViewById(R.id.knap2);
    indstillingerKnap.setText("Indstillinger");

    hjaelpKnap = (Button) rod.findViewById(R.id.knap3);
    hjaelpKnap.setText("Hjælp");

    spilKnap.setOnClickListener(this);
    indstillingerKnap.setOnClickListener(this);
    hjaelpKnap.setOnClickListener(this);

    return rod;
  }


  public void onClick(View v) {
    if (v == spilKnap) {
      getFragmentManager().beginTransaction()
              .replace(R.id.fragmentindhold, new Spillet_frag())
              .addToBackStack(null)
              .commit();
    } else if (v == indstillingerKnap) {
      // Der er ikke lavet et fragment for indstillinger, så her bruger vi aktiviteten
      Intent i = new Intent(getActivity(), Indstillinger_akt.class);
      startActivity(i);
    } else if (v == hjaelpKnap) {
      getFragmentManager().beginTransaction()
              .replace(R.id.fragmentindhold, new Hjaelp_frag())
              .addToBackStack(null)
              .commit();
    }
  }
}
