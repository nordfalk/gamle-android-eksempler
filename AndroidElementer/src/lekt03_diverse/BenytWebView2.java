package lekt03_diverse;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

public class BenytWebView2 extends Activity {

  public class MinKlasse {

    @android.webkit.JavascriptInterface // Skal med fra API level 17+
    public void visToast(String tekst) {
      Log.d("MinKlasse", "Viser toast: " + tekst);
      Toast.makeText(BenytWebView2.this, tekst, Toast.LENGTH_LONG).show();
    }

    @android.webkit.JavascriptInterface // Skal med fra API level 17+
    public void spilLyd() {
      Log.d("MinKlasse", "Spiller en lyd");
      MediaPlayer.create(BenytWebView2.this, R.raw.dyt).start();
    }

    @android.webkit.JavascriptInterface // Skal med fra API level 17+
    public void afslut() {
      finish();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    WebView webView = new WebView(this);

    webView.getSettings().setJavaScriptEnabled(true);

    MinKlasse mitObjekt = new MinKlasse();
    webView.addJavascriptInterface(mitObjekt, "mitObjekt");

    // Tillad alerts og popupper fra JS
    webView.setWebChromeClient(new WebChromeClient());
    webView.loadUrl("javascript:alert('En alert fra Java')");

    // Behold navigeringen i dette webview (start aldrig ekstern browser)
    /* kommenter ind for at få webviewet til at (prøve at) vise alle links
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d("MinKlasse", "Navigering til "+ url);
        view.loadUrl(url);
        return false; // Håndtér selv
      }
    });
    */

    webView.loadUrl("file:///android_asset/benytwebview.html");
    setContentView(webView);
  }
}
