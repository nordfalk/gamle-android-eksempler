package dk.andreas.tabvejrny;

import dk.andreas.tabvejrny.dataklasser.Constants;
import dk.andreas.tabvejrny.dataklasser.Billeder;
import dk.andreas.tabvejrny.dataklasser.Billede;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import dk.andreas.tabvejrny.dataklasser.BilledeOpdateret.OnImageAddedListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class HentogGemBillede {
    private final String TAG = "HentogGemBillede";

    xxx skal bruge cache på sdkort!!
    
    
    public boolean HentBillede(Billede billede, OnImageAddedListener lytter) throws IOException {
        String[] params = new String[] {billede.LæsURL() , billede.LæsNavn()};

        // Slet evt. gammel kopi hvis cachetiden er overskredet
        File dir = new File(Constants.BILLEDDIRECTORY);
        File file = new File(dir+"/"+billede.LæsNavn());
        //final int MAXAGE = maxMillisekunder;

        if(file.exists()&&file.lastModified()+billede.LæsCacheTid()<System.currentTimeMillis()) {
            file.delete();
            Log.d(TAG,file.getName() + " var for gammel og er slettet");
        }

        // Tjeck om billedet allerede findes i ArrayListen
        if(!Billeder.FindesBillede(billede)) {
            // Prøv først at hente billedet lokalt
            try {
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(Constants.BILLEDDIRECTORY +"/"+ billede.LæsNavn());
                // Hvis filen ikke findes lokalt, hentes den i baggrunden
                if(bitmap==null) {
                    Log.d("HentogGem", billede.LæsNavn() + " Findes ikke lokalt");
                    new DownloadImageTask(lytter).execute(billede);
                }
                else {
                    Log.d("HentogGem", billede.LæsNavn() + " Er hentet lokalt");
                    Log.d("HentogGem", billede.LæsNavn() + " Højde: " + String.valueOf(bitmap.getHeight()));
                    billede.SætBillede(bitmap);
                    Billeder.TilføjBillede(billede);
                    if(lytter!=null){
                        lytter.onImageAdded(billede);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.d("HentogGem","Exception");
            }
        }
        return true;
    }

    public boolean SletGamleBilleder(int maxMillisekunder) {
        File dir = new File(Constants.BILLEDDIRECTORY);
        File[] files = dir.listFiles();
        //final int MAXAGE = maxMillisekunder;

        if(files!=null) {
            for (File f : files ) {
               Long lastmodified = f.lastModified();
               if(lastmodified+maxMillisekunder<System.currentTimeMillis()) {
                    f.delete();
                    Log.d(TAG,f.getName() + " var for gammel og er slettet");
               }
            }
        }
        return true;
    }

    private class DownloadImageTask extends AsyncTask<Billede,Void,Bitmap> {

        //ImageView tempImageView = null;
        Billede tmpbillede = null;
        OnImageAddedListener lytter;

        private DownloadImageTask(OnImageAddedListener lytter) {
            this.lytter = lytter;
        }

        @Override
        protected Bitmap doInBackground(Billede... billeder) {
            this.tmpbillede = billeder[0];
            return download_Image(tmpbillede.LæsURL());
        }

        protected void onPostExecute(Bitmap result) {
            if (result!=null) {
              // Gem som filnavn
                //tempImageView.setImageBitmap(result);
                tmpbillede.SætBillede(result);
                Billeder.TilføjBillede(tmpbillede);
                Billeder.GemBillede(tmpbillede);
                if(lytter!=null) {
                    lytter.onImageAdded(tmpbillede);
                }
            }
            else {
              //ptv.setText("Billede kan ikke hentes");
                if(lytter!=null){
                    lytter.onCouldNotDownload(tmpbillede);
                }
            }
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
