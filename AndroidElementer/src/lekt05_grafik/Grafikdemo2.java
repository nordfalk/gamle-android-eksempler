package lekt05_grafik;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class Grafikdemo2 extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GrafikView2 grafikView = new GrafikView2(this);
    grafikView.setBackgroundResource(R.drawable.logo);
    setContentView(grafikView);
  }
}


class GrafikView2 extends View {

  float rotation = -45;

  public GrafikView2(Context context) {
    super(context);
  }

  @Override
  protected void onDraw(Canvas c) {

    Paint paint = new Paint(); // burde ske udenfor onDraw() for bedre ydelse
    paint.setStyle(Paint.Style.FILL);

    // lav baggrunden hvid
    paint.setColor(Color.WHITE);
    c.drawPaint(paint);

    int blå = Color.BLUE;  // blå
    blå = 0xff0000ff;  // også blå
    blå = Color.argb(255, 0, 0, 255);  // også blå
    blå = 255 * 256 * 256 * 256 + 255;  // også blå
    paint.setColor(blå);

    c.drawCircle(20, 20, 15, paint);

    paint.setAntiAlias(true);  // antialias - tegn udjævnede kanter
    c.drawCircle(60, 20, 15, paint);

    paint.setStrokeWidth(2); // sæt stregtykkelsen til 2 punkter
    paint.setColor(Color.RED);

    Path trekantPath = new Path(); // burde ske udenfor onDraw() for bedre ydelse
    trekantPath.moveTo(0, -10);
    trekantPath.lineTo(5, 0);
    trekantPath.lineTo(-5, 0);
    trekantPath.close();

    paint.setStyle(Paint.Style.STROKE); // tegn streger
    trekantPath.offset(100, 25);  // flyt trekanten ned og til højre
    c.drawPath(trekantPath, paint); // tegn den

    trekantPath.offset(40, 0);  // ryk koordinater til højre
    paint.setStyle(Paint.Style.FILL);     // udfyld området - ignorér stregtykkelsen
    c.drawPath(trekantPath, paint);

    trekantPath.offset(40, 0);  // ryk koordinater til højre
    paint.setStyle(Paint.Style.FILL_AND_STROKE); // udfyld området og tegn stregen med stregtykkelsen
    c.drawPath(trekantPath, paint);

    // tegn med STROKE (streg)
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(1);
    paint.setColor(Color.MAGENTA);
    paint.setTextSize(30);
    c.drawText("streg - Style.STROKE", 15, 85, paint);

    // tegn med FILL (fyldt)
    paint.setStyle(Paint.Style.FILL);
    paint.setTextSize(30);
    c.drawText("fyldt - Style.FILL", 15, 120, paint);

    // tegn en tyk stiplet linje
    DashPathEffect dashPath = new DashPathEffect(new float[]{20, 5}, 1);
    paint.setPathEffect(dashPath);
    paint.setStrokeWidth(5);
    c.drawLine(10, 130, 300, 150, paint);

    // tegn noget roteret tekst
    paint.setPathEffect(null);
    paint.setColor(Color.argb(128, 0, 255, 0)); // halvgennemsigtig grøn
    paint.setTextSize(48);
    String roteretTekst = "Tryk for at rotere";

    // find tekstens størrelse på skærmen
    Rect tekstomrids = new Rect();
    paint.getTextBounds(roteretTekst, 0, roteretTekst.length(), tekstomrids);

    int x = getWidth() / 2;
    int y = getHeight() / 2;
    // rotér lærredet omkring tekstens center
    c.rotate(rotation, x + tekstomrids.centerX(), y + tekstomrids.centerY());
    // tegn teksten
    c.drawText(roteretTekst, x, y, paint);

    // nulstil rotation (og translation etc)
    c.restore();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    rotation = event.getX();
    invalidate(); // Får Android til at kalde onDraw() på viewet
    return true;
  }
}

