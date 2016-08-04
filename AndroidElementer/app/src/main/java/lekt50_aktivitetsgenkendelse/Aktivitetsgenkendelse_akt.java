package lekt50_aktivitetsgenkendelse;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

import lekt04_arkitektur.MinApp;
import lekt50_googlested.TekstTilTale;

/**
 * @author Jacob Nordfalk
 */
public class Aktivitetsgenkendelse_akt extends Activity implements OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
  PendingIntent pendingIntent;

  Button knap1, knap2, knap3;
  TextView tv;
  static Aktivitetsgenkendelse_akt instans;
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
    knap1.setText("Start detektering");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Stop detektering");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("System.exit()");
    tl.addView(knap3);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);


    Intent intent = new Intent(this, Aktivitetsgenkendelse_reciever.class);
    pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

    googleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(ActivityRecognition.API)
            .build();
    googleApiClient.connect();
  }


  @Override
  public void onConnected(Bundle bundle) {
    tv.append("Forbundet\n");
    TekstTilTale.instans(this).tal("Der er forbindelse med Google");
  }


  @Override
  public void onClick(View hvadBlevDerKlikketPå) {
    try {
      if (hvadBlevDerKlikketPå == knap1) {
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                googleApiClient, 10000, pendingIntent)
        .setResultCallback(this);
      } else if (hvadBlevDerKlikketPå == knap2) {
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
                googleApiClient, pendingIntent);
      } else if (hvadBlevDerKlikketPå == knap3) {
        finish();
        System.exit(0);
      }
    } catch (Throwable t) {
      t.printStackTrace();
      MinApp.langToast("Fejl: "+t);
    }
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    instans = null;
    googleApiClient.disconnect();
  }

  @Override
  public void onConnectionSuspended(int i) {
    tv.append("Ikke forbundet\n");
    TekstTilTale.instans(this).tal("Ikke forbundet med Google");
    googleApiClient.connect();
  }


  /** Håndtering af forbindelsesfejl */
  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    tv.append("Forbindelsesfejl " + connectionResult);
    TekstTilTale.instans(this).tal("Forbindelsesfejl "+connectionResult);

    if (connectionResult.hasResolution()) {
      try {
        connectionResult.startResolutionForResult(this, 9000);
      } catch (IntentSender.SendIntentException e) {
        e.printStackTrace();
      }
    } else {
      Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 9000);

      // If Google Play services can provide an error dialog
      if (errorDialog != null) {
        errorDialog.show();
      }
    }
  }

  /** Håndtering af forbindelsesfejl */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 9000 && resultCode == RESULT_OK) {
      googleApiClient.connect();
    }
  }

  @Override
  public void onResult(Status status) {
    if (status.isSuccess()) {

    }
  }
}
