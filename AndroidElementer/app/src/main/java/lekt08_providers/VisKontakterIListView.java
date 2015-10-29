package lekt08_providers;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class VisKontakterIListView extends Activity implements OnItemClickListener {

  private SimpleCursorAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String[] kolonnner = {Contacts._ID, Contacts.DISPLAY_NAME, Email.DATA};

    Cursor cursor = getContentResolver().query(Email.CONTENT_URI, kolonnner,
            Contacts.IN_VISIBLE_GROUP + " = '1'", null, Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    startManagingCursor(cursor); // Lad cursoren følge aktivitetens livscyklus

    adapter = new SimpleCursorAdapter(this, R.layout.lekt04_listeelement, cursor,
            // Disse kolonner i cursoren...
            new String[]{Contacts.DISPLAY_NAME, Email.DATA},
            // ... skal afbildes over i disse views i res/layout/lekt04_listeelementelement.xml
            new int[]{R.id.listeelem_overskrift, R.id.listeelem_beskrivelse}
    );
    TextView textView = new TextView(this);
    textView.setText("Der er " + cursor.getCount() + " kontakter på telefonen.");

    ListView listView = new ListView(this);
    listView.addHeaderView(textView);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(this);

    setContentView(listView);
  }

  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    position--; // pga addHeaderView(textView)
    if (position > 0) {
      Cursor cursor = adapter.getCursor();
      cursor.moveToPosition(position);
      String navn = cursor.getString(1); // Contacts.DISPLAY_NAME
      Toast.makeText(this, navn, Toast.LENGTH_LONG).show();
    }
  }
}
