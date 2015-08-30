package lekt02_aktiviteter;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import dk.nordfalk.android.elementer.R;

public class Indstillinger_akt extends PreferenceActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.lekt02_indstillinger);
  }
}
