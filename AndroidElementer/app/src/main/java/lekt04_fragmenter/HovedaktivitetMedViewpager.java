package lekt04_fragmenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;

import dk.nordfalk.aktivitetsliste.Aktivitetsdata;
import dk.nordfalk.aktivitetsliste.Aktivitetsliste3;
import dk.nordfalk.aktivitetsliste.PagerSlidingTabStrip;
import dk.nordfalk.aktivitetsliste.ZoomOutPageTransformer;
import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class HovedaktivitetMedViewpager extends AppCompatActivity {
  private ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_ACTION_BAR);
    super.onCreate(savedInstanceState);
//    setContentView(R.layout.lekt04_fragmenter);

    viewPager = new ViewPager(this);
    viewPager.setId(R.id.viewPager);
    viewPager.setAdapter(new GalgelegViewPagerAdapter(getSupportFragmentManager()));
    viewPager.setPageTransformer(false, new ZoomOutPageTransformer());

    PagerSlidingTabStrip pagerSlidingTabStrip = new PagerSlidingTabStrip(this);
    pagerSlidingTabStrip.setViewPager(viewPager);

    LinearLayout ll = new LinearLayout(this);
    ll.setOrientation(LinearLayout.VERTICAL);
    ll.addView(pagerSlidingTabStrip);
    ll.addView(viewPager);
    ((LinearLayout.LayoutParams) viewPager.getLayoutParams()).weight = 1;
    setContentView(ll);

    setTitle("Hovedaktivitet");
    // Man kan trykke på app-ikonet i øverste venstre hjørne
    // (og det betyder at brugeren vil navigere op i hierakiet)
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed(); // brugeren vil navigere op i hierakiet
    }
    return super.onOptionsItemSelected(item);
  }

  private class GalgelegViewPagerAdapter extends FragmentPagerAdapter {
    public GalgelegViewPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public int getCount() {
      return 7;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return "fane nummer " + position;
    }

    @Override
    public Fragment getItem(int position) {
      Fragment f = null;
      if (position == 0) f = new Spillet_frag();
      else if (position == 2) f = new Spillet_frag();
      else f = new Hjaelp_frag();

//      if (position == 0) f = new Hovedmenu_frag();
//      else if (position == 5) f = new Velkomst_frag();

      Bundle b = new Bundle();
      b.putInt("position", position);
      f.setArguments(b);
      return f;
    }
  }
}
