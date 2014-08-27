package dk.andreas.tabvejrny.aktiviteter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import dk.andreas.tabvejrny.VejrkortBillede;

public class Vejrkort extends Activity {

    private static String TAG = "Vejret"; //  "VejrkortAktivitet";

  // Physical display width and height.
  private static int displayWidth=0;
  private static int displayHeight=0;

  /** Called when the activity is first created. */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate() savedInstanceState="+savedInstanceState);
    super.onCreate(savedInstanceState);

    // displayWidth and displayHeight will change depending on screen
    // orientation. To get these dynamically, we should hook onSizeChanged().
    // This simple example uses only landscape mode, so it's ok to get them
    // once on startup and use those values throughout.
    Display display=((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    displayWidth=display.getWidth();
    displayHeight=display.getHeight();

    // SampleView constructor must be constructed last as it needs the
    // displayWidth and displayHeight we just got.
    setContentView(new VejrkortBillede(this, null));
  }

}
