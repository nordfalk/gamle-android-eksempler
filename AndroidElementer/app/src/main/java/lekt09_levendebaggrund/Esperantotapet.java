/*
 * Copyright (C) 2009 The Android Open Source Project
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
 *
 * Ændret/ŝanĝta de: Jacob Nordfalk
 */
package lekt09_levendebaggrund;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Esperantotapet extends WallpaperService {

  @Override
  public Engine onCreateEngine() {
    return new EsperantoEngine();
  }

  class EsperantoEngine extends Engine {

    Handler guiTråd = new Handler();
    Paint stjernePaint = new Paint();
    Paint tekstPaint = new Paint();
    Path stjernePath;
    //Drawable enBil = getResources().getDrawable(R.drawable.bil);
    PointF berøring = new PointF(-1, -1);
    PointF center = new PointF();
    float offset;
    private String tekst = "Esperantostjerne fra AndroidElementer";
    private final Runnable gentegnRunnable = new Runnable() {

      public void run() {
        gentegn();
      }
    };

    @Override
    public void onCreate(SurfaceHolder surfaceHolder) {
      stjernePaint.setColor(0xff80ff80);
      stjernePaint.setAntiAlias(true);

      tekstPaint.setColor(0xff00a000);
      tekstPaint.setTextSize(24);
      // By default we don't get touch events, so enable them.
      setTouchEventsEnabled(true);
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
      gentegn();
    }

    @Override
    public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      // store the center of the surface, so we can draw the cube in the right spot
      center.x = width / 2.0f;
      center.y = height / 2.0f;
      if (stjernePath == null) {
        float m = Math.min(width, height) / 2.0f;
        stjernePath = new Path();

        double v = 2 * PI / 5;
        stjernePath.moveTo(0, m);
        stjernePath.lineTo((float) (m * sin(v * 3)), (float) (m * cos(v * 3)));
        stjernePath.lineTo((float) (m * sin(v * 1)), (float) (m * cos(v * 1)));
        stjernePath.lineTo((float) (m * sin(v * 4)), (float) (m * cos(v * 4)));
        stjernePath.lineTo((float) (m * sin(v * 2)), (float) (m * cos(v * 2)));
        stjernePath.close();
      }
      gentegn();
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
      offset = xOffset;
      gentegn();
    }

    /*
     * Store the position of the touch event so we can use it for drawing later
     */
    @Override
    public void onTouchEvent(MotionEvent event) {
      if (event.getAction() != MotionEvent.ACTION_UP) {
        berøring.x = event.getX();
        berøring.y = event.getY();
      } else {
        berøring.x = berøring.y = -1;
      }
      gentegn();
    }

    /*
     * Draw one frame of the animation. This method gets called repeatedly
     * by posting a delayed Runnable. You can do any drawing you want in
     * here.
     */
    void gentegn() {
      // Reschedule the next redraw
      guiTråd.removeCallbacks(gentegnRunnable);
      if (!isVisible()) {
        return;
      }
      guiTråd.postDelayed(gentegnRunnable, 1000);

      SurfaceHolder holder = getSurfaceHolder();
      Canvas c = null;
      try {
        c = holder.lockCanvas();
        if (c != null) {
          tegnTapet(c);
        }
      } finally {
        if (c != null) {
          holder.unlockCanvasAndPost(c);
        }
      }
    }

    /*
     * Tegn tapetet
     */
    void tegnTapet(Canvas c) {
      // Først en stjerne
      c.save();
      c.translate(center.x, center.y);
      c.drawColor(0xff000000);

      float vinkel = System.currentTimeMillis() / 1000 % 36000 + offset * 360 * 2;
      //Log.d("Esperantotapet", "vinkel = "+vinkel);
      c.rotate(vinkel);
      c.drawPath(stjernePath, stjernePaint);
      c.drawTextOnPath(tekst, stjernePath, 0, -20, tekstPaint);
      c.restore();

      // Tegn evt berøringspunkt
      if (berøring.x >= 0) {
        c.drawCircle(berøring.x, berøring.y, 80, tekstPaint);
      }
    }

    @Override
    public Bundle onCommand(String action, int x, int y, int z, Bundle extras, boolean resultRequested) {
      tekst = "Fik kommando: " + action + " " + x + "," + y + "," + z + " " + extras;
      Log.d("Esperantotapet", tekst);
      return null;
    }
  }
}
