package lekt05_grafik;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class BenytBilledeknapper extends Activity {

  /**
   * Laver et TextView med en tekst
   */
  private TextView tv(String s) {
    TextView tv = new TextView(this);
    tv.setText(s);
    return tv;
  }

  /**
   * Laver en TableRow
   */
  private TableRow tr(View knap1, View knap2) {
    TableRow tr = new TableRow(this);
    tr.setPadding(5, 2, 5, 2);
    tr.addView(knap1);
    //knap1.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
    tr.addView(knap2);
    return tr;
  }

  private static OnTouchListener farvKnapNårDenErTrykketNed = new OnTouchListener() {
    public boolean onTouch(View view, MotionEvent me) {
      Log.d("onTouch()", me.toString());
      ImageView ib = (ImageView) view;
      if (me.getAction() == MotionEvent.ACTION_DOWN) {
        //log("farve "+me);
        ib.setColorFilter(0xFFA0A0A0, PorterDuff.Mode.MULTIPLY);
      } else if (me.getAction() == MotionEvent.ACTION_MOVE) {
      } else {
        //log("ikke farve "+me);
        ib.setColorFilter(null);
      }
      return true;
    }
  };

  OnFocusChangeListener farvKnapNårFokus = new OnFocusChangeListener() {
    public void onFocusChange(View v, boolean hasFocus) {
      Log.d("onFocusChange()", "hasFocus=" + hasFocus);
      ImageView ib = (ImageView) v;
      if (hasFocus) {
        ib.setColorFilter(0xFFC0C0C0, PorterDuff.Mode.MULTIPLY);
      } else {
        ib.setColorFilter(null);
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    tl.addView(tv("Dette eksempel viser billeder der kan fungere som knapper.\n"
        + "Men hvis man vil slippe for knap-rammen så kan man ikke se at knapppen bliver trykket ned.\n"
        + "Det kan løses ved at farve billedet når det berøres ('trykkes ned').\n"));


    tl.addView(tr(tv("ImageButton"), tv("Med farvefilter")));

    ImageButton knap1 = new ImageButton(this);
    knap1.setImageResource(R.drawable.logo);

    ImageButton knap2 = new ImageButton(this);
    knap2.setImageResource(R.drawable.logo);
    knap2.setOnTouchListener(farvKnapNårDenErTrykketNed);
    knap2.setOnFocusChangeListener(farvKnapNårFokus);

    tl.addView(tr(knap1, knap2));


    tl.addView(tr(tv("IB uden baggrund"), tv("Med farvefilter")));

    ImageButton knap3 = new ImageButton(this);
    knap3.setImageResource(R.drawable.logo);
    knap3.setBackgroundDrawable(null);

    ImageButton knap4 = new ImageButton(this);
    knap4.setImageResource(R.drawable.logo);
    knap4.setBackgroundDrawable(null);
    knap4.setOnTouchListener(farvKnapNårDenErTrykketNed);

    tl.addView(tr(knap3, knap4));


    tl.addView(tr(tv("ImageView"), tv("Med farvefilter")));

    ImageView knap5 = new ImageView(this);
    knap5.setImageResource(R.drawable.logo);

    ImageView knap6 = new ImageView(this);
    knap6.setImageResource(R.drawable.logo);
    knap6.setOnTouchListener(farvKnapNårDenErTrykketNed);

    tl.addView(tr(knap5, knap6));


    tl.addView(tv("Almindelige knapper kan man også sætte til at vise en anden baggrund - men ikke med så heldige resultater:"));

    Button knap7 = new Button(this);
    knap7.setText("Med alm baggrund");

    Button knap8 = new Button(this);
    knap8.setBackgroundResource(R.drawable.logo);
    knap8.setText("Med egen baggrund");
    tl.addView(tr(knap7, knap8));


    tl.addView(tv("Den 'rigtige' løsning er at lave knapgrafik til alle tilstande: normal, fokus, nedtrykket (og evt disablet) og kombinationerne, og så kombinere disse i en 'selector' drawable. Se nogle eksempler (fra Android) herunder:"));
    Button b1, b2;

    tl.addView(tr(tv("btn_star"), tv("btn_radio")));
    tl.addView(tr(b1 = new Button(this), b2 = new Button(this)));
    b1.setBackgroundResource(android.R.drawable.btn_star);
    b2.setBackgroundResource(android.R.drawable.btn_radio);

    tl.addView(tr(tv("btn_plus"), tv("btn_minus")));
    tl.addView(tr(b1 = new Button(this), b2 = new Button(this)));
    b1.setBackgroundResource(android.R.drawable.btn_plus);
    b2.setBackgroundResource(android.R.drawable.btn_minus);

    tl.addView(tr(tv("btn_default"), tv("btn_default_small")));
    tl.addView(tr(b1 = new Button(this), b2 = new Button(this)));
    b1.setBackgroundResource(android.R.drawable.btn_default);
    b2.setBackgroundResource(android.R.drawable.btn_default_small);
    b1.setText("btn_default");
    b2.setText("btn_default_small");

    tl.addView(tr(tv("btn_dialog"), tv("btn_dropdown")));
    tl.addView(tr(b1 = new Button(this), b2 = new Button(this)));
    b1.setBackgroundResource(android.R.drawable.btn_dialog);
    b2.setBackgroundResource(android.R.drawable.btn_dropdown);


    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }
}
