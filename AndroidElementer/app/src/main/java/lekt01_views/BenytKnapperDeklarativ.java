package lekt01_views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class BenytKnapperDeklarativ extends AppCompatActivity implements OnClickListener {
  // Vi erklærer variabler herude så de huskes fra metode til metode
  Button knap1, knap2, knap3;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    System.out.println("savedInstanceState==" + savedInstanceState);

    // Hvis vi erklærer variabler herinde så er de lokale variabler og ikke tilgængelige i andre metoder
    //Button knap1, knap2, knap3;

    setContentView(R.layout.lekt01_tre_knapper);
    // findViewById() kan først kaldes efter setContentView()
    knap1 = (Button) findViewById(R.id.knap1);
    knap2 = (Button) findViewById(R.id.knap2);
    knap3 = (Button) findViewById(R.id.knap3);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
  }

  public void onClick(View v) {
    System.out.println("Der blev trykket på en knap");

    // Vis et tal der skifter så vi kan se hver gang der trykkes
    long etTal = System.currentTimeMillis();

    if (v == knap1) {

      knap1.setText("Du trykkede på mig. Tak! \n" + etTal);

    } else if (v == knap2) {

      knap3.setText("Nej nej, tryk på mig i stedet!\n" + etTal);

    } else if (v == knap3) {

      knap2.setText("Hey, hvis der skal trykkes, så er det på MIG!\n" + etTal);
      // Erstat logoet med en bil
      ImageView ikon = (ImageView) findViewById(R.id.ikon);
      ikon.setImageResource(R.drawable.bil);

    }

  }
}
