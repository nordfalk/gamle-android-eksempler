package lekt07_lister3;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import dk.nordfalk.android.elementer.R;

public class BenytRecyclerviewOverskrifter extends AppCompatActivity {

  String[] landeArray = {"* Norden", "Danmark", "Norge", "Sverige", "Island", "Færøerne", "Finland",
          "* Europa", "Tyskland", "Østrig", "Belgien", "Holland", "Italien", "Grækenland",
          "Frankrig", "Spanien", "Portugal",
          "* Resten af verden", "Nepal", "Indien", "Kina", "Japan", "Thailand"};
  // Vi laver en arrayliste så vi kan fjerne/indsætte elementer
  ArrayList<String> lande = new ArrayList<>(Arrays.asList(landeArray));

  RecyclerView recyclerView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    recyclerView = new RecyclerView(this);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);

    setContentView(recyclerView);
  }


  RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
    @Override
    public int getItemCount()  {
      return lande.size();
    }

    @Override
    public int getItemViewType(int position) {
      if (lande.get(position).startsWith("*")) return 1;
      else return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      if (viewType==0) {
        View itemView = getLayoutInflater().inflate(R.layout.lekt04_listeelement, parent, false);
        ListeelemViewholder vh = new ListeelemViewholder(itemView);
        vh.overskrift = (TextView) itemView.findViewById(R.id.listeelem_overskrift);
        vh.beskrivelse = (TextView) itemView.findViewById(R.id.listeelem_beskrivelse);
        vh.billede = (ImageView) itemView.findViewById(R.id.listeelem_billede);
        return vh;
      } else {
        View itemView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
        return new RecyclerView.ViewHolder(itemView) {};
      }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh0, int position) {
      if (getItemViewType(position)==0) {
        ListeelemViewholder vh = (ListeelemViewholder) vh0;
        vh.overskrift.setText(lande.get(position));
        vh.beskrivelse.setText("Land nummer " + position);
      } else {
        TextView tv = (TextView) vh0.itemView.findViewById(android.R.id.text1);
        tv.setTextSize(36);
        tv.setText(lande.get(position));
      }
    }
  };



  class ListeelemViewholder extends RecyclerView.ViewHolder {
    TextView overskrift;
    TextView beskrivelse;
    ImageView billede;

    public ListeelemViewholder(View itemView) {
      super(itemView);
    }
  }

}
