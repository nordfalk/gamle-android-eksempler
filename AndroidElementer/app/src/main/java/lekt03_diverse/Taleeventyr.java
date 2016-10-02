package lekt03_diverse;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Jacob Nordfalk
 */
public class Taleeventyr extends AppCompatActivity implements OnInitListener {

  TextView udtaleTekst;
  TextToSpeech tts;

  public static String lavEventyr() {
    ArrayList<String> personer = new ArrayList<String>(); // liste af strenge
    personer.add("Mor");
    personer.add("Far");
    personer.add("Thor");
    personer.add("Aske");
    personer.add("Mormor");
    personer.add("Axel");
    personer.add("Freja");
    personer.add("Søren");
    personer.add("Diller");
    personer.add("Jacob");
    personer.add("Tumbe");
    personer.add("Sebastian");
    personer.add("min ven");
    personer.add("Alexander");
    personer.add("Albert");

    ArrayList<String> handlinger = new ArrayList<String>();
    handlinger.add("slikker sig om munden.\n");
    handlinger.add("kysser.\n");
    handlinger.add("griner.\n");
    handlinger.add("er meget tilfreds.\n");
    handlinger.add("gaber.\n");
    handlinger.add("hopper og danser.\n");
    handlinger.add("går på toilettet.\n");
    handlinger.add("tisser.\n");
    handlinger.add("ser fjernsyn.\n");
    handlinger.add("slår en prut.\n");
    handlinger.add("kaster op.\n");
    handlinger.add("går.\n");
    handlinger.add("går sin vej.\n");
    handlinger.add("er træt og går i seng.\n");
    handlinger.add("tegner på ");
    handlinger.add("og ");
    handlinger.add("eller ");
    handlinger.add("elsker at ");
    handlinger.add("kan ikke lide at ");
    handlinger.add("er MEGET glad, fordi ");

    String eventyr = "";

    for (int i = 0; i < 20; i++) {
      int antalPersoner = personer.size(); // antal personer i listen
      int personNummer = (int) (Math.random() * antalPersoner);
      String person = personer.get(personNummer);
      String handling = handlinger.get((int) (Math.random() * handlinger.size()));
      System.out.println(person + " " + handling);
      eventyr = eventyr + person + " " + handling;
    }
    return eventyr + "\nOg de levede lykkeligt til deres dages ende.";
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    udtaleTekst = new TextView(this);
    udtaleTekst.setTextSize(18);

    setContentView(udtaleTekst);
    tts = new TextToSpeech(this, this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    tts.stop();
    tts.shutdown();
  }

  // Fra TextToSpeech.OnInitListener
  public void onInit(int status) {
    if (status == TextToSpeech.SUCCESS) {
      boolean initialiseret = true;
      int res = tts.setLanguage(new Locale("da", ""));
      if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
        res = tts.setLanguage(Locale.getDefault());
        if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
          res = tts.setLanguage(Locale.US);
          if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
            initialiseret = false;
          }
        }
      }


      if (initialiseret) {
        Locale sprog = tts.getLanguage();

        String eventyr = lavEventyr();
        udtaleTekst.setText(eventyr);

        tts.speak(eventyr, TextToSpeech.QUEUE_ADD, null);
      } else {
        udtaleTekst.setText("Kunne ikke indlæse sprog. res er " + res);
      }
    } else {
      udtaleTekst.setText("Kunne ikke indlæse TTS. Status er " + status);
    }
  }

}
