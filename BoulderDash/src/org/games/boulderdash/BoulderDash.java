/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.games.boulderdash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 *
 * @author AH
 */
public class BoulderDash extends Activity implements OnClickListener{

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

        Button game=(Button) findViewById(R.id.new_game);
        game.setOnClickListener(this);

        Button help=(Button) findViewById(R.id.help_button);
        help.setOnClickListener(this);

        Button highScore=(Button) findViewById(R.id.highscore_button);
        highScore.setOnClickListener(this);
      
  }

/*
 Hovedmenu : navigering til andre funktioner i app'en
 */

    public void onClick(View button) {

        Intent i=null;

        switch(button.getId()){

            case R.id.help_button:
                i =new Intent(this, HelpActivity.class);
                
                break;
        
            case R.id.new_game:
                i =new Intent(this, GameActivity.class);
                
                break;
            case R.id.highscore_button:
                i =new Intent(this, HighScoreActivity.class);

                break;
        }
        if(i!=null)
            startActivity(i);
    }
        

}
