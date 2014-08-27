package dk.andreas.tabvejrny.widget;

import java.text.SimpleDateFormat;

import android.appwidget.AppWidgetManager;

import android.appwidget.AppWidgetProvider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;
import dk.andreas.tabvejrny.R;
import dk.andreas.tabvejrny.dataklasser.UVindeksProvider;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Widget extends AppWidgetProvider {
   // ...

   // Define the format string for the date
   private SimpleDateFormat formatter = new SimpleDateFormat(
         "EEEEEEEEE\nd MMM yyyy");

   @Override
   public void onUpdate(Context context,
      AppWidgetManager appWidgetManager, int[] appWidgetIds) {
      // Retrieve and format the current date
      String indeksdag = UVindeksProvider.uvDato();

      BitmapFactory.Options bmOptions;
      bmOptions = new BitmapFactory.Options();
      bmOptions.inSampleSize = 1;
      Bitmap bm = LoadImage("http://www.dmi.dk/dmi/" + UVindeksProvider.uvSymbol(6), bmOptions);

      // Change the text in the widget
      RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
      updateViews.setTextViewText(R.id.widgettext, indeksdag + "\nUV-Indeks: " + UVindeksProvider.uvIndeks(6));
      updateViews.setImageViewBitmap(R.id.widgetimage, bm);
      appWidgetManager.updateAppWidget(appWidgetIds, updateViews);

      // Not really necessary, just a habit
      super.onUpdate(context, appWidgetManager, appWidgetIds);
   }

    private Bitmap LoadImage(String URL, BitmapFactory.Options options)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e1) {
        }
        return bitmap;
    }

private InputStream OpenHttpConnection(String strURL) throws IOException{
    InputStream inputStream = null;
    URL url = new URL(strURL);
    URLConnection conn = url.openConnection();
    try{
        HttpURLConnection httpConn = (HttpURLConnection)conn;
        httpConn.setRequestMethod("GET");
        httpConn.connect();
        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            inputStream = httpConn.getInputStream();
        }
    } catch (Exception ex) {
    }
    return inputStream;}
}
