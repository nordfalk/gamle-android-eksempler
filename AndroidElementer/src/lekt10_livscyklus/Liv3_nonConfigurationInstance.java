package lekt10_livscyklus;

import android.os.Bundle;
import android.widget.EditText;

/**
 * Når skærmen vendes eller tastaturet bliver skubbet ind eller ud
 * bliver aktiviteten normalt kasseret og en ny aktivitet oprettes
 * med de nye skærmdimensioner etc.
 * <p/>
 * Dette kan slås fra ved at sætte denne attribut på aktiviteten i AndroidManifest.xml:
 * android:configChanges="orientation|keyboardHidden"
 * <p/>
 * Men det er normalt bedre at huske data_liste når skærmen vendes.
 * <p/>
 * Det gøres ved at definere onRetainNonConfigurationInstance(),
 * som systemet vil kalde på den gamle aktivitet når telefonen vendes.
 * <p/>
 * Derefter kan man i onCreate() i den nye aktivitet kalde
 * getLastNonConfigurationInstance() som vil give objektet tilbage
 *
 * @author Jacob Nordfalk
 */
public class Liv3_nonConfigurationInstance extends LogAktivitet {

  Programdata data;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Programdata dataFraForrigeAkrivitet = (Programdata) getLastNonConfigurationInstance();

    if (dataFraForrigeAkrivitet == null) {
      data = new Programdata();
      data.liste.add("første element");
    } else {
      data = dataFraForrigeAkrivitet;
      data.liste.add("dataFraForrigeAkrivitet " + data.liste.size());
    }

    EditText et = new EditText(this);
    et.setText(data.toString());
    setContentView(et);
  }

  @Override
  public Object onRetainNonConfigurationInstance() {
    return data;
  }
}
