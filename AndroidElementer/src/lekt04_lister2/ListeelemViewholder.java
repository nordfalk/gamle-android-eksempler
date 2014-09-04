/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt04_lister2;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Cacher forskellige views i et listeelement, sådan at søgninger i viewhierakiet
 * kun behøver ske EN gang. Se
 * http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
 *
 * @author j
 */
class ListeelemViewholder {
  /**
   * Listeelementets position. Bruges til at tjekke at viewholderen ikke er blevet genbrugt
   * til at vise et andet element.
   */
  int position;
  TextView overskrift;
  TextView beskrivelse;
  ImageView billede;
}
