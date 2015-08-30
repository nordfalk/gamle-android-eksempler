package dk.nordfalk.aktivitetsliste;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class VisKildekodeIWebView extends Activity {

  WebView webView;
  String filnavn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    webView = new WebView(this);

    VisKildekode.findWebUrl(this);

    Intent kaldtMedIntent = getIntent();
    if (kaldtMedIntent.getExtras() != null) {
      filnavn = kaldtMedIntent.getExtras().getString(VisKildekode.KILDEKODE_FILNAVN);
    }

    if (filnavn != null) {
      webView.loadUrl(VisKildekode.LOKAL_PRÆFIX + filnavn);
      webView.getSettings().setDefaultTextEncodingName("UTF-8");
      webView.getSettings().setBuiltInZoomControls(true);
      webView.setInitialScale(75);
    } else {
      webView.loadData("<h2>Manglede ekstradata med filen der skal vises.</h2> \n"
              + "Du kan lave et 'langt tryk' på aktivitetslisten for at vise kildekoden til en aktivitet", "text/html", "UTF-8");
      webView.setInitialScale(100);
    }

    setContentView(webView);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, 100, 0, "Vis på nettet");
    menu.add(0, 101, 0, "Vis lokalt");
    menu.add(0, 103, 0, "Ekstern browser");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == 100) {
      webView.loadUrl(VisKildekode.HS_PRÆFIX + filnavn);
    } else if (item.getItemId() == 101) {
      webView.loadUrl(VisKildekode.LOKAL_PRÆFIX + filnavn);
    } else if (item.getItemId() == 103) {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(VisKildekode.HS_PRÆFIX + filnavn)));
    }
    return true;
  }
}
