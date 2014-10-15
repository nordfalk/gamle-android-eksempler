package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import dk.nordfalk.android.elementer.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class BenytFragment extends Activity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt07_fragmenter);

    if (savedInstanceState == null) {
      MitFragment fragment = new MitFragment();
      getFragmentManager().beginTransaction()
          .add(R.id.fragmentindhold, fragment)  // i XML R.id.layout
          .commit();
    }

  }


}
