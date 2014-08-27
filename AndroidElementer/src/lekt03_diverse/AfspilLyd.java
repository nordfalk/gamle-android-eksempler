package lekt03_diverse;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class AfspilLyd extends Activity implements OnClickListener {

  Button enKnap;
  MediaPlayer enLyd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    enKnap = new Button(this);
    enKnap.setText("Spil en lyd");
    enKnap.setOnClickListener(this);
    setContentView(enKnap);

    // Opret lyden
    enLyd = MediaPlayer.create(this, R.raw.jeg_bremser_haardt);
    enLyd.setVolume(1, 1);

    // Trykker brugeren på volumen op/ned i denne aktivitet skal det altid styre lydstyrken af medieafspilleren
    // (kalder man ikke dette så vil volumen op/ned styre telefonen RINGETONEs lydstyrke
    // og kun lige mens der er en lyd der spiller vil det være lydens lydstyrke der justeres)
    setVolumeControlStream(AudioManager.STREAM_MUSIC);

    // Tjek også hvad lydstyrken er lige nu og hvis lyden er lavere end 1/5 af fuld lydstyrke
    // så skru volumen og til 1/5 af fuld lydstyrke
    AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    int fuldStyrke = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    int aktuelStyrke = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    if (aktuelStyrke < fuldStyrke / 5) {
      audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, fuldStyrke / 5, AudioManager.FLAG_SHOW_UI);
    }

  }

  public void onClick(View arg0) {
    enLyd.start();
  }

  @Override
  public void onDestroy() {
    enLyd.stop();
    enLyd.release();
    super.onDestroy();
  }

}
