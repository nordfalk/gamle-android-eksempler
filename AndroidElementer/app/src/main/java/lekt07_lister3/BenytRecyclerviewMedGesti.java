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
            "eller træk det helt væk", Snackbar.LENGTH_INDEFINITE).show();
  }


  RecyclerView.Adapter adapter = new RecyclerView.Adapter<ListeelemViewholder>() {
    @Override
    public int getItemCount()  {
      return lande.size();
    }

    @Override
    public ListeelemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = getLayoutInflater().inflate(R.layout.lekt04_listeelement, parent, false);
      ListeelemViewholder vh = new ListeelemViewholder(view);
      vh.overskrift =  (TextView) view.findViewById(R.id.listeelem_overskrift);
      vh.beskrivelse = (TextView) view.findViewById(R.id.listeelem_beskrivelse);
      vh.billede = (ImageView) view.findViewById(R.id.listeelem_billede);
      return vh;
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


  class ListeelemViewholder extends RecyclerView.ViewHolder {
    TextView overskrift;
    TextView beskrivelse;
    ImageView billede;

    public ListeelemViewholder(View itemView) {
      super(itemView);
    }
  }


  // Læs mere på https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf#.fjo359jbr
  ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
          ItemTouchHelper.UP | ItemTouchHelper.DOWN,  // dragDirs
          ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) { // swipeDirs

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder vh, int actionState) {
      super.onSelectedChanged(vh, actionState);
      Log.d("Lande", "onSelectedChanged "+vh+" "+actionState);
      if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
        vh.itemView.animate().scaleX(0.8f).scaleY(0.8f).alpha(0.6f);
      }
    }

    @Override
    public boolean onMove(RecyclerView rv, RecyclerView.ViewHolder vh, RecyclerView.ViewHolder target) {
      int position = vh.getAdapterPosition();
      int tilPos = target.getAdapterPosition();
      String land = lande.remove(position);
      lande.add(tilPos, land);
      Log.d("Lande", "Flyttet: "+lande);
      adapter.notifyItemMoved(position, tilPos);
      return true; // false hvis rykket ikke skal foretages
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
      int position = viewHolder.getAdapterPosition();
      lande.remove(position);
      Log.d("Lande", "Slettet: "+lande);
      adapter.notifyItemRemoved(position);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder vh) {
      super.clearView(recyclerView, vh);
      Log.d("Lande", "clearView "+vh);
      vh.itemView.animate().scaleX(1).scaleY(1).alpha(1);
    }
  };
}
