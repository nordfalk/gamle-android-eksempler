package org.dook.invaders;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
	private SurfaceHolder surfaceHolder;
	private GraphicsView graphicsView;
	private boolean running = false;
	
	public GameThread(SurfaceHolder surfaceHolder, GraphicsView graphicsView){
		this.surfaceHolder = surfaceHolder;
		this.graphicsView = graphicsView;
		
		graphicsView.onInitialize();
	}
	
	public void setRunning(boolean b){
		running = b;
	}
	
	public void run() {
		Canvas canvas;
		
		while(running){
			canvas = null;
			graphicsView.onUpdate(); // Update game
			
			try {
				canvas = surfaceHolder.lockCanvas();
				synchronized (surfaceHolder){
					graphicsView.onDraw(canvas); // Draw game			
				}				
			} finally {
				if(canvas != null)
					surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
}
