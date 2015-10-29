package dk.nordfalk.aktivitetsliste;

import android.app.Activity;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import dk.nordfalk.android.elementer.R;

/**
 * Created by j on 07-08-15.
 */
public class Aktivitetsdata {
  /**
   * Programdata - static da de ikke fylder det store og vi dermed slipper for reinitialisering
   */
  public static final Aktivitetsdata instans = new Aktivitetsdata();
  public static final String TAG = "Aktivitetsliste";

  static class Pakke implements Serializable {
    String navn;
    String kategori;
    LinkedHashMap<String, String> dokumenter = new LinkedHashMap<>();
    ArrayList<String> aktiviteter = new ArrayList<>();
    ArrayList<String> andreFiler = new ArrayList<>();
    public boolean erTjekketForAndreFiler;

    public Pakke(String s) {
      navn = s;
      //pakkenavn = pakkenavn.replaceFirst("_","\n"); // Linjeskift
      if (s.startsWith("lekt")) {
        s = "lekt "+s.substring(4); // lav 'lekt_05' om til 'lekt 05'
      }
      kategori = s;
    }

    @Override
    public String toString() {
      return navn;
    }
  }
  private ArrayList<String> aktiviteterIManifestet = new ArrayList<>();

  ArrayList<Pakke> pakker = new ArrayList<>();
  private ArrayList<String> pakke_navnx;
  ArrayList<String> pakke_kategorix;
  ArrayList<ArrayList<String>> pakke_klasserx = new ArrayList<ArrayList<String>>();

  private Application app;
  static final boolean FEJLFINDING = true;


  public void init(Application application) {
    if (app != null) return;
    app = application;

    indlæsPakker();

    try {
      // App'en startes i frisk JVM, den er sikkert lige installeret fra USB-kabel, så...
      // Fjern evt skærmlås ...
      KeyguardManager keyguardManager = (KeyguardManager) app.getSystemService(Activity.KEYGUARD_SERVICE);
      KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(app.KEYGUARD_SERVICE);
      lock.disableKeyguard();

      // ... og tænd skærmen 30 sekunder, og også lidt efter...
      PowerManager powerManager = (PowerManager) app.getSystemService(app.POWER_SERVICE);
      PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
              PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "Aktivitetsliste");
      wakeLock.acquire(30000);
    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(app, "Kunne ikke fjerne skærmlås og holde skærmen tændt:\n" + e, Toast.LENGTH_LONG).show();
    }
  }

  public void indlæsPakker() {
    final long tid = System.currentTimeMillis();
    // Førstegangsinitialisering af programdata
    try {
      for (ActivityInfo a : app.getPackageManager().
              getPackageInfo(app.getPackageName(), PackageManager.GET_ACTIVITIES).activities) {
        aktiviteterIManifestet.add(a.name);
      }
    } catch (PackageManager.NameNotFoundException ex) {
      ex.printStackTrace();
    }
    aktiviteterIManifestet.add("AndroidManifest.xml");

    final File cachefil = new File(app.getCacheDir(), "Aktivitetslistecache.ser");

    ObjectInputStream objektstrøm = null;
    try { // Hent gamle resultater for hurtig opstart
      objektstrøm = new ObjectInputStream(new FileInputStream(cachefil));
      Log.d("Aktivitetsliste", "deser1 tid: " + (System.currentTimeMillis() - tid));
      int alleGemteAktiviteterHashCode = (Integer) objektstrøm.readObject();
      Log.d("Aktivitetsliste", "deser2 tid: " + (System.currentTimeMillis() - tid));
      if (alleGemteAktiviteterHashCode == aktiviteterIManifestet.hashCode()) {
        // Gemte aktiviteter er de samme! Vi fortsætter...
        pakker = (ArrayList<Pakke>) objektstrøm.readObject();
        Log.d(TAG, "deser3 tid: " + (System.currentTimeMillis() - tid));
      }
      objektstrøm.close();
    } catch (Exception ex) {
      Log.d(TAG, "Kunne ikke deserialisere: " + ex);
    }

    //pakker.clear(); // til udvikling
    if (!pakker.isEmpty()) return;

    // cache var ikke god, vi indlæser fra grunden

    LinkedHashMap<String, Pakke> pakkenavnTilPakke = new LinkedHashMap<>();
    //kategorier.add("(søg)");
    {
      Pakke pakke = new Pakke("alle_aktiviteter");
      pakker.add(pakke);
      pakkenavnTilPakke.put(pakke.navn, pakke);
      pakke.aktiviteter.addAll(aktiviteterIManifestet);
    }

    Pakke dokupakke = new Pakke("dokumenter_og videoer");
    pakker.add(dokupakke);
    pakkenavnTilPakke.put(dokupakke.navn, dokupakke);

    for (String navn : aktiviteterIManifestet) {
      int n = navn.lastIndexOf(".");
      String pakkenavn = navn.substring(0, n); // Fjern klassenavnet
      //String klassenavn = navn.substring(n+1); // Klassenavnet

      Pakke pakke = pakkenavnTilPakke.get(pakkenavn);
      if (pakke == null) {
        pakke = new Pakke(pakkenavn);
        pakkenavnTilPakke.put(pakkenavn, pakke);
        pakker.add(pakke);
      }
      pakke.aktiviteter.add(navn);
    }

    //pakkenavnTilPakke.put("eks.levendebaggrund", pakkenavnTilPakke.size());
    //pakke_klasser.add(new ArrayList<String>());
    //pakkenavnTilPakke.put("eks.levendeikon", pakkenavnTilPakke.size());
    //pakke_klasser.add(new ArrayList<String>());

    try {
      InputStream is = app.getResources().openRawResource(R.raw.android_elementer_data);
      byte b[] = new byte[is.available()]; // kun små filer
      is.read(b);
      is.close();
      String str = new String(b, "UTF-8");

      for (String linje : str.split("\n")) {
        linje = linje.trim();
        if (linje.length() == 0 || linje.startsWith("#")) continue;
        String[] felter = linje.split(", ");
        String pakkenavn = felter[0];
        String titel = felter[1];
        String url = felter[2];

        Pakke pakke = pakkenavnTilPakke.get(pakkenavn);
        if (pakke != null) {
          pakke.dokumenter.put(titel.replaceAll("^(.|..|...) ", ""), url);
          Log.d(TAG, pakke + " dokumenter=" + pakke.dokumenter);
        }
        dokupakke.dokumenter.put(titel, url);
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    // Påbegynd asynkron indlæsning af pakke_klasser
    new Thread() {
      @Override
      public void run() {
        try {
          for (Pakke p : pakker) {
            SystemClock.sleep(500); // Vent lidt for at lade systemet starte op
            tjekForAndreFilerIPakken(p);
            if (FEJLFINDING)
              Log.d("Aktivitetsliste", "T " + p.navn + " tid: " + (System.currentTimeMillis() - tid));
          }

          // Gem alle resultater for hurtig opstart
          ObjectOutputStream objektstrøm = new ObjectOutputStream(new FileOutputStream(cachefil));
          objektstrøm.writeObject(aktiviteterIManifestet.hashCode());
          objektstrøm.writeObject(pakker);
          objektstrøm.close();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }.start();

  }


  public void tjekForAndreFilerIPakken(Pakke pakke) {
    if (FEJLFINDING)
      Log.d("Aktivitetsliste", "pakkeTilKlasseliste.get " + pakke + " = " + pakke.aktiviteter + " " + pakke.navn);

    synchronized (pakke) {
      if (pakke.erTjekketForAndreFiler) return;
      pakke.erTjekketForAndreFiler = true;
      try { // Skan efter filer der ikke er aktiviteter og vis også dem
        //System.out.println(pakke_klasser);
        String mappe = pakke.navn.replace(".", "/");
        ydre:
        for (String fil : app.getAssets().list("java/" + mappe)) {
          if (FEJLFINDING) Log.d("fil", fil);
          int filendelsePos = fil.lastIndexOf(".");
          if (filendelsePos == -1) continue;
          String klassenavn = fil.substring(0, filendelsePos); // stryg filendelse
          //System.out.println(klassenavn);
          for (String n : pakke.aktiviteter) {
            if (n.endsWith(klassenavn)) {
              continue ydre; // allerede listet
            }
          }
          pakke.andreFiler.add(mappe + "/" + fil);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
