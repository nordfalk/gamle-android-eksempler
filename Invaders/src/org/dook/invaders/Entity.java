package org.dook.invaders;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

 
/**
 * The Entity is the basic abstract game element. All moving parts of the game
 * (ship, enemies and bullets) are extensions of this class.
 */
public abstract class Entity {
    protected int x, y, speedx, speedy;
    protected boolean active = false;
    protected Drawable sprite;  // Basic visual component
    protected Rect bounds;      // Derived from the Drawable, used for position and collision detection
    
    public Entity(int x, int y, int speedx, int speedy, Drawable sprite){
        this.x = x;
        this.y = y;
        this.speedx = speedx;
        this.speedy = speedy;
        this.sprite = sprite;
        bounds = sprite.copyBounds();
        
    }
    
    /**
     * The common behaviour of all entities is simply to move across the screen
     * according to their set speed. If this is not the case this method is
     * overridden.
     */
    public void update(Game game){
        x += speedx;
        y += speedy;
        bounds = sprite.copyBounds();
        bounds.offsetTo(x, y);
        sprite.setBounds(bounds);
    }
    
    public void draw(Canvas canvas){
    	sprite.draw(canvas);
    }
    
    public void setActive(boolean active){
        this.active = active;
    }

    public boolean isActive(){
        return active;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void moveTo(int x, int y){
        this.x = x;
        this.y = y;
        Rect bounds = sprite.copyBounds();
        bounds.offsetTo(x, y);
        sprite.setBounds(bounds);
    }

    public void setSpeed(int x, int y){
        speedx = x;
        speedy = y;
    }

    /**
     * Checks against the given entity if the two collide.
     *
     * @param ent Other Entity to check against
     * @return
     */
    public boolean checkCollision(Entity ent){
        if(active && ent.active)
            return sprite.copyBounds().intersect(ent.sprite.copyBounds());
        else
            return false;
    }
    
}