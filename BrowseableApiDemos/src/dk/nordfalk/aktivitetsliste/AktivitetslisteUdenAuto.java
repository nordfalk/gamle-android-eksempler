package dk.nordfalk.aktivitetsliste;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Fejlsikker udgave der ikke starter aktivitet direkte
 * @author Jacob Nordfalk
 */
public class AktivitetslisteUdenAuto extends Aktivitetsliste {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Toast.makeText(this, "Slår autostart fra", Toast.LENGTH_LONG).show();

    PreferenceManager.getDefaultSharedPreferences(this).edit().
        putBoolean("autostart", false).commit();
    
    super.onCreate(savedInstanceState);
  }
}
