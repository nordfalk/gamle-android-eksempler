// Se http://android-developers.blogspot.de/2015/05/android-design-support-library.html
// for Design Support Library
package lekt40_design;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

public class ScrollingActivity extends AppCompatActivity {

  private CollapsingToolbarLayout ctl;
  private FloatingActionButton fab1, fab2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt40_scrolling);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
    ctl.setTitle("CollapsingToolbar");


    fab1 = (FloatingActionButton) findViewById(R.id.fab1);
    fab2 = (FloatingActionButton) findViewById(R.id.fab2);

    fab1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "En Snackbar er dukker op i bunden og er synlig et par sekunder og kan swipes væk", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    ctl.setTitle("Snackbar trykket");
                  }
                }).show();
      }
    });

  }

}
