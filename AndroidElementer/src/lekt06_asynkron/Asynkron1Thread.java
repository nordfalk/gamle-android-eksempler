package lekt06_asynkron;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

public class Asynkron1Thread extends Activity implements OnClickListener {
  Button knap1, knap2, knap3;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    EditText editText = new EditText(this);
    editText.setText("Prøv at redigere her efter du har trykket på knapperne");
    tl.addView(editText);

    knap1 = new Button(this);
    knap1.setText("Synkront");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("Asynkront men brugerfladen opdateres fra baggrundstråd");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("Asynkront men brugerfladen opdateres fra UI-tråd");
    tl.addView(knap3);

    setContentView(tl);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    if (hvadBlevDerKlikketPå == knap1) {
      knap1.setText("arbejder");  // <1>
      SystemClock.sleep(10000);
      knap1.setText("færdig!");

    } else if (hvadBlevDerKlikketPå == knap2) {

      knap2.setText("arbejder");  // <2>
      Runnable r = new Runnable() {
        public void run() {
          SystemClock.sleep(10000);
          knap2.setText("færdig!"); // Fejl - kun GUI-tråden må røre GUIen!
        }
      };
      new Thread(r).start();

    } else if (hvadBlevDerKlikketPå == knap3) {

      knap3.setText("arbejder");  // <3>
      System.out.println("arbejder");
      Runnable r = new Runnable() {
        public void run() {
          SystemClock.sleep(10000);
          System.out.println("færdig!");
          Runnable r2 = new Runnable() {
            public void run() {
              knap3.setText("færdig!");
            }
          };
          runOnUiThread(r2);
          // Her kunne handler.post(r2); også anvendes
        }
      };
      new Thread(r).start();
    }
  }
  //Handler handler = new Handler(); // brug evt denne i stedet for runOnUiThread(r2);
}