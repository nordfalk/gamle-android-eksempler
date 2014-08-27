package org.dook.invaders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Invaders extends Activity implements OnClickListener{
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Setup click listeners for all the buttons
        View continueButton = findViewById(R.id.help_button);
        continueButton.setOnClickListener(this);
        View newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
        View highscoreButton = findViewById(R.id.hiscore_button);
        highscoreButton.setOnClickListener(this);
        View settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
    }
    
    /**
     * Handle button clicks. 
     */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_button: // Start the game
			startActivity(new Intent(Invaders.this, Game.class));
			break;
		case R.id.help_button: // Show instructions and credits
			startActivity( new Intent(this, Instruction.class));
			break;
		case R.id.hiscore_button: // Show the high score
			startActivity( new Intent(this, Highscore.class));
			break;
		case R.id.settings_button: // Go to settings
			startActivity(new Intent(this, Prefs.class));
			break;
		case R.id.exit_button: // Quit the game
			finish();
			break;
		}
	}
	
}


