package dk.andreas.tabvejrny.dataklasser;

import android.content.res.Resources;
import android.util.Log;
import dk.andreas.tabvejrny.R;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class UVTal {

    private static int[][] uvArray = new int[7][16];
    private static String TAG="UVTal";

    public static Resources res;

    private static int[][] readUVTalFromFile() {
        int[][] tmpArray = new int[7][16];
        try
        {
        Reader fil = new InputStreamReader(res.openRawResource(R.raw.uvtal));
        Log.d(TAG, "reading");
        BufferedReader ind = new BufferedReader(fil);

        String inString;
        int i = 0;
        while((inString = ind.readLine()) != null)
        {
            String[] uvtal = inString.split(",");
            for (int j=0;j<uvtal.length-1;j++) {
                tmpArray[i][j] = Integer.parseInt(uvtal[j]);
            }
            i++;
        }
        ind.close();
        fil.close();
        }
        catch(java.io.IOException exp)
        {
            Log.d(TAG, "Execption kaldt");
            exp.printStackTrace();
        }
        return tmpArray;
    }

    public static int LæsUVTid(int uvIndex, int pigmentProtectionFactor) {
        if(uvArray[0][0]!=100){
            uvArray = readUVTalFromFile();
        }
        return uvArray[uvIndex][pigmentProtectionFactor];
    }
}
