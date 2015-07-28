package lekt01_views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class ByvejrDeklarativ extends Activity implements OnClickListener {

  Button okKnap, annullerKnap;
  EditText postnrEditText;
  WebView webView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.byvejr); // Hvis layout ligger i res/layout/byvejr.xml
    // findViewById() kan først kaldes efter setContentView()
    postnrEditText = (EditText) findViewById(R.id.postnrEditText);
    okKnap = (Button) findViewById(R.id.okKnap);
    annullerKnap = (Button) findViewById(R.id.annullerKnap);
    webView = (WebView) findViewById(R.id.webView);
    webView.loadUrl("http://javabog.dk");

    okKnap.setOnClickListener(this);
    annullerKnap.setOnClickListener(this);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    System.out.println("Der blev klikket på " + hvadBlevDerKlikketPå);
    if (hvadBlevDerKlikketPå == okKnap) {
      String valgtPostNr = postnrEditText.getText().toString();
      Toast.makeText(this, "Viser byvejr for " + valgtPostNr, Toast.LENGTH_LONG).show();
      webView.loadUrl("http://servlet.dmi.dk/byvejr/servlet/byvejr_dag1?by=" + valgtPostNr + "&mode=long");
    } else {
      Toast.makeText(this, "Denne knap er ikke implementeret endnu", Toast.LENGTH_LONG).show();
    }
  }
}
