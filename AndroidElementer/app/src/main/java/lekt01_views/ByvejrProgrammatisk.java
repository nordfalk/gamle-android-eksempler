package lekt01_views;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jacob Nordfalk
 */
public class ByvejrProgrammatisk extends AppCompatActivity implements OnClickListener {

  Button okKnap, annullerKnap;
  EditText editText;
  WebView webView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tableLayout = new TableLayout(this);

    // Lav en række med teksten "Vejret for 2500 Valby" (det første gult)
    TableRow række = new TableRow(this);
    TextView textView = new TextView(this);
    textView.setText("Vejret for ");
    række.addView(textView);

    editText = new EditText(this);
    editText.setText("2500");
    editText.setSingleLine(true);
    editText.setTextColor(Color.BLUE);
    //editText.setSelection(0, 4);  // editText.getText().length() giver 4
    række.addView(editText);
    tableLayout.addView(række);

    okKnap = new Button(this);
    okKnap.setText("OK");
    tableLayout.addView(okKnap);

    annullerKnap = new Button(this);
    annullerKnap.setText("Annuller!");
    tableLayout.addView(annullerKnap);

    webView = new WebView(this);
    webView.loadUrl("http://javabog.dk");
    tableLayout.addView(webView);
    webView.getLayoutParams().height = 300; //LayoutParams.WRAP_CONTENT;

    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(tableLayout);

    setContentView(scrollView);

    okKnap.setOnClickListener(this);
    annullerKnap.setOnClickListener(this);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    System.out.println("Der blev klikket på " + hvadBlevDerKlikketPå);
    if (hvadBlevDerKlikketPå == okKnap) {
      String valgtPostNr = editText.getText().toString();
      Toast.makeText(this, "Viser byvejr for " + valgtPostNr, Toast.LENGTH_LONG).show();
      webView.loadUrl("http://servlet.dmi.dk/byvejr/servlet/byvejr_dag1?by=" + valgtPostNr + "&mode=long");
    } else {
      Toast.makeText(this, "Denne knap er ikke implementeret endnu", Toast.LENGTH_LONG).show();
    }
  }
}
