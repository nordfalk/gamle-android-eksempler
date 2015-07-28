/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt05_grafik;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.Button;

/**
 * @author j
 */
public class ButtonPaaHovedet extends Button {

  public ButtonPaaHovedet(Context c) {
    super(c);
  }

  Paint p = new Paint();

  @Override
  protected void onDraw(Canvas c) {
    c.save();
    c.rotate(180, getWidth() / 2, getHeight() / 2);
    c.scale(0.8f, 1);
    super.onDraw(c);
    c.restore();
    c.drawText("på hovedet", 5, getHeight() / 2, p);
  }
}
