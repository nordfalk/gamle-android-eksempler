package dk.andreas.tabvejrny;

import dk.andreas.tabvejrny.dataklasser.GPSTilBy;
import dk.andreas.tabvejrny.dataklasser.GPSBy;
import dk.andreas.tabvejrny.dataklasser.S;
import dk.andreas.tabvejrny.dataklasser.Billeder;
import dk.andreas.tabvejrny.dataklasser.Tabs;
import dk.andreas.tabvejrny.dataklasser.Tab;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Filter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;
import dk.andreas.tabvejrny.aktiviteter.About;
import dk.andreas.tabvejrny.aktiviteter.Byvejr;
import dk.andreas.tabvejrny.aktiviteter.Glatfoere;
import dk.andreas.tabvejrny.aktiviteter.Radar;
import dk.andreas.tabvejrny.dataklasser.Billede;
import dk.andreas.tabvejrny.dataklasser.UVTal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends TabActivity implements LocationListener, OnClickListener {


   private LocationManager mgr;
   private final String TAG = "Mainactivity";
   private boolean plByvejr=false;
   private boolean plRadar=false;
   private boolean plGlatfoere=false;
   private String gpsPref, postnrString;
   private float x;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        Database.init(this);
        setContentView(R.layout.main);

        Resources res = getResources();
        Tabs.res = res;
        Billeder.res = res;
        UVTal.res = res;
        GPSTilBy.res = res;
        Billeder.SletAl();


        // Her hentes Positionsinformation fra Prefs
        SharedPreferences settings = getSharedPreferences(S.PREFS_NAME, 0);
        String locationLatString = settings.getString(S.LocationLatString, "0");
        String locationLonString = settings.getString(S.LocationLonString, "0");
        String byString = settings.getString(S.ByString, "");
        postnrString = settings.getString(S.PostnrString, "");

        SharedPreferences.Editor editor = settings.edit();

        if (locationLatString=="0")
            editor.putString(S.LocationLatString, "55.6783");
        if (locationLonString=="0")
            editor.putString(S.LocationLonString, "12.58");
        if (byString=="")
            editor.putString(S.ByString, "København");
        if (postnrString=="")
            editor.putString(S.PostnrString, "1000");
        editor.putBoolean(S.LocationOpdateretBoolean, false);
        editor.commit();

        SharedPreferences preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        gpsPref = preferences.getString("posBestem", "pos_aktuel");

        // Her bestemmes positionen
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String best = mgr.getBestProvider(criteria, true);
        Location lastlocation = mgr.getLastKnownLocation(best);
        if(!gpsPref.equals("pos_valgtby"))
            mgr.requestLocationUpdates(best, 100, 100, this);


        // Listener

        TabWidget tabWidget = getTabWidget();
        tabWidget.setOnTouchListener(gestureListener);

        // Opret intents og tabs for aktive tabs
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        Tabs.init(this);
        ArrayList<Tab> tmpAl = Tabs.LæsAl();
        tabHost.clearAllTabs();
        for(Tab tab: tmpAl){
            if(tab.LæsAktiv()) {
                intent = new Intent().setClassName(getPackageName(), getPackageName()  + ".aktiviteter." + tab.LæsKlasse());
                spec = tabHost.newTabSpec(tab.LæsNavn()).setIndicator(tab.LæsNavn()).setContent(intent);
                tabHost.addTab(spec);
            }
            Log.d(TAG, "Klassenavn: " + tab.LæsKlasse());
            if(tab.LæsKlasse().equals("Byvejr")&&tab.LæsAktiv()) {
                plByvejr=true;
                Log.d(TAG, "plByvejr = true");
            }
            if(tab.LæsKlasse().equals("Radar")&&tab.LæsAktiv())
                plRadar=true;
            if(tab.LæsKlasse().equals("Glatfoere")&&tab.LæsAktiv())
                plGlatfoere=true;
        }

    }

    public void onClick(View v) {
        Filter f = (Filter) v.getTag();
        //FilterFullscreenActivity.show(this, input, f);
    }     

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");

        // Her igangsættes hentning af billeder kan justeres til kun at hente resourcer hvis det ønskes
        if(plByvejr&&!gpsPref.equals("pos_aktuel")) {
            Log.d(TAG, "plByvejr henter billeder");
            preLoadByvejBilleder();
        }

        if(plRadar) {
            // Hvis Radar tab er sat til preload hentes billederne ned asynkront gennem HentAnimationsBilleder
            Log.d(TAG, "plRadar henter billeder");
            HentAnimationsBilleder hab = new HentAnimationsBilleder();
            hab.HentAnimationsBilleder("http://dmi.netrum.dk/filnavne.aspx", "http://dmi.netrum.dk/billeder/", null);
        }
        if(plGlatfoere) {
            // Hvis Glatføre tab er sat til preload hentes billederne ned asynkront gennem HentAnimationsBilleder
            Log.d(TAG, "plGlatFøre henter billeder");
            HentogGemBillede hg = new HentogGemBillede();
            try {
                hg.HentBillede(new Billede("glatfoere", "http://www.dmi.dk/dmi/200611090940_cmsglatprog.jpg", 5, 3600000), Glatfoere.instans);
            } catch (IOException ex) {
                Log.d(TAG,"IOExcept");
                Logger.getLogger(Radar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void onLocationChanged(Location location) {
        String bynavn = "";
        SharedPreferences settings = getSharedPreferences(S.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(S.LocationLatString, String.valueOf(location.getLatitude()));
        editor.putString(S.LocationLonString, String.valueOf(location.getLongitude()));

        ArrayList<GPSBy> gpsal = GPSTilBy.ReturnClosest(location.getLatitude(),location.getLongitude(), 1);
        for (GPSBy by : gpsal) {
            editor.putString(S.ByString, by.LæsBynavn());
            editor.putString(S.PostnrString, by.LæsPostnr());
            bynavn = by.LæsBynavn();
            postnrString = by.LæsPostnr();
        }
        editor.putBoolean(S.LocationOpdateretBoolean, true);
        editor.commit();
        mgr.removeUpdates(this); // GPS position kaldes kun en enkelt gang

        preLoadByvejBilleder();

        Toast toast = Toast.makeText(this, "Position er " + bynavn, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onProviderEnabled(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onProviderDisabled(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        HentogGemBillede hg = new HentogGemBillede();
        hg.SletGamleBilleder(86400000);  // Slet billeder der er mere end 24 timer gamle
//        hg.SletGamleBilleder(0);
        //Billeder.SletAl();

    }
    public void preLoadByvejBilleder() {
            HentogGemBillede hg = new HentogGemBillede();
            try {
                Log.d(TAG,"Henter billeder****:" + postnrString);
                hg.HentBillede(new Billede("dag0_2_" + postnrString, "http://servlet.dmi.dk/byvejr/servlet/byvejr_dag1?by="+ postnrString +"&mode=long", 1, 3600000), Byvejr.instans);
                hg.HentBillede(new Billede("dag3_9_" + postnrString, "http://servlet.dmi.dk/byvejr/servlet/byvejr?by="+ postnrString +"&tabel=dag3_9", 2, 3600000), Byvejr.instans);
                hg.HentBillede(new Billede("dag10-14_" + postnrString, "http://servlet.dmi.dk/byvejr/servlet/byvejr?by="+ postnrString +"&tabel=dag10_14", 3, 3600000), Byvejr.instans);
            } catch (IOException ex) {
                Log.d(TAG,"IOExcept");
                Logger.getLogger(Radar.class.getName()).log(Level.SEVERE, null, ex);
            }
    }


    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            x = event.getX();
        }

        float xDist = x-event.getX();
        if(xDist>150) {
            //Toast toast = Toast.makeText(this, "Venstre", Toast.LENGTH_SHORT);
            //toast.show();
            TabHost tabHost = getTabHost();
            int tabid=tabHost.getCurrentTab();
            tabHost.setCurrentTab(tabid-1);
            x = event.getX();
        }
        if(xDist<-150) {
            //Toast toast = Toast.makeText(this, "Højre", Toast.LENGTH_SHORT);
            //toast.show();
            TabHost tabHost = getTabHost();
            int tabid=tabHost.getCurrentTab();
            tabHost.setCurrentTab(tabid+1);
            x = event.getX();
        }
        return true;

    }

}
