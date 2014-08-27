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
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Arrays;

/**
 * @author Jacob Nordfalk
 */
public class VisKontakter extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView textView = new TextView(this);
    textView.append("Herunder kommmer kontakters navn og info fra telefonbogen\n");
    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);

    ContentResolver cr = getContentResolver();

    Uri uri = Email.CONTENT_URI;
    textView.append("uri=" + uri + "\n");
    String[] kolonnner = {Contacts._ID, Contacts.DISPLAY_NAME, Email.DATA};
    String where = Contacts.IN_VISIBLE_GROUP + " = '1'";
    String orderBy = Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
    textView.append("SELECT " + Arrays.asList(kolonnner) + " FROM " + uri + " WHERE " + where + " ORDER BY " + orderBy);
    Cursor cursor = cr.query(uri, kolonnner, where, null, orderBy);

    while (cursor.moveToNext()) {
      String id = cursor.getString(0);
      String navn = cursor.getString(1);
      String epost = cursor.getString(2);
      textView.append("\n" + id + " " + navn + "  " + epost);
    }
    cursor.close();

  }
}
