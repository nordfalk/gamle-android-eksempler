package lekt02_aktiviteter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * @author Jacob Nordfalk
 */
public class Spillet_akt extends Activity implements View.OnClickListener {

  Galgelogik logik = new Galgelogik();
  private TextView info;
  private Button spilKnap;
  private EditText et;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Programmatisk layout
    TableLayout tl = new TableLayout(this);

    info = new TextView(this);
    info.setText("Velkommen til mit fantastiske spil.\nSkriv et bogstav herunder og tryk 'Spil'.");
    tl.addView(info);

    et = new EditText(this);
    et.setHint("Skriv et bogstav her.");
    tl.addView(et);

    spilKnap = new Button(this);
    spilKnap.setText("Spil");
    spilKnap.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play, 0, 0, 0);
    tl.addView(spilKnap);

    spilKnap.setOnClickListener(this);

    setContentView(tl);
    opdaterSkærm();
  }

  @Override
  public void onClick(View view) {
    String bogstav = et.getText().toString();
    logik.gætBogstav(bogstav);
    et.setText("");
    opdaterSkærm();
  }


  private void opdaterSkærm() {
    info.setText("Gæt ordet: " + logik.getSynligtOrd() + "\n" + logik.getBrugteBogstaver());
    info.append("\nDu har " + logik.getAntalForkerteBogstaver() + " forkerte.");

    if (logik.erSpilletVundet()) {
      info.append("\nDu har vundet");
    }
    if (logik.erSpilletTabt()) {
      info.setText("Du har tabt, ordet var : " + logik.getOrdet());
    }

  }


}
