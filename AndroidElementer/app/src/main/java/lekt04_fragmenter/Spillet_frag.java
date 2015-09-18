package lekt04_fragmenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import lekt02_aktiviteter.Galgelogik;

/**
 * @author Jacob Nordfalk
 */
public class Spillet_frag extends Fragment implements View.OnClickListener {

  Galgelogik logik = new Galgelogik();
  private TextView info;
  private Button spilKnap;
  private EditText et;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d("Velkomst_frag", "fragmentet blev vist!");

    // Programmatisk layout
    TableLayout tl = new TableLayout(getActivity());

    info = new TextView(getActivity());
    info.setText("Velkommen til mit fantastiske spil." +
            "\nDu skal gætte dette ord: "+logik.getSynligtOrd() +
            "\nSkriv et bogstav herunder og tryk 'Spil'.\n");
    String velkomst = getArguments().getString("velkomst");
    if (velkomst!=null) info.append(velkomst);
    tl.addView(info);

    et = new EditText(getActivity());
    et.setHint("Skriv et bogstav her.");
    tl.addView(et);

    spilKnap = new Button(getActivity());
    spilKnap.setText("Spil");
    spilKnap.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play, 0, 0, 0);
    tl.addView(spilKnap);

    spilKnap.setOnClickListener(this);

    return tl;
  }

  @Override
  public void onClick(View view) {
    String bogstav = et.getText().toString();
    if (bogstav.length() != 1) {
      et.setError("Skriv præcis ét bogstav");
      return;
    }
    logik.gætBogstav(bogstav);
    et.setText("");
    et.setError(null);
    opdaterSkærm();
  }


  private void opdaterSkærm() {
    info.setText("Gæt ordet: " + logik.getSynligtOrd());
    info.append("\n\nDu har " + logik.getAntalForkerteBogstaver() + " forkerte:" + logik.getBrugteBogstaver());

    if (logik.erSpilletVundet()) {
      info.append("\nDu har vundet");
    }
    if (logik.erSpilletTabt()) {
      info.setText("Du har tabt, ordet var : " + logik.getOrdet());
    }
  }
}
