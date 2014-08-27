package dk.andreas.tabvejrny;

import android.os.AsyncTask;
import android.util.Log;
import dk.andreas.tabvejrny.dataklasser.Billede;
import dk.andreas.tabvejrny.dataklasser.BilledeOpdateret.OnImageAddedListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONTokener;

public class HentAnimationsBilleder {

    private final String TAG = "HentAnimationsBilleder";

    public void HentAnimationsBilleder(String jsonURL, String billedURL, OnImageAddedListener lytter) {
        String[] urls = new String[] {jsonURL,billedURL};
        new DownloadJSONTask(lytter).execute(urls);
    }

    private class DownloadJSONTask extends AsyncTask<String,Void,org.json.JSONArray> {

        //ImageView tempImageView = null;
        String jsonurl = null;
        String billedurl = null;
        OnImageAddedListener lytter;

        private DownloadJSONTask(OnImageAddedListener lytter) {
            this.lytter = lytter;
        }

        @Override
        protected org.json.JSONArray doInBackground(String... url) {
            this.jsonurl = url[0];
            this.billedurl = url[1];
            return download_JSON(jsonurl);
        }

        protected void onPostExecute(org.json.JSONArray result) {
            if (result!=null) {
                HentogGemBillede hg = new HentogGemBillede();
                try {
                    for (int i=0;i<result.length();i++) {
                        hg.HentBillede(new Billede(result.getString(i), "http://dmi.netrum.dk/billeder/" + result.getString(i), 4, 18000000), lytter);
                    }
                }catch(Exception ex){
                    Log.d(TAG,"Filnavne kan ikke hentes");
                }
            }
        }

        private org.json.JSONArray download_JSON(String url) {
            //---------------------------------------------------
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            try {
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();
                JSONTokener tokener = new JSONTokener(json);
                JSONArray finalResult = new JSONArray(tokener);
                return finalResult;
            }catch(Exception ex){
                Log.d(TAG,"Filnavne kan ikke hentes");
                return null;
            }
            //---------------------------------------------------
        }

    }
}
