package lekt06_data;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import dk.nordfalk.android.elementer.R;

/**
 * Simpel dataklasse til at geme XML-værdier i
 */
class Kunde {
  String navn;
  double kredit;
}

/**
 * Simpel dataklasse til at geme XML-værdier i
 */
class Bank {
  String navn;
  ArrayList<Kunde> kunder = new ArrayList<Kunde>();
}

/**
 * @author Jacob Nordfalk
 */
public class XmlParsning extends Activity {
  //Ku også parse f.eks http://www.dmi.dk/dmi/rss-nyheder.xml ?

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView tv = new TextView(this);

    ArrayList<Bank> banker = new ArrayList<Bank>();

    try {
      InputStream is = getResources().openRawResource(R.raw.data_xmleksempel);
      // Det kan være nødvendigt at hoppe over BOM mark - se http://android.forums.wordpress.org/topic/xml-pull-error?replies=2
      //is.read(); is.read(); is.read(); // - dette virker kun hvis der ALTID er en BOM
      // Hop over BOM - hvis den er der!
      is = new BufferedInputStream(is);  // bl.a. FileInputStream understøtter ikke mark, så brug BufferedInputStream
      is.mark(1); // vi har faktisk kun brug for at søge én byte tilbage
      if (is.read() == 0xef) {
        is.read();
        is.read();
      } // Der var en BOM! Læs de sidste 2 byte
      else {
        is.reset(); // Der var ingen BOM - hop tilbage til start
      }
      /*  Til udvikliing i standard Java kan du bruge xpp3-1.1.4.jar fra http://www.extreme.indiana.edu/xgws/xsoap/xpp/
      XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
			XmlPullParser xpp=factory.newPullParser();
			 */
      XmlPullParser parser = android.util.Xml.newPullParser();  // Android-kald til at oprette parser
      parser.setInput(is, null); // null detekterer indkodning, brug evt. "UTF-8" eller "ISO-8859-1" her

      Bank bank = null; // Husker den aktuelle bank
      Kunde kunde = null; // Husker den aktuelle kunde

      int eventType = parser.getEventType();
      while (eventType != XmlPullParser.END_DOCUMENT) {
        if (eventType == XmlPullParser.START_TAG) {
          String tag = parser.getName();
          if ("bank".equals(tag)) {
            bank = new Bank();
            banker.add(bank);
            bank.navn = parser.getAttributeValue(null, "navn");
          } else if ("kunde".equals(tag)) {
            kunde = new Kunde();
            bank.kunder.add(kunde);
            kunde.navn = parser.getAttributeValue(null, "navn");
          } else if ("kredit".equals(tag)) {
            kunde.kredit = Double.parseDouble(parser.nextText());
          }
        }
        eventType = parser.next();
      }
      is.close();
    } catch (Exception ex) {
      ex.printStackTrace();
      tv.append("FEJL:" + ex.toString());
    }

    for (Bank bank : banker) {
      tv.append("\n=== Oversigt over " + bank.navn + "s kunder ===\n");
      double totalKredit = 0;
      for (Kunde kunde : bank.kunder) {
        tv.append(kunde.navn + " med " + kunde.kredit + " kr.\n");
        totalKredit = totalKredit + kunde.kredit;
      }
      tv.append("\n\nTotal kredit er " + totalKredit + " kr.");
    }

    setContentView(tv);
  }
}
