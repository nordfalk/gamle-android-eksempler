package lekt03_diverse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;
import lekt05_grafik.Tegneprogram;

/**
 * @author Jacob Nordfalk
 */
public class BenytDialogerOgToasts extends Activity implements OnClickListener {

  Button visStandardToast, visToastMedBillede, visAlertDialog, visAlertDialog1, visAlertDialog2, visProgressDialog, visProgressDialogMedBillede, visNoitification;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    visStandardToast = new Button(this);
    visStandardToast.setText("visStandardToast");
    tl.addView(visStandardToast);

    visToastMedBillede = new Button(this);
    visToastMedBillede.setText("visToastMedBillede");
    tl.addView(visToastMedBillede);

    visAlertDialog = new Button(this);
    visAlertDialog.setText("visAlertDialog");
    tl.addView(visAlertDialog);

    visAlertDialog1 = new Button(this);
    visAlertDialog1.setText("visAlertDialog med 1 knap");
    tl.addView(visAlertDialog1);

    visAlertDialog2 = new Button(this);
    visAlertDialog2.setText("visAlertDialog med 2 knapper");
    tl.addView(visAlertDialog2);

    visProgressDialog = new Button(this);
    visProgressDialog.setText("visProgressDialog");
    tl.addView(visProgressDialog);

    visProgressDialogMedBillede = new Button(this);
    visProgressDialogMedBillede.setText("visProgressDialogMedBillede");
    tl.addView(visProgressDialogMedBillede);

    visNoitification = new Button(this);
    visNoitification.setText("visNoitification");
    tl.addView(visNoitification);

    visStandardToast.setOnClickListener(this);
    visToastMedBillede.setOnClickListener(this);
    visProgressDialog.setOnClickListener(this);
    visProgressDialogMedBillede.setOnClickListener(this);
    visAlertDialog.setOnClickListener(this);
    visAlertDialog1.setOnClickListener(this);
    visAlertDialog2.setOnClickListener(this);
    visNoitification.setOnClickListener(this);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    if (hvadBlevDerKlikketPå == visStandardToast) {
      Toast.makeText(this, "Standard-toast", Toast.LENGTH_LONG).show();
    } else if (hvadBlevDerKlikketPå == visToastMedBillede) {
      Toast t = new Toast(this);
      ImageView im = new ImageView(this);
      im.setImageResource(R.drawable.logo);
      im.setAlpha(180);
      t.setView(im);
      t.setGravity(Gravity.CENTER, 0, 0);
      t.show();
    } else if (hvadBlevDerKlikketPå == visAlertDialog) {
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("En AlertDialog");
      dialog.setMessage("Denne her har ingen knapper");
      dialog.show();
    } else if (hvadBlevDerKlikketPå == visAlertDialog1) {
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("En AlertDialog");
      dialog.setIcon(R.drawable.logo);
      dialog.setMessage("Denne her har én knap");
      dialog.setPositiveButton("Vis endnu en toast", new AlertDialog.OnClickListener() {

        public void onClick(DialogInterface arg0, int arg1) {
          Toast.makeText(BenytDialogerOgToasts.this, "Standard-toast", Toast.LENGTH_LONG).show();
        }
      });
      dialog.show();
    } else if (hvadBlevDerKlikketPå == visAlertDialog2) {
      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      dialog.setTitle("En AlertDialog");
      EditText et = new EditText(this);
      et.setText("Denne her viser et generelt view og har to knapper");
      dialog.setView(et);
      dialog.setPositiveButton("Vis endnu en toast", new AlertDialog.OnClickListener() {

        public void onClick(DialogInterface arg0, int arg1) {
          Toast.makeText(BenytDialogerOgToasts.this, "Endnu en standard-toast", Toast.LENGTH_LONG).show();
        }
      });
      dialog.setNegativeButton("Nej tak", null);
      dialog.show();
    } else if (hvadBlevDerKlikketPå == visProgressDialog) {
      ProgressDialog.show(this, "", "En ProgressDialog", true).setCancelable(true);
    } else if (hvadBlevDerKlikketPå == visProgressDialogMedBillede) {
      ProgressDialog dialog = new ProgressDialog(this);
      dialog.setIndeterminate(true); // drejende hjul
      dialog.setTitle("En ProgressDialog");
      dialog.setIcon(R.drawable.logo);
      dialog.setMessage("hej herfra");
      dialog.setOnCancelListener(new OnCancelListener() {

        public void onCancel(DialogInterface dialog) {
          Toast.makeText(BenytDialogerOgToasts.this, "Annulleret", Toast.LENGTH_LONG).show();
        }
      });
      dialog.show();
    } else if (hvadBlevDerKlikketPå == visNoitification) {
      Intent intent = new Intent(this, Tegneprogram.class);
      PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

      NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
              .setSmallIcon(R.drawable.logo)
              .setTicker("Der skal tegnes!")
              .setContentTitle("Tegn!")
              .setContentText("Du er nødt til at tegne lidt")
              .setSubText("Bla bla bla og en længere forklaring")
      ;

      long[] vibrate = {0, 100, 300, 400, 500, 510, 550, 560, 600, 610, 650, 610, -1};
      builder.setVibrate(vibrate);

      NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.notify(42, builder.build());
    }
  }
}
