package lekt50_googlested;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import dk.nordfalk.android.elementer.R;

/**
 * Created by j on 19-11-15.
 */
public class GeofenceIntentService extends IntentService {

  public GeofenceIntentService() {
    super("GeofenceIntentService");
  }

  protected void onHandleIntent(Intent intent) {
    GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
    if (geofencingEvent.hasError()) {
      TekstTilTale.instans(this).tal("Fejl i geofence " + geofencingEvent.getErrorCode());
      return;
    }

    int transition = geofencingEvent.getGeofenceTransition();
    String tekst = "Ukendt";
    if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) tekst = "Du er i området";
    if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) tekst = "Du er udenfor området";

    TekstTilTale.instans(this).tal(tekst);

    if (Stedplacering_akt.instans != null) {
      List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();
      Stedplacering_akt.instans.log(tekst+" "+geofences);
    }

  }
}
