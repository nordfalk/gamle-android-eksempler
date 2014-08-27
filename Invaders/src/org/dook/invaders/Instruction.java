package org.dook.invaders;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Instruction extends Activity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		WebView webview = new WebView(this);
		webview.loadUrl("file:///android_asset/instructions.html");
		setContentView(webview);
	}

}
