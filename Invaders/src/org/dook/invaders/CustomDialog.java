package org.dook.invaders;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CustomDialog extends Activity{
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customdialog);
		
		TextView textView = (TextView) findViewById(R.id.dialog_text);
		
		String text;
		
		// Set the text depending on high score.
		if(getIntent().getBooleanExtra("highscore", false)){
			text = getString(R.string.high_score_notify) + getIntent().getIntExtra("score", 0);
			textView.setText(text);
		} else {
			text = getString(R.string.no_high_score_notify) + getIntent().getIntExtra("score", 0);
			textView.setText(text);
		}
	}
		
}
