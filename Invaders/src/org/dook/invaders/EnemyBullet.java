package org.dook.invaders;

import android.graphics.drawable.Drawable;

public class EnemyBullet extends Entity{


    public EnemyBullet(Drawable sprite){
        super(0, 0, 0, 3, sprite);
        sprite.setBounds(x, y, 6, 15);
    }
    
    public void update(Game game){
        if(y > game.getView().getHeight())
            setActive(false);
            
        super.update(game);
    }
    
}
