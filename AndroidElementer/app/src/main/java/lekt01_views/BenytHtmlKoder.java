package lekt01_views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.util.Linkify;
import android.widget.TextView;

/**
 * @author Jacob Nordfalk
 */
public class BenytHtmlKoder extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView tv = new TextView(this);

    // B (bold), I (italic), U (underline), TT (monospace),
    // BIG, SMALL, SUP (superscript), SUB (subscript), and STRIKE (strikethrough).
    String htmltekst = "Med Html&period;fromHtml() kan man nemt få tekst med <b>fed</b> skrift, <small>lille</small> skrift og <i>kursiv</i><br />" +
            "De understøttede HTML-koder er:<br />" +
            "B (<b>bold</b>), I (<i>italic</i>), U (<u>underline</u>), TT (<tt>monospace</tt>), <big>BIG</big>, " +
            "<small>SMALL</small>, SUP (<sup>superscript</sup>) og SUB (<sub>subscript</sub>)<br /><br /><br />" +
            "<br />" +
            "Med Linkify&period;addLinks() kan man putte lænker ind i tekst.<br />\n" +
            "Mit telefonnummer er 26206512, min e-post er jacob.nordfalk@gmail.com " +
            "og jeg har en hjemmeside på http://javabog.dk\n" +
            "Måske vil http://javabog.dk/OOP/kapitel2.jsp åbne AndroidElementers webbrowser.";

    Spanned tekst = Html.fromHtml(htmltekst);
    tv.setText(tekst);

    Linkify.addLinks(tv, Linkify.ALL);

    setContentView(tv);
  }
}
