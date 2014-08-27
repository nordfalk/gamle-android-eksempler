package dk.andreas.tabvejrny.aktiviteter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import dk.andreas.tabvejrny.HentogGemBillede;
import dk.andreas.tabvejrny.R;
import dk.andreas.tabvejrny.dataklasser.Billede;
import dk.andreas.tabvejrny.dataklasser.BilledeOpdateret.OnImageAddedListener;
import dk.andreas.tabvejrny.dataklasser.S;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Glatfoere extends Activity implements OnImageAddedListener {
    ImageView gfiv;
    TextView gftv;
    String TAG="Glatføre";

    public static Glatfoere instans;

    public void onCreate(Bundle savedInstanceState) {
        instans = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glatfoere);

        Log.d(TAG,"onCreate");

        gftv = (TextView)findViewById(R.id.GlatfoereTextView);
        gfiv = (ImageView)findViewById(R.id.GlatfoereImageView);

        String prognosetext = new String();
        prognosetext += "Glatførevarsel";
        gftv.setText(prognosetext);

        gfiv.setAdjustViewBounds(true);
        gfiv.setImageResource(R.drawable.henter);

            HentogGemBillede hg = new HentogGemBillede();
            try {
                hg.HentBillede(new Billede("glatfoere", "http://www.dmi.dk/dmi/200611090940_cmsglatprog.jpg", 5, 3600000), this);
            } catch (IOException ex) {
                Log.d(TAG,"IOExcept");
                Logger.getLogger(Radar.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    public void onImageAdded(Billede b) {
        Log.d(TAG,"OnImageAdded " + b.LæsNavn() + "Type: " + String.valueOf(b.LæsType()));
        switch (b.LæsType()) {
            case 5:
                gfiv.setImageBitmap(b.LæsBillede());
                break;
        }
    }

    public void onCouldNotDownload(Billede b) {
        Log.d(TAG,"onCouldNotDownload " + b.LæsNavn());
        switch (b.LæsType()) {
            case 5:
                gfiv.setImageResource(R.drawable.kanikkehente);
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

