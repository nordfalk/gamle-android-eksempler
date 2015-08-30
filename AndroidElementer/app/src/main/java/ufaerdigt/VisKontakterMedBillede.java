package ufaerdigt;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

import dk.nordfalk.android.elementer.R;

public class VisKontakterMedBillede extends Activity {

  private static LinkedHashMap<String, Kontakt> kontakter;
  private ListView listView;
  private ArrayList<String> numre;
  private ContentResolver cr;

  public static void log(Object txt) {
    Log.d("Log", "" + txt);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    cr = getContentResolver();

    kontakter = new LinkedHashMap<String, Kontakt>();

    Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    String[] kolonner = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            //          Contacts.PHOTO_ID
    };
    String where = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
    String orderBy = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
    Cursor cursor = cr.query(uri, kolonner, where, null, orderBy);

    while (cursor.moveToNext()) {
      Kontakt k = new Kontakt();
      k.id = cursor.getLong(0);
      k.navn = cursor.getString(1);
      k.telefonNr = cursor.getString(2);
      kontakter.put(k.telefonNr, k);
    }
    cursor.close();
    Set<String> ks = kontakter.keySet();

    ArrayAdapter adapter = new ArrayAdapter<String>(this,
            R.layout.lekt04_listeelement, R.id.listeelem_overskrift, numre) {
      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView listeelem_overskrift = (TextView) view.findViewById(R.id.listeelem_overskrift);
        TextView listeelem_beskrivelse = (TextView) view.findViewById(R.id.listeelem_beskrivelse);
        ImageView listeelem_billede = (ImageView) view.findViewById(R.id.listeelem_billede);

        Kontakt k = kontakter.get(numre.get(position));

        listeelem_overskrift.setText(k.navn);
        listeelem_beskrivelse.setText(k.telefonNr);
        Bitmap billede = hentBillede(cr, k.telefonNr);
        if (billede != null) listeelem_billede.setImageBitmap(billede);
        else listeelem_billede.setImageResource(android.R.drawable.ic_menu_gallery);
        return view;
      }
    };

  }

  Bitmap hentBillede(ContentResolver cr, String telefonNr) {
    Uri phoneUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(telefonNr));
    Cursor contact = cr.query(phoneUri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

    if (contact.moveToFirst()) {
      long userId = contact.getLong(contact.getColumnIndex(ContactsContract.Contacts._ID));
      Uri billedeUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, userId);
      InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, billedeUri);
      log("Billede " + telefonNr + " (" + billedeUri + ") er: " + input);
      Bitmap b = BitmapFactory.decodeStream(input);
      return b;
    }
    return null;
  }
}
