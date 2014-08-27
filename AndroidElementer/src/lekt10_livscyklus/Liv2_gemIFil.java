package lekt10_livscyklus;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;

/**
 * Viser hvordan data kan gemmes i en fil. Her bruges serialisering.
 *
 * @author Jacob Nordfalk
 */
public class Liv2_gemIFil extends LogAktivitet {

  Programdata data;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


//    getAssets().open("benytwebview.html");
//    getResources().openRawResource(R.raw.jeg_bremser_haardt);

    try {
      data = (Programdata) Serialisering.hent(getCacheDir() + "/programdata.ser");
      Log.d("data.liste", "" + data);
      Log.d("data.liste", "" + data.liste);

      data.liste.add("dataFraForrigeAkrivitet " + data.liste.size());
      System.out.println("programdata indlæst fra fil");
    } catch (Exception ex) {
      data = new Programdata(); // fil fandtes ikke eller data var inkompatible
      data.liste.add("første element");
      System.out.println("programdata oprettet fra ny: " + ex);
    }

    EditText et = new EditText(this);
    et.setText(data.toString());
    setContentView(et);
  }

  @Override
  protected void onPause() {
    super.onPause();
    try {  // nu er aktiviteten ikke synlig, så er det tid til at gemme data!
      Serialisering.gem(data, getCacheDir() + "/programdata.ser");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
