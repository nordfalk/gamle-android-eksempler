package lekt04_lister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

public class BenytGallery extends Activity implements OnItemClickListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String[] lande = {"Danmark", "Norge", "Sverige", "Finland",
        "Holland", "Italien", "Tyskland", "Frankrig", "Spanien", "Portugal",
        "Nepal", "Indien", "Kina", "Japan", "Thailand"};

    Gallery gallery = new Gallery(this);
    gallery.setOnItemClickListener(this);
    gallery.setSpacing(25); // 25 punkter

    gallery.setAdapter(new ArrayAdapter(this, R.layout.listeelement, R.id.listeelem_overskrift, lande));
    setContentView(gallery);

  }

  public void onItemClick(AdapterView<?> l, View v, int position, long id) {
    Toast.makeText(this, "Klik på " + position, Toast.LENGTH_SHORT).show();
  }
}
