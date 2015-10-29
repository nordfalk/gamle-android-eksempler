package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by j on 30-09-14.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TekstDialog_frag extends DialogFragment {

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

    Dialog dialog = getDialog();
    if (dialog != null) { // Hvis fragmentet bruges som dialog, så sæt titlen
      dialog.setTitle("Overskrift");
      // Se http://developer.android.com/reference/android/app/DialogFragment.html#AlertDialog
      // for flere muligheder for at tilrette dialogers udseende
    }

    TextView tv = new TextView(getActivity());
    tv.setText("Dette er TekstDialog_frag - tryk tilbage");


    // Fik vi argumenter med? Så vis dem
    if (getArguments() != null) {
      tv.setText(getArguments().getString("TEKST"));
    }

    tv.setBackgroundColor(0xFF660000);
    tv.setPadding(20, 20, 20, 20);
    return tv;
  }
}
