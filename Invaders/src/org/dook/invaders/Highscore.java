package org.dook.invaders;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Highscore extends Activity{
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscore);
		
		SharedPreferences highscore = getSharedPreferences("highscore", 0);
		
		TextView text = (TextView) findViewById(R.id.highscore_text);
		String scorelist = "";
		
		// Generate a list of the names and scores in the SharedPreferences
		for(int n = 0 ; n < 10 ; n++){
			scorelist += n + 1 + ". "
				       + highscore.getString("name" + n, getString(R.string.no_name)) 
					   + "  -  "
					   + highscore.getInt("score" + n, 0)
					   + "\n";
		}
		
		text.setText(scorelist);
	}
}

