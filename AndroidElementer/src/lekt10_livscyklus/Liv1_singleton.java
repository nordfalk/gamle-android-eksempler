package lekt10_livscyklus;

import android.os.Bundle;
import android.widget.EditText;

import lekt04_arkitektur.Programdata;

/**
 * @author Jacob Nordfalk
 */
public class Liv1_singleton extends LogAktivitet {

  /**
   * Gem programmets data i en klassevariabel - den er fælles for alle instanser
   */
  //static Programdata data;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Programdata data = SingletonSimpel.instans.programdata;
    /*
    if (data == null) {
      data = new Programdata();
      data.noter.add("første element");
    } else {
    }*/
    data.noter.add("dataFraForrigeAkrivitet " + data.noter.size());

    EditText et = new EditText(this);
    et.setText(data.toString());
    setContentView(et);

    data.noter.add("hej");
  }
}
