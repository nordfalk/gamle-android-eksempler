package lekt32_overgange;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import dk.nordfalk.android.elementer.R;

public class AktivitetsovergangBegynd extends AppCompatActivity implements View.OnClickListener {
    private Button knap1, knap2, knap3;
    private ViewGroup rod;
    private ImageView ikon;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt01_tre_knapper);
    findViewById(R.id.knap1).setOnClickListener(this);
    findViewById(R.id.knap2).setOnClickListener(this);
    findViewById(R.id.knap3).setOnClickListener(this);
  }

  public void onClick(View trykketPåKnap) {

    Intent intent = new Intent(this, AktivitetsovergangSlut.class);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      // Lav bindinger til mål-aktiviteten, så der kan laves glidende overgange
      // Navnene skal passe med det TransitionName viewsne har i mål-aktiviteten
      Pair<View, String> par1 = Pair.create(findViewById(R.id.ikon), "ikon");
      Pair<View, String> par2 = Pair.create(trykketPåKnap, "knappen");
      Pair<View, String> par3 = Pair.create(findViewById(R.id.knap3), "knap3");
      ActivityOptionsCompat options = ActivityOptionsCompat.
              makeSceneTransitionAnimation(this, par1, par2, par3);
      startActivity(intent, options.toBundle());
    } else {
      startActivity(intent);
    }
  }
}
