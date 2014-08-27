
package dk.andreas.tabvejrny.dataklasser;

import dk.andreas.tabvejrny.MainActivity;
import dk.andreas.tabvejrny.dataklasser.Tab;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import dk.andreas.tabvejrny.Database;
import dk.andreas.tabvejrny.R;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class Tabs {

    private static ArrayList<Tab> al = new ArrayList();

    public static Resources res;


        private static ArrayList<Tab> ReadTabsFromFile() {
            ArrayList<Tab> nylist = new ArrayList();
            String TAG="ReadTabs";
            Log.d(TAG, "Start");
            try
            {

            Reader fil = new InputStreamReader(res.openRawResource(R.raw.tabs));
            Log.d(TAG, "reading");
            BufferedReader ind = new BufferedReader(fil);

            String inString;
            while((inString = ind.readLine()) != null)
            {
                String[] tabinfo = inString.split(",");

                Tab tab = new Tab(Integer.parseInt(tabinfo[0]),tabinfo[1],tabinfo[2], Boolean.parseBoolean(tabinfo[3]),Integer.parseInt(tabinfo[4]), Boolean.parseBoolean(tabinfo[5]));
                nylist.add(tab);
            }
            ind.close();
            fil.close();
            }
            catch(java.io.IOException exp)
            {
                Log.d(TAG, "Execption kaldt");
                exp.printStackTrace();
            }
            return nylist;
        }

        public static ArrayList<Tab> LæsAl(){
            return al;
        }

        public static void ResetAl(){
            al.clear();
            al = ReadTabsFromFile();
        }

        public static int LæsAlLængde() {
            return al.size();
        }

    public static void init(MainActivity aThis) {
        Database db = Database.hentInstans();
        al = db.ReadTabsFromDb();

            if (al.isEmpty()) {
//                Database db = new Database();
//                al = db.ReadTabsFromDb();
                al = ReadTabsFromFile();
                db.ResetDB();
            }

    }


}
