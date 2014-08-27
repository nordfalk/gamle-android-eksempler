package lekt06_asynkron;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;


/**
 * Dette eksempel viser en AsyncTask der er knyttet korrekt til en aktivitet.
 * Hvis skærmen vendes knyttes AsyncTask'en korrekt til næste aktivitet og
 * fortsætter med at fungere der.
 * Se også diskussionen på
 * http://groups.google.com/group/android-developers/browse_thread/thread/e1d5b8f8a3142892
 */
public class Asynkron3Korrekt extends Activity implements OnClickListener {

  ProgressBar progressBar;
  Button knap, annullerknap;

  AsyncTaskMedUdskifteligAktivitet asyncTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    EditText editText = new EditText(this);
    editText.setText("Prøv at redigere her efter du har trykket på knapperne");
    editText.setId(117); // Giv ID så indhold huskes ved skærmvendinger
    tl.addView(editText);

    progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
    progressBar.setMax(99);
    progressBar.setId(118); // Giv ID så indhold huskes ved skærmvendinger
    tl.addView(progressBar);

    knap = new Button(this);
    knap.setText("AsyncTask med løbende opdatering og resultat");
    tl.addView(knap);

    annullerknap = new Button(this);
    annullerknap.setText("Annullér AsyncTask");
    annullerknap.setVisibility(View.GONE); // Skjul knappen
    tl.addView(annullerknap);

    setContentView(tl);

    knap.setOnClickListener(this);
    annullerknap.setOnClickListener(this);

    // Hvis der er sket en konfigurationsændring så kan det være vi har en gammel
    // asynctask som vi skal genbruge
    asyncTask = (AsyncTaskMedUdskifteligAktivitet) getLastNonConfigurationInstance();
    if (asyncTask != null) {
      asyncTask.akt = this;
      knap.setEnabled(false);
      annullerknap.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Kaldes når der sker en konfigurationsændring (skærm vendes, den dokkes i bilen, ...)
   */
  @Override
  public Object onRetainNonConfigurationInstance() {
    return asyncTask;
  }


  public void onClick(View hvadBlevDerKlikketPå) {

    if (hvadBlevDerKlikketPå == knap) {

      asyncTask = new AsyncTaskMedUdskifteligAktivitet();
      asyncTask.akt = this;
      asyncTask.execute(500, 50);
      knap.setEnabled(false);
      annullerknap.setVisibility(View.VISIBLE);
    } else if (hvadBlevDerKlikketPå == annullerknap) {

      asyncTask.cancel(false);

    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed(); //To change body of generated methods, choose Tools | Templates.
  }
}

/**
 * en AsyncTask hvor input er int, progress er double, resultat er String
 */
class AsyncTaskMedUdskifteligAktivitet extends AsyncTask<Integer, Double, String> {

  public Asynkron3Korrekt akt;

  @Override
  protected String doInBackground(Integer... param) {
    int antalSkridt = param[0];
    int ventetidPrSkridtIMilisekunder = param[1];
    for (int i = 0; i < antalSkridt; i++) {
      SystemClock.sleep(ventetidPrSkridtIMilisekunder);
      if (isCancelled()) {
        return null; // stop uden resultat
      }
      double procent = i * 100.0 / antalSkridt;
      double resttidISekunder = (antalSkridt - i) * ventetidPrSkridtIMilisekunder / 100 / 10.0;
      publishProgress(procent, resttidISekunder); // sendes som parameter til onProgressUpdate()
    }
    return "færdig med doInBackground()!"; // resultat (String) sendes til onPostExecute()
  }

  @Override
  protected void onProgressUpdate(Double... progress) {
    double procent = progress[0];
    double resttidISekunder = progress[1];
    String tekst = "arbejder - " + procent + "% færdig, mangler " + resttidISekunder + " sekunder endnu";
    Log.d("AsyncTask", tekst);
    akt.knap.setText(tekst);
    akt.progressBar.setProgress((int) procent);
  }

  @Override
  protected void onPostExecute(String resultat) {
    akt.knap.setText(resultat);
    akt.knap.setEnabled(true);
    akt.annullerknap.setVisibility(View.GONE); // Skjul knappen
    akt.asyncTask = null;
  }

  @Override
  protected void onCancelled() {
    akt.knap.setText("Annulleret før tid");
    akt.knap.setEnabled(true);
    akt.annullerknap.setVisibility(View.GONE); // Skjul knappen
    akt.asyncTask = null;
  }
}
