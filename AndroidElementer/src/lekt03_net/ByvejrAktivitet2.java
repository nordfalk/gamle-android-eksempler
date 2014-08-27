/*
 * /dmi/femdgn_dk.png
 * /dmi/index/danmark/solvarsel.htm
 * /dmi/varsel.xml  - DMI - varsel om farligt vejr
 * /dmi/rss-nyheder
 */        //http://servlet.dmi.dk/byvejr/servlet/byvejr?valgtBy=2500&tabel=dag10_14
// For kun at se denne proces' output:
// adb logcat -v tag ActivityManager:I Vejret:V System.err:V *:S
// Fremtidig geokodning bliver med
// http://www.geonames.org/postal-codes/postal-codes-denmark.html
package lekt03_net;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import dk.nordfalk.android.elementer.R;
import lekt02_aktiviteter.Indstillinger_akt;

/**
 * Simpel aktivitet der viser byvejret i Valby
 *
 * @author Jacob Nordfalk
 */
public class ByvejrAktivitet2 extends Activity {

  private static final String TAG = "Vejret";
  ImageView imageView_dag1;
  ImageView imageView_dag3_9;
  ImageView imageView_dag10_14;
  TextView postnrByTextView;
  int valgtPostNr = 2500;
  String valgtBy = "Valby";
  GpsDataPostnrBy postnrGpsBy = GpsDataPostnrBy.instans();
  LocationManager locationManager;
  String stedProvider;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


		/*
     * Opsætning af de grafiske komponenter
		 */
    LinearLayout linearLayout = new LinearLayout(this);
    linearLayout.setOrientation(LinearLayout.VERTICAL);

    // Lav en række med teksten "Vejret for 2500 Valby" (det sidste blåt)
    LinearLayout række = new LinearLayout(this);
    TextView textView = new TextView(this);
    textView.setText("Vejret for ");
    række.addView(textView);

    postnrByTextView = new TextView(this);
    postnrByTextView.setText(valgtPostNr + " " + valgtBy);
    postnrByTextView.setTextColor(Color.BLUE);
    række.addView(postnrByTextView);
    // når bruger klikker på den blå tekst kan han vælge ny by
    postnrByTextView.setOnClickListener(new OnClickListener() {
      public void onClick(View arg0) {
        visVælgByDialog();
      }
    });

    // tilføj rækken
    linearLayout.addView(række);

    // første billede
    imageView_dag1 = new ImageView(this);
    //imageView_dag1.setScaleType(ImageView.ScaleType.FIT_XY);
    imageView_dag1.setAdjustViewBounds(true);
    linearLayout.addView(imageView_dag1);

    // en tekst
    textView = new TextView(this);
    textView.setText("... med 3 til 9-døgnsudsigt...");
    linearLayout.addView(textView);

    // andet billede
    imageView_dag3_9 = new ImageView(this);
    imageView_dag3_9.setAdjustViewBounds(true);
    linearLayout.addView(imageView_dag3_9);

    // mere tekst
    textView = new TextView(this);
    textView.setText("... og en 10-14-døgnsudsigt.");
    linearLayout.addView(textView);

    // tredie billede
    imageView_dag10_14 = new ImageView(this);
    imageView_dag10_14.setAdjustViewBounds(true);
    linearLayout.addView(imageView_dag10_14);


		/**/
    // alternativ række hvor postnr kan indtastes
    række = new LinearLayout(this);
    textView = new TextView(this);
    textView.setText("Vælg postnummer");
    række.addView(textView);
    //((LinearLayout.LayoutParams) textView.getLayoutParams()).weight = 1;

    final EditText editText_postnr = new EditText(this);
    editText_postnr.setText("2500");
    række.addView(editText_postnr);

    Button button_postnr = new Button(this);
    button_postnr.setText("OK");
    række.addView(button_postnr);

    // når bruger trykker OK skal det indlæses
    button_postnr.setOnClickListener(new OnClickListener() {

      public void onClick(View arg0) {
        valgtPostNr = Integer.parseInt("" + editText_postnr.getText());
        valgtBy = "ukendt";
        postnrByTextView.setText(valgtPostNr + " " + valgtBy);
        hentBilleder(true, 60 * 60); // max 1 time gamle
      }
    });
    // tilføj rækken
    linearLayout.addView(række);

    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(linearLayout);
    setContentView(scrollView);

    try {
      // Indlæs postnumre, byer og deres GPS-koordinater
      GpsDataPostnrBy.init(getResources());
    } catch (IOException ex) {
      advarBruger("PostnrGpsBy.init fejlede!");
      ex.printStackTrace();
    }

    // Stedbestemmelse - find en udbyder af positionsdata
    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    Log.d(TAG, "locationManager.getAllProviders() = " + locationManager.getAllProviders());
    stedProvider = locationManager.getBestProvider(new Criteria(), false);
    Log.d(TAG, "locationManager.getBestProvider() gav " + stedProvider);

    hentBilleder(false, Integer.MAX_VALUE);  // vis gamle billeder hvis de findes

    SharedPreferences indst = PreferenceManager.getDefaultSharedPreferences(this);

    if (indst.getBoolean("brugPosVedStart", true)) {
      if (!indst.getBoolean("ventPåPos", false)) {
        Location sidstePos = locationManager.getLastKnownLocation(stedProvider);
        textView = new TextView(this);
        textView.append("\n\nSted: bedst = " + stedProvider + " med sidstePos = " + sidstePos);
        linearLayout.addView(textView);
        findNærmestePostnr(sidstePos);
        hentBilleder(true, 60 * 60); // max 1 time gamle
      }
      startStedbestemmelseOgOpdatering();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    menu.add(0, 43, 0, "Indstillinger").setIcon(android.R.drawable.ic_menu_preferences);
    menu.add(0, 45, 0, "Vælg by");
    menu.add(0, 46, 0, "Vejret her");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case 43:
        startActivity(new Intent(this, Indstillinger_akt.class));
        return true;
      case 45:
        visVælgByDialog();
        return true;
      case 46:
        startStedbestemmelseOgOpdatering();
        return true;
    }
    return false;
  }

  /**
   * Stedbestemmelse.
   * Kalder en
   */
  private void startStedbestemmelseOgOpdatering() {
    locationManager.requestLocationUpdates(stedProvider, 60000, 1, locationListener);
  }

  LocationListener locationListener = new LocationListener() {

    public void onLocationChanged(Location pos) {
      locationManager.removeUpdates(this);
      findNærmestePostnr(pos);
      hentBilleder(true, 60 * 60); // max 1 time gamle
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

    public void onProviderEnabled(String arg0) {
    }

    public void onProviderDisabled(String arg0) {
    }
  };

  private void findNærmestePostnr(Location pos) {
    if (pos == null) {
      advarBruger("Fik ikke en position");
      return;
    }
    // TODO brug http://developer.android.com/reference/android/location/Geocoder.html i stedet
    int n = postnrGpsBy.findNærmestePunkt(pos);
    if (n != -1) {
      valgtBy = postnrGpsBy.byNavn[n];
      valgtPostNr = postnrGpsBy.postNr[n];
      postnrByTextView.setText(valgtPostNr + " " + valgtBy);
    } else {
      advarBruger("Dette område er ikke dækket");
    }
  }

  private void hentBilleder(boolean hentOverNetværk, int billedersMaxAlder) {
    // Dette skulle egentlig ikke gøres i GUI-tråden, men det tager vi senere
    // http://android-developers.blogspot.com/2009/05/painless-threading.html
    // http://developer.android.com/reference/android/os/AsyncTask.html
    try {
      Bitmap byvejr_dag1 = opretBitmapFraUrl("http://servlet.dmi.dk/byvejr/servlet/byvejr_dag1?by=" + valgtPostNr + "&mode=long");
      imageView_dag1.setImageBitmap(byvejr_dag1);
      // Sørg for at den er synlig på skærmen
      imageView_dag1.requestRectangleOnScreen(new Rect());

      Bitmap byvejr_dag3_9 = opretBitmapFraUrl("http://servlet.dmi.dk/byvejr/servlet/byvejr?by=" + valgtPostNr + "&tabel=dag3_9");
      imageView_dag3_9.setImageBitmap(byvejr_dag3_9);

      Bitmap byvejr_dag10_14 = opretBitmapFraUrl("http://servlet.dmi.dk/byvejr/servlet/byvejr?by=" + valgtPostNr + "&tabel=dag10_14");
      imageView_dag10_14.setImageBitmap(byvejr_dag10_14);
    } catch (Exception ex) {
      ex.printStackTrace();
      advarBruger("Kunne ikke få data fra DMI");
      advarBruger("" + ex);
    }
  }

  private void advarBruger(String advarsel) {
    Log.w(TAG, advarsel);
    Toast.makeText(this, advarsel, Toast.LENGTH_LONG).show();
  }

  /**
   * Vis en dialog hvor brugeren kan vælge by/postnummer manuelt
   */
  private void visVælgByDialog() {
    DialogInterface.OnClickListener klikLytter = new DialogInterface.OnClickListener() {

      public void onClick(DialogInterface dialogenUbrugt, int n) {
        valgtBy = postnrGpsBy.byNavn[n];
        valgtPostNr = postnrGpsBy.postNr[n];
        postnrByTextView.setText(valgtPostNr + " " + valgtBy);
        Log.d(TAG, "valgtBy=" + valgtBy + " valgtPostNr=" + valgtPostNr);
        hentBilleder(true, 5 * 60); // max 5 minutter gamle
      }
    };

    Builder ab = new Builder(this);
    ab.setTitle("Vælg by");
    ab.setItems(postnrGpsBy.byNavn, klikLytter);
    ab.show();
  }

  public static Bitmap opretBitmapFraUrl(String url) throws IOException {
    InputStream is = new URL(url).openStream();
    Bitmap bitmap = BitmapFactory.decodeStream(is);
    is.close();
    return bitmap;
  }
}
/*
Log.d(TAG,"getFilesDir() = "+getFilesDir());
File privatMappe = getFilesDir();
for (File fil : privatMappe.listFiles()) {
Log.d(TAG,"  "+fil.getAbsolutePath());
}

File nyFil = new File(privatMappe, "minFil.txt");
try {
FileWriter fw = new FileWriter( nyFil );
fw.append("Hej verden\n");
fw.close();
} catch (IOException e) {
e.printStackTrace();
}

Log.d(TAG,"getCacheDir() = "+getCacheDir());


String state = Environment.getExternalStorageState();
Log.d(TAG,"getExternalStorageDirectory() = "+Environment.getExternalStorageDirectory() + " "+state);

 */


class GpsDataPostnrBy {

  static int[] postNr;
  static int[] gpsX;
  static int[] gpsY;
  static String[] byNavn;
  static GpsDataPostnrBy instans = new GpsDataPostnrBy();

  /**
   * Indlæs postnumre, byer og deres GPS-koordinater
   *
   * @param resources Applikationens resurser
   */
  static void init(Resources resources) throws IOException {
    if (postNr != null) {
      return;
    }
    DataInputStream ins = new DataInputStream(resources.openRawResource(R.raw.vejret_postnr_gps_by));
    int antal = ins.readInt();
    postNr = new int[antal];
    gpsX = new int[antal];
    gpsY = new int[antal];
    byNavn = new String[antal];

    for (int i = 0; i < antal; i++) {
      postNr[i] = ins.readInt();
    }
    for (int i = 0; i < antal; i++) {
      gpsX[i] = ins.readInt();
    }
    for (int i = 0; i < antal; i++) {
      gpsY[i] = ins.readInt();
    }
    String byer = ins.readUTF();
    System.err.println("byer = " + byer);
    // byer = ,Viborg,Rødby,Vojens,Slagelse,Hadsund,Læsø/Byrum,Ølgod,Ebeltoft,Haderslev,Sorø,Fejø,Blåvand,Hasle,Lemvig,Hedehusene,Århus,Svendborg,Støvring,Ishøj
    // del op i array
    int n = byer.length();
    int n0 = n;
    while (--n >= 0) {
      if (byer.charAt(n) == ',') {
        byNavn[--antal] = byer.substring(n + 1, n0);
        System.err.println("byNavn[" + antal + "] = " + byNavn[antal]);
        n0 = n;
      }
    }
  }

  static GpsDataPostnrBy instans() {
    return instans;
  }

  static final int maxDist = (int) 5E5;

  int findNærmestePunkt(Location pos) {

    System.err.println("findNærmestePunkt pos = " + pos); // mLatitude=55.663655774999995,mLongitude=12.502774549999998
    int y = (int) (pos.getLatitude() * 1E6); // "latitude=y","longitude=x"
    int x = (int) (pos.getLongitude() * 1E6);

    int dist = Integer.MAX_VALUE;
    int bedsteN = -1;

    for (int n = gpsX.length - 1; n >= 0; n--) {
      int dx = gpsX[n] - x;
      if (dx > maxDist || dx < -maxDist) {
        continue;
      }
      int dy = gpsY[n] - y;
      if (dy > maxDist || dy < -maxDist) {
        continue;
      }

      int dist2 = dx * dx + dy * dy;
      if (dist > dist2) {
        dist = dist2;
        bedsteN = n;
        System.err.println(x + " - dx = " + dx);
        System.err.println("dist2 = " + dist2 + "   for " + n);
      }
    }
    return bedsteN;
  }
}
