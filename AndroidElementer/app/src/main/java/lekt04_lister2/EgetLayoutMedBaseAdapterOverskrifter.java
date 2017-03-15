package lekt04_lister2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

public class EgetLayoutMedBaseAdapterOverskrifter extends Activity implements AdapterView.OnItemClickListener {

  String[] landeOgOverskrifter = {
          "0En overskrift",
          "1Europas lande",
          "2Danmark", "2Norge", "2Sverige", "2Finland", "2Holland", "2Italien",
          "1Asien",
          "2Nepal", "2Kina",
          "0En overskrift mere",
          "1Flere lande i Europa",
          "2Tyskland", "2Finland", "2Holland", "2Italien",
          "1Flere lande i Asien",
          "2Indien", "2Nepal",
  };

  MinAdapterMedOverskrifter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ListView listView = new ListView(this);
    adapter = new MinAdapterMedOverskrifter();
    listView.setAdapter(adapter);
    listView.setDivider(null);
    listView.setOnItemClickListener(this);
    // Det er også muligt at tilføje views som top- eller bundelementer
    listView.addFooterView(new RatingBar(this));
    listView.addFooterView(new RatingBar(this));
    listView.addFooterView(getLayoutInflater().inflate(R.layout.lekt01_tre_knapper, null));
    setContentView(listView);
  }

  public void onItemClick(AdapterView<?> l, View v, int position, long id) {
    Toast.makeText(this, "Klik på " + position, Toast.LENGTH_SHORT).show();
    landeOgOverskrifter[position] = "1Du klikkede her!"; // Skift type til overskrift
    adapter.notifyDataSetChanged();
  }

  public class MinAdapterMedOverskrifter extends BaseAdapter {

    public int getCount() {
      return landeOgOverskrifter.length;
    }

    public Object getItem(int position) {
      return position;
    } // bruges ikke

    public long getItemId(int position) {
      return position;
    } // bruges ikke

    /** Antallet af forskellige slags elementer */
    @Override
    public int getViewTypeCount() {
      return 3;
    }

    /**
     * Skal give typen/slagsen af elementet der skal vises.
     * 0 er normale lande, 1 er kategorier og 2 er overskrifter
     */
    @Override
    public int getItemViewType(int position) {
      String landEllerOverskrift = landeOgOverskrifter[position];
      if (landEllerOverskrift.startsWith("0")) return 0;
      if (landEllerOverskrift.startsWith("1")) return 1;
      return 2;
    }

    /** Om dette element kan vælges */
    @Override
    public boolean isEnabled(int position) {
      return getItemViewType(position) > 1;
    }

    public View getView(final int position, View view, ViewGroup parent) {
      int typen = getItemViewType(position);

      if (view == null) {
        // Vi skal oprette et nyt view afhængig af typen
        if (typen == 0) {
          view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
        } else if (typen == 1) {
          view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
        } else {
          view = getLayoutInflater().inflate(R.layout.lekt04_listeelement, parent, false);
        }
      }

      String landEllerOverskrift = landeOgOverskrifter[position].substring(1);
      // Sæt indholdet afhængig af typen
      if (typen == 0) {
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setTextSize(36);
        tv.setText(landEllerOverskrift);
      } else if (typen == 1) {
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setText(landEllerOverskrift);
      } else {
        TextView tvo = (TextView) view.findViewById(R.id.listeelem_overskrift);
        tvo.setText(landEllerOverskrift);
        TextView beskrivelse = (TextView) view.findViewById(R.id.listeelem_beskrivelse);
        beskrivelse.setText("Position nummer " + position);
      }

      return view;
    }
  }
}
