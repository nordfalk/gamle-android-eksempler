package lekt03_diverse;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class BenytLayoutInflater extends Activity implements OnClickListener {

  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.lekt01_tre_knapper);
    findViewById(R.id.knap1).setOnClickListener(this);
    findViewById(R.id.knap2).setOnClickListener(this);
    findViewById(R.id.knap3).setVisibility(View.INVISIBLE); // tager stadig plads
    findViewById(R.id.ikon).setVisibility(View.GONE); // tager ikke plads i layoutet

    ViewGroup indhold = (ViewGroup) findViewById(R.id.indhold);

    textView = new TextView(this);
    textView.setText("Man kan tilføje views til et eksisterende layout, både programmatisk og deklarativt." +
            "\nBemærk at der er en fejl: Tryk på den midterste overskrift herunder fungerer ikke");
    indhold.addView(textView); // Tilføj view programmatisk til 'indhold'

    getLayoutInflater().inflate(R.layout.lekt04_listeelement, indhold, true); // tilføj deklarativt til 'indhold'
    findViewById(R.id.listeelem_billede).setOnClickListener(this);
    findViewById(R.id.listeelem_overskrift).setOnClickListener(this);
    findViewById(R.id.listeelem_beskrivelse).setOnClickListener(this);

    // Hvis det samme layout 'blæses op' flere gange vil der være flere views med det samme id!
    // I så tilfælde vil findViewById() give det FØRSTE view den kan finde med det pågældende id.
    getLayoutInflater().inflate(R.layout.lekt04_listeelement, indhold, true); // FORKERT!
    findViewById(R.id.listeelem_billede).setOnClickListener(this);     // FORKERT!
    findViewById(R.id.listeelem_overskrift).setOnClickListener(this);  // FORKERT!
    findViewById(R.id.listeelem_beskrivelse).setOnClickListener(this); // FORKERT!

    // Løsningen er at vente med at fæstne viesne til layoutet til EFTER kald til findViewById()
    View rod = getLayoutInflater().inflate(R.layout.lekt04_listeelement, indhold, false); // false=fæstn ikke
    rod.findViewById(R.id.listeelem_billede).setOnClickListener(this);    // rigtigt
    rod.findViewById(R.id.listeelem_overskrift).setOnClickListener(this); // rigtigt
    rod.findViewById(R.id.listeelem_beskrivelse).setOnClickListener(this);// rigtigt
    indhold.addView(rod);
  }

  public void onClick(View v) {
    textView.setText("Der blev trykket på: " + v + " med id " + v.getId());

    if (v.getId() == R.id.knap1) {
      findViewById(R.id.ikon).setVisibility(View.VISIBLE);
    } else if (v.getId() == R.id.knap2) {
      findViewById(R.id.knap3).setVisibility(View.VISIBLE);
    }

  }
}
