package dk.andreas.tabvejrny.aktiviteter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import dk.andreas.tabvejrny.dataklasser.Tabs;
import dk.andreas.tabvejrny.dataklasser.Tab;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MotionEvent;
import dk.andreas.tabvejrny.MainActivity;
import dk.andreas.tabvejrny.R;
import dk.andreas.tabvejrny.dataklasser.GPSBy;
import dk.andreas.tabvejrny.dataklasser.GPSTilBy;
import dk.andreas.tabvejrny.dataklasser.S;
import java.util.ArrayList;

public class Settings extends PreferenceActivity {
    private final String TAG = "Settings";
    private Context context;
    private CharSequence[] byvalg;
    private ArrayList<GPSBy> gpsal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        context = this;
        addPreferencesFromResource(R.xml.settings);

        ArrayList<Tab> al =  Tabs.LæsAl();
        for(Tab tab: al){
            if(tab.LæsKlasse().equals("Settings"))
                al.remove(tab);
        }
        String[] sEntries = new String[al.size()];
        String[] sEntryValues = new String[al.size()];

        int i=0;
        for(Tab tab : al){
            if (tab.LæsId()<al.size()+1) {
                sEntries[i] = tab.LæsNavn();
                sEntryValues[i] = String.valueOf(tab.LæsId());
            }
            i++;
        }
        CharSequence[] entries = sEntries;
    	CharSequence[] entryValues = sEntryValues;

        final ListPreference tabs = (ListPreference) findPreference(S.PREF_TAB_KEY);
        tabs.setEntries(entries);
        tabs.setEntryValues(entryValues);
        tabs.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            public boolean onPreferenceChange(Preference arg0, Object arg1) {
                Log.d(TAG, "OnPreferenceChangeListener - Tabs");
                Intent intent = new Intent(Settings.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
        });

        Preference pref = findPreference("posBestem");
        pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
               if (newValue.toString().equals("pos_valgtby")) {
                    posBestemValg1();
               }
               else {
                    Log.d(TAG, "OnPreferenceChangeListener - PosBestem");
                    Intent intent = new Intent(Settings.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
               }
               return true;
            }
        });
    }

    private void posBestemValg1() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.posValg1)
                .setItems(R.array.posValgt1Values,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        Log.d(TAG,"**********Valgt: " + String.valueOf(i));
                        posBestemValg2(i);
                    }
                })
                .show();
    }

    private void posBestemValg2(int i) {
        Log.d(TAG, "posBestemValg2" + String.valueOf(i));
        gpsal = new ArrayList<GPSBy>();
        if(i==0) {
            gpsal = GPSTilBy.ReturnByName();
        }
        if(i==1) {
            Log.d(TAG,"***************i=2");
            SharedPreferences settings = getSharedPreferences(S.PREFS_NAME, 0);
            String locationLatString = settings.getString(S.LocationLatString, "0");
            String locationLonString = settings.getString(S.LocationLonString, "0");
            Double locationLatDouble = Double.parseDouble(locationLatString);
            Double locationLonDouble = Double.parseDouble(locationLonString);

            gpsal = GPSTilBy.ReturnClosest(locationLatDouble,locationLonDouble, 10);
        }

        int j = 0;
        byvalg = new CharSequence[gpsal.size()];
        for (GPSBy by: gpsal) {
            byvalg[j] = by.LæsBynavn();
            Log.d(TAG,"*************by: " + by.LæsBynavn());
            j++;
        }
        
        Log.d(TAG, String.valueOf(R.array.posValgt1Values));

        new AlertDialog.Builder(this)
                .setTitle(R.string.posValg1)
                .setItems(byvalg,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        Log.d(TAG, "****************" + byvalg[i]);
                        GPSBy tmpby = (GPSBy) gpsal.get(i);
                        SharedPreferences settings = getSharedPreferences(S.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(S.LocationLatString, String.valueOf(tmpby.Læslat().toString()));
                        editor.putString(S.LocationLonString, String.valueOf(tmpby.Læslon().toString()));
                        editor.putString(S.ByString, tmpby.LæsBynavn());
                        editor.putString(S.PostnrString, tmpby.LæsPostnr());
                        editor.putBoolean(S.LocationOpdateretBoolean, true);
                        editor.commit();

                        Log.d(TAG, "OnPreferenceChangeListener - PosBestem");
                        Intent intent = new Intent(Settings.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .show();


    }

    public Resources læsResources() {
        return getResources();
    }


}
