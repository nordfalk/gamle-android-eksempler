package dk.andreas.tabvejrny.dataklasser;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Billeder {

    private static ArrayList<Billede> al = new ArrayList();
    public static Resources res;


        public static ArrayList<Billede> LæsAl(){
            return al;
        }

        public static ArrayList<String> LæsBilledNavne(int type){
            ArrayList<String> tmpal = new ArrayList();
            for(Billede tmpBillede: al){
                if(tmpBillede.LæsType() == type) {
                    tmpal.add(tmpBillede.LæsNavn());
                }
            }
            String[] tmpar = new String[tmpal.size()];
            int i=0;
            for (String tmpnavn: tmpal) {
                tmpar[i]=tmpnavn;
                i++;
            }
            Arrays.sort(tmpar);
            tmpal.clear();
            for(int j =0;j<tmpar.length;j++) {
                tmpal.add(tmpar[j]);
            }
            return tmpal;
        }

        public static void SletAl(){
            for(Billede b:al){
                b.frigiv();
            }
            al.clear();
        }

        public static int LæsAlLængde() {
            return al.size();
        }

        public static void TilføjBillede(Billede billede) {
            boolean billedeFindes=false;
            for(Billede tmpBillede: al){
                if(tmpBillede.LæsNavn() == null ? billede.LæsNavn() == null : tmpBillede.LæsNavn().equals(billede.LæsNavn())) {
                    billedeFindes = true;
                    Log.d("Billeder", billede.LæsNavn() + " Findes allerede." + " - "+String.valueOf(al.size()) +" i alt");
                }
            }
            if(!billedeFindes){
                al.add(billede);
                Log.d("Billeder", billede.LæsNavn() + " Tilføjet." + " - "+String.valueOf(al.size()) +" i alt");
            }

            BilledeOpdateret bo = new BilledeOpdateret();
            bo.BilledHåndtering(billede);

        }

        public static boolean FindesBillede(Billede billede) {
            boolean billedeFindes=false;
            for(Billede tmpBillede: al){
                if(tmpBillede.LæsNavn() == null ? billede.LæsNavn() == null : tmpBillede.LæsNavn().equals(billede.LæsNavn())) {
                    billedeFindes = true;
                    Log.d("Billeder", billede.LæsNavn() + " Billedet findes allerede." + " - "+String.valueOf(al.size()) +" i alt");
                }
            }
            return billedeFindes;
        }

        public static Bitmap LæsBillede(String billednavn) {
            Bitmap returbillede = null;
            for(Billede tmpBillede: al){
                if(tmpBillede.LæsNavn().equals(billednavn)) {
                    returbillede= tmpBillede.LæsBillede();
                }
            }
            return returbillede;
        }

        public static void GemBillede(Billede billede) {
            try {
                   File wallpaperDirectory = new File(Constants.BILLEDDIRECTORY);
                   wallpaperDirectory.mkdirs();

                   File file = new File(Constants.BILLEDDIRECTORY, billede.LæsNavn());
                   FileOutputStream out = new FileOutputStream(file);
                   billede.LæsBillede().compress(Bitmap.CompressFormat.JPEG, 100, out);
                   Log.d("Billeder", billede.LæsNavn() +  " Gemt");
            } catch (Exception e) {
                   e.printStackTrace();
            }
        }

}
