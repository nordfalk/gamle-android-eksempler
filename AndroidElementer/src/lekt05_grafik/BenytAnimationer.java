package lekt05_grafik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class BenytAnimationer extends Activity implements OnClickListener {

  Button knap1, knap2, knap3, knap4, knap5, knap6;
  private int animIndeks;
  private Button knap7;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    knap1 = new Button(this);
    knap1.setText("AnimationUtils.makeOutAnimation(this, true)");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("InAnimation(this, true)\nOutAnimation(this, false)");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("loadAnimation(R.anim.egen_anim)\nInChildBottomAnimation");
    tl.addView(knap3);

    knap4 = new Button(this);
    knap4.setText("Programmatiske animationer");
    tl.addView(knap4);

    knap5 = new Button(this);
    knap5.setText("Animeret skift af aktivitet");
    tl.addView(knap5);

    knap6 = new Button(this);
    knap6.setText("Eksempler på animationer");
    tl.addView(knap6);

    knap7 = new Button(this);
    knap7.setText("Pyjamasdans!");
    tl.addView(knap7);

    if (savedInstanceState == null) {
      knap1.startAnimation(AnimationUtils.makeInAnimation(this, true));
      knap2.startAnimation(AnimationUtils.makeInAnimation(this, false));
    }

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
    knap4.setOnClickListener(this);
    knap5.setOnClickListener(this);
    knap6.setOnClickListener(this);
    knap7.setOnClickListener(this);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  public void onClick(View hvadBlevDerKlikketPå) {

    if (hvadBlevDerKlikketPå == knap1) {
      Animation animation = AnimationUtils.makeOutAnimation(this, true);
      knap1.startAnimation(animation);
    } else if (hvadBlevDerKlikketPå == knap2) {
      knap1.startAnimation(AnimationUtils.makeInAnimation(this, true));
      knap2.startAnimation(AnimationUtils.makeOutAnimation(this, false));
    } else if (hvadBlevDerKlikketPå == knap3) {
      knap1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.egen_anim));
      knap2.startAnimation(AnimationUtils.makeInChildBottomAnimation(this));
    } else if (hvadBlevDerKlikketPå == knap4) {
      TranslateAnimation translationsanim = new TranslateAnimation(-100.0f, 0, 0, 0);
      translationsanim.setDuration(5000); // 5 sekunder
      translationsanim.setInterpolator(new BounceInterpolator());
      knap2.startAnimation(translationsanim);

      AlphaAnimation alfaanim = new AlphaAnimation(0.0f, 1.0f);
      alfaanim.setDuration(200);
      alfaanim.setRepeatCount(4); //evt Animation.INFINITE);
      alfaanim.setRepeatMode(Animation.REVERSE);
      knap3.startAnimation(alfaanim);

      ScaleAnimation skalaanim = new ScaleAnimation(1, 0.7f, 1, 1.5f, knap4.getWidth()/2, knap4.getHeight()/2);
      skalaanim.setDuration(300);
      skalaanim.setRepeatCount(1); // skalér ind og ud igen
      skalaanim.setRepeatMode(Animation.REVERSE);
      skalaanim.setInterpolator(new DecelerateInterpolator());
      knap4.startAnimation(skalaanim);

      AnimationSet sæt = new AnimationSet(false);
      sæt.addAnimation(skalaanim);
      sæt.addAnimation(translationsanim);
      sæt.addAnimation(alfaanim);
      knap5.startAnimation(sæt);
      sæt.setAnimationListener(new AnimationListener() {
        public void onAnimationStart(Animation animation) {
          knap1.setText("onAnimationStart(\n" + animation);
        }

        public void onAnimationEnd(Animation animation) {
          knap1.setText("onAnimationEnd(\n" + animation);
        }

        public void onAnimationRepeat(Animation animation) {
          knap1.setText("onAnimationRepeat(\n" + animation);
        }
      });
    } else if (hvadBlevDerKlikketPå == knap5) {
      startActivity(new Intent(this, Grafikdemo2.class));
      overridePendingTransition(R.anim.egen_anim, android.R.anim.fade_out);
    } else if (hvadBlevDerKlikketPå == knap6) {

      int[] animationer = {R.anim.egen_anim, R.anim.hyperspace_out,
          R.anim.push_left_in, R.anim.push_left_out, R.anim.push_up_in, R.anim.push_up_out
      };
      int animResId = animationer[animIndeks++ % animationer.length];
      String navn = getResources().getResourceEntryName(animResId);
      knap5.setText("Viste " + navn);
      knap5.startAnimation(AnimationUtils.loadAnimation(this, animResId));
    } else if (hvadBlevDerKlikketPå == knap7) {
      //Animation settet bruges til at samle flere former for animationer (scale,rotate,translate)
      AnimationSet animationSet = new AnimationSet(true);

      TranslateAnimation t = new TranslateAnimation(0, 0, 0, 200);
      t.setDuration(500);
      t.setFillAfter(true);
      t.setRepeatCount(-1);
      t.setRepeatMode(TranslateAnimation.REVERSE);
      animationSet.addAnimation(t);

      RotateAnimation r = new RotateAnimation(0f, 15f, 0, 0);
      //r.setStartOffset(1000);
      r.setDuration(300);
      r.setRepeatCount(-1);
      r.setRepeatMode(RotateAnimation.REVERSE);
      animationSet.addAnimation(r);
      knap1.startAnimation(animationSet);
    }
  }

  // Kaldes når vi vender tilbage til denne aktivitet
  @Override
  protected void onRestart() {
    super.onRestart();
    // Når der vendes tilbage til denne aktivitet skal den gamle aktivitet
    // fades ud og denne aktivitet flyve ind med animationen i res/anim/egen_anim.xml
    overridePendingTransition(R.anim.egen_anim, android.R.anim.fade_out);
  }
}
