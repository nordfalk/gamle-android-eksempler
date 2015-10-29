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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Created by j on 07-08-15.
 */
public class Aktivitetsdata {
  /**
   * Programdata - static da de ikke fylder det store og vi dermed slipper for reinitialisering
   */
  public static final Aktivitetsdata instans = new Aktivitetsdata();

  static class Pakke {
    String navn;
    String kategori;
    ArrayList<String> klasser = new ArrayList<String>();
  }
  private ArrayList<String> aktiviteterIManifestet = new ArrayList<String>();

  private ArrayList<String> pakke_navn;
  ArrayList<String> pakke_kategori;
  ArrayList<ArrayList<String>> pakke_klasser = new ArrayList<ArrayList<String>>();

  private HashSet<Integer> manglerTjekForAndreFiler = new HashSet<Integer>();
  private Application app;
  static final boolean FEJLFINDING = true;


  public void init(Application application) {
    if (app != null) return;
    app = application;

    indlæsAktiviteter();

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

  public void indlæsAktiviteter() {
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
      ArrayList<String> alleGemteAktiviteter = (ArrayList<String>) objektstrøm.readObject();
      Log.d("Aktivitetsliste", "deser2 tid: " + (System.currentTimeMillis() - tid));
      if (alleGemteAktiviteter.equals(aktiviteterIManifestet)) {
        // Gemte aktiviteter er de samme! Vi fortsætter...
        pakke_navn = (ArrayList<String>) objektstrøm.readObject();
        pakke_kategori = (ArrayList<String>) objektstrøm.readObject();
        pakke_klasser = (ArrayList<ArrayList<String>>) objektstrøm.readObject();
        Log.d("Aktivitetsliste", "deser3 tid: " + (System.currentTimeMillis() - tid));
      }
      objektstrøm.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    if (pakke_klasser.isEmpty()) { // cache var ikke god, vi indlæser fra grunden

      LinkedHashMap<String, Integer> pakkeTilPosition = new LinkedHashMap<String, Integer>();
      //kategorier.add("(søg)");
      pakkeTilPosition.put(" = vis alle = ", 0);
      pakke_klasser.add(aktiviteterIManifestet);
      for (String navn : aktiviteterIManifestet) {
        int n = navn.lastIndexOf(".");
        String pakkenavn = navn.substring(0, n); // Fjern klassenavnet
        //String klassenavn = navn.substring(n+1); // Klassenavnet

        Integer position = pakkeTilPosition.get(pakkenavn);
        ArrayList klasser;
        if (position == null) {
          position = pakkeTilPosition.size();
          pakkeTilPosition.put(pakkenavn, position);
          klasser = new ArrayList<String>();
          pakke_klasser.add(klasser);
        } else {
          klasser = pakke_klasser.get(position);
        }
        klasser.add(navn);
      }

      //pakkeTilPosition.put("eks.levendebaggrund", pakkeTilPosition.size());
      //pakke_klasser.add(new ArrayList<String>());
      //pakkeTilPosition.put("eks.levendeikon", pakkeTilPosition.size());
      //pakke_klasser.add(new ArrayList<String>());
      pakke_navn = new ArrayList(pakkeTilPosition.keySet());
      pakke_kategori = new ArrayList(pakke_navn); // tag kopi og ændr den
      for (int i = 1; i < pakke_kategori.size(); i++) {
        String pakkenavn = pakke_kategori.get(i);
        //pakkenavn = pakkenavn.replaceFirst("_","\n"); // Linjeskift
        if (pakkenavn.startsWith("lekt")) {
          pakkenavn = "lekt "+pakkenavn.substring(4); // lav 'lekt_05' om til 'lekt 05'
          pakke_kategori.set(i, pakkenavn);
        }
        manglerTjekForAndreFiler.add(i);
      }


      // Påbegynd asynkron indlæsning af pakke_klasser
      new Thread() {
        @Override
        public void run() {
          for (int i = 1; i < pakke_kategori.size(); i++) {
            SystemClock.sleep(500); // Vent lidt for at lade systemet starte op
            tjekForAndreFilerIPakken(i);
            if (FEJLFINDING)
              Log.d("Aktivitetsliste", "T " + i + " tid: " + (System.currentTimeMillis() - tid));

            try { // Gem alle resultater for hurtig opstart
              ObjectOutputStream objektstrøm = new ObjectOutputStream(new FileOutputStream(cachefil));
              objektstrøm.writeObject(aktiviteterIManifestet);
              objektstrøm.writeObject(pakke_navn);
              objektstrøm.writeObject(pakke_kategori);
              objektstrøm.writeObject(pakke_klasser);
              objektstrøm.close();
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        }
      }.start();

    } // cacheindlæsning slut
  }


  public synchronized void tjekForAndreFilerIPakken(int position) {
    if (!manglerTjekForAndreFiler.contains(position)) {
      return;
    }
    manglerTjekForAndreFiler.remove(position);
    String pnavn = pakke_navn.get(position);
    ArrayList<String> klasser = pakke_klasser.get(position);
    if (FEJLFINDING)
      Log.d("Aktivitetsliste", "pakkeTilKlasseliste.get " + position + " = " + klasser + " " + pnavn);

    // if (a.toLowerCase().contains(kategori)) klasserDerVisesNu.add(a); // kun nødvendig til søgning
    try { // Skan efter filer der ikke er aktiviteter og vis også dem
      //System.out.println(klasserDerVisesNu);
      String mappe = pnavn.replace(".", "/");
      ydre:
      for (String fil : app.getAssets().list("java/" + mappe)) {
        if (FEJLFINDING) Log.d("fil", fil);
        int filendelsePos = fil.lastIndexOf(".");
        if (filendelsePos == -1) continue;
        String klassenavn = fil.substring(0, filendelsePos); // stryg filendelse
        //System.out.println(klassenavn);
        for (String n : klasser) {
          if (n.endsWith(klassenavn)) {
            continue ydre; // allerede listet
          }
        }
        klasser.add(mappe + "/" + fil);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
   }

}
