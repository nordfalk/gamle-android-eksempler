/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt07_lister3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import dk.nordfalk.android.elementer.R;

/**
 * Cacher forskellige views i et listeelement, sådan at søgninger i viewhierakiet
 * kun behøver ske EN gang. Se
 * https://developer.android.com/training/material/lists-cards.html
 *
 * @author j
 */
class ListeelemViewholder extends RecyclerView.ViewHolder {
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
  }
}
