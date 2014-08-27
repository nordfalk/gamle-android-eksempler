package lekt05_grafik;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
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
public class ProgrammatiskLayout2 extends Activity implements OnClickListener {

  String teksten = "GrafikView";
  Button okKnap;
  EditText postnrEditText;
  View minGrafik;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Opret grafik-view
    minGrafik = new View(this) {

      @Override
      protected void onDraw(Canvas c) {
        Paint tekstStregtype = new Paint();
        tekstStregtype.setColor(Color.GREEN);
        tekstStregtype.setTextSize(24);
        c.rotate(23, 0, 0); // getWidth()/2, getHeight()/2// rotér 23 grader om midten
        c.drawText(teksten, 0, 20, tekstStregtype);
      }
    };

    TableLayout tableLayout = new TableLayout(this);

    tableLayout.addView(minGrafik);
    minGrafik.getLayoutParams().height = 60;

    // Lav en række med teksten "Vejret for 2500 Valby" (det første gult)
    TableRow række = new TableRow(this);
    TextView textView = new TextView(this);
    textView.setText("Vejret for ");
    textView.setTextColor(Color.YELLOW);
    række.addView(textView);

    postnrEditText = new EditText(this);
    postnrEditText.setText("2500");
    postnrEditText.setSingleLine(true);
    //postnrEditText.setSelection(0, 4);  // postnrEditText.getText().length() giver 4
    række.addView(postnrEditText);


    tableLayout.addView(række);

    okKnap = new Button(this);
    okKnap.setText("OK");
    tableLayout.addView(okKnap);


    ButtonPaaHovedet annullerKnap = new ButtonPaaHovedet(this);
    annullerKnap.setText("Annuller!");
    tableLayout.addView(annullerKnap);


    WebView webView = new WebView(this);
    webView.loadUrl("http://javabog.dk");
    tableLayout.addView(webView);
    webView.getLayoutParams().height = 300; //LayoutParams.WRAP_CONTENT;

    //simpla.setContentView(tableLayout);

    ScrollView scrollView = new ScrollView(this);
    scrollView.addView(tableLayout);

    setContentView(scrollView);

    okKnap.setOnClickListener(this);
    annullerKnap.setOnClickListener(this);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    System.out.println("Der blev klikket på " + hvadBlevDerKlikketPå);
    if (hvadBlevDerKlikketPå == okKnap) {
      teksten = postnrEditText.getText().toString();
      minGrafik.invalidate();
    } else {
      Toast.makeText(this, "Denne knap er ikke implementeret endnu", Toast.LENGTH_LONG).show();
    }
  }
}
