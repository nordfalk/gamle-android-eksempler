package dk.nordfalk.aktivitetsliste;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AktivitetslisteHierakisk extends Activity implements ExpandableListView.OnChildClickListener {

  private ActivityInfo[] aktiviteter;
  private CheckBox autostart;
  private int onStartTæller;

    private String[] pakker={"People Names", "Dog Names", "Cat Names", "Fish Names"};
    private String[][] klasser={
      {"Arnold", "Barry", "Chuck", "David"},
      {"Ace", "Bandit", "Cha-Cha", "Deuce"},
      {"Fluffy", "Snuggles"},
      {"Goldy", "Bubbles"}
    };
    
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ExpandableListView listView=new ExpandableListView(this);
    ExpandableListAdapter mAdapter=new MyExpandableListAdapter();
    listView.setAdapter(mAdapter);

    try {
      aktiviteter=getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES).activities;
    } catch (NameNotFoundException ex) {
      ex.printStackTrace();
    }

    for (int position=0; position<aktiviteter.length; position++) {
      String pakkeOgKlasse=aktiviteter[position].name;
      String pakkenavn=pakkeOgKlasse.substring(0, pakkeOgKlasse.lastIndexOf("."));
      String klassenavn=pakkeOgKlasse.substring(pakkenavn.length()+1);
    }
    /*
        TextView listeelem_overskrift=(TextView) view.findViewById(R.id.listeelem_overskrift);
        TextView listeelem_beskrivelse=(TextView) view.findViewById(R.id.listeelem_beskrivelse);
        ImageView listeelem_billede=(ImageView) view.findViewById(R.id.listeelem_billede);

        String pakkeOgKlasse=aktiviteter[position].name;
        String pakkenavn=pakkeOgKlasse.substring(0, pakkeOgKlasse.lastIndexOf("."));
        String klassenavn=pakkeOgKlasse.substring(pakkenavn.length()+1);

        listeelem_overskrift.setText(klassenavn);
        listeelem_beskrivelse.setText(pakkenavn);
*/

    SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
    int position=prefs.getInt("position", 0);
    listView.setSelectionFromTop(position, 30);
    listView.setOnChildClickListener(this);

    //boolean startetFraLauncher = getIntent().getCategories().contains(Intent.CATEGORY_LAUNCHER);
    boolean startetFraLauncher = Intent.ACTION_MAIN.equals(getIntent().getAction());
    
    autostart=new CheckBox(this);
    autostart.setText("Start automatisk aktivitet næste gang");
    autostart.setChecked(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("autostart", false));
    if (autostart.isChecked() && startetFraLauncher) {
      onItemClick(listView, null, position, 0); // hack - 'klik' på listen!
    }
    
    TableLayout linearLayout=new TableLayout(this);
    linearLayout.addView(autostart);
    linearLayout.addView(listView);

    setContentView(linearLayout);
  }

  
  @Override
  public void onStart() {
    super.onStart();
    if (onStartTæller++ == 2) Toast.makeText(this, "Vink: Tryk længe på et punkt for at se kildekoden", Toast.LENGTH_LONG).show();
  }
  
  public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
    ActivityInfo aktivitetsinfo=aktiviteter[position];
    Toast.makeText(this, "Starter "+aktivitetsinfo.name, Toast.LENGTH_SHORT).show();

    Intent intent=new Intent();
    intent.setClassName(aktivitetsinfo.applicationInfo.packageName, aktivitetsinfo.name);
    startActivity(intent);

    // Gem position og 'start aktivitet direkte' til næste gang
    PreferenceManager.getDefaultSharedPreferences(this).edit().
        putInt("position", position).putBoolean("autostart", autostart.isChecked()).commit();
  }



  public TextView lavTextView() {
    TextView textView=new TextView(this);

    // Layout parameters for the ExpandableListView
    AbsListView.LayoutParams lp=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);

    textView.setLayoutParams(lp);
    // Center the text vertically
    textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
    // Set the text starting position
    textView.setPadding(36, 0, 0, 0);
    return textView;
  }


  /**
   * A simple adapter which maintains an ArrayList of photo resource Ids. 
   * Each photo is displayed as an image. This adapter supports clearing the
   * list of photos and adding a new photo.
   *
   */
  public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    // Sample data set.  children[i] contains the children (String[]) for groups[i].

    public Object getChild(int groupPosition, int childPosition) {
      return klasser[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
      return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
      return klasser[groupPosition].length;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
      TextView textView=lavTextView();
      textView.setText(getChild(groupPosition, childPosition).toString());
      return textView;
    }

    public Object getGroup(int groupPosition) {
      return pakker[groupPosition];
    }

    public int getGroupCount() {
      return pakker.length;
    }

    public long getGroupId(int groupPosition) {
      return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
      TextView textView=lavTextView();
      textView.setText(getGroup(groupPosition).toString());
      return textView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return true;
    }

    public boolean hasStableIds() {
      return true;
    }
  }

  
 
  
  @Override
  public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
      String tekst = klasser[groupPosition][childPosition];
      Toast.makeText(this, "Klik på gruppe " +groupPosition+" nummer "+childPosition+": "+ tekst, Toast.LENGTH_SHORT).show();
      return true;
  }
}
