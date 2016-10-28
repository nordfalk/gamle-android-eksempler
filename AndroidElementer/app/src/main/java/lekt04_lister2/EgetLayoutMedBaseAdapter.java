package lekt04_lister2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

public class EgetLayoutMedBaseAdapter extends Activity implements OnItemClickListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final String[] lande = {"Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal",};

    BaseAdapter adapter = new BaseAdapter() {
      @Override
      public int getCount() {
        return lande.length;
      }

      public Object getItem(int position) {
        return position;
      } // bruges ikke

      public long getItemId(int position) {
        return position;
      } // bruges ikke

      @Override
      public View getView(int position, View view, ViewGroup parent) {
        // view kan indeholde views fra et gammelt listeelement, der kan genbruges
        if (view==null) view = getLayoutInflater().inflate(R.layout.lekt04_listeelement, null);

        TextView overskrift = (TextView) view.findViewById(R.id.listeelem_overskrift);
        overskrift.setText(lande[position]);

        TextView beskrivelse = (TextView) view.findViewById(R.id.listeelem_beskrivelse);
        beskrivelse.setText("Land nummer " + position);
        ImageView billede = (ImageView) view.findViewById(R.id.listeelem_billede);
        if (position % 3 == 2) {
          billede.setImageResource(android.R.drawable.sym_action_call);
        } else {
          billede.setImageResource(android.R.drawable.sym_action_email);
        }

        return view;
      }
    };

    ListView listView = new ListView(this);
    listView.setOnItemClickListener(this);
    listView.setAdapter(adapter);

    listView.setSelector(android.R.drawable.ic_notification_overlay);

    setContentView(listView);
  }

  public void onItemClick(AdapterView<?> l, View v, int position, long id) {
    Toast.makeText(this, "Klik på " + position, Toast.LENGTH_SHORT).show();
  }
}
