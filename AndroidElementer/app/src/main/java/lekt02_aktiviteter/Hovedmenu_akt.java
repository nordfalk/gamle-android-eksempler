package lekt02_aktiviteter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;

/**
 * @author Jacob Nordfalk
 */
public class Hovedmenu_akt extends Activity implements OnClickListener {
  // Vi erklærer variabler herude så de huskes fra metode til metode
  Button spilKnap, indstillingerKnap, hjaelpKnap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Programmatisk layout
    TableLayout tl = new TableLayout(this);

    spilKnap = new Button(this);
    spilKnap.setText("Spil");
    tl.addView(spilKnap);

    indstillingerKnap = new Button(this);
    indstillingerKnap.setText("Indstillinger");
    tl.addView(indstillingerKnap);

    hjaelpKnap = new Button(this);
    hjaelpKnap.setText("Hjælp");
    tl.addView(hjaelpKnap);

    setContentView(tl);
    // Havde vi brugt deklarativt layout i XML havde vi i stedet her skrevet
    //setContentView(R.layout.mit_layout);
    //spilKnap = (Button) findViewById(R.id.spilKnap);
    //indstillingerKnap = (Button) findViewById(R.id.indstillingerKnap);
    //hjaelpKnap = (Button) findViewById(R.id.hjaelpKnap);

    spilKnap.setOnClickListener(this);
    indstillingerKnap.setOnClickListener(this);
    hjaelpKnap.setOnClickListener(this);
  }

  public void onClick(View v) {
    System.out.println("Der blev trykket på en knap");

    if (v == spilKnap) {

      Intent i = new Intent(this, Spillet_akt.class);
      startActivity(i);

    } else if (v == indstillingerKnap) {

      Intent i = new Intent(this, Indstillinger_akt.class);
      startActivity(i);

    } else if (v == hjaelpKnap) {

      Intent i = new Intent(this, Hjaelp_akt.class);
      startActivity(i);

    }

  }
}
