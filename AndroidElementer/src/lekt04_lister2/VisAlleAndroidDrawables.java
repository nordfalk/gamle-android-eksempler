package lekt04_lister2;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

/**
 * Hvis man har en lang liste, krav til jævn scrolling og/eller skal
 * vise listeelementer der kræver indlæsning af resurser.
 * På Android bruges det goe gamle designmønster med GUI-tråden: Al GUI
 * afvikles i ÉN tråd, både visning/scrolling og forberedelse af de ting
 * der skal vise. I visse tilfælde bliver det vigtigt at tænke sig godt
 * om når man programmerer listefunktioner.
 * Der skal simpelt hen ske så lidt som muligt (herunder ingen indlæsning
 * af resurser eller dekodning af bitmaps) synkront i GUI-tråden under
 * listevisningen.
 * Et andet tip er her:
 * http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
 * Et tredje tip er at tænke over hvilken baggrund man bruger. Jeg har
 * selv prøvet at gå fra forfærdelig til fantastisk performance ved at
 * kigge mine ListViews/ListActivities efter i sømmene og få luget ud i
 * baggrundene.
 *
 * @author j
 */
public class VisAlleAndroidDrawables extends Activity {
  /**
   * Om billeder og resurser skal indlæses i en baggrundstråd eller i GUI-tråden
   */
  boolean asynkronIndlæsning = true;
  /**
   * Om views bliver genbrugt eller ej
   */
  boolean genbrugElementer = true;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ListView listView = new ListView(this);
    listView.setAdapter(new AndroidDrawablesAdapter());
    listView.setDividerHeight(3);


		/*
    håndtering af baggrundsbilleder
		listView.setBackgroundResource(R.drawable.bil);

		// Sørg for at baggrunden bliver tegnet, også når listen scroller.
		// Se http://android-developers.blogspot.com/2009/01/why-is-my-list-black-android.html
		listView.setCacheColorHint(0x00000000);
		// Man kunne have en ensfarvet baggrund, det gør scroll mere flydende
		//getListView().setCacheColorHint(0xffe4e4e4);
		 */
    if (savedInstanceState == null) {
      // Ny aktivitet, vis hjælp
      Toast.makeText(this, "Dette eksempel viser også hvor stor forskel genbrug af elementer og asynkron indlæsning gør", Toast.LENGTH_LONG).show();
      Toast.makeText(this, "Tryk MENU for at slå disse forbedringer fra og mærk forskellen", Toast.LENGTH_LONG).show();
    }

    setContentView(listView);
  }

  public class AndroidDrawablesAdapter extends BaseAdapter {
    Resources res = getResources();

    public int getCount() {
      return 1500;
    } // der er omkring tusind drawables

    public Object getItem(int position) {
      return position;
    } // bruges ikke

    public long getItemId(int position) {
      return position;
    } // bruges ikke

    public View getView(final int position, View view, ViewGroup parent) {
      final ListeelemViewholder listeelem;
      if (view == null || !genbrugElementer) {
        view = getLayoutInflater().inflate(R.layout.listeelement, null);

        // For at spare CPU-cykler cacher vi opslagene i findViewById(). Se
        // http://developer.android.com/training/improving-layouts/smooth-scrolling.html
        listeelem = new ListeelemViewholder();
        listeelem.overskrift = (TextView) view.findViewById(R.id.listeelem_overskrift);
        listeelem.beskrivelse = (TextView) view.findViewById(R.id.listeelem_beskrivelse);
        listeelem.billede = (ImageView) view.findViewById(R.id.listeelem_billede);
        view.setTag(listeelem);
      } else {
        listeelem = (ListeelemViewholder) view.getTag();
      }

      final int resurseId = android.R.drawable.alert_dark_frame + position; // første resurse
      listeelem.overskrift.setText(Integer.toString(resurseId));
      listeelem.beskrivelse.setText("Hex: " + Integer.toHexString(resurseId));

      // For at sikre flydende scroll kan vi IKKE indlæse resursen i GUI-tråden
      if (!asynkronIndlæsning) {
        listeelem.billede.setImageResource(resurseId);
      } else {
        listeelem.billede.setImageDrawable(null);
        listeelem.position = position;
        // Brug en AsyncTask til at indlæse billedet i baggrunden
        new AsyncTask() {
          @Override
          protected Object doInBackground(Object... params) {
            // Tjek om viewholderen er blevet genbrugt til anden position
            if (listeelem.position == position) {
              try {
                return res.getDrawable(resurseId); // Overfør til onPostExecute()
              } catch (Exception e) {// sker hvis en drawable med det ID ikke findes
              }
            }
            return null;
          }

          @Override
          protected void onPostExecute(Object result) {
            // Tjek om viewholderen er blevet genbrugt til anden position
            if (listeelem.position != position) {
              return;
            }
            listeelem.billede.setImageDrawable((Drawable) result);
          }
        }.execute();
      }


      return view;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(Menu.NONE, 100, Menu.NONE, "Genbrug elementer");
    menu.add(Menu.NONE, 101, Menu.NONE, "Asynkron indlæsning");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == 100) {
      genbrugElementer = !genbrugElementer;
      Toast.makeText(this, "Genbrug elementer: " + genbrugElementer, Toast.LENGTH_SHORT).show();
    }
    if (item.getItemId() == 101) {
      asynkronIndlæsning = !asynkronIndlæsning;
      Toast.makeText(this, "Asynkron indlæsning: " + asynkronIndlæsning, Toast.LENGTH_SHORT).show();
    }
    return true;
  }
}
