package lekt02_intents;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jacob Nordfalk
 */
public class BenytIntentsMedTilladelser extends AppCompatActivity implements OnClickListener {
  EditText nummerfelt;
  Button ringOpDirekte;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    nummerfelt = new EditText(this);
    nummerfelt.setHint("Skriv telefonnummer her");
    nummerfelt.setInputType(InputType.TYPE_CLASS_PHONE);
    tl.addView(nummerfelt);

    ringOpDirekte = new Button(this);
    ringOpDirekte.setText("Ring op - direkte");
    tl.addView(ringOpDirekte);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);


    ringOpDirekte.setOnClickListener(this);
  }


  public void onClick(View v) {
    String nummer = nummerfelt.getText().toString();
    nummerfelt.setError(null);
    if (nummer.length() == 0) {
      nummerfelt.setError("Skriv et telefonnummer");
      Toast.makeText(this, "Skriv et telefonnummer", Toast.LENGTH_LONG).show();
      return;
    }


    try {
      if (v == ringOpDirekte) {
        try {
          // Bemærk: Kræver <uses-permission android:name="android.permission.CALL_PHONE" /> i manifestet.
          // Bemærk: Fra Android 6 (targetSdkVersion 23) og frem skal brugeren spørges om lov først
          int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
          if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
              Snackbar.make(v, "Giv tilladelse", Snackbar.LENGTH_LONG).setAction("OK", new OnClickListener() {
                @Override
                public void onClick(View v) {
                  ActivityCompat.requestPermissions(BenytIntentsMedTilladelser.this, new String[]{Manifest.permission.CALL_PHONE}, 4242);
                }
              }).show();
              return;
            }

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 4242);
            return;
          } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + nummer)));
          }
        } catch (Exception e) {
          Toast.makeText(this, "Du mangler <uses-permission android:name=\"android.permission.CALL_PHONE\" /> i manifestet\n" + e, Toast.LENGTH_LONG).show();
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      Toast.makeText(this, "Denne telefon mangler en funktion:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
      e.printStackTrace();
    }
  }


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      String nummer = nummerfelt.getText().toString();
      startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + nummer)));
    } else {
      Snackbar.make(ringOpDirekte, "Du har afvist at give tilladelser", Snackbar.LENGTH_SHORT).show();
    }
  }
}
