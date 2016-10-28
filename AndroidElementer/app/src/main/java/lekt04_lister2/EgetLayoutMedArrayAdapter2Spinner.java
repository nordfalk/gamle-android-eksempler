package lekt04_lister2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

public class EgetLayoutMedArrayAdapter2Spinner extends Activity implements OnItemSelectedListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String[] lande = {"Danmark", "Norge", "Sverige", "Finland",
            "Holland", "Italien", "Tyskland", "Frankrig", "Spanien", "Portugal",
            "Nepal", "Indien", "Kina", "Japan", "Thailand"};
    Spinner spinner = new Spinner(this);
    spinner.setOnItemSelectedListener(this);
    // Standard-udseende
    //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, android.R.id.text1, lande);
    //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    // Eget layout af listeelementerne
    ArrayAdapter adapter = new ArrayAdapter(this, R.layout.lekt04_listeelement, R.id.listeelem_overskrift, lande) {
      @Override
      public View getView(int position, View cachedView, ViewGroup parent) {
        View view = super.getView(position, cachedView, parent);

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

      @Override
      public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
      }
    };

    spinner.setAdapter(adapter);
    spinner.setPrompt("Vælg et land");

    TableLayout tl = new TableLayout(this);
    tl.addView(spinner);
    setContentView(tl);
  }

  public void onItemSelected(AdapterView<?> liste, View v, int position, long id) {
    Toast.makeText(this, "Klik på " + position, Toast.LENGTH_SHORT).show();
  }

  public void onNothingSelected(AdapterView<?> liste) {
    Toast.makeText(this, "Intet valgt", Toast.LENGTH_SHORT).show();
  }
}
