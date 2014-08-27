/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt06_youtube;

import android.os.SystemClock;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * @author Jacob Nordfalk
 */
public class FilCache {

  private static final int BUFFERSTR = 4 * 1024;
  private static String lagerDir;
  public static int byteHentetOverNetværk = 0;

  private static void log(String tekst) {
    Log.d("Cache", tekst);
  }

  public static void init(File dir) {
    if (lagerDir != null) {
      return; // vi skifter ikke lager midt i det hele
    }
    lagerDir = dir.getPath();
    dir.mkdirs();
    try { // skjul lyd og billeder for MP3-afspillere o.lign.
      new File(dir, ".nomedia").createNewFile();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  static long nu = System.currentTimeMillis();

  public static String hentFil(String url, boolean ændrerSigIkke) throws IOException {
    String cacheFilnavn = findLokaltFilnavn(url);
    File cacheFil = new File(cacheFilnavn);

    log("cacheFil lastModified " + new Date(cacheFil.lastModified()) + " for " + url);

    if (cacheFil.exists() && ændrerSigIkke) {
      log("Læser " + cacheFilnavn);
      return cacheFilnavn;
    } else {
      log("Kontakter " + url);
      long hentHvisNyereEnd = cacheFil.lastModified();

      int prøvIgen = 3;
      while (prøvIgen > 0) {
        HttpURLConnection httpForb = (HttpURLConnection) new URL(url).openConnection();

        if (cacheFil.exists()) {
          httpForb.setIfModifiedSince(hentHvisNyereEnd);
        }

        httpForb.setConnectTimeout(10000); // 10 sekunder
        try {
          httpForb.connect();
        } catch (IOException e) {
          if (!cacheFil.exists()) {
            throw e; // netværksfejl - og vi har ikke en lokal kopi
          }
          log("Netværksfejl, men der er cachet kopi i " + cacheFilnavn);
          return cacheFilnavn;
        }
        int responseCode = httpForb.getResponseCode();
        if (responseCode == 400 && cacheFil.exists()) {
          httpForb.disconnect();
          log("Netværksfejl, men der er cachet kopi i " + cacheFilnavn);
          return cacheFilnavn;
        }
        if (responseCode == 304) {
          httpForb.disconnect();
          log("Der er cachet kopi i " + cacheFilnavn);
          return cacheFilnavn;
        }
        if (responseCode != 200) {
          if (prøvIgen == 0)
            throw new IOException(responseCode + " " + httpForb.getResponseMessage() + " for " + url);
          // Prøv igen
          log("Netværksfejl, vi venter lidt og prøver igen");
          log(responseCode + " " + httpForb.getResponseMessage() + " for " + url);
          SystemClock.sleep(100);
          continue;
        }

        log("Henter " + url + " og gemmer i " + cacheFilnavn);
        boolean ok = false;
        try {
          InputStream is = httpForb.getInputStream();
          FileOutputStream fos = new FileOutputStream(cacheFilnavn);
          kopierOgLuk(is, fos);
          ok = true;
        } finally {
          if (!ok) {
            cacheFil.delete(); // gem ikke halve filer!
          }
        }

        long lastModified = httpForb.getHeaderFieldDate("last-modified", nu);
        log("last-modified" + new Date(lastModified));
        cacheFil.setLastModified(lastModified);

        return cacheFilnavn;
      }
    }
    throw new IllegalStateException("Dette burde aldrig ske!");
  }

  public static String findLokaltFilnavn(String url) {
    //String cacheFilnavn = url.substring(url.lastIndexOf('/') + 1).replace('?', '_').replace('/', '_').replace('&', '_'); // f.eks.  byvejr_dag1?by=2500&mode=long
    String cacheFilnavn = url.replace('?', '_').replace('/', '_').replace('&', '_'); // f.eks.  byvejr_dag1?by=2500&mode=long
    cacheFilnavn = lagerDir + "/" + cacheFilnavn;
    log("URL: " + url + "  -> " + cacheFilnavn);
    return cacheFilnavn;
  }

  private static void kopierOgLuk(InputStream in, OutputStream out) throws IOException {
    try {
      byte[] b = new byte[BUFFERSTR];
      int read;
      while ((read = in.read(b)) != -1) {
        out.write(b, 0, read);
        byteHentetOverNetværk += read;
      }
    } finally {
      luk(in);
      luk(out);
    }
  }

  public static void luk(Closeable stream) {
    if (stream != null) {
      try {
        stream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  /*
  public static String læsStrengOgLuk(Reader r) throws IOException {
	BufferedReader br = new BufferedReader(r, 8 * 1024);
	StringBuilder sb = new StringBuilder();

	String line = null;
	try {
	while ((line = br.readLine()) != null) {
	sb.append(line + "\n");
	}
	} finally {
	br.close();
	}

	return sb.toString();
	}
	 */
}
