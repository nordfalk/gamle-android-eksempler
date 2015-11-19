package lekt50_googlested;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import java.util.Locale;

/**
 * http://stackoverflow.com/questions/3058919/text-to-speechtts-android
 * http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/app/TextToSpeechActivity.html
 * http://developer.android.com/resources/articles/tts.html
 * http://android-coding.blogspot.com/2011/05/voice-recognizer-text-to-speech.html
 * Saluton, mi estas via amiko.
 * Kion vi volas fari hodiaŭ?
 * Tio estas pomo. Kion vi volas fari per la pomo?
 *
 * @author Jacob Nordfalk
 */
public class TekstTilTale implements OnInitListener {

  // Singleton designmønster
  private static TekstTilTale instans;

  public static TekstTilTale instans(Context ctx) {
    if (instans==null) {
      instans = new TekstTilTale();
      instans.tts = new TextToSpeech(ctx.getApplicationContext(), instans);
    }
    return instans;
  }

  private TextToSpeech tts;
  private boolean initialiseret;
  private String tekstDerVenter = "Tekst til tale initialiseret.";


  // Fra TextToSpeech.OnInitListener
  public void onInit(int status) {
    if (status == TextToSpeech.SUCCESS) {
      initialiseret = true;
      int res = tts.setLanguage(Locale.getDefault());
      if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
        res = tts.setLanguage(Locale.US);
        if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
          initialiseret = false;
        }
      }

      if (initialiseret) tts.speak(tekstDerVenter, TextToSpeech.QUEUE_ADD, null);
    }
  }

  public void tal(String tekst) {
    Log.d(tekst);
    if (instans.initialiseret) instans.tts.speak(tekst, TextToSpeech.QUEUE_ADD, null);
    else tekstDerVenter = tekst;
  }
}
