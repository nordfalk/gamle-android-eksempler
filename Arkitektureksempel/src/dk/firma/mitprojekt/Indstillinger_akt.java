package dk.firma.mitprojekt;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Indstillinger_akt extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.indstillinger);
	}
}
