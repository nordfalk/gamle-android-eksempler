package lekt06_asynkron;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class Asynk1Handler extends Activity implements OnClickListener {

  Handler handler = new Handler();
  Runnable opgave;
  Button knap1, knap2, knapAnnuller;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    TableLayout tl = new TableLayout(this);
    EditText editText = new EditText(this);
    editText.setText("Prøv at redigere her efter du har trykket på knapperne");
    editText.setId(R.id.editText); // Giv viewet et ID så dets indhold overlever en skærmvending
    tl.addView(editText);

    knap1 = new Button(this);
    knap1.setText("Opgave sendes til senere udførelse");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Brugerfladen opdateres løbende fra GUI-tråden");
    tl.addView(knap2);

    knapAnnuller = new Button(this);
    knapAnnuller.setText("Annuller");

    tl.addView(knapAnnuller);

    setContentView(tl);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knapAnnuller.setOnClickListener(this);
  }

  public void onClick(View v) {

    if (v == knap1) {
      knap1.setText("arbejder");
      opgave = new Runnable() {
        public void run() {
          knap1.setText("færdig!");
        }
      };
      handler.postDelayed(opgave, 10000); // udfør om 10 sekunder

    } else if (v == knap2) {

      knap2.setText("arbejder...");
      opgave = new Runnable() {

        int antalSekunderGået = 0;

        public void run() {
          if (antalSekunderGået++ < 10) {
            knap2.setText(antalSekunderGået+"...");
            handler.postDelayed(this, 1000); // udfør denne Runnable igen om 1 sekund
          } else {
            knap2.setText("færdig!"); // Der er gået 10 sekunder
          }
        }
      };
      handler.postDelayed(opgave, 1000); // udfør om 1 sekund

    } else if (v == knapAnnuller) {
      handler.removeCallbacks(opgave);
    }
  }
}
