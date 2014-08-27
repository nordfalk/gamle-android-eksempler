package dk.andreas.tabvejrny.aktiviteter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import dk.andreas.tabvejrny.dataklasser.S;
import dk.andreas.tabvejrny.dataklasser.UVTal;
import dk.andreas.tabvejrny.dataklasser.UVindeksProvider;
import java.io.InputStream;
import java.net.URL;

public class UVIndeks extends Activity {

    private TextView textview;
    String TAG = "UVIndeks";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        textview = new TextView(this);

        String PREF_FILE_NAME = getPackageName() + "_preferences";
        SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        String hudType = preferences.getString(S.PREF_HUDTYPE_KEY, "2");

        String uvstring = UVindeksProvider.uvIndeks(6); // regionen er pt hardcodet til 6
        if (uvstring==null) {
          textview.setText("\n\n\nKunne ikke hente UV-indekset. Har du netforbindelse?");
          setContentView(textview);
          return;
        }
        
        textview.setText("\n\n\nDagens UV indeks: " + UVindeksProvider.uvDato() + "\n\n");
        textview.append("Dagens UV-Indeks for region Hovedstaden er "+ uvstring +"\n\n");

        textview.append("Din hudtype er angivet til: " + hudType +"\n\n");

        int hudTypeInt = Integer.parseInt(hudType);

        int i = uvstring.lastIndexOf("-");
        if(i>0)
            uvstring = uvstring.substring(i+1);
        float uvindexpræcist = Float.valueOf(uvstring);

        int uvindex = Math.round(uvindexpræcist);
        if(uvindex==0)
            uvindex=1;
        int uvTid = UVTal.LæsUVTid(uvindex,hudTypeInt*2);

        textview.append("Det betyder at du skal huske solcremen, hvis du regner med at være i solen i mere end " + String.valueOf(uvTid) + " minutter\n");
        setContentView(textview);

        Button button = new Button(this);
        button.setText("Læs mere om UV-Indeks");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebURL("http://www.dmi.dk/dmi/index/danmark/solvarsel.htm");
            }
        });
        
//        addContentView(button, );

        try
        {
            InputStream is = (InputStream) new URL("http://www.dmi.dk/dmi/" + UVindeksProvider.uvSymbol(6)).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            ImageView iv = new ImageView(this);
            iv.setImageDrawable(d);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ); 
            addContentView(iv, params);
        }catch (Exception e) {
            System.out.println("Exc="+e);
        }
    }

    public void openWebURL( String inURL ) {
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( inURL ) );
        startActivity( browse );
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
    }

    
}