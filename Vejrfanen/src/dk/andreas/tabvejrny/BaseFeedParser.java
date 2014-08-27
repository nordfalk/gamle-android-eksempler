package dk.andreas.tabvejrny;

import android.app.AlertDialog;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public abstract class BaseFeedParser implements FeedParser {

    // names of the XML tags
    static final String CHANNEL = "channel";
    static final String PUB_DATE = "pubDate";
    static final  String DESCRIPTION = "description";
    static final  String LINK = "link";
    static final  String TITLE = "title";
    static final  String ITEM = "item";

    final URL feedUrl;

    protected BaseFeedParser(String feedUrl){
        Log.i("BaseFeedParser","beforeTry");
        try {
            Log.i("BaseFeedParser","beforeTry2");
            this.feedUrl = new URL(feedUrl);
            Log.i("BaseFeedParser","afterTry");
        } catch (MalformedURLException e) {
            Log.i("BaseFeedParser","throwExcep");
            throw new RuntimeException(e);
        }
    }

    protected InputStream getInputStream() {
        /*
         try {
              InetAddress i = InetAddress.getByName(feedUrl.toString());
            } catch (UnknownHostException e1) {
              e1.printStackTrace();
            }

          Log.i("InputStream", "After Stacktrace");
        */

        try {
          Log.i("InputStream","beforeTry");



            return feedUrl.openConnection().getInputStream();
        } catch (IOException e) {
            Log.i("InputStream","IOExeption");


//*******************
//            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
//            alertbox.setMessage("HostName not resolved");
//            alertbox.show();
//*******************'
            

            throw new RuntimeException(e);
        }
    }
}


