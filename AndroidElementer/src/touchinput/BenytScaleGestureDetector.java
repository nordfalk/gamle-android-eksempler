package touchinput;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.widget.TextView;

/**
 * @author Jacob Nordfalk
 */
public class BenytScaleGestureDetector extends Activity implements OnScaleGestureListener {

  ScaleGestureDetector detector;
  TextView tv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    detector = new ScaleGestureDetector(this, this); // Context, OnScaleGestureListener
    tv = new TextView(this);
    tv.setText("Spred fingrene på skærmen eller knib dem sammen");
    setContentView(tv);
  }

  /**
   * Send hændelser videre til detektoren
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return detector.onTouchEvent(event);
  }

  public void log(String tekst, ScaleGestureDetector sgd) {
    tekst = tekst + "\n" + sgd.getScaleFactor()
        + "\n(" + sgd.getFocusX() + "," + sgd.getFocusY() + ")";
    tv.setText(tekst);
    Log.d("Gestus", tekst);
  }


  public boolean onScale(ScaleGestureDetector sgd) {
    log("onScale()", sgd);
    return false;
  }

  public boolean onScaleBegin(ScaleGestureDetector sgd) {
    log("onScaleBegin()", sgd);
    return true;
  }

  public void onScaleEnd(ScaleGestureDetector sgd) {
    log("onScaleEnd()", sgd);
  }
}
