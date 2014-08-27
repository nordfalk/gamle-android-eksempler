package org.dook.invaders;

import android.graphics.drawable.Drawable;

public class Enemy extends Entity {
    
	private boolean edgeReached = false;
	
    public Enemy(int x, int y, Drawable sprite){
        super(x, y, 1, 0,  sprite);
        sprite.setBounds(x, y, x + 24, y + 24);
    }
    
    public void update(Game game){
        super.update(game);
        
        //Check for collisions with bullets
        for(PlayerBullet b : game.getPlayerBullets()){
            if(checkCollision(b)){
                b.setActive(false);
                this.setActive(false);
                game.addPoints(1);
            }
        }
        
        // Check if enemy has reached the edge
        edgeReached = x < 0 || x > game.getView().getWidth() - 24;
        
        // The game ends if any enemy reaches the bottom
        if(y >= game.getView().getHeight())
            game.endGame();
    }
    
    /**
     * Checks if the enemy has reached the edge.
     *
     * @return true if either edge is reached.
     */
    public boolean atEdge(){
        return edgeReached;
    }
        
    /**
     * Called by the game when an enemy has reached the edge. Instructs the
     * enemy to go down a step and inverse the speed.
     */
    public void uTurn(){
        speedx = -speedx;
        moveTo(x + speedx, y +24);
    }

    
}