package org.dook.invaders;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
	
	public static String getMoveSettings(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("move", "touch");
	}
	
	public static String getShootSettings(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("shoot", "dpad");
	}
	
	public static String getPlayerName(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("playername",  context.getString(R.string.default_name));
	}
}