package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;

import dk.nordfalk.android.elementer.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class BenytFragment extends Activity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Her kunne også pakkes et FrameLayout ud fra XML
    FrameLayout layout = new FrameLayout(this);
    layout.setId(R.id.indhold); // i XML android:id="@+id/layout"
    setContentView(layout);

    if (savedInstanceState == null) {
      MitFragment fragment = new MitFragment();
      getFragmentManager().beginTransaction()
          .add(R.id.indhold, fragment)  // i XML R.id.layout
          .commit();
    }

  }


}
