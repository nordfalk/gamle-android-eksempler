package dk.andreas.tabvejrny.dataklasser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class UVindeksProvider {

    private static String uvDato, uvIndeks, uvSymbol;
    private static String[] uvIndeks2, uvSymbol2;
    private static String REGEX = ",";
  
    
    public static void initialiser() {
        String url, tag, webside;
        url = "http://www.dmi.dk/dmi/index/danmark/solvarsel.htm";
        tag = "header=";
        try {
            webside = getWebPage(url);
            String resultat = getSubString(tag, webside);
            resultat = resultat.substring(resultat.indexOf("'")+1, resultat.length()-1);
            uvDato = resultat;
            tag = "uvList=";
            String tmpIndeks = getSubString(tag, webside);
            uvIndeks = tmpIndeks;
            uvIndeks = uvIndeks.substring(8, uvIndeks.length()-1);
            uvIndeks = uvIndeks.replaceAll("'", "");
            Pattern p = Pattern.compile(REGEX);
            uvIndeks2 = p.split(uvIndeks);

            tag = "symbolList=";
            String tmpSymbol = getSubString(tag, webside);
            uvSymbol = tmpSymbol;
            uvSymbol = uvSymbol.substring(12, uvSymbol.length()-1);
            uvSymbol = uvSymbol.replaceAll("'", "");
            uvSymbol2 = p.split(uvSymbol);
        }
        catch(Exception ex)
        {
            uvDato = "fejl";
            webside="";
        }
    }

    public static String uvDato() {
        if (uvDato==null) {
            initialiser();
        }
        return uvDato;
    }
    
    public static String uvIndeks(int region) {
        if (uvIndeks==null) {
            initialiser();
        }
        if (uvIndeks==null) {
            return null; // ingen netforbindelse
        }
        return uvIndeks2[region];
    }

    public static String uvSymbol(int region) {
        if (uvSymbol==null) {
            initialiser();
        }
        if (uvSymbol==null) {
            return null; // ingen netforbindelse
        }
        return uvSymbol2[region];
    }


    private static String getWebPage(String urlString) throws Exception  {
      URL url; // The URL to read
      HttpURLConnection conn; // The actual connection to the web page
      BufferedReader rd; // Used to read results from the web page
      String line; // An individual line of the web page HTML
      StringBuilder result = new StringBuilder(); // A long string containing all the HTML
      try {
         url = new URL(urlString);
         conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("GET");
         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         while ((line = rd.readLine()) != null) {
            result.append(line);
         }
         rd.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
      return result.toString();
   }

    private static String getSubString(String tag, String htmlResponse) {
        String returnString="";
        int startpos, slutpos;
        startpos= htmlResponse.indexOf(tag);
        returnString = htmlResponse.substring(startpos, startpos+250);
        slutpos = returnString.indexOf(";");
        returnString=returnString.substring(0, slutpos);
        return returnString;
    }





}
