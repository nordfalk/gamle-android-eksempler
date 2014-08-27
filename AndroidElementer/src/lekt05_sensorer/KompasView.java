package lekt05_sensorer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.view.View;

import dk.nordfalk.android.elementer.R;

public class KompasView extends View {
  float nordvinkel, hældning, krængning;
  Paint paint = new Paint();
  Path kompaspilPath = new Path();

  // Indlæs res/drawable/bil.png
  Drawable enBil = getResources().getDrawable(R.drawable.bil);

  public KompasView(Context context) {
    super(context);

    // Lav en form som en kompas-pil
    kompaspilPath.moveTo(0, -50);
    kompaspilPath.lineTo(-20, 60);
    kompaspilPath.lineTo(0, 50);
    kompaspilPath.lineTo(20, 60);
    kompaspilPath.close();

    paint.setAntiAlias(true);
    paint.setColor(Color.BLACK);
    paint.setStyle(Paint.Style.FILL);

    enBil.setBounds(-100, -100, 100, 100);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    // Tegn en hvid baggrund
    canvas.drawColor(Color.WHITE);

    // Tegn kompas-pilen
    canvas.save();
    canvas.translate(getWidth() / 2, getHeight() / 2);
    canvas.rotate(-nordvinkel);
    canvas.drawPath(kompaspilPath, paint);
    canvas.restore();

    // Tegn også en bil for sjovs skyld
    // Den drejer afhængig af hældning og bliver trykket skæv afhængig af krængning
    canvas.save();
    canvas.translate(100, 100);
    canvas.rotate(hældning);
    canvas.skew(krængning / 20, 0);
    enBil.draw(canvas);
    canvas.restore();

  }
}
