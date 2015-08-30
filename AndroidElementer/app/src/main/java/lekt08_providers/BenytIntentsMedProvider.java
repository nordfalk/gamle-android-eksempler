package lekt08_providers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.File;

/**
 * Demonstrerer hvordan man benytter en ccontent providerintents til at vælg en
 * kontaktperson, et billede eller tage et billede med kameraet
 *
 * @author Jacob Nordfalk
 */
public class BenytIntentsMedProvider extends Activity implements OnClickListener {

  Button tagBillede, sendBillede;
  TextView resultatTextView;
  LinearLayout resultatHolder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    tagBillede = new Button(this);
    tagBillede.setText("Tag billede med kameraet");
    tagBillede.setOnClickListener(this);
    tl.addView(tagBillede);

    sendBillede = new Button(this);
    sendBillede.setText("Send billede som vedhæftning");
    sendBillede.setOnClickListener(this);
    tl.addView(sendBillede);

    resultatTextView = new TextView(this);
    tl.addView(resultatTextView);

    resultatHolder = new LinearLayout(this);
    tl.addView(resultatHolder);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    if (hvadBlevDerKlikketPå == tagBillede) {
      Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      // Vi vil have billedet gemt i vores content provider:
      File fil = new File(Environment.getExternalStorageDirectory(), "billede.jpg");
      //i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fil));
      i.putExtra(MediaStore.EXTRA_OUTPUT, MinProvider.URI);
      startActivityForResult(i, 42);

    } else {
      Intent i = new Intent(Intent.ACTION_SEND);
      i.setType("text/plain");
      i.putExtra(Intent.EXTRA_EMAIL, new String[]{"jacob.nordfalk@gmail.com"});
      i.putExtra(Intent.EXTRA_CC, new String[]{"jacob.nordfalk@gmail.com"});
      i.putExtra(Intent.EXTRA_SUBJECT, "Et billede fra AndroidElementer");
      i.putExtra(Intent.EXTRA_TEXT, "Her er et billede");
      i.putExtra(Intent.EXTRA_STREAM, MinProvider.URI);

      startActivity(Intent.createChooser(i, "Send e-post..."));
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent resIntent) {
    resultatTextView.setText(requestCode + " gav resultat " + resultCode + " og data:\n" + resIntent);
    System.out.println(requestCode + " gav resultat " + resultCode + " med data=" + resIntent);

    resultatHolder.removeAllViews();

    if (resultCode == Activity.RESULT_OK) {
      ImageView iv = new ImageView(this);
      iv.setImageURI(MinProvider.URI);
      resultatHolder.addView(iv);
      iv = new ImageView(this);
      iv.setImageResource(android.R.drawable.btn_star);
      resultatHolder.addView(iv);
    }
    ImageView iv = new ImageView(this);
    iv.setImageResource(android.R.drawable.ic_dialog_email);
    resultatHolder.addView(iv);

  }
}
