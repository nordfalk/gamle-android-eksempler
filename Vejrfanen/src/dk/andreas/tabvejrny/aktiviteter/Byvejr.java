package dk.andreas.tabvejrny.aktiviteter;

import dk.andreas.tabvejrny.dataklasser.Billede;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import dk.andreas.tabvejrny.HentogGemBillede;
import dk.andreas.tabvejrny.R;
import dk.andreas.tabvejrny.dataklasser.S;
import dk.andreas.tabvejrny.dataklasser.BilledeOpdateret.OnImageAddedListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Byvejr extends Activity implements OnImageAddedListener {

    ImageView piv1, piv2, piv3;
    TextView ptv;
    String TAG="Byvejr";

    public static Byvejr instans;

    public void onCreate(Bundle savedInstanceState) {
        instans = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.byvejr);

        Log.d(TAG,"onCreate");

        ptv = (TextView)findViewById(R.id.PrognoseTextView);
        piv1 = (ImageView)findViewById(R.id.PrognoseImageView1);
        piv2 = (ImageView)findViewById(R.id.PrognoseImageView2);
        piv3 = (ImageView)findViewById(R.id.PrognoseImageView3);
        
        String prognosetext = new String();
        prognosetext += "Byvejr";
        ptv.setText(prognosetext);

        SharedPreferences settings = getSharedPreferences(S.PREFS_NAME, 0);
        String postnrString = settings.getString(S.PostnrString, "");
        piv1.setAdjustViewBounds(true);
        piv2.setAdjustViewBounds(true);
        piv3.setAdjustViewBounds(true);
        piv1.setImageResource(R.drawable.henter);
        piv2.setImageResource(R.drawable.henter);
        piv3.setImageResource(R.drawable.henter);

        SharedPreferences preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        String gpsPref = preferences.getString("posBestem", "pos_aktuel");

        if(!gpsPref.equals("pos_aktuel")) {
            HentogGemBillede hg = new HentogGemBillede();
            try {
                hg.HentBillede(new Billede("dag0_2_" + postnrString, "http://servlet.dmi.dk/byvejr/servlet/byvejr_dag1?by="+ postnrString +"&mode=long", 1, 3600000), this);
                hg.HentBillede(new Billede("dag3_9_" + postnrString, "http://servlet.dmi.dk/byvejr/servlet/byvejr?by="+ postnrString +"&tabel=dag3_9", 2, 3600000), this);
                hg.HentBillede(new Billede("dag10-14_" + postnrString, "http://servlet.dmi.dk/byvejr/servlet/byvejr?by="+ postnrString +"&tabel=dag10_14", 3, 3600000), this);
            } catch (IOException ex) {
                Log.d("Radar4","IOExcept");
                Logger.getLogger(Radar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void onImageAdded(Billede b) {
        Log.d(TAG,"OnImageAdded " + b.LæsNavn() + "Type: " + String.valueOf(b.LæsType()));
        switch (b.LæsType()) {
            case 1: 
                piv1.setImageBitmap(b.LæsBillede());
                break;
            case 2: 
                piv2.setImageBitmap(b.LæsBillede());
                break;
            case 3: 
                piv3.setImageBitmap(b.LæsBillede());
                break;
        }
    }

    public void onCouldNotDownload(Billede b) {
        Log.d(TAG,"onCouldNotDownload " + b.LæsNavn());
        switch (b.LæsType()) {
            case 1:
                piv1.setImageResource(R.drawable.kanikkehente);
                break;
            case 2:
                piv2.setImageResource(R.drawable.kanikkehente);
                break;
            case 3:
                piv3.setImageResource(R.drawable.kanikkehente);
                break;
        }
    }


    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
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
        instans = null;
        Log.d(TAG, "onDestroy");
    }

    public void setInstans() {
        instans=this;
    }
}

