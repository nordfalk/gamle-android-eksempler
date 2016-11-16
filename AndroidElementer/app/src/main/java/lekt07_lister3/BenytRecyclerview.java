package lekt07_lister3;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;

import dk.nordfalk.android.elementer.R;

public class BenytRecyclerview extends Activity {

  String[] lande = {"Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Tyskland",
          "Frankrig", "Spanien", "Portugal", "Nepal", "Indien", "Kina", "Japan", "Thailand"};

  RecyclerView recyclerView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    recyclerView = new RecyclerView(this);

    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
    //recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(10, LinearLayoutManager.HORIZONTAL));

    //recyclerView.setOnItemClickListener(this);
    recyclerView.setAdapter(adapter);

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);
    setContentView(recyclerView);
  }

  RecyclerView.Adapter adapter = new RecyclerView.Adapter<ListeelemViewholder>() {
    @Override
    public int getItemCount()  {
      return lande.length;
    }


    @Override
    public ListeelemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ListeelemViewholder(parent);
    }

    @Override
    public void onBindViewHolder(ListeelemViewholder vh, int position) {
      vh.overskrift.setText(lande[position]);

      vh.beskrivelse.setText("Land nummer " + position + " på vh@"+Integer.toHexString(vh.hashCode()));
      if (position % 3 == 2) {
        vh.billede.setImageResource(android.R.drawable.sym_action_call);
      } else {
        vh.billede.setImageResource(android.R.drawable.sym_action_email);
      }
    }
  };

  ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
      final int fromPosition = viewHolder.getAdapterPosition();
      final int toPosition = target.getAdapterPosition();
      /*
      if (fromPosition < toPosition) {
        for (int i = fromPosition; i < toPosition; i++) {
          Collections.swap(adapter.getCapitolos(), i, i + 1);
        }
      } else {
        for (int i = fromPosition; i > toPosition; i--) {
          Collections.swap(adapter.getCapitolos(), i, i - 1);
        }
      }
      */
      adapter.notifyItemMoved(fromPosition, toPosition);
      return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
      ListeelemViewholder svH = (ListeelemViewholder) viewHolder;
      /*
      int index = mAdapter.getCapitolos().indexOf(svH.currentItem);
      mAdapter.getCapitolos().remove(svH.currentItem);
      mAdapter.notifyItemRemoved(index);
      if (emptyView != null) {
        if (mAdapter.getCapitolos().size() > 0) {
          emptyView.setVisibility(TextView.GONE);
        } else {
          emptyView.setVisibility(TextView.VISIBLE);
        }
      }
      */
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
      super.clearView(recyclerView, viewHolder);
      //reorderData();
    }
  };


  public class OptagelseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ImageView billede;
    private final ImageView slet;
    private final EditText billedetekst;
    public boolean billedetekstUnderOpdatering;

    public OptagelseViewHolder(View v) {
      super(v);
      billede = (ImageView) v.findViewById(R.id.listeelem_billede);
      billede.setOnClickListener(this);
      billedetekst = (EditText) v.findViewById(R.id.listeelem_overskrift);
      slet = (ImageView) v.findViewById(R.id.listeelem_beskrivelse);
      slet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (v == billede) {
      } else if (v == slet) {
        final int pos = getAdapterPosition();
        adapter.notifyItemRemoved(pos);
        Snackbar.make(recyclerView, "Optagelse fjernet", Snackbar.LENGTH_LONG)
                .setAction("Fortryd", new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                    adapter.notifyItemInserted(pos);
                    recyclerView.smoothScrollToPosition(pos);
                  }
                }).show();
        /*
        int pos = getAdapterPosition();
        Optagelse o = trin.svar.optagelser.remove(pos);
        trin.svar.optagelser.add(o);
        adapter.notifyItemMoved(pos, trin.svar.optagelser.size()-1);
        */
      }
    }
  }
}
