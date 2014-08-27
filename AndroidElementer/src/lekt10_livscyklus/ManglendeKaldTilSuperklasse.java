package lekt10_livscyklus;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ManglendeKaldTilSuperklasse extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Toast.makeText(this, "onPause() mangler at kalde videre i superklassen,", Toast.LENGTH_LONG).show();
    Toast.makeText(this, "så når du vender skærnen eller trykker tilbage så vil aktiviteten crashe", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onPause() {
    //super.onResume();
  }
}
