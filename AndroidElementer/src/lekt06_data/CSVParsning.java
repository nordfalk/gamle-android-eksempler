package lekt06_data;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.InputStream;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class CSVParsning extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView tv = new TextView(this);

    try {
      InputStream is = getResources().openRawResource(R.raw.data_csveksempel);
      byte b[] = new byte[is.available()]; // kun små filer
      is.read(b);
      String str = new String(b, "UTF-8");
      tv.append(str);

      tv.append("\n\n---------------\n");

      double totalKredit = 0;
      for (String linje : str.split("\n")) {
        linje = linje.trim();
        if (linje.length() == 0 || linje.startsWith("#")) continue;
        String[] felter = linje.split(", ");
        String bank = felter[0];
        String navn = felter[1];
        double kredit = Double.parseDouble(felter[2]);
        tv.append(bank + " har " + navn + " som kunde med " + kredit + " kr.\n");
        totalKredit = totalKredit + kredit;
      }
      tv.append("\n\nTotal kredit er " + totalKredit + " kr.");

    } catch (Exception ex) {
      ex.printStackTrace();
      tv.append("FEJL:" + ex.toString());
    }

    setContentView(tv);
  }
}
