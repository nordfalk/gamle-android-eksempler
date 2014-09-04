/*
 * /dmi/femdgn_dk.png
 * /dmi/index/danmark/solvarsel.htm
 * /dmi/varsel.xml  - DMI - varsel om farligt vejr
 * /dmi/rss-nyheder
 */        //http://servlet.dmi.dk/byvejr/servlet/byvejr?valgtBy=2500&tabel=dag10_14
// For kun at se denne proces' output:
// adb logcat -v tag ActivityManager:I Vejret:V System.err:V *:S
// Fremtidig geokodning bliver med
// http://www.geonames.org/postal-codes/postal-codes-denmark.html
package lekt04_arkitektur;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Simpel aktivitet til at tage noter
 * Benytter MinApp.data - som vi er SIKRE på er initialiseret,
 * da Application Singletons onCreate() metode bliver kaldt som
 * det første når en app (gen)startes
 * @author Jacob Nordfalk
 */
public class NoteAktivitet extends Activity implements OnClickListener {

  EditText editText_postnr;
  private TextView alleNoterTv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LinearLayout linearLayout = new LinearLayout(this);
    linearLayout.setOrientation(LinearLayout.VERTICAL);

    TextView textView = new TextView(this);
    textView.setText("Velkommen, "+MinApp.data.navn+", skriv dine noter herunder:");
    linearLayout.addView(textView);

    editText_postnr = new EditText(this);
    linearLayout.addView(editText_postnr);

    Button okKnap = new Button(this);
    okKnap.setText("OK");
    okKnap.setOnClickListener(this);
    linearLayout.addView(okKnap);

    alleNoterTv = new TextView(this);
    alleNoterTv.setText("");
    linearLayout.addView(alleNoterTv);

    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(linearLayout);
    setContentView(scrollView);

    visNoter();
  }

  @Override
  public void onClick(View v) {
    String note = editText_postnr.getText().toString();
    MinApp.data.noter.add(note);
    MinApp.gemData();
    editText_postnr.setText("");
    visNoter(); // max 1 time gamle
  }

  private void visNoter() {
    String notetekst = MinApp.data.noter.toString().replaceAll(", ", "\n");
    alleNoterTv.setText( "Noter:\n"+notetekst );
  }
}
