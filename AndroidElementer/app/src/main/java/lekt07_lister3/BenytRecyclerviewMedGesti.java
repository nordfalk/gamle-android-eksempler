package lekt07_lister3;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import dk.nordfalk.android.elementer.R;

public class BenytRecyclerviewMedGesti extends AppCompatActivity {

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
    recyclerView.setAdapter(adapter);

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);
    setContentView(recyclerView);
    Snackbar.make(recyclerView, "Tag fat i et listeelement og arrangér det et andet sted i listen " +
            "eller træk det helt væk", Snackbar.LENGTH_INDEFINITE);
  }

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
      Toast.makeText(v.getContext(), "Klik på " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
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
        vh.billede.setImageResource(android.R.drawable.sym_action_call);
      } else {
        vh.billede.setImageResource(android.R.drawable.sym_action_email);
      }
    }
  };

  ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
          ItemTouchHelper.UP | ItemTouchHelper.DOWN,  // dragDirs
          ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) { // swipeDirs
    @Override
    public boolean onMove(RecyclerView rv, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
      int position = viewHolder.getAdapterPosition();
      int tilPos = target.getAdapterPosition();
      String land = lande.remove(position);
      lande.add(tilPos, land);
      Log.d("Lande", "Flyttet: "+lande);
      adapter.notifyItemMoved(position, tilPos);
      return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
      int position = viewHolder.getAdapterPosition();
      lande.remove(position);
      Log.d("Lande", "Slettet: "+lande);
      adapter.notifyItemRemoved(position);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
      super.clearView(recyclerView, viewHolder);
      Log.d("Lande", "Interaktion færdig");
    }
  };
}
