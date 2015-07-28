package lekt03_diverse;

import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class AfspilVideo extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    VideoView videoView = new VideoView(this);
    setContentView(videoView);

    videoView.setVideoPath("http://javabog.dk/filer/hej_fra_Jacob.mp4");
    // Du kan eventuelt pege på en lokal fil fra SD-kortet i stedet
    //videoView.setVideoPath("file:///sdcard/DCIM/100MEDIA/VIDEO0025.3gp");
    videoView.setMediaController(new MediaController(this));
    videoView.requestFocus();
    videoView.start();

  }
}
