package lekt50_aktivitetsgenkendelse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.Date;

import lekt50_googlested.Log;
import lekt50_googlested.TekstTilTale;


/**
 * Created by j on 10-11-13.
 */
public class Aktivitetsgenkendelse_reciever extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {

    if (ActivityRecognitionResult.hasResult(intent)) {

      ActivityRecognitionResult res = ActivityRecognitionResult.extractResult(intent);

      DetectedActivity akt = res.getMostProbableActivity();
      String aktivitetsnavn = getBeskrivelse(akt.getType());

      String tekst = aktivitetsnavn+" med "+ akt.getConfidence() +" procents sandsynlighed";
      TekstTilTale.instans(context).tal(tekst);

      if (Aktivitetsgenkendelse_akt.instans != null) {
        Aktivitetsgenkendelse_akt.instans.tv.append("\n" + new Date() + "\n");
        for (DetectedActivity a : res.getProbableActivities()) {
          String log = a.getType() + ":" + getBeskrivelse(a.getType())+" "+ +a.getConfidence()+ "%\n";
          Aktivitetsgenkendelse_akt.instans.tv.append(log);
          Log.d(log);
        }
      }
    }
  }

  private String getBeskrivelse(int activityType) {
    switch (activityType) {
      case DetectedActivity.IN_VEHICLE:
        return "Du kører bil";
      case DetectedActivity.ON_BICYCLE:
        return "Du cykler";
      case DetectedActivity.ON_FOOT:
        return "Du er til fods";
      case DetectedActivity.WALKING:
        return "Du går";
      case DetectedActivity.STILL:
        return "Telefonen ligger stille";
      case DetectedActivity.TILTING:
        return "Du holder telefonen";
      case DetectedActivity.UNKNOWN:
    }
    return "Ukendt aktivitet";
  }
}
