/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lekt05_sensorer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

/**
 * Kompas - korrekt implementation
 */
public class Kompas2 extends Activity implements SensorEventListener {
  SensorManager sensorManager;
  KompasView kompasView;
  private float[] gData = new float[3];
  private float[] mData = new float[3];
  private float[] rotationsmatrix = new float[16];
  private float[] orientering = new float[3];
  final float radianerTilGrader = (float) (180.0f / Math.PI);

  @Override
  protected void onCreate(Bundle icicle) {
    super.onCreate(icicle);

    kompasView = new KompasView(this);
    setContentView(kompasView);

    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
  }

  @Override
  protected void onResume() {
    super.onResume();
    Sensor gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    Sensor msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    sensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_NORMAL);
    sensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onPause() {
    super.onPause();
    sensorManager.unregisterListener(this); // Stop med at modtage sensordata
  }

  public void onSensorChanged(SensorEvent event) {
    int type = event.sensor.getType();
    if (type == Sensor.TYPE_ACCELEROMETER) {
      System.arraycopy(event.values, 0, gData, 0, 3);
    } else {
      System.arraycopy(event.values, 0, mData, 0, 3);
    }

    SensorManager.getRotationMatrix(rotationsmatrix, null, gData, mData);
    SensorManager.getOrientation(rotationsmatrix, orientering);

    Log.d("Kompas2", Arrays.toString(orientering));

    kompasView.nordvinkel = orientering[0] * radianerTilGrader; // Vinkel til nord
    kompasView.hældning = orientering[1] * radianerTilGrader;
    kompasView.krængning = orientering[2] * radianerTilGrader;
    kompasView.invalidate();
  }

  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }
}
