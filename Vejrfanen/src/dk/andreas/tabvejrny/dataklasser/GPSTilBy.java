package dk.andreas.tabvejrny.dataklasser;

import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import dk.andreas.tabvejrny.R;
import dk.andreas.tabvejrny.SortByDistance;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GPSTilBy {

    private static ArrayList<GPSBy> al = new ArrayList();
    private static double localLat, localLon;

    public static Resources res;
    private static Location currentLocation;

    private static void ReadGPSFromFile() {
        String TAG="ReadGPS";
        Log.d(TAG, "Start ReadGPS");
        try 
        {

//            FileReader fil = new FileReader("Resources/raw/gpsdata.txt");
        Reader fil = new InputStreamReader(res.openRawResource(R.raw.gpsdata));//new FileReader(R.raw.gpsdata);
        BufferedReader ind = new BufferedReader(fil);

        String inString;
        al.clear();
        while((inString = ind.readLine()) != null)
        {
            String[] gpsinfo = inString.split(",");

            GPSBy gpsby = new GPSBy(gpsinfo[0],gpsinfo[1],Double.parseDouble(gpsinfo[2]),Double.parseDouble(gpsinfo[3]));
            al.add(gpsby);
        }
        ind.close();
        fil.close();
        }
        catch(java.io.IOException exp)
        {
            Log.d(TAG, "Execption kaldt");
            exp.printStackTrace();
        }
    }

    private static void FindClosest(double latin, double lonin) {
        String TAG="Find Closest";
        Log.d(TAG, "start Find Closest");
        for(GPSBy gpsby : al){
            Double x = 69.1 * (gpsby.Læslat() - latin);
            Double y = 69.1 * (gpsby.Læslon() - lonin) * Math.cos(latin/57.3);
            Double dist = 1.609344 * Math.sqrt(x * x + y * y);
            gpsby.SætAfstand(dist);
        }
        Comparator sammenligner = new SortByDistance();
        Collections.sort(al, sammenligner);
    }

    public static ArrayList<GPSBy> ReturnClosest(double lat, double lon, int antal) {
        if (al.isEmpty() || localLat!=lat || localLon!=lon)
        {
            localLat = lat;
            localLon = lon;
            ReadGPSFromFile();
            FindClosest(lat, lon);
        }
        ArrayList<GPSBy> tmpal = new ArrayList();
        for (int i=0;i<antal;i++)
        {
            tmpal.add(al.get(i));
        }
        return tmpal;
    }

    public static ArrayList<GPSBy> ReturnByName() {
        ReadGPSFromFile();
        return al;
    }
}


