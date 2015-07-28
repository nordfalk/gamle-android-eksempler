package lekt05_sensorer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

/**
 * Kan du holde bilen på skærmen uden at ramme stjernerne?
 * Som tiden går bliver sidevinden kraftigere og kraftigere...
 */
public class Balancespil extends Activity implements SensorEventListener {
  SensorManager sensorManager;
  Sensor sensor;
  WakeLock wakeLock;

  float hældning, krængning, bilX, bilY;
  float[][] stjernerXY = new float[6][4]; // 6 stjerner der hver har: x, y, xfart, yfart
  boolean duDøde;
  long startTid = 0;
  int vindretning;
  int pointOpnået = 0;
  int maxPointOpnået = 0;

  SpilView spilView;

  @Override
  protected void onCreate(Bundle icicle) {
    super.onCreate(icicle);

    spilView = new SpilView(this);
    setContentView(spilView);

    sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

    try {
      PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
      // Hold skærmen helt tændt, også lidt efter...
      wakeLock = powerManager.newWakeLock(
          PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "Spil");
    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(this, "Kunne ikke holde skærmen tændt:\n" + e, Toast.LENGTH_LONG).show();
      Toast.makeText(this, "Har du husket:\n<uses-permission android:name=\"android.permission.WAKE_LOCK\"/>\n i manifestet?", Toast.LENGTH_LONG).show();
    }

    for (float[] pxy : stjernerXY) {
      pxy[0] = 800 * (float) Math.random() - 400; // x
      pxy[1] = 800 * (float) Math.random() - 400; // y
      pxy[2] = 200 * (float) Math.random() - 100; // hastighed x
      pxy[3] = 200 * (float) Math.random() - 100; // hastighed y
    }

  }

  @Override
  protected void onResume() {
    super.onResume();
    sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    if (wakeLock != null) wakeLock.acquire(); // Hold skærmen tændt
    startTid = System.currentTimeMillis();
  }

  @Override
  protected void onPause() {
    super.onPause();
    sensorManager.unregisterListener(this); // Stop med at modtage sensordata
    if (wakeLock != null) wakeLock.release(); // Frigiv låsen på at holde skærmen tændt
  }

  long sidsteTid = 0;

  public void onSensorChanged(SensorEvent event) {
    float dt = (event.timestamp - sidsteTid) / 1000000000f; // sekunder siden sidste måling
    sidsteTid = event.timestamp;
    if (dt > 1) {
      System.out.println("dt = " + dt);
      return; // Ignorer hvis der er mere end 1 sekund mellem dem
    }

    // pointOpnået opnået, dvs antal sekunder siden spillet startede
    pointOpnået = (int) (System.currentTimeMillis() - startTid) / 1000;
    if (pointOpnået > maxPointOpnået) {
      maxPointOpnået = pointOpnået; // ny rekord!
    }

    hældning = event.values[1];
    krængning = event.values[2];

    // Beregn vinden
    vindretning = (360 * pointOpnået * 3 / 5) % 360; // en ny retning hvert sekund
    double r = Math.toRadians(vindretning); // vinklen i radianer
    int vindstyrke = 10 * pointOpnået; // vinden tager mere og mere til!
    float vindDX = (float) Math.cos(r) * vindstyrke * dt;
    float vindDY = (float) Math.sin(r) * vindstyrke * dt;

    bilX = bilX - 50 * krængning * dt + vindDX;
    bilY = bilY - 50 * hældning * dt + vindDY;

    duDøde = false;
    if (bilX * bilX + bilY * bilY > 100000) {
      // Bil for langt væk - du døde - nulstil
      duDøde = true; // baggrunden tegnes rødt
    }

    // Opdater partiklernes positioner og fart
    for (float[] pxy : stjernerXY) {
      pxy[0] = pxy[0] + vindDX + pxy[2] * dt;
      pxy[1] = pxy[1] + vindDY + pxy[3] * dt;
      // Er den fløjet ud af skærmen så giv den koordinater i modsatte side
      if (Math.signum(vindDX) * pxy[0] > 400) pxy[0] = -pxy[0];
      if (Math.signum(vindDY) * pxy[1] > 400) pxy[1] = -pxy[1];

      // Rammer den bilen så...
      double dist2 = Math.pow(pxy[0] - bilX, 2) + Math.pow(pxy[1] - bilY, 2);
      if (dist2 < 2500) { // Stjernen er mindre end 50 punkter fra bilen
        System.out.println(dist2);
        duDøde = true; // baggrunden tegnes rødt
        pxy[0] = -200; // Flyt punktet væk så vi ikke rammer det igen med det samme
      }
    }

    if (duDøde) {
      bilX = bilY = 0;
      try {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(100);
      } catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(this, "Kunne ikke vibrere med telefonen:\n" + e, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Har du husket:\n<uses-permission android:name=\"android.permission.VIBRATE\"/>\n i manifestet?", Toast.LENGTH_LONG).show();
      }
      startTid = System.currentTimeMillis();
    }

    spilView.invalidate();
  }

  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }


  class SpilView extends View {
    Paint paint = new Paint();
    Path vindretningspilPath = new Path();

    // Indlæs res/drawable/bil.png
    Drawable enBil = getResources().getDrawable(R.drawable.bil);

    // Indlæs Android-resurse android-sdk/platforms/android-*/data/res/drawable/star_on.png
    Drawable enStjerne = getResources().getDrawable(android.R.drawable.star_on);

    public SpilView(Context context) {
      super(context);

      // Lav en form som en kompas-pil
      vindretningspilPath.moveTo(0, -50);
      vindretningspilPath.lineTo(-20, 60);
      vindretningspilPath.lineTo(0, 50);
      vindretningspilPath.lineTo(20, 60);
      vindretningspilPath.close();

      paint.setAntiAlias(true);
      paint.setColor(Color.BLACK);
      paint.setStyle(Paint.Style.FILL);
      paint.setTextSize(36);

      enBil.setBounds(-50, -50, 50, 50);
      enStjerne.setBounds(-20, -20, 20, 20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
      // Tegn en hvid baggrund, eller rød hvis stjernen kom for langt væk
      canvas.drawColor(duDøde ? Color.RED : Color.WHITE);

      // Spillet er beregnet til en skærm der er 480 punkter bred...
      float skærmSkala = getWidth() / 480f; // ... så skalér derefter
      canvas.scale(skærmSkala, skærmSkala);

      // Tegn point
      canvas.drawText("" + pointOpnået, 20, 40, paint);
      canvas.drawText("" + maxPointOpnået, 20, 80, paint);

      // Tegn pilen der viser vindretningen
      canvas.save();
      canvas.translate(getWidth() / skærmSkala - 50, 50);
      canvas.rotate(vindretning + 90);
      canvas.drawPath(vindretningspilPath, paint);
      canvas.restore();

      // Flyt koordinatsystem til midten
      canvas.translate(getWidth() / skærmSkala / 2, getHeight() / skærmSkala / 2);

      // Tegn bilen
      canvas.save();
      canvas.translate(bilX, bilY);
      enBil.draw(canvas);
      canvas.restore();

      // Tegn stjernerne
      for (float[] pxy : stjernerXY) {
        canvas.save();
        canvas.translate(pxy[0], pxy[1]);
        canvas.rotate(krængning * 3);
        enStjerne.draw(canvas);
        canvas.restore();
      }
    }
  }
}
