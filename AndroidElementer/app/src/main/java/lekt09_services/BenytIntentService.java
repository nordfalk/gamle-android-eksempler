package lekt09_services;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;

import dk.nordfalk.android.elementer.R;


/**
 * Dette eksempel bruger en IntentService til en baggrundsopgave.
 * Hvis skærmen vendes knyttes IntentService'en korrekt til næste aktivitet og
 * fortsætter med at fungere der.
 */
public class BenytIntentService extends Activity implements OnClickListener {

  ProgressBar progressBar;
  Button knap, annullerknap;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    EditText editText = new EditText(this);
    editText.setText("En IntentService er kombinationen af en arbejds-/baggrundstråd og en service.");
    editText.setId(R.id.editText); // Giv viewet et ID så dets indhold overlever en skærmvending
    tl.addView(editText);

    progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
    progressBar.setMax(99);
    progressBar.setId(R.id.enKnap); // Giv viewet et ID så dets indhold overlever en skærmvending
    tl.addView(progressBar);

    knap = new Button(this);
    knap.setText("Start intentservice");
    tl.addView(knap);

    annullerknap = new Button(this);
    annullerknap.setText("Annullér");
    tl.addView(annullerknap);

    setContentView(tl);

    knap.setOnClickListener(this);
    annullerknap.setOnClickListener(this);

    MinIntentService.aktivitetDerSkalOpdateres = this;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    // frigiv reference, ellers er der en hukommelseslæk
    MinIntentService.aktivitetDerSkalOpdateres = null;
  }


  public void onClick(View hvadBlevDerKlikketPå) {

    if (hvadBlevDerKlikketPå == knap) {
      startService(new Intent(this, MinIntentService.class));
    } else if (hvadBlevDerKlikketPå == annullerknap) {
      MinIntentService.annulleret = true;
    }
  }
}
