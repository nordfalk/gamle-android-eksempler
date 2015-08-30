package lekt01_views;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.TextView;

/**
 * @author Jacob Nordfalk
 */
public class BenytHtmlTags extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView tv = new TextView(this);

    // B (bold), I (italic), U (underline), TT (monospace),
    // BIG, SMALL, SUP (superscript), SUB (subscript), and STRIKE (strikethrough).
    String htmltekst = "<b>Fed skrift</b><br />"
        + "og <small>lille skrift</small><br />"
        + "og <i>kursiv</i><br />"
        + "Understøttede tags er:<br />"
        + "B (<b>bold</b>), I (<i>italic</i>), U (<u>underline</u>), TT (<tt>monospace</tt>), <big>BIG</big>, "
        + "<small>SMALL</small>, SUP (<sup>superscript</sup>) og SUB (<sub>subscript</sub>)<br /><br />"
        + "Man kan også bruge klassen Linkify til at putte lænker ind i tekst.\n"
        + "Mit telefonnummer er 26206512, min e-post er jacob.nordfalk@gmail.com "
        + "og jeg har en hjemmeside på http://javabog.dk\n"
        + "Måske vil http://javabog.dk/OOP/kapitel2.jsp åbne AndroidElementers webbrowser.";

    tv.setText(Html.fromHtml(htmltekst));
    Linkify.addLinks(tv, Linkify.ALL);
    setContentView(tv);
  }
}
