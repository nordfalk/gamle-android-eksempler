package lekt10_livscyklus;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import lekt04_arkitektur.Programdata;

/**
 * @author Jacob Nordfalk
 */
public class Liv4_bundle extends LogAktivitet {

  Programdata data;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    data = new Programdata();
    if (savedInstanceState == null) {
      data.noter.add("første element");
    } else {
      data.alder = savedInstanceState.getInt("alder");
      data.navn = savedInstanceState.getString("navn");
      data.noter = savedInstanceState.getStringArrayList("noter");
      //data = (Programdata) savedInstanceState.getSerializable("data");
      data.noter.add("dataFraForrigeAkrivitet " + data.noter.size());
    }

    // Koden herunder er overflødig i nedarvinger, men forstørrer ikke,
    // så længe nedarvingerne også kalder setContentView()
    TextView tv = new TextView(this);
    tv.setText("Redigér i de to tekstfelter. "
            + "Det med id vil få genskabt sine data når telefonen vendes (i emulatoren tryk Ctrl-F11)\n"
            + data.toString());

    EditText et1 = new EditText(this);
    et1.setText("Et view uden id");

    EditText et2 = new EditText(this);
    et2.setText("Et view med id");
    // De Views der har et ID bliver gemt i onSaveInstanceState()
    // og genskabt i onRestoreInstanceState().
    // I XML-layout-filer sættes attributten ID med f.eks. android:id="@+id/navn"
    et2.setId(1000042); // bare et eller andet

    TableLayout tl = new TableLayout(this);
    tl.addView(tv);
    tl.addView(et1);
    tl.addView(et2);
    setContentView(tl);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    // her genskabes indhold for alle views med id
    super.onRestoreInstanceState(savedInstanceState);
    // data.navn = savedInstanceState.getString("navn");
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putInt("alder", ++data.alder);
    outState.putString("navn", data.navn);
    outState.putStringArrayList("noter", data.noter);
    outState.putSerializable("data", data);
    super.onSaveInstanceState(outState); // gem indhold for alle views med id
  }
}
