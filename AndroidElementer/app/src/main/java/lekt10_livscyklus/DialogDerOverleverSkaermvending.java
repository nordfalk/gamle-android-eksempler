package lekt10_livscyklus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class DialogDerOverleverSkaermvending extends Activity implements OnClickListener {

  Button visAlertDialog, visAlertDialog1, visAlertDialog2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    visAlertDialog = new Button(this);
    visAlertDialog.setText("visAlertDialog");
    tl.addView(visAlertDialog);

    visAlertDialog1 = new Button(this);
    visAlertDialog1.setText("visAlertDialog med 1 knap");
    tl.addView(visAlertDialog1);

    visAlertDialog2 = new Button(this);
    visAlertDialog2.setText("visAlertDialog med 2 knapper");
    tl.addView(visAlertDialog2);

    visAlertDialog.setOnClickListener(this);
    visAlertDialog1.setOnClickListener(this);
    visAlertDialog2.setOnClickListener(this);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  @Override
  protected Dialog onCreateDialog(int id) {
    if (id == 1001) {
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("En AlertDialog");
      dialog.setMessage("Denne her har ingen knapper");
      return dialog.create();
    } else if (id == 1002) {
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("En AlertDialog");
      dialog.setIcon(R.drawable.logo);
      dialog.setMessage("Denne her har én knap");
      dialog.setPositiveButton("Vis endnu en toast", new AlertDialog.OnClickListener() {

        public void onClick(DialogInterface arg0, int arg1) {
          Toast.makeText(DialogDerOverleverSkaermvending.this, "Standard-toast", Toast.LENGTH_LONG).show();
        }
      });
      return dialog.create();
    } else if (id == 1003) {
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("En AlertDialog");
      EditText et = new EditText(this);
      et.setText("Denne her viser et generelt view og har to knapper");
      et.setId(R.id.editText); // sæt ID så den redigerede tekst bliver genskabt ved skærmvending
      dialog.setView(et);
      dialog.setPositiveButton("Vis endnu en toast", new AlertDialog.OnClickListener() {

        public void onClick(DialogInterface arg0, int arg1) {
          Toast.makeText(DialogDerOverleverSkaermvending.this, "Standard-toast", Toast.LENGTH_LONG).show();
        }
      });
      dialog.setNegativeButton("Nej tak", null);
      return dialog.create();
    } else {
      return null;
    }
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    if (hvadBlevDerKlikketPå == visAlertDialog) {
      showDialog(1001);
    } else if (hvadBlevDerKlikketPå == visAlertDialog1) {
      showDialog(1002);
    } else if (hvadBlevDerKlikketPå == visAlertDialog2) {
      showDialog(1003);
    }
  }
}
