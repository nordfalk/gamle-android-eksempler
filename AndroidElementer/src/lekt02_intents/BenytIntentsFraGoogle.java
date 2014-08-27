package lekt02_intents;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

/**
 * Demonstrerer hvordan man anvender intents til at aktivere nogle af Googles
 * proprietære apps, nemlig kortvisning, rutevisning, gadevisning og Google Play
 *
 * @author Jacob Nordfalk
 */
public class BenytIntentsFraGoogle extends Activity implements OnClickListener {

  Button kortvisning, rutevisning, gadevisning, googlePlay;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    kortvisning = new Button(this);
    kortvisning.setText("Vis kort");
    tl.addView(kortvisning);

    rutevisning = new Button(this);
    rutevisning.setText("Vis rute");
    tl.addView(rutevisning);

    gadevisning = new Button(this);
    gadevisning.setText("Gadevisning");
    tl.addView(gadevisning);

    googlePlay = new Button(this);
    googlePlay.setText("Installér app via Google Play");
    tl.addView(googlePlay);

    kortvisning.setOnClickListener(this);
    rutevisning.setOnClickListener(this);
    gadevisning.setOnClickListener(this);
    googlePlay.setOnClickListener(this);

    setContentView(tl);
  }

  /* Sæt placering fra kommandolinjen med f.eks.
     (echo geo fix 12.493775 55.65407 100; sleep 1) | telnet localhost 5554
     Sætter placering til Valby i 100 meters højde
   */

  public void onClick(View hvadBlevDerKlikketPå) {
    try {
      if (hvadBlevDerKlikketPå == kortvisning) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:55.65407,12.493775?z=3"));
        startActivity(i);
      } else if (hvadBlevDerKlikketPå == rutevisning) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=55.65407,12.493775&daddr=55.66,12.5"));
        startActivity(i);
      } else if (hvadBlevDerKlikketPå == gadevisning) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.streetview:cbll=55.65407,12.493775&cbp=1"));
        startActivity(i);
      } else if (hvadBlevDerKlikketPå == googlePlay) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=dk.nordfalk.esperanto.radio"));
        startActivity(i);
      }
    } catch (ActivityNotFoundException e) {
      Toast.makeText(this, "Du mangler Googles udvidelser på denne telefon:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }
}
