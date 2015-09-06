package lekt05_grafik;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class Grafikdemo1 extends Activity {

  private Drawable enBil;
  private Paint tekststreg;
  private Path cirkel;
  private Paint cirkelstreg;
  private long t0;
  private GrafikView1 grafikView;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Indlæs res/drawable/bil.png
    enBil = getResources().getDrawable(R.drawable.bil);

    tekststreg = new Paint();
    tekststreg.setColor(Color.GREEN);
    tekststreg.setTextSize(24);
    tekststreg.setStyle(Paint.Style.FILL);

    cirkel = new Path();
    cirkel.addCircle(150, 150, 100, Direction.CW);

    cirkelstreg = new Paint();
    cirkelstreg.setStyle(Paint.Style.STROKE);
    cirkelstreg.setColor(Color.LTGRAY);
    cirkelstreg.setStrokeWidth(5);

    t0 = System.currentTimeMillis();

    grafikView = new GrafikView1(this);
    setContentView(grafikView);
  }

  void tegnGrafik(Canvas c) {
    int t = (int) (System.currentTimeMillis() - t0) / 10; // millisekunder sekunder siden start
    int x = t * 20 / 1000; // går fra 0 til 200
    int y = t * 40 / 1000; // går fra 0 til 400
    //System.out.println(t + " x=" + x + " y=" + y);

    c.drawPath(cirkel, cirkelstreg);
    c.drawTextOnPath("Hvornår er en Tuborg bedst?", cirkel, x, y - 100, tekststreg);

    c.rotate(t * 0.05f, x, y);  // rotér om (x,y)
    enBil.setBounds(x, y, x + 50, y + 50 + (int) (10 * Math.sin(t * Math.PI / 1000)));
    enBil.draw(c);
    c.drawText("t=" + t, x, y - 20, tekststreg);
    if (t < 10000) {
      grafikView.postInvalidateDelayed(10); // tegn igen om 1/100 sekund
    } else {
      finish(); // afslut aktiviteten hvis der er gået 10 sekunder
    }
  }

  class GrafikView1 extends View {
    // programmatisk konstruktør
    public GrafikView1(Context a) {
      super(a);
    }

    @Override
    protected void onDraw(Canvas c) {
      tegnGrafik(c);
    }
  }

}
