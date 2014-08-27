package dk.andreas.tabvejrny.aktiviteter;

import dk.andreas.tabvejrny.dataklasser.Billede;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import dk.andreas.tabvejrny.HentAnimationsBilleder;
import dk.andreas.tabvejrny.HentogGemBillede;
import dk.andreas.tabvejrny.R;
import dk.andreas.tabvejrny.dataklasser.BilledeOpdateret.OnImageAddedListener;
import dk.andreas.tabvejrny.dataklasser.Billeder;
import dk.andreas.tabvejrny.dataklasser.Constants;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Radar extends Activity implements AdapterView.OnItemSelectedListener , ViewSwitcher.ViewFactory, OnImageAddedListener {
    String TAG="Radar";
    private final Handler handler = new Handler();
    private Gallery g;
    private ImageView precTypeView;
    private int picPosition;
    private boolean animationKører = true;
    private boolean animationRunnableKører = false;
    private float x;
    private Context context;
    private ArrayList<String> mBilledNavne;
    private ArrayList<String> mThumbNavne;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        HentAnimationsBilleder hab = new HentAnimationsBilleder();
        hab.HentAnimationsBilleder("http://dmi.netrum.dk/filnavne.aspx", "http://dmi.netrum.dk/billeder/", this);

        Log.d(TAG,"onCreate");

        context=this;
        mBilledNavne = Billeder.LæsBilledNavne(4);
        mThumbNavne = Billeder.LæsBilledNavne(4);

        setContentView(R.layout.radar);

        mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
        mSwitcher.setFactory(this);

        g = (Gallery) findViewById(R.id.gallery);
        g.setAdapter(new ImageAdapter(this));
        g.setOnItemSelectedListener(this);

        precTypeView = (ImageView) findViewById(R.id.PrecTypeLegendImageView);
        precTypeView.setAdjustViewBounds(true);

        HentogGemBillede hg = new HentogGemBillede();
        try {
            hg.HentBillede(new Billede("precTypeLegend", "http://www.dmi.dk/dmi/prectypelegend.gif",5,86400000), this);
        } catch (IOException ex) {
            Logger.getLogger(Radar.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void onItemSelected(AdapterView parent, View v, int position, long id) {

        File file = new File(Constants.BILLEDDIRECTORY + "/"+ mBilledNavne.get(position));
        Uri uri = Uri.fromFile(file);
        mSwitcher.setImageURI(uri);
//        Ovenstående kode virker fint i Android 2.2, men skal tilsyneladende modificeres for at fungere i 1.6 - Noget med
//        File imageFileDir = new File(Constants.BILLEDDIRECTORY);
//        Uri imageUri = Uri.parse(imageFileDir.listFiles()[position].getAbsolutePath());
//        mSwitcher.setImageURI(imageUri);
//        BitmapDrawable bm =  Billeder.LæsBillede(mThumbNavne.get(position));
//        mSwitcher.setImageDrawable(bm);
    }


    public void onNothingSelected(AdapterView parent) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            x = event.getX();
        }
        if(event.getAction()==MotionEvent.ACTION_CANCEL||event.getAction()==MotionEvent.ACTION_UP)
            animationKører=true;
        else
            animationKører=false;
        float xDist = x-event.getX();
        if(xDist>25) {
            Log.d(TAG,"X-Dist: " + String.valueOf(xDist));
            picPosition = g.getSelectedItemPosition() -1;
            if (picPosition <=  0) {
                picPosition =  0;
            }
            g.setSelection(picPosition);
            x = event.getX();
        }
        if(xDist<-25) {
            Log.d(TAG,"X-Dist: " + String.valueOf(xDist));
            picPosition = g.getSelectedItemPosition() +1;
            if (picPosition >=  mBilledNavne.size()) {
                picPosition =  mBilledNavne.size()-1;
            }
            g.setSelection(picPosition);
            x = event.getX();
        }
        return true;

    }
    
    public View makeView() {
        ImageView i = new ImageView(this);
        i.setBackgroundColor(0xFF000000);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        return i;
    }

    private ImageSwitcher mSwitcher;

    public void onImageAdded(Billede b) {
        Log.d(TAG,"onImageAdded");
        switch (b.LæsType()) {
            case 5: precTypeView.setImageBitmap(b.LæsBillede());
        }
    }

    public void onCouldNotDownload(Billede tmpbillede) {
        Log.d(TAG,"onCouldNotDownload");
    }

    public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mBilledNavne.size();
            //return mThumbIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);

            //i.setImageResource(mThumbIds[position]);  //**************
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 8;
            //Bitmap bm = BitmapFactory.decodeFile(Constants.BILLEDDIRECTORY + "/" + mThumbNavne.get(position));
            Log.d(TAG, "Position: " + String.valueOf(position));
            Bitmap bm = Billeder.LæsBillede(mThumbNavne.get(position));
            i.setImageBitmap(bm);

            i.setAdjustViewBounds(true);
            i.setLayoutParams(new Gallery.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            i.setBackgroundResource(R.drawable.picture_frame);
            return i;
        }

        private Context mContext;
        private ArrayList<Billede> billedliste;

    }

    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        animationKører=true;

        handler.postDelayed(new Runnable() {
            public void run() {
                Log.d(TAG,"runnable");
                boolean læstOK = læsBillederFraArray();
                if(!læstOK) {
                    Toast toast = Toast.makeText(context, "Henter billeder", Toast.LENGTH_SHORT);
                    //toast.show();
                    handler.postDelayed(this, 2000);
                }
            }
        },1000);

        if(!animationRunnableKører) {
            handler.postDelayed(new Runnable() {
                public void run() {
                    Log.d(TAG,"runnable");
                    slideShow();
                    int delay = 1000;
                    if (g.getSelectedItemPosition()+1>=mBilledNavne.size()) {
                        delay = 3000;
                    }
                    handler.postDelayed(this, delay);
                }
            },1000);
            animationRunnableKører=true;
        }
    }

    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
        animationKører=false;
    }

    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private void slideShow() {
        Log.d(TAG,"slideShow: " + String.valueOf(g.getSelectedItemPosition()) + " af " + String.valueOf(mBilledNavne.size()) + " Animationen kører: " + String.valueOf(animationKører));
        if(animationKører){
            picPosition = g.getSelectedItemPosition() +1;
            if (picPosition >=  mBilledNavne.size()) {
                picPosition =  0; //loop
            }
            g.setSelection(picPosition);//vis næste billede
        }
    }

    private boolean læsBillederFraArray() {
        mBilledNavne = Billeder.LæsBilledNavne(4);
        mThumbNavne = Billeder.LæsBilledNavne(4);
        g.setAdapter(new ImageAdapter(this));
        Log.d(TAG, "læBillederFraArray: " + String.valueOf(mBilledNavne.size()) + " af " + String.valueOf(Constants.ANTALRADARBILLEDER));
        if(mBilledNavne.size()<Constants.ANTALRADARBILLEDER)
            return false;
        else
            return true;
    }

}







