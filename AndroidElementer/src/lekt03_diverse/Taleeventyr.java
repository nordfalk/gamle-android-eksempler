package lekt03_diverse;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Jacob Nordfalk
 */
public class Taleeventyr extends Activity implements OnInitListener, OnClickListener {

  TextView udtaleTekst;
  TextToSpeech tts;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    udtaleTekst = new TextView(this);
    udtaleTekst.setText("Min danske oot tale - med Locale US - eer maiet dorli.");

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

  public void onClick(View arg0) {
    String tekst = udtaleTekst.getText().toString();
    tts.speak(tekst, TextToSpeech.QUEUE_ADD, null);
  }


  public static String lavEventyr()
  {
    ArrayList<String> personer = new ArrayList<String>(); // liste af strenge
    personer.add("Mor");
    personer.add("Far");
    personer.add("Thor");
    personer.add("Aske");
    personer.add("Mormor");
    personer.add("Axel");
    personer.add("Freja");
    personer.add("Gülsen");
    personer.add("Numsen");
    personer.add("Diller");
    personer.add("Hr Knivkniv");
    personer.add("Tumbe");
    personer.add("Sebastian");
    personer.add("Ven");
    personer.add("Alexander");
    personer.add("Albert");

    ArrayList<String> handlinger = new ArrayList<String>();
    handlinger.add("slikker sig om munden.\n");
    handlinger.add("kysser.\n");
    handlinger.add("hopper og danser med sin dollarautomat.\n");
    handlinger.add("laver lort.\n");
    handlinger.add("tegner på");
    handlinger.add("tisser.\n");
    handlinger.add("ser fjernsyn.\n");
    handlinger.add("slår en prut.\n");
    handlinger.add("og");
    handlinger.add("og");
    handlinger.add("eller");
    handlinger.add("kaster op.\n");
    handlinger.add("elsker at");
    handlinger.add("kan ikke lide at");
    handlinger.add("går.");
    handlinger.add("er MEGET glad, fordi:");
    handlinger.add("er træt og går i seng.\n");

    String eventyr = "";

    for (int i=0; i<20; i++) {
      int antalPersoner = personer.size(); // antal personer i listen, dvs 3
      int personNummer = (int) (Math.random()*antalPersoner); // giver 0-2
      String person = personer.get( personNummer );
      String handling = handlinger.get( (int)(Math.random()*handlinger.size()));
      System.out.println(person + " " + handling);
      eventyr = eventyr + person + " " + handling + " ";
    }
    return eventyr;
  }

}
