package lekt02_aktiviteter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class Hovedmenu_akt extends Activity implements OnClickListener {
  // Vi erklærer variabler herude så de huskes fra metode til metode
  Button hjaelpKnap, indstillingerKnap, spilKnap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.lekt01_tre_knapper);

    hjaelpKnap = (Button) findViewById(R.id.knap1);
    hjaelpKnap.setText("Hjælp");

    indstillingerKnap = (Button) findViewById(R.id.knap2);
    indstillingerKnap.setText("Indstillinger");

    spilKnap = (Button) findViewById(R.id.knap3);
    spilKnap.setText("Spil");

    hjaelpKnap.setOnClickListener(this);
    indstillingerKnap.setOnClickListener(this);
    spilKnap.setOnClickListener(this);
  }

  public void onClick(View v) {
    System.out.println("Der blev trykket på en knap");
    if (v == hjaelpKnap) {

      Intent i = new Intent(this, Hjaelp_akt.class);
      startActivity(i);

    } else if (v == indstillingerKnap) {

      Intent i = new Intent(this, Indstillinger_akt.class);
      startActivity(i);

    } else if (v == spilKnap) {

      Intent i = new Intent(this, Spillet_akt.class);
      i.putExtra("velkomst", "\n\nHalløj fra Hovedmenu_akt!\n");
      startActivity(i);

    }
  }
}
