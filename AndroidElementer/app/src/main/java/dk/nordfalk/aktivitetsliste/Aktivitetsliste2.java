package dk.nordfalk.aktivitetsliste;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import dk.nordfalk.android.elementer.R;

public class Aktivitetsliste2 extends FragmentActivity {
  int onStartTæller;
  ToggleButton seKildekode;
  ViewPager kategorivalg;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Aktivitetsdata.instans.init(getApplication());

    // Layout
    TableLayout tl = new TableLayout(this);
    //tl.addView(textView);
    getLayoutInflater().inflate(R.layout.viewpager_med_titel, tl, true); // tilføj deklarativt til 'indhold'
    kategorivalg = (ViewPager) tl.findViewById(R.id.viewPager);
    kategorivalg.setAdapter(new VPAdapter(getSupportFragmentManager()));
    ((TableLayout.LayoutParams) kategorivalg.getLayoutParams()).height = 0;
    ((TableLayout.LayoutParams) kategorivalg.getLayoutParams()).weight = 1;
    /*
  <android.support.v4.view.PagerTitleStrip
      android:id="@+id/pager_title_strip"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:layout_gravity="top"/>

     */

    if (savedInstanceState == null) // Frisk start - vis animation
    {
      kategorivalg.startAnimation(AnimationUtils.loadAnimation(this, R.anim.egen_anim2));
    }


    seKildekode = new ToggleButton(this);
    seKildekode.setTextOff("Se kilde");
    seKildekode.setTextOn("Se kilde");
    seKildekode.setChecked(false);


    // Layout
    //TableLayout tl = new TableLayout(this);
    //tl.addView(textView);
    //tl.addView(kategorivalg, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
    //tl.addView(visKlasserListView, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
    //tl.addView(visKlasserListView);
    //((LinearLayout.LayoutParams) visKlasserListView.getLayoutParams()).weight = 1; // Stræk listen
    TableRow tr = new TableRow(this);
    tr.addView(seKildekode);
    //tr.addView(søgEditText);


    //tr.addView(kategorivalg, new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1));
    tl.addView(tr);

    setContentView(tl);


    kategorivalg.setId(118);
    seKildekode.setId(119);

    // Genskab valg fra sidst der blev startet en aktivitet
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    int position = prefs.getInt("position", 0);

    // Sæt ID'er så vi understøtter vending
    //XXXvisKlasserListView.setId(117);
    //XXXvisKlasserListView.setSelectionFromTop(position, 30);
    //XXXkategorivalg.setSelection(prefs.getInt("kategoriPos", 1));

  }



  private void visDialog(String tekst) {
    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle("Kunne ikke starte");
    TextView tv = new TextView(this);
    tv.setText(tekst);
    dialog.setView(tv);
    dialog.show();
  }


  void visKildekode(String klasse) {
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



  private class VPAdapter extends FragmentPagerAdapter {
    public VPAdapter(FragmentManager fm) {
      super(fm);
    }
    // ArrayAdapter(this, android.R.layout.simple_gallery_item, android.R.id.text1, Aktivitetsdata.instans.pakkekategorier)
    @Override
    public int getCount() {
      return Aktivitetsdata.instans.pakkekategorier.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return Aktivitetsdata.instans.pakkekategorier.get(position);
    }

    @Override
    public float getPageWidth(int position) {
      return 0.95f;
    }

    @Override
    public Fragment getItem(int position) {
      Fragment f = new KarruselFrag();
      Bundle b = new Bundle();
      b.putInt("position", position);
      f.setArguments(b);
      return f;
    }
  }

  public static class KarruselFrag extends Fragment implements OnItemClickListener, OnItemLongClickListener {
    ArrayList<String> klasserDerVisesNu = new ArrayList<String>();
    private int position;
    private Aktivitetsliste2 a;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      a = (Aktivitetsliste2) getActivity();
      position = getArguments().getInt("position");
      Aktivitetsdata.instans.tjekForAndreFilerIPakken(position);
      klasserDerVisesNu.addAll(Aktivitetsdata.instans.klasselister.get(position));

      // Anonym nedarving af ArrayAdapter med omdefineret getView()
      ArrayAdapter<String> klasserDerVisesNuAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, klasserDerVisesNu) {
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
      ListView visKlasserListView = new ListView(getActivity());
      visKlasserListView.setAdapter(klasserDerVisesNuAdapter);

      visKlasserListView.setOnItemClickListener(this);
      visKlasserListView.setOnItemLongClickListener(this);

      return visKlasserListView;
    }


    public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
      String akt = klasserDerVisesNu.get(position);

      if (a.seKildekode.isChecked() || akt.endsWith(".java") || akt.endsWith(".xml")) {
        a.visKildekode(akt);
        return;
      }

      try {
        // Tjek at klassen faktisk kan indlæses (så prg ikke crasher hvis den ikke kan!)
        Class klasse = Class.forName(akt);

        if (akt.toLowerCase().contains("fragment") && Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
          a.visDialog("Denne aktivitet kan kun køre på Android 4\nSkal den køre på Android 2 skal et kompatibilitetsbibliotek inkluderes og koden ændres til at bruge kompatibilitetsbiblioteket.");
          return;
        }
        startActivity(new Intent(getActivity(), klasse));
        a.overridePendingTransition(0, 0); // hurtigt skift
        Toast.makeText(getActivity(), akt + " startet", Toast.LENGTH_SHORT).show();
      } catch (Throwable e) {
        e.printStackTrace();
        //while (e.getCause() != null) e = e.getCause(); // Hop hen til grunden
        String tekst = akt + " gav fejlen:\n" + Log.getStackTraceString(e);
        a.visDialog(tekst);
      }

      // Find position i fuld liste
      position = Aktivitetsdata.instans.alleAktiviteter.indexOf(akt);
      // Gem position og 'start aktivitet direkte' til næste gang

      /*
      PreferenceManager.getDefaultSharedPreferences(this).edit().
              putInt("position", position).
              putInt("kategoriPos", kategorivalg.getSelectedItemPosition()).
              commit();
              */
    }

    public boolean onItemLongClick(AdapterView<?> listView, View v, int position, long id) {
      a.visKildekode(klasserDerVisesNu.get(position));
      return true;
    }
  }
}
