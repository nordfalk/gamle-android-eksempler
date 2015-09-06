package dk.nordfalk.aktivitetsliste;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

public class VisKildekodeIWebView extends AppCompatActivity {

  WebView webView;
  String filnavn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    webView = new WebView(this);

    VisKildekode.find_HS_PRÆFIX(this);

    Intent kaldtMedIntent = getIntent();
    if (kaldtMedIntent.getExtras() != null) {
      filnavn = kaldtMedIntent.getExtras().getString(VisKildekode.KILDEKODE_FILNAVN);
    }

    if (filnavn != null) {
      webView.loadUrl(VisKildekode.LOKAL_PRÆFIX + filnavn);
      webView.getSettings().setDefaultTextEncodingName("UTF-8");
      webView.getSettings().setBuiltInZoomControls(true);
      webView.setInitialScale(75);
      setTitle(filnavn);
    } else {
      webView.loadData("<h2>Manglede ekstradata med filen der skal vises.</h2> \n"
              + "Du kan lave et 'langt tryk' på aktivitetslisten for at vise kildekoden til en aktivitet", "text/html", "UTF-8");
      webView.setInitialScale(100);
    }

    setContentView(webView);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (Build.VERSION.SDK_INT>=19) menu.add(0, 115, 0, "Fuldskærm");
    menu.add(0, 100, 0, "Vis på nettet");
    menu.add(0, 101, 0, "Vis lokalt");
    menu.add(0, 103, 0, "Ekstern browser");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == 100) {
      webView.loadUrl(VisKildekode.HS_PRÆFIX + filnavn);
    } else if (item.getItemId() == 115) {
      if (Build.VERSION.SDK_INT>=19)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    } else if (item.getItemId() == 101) {
      webView.loadUrl(VisKildekode.LOKAL_PRÆFIX + filnavn);
    } else if (item.getItemId() == 103) {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(VisKildekode.HS_PRÆFIX + filnavn)));
    }
    return true;
  }
}
