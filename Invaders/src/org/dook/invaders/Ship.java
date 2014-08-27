package org.dook.invaders;

import android.graphics.drawable.Drawable;

public class Ship extends Entity{
	
    public Ship(int x, int y, Drawable sprite){
        super(x, y, 0, 0, sprite);
        sprite.setBounds(x, y, x + 24, x + 24);
    }

    public void update(Game game){
    	super.update(game);
    	
    	// Don't let the ship go beyond the edge of the screen
    	if(getX() > game.getView().getWidth() - sprite.getBounds().width()){
    		x = game.getView().getWidth() - sprite.getBounds().width();
    		speedx = 0; 
    	}    		
    	if(getX() < 0){
    		speedx = 0;
    		x = 0;
    	}
    	
    	// Check for collisions
        for(EnemyBullet b : game.getEnemyBullets()){
            if(checkCollision(b)){
                b.setActive(false);
                game.takeLife(1);
            }
        }
                
    }
}
