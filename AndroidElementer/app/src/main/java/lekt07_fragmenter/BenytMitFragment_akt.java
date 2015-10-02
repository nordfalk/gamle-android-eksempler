package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dk.nordfalk.android.elementer.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class BenytMitFragment_akt extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt04_fragmenter);

    if (savedInstanceState == null) {
      MitFragment_frag fragment = new MitFragment_frag();
      getSupportFragmentManager().beginTransaction()
              .add(R.id.fragmentindhold, fragment)  // tom container i layout
              .commit();
    }
  }
}
