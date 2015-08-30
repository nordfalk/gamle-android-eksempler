/**
 * MultitouchView - simplificeret udgave af PhotoSorterView.java
 * <p/>
 * PhotoSorterView.java er (c) Luke Hutchison (luke.hutch@mit.edu)
 * Released under the Apache License v2.
 */
package lekt20_touchinput;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.View;

import dk.nordfalk.android.elementer.R;
import lekt20_touchinput.MultiTouchController.MultiTouchObjectCanvas;
import lekt20_touchinput.MultiTouchController.PointInfo;
import lekt20_touchinput.MultiTouchController.PositionAndScale;

public class MultitouchView extends View implements MultiTouchObjectCanvas {

  private MultiTouchController multiTouchController = new MultiTouchController(this);
  private Bitmap img;
  private float x = 100;
  private float y = 100;
  ;
  private float scale = 1;
  private float angle = 0;
  private PointInfo currTouchPoints = new PointInfo();
  private Paint mLinePaintTouchPointCircle = new Paint();

  // ---------------------------------------------------------------------------------------------------
  public MultitouchView(Context context) {
    super(context);
    img = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

    mLinePaintTouchPointCircle.setColor(Color.YELLOW);
    mLinePaintTouchPointCircle.setStrokeWidth(5);
    mLinePaintTouchPointCircle.setStyle(Style.STROKE);
    mLinePaintTouchPointCircle.setAntiAlias(true);
    setBackgroundColor(Color.BLACK);
  }

  // ---------------------------------------------------------------------------------------------------

  /**
   * Pass touch events to the MT controller
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return multiTouchController.onTouchEvent(event);
  }

  /**
   * Get the image that is under the single-touch point, or return null (canceling the drag op) if none
   */
  public Object getDraggableObjectAtPoint(PointInfo pt) {
    return "ja, der var noget :-)";
  }

  /**
   * Select an object for dragging. Called whenever an object is found to be under the point (non-null is returned by getDraggableObjectAtPoint())
   * and a drag operation is starting. Called with null when drag op ends.
   */
  public void selectObject(Object obj, PointInfo touchPoint) {
    currTouchPoints.set(touchPoint);
    invalidate();
  }

  /**
   * Get the current position and scale of the selected image. Called whenever a drag starts or is reset.
   */
  public void getPositionAndScale(Object obj, PositionAndScale objPosAndScaleOut) {
    objPosAndScaleOut.set(x, y, true, scale, false, scale, scale, true, angle);
  }

  /**
   * Set the position and scale of the dragged/stretched image.
   */
  public boolean setPositionAndScale(Object obj, PositionAndScale newPosAndScale, PointInfo touchPoint) {
    currTouchPoints.set(touchPoint);
    x = newPosAndScale.getXOff();
    y = newPosAndScale.getYOff();
    scale = newPosAndScale.getScale();
    angle = newPosAndScale.getAngle();
    invalidate();
    return true;
  }

  // ---------------------------------------------------------------------------------------------------
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.save();
    canvas.scale(scale, scale, x, y);
    canvas.rotate(angle * 180.0f / (float) Math.PI, x, y);
    canvas.drawBitmap(img, x, y, null);
    canvas.restore();
    drawMultitouchDebugMarks(canvas);
  }

  // ---------------------------------------------------------------------------------------------------
  private void drawMultitouchDebugMarks(Canvas canvas) {
    if (currTouchPoints.isDown()) {
      float[] xs = currTouchPoints.getXs();
      float[] ys = currTouchPoints.getYs();
      float[] pressures = currTouchPoints.getPressures();
      int numPoints = currTouchPoints.getNumTouchPoints();
      for (int i = 0; i < numPoints; i++) {
        canvas.drawCircle(xs[i], ys[i], 10 + pressures[i] * 200, mLinePaintTouchPointCircle);
      }
      if (numPoints == 2) {
        canvas.drawLine(xs[0], ys[0], xs[1], ys[1], mLinePaintTouchPointCircle);
      }
    }
  }
}
