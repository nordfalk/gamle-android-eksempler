package lekt05_grafik;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class Tegneprogram extends Activity {
  Tegneflade tegneflade;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    tegneflade = new Tegneflade(this);
    setContentView(tegneflade);
  }

  public static class Tegneflade extends View {
    ArrayList<Point> berøringspunkter = new ArrayList<Point>();
    Paint tekstStregtype = new Paint();

    // programmatisk konstruktør
    public Tegneflade(Context a) {
      super(a);
      tekstStregtype.setColor(Color.GREEN);
      tekstStregtype.setTextSize(24);
    }

    @Override
    public boolean onTouchEvent(MotionEvent berøring) {
      System.out.println(berøring);
      Point punktet = new Point((int) berøring.getX(), (int) berøring.getY());
      berøringspunkter.add(punktet);
      //System.out.println(berøringspunkter);
      invalidate(); // forårsager kald til onDraw()
      return true;
    }

    @Override
    protected void onDraw(Canvas c) {
      super.onDraw(c);
      c.drawText("Tryk og træk over skærmen", 0, 20, tekstStregtype);
      for (Point p : berøringspunkter) {
        c.drawCircle(p.x, p.y, 3, tekstStregtype);
      }
    }
  }
}
