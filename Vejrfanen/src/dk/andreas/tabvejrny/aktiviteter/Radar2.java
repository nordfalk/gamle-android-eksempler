package dk.andreas.tabvejrny.aktiviteter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import dk.andreas.tabvejrny.R;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Radar2 extends Activity implements View.OnClickListener  {

    ImageView riv;
//    VideoView rvv;
    TextView rtv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radar2);

        rtv = (TextView)findViewById(R.id.RadarTextView);
        riv = (ImageView)findViewById(R.id.RadarImageView);
        Button radarKnap = (Button)findViewById(R.id.radar_radar);
        radarKnap.setOnClickListener(this);
        Button nedbørKnap = (Button)findViewById(R.id.radar_nedbørstype);
        nedbørKnap.setOnClickListener(this);
        riv.setOnClickListener(this);

        String prognosetext = new String();
        prognosetext += "Radar";
        rtv.setText(prognosetext);

        String URL = "http://www.dmi.dk/dmi/radaranim2.gif";
        String URL2 = "http://www.dmi.dk/dmi/200611090930_prectypefdk.jpg";

        riv.setTag(URL);
        riv.setAdjustViewBounds(true);


        new DownloadImageTask().execute(riv);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radar_radar:
                String URL = "http://www.dmi.dk/dmi/radaranim2.gif";
                riv.setTag(URL);
                riv.setAdjustViewBounds(true);
                new DownloadImageTask().execute(riv);
                break;
            case R.id.radar_nedbørstype:
                String URL2 = "http://www.dmi.dk/dmi/200611090930_prectypefdk.jpg";
                riv.setTag(URL2);
                riv.setAdjustViewBounds(true);
                new DownloadImageTask().execute(riv);
                break;
            case R.id.RadarImageView:
                riv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                break;
        }
    }

    private class DownloadImageTask extends AsyncTask<ImageView,Void,Bitmap> {

        ImageView tempImageView = null;

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.tempImageView = imageViews[0];
            return download_Image((String)tempImageView.getTag());
        }

        protected void onPostExecute(Bitmap result) {
            if (result!=null)
              tempImageView.setImageBitmap(result);
            else
              rtv.setText("Billede kan ikke hentes");
        }

        private Bitmap download_Image(String url) {
            //---------------------------------------------------
            Bitmap bm = null;
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize = 8;
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e("Hub","Error getting the image from server : " + e.getMessage().toString());
            }
            return bm;
            //---------------------------------------------------
        }

    }
}
