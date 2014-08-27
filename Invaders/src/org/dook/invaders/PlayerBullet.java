package org.dook.invaders;

import android.graphics.drawable.Drawable;

public class PlayerBullet extends Entity{

    public PlayerBullet(Drawable sprite){
        super(0, 0, 0, -5, sprite);
        sprite.setBounds(x, y, 6, 15);
    }
    
    public void update(Game game){
        if(y < 0)
            setActive(false);
        
        super.update(game);
    }

}
