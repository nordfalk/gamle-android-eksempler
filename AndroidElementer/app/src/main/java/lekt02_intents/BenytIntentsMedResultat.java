package lekt02_intents;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * Demonstrerer hvordan man benytter intents til at vælg en
 * kontaktperson, et billede eller tage et billede med kameraet
 *
 * @author Jacob Nordfalk
 */
public class BenytIntentsMedResultat extends Activity implements OnClickListener {

  Button vælgKontakt, vælgKontaktFraBillede, vælgBillede, tagBillede, dokumentation;
  TextView resultatTextView;
  LinearLayout resultatHolder;
  private int VÆLG_KONTAKT = 1111;
  private int VÆLG_BILLEDE = 2222;
  private int TAG_BILLEDE = 3333;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    vælgKontakt = new Button(this);
    vælgKontakt.setText("Vælg kontakt");
    vælgKontakt.setOnClickListener(this);
    tl.addView(vælgKontakt);

    vælgKontaktFraBillede = new Button(this);
    vælgKontaktFraBillede.setText("Vælg kontakt fra billede");
    vælgKontaktFraBillede.setOnClickListener(this);
    tl.addView(vælgKontaktFraBillede);

    vælgBillede = new Button(this);
    vælgBillede.setText("Vælg billede fra galleri");
    vælgBillede.setOnClickListener(this);
    tl.addView(vælgBillede);

    tagBillede = new Button(this);
    tagBillede.setText("Tag billede med kameraet");
    tagBillede.setOnClickListener(this);
    tl.addView(tagBillede);

    dokumentation = new Button(this);
    dokumentation.setText("Dokumentation om intents");
    dokumentation.setOnClickListener(this);
    tl.addView(dokumentation);

    resultatTextView = new TextView(this);
    tl.addView(resultatTextView);

    resultatHolder = new LinearLayout(this);
    tl.addView(resultatHolder);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    try {
      if (hvadBlevDerKlikketPå == vælgKontakt) {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, VÆLG_KONTAKT);

      } else if (hvadBlevDerKlikketPå == vælgBillede) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(i, VÆLG_BILLEDE);

      } else if (hvadBlevDerKlikketPå == tagBillede) {
        // Bemærk at jeg måtte have android:configChanges="orientation" for at aktiviteten
        // ikke blev vendt og jeg mistede billedet. I et rigtigt ville jeg forsyne mine views med
        // ID'er så deres indhold overlevede at skærmen skiftede orientering
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Hvis vi vil have billedet gemt
        //File fil = new File(Environment.getExternalStorageDirectory(),"billede.jpg");
        //i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fil));
        startActivityForResult(i, TAG_BILLEDE);

      } else {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://developer.android.com/reference/android/content/Intent.html")));
      }
    } catch (ActivityNotFoundException e) {
      Toast.makeText(this, "Du mangler Googles udvidelser på denne telefon:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent resIntent) {
    resultatTextView.setText(requestCode + " gav resultat " + resultCode + " og data:\n" + resIntent);
    System.out.println(requestCode + " gav resultat " + resultCode + " med data=" + resIntent);

    resultatHolder.removeAllViews();
    ContentResolver cr = getContentResolver();

    if (resultCode == Activity.RESULT_OK) {
      try {
        if (requestCode == VÆLG_KONTAKT) {
          resultatTextView.append("\n\nres.getData()=" + resIntent.getData());
          resultatTextView.append("\n\nres.getExtras()=" + resIntent.getExtras());
          Uri kontaktData = resIntent.getData();
          Cursor c = cr.query(kontaktData, null, null, null, null);
          while (c.moveToNext()) {
            for (int i = 0; i < c.getColumnCount(); i++) {
              resultatTextView.append("\n" + c.getColumnName(i) + ": " + c.getString(i));
//								+ c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            }
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                    c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID)));
            ImageView iv = new ImageView(this);
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
            if (input != null) {
              iv.setImageBitmap(BitmapFactory.decodeStream(input));
              input.close();
            } else {
              iv.setImageResource(android.R.drawable.ic_menu_gallery);
            }
            resultatHolder.addView(iv);
          }
          c.close();
        } else if (requestCode == VÆLG_BILLEDE) {
          AssetFileDescriptor filDS = getContentResolver().openAssetFileDescriptor(resIntent.getData(), "r");
          Bitmap bmp = BitmapFactory.decodeStream(filDS.createInputStream());
          ImageView iv = new ImageView(this);
          iv.setImageBitmap(bmp);
          resultatHolder.addView(iv);
        } else if (requestCode == TAG_BILLEDE) {
          Bitmap bmp = (Bitmap) resIntent.getExtras().get("data");
          ImageView iv = new ImageView(this);
          iv.setImageBitmap(bmp);
          resultatHolder.addView(iv);
          iv = new ImageView(this);
          iv.setImageResource(android.R.drawable.btn_star);
          resultatHolder.addView(iv);
        }

      } catch (IOException e) {
        e.printStackTrace();
        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
      }
    }
    ImageView iv = new ImageView(this);
    iv.setImageResource(android.R.drawable.ic_dialog_email);
    resultatHolder.addView(iv);

  }
}
