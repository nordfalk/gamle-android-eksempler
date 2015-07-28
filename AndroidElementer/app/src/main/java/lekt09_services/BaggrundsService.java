package lekt09_services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Simpel service der, når startet, forsøger at holde app'en i hukommelsen
 *
 * @author j
 */
public class BaggrundsService extends Service {
  final String TAG = getClass().getName();

  /**
   * Service-mekanik. Ligegyldig da vi kører i samme proces og ikke
   * ønsker at binde til denne service
   */
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    Toast.makeText(this, TAG + " onCreate", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onDestroy() {
    Toast.makeText(this, TAG + " onDestroy", Toast.LENGTH_LONG).show();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Bundle b = intent.getExtras();
    Toast.makeText(this, TAG + " onStartCommand(\n"
        + intent + " " + " " + flags + " " + startId, Toast.LENGTH_LONG).show();
    return START_STICKY;
  }
}
