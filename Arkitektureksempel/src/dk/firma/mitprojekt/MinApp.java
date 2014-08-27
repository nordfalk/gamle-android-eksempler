package dk.firma.mitprojekt;

import dk.firma.mitprojekt.logik.Stamdata;
import dk.firma.mitprojekt.util.FilCache;
import dk.firma.mitprojekt.logik.Programdata;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import dk.firma.mitprojekt.util.JsonIndlaesning;
import dk.firma.mitprojekt.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Her kan foretages fælles initialisering.
 * Resten af programmet bliver først initialiseret efter at objektet og
 * kaldet til metoden onCreate() er afsluttet, så det er vigtigt kun at
 * udføre de allermest nødvendige ting her.
 */
public class MinApp extends Application
{
	private static final int stamdataNr = 11;
	private static final String STAMDATA = "stamdata_v" + stamdataNr ;
	private static final String STAMDATAURL = "http://javabog.dk:8080/ServerEksempel/" + STAMDATA + ".json";

	public static MinApp instans;

	/** Disse data kunne også være gemt i klassevariabler andetsteds,
	    det vigtige er at de initialiseret centralt, dvs. i onCreate() i denne klasse */
	public static SharedPreferences prefs;
	public static Programdata data;
	public static Stamdata stamdata;

	/** Kaldes som det første der sker når applikationen bliver startet */
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("Ny proces startet");
		//Log.kritiskFejl(null, null);

		instans = this;

		// Initialisering der kræver en Context
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// Er der nogle indstillinger der har global indflydelse på programmet
		// bør det opdages ved at lytte på prefs
		//prefs.registerOnSharedPreferenceChangeListener(this);


		// Initialisering af hjælpeklasser, f.eks. mappen som en cache af filer
		// hentet over netværket behøver, er en fin ide at lægge her, for ellers
		// skal tjek for denne initialisering ske i alle de aktiviteter, services
		// og recievers der er afhængig af hjælpeklasserne
		FilCache.init(this.getCacheDir());



		File stamdatafil = new File(FilCache.findLokaltFilnavn(STAMDATAURL));
		if (stamdatafil.exists()) try {
			// Vi har en cachet stamdata. Brug den
			InputStream is = new FileInputStream(stamdatafil);
			stamdata = JsonIndlaesning.parseStamdata(JsonIndlaesning.læsInputStreamSomStreng(is));
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (stamdata == null) try {
			// Indlæs fra raw this vi ikke har nogle cachede stamdata i prefs
			//InputStream is = getResources().openRawResource(R.raw.stamdata_v10);
			InputStream is = getResources().openRawResource(R.raw.stamdata_v11);
			stamdata = JsonIndlaesning.parseStamdata(JsonIndlaesning.læsInputStreamSomStreng(is));
			is.close();
		} catch (Exception e) {
			// Det her er en kritisk fejl der bør få programmet til at crashe !
			throw new Error(e);
		}

		// Her kan initialisering af programdata ske
		data = new Programdata();

		// Nu kan tjek for en ny udgave af stamdata foregå - i en baggrundstråd
		TjekForNyeStamdata baggrundstråd = new TjekForNyeStamdata();
		baggrundstråd.execute();
	}

	class TjekForNyeStamdata extends AsyncTask {
		private Stamdata nyeStamdata = null;

		/** Denne metode udføres af en baggrundstråd, sådan at hovedtråden ikke forsinkes */
		@Override
		protected Object doInBackground(Object... params) {
			File stamdatafil = new File(FilCache.findLokaltFilnavn(STAMDATAURL));
			long gammeltTidsstempel = stamdatafil.lastModified();
			try { // Forsøg at hente en fil over netværket
				FilCache.hentFil(STAMDATAURL, false);
				long nytTidsstempel = stamdatafil.lastModified();
				if (gammeltTidsstempel != nytTidsstempel) {
					Log.d("STAMDATA HAR ÆNDRET SIG");
					InputStream is = new FileInputStream(stamdatafil);
					nyeStamdata = JsonIndlaesning.parseStamdata(JsonIndlaesning.læsInputStreamSomStreng(is));
					is.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		/** Denne metode udføres af hovedtråden, efter at baggrundstråden er afsluttet */
		@Override
    protected void onPostExecute(Object res) {
			if (nyeStamdata != null) {
				stamdata = nyeStamdata;
				// Her skal aktiviteterne opdateres.
				// Det gøres ved at sende et broadcast om at der er nye data
				Intent i = new Intent(OPDATERINGSINTENT_Stamdata);
				sendBroadcast(i);
			}
    }
	}

	/** Bruges til at sende broadcasts om nye stamdata */
	public static final String OPDATERINGSINTENT_Stamdata = "OPDATERING_Stamdata";
}
