package lekt06_asynkron;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

import dk.nordfalk.android.elementer.R;


/**
 * Dette eksempel viser hvorledes det er muligt at udføre message passing
 * tilbage til UI.
 * Vær opmærksom på at handler postede runnable objekter bliver kørt i den
 * tråd der starter den.
 *
 * I dette eksempel bliver der oprettet en anonym tråd, hvis
 * eneste job er at starte tingene op.
 *
 * En handler der bliver brugt til at modtage beskeder fra en runnable har
 * den fordel, at der ikke er nogen begrænsninger for hvor mange forskellige
 * typer data der kan sendes i samme omgang.
 */
public class Asynk5Message extends Activity implements OnClickListener {

  ProgressBar progressBar;
  Button knap, annullerknap;

  Asynk5Message synligAktivitet;

  // for at sikre der rent faktisk bliver afbrudt
  private static AtomicBoolean afbryd = new AtomicBoolean();

  // runnable objektet brugt til at sende beskeder fra
  private Runnable beskedRunnable;

  // besked nøgler, for at kunne kende forskel på de forskellige situationer.
  private static final int BESKED_IGANG = 1;  // der sker stadig noget.
  private static final int BESKED_SLUT = 2;   // arbejdet er udført.
  private static final int BESKED_AFBRUDT = 3; // arbejdet er afbrudt.

  // message data er bundle m. K/V par, følgende keys er brugt
  private static final String DATA_REST_TID = "rest_tid";
  private static final String DATA_PROCENT = "procent";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    EditText editText = new EditText(this);
    editText.setText("Prøv at redigere her efter du har trykket på knapperne");
    editText.setId(R.id.editText); // Giv viewet et ID så dets indhold overlever en skærmvending
    tl.addView(editText);

    progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
    progressBar.setMax(99);
    editText.setId(R.id.enKnap); // Giv viewet et ID så dets indhold overlever en skærmvending
    tl.addView(progressBar);

    knap = new Button(this);
    knap.setText("Runnable med løbende opdatering og resultat");
    tl.addView(knap);

    annullerknap = new Button(this);
    annullerknap.setText("Annullér Runnable");
    annullerknap.setVisibility(View.GONE); // Skjul knappen
    tl.addView(annullerknap);

    setContentView(tl);

    knap.setOnClickListener(this);
    annullerknap.setOnClickListener(this);

    synligAktivitet = this;

    // tjek om runnable objektet er null, og lav et nyt hvis det er.
    if (beskedRunnable == null) {
      beskedRunnable = new RunnableClass(synligAktivitet, 50, 500);
    }
  }

  @Override
  protected void onDestroy() {
    synligAktivitet = null; // Vigtigt, ellers bliver aktivitetsinstansen hængende i hukommelsen
    beskedRunnable = null;
    super.onDestroy();
  }

  public void onClick(View hvadBlevDerKlikketPå) {

    if (hvadBlevDerKlikketPå == knap) {
      afbryd.set(false);
      new Thread(beskedRunnable).start();
      knap.setEnabled(false);
      annullerknap.setVisibility(View.VISIBLE);
    } else if (hvadBlevDerKlikketPå == annullerknap) {
      afbryd.set(true); // sæt afbryd til sand, da vi ønsker at afbryde tråden
    }
  }

  /**
   * Klasse der køres i seperat tråd.
   */
  static class RunnableClass implements Runnable {

    private Message message;
    private final int antalSkridt;
    private final int ventetidPrSkridtIMilisekunder;
    private final Bundle data;
    private final Handler beskedHandler;

    RunnableClass(Asynk5Message aktivitet, int antalSkridt, int ventetidMS) {
      this.antalSkridt = antalSkridt;
      ventetidPrSkridtIMilisekunder = ventetidMS;
      data = new Bundle();
      beskedHandler = new MessagePassingHandler(aktivitet);
    }

    @Override
    public void run() {

      double procent, resttidISekunder;

      for (int i = 0; i < antalSkridt; i++) {

        SystemClock.sleep(ventetidPrSkridtIMilisekunder);

        procent = i * 100.0d / antalSkridt;
        resttidISekunder = (antalSkridt - i) * ventetidPrSkridtIMilisekunder / 100 / 10.0d;

        // tjek om der ønskes af afbrydes fra brugerens side
        if (afbryd.get()) {
          message = beskedHandler.obtainMessage(BESKED_AFBRUDT);
          data.putDouble(DATA_REST_TID, -1d);
          message.setData(data);
          message.sendToTarget();
          break;
        }

        // send normal besked om igangværende hurlumhej
        message = beskedHandler.obtainMessage(BESKED_IGANG);
        data.putDouble(DATA_PROCENT, procent);
        data.putDouble(DATA_REST_TID, resttidISekunder);
        message.setData(data);
        message.sendToTarget();
      }

      // færdig, send slut information
      message = beskedHandler.obtainMessage(BESKED_SLUT);
      data.putDouble(DATA_PROCENT, 100d);
      data.putDouble(DATA_REST_TID, 0d);
      message.setData(data);
      message.sendToTarget();

    }
  }

  /**
   * Handler klasse der modtager beskeder fra tråd og agere herefter.
   */
  static class MessagePassingHandler extends Handler {

    private Bundle data;
    private final Asynk5Message aktivitet;

    MessagePassingHandler(final Asynk5Message aktivitet) {
      this.aktivitet = aktivitet;
    }

    @Override
    public void handleMessage(final Message msg) {
      super.handleMessage(msg);

      if (msg == null) {
        return; // afbryd da da ikke forefindes nogen besked
      }

      data = msg.getData(); // snup data fra beskeden

      if (data == null) {
        return;
      }

      String tekst = "arbejder - " + Double.toString(data.getDouble(DATA_PROCENT))
              + "% færdig, mangler " + Double.toString(data.getDouble(DATA_REST_TID))
              + " sekunder endnu";

      Log.d("HandleMessage", tekst);
      if (aktivitet == null) {
        Log.d("HandleMessage", "Aktivitet ikke synlig");
        return;
      }

      // find ud af hvilken besked der blev sendt og vis information
      if (msg.what == BESKED_AFBRUDT) {
        toast("afbrudt");
      } else if (msg.what == BESKED_SLUT && !afbryd.get()) {
        toast("afsluttet");
      }

      if (msg.what == BESKED_IGANG) {
        aktivitet.knap.setText(tekst);
        aktivitet.progressBar.setProgress((int) data.getDouble(DATA_PROCENT));
      } else {
        aktivitet.knap.setText("færdig");
        aktivitet.knap.setEnabled(true);
        aktivitet.annullerknap.setVisibility(View.GONE); // Skjul knappen
      }
    }

    private void toast(CharSequence tekst) {
        Toast.makeText(aktivitet.getApplicationContext(), tekst, Toast.LENGTH_SHORT).show();
    }

  }
}

