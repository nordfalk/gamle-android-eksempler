package dk.firma.mitprojekt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Hjaelp_akt extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hjaelp);


		String hjælpetekst = MinApp.stamdata.json.optString("hjælpetekst", "Hjælpen er ikke tilgængelig");

		TextView tv = (TextView) findViewById(R.id.hjælpetekst);
		tv.setText(hjælpetekst);
	}
}
