/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt08_providers;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Jacob Nordfalk
 */
public class VisOpkald extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView textView = new TextView(this);
    textView.append("Herunder kommmer opkald\n");
    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);

    String[] kolonner = {Calls.DATE, Calls.TYPE, Calls.NUMBER, Calls.CACHED_NAME, Calls.DURATION};
    String where = Calls.DATE + " >= " + (System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 7); // sidste 7 dage

    ContentResolver cr = getContentResolver();
    //Cursor c = cr.query(Calls.CONTENT_URI, kolonner, where, null, Calls.DATE);
    Cursor c = cr.query(Uri.parse("content://call_log/calls"), kolonner, where, null, Calls.DATE);
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    textView.append("\nkolonner = " + Arrays.asList(kolonner)); // date, number, name, duration
    textView.append("\nwhere = " + where); // date >= 1304737637646
    textView.append("\nURI = " + Calls.CONTENT_URI); // content://call_log/calls
    textView.append("\n\nDer er "+c.getCount()+" rækker:\n\n");
    while (c.moveToNext()) {
      textView.append(df.format(new Date(c.getLong(0))) + "  " + c.getInt(1) + " " + c.getString(2) + "  " + c.getString(3) + "  " + c.getInt(4) + "  " + "\n");
    }
    c.close();

  }
}
