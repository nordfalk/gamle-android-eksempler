package lekt04_lister2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

public class EgetLayoutMedBaseAdapterViewholder extends Activity implements OnItemClickListener {

  String[] lande = {"Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Tyskland",
          "Frankrig", "Spanien", "Portugal", "Nepal", "Indien", "Kina", "Japan", "Thailand"};

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ListView listView = new ListView(this);
    listView.setOnItemClickListener(this);
    listView.setAdapter(adapter);

    listView.setSelector(android.R.drawable.ic_notification_overlay);

    setContentView(listView);
  }

  public void onItemClick(AdapterView<?> l, View v, int position, long id) {
    Toast.makeText(this, "Klik på " + position, Toast.LENGTH_SHORT).show();
  }

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
      ListeelemViewholder vh; // holder en reference til alle views i listelementet
      // view kan indeholde views fra et gammelt listeelement, der kan genbruges
      if (view==null) {
        view = getLayoutInflater().inflate(R.layout.lekt04_listeelement, null);
        vh = new ListeelemViewholder();
        vh.overskrift =  (TextView) view.findViewById(R.id.listeelem_overskrift);
        vh.beskrivelse = (TextView) view.findViewById(R.id.listeelem_beskrivelse);
        vh.billede = (ImageView) view.findViewById(R.id.listeelem_billede);
        view.setTag(vh);
      } else {
        vh = (ListeelemViewholder) view.getTag();
      }

      vh.overskrift.setText(lande[position]);

      vh.beskrivelse.setText("Land nummer " + position + " på vh@"+Integer.toHexString(vh.hashCode()));
      if (position % 3 == 2) {
        vh.billede.setImageResource(android.R.drawable.sym_action_call);
      } else {
        vh.billede.setImageResource(android.R.drawable.sym_action_email);
      }

      return view;
    }
  };
}
