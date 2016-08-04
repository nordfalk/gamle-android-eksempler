package lekt02_aktiviteter;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
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
    info.setText("Velkommen til mit fantastiske spil." +
            "\nDu skal gætte dette ord: "+logik.getSynligtOrd() +
            "\nSkriv et bogstav herunder og tryk 'Spil'.\n");
    String velkomst = getIntent().getStringExtra("velkomst");
    if (velkomst!=null) info.append(velkomst);
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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
      spilKnap.animate().rotationBy(3*360).setInterpolator(new DecelerateInterpolator());
//      spilKnap.animate().translationYBy(30).setInterpolator(new BounceInterpolator());
    }
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
