package lekt04_lister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

public class BenytGridView extends Activity implements OnItemClickListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String[] lande = {"Danmark", "Norge", "Sverige", "Finland",
        "Holland", "Italien", "Tyskland", "Frankrig", "Spanien", "Portugal",
        "Nepal", "Indien", "Kina", "Japan", "Thailand",
        "Danmark", "Norge", "Sverige", "Finland",
        "Holland", "Italien", "Tyskland", "Frankrig", "Spanien", "Portugal",
        "Nepal", "Indien", "Kina", "Japan", "Thailand" };
    ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listeelement, R.id.listeelem_overskrift, lande);

    GridView gridView = new GridView(this);
    gridView.setOnItemClickListener(this);
    gridView.setNumColumns(GridView.AUTO_FIT);

    gridView.setAdapter(adapter);

    setContentView(gridView);
  }

  public void onItemClick(AdapterView<?> l, View v, int position, long id) {
    Toast.makeText(this, "Klik på " + position, Toast.LENGTH_SHORT).show();
  }
}
