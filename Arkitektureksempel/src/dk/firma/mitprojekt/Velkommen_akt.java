package dk.firma.mitprojekt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import dk.firma.mitprojekt.util.Log;

public class Velkommen_akt extends Activity implements OnClickListener {
	private Button k1;
	private Button k2;
	private Button k3;
	private TextView velkomsttekst;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.velkommen);
		k1 = (Button) findViewById(R.id.knap1);
		k2 = (Button) findViewById(R.id.knap2);
		k3 = (Button) findViewById(R.id.knap3);
		velkomsttekst = (TextView) findViewById(R.id.velkomsttekst);
		k1.setOnClickListener(this);
		k2.setOnClickListener(this);
		k3.setOnClickListener(this);

		IntentFilter ifilter = new IntentFilter(MinApp.OPDATERINGSINTENT_Stamdata);
		registerReceiver(stamdataOpdateretReciever, ifilter);
		initFraStamdata();
	}

	void initFraStamdata() {
		String txt = MinApp.stamdata.s("velkomsttekst");
		velkomsttekst.setText("v:"+txt);

		final String NØGLE = "driftmeddelelse";
		final String nyDriftmeddelelse = MinApp.stamdata.s(NØGLE);
		// Tjek i prefs om denne drifmeddelelse allerede er vist.
		final String gammelDriftmeddelelse = MinApp.prefs.getString(NØGLE, null);
		Log.d("drift_statusmeddelelse      ='" + nyDriftmeddelelse);
		if (nyDriftmeddelelse.length()>0 && !nyDriftmeddelelse.equals(gammelDriftmeddelelse)) {
			// Driftmeddelelsen er ændret. Vis den...
			Log.d("statusmeddelelse ÆNDRET FRA '" + gammelDriftmeddelelse);
			AlertDialog.Builder dialog = new AlertDialog.Builder(Velkommen_akt.this);
			dialog.setMessage(nyDriftmeddelelse);
			dialog.setPositiveButton("OK", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					// ... gem ny værdi i prefs når brugeren har trykkket OK
					MinApp.prefs.edit().putString(NØGLE, nyDriftmeddelelse).commit();
				}
			});
			dialog.show();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(stamdataOpdateretReciever);
	}

	public void onClick(View knappen) {
		if (knappen == k1) {
			Intent i = new Intent(this, Hjaelp_akt.class);
			startActivity(i);
		} else if (knappen == k2) {
			Intent i = new Intent(this, Indstillinger_akt.class);
			startActivity(i);
		} else {
			Intent i = new Intent(this, Chat_akt.class);
			startActivity(i);
		}

	}



	private BroadcastReceiver stamdataOpdateretReciever = new BroadcastReceiver() {
		@Override
		public void onReceive(Context ctx, Intent i) {
			Log.d("stamdataOpdateretReciever");
			initFraStamdata();
		}
	};
}
