package dk.nordfalk.aktivitetsliste;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import dk.nordfalk.android.elementer.R;

public class Aktivitetsliste extends Activity implements OnItemClickListener, OnItemLongClickListener, OnItemSelectedListener {
  ArrayList<String> klasserDerVisesNu = new ArrayList<String>();
  ArrayAdapter<String> klasserDerVisesNuAdapter;
  int onStartTæller;
  ToggleButton seKildekode;
  Gallery kategorivalg;
    static final boolean FEJLFINDING = false;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Aktivitetsdata.instans.init(getApplication());
    if (Aktivitetsdata.instans.klasselister.isEmpty()) {

      indlæsAktiviteter();

      try {
        // App'en startes i frisk JVM, den er sikkert lige installeret fra USB-kabel, så...
        // Fjern evt skærmlås ...
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
        KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
        lock.disableKeyguard();

        // ... og tænd skærmen 30 sekunder, og også lidt efter...
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        WakeLock wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "Aktivitetsliste");
        wakeLock.acquire(30000);
      } catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(this, "Kunne ikke fjerne skærmlås og holde skærmen tændt:\n" + e, Toast.LENGTH_LONG).show();
      }

    }


    //kategorier.add("(søg)");
    /*
    søgEditText = new EditText(this);
		søgEditText.setHint("Søg");
		søgEditText.setSingleLine();
		søgEditText.setEnabled(true);
		søgEditText.setOnKeyListener(this);
		 */

    kategorivalg = new Gallery(this);
    kategorivalg.setAdapter(new ArrayAdapter(this, android.R.layout.simple_gallery_item, android.R.id.text1, Aktivitetsdata.instans.pakkekategorier));
    kategorivalg.setSpacing(10);
    kategorivalg.setVerticalScrollBarEnabled(true);
    kategorivalg.setOnItemSelectedListener(this);
    kategorivalg.setUnselectedAlpha(0.4f);
    //kategorivalg.setBackgroundColor(Color.DKGRAY);
    if (savedInstanceState == null) // Frisk start - vis animation
    {
      kategorivalg.startAnimation(AnimationUtils.loadAnimation(this, R.anim.egen_anim2));
    }


    klasserDerVisesNu.addAll(Aktivitetsdata.instans.alleAktiviteter);
    // Anonym nedarving af ArrayAdapter med omdefineret getView()
    klasserDerVisesNuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, klasserDerVisesNu) {
      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView listeelem_overskrift = (TextView) view.findViewById(android.R.id.text1);
        TextView listeelem_beskrivelse = (TextView) view.findViewById(android.R.id.text2);

        String pakkeOgKlasse = klasserDerVisesNu.get(position);
        if (pakkeOgKlasse.endsWith(".java")) {
          String pakkenavn = pakkeOgKlasse.substring(0, pakkeOgKlasse.lastIndexOf('/'));
          String klassenavn = pakkeOgKlasse.substring(pakkenavn.length() + 1);
          listeelem_overskrift.setText(klassenavn);
          listeelem_beskrivelse.setText(pakkeOgKlasse);
        } else if (pakkeOgKlasse.endsWith(".xml")) {
          listeelem_overskrift.setText(pakkeOgKlasse);
          listeelem_beskrivelse.setText("");
        } else {
          String pakkenavn = pakkeOgKlasse.substring(0, pakkeOgKlasse.lastIndexOf('.'));
          String klassenavn = pakkeOgKlasse.substring(pakkenavn.length() + 1);
          listeelem_overskrift.setText(klassenavn);
          listeelem_beskrivelse.setText(pakkenavn);
        }

        return view;
      }
    };
    ListView visKlasserListView = new ListView(this);
    visKlasserListView.setAdapter(klasserDerVisesNuAdapter);

    visKlasserListView.setOnItemClickListener(this);
    visKlasserListView.setOnItemLongClickListener(this);

    seKildekode = new ToggleButton(this);
    seKildekode.setTextOff("Se kilde");
    seKildekode.setTextOn("Se kilde");
    seKildekode.setChecked(false);


    // Layout
    TableLayout tl = new TableLayout(this);
    //tl.addView(textView);
    tl.addView(visKlasserListView, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
    //tl.addView(visKlasserListView);
    //((LinearLayout.LayoutParams) visKlasserListView.getLayoutParams()).weight = 1; // Stræk listen
    TableRow tr = new TableRow(this);
    tr.addView(seKildekode);
    //tr.addView(søgEditText);


    tr.addView(kategorivalg, new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1));
    tl.addView(tr);

    setContentView(tl);


    // Sæt ID'er så vi understøtter vending
    visKlasserListView.setId(117);
    kategorivalg.setId(118);
    seKildekode.setId(119);

    // Genskab valg fra sidst der blev startet en aktivitet
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    int position = prefs.getInt("position", 0);
    visKlasserListView.setSelectionFromTop(position, 30);
    kategorivalg.setSelection(prefs.getInt("kategoriPos", 1));

  }


  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      // nulstil først kategorivalg, afslut derefter
      if (kategorivalg.getSelectedItemPosition() > 0) {
        //kategorivalg.setSelection(0, true);
        kategorivalg.onFling(null, null, 3000, 0);
      } else {
        finish();
      }
      return true;
    } else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
      // søgning her?
    }
    //tv.append("\nonKeyDown "+keyCode);
    return super.onKeyDown(keyCode, event);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (onStartTæller++ == 2) {
      Toast.makeText(this, "Vink: Tryk længe på et punkt for at se kildekoden", Toast.LENGTH_LONG).show();
    }
  }


  public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
    String akt = klasserDerVisesNu.get(position);

    if (seKildekode.isChecked() || akt.endsWith(".java") || akt.endsWith(".xml")) {
      visKildekode(akt);
      return;
    }

    try {
      // Tjek at klassen faktisk kan indlæses (så prg ikke crasher hvis den ikke kan!)
      Class klasse = Class.forName(akt);

      if (akt.toLowerCase().contains("fragment") && Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB_MR2) {
        visDialog("Denne aktivitet kan kun køre på Android 4\nSkal den køre på Android 2 skal et kompatibilitetsbibliotek inkluderes og koden ændres til at bruge kompatibilitetsbiblioteket.");
        return;
      }
      startActivity(new Intent(this, klasse));
      overridePendingTransition(0, 0); // hurtigt skift
      Toast.makeText(this, akt+" startet", Toast.LENGTH_SHORT).show();
    } catch (Throwable e) {
      e.printStackTrace();
      //while (e.getCause() != null) e = e.getCause(); // Hop hen til grunden
      String tekst = akt + " gav fejlen:\n" + Log.getStackTraceString(e);
      visDialog(tekst);
    }

    // Find position i fuld liste
    position = Aktivitetsdata.instans.alleAktiviteter.indexOf(akt);
    // Gem position og 'start aktivitet direkte' til næste gang
    PreferenceManager.getDefaultSharedPreferences(this).edit().
        putInt("position", position).
        putInt("kategoriPos", kategorivalg.getSelectedItemPosition()).
        commit();
  }

  private void visDialog(String tekst) {
    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle("Kunne ikke starte");
    TextView tv = new TextView(this);
    tv.setText(tekst);
    dialog.setView(tv);
    dialog.show();
  }

  public boolean onItemLongClick(AdapterView<?> listView, View v, int position, long id) {
    visKildekode(klasserDerVisesNu.get(position));
    return true;
  }

  private void visKildekode(String klasse) {
    String filnavn = klasse;
    if (filnavn.equals("AndroidManifest.xml")) {
      // ingen ekstra sti eller andet
    } else {
      filnavn = "java/" + filnavn;
      if (!filnavn.endsWith(".java")) {
        filnavn = filnavn.replace('.', '/') + ".java";
      }
    }

    Toast.makeText(this, "Viser " + filnavn, Toast.LENGTH_LONG).show();

    Intent i = new Intent(this, VisKildekode.class);
    i.putExtra(VisKildekode.KILDEKODE_FILNAVN, filnavn);
    startActivity(i);
  }


  // Galleriet/pakkelisten
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    //tv.setText("onItemSelected "+position+" "+kategorivalg.getSelectedItemPosition());
    klasserDerVisesNu.clear();
    tjekForAndreFilerIPakken(position);
    klasserDerVisesNu.addAll(Aktivitetsdata.instans.klasselister.get(position));
    klasserDerVisesNuAdapter.notifyDataSetChanged();
  }

  public void onNothingSelected(AdapterView<?> parent) {
  }



  private synchronized void tjekForAndreFilerIPakken(int position) {
    if (!Aktivitetsdata.instans.manglerTjekForAndreFiler.contains(position)) {
      return;
    }
    Aktivitetsdata.instans.manglerTjekForAndreFiler.remove(position);
    String pnavn = Aktivitetsdata.instans.pakkenavne.get(position);
    ArrayList<String> klasser = Aktivitetsdata.instans.klasselister.get(position);
    if (FEJLFINDING) Log.d("Aktivitetsliste", "pakkeTilKlasseliste.get " + position + " = " + klasser + " " + pnavn);

    // if (a.toLowerCase().contains(kategori)) klasserDerVisesNu.add(a); // kun nødvendig til søgning
    try { // Skan efter filer der ikke er aktiviteter og vis også dem
      //System.out.println(klasserDerVisesNu);
      String mappe = pnavn.replace(".", "/");
        ydre:
      for (String fil : getAssets().list("java/" + mappe)) {
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

  public void indlæsAktiviteter() {
    final long tid = System.currentTimeMillis();
    // Førstegangsinitialisering af programdata
    try {
      for (ActivityInfo a : getPackageManager().
          getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES).activities) {
        Aktivitetsdata.instans.alleAktiviteter.add(a.name);
      }
    } catch (NameNotFoundException ex) {
      ex.printStackTrace();
    }
    Aktivitetsdata.instans.alleAktiviteter.add("AndroidManifest.xml");

    final File cachefil = new File(getCacheDir(), "Aktivitetslistecache.ser");

    ObjectInputStream objektstrøm = null;
    try { // Hent gamle resultater for hurtig opstart
      objektstrøm = new ObjectInputStream(new FileInputStream(cachefil));
      Log.d("Aktivitetsliste", "deser1 tid: " + (System.currentTimeMillis() - tid));
      ArrayList<String> alleGemteAktiviteter = (ArrayList<String>) objektstrøm.readObject();
      Log.d("Aktivitetsliste", "deser2 tid: " + (System.currentTimeMillis() - tid));
      if (alleGemteAktiviteter.equals(Aktivitetsdata.instans.alleAktiviteter)) {
        // Gemte aktiviteter er de samme! Vi fortsætter...
        Aktivitetsdata.instans.pakkenavne = (ArrayList<String>) objektstrøm.readObject();
        Aktivitetsdata.instans.pakkekategorier = (ArrayList<String>) objektstrøm.readObject();
        Aktivitetsdata.instans.klasselister = (ArrayList<ArrayList<String>>) objektstrøm.readObject();
        Log.d("Aktivitetsliste", "deser3 tid: " + (System.currentTimeMillis() - tid));
      }
      objektstrøm.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    if (Aktivitetsdata.instans.klasselister.isEmpty()) { // cache var ikke god, vi indlæser fra grunden

      LinkedHashMap<String, Integer> pakkeTilPosition = new LinkedHashMap<String, Integer>();
      //kategorier.add("(søg)");
      pakkeTilPosition.put(" = vis alle = ", 0);
      Aktivitetsdata.instans.klasselister.add(Aktivitetsdata.instans.alleAktiviteter);
      for (String navn : Aktivitetsdata.instans.alleAktiviteter) {
        int n = navn.lastIndexOf(".");
        String pakkenavn = navn.substring(0, n); // Fjern klassenavnet
        //String klassenavn = navn.substring(n+1); // Klassenavnet

        Integer position = pakkeTilPosition.get(pakkenavn);
        ArrayList klasser;
        if (position == null) {
          position = pakkeTilPosition.size();
          pakkeTilPosition.put(pakkenavn, position);
          klasser = new ArrayList<String>();
          Aktivitetsdata.instans.klasselister.add(klasser);
        } else {
          klasser = Aktivitetsdata.instans.klasselister.get(position);
        }
        klasser.add(navn);
      }

      //pakkeTilPosition.put("eks.levendebaggrund", pakkeTilPosition.size());
      //klasselister.add(new ArrayList<String>());
      //pakkeTilPosition.put("eks.levendeikon", pakkeTilPosition.size());
      //klasselister.add(new ArrayList<String>());
      Aktivitetsdata.instans.pakkenavne = new ArrayList(pakkeTilPosition.keySet());
      Aktivitetsdata.instans.pakkekategorier = new ArrayList(Aktivitetsdata.instans.pakkenavne); // tag kopi og ændr den
      for (int i = 1; i < Aktivitetsdata.instans.pakkekategorier.size(); i++) {
        String pakkenavn = Aktivitetsdata.instans.pakkekategorier.get(i);
        //pakkenavn = pakkenavn.replaceFirst("_","\n"); // Linjeskift
        if (pakkenavn.startsWith("lekt")) {
          pakkenavn = pakkenavn.substring(4); // tag 'lekt' fra 'lekt_05'
          Aktivitetsdata.instans.pakkekategorier.set(i, pakkenavn);
        }
        Aktivitetsdata.instans.manglerTjekForAndreFiler.add(i);
      }


      // Påbegynd asynkron indlæsning af klasselister
      new Thread() {
        @Override
        public void run() {
          for (int i = 1; i < Aktivitetsdata.instans.pakkekategorier.size(); i++) {
            SystemClock.sleep(500); // Vent lidt for at lade systemet starte op
            tjekForAndreFilerIPakken(i);
            if (FEJLFINDING) Log.d("Aktivitetsliste", "T " + i + " tid: " + (System.currentTimeMillis() - tid));

            try { // Gem alle resultater for hurtig opstart
              ObjectOutputStream objektstrøm = new ObjectOutputStream(new FileOutputStream(cachefil));
              objektstrøm.writeObject(Aktivitetsdata.instans.alleAktiviteter);
              objektstrøm.writeObject(Aktivitetsdata.instans.pakkenavne);
              objektstrøm.writeObject(Aktivitetsdata.instans.pakkekategorier);
              objektstrøm.writeObject(Aktivitetsdata.instans.klasselister);
              objektstrøm.close();
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        }
      }.start();

    } // cacheindlæsning slut
  }
}
