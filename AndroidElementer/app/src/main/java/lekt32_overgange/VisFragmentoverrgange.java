package lekt32_overgange;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import dk.nordfalk.android.elementer.R;

public class VisFragmentoverrgange extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt07_fragmenter);

    FragmentovergangBegynd fragmentovergangBegynd = new FragmentovergangBegynd();
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
            .replace(R.id.fragmentindhold, fragmentovergangBegynd)
            .commit();
  }
}
