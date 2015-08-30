package lekt31_venstremenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import dk.nordfalk.android.elementer.R;

/**
 */
public class BenytVenstremenu extends Activity implements OnClickListener {
  // Vi erklærer variabler herude så de huskes fra metode til metode
  Button knap1, knap2, knap3;
  private DrawerLayout mDrawerLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.lekt31_hovedaktivitet);
    // findViewById() kan først kaldes efter setContentView()
    knap1 = (Button) findViewById(R.id.knap1);
    knap2 = (Button) findViewById(R.id.knap2);
    knap3 = (Button) findViewById(R.id.knap3);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);

    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    if (navigationView != null) {
      setupDrawerContent(navigationView);
    }
  }
  private void setupDrawerContent(NavigationView navigationView) {
    navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
              }
            });
  }


  public void onClick(View v) {
    System.out.println("Der blev trykket på en knap");

    // Vis et tal der skifter så vi kan se hver gang der trykkes
    long etTal = System.currentTimeMillis();

    if (v == knap1) {

      knap1.setText("Du trykkede på mig. Tak! \n" + etTal);

    } else if (v == knap2) {

      knap3.setText("Nej nej, tryk på mig i stedet!\n" + etTal);

    } else if (v == knap3) {

      knap2.setText("Hey, hvis der skal trykkes, så er det på MIG!\n" + etTal);
      // Erstat logoet med en bil
      ImageView ikon = (ImageView) findViewById(R.id.ikon);
      ikon.setImageResource(R.drawable.bil);

    }

  }
}
