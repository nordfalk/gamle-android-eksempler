package lekt02_aktiviteter;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * @author Jacob Nordfalk
 */
public class Hjaelp_akt extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String hjælpHtml = "<html><body>"
            + "<h1>Hj&aelig;lpesk&aelig;rm</h1>"
            + "<p>Her kunne st&aring; noget hj&aelig;lp.<br>Men den er ikke skrevet endnu.</p>";

    WebView wv = new WebView(this);
    wv.loadData(hjælpHtml, "text/html", null);

    setContentView(wv);
  }
}
