package lekt03_net;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class BenytNetOgAsyncTask extends Activity implements OnClickListener {
  Button knap1, knap2, knap3;
  TextView textView;

  public static String hentUrl(String url) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
    StringBuilder sb = new StringBuilder();
    String linje = br.readLine();
    while (linje != null) {
      sb.append(linje + "\n");
      linje = br.readLine();
      Log.d("LÆST LINJE", "" + linje);
    }
    return sb.toString();
  }

  private static String findTitler(String rssdata) {
    String titler = "";
    while (true) {
      int tit1 = rssdata.indexOf("<title>") + 7;
      int tit2 = rssdata.indexOf("</title>");
      if (tit2 == -1) break; // hop ud hvis der ikke er flere titler
      if (titler.length() > 400) break; // .. eller hvis vi har nok
      String titel = rssdata.substring(tit1, tit2);
      System.out.println(titel);
      titler = titler + titel + "\n";
      rssdata = rssdata.substring(tit2 + 8); // Søg videre i teksten efter næste titel
    }
    return titler;
  }

  // Til afprøvning af logikken i standard Java (meget hurtigere)
  public static void main(String[] args) throws IOException {
    String rssdata = hentUrl("http://www.version2.dk/it-nyheder/rss");
    String titler = findTitler(rssdata);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

    TableLayout tl = new TableLayout(this);

    textView = new TextView(this);
    textView.setText("Forskellige praksisser til netværkskommunikation");
    tl.addView(textView);

    knap1 = new Button(this);
    knap1.setText("Hent i forgrunden");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Hent i baggrunden");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("Vis cachet kopi og hent i baggrunden");
    tl.addView(knap3);

    setContentView(tl);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    setProgressBarIndeterminateVisibility(true);
    try {
      if (hvadBlevDerKlikketPå == knap1) {

        String rssdata = hentUrl("http://www.version2.dk/it-nyheder/rss");
        String titler = findTitler(rssdata);
        textView.setText(titler);
        setProgressBarIndeterminateVisibility(false);

      } else if (hvadBlevDerKlikketPå == knap2) {
        textView.setText("Henter...");

        new AsyncTask() {
          @Override
          protected Object doInBackground(Object... arg0) {
            try {
              String rssdata = hentUrl("http://www.version2.dk/it-nyheder/rss");
              String titler = findTitler(rssdata);
              return titler;
            } catch (Exception e) {
              e.printStackTrace();
              return e;
            }
          }

          @Override
          protected void onPostExecute(Object titler) {
            textView.setText("resultat: \n" + titler);
            setProgressBarIndeterminateVisibility(false);
          }
        }.execute();


      } else if (hvadBlevDerKlikketPå == knap3) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String titler = prefs.getString("titler", "(ingen titler)"); // Hent fra prefs
        textView.setText("henter... her er hvad jeg ved:\n" + titler);

        new AsyncTask() {
          @Override
          protected Object doInBackground(Object... arg0) {
            try {
              String rssdata = hentUrl("http://www.version2.dk/it-nyheder/rss");
              String titler = findTitler(rssdata);
              prefs.edit().putString("titler", titler).commit();     // Gem i prefs
              return titler;
            } catch (Exception e) {
              e.printStackTrace();
              return e;
            }
          }

          @Override
          protected void onPostExecute(Object titler) {
            textView.setText("nyeste titler: \n" + titler);
            setProgressBarIndeterminateVisibility(false);
          }
        }.execute();

      }
    } catch (Exception e) {
      e.printStackTrace();
      textView.setText("Der skete en fejl:\n" + e);
      setProgressBarIndeterminateVisibility(false);
    }

  }
}
