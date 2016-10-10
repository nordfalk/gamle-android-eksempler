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
 *
 * @author Jacob Nordfalk
 */
public class NoteAktivitet2 extends Activity implements OnClickListener {

  EditText editText_postnr;
  private TextView alleNoterTv;
  private Runnable dataobservatør = new Runnable() {
    @Override
    public void run() {
      opdaterSkærm();
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    LinearLayout linearLayout = new LinearLayout(this);
    linearLayout.setOrientation(LinearLayout.VERTICAL);

    TextView textView = new TextView(this);
    textView.setText("Velkommen, " + MinApp.getData().navn + ", skriv dine noter herunder:");
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

    MinApp.getData().observatører.add(dataobservatør);
    opdaterSkærm();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    MinApp.getData().observatører.remove(dataobservatør);
  }

  @Override
  public void onClick(View v) {
    String note = editText_postnr.getText().toString();
    editText_postnr.setText("");
    MinApp.getData().noter.add(note);
    MinApp.getData().kaldObservatører(); // kalder run() på dataobservatør (og evt andre observatører)
  }

  private void opdaterSkærm() {
    String notetekst = MinApp.getData().noter.toString().replaceAll(", ", "\n");
    alleNoterTv.setText("Noter:\n" + notetekst);
  }
}
