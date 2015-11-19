/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt50_googlested;

import java.util.Arrays;


public class Log {

  public static void d(Object string) {
    d("", string);
  }

  public static void d(String TAG, Object string) {
    //TAG = Thread.currentThread().getName() + TAG;
    String s = String.valueOf(string);
    android.util.Log.d(TAG, s);
    logappend(s);
  }

  public static void i(Object string) {
    i("", string);
  }


  public static void i(String TAG, Object string) {
    TAG = Thread.currentThread().getName() + TAG;
    String s = String.valueOf(string);
    android.util.Log.i(TAG, s);
    logappend(s);
  }

  public static void e(String tekst, Exception ex) {
    ex.printStackTrace();
    android.util.Log.e(Thread.currentThread().getName(), tekst, ex);
    logappend(tekst);
    logappend(Arrays.toString(ex.getStackTrace()).replaceAll(", ", "\n"));
  }


  public static void rapporterFejl(Throwable e) {
    android.util.Log.e(Thread.currentThread().getName(), "fejl", e);
    //e.printStackTrace();
    logappend(e.toString());
    logappend(Arrays.toString(e.getStackTrace()).replaceAll(", ", "\n"));
  }

  public static void rapporterFejl(String e) {
    rapporterFejl(new Exception(e));
  }

  private static StringBuilder log = new StringBuilder(18000);

  /**
   * Føjer data til loggen.
   * Er loggen blevet for lang trimmes den.
   * Er synkroniseret da der enkelte gange er blevet set crashes fordi der blev skrevet
   * loggen samtidig med at den var ved at blive trimmet.
   * Af performancehensyn bør logning nok begrænses til kun at omfatte det vi som udviklere
   * tror vil afhjælpe en evt senere fejlfinding
   */
  private static synchronized void logappend(String s) {
    if (log.length() > 57500) {
      log.delete(0, 10000);
    }
    // Roterende log
    int n = s.length();
    if (n > 10000) n = 10000;
    log.append(s, 0, n);
    log.append('\n');
  }

  public static synchronized String getLog() {
    return log.toString();
  }

  public static synchronized String hentOgSletLog() {
    String res = log.toString();
    log  = new StringBuilder(18000);
    return res;
  }
}
