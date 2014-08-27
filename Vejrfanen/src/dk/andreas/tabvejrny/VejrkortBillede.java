/*
 * Kilde:
 * http://www.anddev.org/large_image_scrolling_using_low_level_touch_events-t11182.html
 */

package dk.andreas.tabvejrny;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Scroller;

public class VejrkortBillede extends View implements OnGestureListener, OnDoubleTapListener {

	private static final String TAG = "Scroll";
	private static final int WIDTH = 640;
	private static final int HEIGHT = 360;
	private static final float MAX_ZOOM = 3;

	private static final int MSG_ZOOMING_IN = 1;
	private static final int MSG_ZOOMING_OUT = 2;

	private float mX;
	private float mY;
	private Scroller mScroller;
	private Paint mTextPaint;
	private GestureDetector mGestureDetector;
	private float mScale;
	private Bitmap mAndroid;

	public VejrkortBillede(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);

                mTextPaint = new Paint();
		mTextPaint.setTextSize(18);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		mTextPaint.setStrokeWidth(2);

		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setIsLongpressEnabled(false);
		mScale = 1;
		mAndroid = BitmapFactory.decodeResource(getResources(), R.drawable.prec_nordeuro_9);
		mTextPaint.setColor(0xff000000);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		if (mScroller.computeScrollOffset()) {
			mX = mScroller.getCurrX();
			mY = mScroller.getCurrY();
			invalidate();
		}
		canvas.scale(mScale, mScale);
                System.err.println("dx = " + mX+ "    dy = " + mY);
		float dx = mX - (getWidth() / mScale) * (mX / WIDTH);
		float dy = mY - (getHeight() / mScale) * (mY / HEIGHT);

		canvas.translate(dx, dy);

		// draw droid
		canvas.drawBitmap(mAndroid, 0, 0, null);
		canvas.restore();
		//canvas.drawText("you can drag, swipe & double tap", 15, 170, mTextPaint);
	}

	public boolean onDown(MotionEvent e) {
		return true;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//		Log.d(TAG, "onFling ");

		// you can control your fling speed by adjusting velocities
		velocityX *= 0.75;
		velocityY *= 0.75;

		mScroller.fling((int) mX, (int) mY, (int) velocityX, (int) velocityY, -WIDTH, 0, -HEIGHT, 0);
		invalidate();
		return true;
	}

	public void onLongPress(MotionEvent e) {
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		mX -= distanceX / mScale;
		mY -= distanceY / mScale;
		mX = Math.max(-WIDTH, Math.min(0, mX));
		mY = Math.max(-HEIGHT, Math.min(0, mY));
		Log.d(TAG, "onScroll x:" + String.valueOf(distanceX) + " y:" + String.valueOf(distanceY) + " mX:" + String.valueOf(mX) + " mY:" + String.valueOf(mY) +" H:" + String.valueOf(getHeight()) + " W:" + String.valueOf(getWidth()));
		invalidate();
		return true;
	}

	public void onShowPress(MotionEvent e) {
	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	public boolean onDoubleTap(MotionEvent e) {
//		Log.d(TAG, "onDoubleTap ");
		mHandler.removeMessages(MSG_ZOOMING_IN);
		mHandler.removeMessages(MSG_ZOOMING_OUT);
		mHandler.sendEmptyMessage(mScale > (MAX_ZOOM / 2)? MSG_ZOOMING_OUT : MSG_ZOOMING_IN);
		return true;
	}

	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}

	public boolean onSingleTapConfirmed(MotionEvent e) {
		return false;
	}

	Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			if (msg.what == MSG_ZOOMING_IN) {
				mScale += 0.1f;
				if (mScale > MAX_ZOOM) {
					mScale = MAX_ZOOM;
				} else {
					sendEmptyMessage(msg.what);
				}
				invalidate();
			} else
			if (msg.what == MSG_ZOOMING_OUT) {
				mScale -= 0.1f;
				if (mScale < 1) {
					mScale = 1;
				} else {
					sendEmptyMessage(msg.what);
				}
				invalidate();
			}
		}
	};
}
