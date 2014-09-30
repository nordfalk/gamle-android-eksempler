package lekt07_fragmenter;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
* Created by j on 30-09-14.
*/ // Bemærk, fragmenter i indre klasser SKAL erklæres static
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TekstDialogFragment extends DialogFragment {

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

    TextView rod = new TextView(getActivity());
    rod.setText("Dette er Fragment2 - tryk tilbage");
    return rod;
  }
}
