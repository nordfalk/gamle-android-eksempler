/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt05_sensorer;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

/**
 * @author Jacob Nordfalk
 */
public class Stedbestemmelse extends Activity implements LocationListener {

  TextView textView;
  ScrollView scrollView;
  LocationManager locationManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    textView = new TextView(this);
    scrollView = new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);
    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    textView.append("locationManager.getAllProviders() : " + locationManager.getAllProviders() + "\n\n");
    // Løb igennem alle udbyderne (typisk "gps", "network" og "passive")
    for (String udbyder : locationManager.getAllProviders()) {
      LocationProvider lp = locationManager.getProvider(udbyder);
      Location sted = locationManager.getLastKnownLocation(udbyder);

      textView.append(udbyder + " - tændt: " + locationManager.isProviderEnabled(udbyder)
          + "\n præcision=" + lp.getAccuracy() + " strømforbrug=" + lp.getPowerRequirement()
          + "\n kræver satellit=" + lp.requiresSatellite() + " kræver net=" + lp.requiresNetwork()
          + "\n sted=" + sted + "\n\n");

    }
		/*
    // Geofencing: Start denne aktivitet igen hvis vi nærmer os eller forlader Valby!
		Intent intent = new Intent(this, Stedbestemmelse.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
		locationManager.addProximityAlert(55.654074f, 12.493775f, 5000, 60*60*24*1000, pi);
		 */
  }

  @Override
  protected void onResume() {
    super.onResume();

    Criteria kriterium = new Criteria();
    kriterium.setAccuracy(Criteria.ACCURACY_FINE);
    String udbyder = locationManager.getBestProvider(kriterium, true); // giver "gps" hvis den er slået til

    textView.append("\n\n========= Lytter til udbyder: " + udbyder + "\n\n");

    if (udbyder == null) {
      textView.append("\n\nUps, der var ikke tændt for nogen udbyder. Tænd for GPS eller netværksbaseret stedplacering og prøv igen.");
      startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
      return;
    }

    //  Bed om opdateringer, der går mindst 60. sekunder og mindst 20. meter mellem hver
    locationManager.requestLocationUpdates(udbyder, 60000, 20, this);

    Location sted = locationManager.getLastKnownLocation(udbyder);

    if (sted != null) { // forsøg at finde nærmeste adresse
      try {
        Geocoder geocoder = new Geocoder(this);
        List<Address> adresser = geocoder.getFromLocation(sted.getLatitude(), sted.getLongitude(), 1);
        if (adresser != null && adresser.size() > 0) {
          Address adresse = adresser.get(0);
          textView.append("NÆRMESTE ADRESSE: " + adresse + "\n\n");
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(this);
  }

  // Metode specificeret i LocationListener
  public void onLocationChanged(Location sted) {
    textView.append(sted + "\n\n");
    scrollView.scrollTo(0, textView.getHeight()); // rul ned i bunden
  }

  // ignorér - men vi er nødt til at have metoderne for at implementere LocationListener
  public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
  }

  public void onProviderEnabled(String arg0) {
  }

  public void onProviderDisabled(String arg0) {
  }
}
