package lekt09_services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

/**
 * Simpel service der, når startet, forsøger at holde app'en i hukommelsen
 *
 * @author j
 */
public class ForgrundsService extends Service {
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
    Toast.makeText(this, TAG + " onStartCommand(\n"
        + intent + " " + flags + " " + startId, Toast.LENGTH_LONG).show();
    Intent i = new Intent(this, BenytService.class);
    PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
    Notification notification = new Notification(R.drawable.logo,
        "AndroidElementer holdes i hukommelsen", System.currentTimeMillis());
    notification.setLatestEventInfo(this,
        "Bliver i hukommelsen", "Klik her for at stoppe servicen", pi);
    startForeground(42, notification);
    return START_STICKY;
  }
}
