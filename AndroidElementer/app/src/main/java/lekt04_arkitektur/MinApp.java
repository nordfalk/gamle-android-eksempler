/**
 * Esperanto-radio por Androjd, farita de Jacob Nordfalk.
 * Kelkaj partoj de la kodo originas de DR Radio 2 por Android, vidu
 * http://code.google.com/p/dr-radio-android/
 * <p/>
 * Esperanto-radio por Androjd estas libera softvaro: vi povas redistribui
 * ĝin kaj/aŭ modifi ĝin kiel oni anoncas en la licenco GNU Ĝenerala Publika
 * Licenco (GPL) versio 2.
 * <p/>
 * Esperanto-radio por Androjd estas distribuita en la espero ke ĝi estos utila,
 * sed SEN AJNA GARANTIO; sen eĉ la implica garantio de surmerkatigindeco aŭ
 * taŭgeco por iu aparta celo.
 * Vidu la GNU Ĝenerala Publika Licenco por pli da detaloj.
 * <p/>
 * Vi devus ricevi kopion de la GNU Ĝenerala Publika Licenco kune kun la
 * programo. Se ne, vidu <http://www.gnu.org/licenses/>.
 */
package lekt04_arkitektur;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import lekt06_youtube.FilCache;

/**
 * Her kan foretages fælles initialisering.
 * Resten af programmet bliver først initialiseret efter at objektet og
 * kaldet til metoden onCreate() er afsluttet, så det er vigtigt kun at
 * udføre de allermest nødvendige ting her.
 *
 * BEMÆRK - Klassen her skal være nævnt i <application>-tagget i AndroidManifest.xml,
 * <application android:name="lekt04_arkitektur.MinApp"
 */
public class MinApp extends Application {
  // Globale data (kunne godt være gemt i en klassevariabel andetsteds)
  public static SharedPreferences prefs;
  public static MinApp instans;
  /**
   * Håndtag til forgrundstråden
   */
  public static Handler forgrundstråd = new Handler();
  private static Programdata data;

  public static Programdata getData() {
    // if (data == null) data = new Programdata(); // klassisk singleon unødvendigt da den oprettes i onCreate()
    return data;
  }

  @Override
  public void onCreate() {
    Log.d("MinApp", "onCreate() kaldt");
    super.onCreate();
    instans = this;

    // Initialisering der kræver en Context
    prefs = PreferenceManager.getDefaultSharedPreferences(this);
    udvikling = MinApp.prefs.getBoolean("startIUdvikling", udvikling);

    // Programdata der skal være indlæst ved opstart
    try {
      data = (Programdata) Serialisering.hent(getFilesDir() + "/programdata.ser");
      Log.d("data", "" + data);
      System.out.println("programdata indlæst fra fil");
      data.observatører = new ArrayList<Runnable>();
    } catch (Exception ex) {
      data = new Programdata(); // fil fandtes ikke eller data var inkompatible
      System.out.println("programdata oprettet fra ny: " + ex);
    }

    data.observatører.add(new Runnable() {
      @Override
      public void run() {
        MinApp.gemData();
      }
    });

    // Initialisering af hjælpeklasser, f.eks. mappen som en cache af filer
    // hentet over netværket behøver, er en fin ide at lægge her, for ellers
    // skal tjek for denne initialisering ske i alle de aktiviteter, services
    // og recievers der er afhængig af hjælpeklasserne
    FilCache.init(this.getCacheDir());
  }

  public static void gemData() {
    try {  // nu er aktiviteten ikke synlig, så er det tid til at gemme data!
      Serialisering.gem(data, instans.getFilesDir() + "/programdata.ser");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }


  public static final boolean EMULATOR = Build.PRODUCT.contains("sdk") || Build.MODEL.contains("Emulator"); // false;
  public static boolean udvikling = true;



  public static void langToast(final String txt) {
    forgrundstråd.post(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(instans, txt, Toast.LENGTH_LONG).show();
      }
    });
  }

  public static void kortToast(final String txt) {
    forgrundstråd.post(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(instans, txt, Toast.LENGTH_SHORT).show();
      }
    });
  }

  /*
   * Version fra
   * http://developer.android.com/training/basics/network-ops/managing.html
   */
  public static boolean erOnline() {
    ConnectivityManager connMgr = (ConnectivityManager) instans.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    return (networkInfo != null && networkInfo.isConnected());
  }


  public static void rapporterOgvisFejl(Throwable e) {
    e.printStackTrace();
    langToast("Der skete en fejl:\n" + e);
  }
}
