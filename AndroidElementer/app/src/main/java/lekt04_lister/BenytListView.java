package lekt04_lister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BenytListView extends Activity implements OnItemClickListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String[] lande = {"Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Tyskland",
            "Frankrig", "Spanien", "Portugal", "Nepal", "Indien", "Kina", "Japan", "Thailand"};

    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, lande);

    ListView listView = new ListView(this);
    listView.setOnItemClickListener(this);
    listView.setAdapter(adapter);

    setContentView(listView);
  }

  public void onItemClick(AdapterView<?> liste, View v, int position, long id) {
    Toast.makeText(this, "Klik på " + position, Toast.LENGTH_SHORT).show();
  }
}
