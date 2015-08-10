package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import dk.nordfalk.android.elementer.R;

/**
 * Et simpelt fragment. Fragmenter som indre klasser SKAL erklæres static
 * Sammenlign med BenytFlereKnapperXml
 *
 * @see lekt01_views.BenytKnapperDeklarativ
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MitFragment_frag extends Fragment implements View.OnClickListener {
  // Vi erklærer variabler herude så de huskes fra metode til metode
  private Button knap1, knap2, knap3;
  private View rod;

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
    rod = i.inflate(R.layout.lekt01_tre_knapper, container, false);

    knap1 = (Button) rod.findViewById(R.id.knap1);
    knap2 = (Button) rod.findViewById(R.id.knap2);
    knap3 = (Button) rod.findViewById(R.id.knap3);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);

    return rod;
  }


  public void onClick(View v) {
    System.out.println("Der blev trykket på en knap");

    // Vis et tal der skifter så vi kan se hver gang der trykkes
    long etTal = System.currentTimeMillis();

    if (v == knap1) {

      knap1.setText("Du trykkede på mig. Tak! \n" + etTal);

    } else if (v == knap2) {

      knap3.setText("Nej nej, tryk på mig i stedet!\n" + etTal);

    } else if (v == knap3) {

      knap2.setText("Hey, hvis der skal trykkes, så er det på MIG!\n" + etTal);
      // Erstat logoet med en bil
      ImageView ikon = (ImageView) rod.findViewById(R.id.ikon);
      ikon.setImageResource(R.drawable.bil);

    }

  }
}
