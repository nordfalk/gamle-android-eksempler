package lekt10_livscyklus;

import android.os.Bundle;
import android.widget.EditText;

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
      data.liste.add("første element");
    } else {
    }*/
    data.liste.add("dataFraForrigeAkrivitet " + data.liste.size());

    EditText et = new EditText(this);
    et.setText(data.toString());
    setContentView(et);

    data.liste.add("hej");
  }
}
