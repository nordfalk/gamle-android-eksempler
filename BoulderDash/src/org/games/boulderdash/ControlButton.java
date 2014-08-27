/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.games.boulderdash;

import org.games.boulderdash.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

/**
 *
 * @author AH
 */
public class ControlButton extends Button{

    public ControlButton(Context context, AttributeSet as ){
			super(context, as);
			this.setBackgroundColor(Color.TRANSPARENT);
			
		}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        int h=this.getHeight();
        int w=this.getWidth();
        Paint paint= new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);
        paint.setAlpha(150);
        Path path = new Path();
        
        int id =this.getId();
        
        switch(id){
        
        case R.id.up:
        	path.moveTo(w/2, 0);
        	path.lineTo(w, h);
        	path.lineTo(0, h);
        	break;
        case R.id.right:
        	path.moveTo(0, 0);            
        	path.lineTo(w, h/2);
        	path.lineTo(0, h);
        	break;	
        case R.id.left:
        	path.moveTo(0, h/2);            
        	path.lineTo(w, h);
        	path.lineTo(w, 0);
        	break;
        
        case R.id.down:
        	path.moveTo(0, 0);
        	path.lineTo(w/2, h);
        	path.lineTo(w, 0);
        	break;
        default:
        	path.moveTo(0, 0);
        	path.lineTo(w, 0);
        	path.lineTo(w, h);
        	path.lineTo(0, h);
        	paint.setColor(Color.BLACK);
        	paint.setAlpha(0);
        }
        path.close();
        canvas.drawPath(path, paint);
       
        
        
    }

}
