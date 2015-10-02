package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dk.nordfalk.android.elementer.R;

/**
 * Created by j on 30-09-14.
 */ // Bemærk, fragmenter i indre klasser SKAL erklæres static
public class BenytDialoger_frag extends Fragment implements View.OnClickListener {
  private Button knap1, knap2, knap3;

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
    View rod = i.inflate(R.layout.lekt01_tre_knapper, container, false);
    rod.findViewById(R.id.ikon).setVisibility(View.GONE);

    knap1 = (Button) rod.findViewById(R.id.knap1);
    knap1.setText("TekstDialog_frag\nsom dialog");

    knap2 = (Button) rod.findViewById(R.id.knap2);
    knap2.setText("TekstDialog_frag\nsom fragment");

    knap3 = (Button) rod.findViewById(R.id.knap3);
    knap3.setText("TekstDialog_frag\nsom fragment\nmed argument");

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);

    return rod;
  }


  public void onClick(View v) {

    if (v == knap1) {
      // DialogFragment har mulighed for at vises som en dialog
      new TekstDialog_frag().show(getFragmentManager(), "dialog");
    } else if (v == knap2) {
      // ... eller som et fragment
      getFragmentManager().beginTransaction()
              .replace(R.id.fragmentindhold, new TekstDialog_frag())
              .addToBackStack(null)
              .commit();
    } else if (v == knap3) {

      // Send argumenter med, som påvirker udseendet
      Bundle args = new Bundle();
      args.putString("TEKST", "Denne tekst kommer fra BenytDialoger_frag, " +
              "der har overført den som et argument til TekstDialog_frag.");
      TekstDialog_frag tekstDialogFrag = new TekstDialog_frag();
      tekstDialogFrag.setArguments(args);
      getFragmentManager().beginTransaction()
              .replace(R.id.fragmentindhold, tekstDialogFrag)
              .addToBackStack(null)
              .commit();
    }
  }
}
