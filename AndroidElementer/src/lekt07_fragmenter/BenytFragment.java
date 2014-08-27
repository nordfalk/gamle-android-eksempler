package lekt07_fragmenter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import dk.nordfalk.android.elementer.R;


public class BenytFragment extends Activity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Her kunne også pakkes et FrameLayout ud fra XML
    FrameLayout layout = new FrameLayout(this);
    layout.setId(android.R.id.content); // i XML android:id="@+id/layout"
    setContentView(layout);

    if (savedInstanceState == null) {
      MitFragment fragment = new MitFragment();
      getFragmentManager().beginTransaction()
          .add(android.R.id.content, fragment)  // i XML R.id.layout
          .commit();
    }

  }


  /**
   * Et simpelt fragment. Fragmenter SKAL erklæres static
   * Sammenlign med BenytFlereKnapperXml
   *
   * @see lekt01_views.BenytKnapperDeklarativ
   */
  public static class MitFragment extends Fragment implements View.OnClickListener {
    // Vi erklærer variabler herude så de huskes fra metode til metode
    Button knap1, knap2, knap3;
    private View rod;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
      rod = i.inflate(R.layout.tre_knapper, container, false);

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
}
