/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt09_services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

/**
 * @author j
 */
public class MinIntentService extends IntentService {

  static BenytIntentService aktivitetDerSkalOpdateres;
  static boolean annulleret;

  public MinIntentService() {
    super("MinIntentService");
  }

  /**
   * Håndtag til forgrundstråden
   */
  public Handler forgrundstråd = new Handler();


  @Override
  protected void onHandleIntent(Intent intent) {
    annulleret = false;
    Log.d(getClass().getSimpleName(), "onHandleIntent( " + intent);

    for (int i = 0; i < 100; i++) {
      if (annulleret) return;
      SystemClock.sleep(100);
      final int progress = i;
//      if (aktivitetDerSkalOpdateres != null) Farligt: Sættes af hovedtråd, aflæses fra baggrundstråd.
      forgrundstråd.post(new Runnable() {
        public void run() {
          if (aktivitetDerSkalOpdateres == null) return;  // OK: Sættes af hovedtråd, aflæses fra hovedtråd
          aktivitetDerSkalOpdateres.progressBar.setProgress(progress);
          aktivitetDerSkalOpdateres.knap.setText("progress = " + progress);
        }
      });
    }

    forgrundstråd.post(new Runnable() {
      public void run() {
        if (aktivitetDerSkalOpdateres == null) return;  // Nødvendigt tjek
        aktivitetDerSkalOpdateres.knap.setText("færdig");
      }
    });
    Log.d(getClass().getSimpleName(), "onHandleIntent() færdig ");
  }
}
