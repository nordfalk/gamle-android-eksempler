package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
* Created by j on 30-09-14.
*/
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TekstDialogFragment extends DialogFragment {

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

    Dialog dialog = getDialog();
    if (dialog!=null) { // Hvis fragmentet bruges som dialog, så sæt titlen
      dialog.setTitle("Overskrift");
      // Se http://developer.android.com/reference/android/app/DialogFragment.html#AlertDialog
      // for flere muligheder for at tilrette dialogers udseende
    }

    TextView rod = new TextView(getActivity());
    rod.setText("Dette er Fragment2 - tryk tilbage");
    rod.setBackgroundColor(0xFF660000);
    rod.setPadding(20,20,20,20);
    return rod;
  }
}
