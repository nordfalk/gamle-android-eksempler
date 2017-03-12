package lekt07_lister3;

import android.app.Activity;
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

public class BenytRecyclerview extends AppCompatActivity {

  String[] landeArray = {"Danmark", "Norge", "Sverige", "Island", "Færøerne", "Finland",
          "Tyskland", "Østrig", "Belgien", "Holland", "Italien", "Grækenland",
          "Frankrig", "Spanien", "Portugal", "Nepal", "Indien", "Kina", "Japan", "Thailand"};
  // Vi laver en arrayliste så vi kan fjerne/indsætte elementer
  ArrayList<String> lande = new ArrayList<>(Arrays.asList(landeArray));

  RecyclerView recyclerView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    recyclerView = new RecyclerView(this);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    //recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(10, LinearLayoutManager.HORIZONTAL));

    //recyclerView.setOnItemClickListener(this); FINDES IKKE - i stedet skal man lytte efter onClick på de enkelte vieww
    recyclerView.setAdapter(adapter);

    setContentView(recyclerView);
    Snackbar.make(recyclerView, "Tryk på titlen for at flytte et element til toppen " +
            "eller på billedet for at fjerne det", Snackbar.LENGTH_INDEFINITE);
  }


  /**
   * Cacher forskellige views i et listeelement, sådan at søgninger i viewhierakiet
   * mew findViewById() kun behøver ske EN gang.
   * Se https://developer.android.com/training/material/lists-cards.html
   *
   * @author j
   */
  class ListeelemViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView overskrift;
    TextView beskrivelse;
    ImageView billede;

    public ListeelemViewholder(ViewGroup parent) {
      super(LayoutInflater.from(parent.getContext())
              .inflate(R.layout.lekt04_listeelement, parent, false));
      // itemView indeholder layoutet der lige er blevet pakket ud
      overskrift =  (TextView) itemView.findViewById(R.id.listeelem_overskrift);
      beskrivelse = (TextView) itemView.findViewById(R.id.listeelem_beskrivelse);
      billede = (ImageView) itemView.findViewById(R.id.listeelem_billede);

      overskrift.setOnClickListener(this);
      beskrivelse.setOnClickListener(this);
      billede.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      final int position = getAdapterPosition();
      final String landenavn = lande.get(position);
      Toast.makeText(v.getContext(), "Klik på " + position, Toast.LENGTH_SHORT).show();
      if (v == billede) { // Klik på billede fjerner landet fra listen
        lande.remove(position);
        adapter.notifyItemRemoved(position);
        Snackbar.make(recyclerView, landenavn + " fjernet", Snackbar.LENGTH_INDEFINITE)
                .setAction("Fortryd", new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                    lande.add(position, landenavn);
                    adapter.notifyItemInserted(position);
                    recyclerView.smoothScrollToPosition(position);
                  }
                }).show();
      }

      if (v == overskrift) { // Klik på overskrift flytter landet op til toppen
        lande.remove(position);
        lande.add(0, landenavn);
        adapter.notifyItemMoved(position, 0);
        recyclerView.scrollToPosition(0);
      }
    }
  }

  RecyclerView.Adapter adapter = new RecyclerView.Adapter<ListeelemViewholder>() {
    @Override
    public int getItemCount()  {
      return lande.size();
    }

    @Override
    public ListeelemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ListeelemViewholder(parent);
    }

    @Override
    public void onBindViewHolder(ListeelemViewholder vh, int position) {
      vh.overskrift.setText(lande.get(position));

      vh.beskrivelse.setText("Land nummer " + position + " på vh@"+Integer.toHexString(vh.hashCode()));
      if (position % 3 == 2) {
        vh.billede.setImageResource(android.R.drawable.ic_menu_delete);
      } else {
        vh.billede.setImageResource(android.R.drawable.ic_delete);
      }
    }
  };
}
