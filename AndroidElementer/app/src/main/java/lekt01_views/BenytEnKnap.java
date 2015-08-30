package lekt01_views;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * En aktivitet med én knap
 */
public class BenytEnKnap extends Activity implements OnClickListener {

  // Vi erklærer variabler herude så de huskes fra metode til metode
  Button enKnap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Programmatisk layout
    enKnap = new Button(this);
    enKnap.setText("Tryk på mig");
    setContentView(enKnap);
    // Havde vi brugt deklarativt layout i XML havde vi i stedet skrevet
    //setContentView(R.layout.mit_layout);
    //enKnap = (Button) findViewById(R.id.enKnap);

    enKnap.setOnClickListener(this);
  }

  public void onClick(View v) {
    // Vis et tal der skifter så vi kan se hver gang der trykkes
    long etTal = System.currentTimeMillis();

    // Skriv meddelelse på knappen (kan på skærmen)
    enKnap.setText("Du trykkede på mig. Tak! \n" + etTal);

    // Skriv meddelelse til loggen (loggen kan ses med adb logcat)
    System.out.println("Der blev trykket på knappen");

    Log.d("BenytEnKnap", "Knap trykket");
  }
}
