package org.dook.invaders;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GraphicsView extends SurfaceView implements SurfaceHolder.Callback {
	protected GameThread thread;
		
	public GraphicsView(Context context){
		super(context);
		getHolder().addCallback(this);
		thread = new GameThread(getHolder(), this);
	}

	public void onInitialize() {
		
	}

	public void onUpdate() {
		
	}
	
	@Override
	public void onDraw(Canvas canvas){
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		/*
		 * the thread is started once the surface is created
		 */
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		/*
		 * The thread is stopped when the surface is destroyed. 
		 * If the activity is no longer is visible for example.
		 */
		boolean retry = true;
		thread.setRunning(false);
		while(retry){
			try{
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				
			}
		}
	}
}
