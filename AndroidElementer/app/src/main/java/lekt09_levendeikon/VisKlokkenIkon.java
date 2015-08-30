package lekt09_levendeikon;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Arrays;
import java.util.Date;

import dk.nordfalk.android.elementer.R;
import lekt05_grafik.Tegneprogram;

public class VisKlokkenIkon extends AppWidgetProvider {

  private static final String TAG = "VisKlokkenIkon";

  /**
   * Hjælpemetode der opdaterer et antal ikoner på hjemmeskærmen
   *
   * @param ctx
   * @param appWidgetManager
   * @param appWidgetIds
   */
  private static void opdaterIkoner(Context ctx, AppWidgetManager appWidgetManager, final int[] appWidgetIds) {

    RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.lekt09_levendeikon_visklokken);

    remoteViews.setTextViewText(R.id.etTextView, "Klokken er:\n" + new Date());
    // Vis en tilfældig farve på TextViewet
    int farve = (int) System.currentTimeMillis() | 0xff0000ff;
    remoteViews.setTextColor(R.id.etTextView, farve);

    // generisk måde at gøre det samme på
    remoteViews.setInt(R.id.etTextView, "setTextColor", farve);

    // Lav et intent der skal affyres hvis knapppen trykkes
    Intent tegneIntent = new Intent(ctx, Tegneprogram.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, tegneIntent, 0);
    remoteViews.setOnClickPendingIntent(R.id.enKnap, pendingIntent);

    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
  }

  /**
   * Eksempel på hvordan man selv kan opdatere sine ikoner når man har brug for det
   */
  public static void opdaterIkoner(Context context) {

    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, VisKlokkenIkon.class));
    Log.d(TAG, "opdaterIkoner - der er følgende ikoner " + Arrays.asList(appWidgetIds));

    opdaterIkoner(context, appWidgetManager, appWidgetIds);
  }

  /**
   * Broadcastrecieverens onRecieve().
   * BØR NORMALT IKKE OMDEFINERES da det er den som superklassen AppWidgetProvider bruger til
   * at kalde de andre metoder
   */
  @Override
  public void onReceive(Context c, Intent intent) {
    super.onReceive(c, intent);
    Log.d(TAG, "onReceive:" + intent);
  }

  @Override
  public void onDeleted(Context context, int[] appWidgetIds) {
    Log.d(TAG, "onDeleted");
  }

  @Override
  public void onEnabled(Context context) {
    Log.d(TAG, "onEnabled");
  }

  @Override
  public void onDisabled(Context context) {
    Log.d(TAG, "onDisabled");
  }

  @Override
  public void onUpdate(final Context ctx, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {

    Log.d(TAG, "onUpdate " + Arrays.asList(appWidgetIds));

    opdaterIkoner(ctx, appWidgetManager, appWidgetIds);

    // Lad uret opdatere hvert sekund i det næste minut. Burde gøres fra en service...
    new Thread() {
      @Override
      public void run() {
        for (int i = 0; i < 60; i++) {
          SystemClock.sleep(1000);
          opdaterIkoner(ctx);
          /*
          remoteViews.setTextViewText(R.id.etTextView, "KL er:\n" + new Date());
					remoteViews.setTextColor(R.id.etTextView, i % 2 == 0 ? Color.RED : Color.GREEN);
					appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
					*/
        }
      }
    }.start();


  }


}
