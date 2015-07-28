package lekt05_grafik;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * @author Jacob Nordfalk
 */
public class Braetspil extends Activity {
  BraetspilView braetspilView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    braetspilView = new BraetspilView(this);
    setContentView(braetspilView);
  }
}

class Symbol {
  RectF r = new RectF();
  String tekst;

  Symbol(String string, RectF rectF) {
    tekst = string;
    r = rectF;
  }
}

class BraetspilView extends View {
  PointF finger = new PointF();
  ArrayList<Symbol> symboler = new ArrayList<Symbol>();
  Symbol valgtSymbol = null;
  Paint tekstStregtype = new Paint();
  Paint omridsStregtype = new Paint();

  private void init() {
    tekstStregtype.setColor(Color.GREEN);
    tekstStregtype.setTextSize(24);
    tekstStregtype.setAntiAlias(true);
    omridsStregtype.setColor(0x40ffffff);
    omridsStregtype.setStyle(Paint.Style.STROKE);
    symboler.add(new Symbol("2", new RectF(100, 20, 120, 40)));
    symboler.add(new Symbol("--", new RectF(100, 40, 120, 60)));
    symboler.add(new Symbol("3", new RectF(100, 60, 120, 80)));
  }

  // programmatisk konstruktør
  public BraetspilView(Context a) {
    super(a);
    init();
  }

  // deklarativ konstruktør
  public BraetspilView(Context a, AttributeSet at) {
    super(a, at);
    init();
  }

  @Override
  protected void onDraw(Canvas c) {
    // Spillet er beregnet til en skærm der er 480 punkter bred...
    float skærmSkala = 1;//getWidth()/480f; // ... så skalér derefter
    c.scale(skærmSkala, skærmSkala);

    for (Symbol s : symboler) {
      if (s == valgtSymbol) continue;
      tegnSymbol(c, s.r.left, s.r.top, s);
    }
    c.drawCircle(finger.x, finger.y, 5, tekstStregtype);
    if (valgtSymbol != null) tegnSymbol(c, finger.x, finger.y, valgtSymbol);
  }

  @Override
  public boolean onTouchEvent(MotionEvent e) {
    //System.out.println(e);
    // Spillet er beregnet til en skærm der er 480 punkter bred...
    float skærmSkala = 1;//getWidth()/480f; // ... så skalér derefter
    float ex = e.getX() / skærmSkala;
    float ey = e.getY() / skærmSkala;
    finger.x = ex;
    finger.y = ey;

    if (e.getAction() == MotionEvent.ACTION_DOWN) {
      for (Symbol s : symboler) {
        if (s.r.contains(ex, ey)) {
          valgtSymbol = s;
          System.out.println("valgtSymbol=" + s);
          break;
        }
      }
    }
    if (e.getAction() == MotionEvent.ACTION_MOVE) {
      if (valgtSymbol != null) {
        System.out.println("finger=" + finger);
      }
    }
    if (e.getAction() == MotionEvent.ACTION_UP) {
      if (valgtSymbol != null) {
        valgtSymbol.r.offsetTo(ex, ey);
        System.out.println("valgtSymbol.r=" + valgtSymbol.r);
      }
      valgtSymbol = null;
    }
    invalidate();
    return true;
  }

  private void tegnSymbol(Canvas c, float x, float y, Symbol s) {
    RectF r = new RectF(s.r);
    r.offsetTo(x, y);
    c.drawRect(r, omridsStregtype);
    c.drawText(s.tekst, r.left, r.bottom, tekstStregtype);
  }
}
