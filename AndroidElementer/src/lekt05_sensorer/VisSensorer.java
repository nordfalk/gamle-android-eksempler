package lekt05_sensorer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class VisSensorer extends Activity implements SensorEventListener {
  TextView textView;
  String[] senesteMålinger = new String[20];
  SensorManager sensorManager;
  MediaPlayer enLyd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    textView = new TextView(this);
    //textView.setTypeface(Typeface.MONOSPACE);
    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
  }

  @Override
  protected void onResume() {
    super.onResume();
    //int hyppighed = SensorManager.SENSOR_DELAY_NORMAL;
    int hyppighed = 250000; // 4 gange i sekundet

		/*
    Sensor orienteringsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		if (orienteringsSensor != null) {
		sensorManager.registerListener(this, orienteringsSensor, hyppighed);
		} else {
		textView.setText("Fejl. Din telefon har ikke en orienteringssensor");
		}
		/**/
    for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)) {
      System.out.println("sensor=" + sensor);
      sensorManager.registerListener(this, sensor, hyppighed);
    }
    /**/
    enLyd = MediaPlayer.create(this, R.raw.dyt);
  }

  @Override
  protected void onPause() {
    super.onPause();
    System.out.println("nu blev onPause() kaldt");
    sensorManager.unregisterListener(this);
    enLyd.release();
  }

  public void onSensorChanged(SensorEvent e) {
    int sensortype = e.sensor.getType();

    String måling = "Type " + sensortype + " " + e.sensor.getName() + "\n"
        + "Fra: " + e.sensor.getVendor() + "  (" + e.sensor.getPower() + " mA)\n"
        + "Tid: " + e.timestamp + "  præcision: " + e.accuracy + "\n";

    for (float v : e.values)
      måling = måling + String.format("%9.4f", v); // Normalt 3, men det er set på en Nexus 5 at der er en sensor med kun 1 værdi og med 5 værdier

    if (sensortype == Sensor.TYPE_ORIENTATION) {
      måling = måling + "\nnordvinkel hældning krængning";

    }

    if (sensortype == Sensor.TYPE_ACCELEROMETER) {
      // Tjek om det er 3 * normal tyngdeaccelerationen - se http://da.wikipedia.org/wiki/Tyngdeacceleration
      double sum = Math.abs(e.values[0]) + Math.abs(e.values[1]) + Math.abs(e.values[2]);
      if (sum > 3 * SensorManager.GRAVITY_EARTH) {
        if (!enLyd.isPlaying()) {
          enLyd.start(); // BANG!
        }
        måling = måling + "\nBANG!!";
      }
    }

    Log.d("VisSensorer", måling);

    if (sensortype < senesteMålinger.length) {
      senesteMålinger[sensortype] = måling;
    }

    StringBuilder tekst = new StringBuilder();
    // Tilføj alle de forskellige sensorers seneste målinger til tekst
    for (String enMåling : senesteMålinger) {
      if (enMåling != null) {
        tekst.append(enMåling).append("\n\n");
      }
    }
    tekst.append("=========== Aktuel måling ===========\n" + måling);


    textView.setText(tekst);
  }

  public void onAccuracyChanged(Sensor sensor, int præcision) {
    // ignorér - men vi er nødt til at have metoden for at implementere interfacet
  }
}
