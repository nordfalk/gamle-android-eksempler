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
 * Simpelt kompas.
 * Bemærk at Sensor.TYPE_ORIENTATION frarådes, se Kompas2 for korrekt implementation
 */
public class Kompas extends Activity implements SensorEventListener {
  SensorManager sensorManager;
  KompasView kompasView;

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
    Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onPause() {
    super.onPause();
    sensorManager.unregisterListener(this); // Stop med at modtage sensordata
  }

  public void onSensorChanged(SensorEvent event) {
    Log.d("Kompas", Arrays.toString(event.values));
    kompasView.nordvinkel = event.values[0]; // Vinkel til nord
    kompasView.hældning = event.values[1];
    kompasView.krængning = event.values[2];
    kompasView.invalidate();
  }

  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }
}

    /*
    Eksempel på hvordan data kunne logges til en server

    SensorObs so = new SensorObs(event.timestamp, event.values);
    SensorData.liste.add(so);
    System.out.println("&t=" + so.timestamp + "&v=" + so.values[0]);

    long nu = System.currentTimeMillis();
    if (SensorData.sidstDerBlevSendtData < nu - 1000*10) {
      SensorData.sidstDerBlevSendtData = nu;
      System.out.println("SensorData.sidstDerBlevSendtData = "+SensorData.sidstDerBlevSendtData);

      new AsyncTask() {
        int n = 0;
        @Override
        protected Object doInBackground(Object... params) {
          System.out.println("doInBackground START");

          try {
            String data = "";
            for (; n<SensorData.liste.size(); n++) {
              SensorObs sobs = SensorData.liste.get(n);
              data = data + "_t="+sobs.timestamp+"_v="+sobs.values[0];
            }

            URL url = new URL("http://javabog.dk/sensorObs_id=jacob"+data);
            URLConnection uc = url.openConnection();
            uc.setDoOutput(true);
            uc.connect();
            uc.getOutputStream();
            System.out.println(""+url);
            Object c = url.getContent();
            System.out.println("c="+c);

          } catch (Exception ex) {
            ex.printStackTrace();
            n = 0;
          }
          System.out.println("doInBackground SLUT");

          return "ok";
        }

        @Override
        protected void onPostExecute(Object result) {
          SensorData.liste = new ArrayList<SensorObs>(
              SensorData.liste.subList(n, SensorData.liste.size()));
        }

      }.execute();


    }
    */

