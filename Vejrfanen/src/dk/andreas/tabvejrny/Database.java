
package dk.andreas.tabvejrny;

import dk.andreas.tabvejrny.dataklasser.Constants;
import dk.andreas.tabvejrny.dataklasser.Tabs;
import dk.andreas.tabvejrny.dataklasser.Tab;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;

 public class Database extends SQLiteOpenHelper {
   private static final String DATABASE_NAME = Constants.DATABASE_NAME;
   private static final int DATABASE_VERSION = 1;
   private static final String TAG = "Database";
   private static Database instans;

   /** Create a helper object for the Events database */
   private Database(Context ctx) {
      super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
   }

   public static void init(Context ctx) {
     if (instans == null) {
         instans = new Database(ctx);
     }
   }

   public static Database hentInstans() {
       if (instans==null) throw new IllegalStateException("Database.init() er ikke blevet kaldt endnu");
       return instans;
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
       Log.d(TAG, "onCreate");

       final String CREATE_TABLE_TABS_DEFAULT =
        "CREATE TABLE IF NOT EXISTS "+ Constants.TABLE_NAME_DEFAULT +" ("
        + Constants.ID + " INTEGER,"
        + Constants.TAB_NAVN + " TEXT NOT NULL,"
        + Constants.TAB_KLASSE + " TEXT NOT NULL,"
        + Constants.TAB_AKTIV + " BOOLEAN,"
        + Constants.TAB_PRIORITET + " INTEGER, "
        + Constants.TAB_PRELOAD + " BOOLEAN"
        + ");";

        db.execSQL(CREATE_TABLE_TABS_DEFAULT);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion,
         int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
      onCreate(db);
   }

    public void CreateDB() {
    }
    
    public void ResetDB() {

        Log.d(TAG, "ResetDB");
        ArrayList<Tab> al =  Tabs.LæsAl();
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from " + Constants.TABLE_NAME_DEFAULT);

        ContentValues values = new ContentValues();
        for(Tab tab : al){
            values.put(Constants.ID,String.valueOf(tab.LæsId()));
            values.put(Constants.TAB_NAVN,tab.LæsNavn());
            values.put(Constants.TAB_KLASSE,tab.LæsKlasse());
            values.put(Constants.TAB_AKTIV,String.valueOf(tab.LæsAktiv()));
            values.put(Constants.TAB_PRIORITET,String.valueOf(tab.LæsPrioritet()));
            values.put(Constants.TAB_PRELOAD,String.valueOf(tab.LæsPreLoad()));
            db.insertOrThrow(Constants.TABLE_NAME_DEFAULT, null, values);
        }
        this.close();
    }

    public void OpdaterAktive(ArrayList<Tab> opdateredeTabs) {
        Log.d("Database", "OpdaterAktive");
        SQLiteDatabase db = getWritableDatabase();
        for (Tab tab : opdateredeTabs) {
            db.execSQL("update " + Constants.TABLE_NAME_DEFAULT + " set " + Constants.TAB_AKTIV + " = '"+ String.valueOf(tab.LæsAktiv()) +"' where "+ Constants.ID +"= " + String.valueOf(tab.LæsId()));
        }
        close();
    }

    public ArrayList<Tab> ReadTabsFromDb() {
        Log.d(TAG,"ReadTabsFromDB");
        ArrayList<Tab> al = new ArrayList();
        SQLiteDatabase db2 = getReadableDatabase();

        String[] FROM = { Constants.ID,Constants.TAB_NAVN,Constants.TAB_KLASSE,Constants.TAB_AKTIV,Constants.TAB_PRIORITET,Constants.TAB_PRELOAD};
        String ORDER_BY = Constants.TAB_PRIORITET + " ASC";
        Cursor cursor = db2.query(Constants.TABLE_NAME_DEFAULT, FROM, null, null, null, null, ORDER_BY);

        while (cursor.moveToNext()) {
            Tab tab = new Tab(cursor.getInt(0), cursor.getString(1), cursor.getString(2), Boolean.valueOf(cursor.getString(3)), cursor.getInt(4), Boolean.valueOf(cursor.getString(5)));
            al.add(tab);
        }
        cursor.close();
        close();
        return al;
    }

}
