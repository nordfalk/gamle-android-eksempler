package lekt50_googlested;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import lekt04_arkitektur.MinApp;


public class Stedplacering_akt extends Activity implements LocationListener, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  Button knap1, knap2, knap3;
  TextView tv;

  private Button knap4;
  private Button knap5;
  static Stedplacering_akt instans;
  private PendingIntent geofencePi;
  private GoogleApiClient googleApiClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    instans = this;

    TableLayout tl = new TableLayout(this);
    tv = new TextView(this);
    tv.setText("Detektering af brugerens aktiviteter\n");
    tl.addView(tv);

    knap1 = new Button(this);
    knap1.setText("Start logning af sted");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Præcis stedbestemmelse");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("Stop stedplacering");
    tl.addView(knap3);


    Intent intent = new Intent(this, GeofenceIntentService.class);
    // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
    // calling addGeofences() and removeGeofences().
    geofencePi  = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    knap4 = new Button(this);
    knap4.setText("Start geofence her");
    tl.addView(knap4);

    knap5 = new Button(this);
    knap5.setText("Stop geofencing");
    tl.addView(knap5);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
    knap4.setOnClickListener(this);
    knap5.setOnClickListener(this);

    googleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
    googleApiClient.connect();
    TekstTilTale.instans(this);
  }

  void log(String s) {
    Log.d(s);
    tv.append(s + "\n");
  }

  /* Kaldes af klienten hvis connect()-kaldet går godt */
  @Override
  public void onConnected(Bundle bundle) {
    log("onConnected( " + bundle);
    if (MinApp.udvikling) MinApp.langToast("ny stedplacering onConnected()");
  }

  /* Kaldes af klienten hvis connect()-kaldet fejler */
  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    log("onConnectionFailed( " + connectionResult);
    if (MinApp.udvikling) MinApp.langToast("ny stedplacering onConnectionFailed( " + connectionResult);

  /*
   * Google Play services can resolve some errors it detects.
   * If the error has a resolution, try sending an Intent to
   * start a Google Play services activity that can resolve
   * error.
   */
    if (connectionResult.hasResolution()) {
      try {
        connectionResult.startResolutionForResult(this, 9000);
      } catch (IntentSender.SendIntentException e) {
        Log.rapporterFejl(e);
      }
    } else {
      Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 9000);

      // If Google Play services can provide an error dialog
      if (errorDialog != null) {
        errorDialog.show();;
      }
    }
  }

  /* Kaldes af Google-klienten hvis forbindelsen ryger ved en fejl */
  @Override
  public void onConnectionSuspended(int grund) {
    log("onConnectionSuspended()");
    if (MinApp.udvikling) MinApp.langToast("ny stedplacering onConnectionSuspended() "+grund);
  }

  @Override
  public void onClick(View v) {
    if (v==knap1) {
      LocationRequest locationRequest = LocationRequest.create();
      locationRequest.setSmallestDisplacement(50); // 50 meter
      locationRequest.setInterval(1000 * 60 * 10); // 10 min
      locationRequest.setFastestInterval(1000 * 60 * 3); // 3 min
      locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
      log("Anmoder om "+locationRequest);
      LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
    if (v==knap2) {
      LocationRequest locationRequest = LocationRequest.create();
      locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
      locationRequest.setExpirationDuration(10000);
      locationRequest.setNumUpdates(1);
      log("Anmoder om "+locationRequest);
      LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
    if (v==knap3) {
      LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }
    if (v==knap4) {
      Location her = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
      ArrayList<Geofence> geofenceliste = new ArrayList<Geofence>();
      geofenceliste.add(new Geofence.Builder()
              .setCircularRegion(her.getLatitude(), her.getLongitude(), 100) // 100 meter
              .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_ENTER)
              .setExpirationDuration(1000 * 60 * 60 * 2) // 2 timer
              .setRequestId("100meter")
              .build());
      log("Anmoder om " + geofenceliste);
      LocationServices.GeofencingApi.addGeofences(googleApiClient, geofenceliste, geofencePi)
              .setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                  log("onAddGeofencesResult status=" + status);
                  TekstTilTale.instans(null).tal("Gå nu 100 meter væk");
                }
              });
    }
    if (v==knap5) {
      LocationServices.GeofencingApi.removeGeofences(googleApiClient, geofencePi);
    }
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (googleApiClient.isConnected()) {
      LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
      googleApiClient.disconnect();
    }
    instans = null;
  }

  @Override
  public void onLocationChanged(Location l) {
    log("onLocationChanged( " + l);
    TekstTilTale.instans(null).tal("ny placering registreret inden for " + (int) l.getAccuracy() + " meter");
  }
}
