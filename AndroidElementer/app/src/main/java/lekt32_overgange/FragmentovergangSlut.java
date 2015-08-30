package lekt32_overgange;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.nordfalk.android.elementer.R;

public class FragmentovergangSlut extends Fragment {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lekt32_overgange_slut);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.ikon).setTransitionName("ikon");
            findViewById(R.id.etTextView).setTransitionName("knappen");
            findViewById(R.id.bil).setTransitionName("knap3");
        }
    }
*/

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.lekt32_overgange_slut, container, false);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      view.findViewById(R.id.ikon).setTransitionName("ikon");
      view.findViewById(R.id.etTextView).setTransitionName("knappen");
      view.findViewById(R.id.bil).setTransitionName("knap3");
    }

    return view;
  }
}
