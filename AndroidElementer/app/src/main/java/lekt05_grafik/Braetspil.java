package lekt05_grafik;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;

import java.util.ArrayList;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class Braetspil extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(new BraetspilView(this));
  }

  class Brik {
    RectF rectF = new RectF();
    String tekst;

    Brik(String string, int x, int y) {
      tekst = string;
      rectF = new RectF(x + 2, y + 2, x + 38, y + 38);
    }
  }


  class BraetspilView extends View {
    PointF finger = new PointF();
    ArrayList<Brik> brikker = new ArrayList<>();
    Brik valgtBrik = null;
    Paint tekstStregtype = new Paint();
    Paint brikStregtype = new Paint();
    Paint brikStregtypeValgt;

    // programmatisk konstruktør
    public BraetspilView(Context context) {
      super(context);
      tekstStregtype.setColor(Color.GREEN);
      tekstStregtype.setTextSize(24);
      tekstStregtype.setAntiAlias(true);
      brikStregtype.setColor(Color.GRAY);
      brikStregtype.setStyle(Paint.Style.FILL);
      brikStregtype.setAntiAlias(true);
      brikStregtype.setStrokeWidth(2);
      brikStregtypeValgt = new Paint(brikStregtype);
      brikStregtypeValgt.setStyle(Paint.Style.STROKE);
      brikStregtypeValgt.setColor(Color.RED);

      brikker.add(new Brik("6", 30, 30));
      brikker.add(new Brik("+", 80, 80));
      brikker.add(new Brik("2", 140, 40));
      brikker.add(new Brik("=", 130, 90));
      brikker.add(new Brik("8", 170, 130));

      Brik forklaring = new Brik("Få regnestykket til at passe", 40, 280);
      forklaring.rectF.right += 280;
      brikker.add(forklaring);
    }

    @Override
    protected void onDraw(Canvas c) {
      // Spillet er beregnet til en skærm der er 480 punkter bred...
      float skærmSkala = getWidth() / 480f; // ... så skalér derefter
      c.scale(skærmSkala, skærmSkala);

      // Tegn felter
      for (int i = 0; i <= 10; i++) {
        c.drawLine(i * 40, 0, i * 40, 400, brikStregtype);
        c.drawLine(0, i * 40, 400, i * 40, brikStregtype);
      }
      // Tegn først alle brikker, undtagen den valgte
      for (Brik brik : brikker) {
        if (brik == valgtBrik) continue;
        c.drawRoundRect(brik.rectF, 10, 10, brikStregtype);
        c.drawText(brik.tekst, brik.rectF.left + 13, brik.rectF.bottom - 10, tekstStregtype);
      }

      // Tegn den valgte brik til sidst, på fingerens plads
      if (valgtBrik != null) {
        RectF rectF = new RectF(valgtBrik.rectF);
        rectF.offsetTo(finger.x - rectF.width() / 2, finger.y - rectF.height() / 2);
        c.drawRoundRect(rectF, 10, 10, brikStregtype);
        c.drawText(valgtBrik.tekst, rectF.left + 13, rectF.bottom - 10, tekstStregtype);
        //fixerTilBane(rectF);
        c.drawRoundRect(rectF, 12, 12, brikStregtypeValgt);
      } else {
        // Ingen brik valgt - tegn finger
        c.drawCircle(finger.x, finger.y, 15, brikStregtypeValgt);
      }
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
        for (Brik s : brikker) {
          if (s.rectF.contains(ex, ey)) {
            valgtBrik = s;
            System.out.println("valgtBrik=" + s);
            break;
          }
        }
      }
      if (e.getAction() == MotionEvent.ACTION_MOVE) {
        if (valgtBrik != null) {
          System.out.println("finger=" + finger);
        }
      }
      if (e.getAction() == MotionEvent.ACTION_UP && valgtBrik != null) {
        RectF rectF = valgtBrik.rectF;
        rectF.offsetTo(finger.x - rectF.width() / 2, finger.y - rectF.height() / 2);
        fixerTilBane(rectF);
        System.out.println("valgtBrik.rectF=" + valgtBrik.rectF);
        valgtBrik = null;
        boolean korrekt = true;
        for (int i = 0; i < 4; i++) {
          Brik s1 = brikker.get(i);
          Brik s2 = brikker.get(i + 1);
          float afstandTilKorrekt = Math.abs(s1.rectF.top - s2.rectF.top) + Math.abs(s1.rectF.left + 40 - s2.rectF.left);
          Log.d("Braetspil", s1.tekst + " til " + s2.tekst + " afstandTilKorrekt = " + afstandTilKorrekt);
          if (afstandTilKorrekt > 1) korrekt = false;
        }
        if (korrekt) MediaPlayer.create(getContext(), R.raw.dyt).start();
        else this.playSoundEffect(SoundEffectConstants.CLICK);

      }
      invalidate();
      return true;
    }

    private void fixerTilBane(RectF rectF) {
      int left = Math.round(rectF.left / 40) * 40 + 2;
      int top = Math.round(rectF.top / 40) * 40 + 2;
      rectF.offsetTo(left, top);
    }
  }
}