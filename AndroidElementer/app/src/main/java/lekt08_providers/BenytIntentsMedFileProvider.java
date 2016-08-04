package lekt08_providers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Demonstrerer hvordan man benytter FileProvider til
 * 1) få et billede fra kameraet i fuld opløsning
 * 2) sende store data i et intent (billedet som vedhæftning i f.eks. gmail)
 *
 * Se også https://developer.android.com/training/secure-file-sharing/
 * @author Jacob Nordfalk
 */
public class BenytIntentsMedFileProvider extends Activity implements OnClickListener {

  Button tagBillede, sendBillede;
  TextView resultatTextView;
  LinearLayout resultatHolder;
  File billedeFil;
  Uri billedeUri;

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

    File dir = new File(getFilesDir(), "delt");
    dir.mkdirs();
    billedeFil = new File(dir, "billede.jpg");
    billedeUri = FileProvider.getUriForFile(this, "dk.nordfalk.android.elementer.fileprovider", billedeFil);
  }

  public void onClick(View hvadBlevDerKlikketPå) {

    if (hvadBlevDerKlikketPå == tagBillede) {
      Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      // Vi vil have billedet gemt i filen via FileProvider
      billedeFil.delete();
      i.putExtra(MediaStore.EXTRA_OUTPUT, billedeUri);
      i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
      startActivityForResult(i, 42);
    } else {
      Intent i = new Intent(Intent.ACTION_SEND);
      i.setType("message/rfc822");
      i.putExtra(Intent.EXTRA_EMAIL, new String[]{"jacob.nordfalk@gmail.com"});
      i.putExtra(Intent.EXTRA_CC, new String[]{"jacob.nordfalk@gmail.com"});
      i.putExtra(Intent.EXTRA_SUBJECT, "Et billede fra AndroidElementer");
      i.putExtra(Intent.EXTRA_TEXT, "Her er et billede");
      i.putExtra(Intent.EXTRA_STREAM, billedeUri);
      i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
      iv.setImageURI(billedeUri);
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
