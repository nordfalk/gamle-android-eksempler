package dk.firma.mitprojekt.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;

/**
 * Loggerklasse
 * - hvor man slipper for at angive tag
 * - man kan logge objekter (få kaldt toString)
 * - cirkulær buffer tillader at man kan gemme loggen til fejlrapportering
 * @author j
 */
public class Log {
  public static final String TAG = "MinApp";

  // Fjernet da det ser ud til at overbelaste regnearket
  public static final boolean RAPPORTER_VELLYKKET_AFSPILNING = false;

  public static StringBuilder log = new StringBuilder(18000);

  private static void logappend(String s) {
    // Roterende log
    log.append(s);
    log.append('\n');
    if (log.length()>17500) log.delete(0, 7000);
  }

  /** Logfunktion uden TAG som tager et objekt. Sparer bytekode og tid */
  public static void d(Object o) {
    String s = String.valueOf(o);
    android.util.Log.d(TAG, s);
    logappend(s);
  }

  public static void e(Exception e) {
    e("fejl", e);
  }

  public static void e(String tekst, Exception e) {
    android.util.Log.e(TAG, tekst, e);
    //e.printStackTrace();
    logappend(android.util.Log.getStackTraceString(e));
  }


  public static void kritiskFejl(final Activity akt, final Exception e) {
    //org.acra.ErrorReporter.getInstance().handleSilentException(e);
    Log.e(e);

    Builder ab=new AlertDialog.Builder(akt);
    ab.setTitle("Beklager, der skete en fejl");
    ab.setMessage(e.toString());
    ab.setNegativeButton("Fortsæt", null);
    ab.setPositiveButton("Indsend fejl", new Dialog.OnClickListener() {
      public void onClick(DialogInterface arg0, int arg1) {
        String brødtekst = "Skriv, hvad der skete:\n\n\n---\n";
        brødtekst += "\nFejlspor;\n"+android.util.Log.getStackTraceString(e);
        brødtekst += "\n\n" + Diverse.lavTelefoninfo(akt);
        brødtekst += "\n\n" + Log.log;
        Diverse.kontakt(akt, "Fejlrapport", brødtekst);
      }

    });
    ab.create().show();
  }
}
