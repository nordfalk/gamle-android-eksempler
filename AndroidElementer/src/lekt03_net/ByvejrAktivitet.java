/*
 * /dmi/femdgn_dk.png
 * /dmi/index/danmark/solvarsel.htm
 * /dmi/varsel.xml  - DMI - varsel om farligt vejr
 * /dmi/rss-nyheder
 */        //http://servlet.dmi.dk/byvejr/servlet/byvejr?valgtBy=2500&tabel=dag10_14
// For kun at se denne proces' output:
// adb logcat -v tag ActivityManager:I Vejret:V System.err:V *:S
// Fremtidig geokodning bliver med
// http://www.geonames.org/postal-codes/postal-codes-denmark.html
package lekt03_net;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Simpel aktivitet der viser byvejret i Valby
 *
 * @author Jacob Nordfalk
 */
public class ByvejrAktivitet extends Activity {

  private static final String TAG = "Vejret";
  ImageView imageView_dag1;
  ImageView imageView_dag3_9;
  ImageView imageView_dag10_14;
  TextView postnrByTextView;
  int valgtPostNr = 2500;
  String valgtBy = "Valby";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


		/*
     * Opsætning af de grafiske komponenter
		 */
    LinearLayout linearLayout = new LinearLayout(this);
    linearLayout.setOrientation(LinearLayout.VERTICAL);

    // Lav en række med teksten "Vejret for 2500 Valby" (det sidste blåt)
    LinearLayout række = new LinearLayout(this);
    TextView textView = new TextView(this);
    textView.setText("Vejret for ");
    række.addView(textView);

    postnrByTextView = new TextView(this);
    postnrByTextView.setText(valgtPostNr + " " + valgtBy);
    række.addView(postnrByTextView);

    // tilføj rækken
    linearLayout.addView(række);

    // første billede
    imageView_dag1 = new ImageView(this);
    //imageView_dag1.setScaleType(ImageView.ScaleType.FIT_XY);
    imageView_dag1.setAdjustViewBounds(true);
    linearLayout.addView(imageView_dag1);

    // en tekst
    textView = new TextView(this);
    textView.setText("... med 3 til 9-døgnsudsigt...");
    linearLayout.addView(textView);

    // andet billede
    imageView_dag3_9 = new ImageView(this);
    imageView_dag3_9.setAdjustViewBounds(true);
    linearLayout.addView(imageView_dag3_9);

    // mere tekst
    textView = new TextView(this);
    textView.setText("... og en 10-14-døgnsudsigt.");
    linearLayout.addView(textView);

    // tredie billede
    imageView_dag10_14 = new ImageView(this);
    imageView_dag10_14.setAdjustViewBounds(true);
    linearLayout.addView(imageView_dag10_14);


		/**/
    // alternativ række hvor postnr kan indtastes
    række = new LinearLayout(this);
    textView = new TextView(this);
    textView.setText("Vælg postnummer");
    række.addView(textView);
    //((LinearLayout.LayoutParams) textView.getLayoutParams()).weight = 1;

    final EditText editText_postnr = new EditText(this);
    editText_postnr.setText("2500");
    række.addView(editText_postnr);

    Button button_postnr = new Button(this);
    button_postnr.setText("OK");
    række.addView(button_postnr);

    // når bruger trykker OK skal det indlæses
    button_postnr.setOnClickListener(new OnClickListener() {

      public void onClick(View arg0) {
        valgtPostNr = Integer.parseInt("" + editText_postnr.getText());
        valgtBy = "ukendt";
        postnrByTextView.setText(valgtPostNr + " " + valgtBy);
        hentBilleder(); // max 1 time gamle
      }
    });
    // tilføj rækken
    linearLayout.addView(række);

    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(linearLayout);
    setContentView(scrollView);

    hentBilleder();
  }

  private void hentBilleder() {
    // Dette skulle egentlig ikke gøres i GUI-tråden, men det tager vi senere
    // http://android-developers.blogspot.com/2009/05/painless-threading.html
    // http://developer.android.com/reference/android/os/AsyncTask.html
    try {
      Bitmap byvejr_dag1 = opretBitmapFraUrl("http://servlet.dmi.dk/byvejr/servlet/byvejr_dag1?by=" + valgtPostNr + "&mode=long");
      imageView_dag1.setImageBitmap(byvejr_dag1);
      // Sørg for at den er synlig på skærmen
      imageView_dag1.requestRectangleOnScreen(new Rect());

      Bitmap byvejr_dag3_9 = opretBitmapFraUrl("http://servlet.dmi.dk/byvejr/servlet/byvejr?by=" + valgtPostNr + "&tabel=dag3_9");
      imageView_dag3_9.setImageBitmap(byvejr_dag3_9);

      Bitmap byvejr_dag10_14 = opretBitmapFraUrl("http://servlet.dmi.dk/byvejr/servlet/byvejr?by=" + valgtPostNr + "&tabel=dag10_14");
      imageView_dag10_14.setImageBitmap(byvejr_dag10_14);
    } catch (Exception ex) {
      ex.printStackTrace();
      advarBruger("Kunne ikke få data fra DMI");
      advarBruger("" + ex);
    }
  }

  private void advarBruger(String advarsel) {
    Log.w(TAG, advarsel);
    Toast.makeText(this, advarsel, Toast.LENGTH_LONG).show();
  }

  public static Bitmap opretBitmapFraUrl(String url) throws IOException {
    InputStream is = new URL(url).openStream();
    Bitmap bitmap = BitmapFactory.decodeStream(is);
    is.close();
    return bitmap;
  }
}
