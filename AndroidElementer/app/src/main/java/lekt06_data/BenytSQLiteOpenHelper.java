/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt06_data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;


class KundeDb extends SQLiteOpenHelper {
  static final int VERSION = 2;
  static final String DATABASE = "database.db";
  static final String TABEL = "kunder";

  static final String ID = "_id";
  static final String NAVN = "navn";
  static final String KREDIT = "kredit";

  public KundeDb(Context context) {
    super(context, DATABASE, null, VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("create table " + TABEL + " ("
            + ID + " integer primary key, "
            + KREDIT + " text not null, " + NAVN + " text)");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("drop table " + TABEL);
    this.onCreate(db);
  }
}

/**
 * @author Jacob Nordfalk
 */
public class BenytSQLiteOpenHelper extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView textView = new TextView(this);
    textView.append("Herunder resultatet af en forespørgsel på en SQLite-database\n\n");
    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);


    // Oprettelse af database
    KundeDb kundeDb = new KundeDb(this);
    SQLiteDatabase db = kundeDb.getWritableDatabase();

    // Oprette en række
    ContentValues række = new ContentValues();
    række.put(KundeDb.NAVN, "Jacob Nordfalk");
    række.put(KundeDb.KREDIT, 500);
    db.insert(KundeDb.TABEL, null, række);

    db.execSQL("INSERT INTO kunder (navn, kredit) VALUES ('Troels Nordfalk', 400);");

    // Søgning
    //Cursor cursor = db.rawQuery("SELECT * from kunder WHERE kredit > 100 ORDER BY kredit ASC;", null);
    String[] kolonner = {KundeDb.ID, KundeDb.NAVN, KundeDb.KREDIT};
    String valg = "kredit > 100"; // WHERE
    String sortering = "kredit ASC"; // ORDER BY
    Cursor cursor = db.query(KundeDb.TABEL, kolonner, valg, null, null, null, sortering);

    while (cursor.moveToNext()) {
      long id = cursor.getLong(0);
      String navn = cursor.getString(1);
      int kredit = cursor.getInt(2);
      textView.append(id + "  " + navn + " " + kredit + "\n");
    }
    cursor.close();

    db.close();
  }
}
