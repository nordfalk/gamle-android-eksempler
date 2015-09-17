package lekt02_aktiviteter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import dk.nordfalk.android.elementer.R;

public class Velkomst_akt extends Activity implements Runnable {

  static Velkomst_akt aktivitetDerVisesNu = null;
  Handler handler = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d("Velkomst_akt", "aktiviteten blev startet!");

    ImageView iv = new ImageView(this);
    iv.setImageResource(R.drawable.logo);
    iv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.egen_anim));
    setContentView(iv);

    // Hvis savedInstanceState ikke er null er det en aktivitet der er ved at blive genstartet
    if (savedInstanceState == null) {
      handler.postDelayed(this, 3000); // <1> Kør run() om 3 sekunder
    }
    aktivitetDerVisesNu = this;
  }

  public void run() {
    startActivity(new Intent(this, Hovedmenu_akt.class));
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    aktivitetDerVisesNu.finish();  // <2> Luk velkomsaktiviteten
    aktivitetDerVisesNu = null;
  }

  /**
   * Kaldes hvis brugeren trykker på tilbage-knappen.
   * I så tilfælde skal vi ikke hoppe videre til næste aktivitet
   */
  @Override
  public void finish() {
    super.finish();
    handler.removeCallbacks(this);
  }
}