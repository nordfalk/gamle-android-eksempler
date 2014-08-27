package dk.firma.mitprojekt.util;

import dk.firma.mitprojekt.logik.Stamdata;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonIndlaesning {

  /**
   * Henter stamdata (faste data)
   * @throws IOException hvis der er et problem med netværk
   * eller parsning (dvs interne fejl af forskellig art som bør rapporteres til udvikler)
   */
	public static Stamdata parseStamdata(String str) throws JSONException
  {
    Log.d("\n=============Forsøger at parse\n"+str+"\n==================");

    Stamdata d = new Stamdata();
    JSONObject json = d.json=new JSONObject(str);

    d.chatUrl = json.getString("chatUrl");
    d.kanalkoder = jsonArrayTilArrayListString(json.getJSONArray("kanalkoder"));
    Log.d("\n=============Parsning gik godt, vi fik bl.a. \n"+d.kanalkoder+"\n==================");

    return d;
	}



  private static HttpClient httpClient;

  public static String hentUrlSomStreng(String url) throws IOException {
    // AndroidHttpClient er først defineret fra Android 2.2
    //if (httpClient == null) httpClient = android.net.http.AndroidHttpClient.newInstance("Android DRRadio");
    if (httpClient == null) {
      HttpParams params = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout(params, 15 * 1000);
      HttpConnectionParams.setSoTimeout(params, 15 * 1000);
      HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
      HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
      //HttpProtocolParams.setUseExpectContinue(params, true);
      HttpProtocolParams.setUserAgent(params, "Android MinApp/1.x");
      httpClient = new DefaultHttpClient(params);
    }
    //dt("");
    Log.d("Henter "+url);
    //Log.e(new Exception("Henter "+url));
    //InputStream is = new URL(url).openStream();

    HttpGet c = new HttpGet(url);
    HttpResponse response = httpClient.execute(c);
    InputStream is = response.getEntity().getContent();

    String jsondata = læsInputStreamSomStreng(is);
    //Log.d("Hentede "+url+" på "+dt("hente "+url));
    return jsondata;
  }


  public static String læsInputStreamSomStreng(InputStream is) throws IOException, UnsupportedEncodingException {

    final char[] buffer = new char[0x3000];
    StringBuilder out = new StringBuilder();
    Reader in = new InputStreamReader(is, "UTF-8");
    int read;
    do {
      read = in.read(buffer, 0, buffer.length);
      if (read>0) {
        out.append(buffer, 0, read);
      }
    } while (read>=0);
    in.close();
    String jsondata = out.toString();
    return jsondata;
  }



  public static ArrayList<String> jsonArrayTilArrayListString(JSONArray j) throws JSONException {
    int n = j.length();
    ArrayList<String> res = new ArrayList<String>(n);
    for (int i=0; i<n; i++) {
      res.add(j.getString(i));
    }
    return res;
  }

}
