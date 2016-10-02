package lekt03_diverse;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import java.util.Locale;

import dk.nordfalk.android.elementer.R;

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
public class BenytTekstTilTale extends Activity implements OnInitListener, OnClickListener {

  EditText udtaleTekst;
  Button udtalKnap;
  TextToSpeech tts;
  boolean friskStart = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState == null) {
      friskStart = true; // tjek om skærm vendt eller aktivitet genstartet efter JVM har været smidt ud af hukommelsen
    }
    udtaleTekst = new EditText(this);
    udtaleTekst.setText("Min danske oot tale - med Locale US - eer maiet dorli.");
    udtaleTekst.setId(R.id.editText); // sæt ID så den redigerede tekst bliver genskabt ved skærmvending

    udtalKnap = new Button(this);
    udtalKnap.setOnClickListener(this);
    udtalKnap.setText("Vent, indlæser TTS-modul...");
    udtalKnap.setEnabled(false);

    TableLayout ll = new TableLayout(this);
    ll.addView(udtaleTekst, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
    ll.addView(udtalKnap);
    setContentView(ll);
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
        tts.speak("Tekst til tale initialiseret for sproget " + sprog.getDisplayLanguage(sprog), TextToSpeech.QUEUE_ADD, null);
        udtalKnap.setText("TTS klar for " + tts.getLanguage() + "\nMotor: " + tts.getDefaultEngine());
        udtalKnap.setEnabled(true);
        //tts.addSpeech("jeg bremser hårdt", getPackageName(), R.raw.jeg_bremser_haardt);

        if (friskStart) {
          tts.speak("Android-Elementers eksempel på text til tale er klar.", TextToSpeech.QUEUE_ADD, null);
        }
        // Vis tastaturet
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(udtaleTekst, InputMethodManager.SHOW_FORCED);
      } else {
        udtalKnap.setText("Kunne ikke indlæse sprog. res er " + res);
      }
    } else {
      udtalKnap.setText("Kunne ikke indlæse TTS. Status er " + status);
    }
  }

  public void onClick(View arg0) {
    String tekst = udtaleTekst.getText().toString();
    tts.speak(tekst, TextToSpeech.QUEUE_ADD, null);
  }
}
