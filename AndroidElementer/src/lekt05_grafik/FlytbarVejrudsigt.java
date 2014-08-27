package lekt05_grafik;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import lekt03_net.ByvejrAktivitet;

/**
 * @author Jacob Nordfalk
 */
public class FlytbarVejrudsigt extends Activity {

  float x, y;
  float xFingerSidst, yFingerSidst;
  Bitmap vejrudsigt;
  View vejrudsigtView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    vejrudsigtView = new View(this) { // anonym nedarving af View
      @Override
      protected void onDraw(Canvas canvas) {
        tegnVejrudsigt(canvas);
      }
    };

    final TextView intro = new TextView(this);
    intro.setTextSize(36);
    intro.setText("Vent, indlæser vejrudsigt...");
    setContentView(intro);

    new AsyncTask() {
      @Override
      protected Object doInBackground(Object... arg) {
        try {
          int postnr = 2500;
          vejrudsigt = ByvejrAktivitet.opretBitmapFraUrl("http://servlet.dmi.dk/byvejr/servlet/byvejr_dag1?by=" + postnr + "&mode=long");
          return "OK";
        } catch (Exception ex) {
          return "fejl: " + ex;
        }
      }

      @Override
      protected void onPostExecute(Object resultat) {
        if ("OK".equals(resultat)) {
          setContentView(vejrudsigtView);
        } else {
          intro.append("\n\nBeklager, vejrudsigten ku' ikke hentes\n\n" + resultat);
        }
      }
    }.execute();
  }


  public void tegnVejrudsigt(Canvas canvas) {
    Paint stregtype = new Paint();
    canvas.drawBitmap(vejrudsigt, x, y, stregtype);
    canvas.drawText("Dagens vejrudsigt:", 10, 5, stregtype);
  }

  @Override
  public boolean onTrackballEvent(MotionEvent event) {
    x = x + event.getX();
    y = y + event.getY();
    vejrudsigtView.invalidate();
    return true;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    System.out.println(event);
    System.out.println(event.getAction());

    if (event.getAction() == MotionEvent.ACTION_MOVE) {
      // husk startpunkt
      float dx = event.getX() - xFingerSidst;
      float dy = event.getY() - yFingerSidst;
      x = x + dx;
      y = y + dy;
      vejrudsigtView.invalidate();
    }
    xFingerSidst = event.getX();
    yFingerSidst = event.getY();
    return true;
  }
}
