package lekt06_data;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class JsonParsning extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView tv = new TextView(this);

    try {
      InputStream is = getResources().openRawResource(R.raw.data_jsoneksempel);
      //InputStream is = new URL("http://javabog.dk/eksempel.json").openStream();

      byte b[] = new byte[is.available()]; // kun små filer
      is.read(b);
      String str = new String(b, "UTF-8");
      tv.append(str);

      JSONObject json = new JSONObject(str);
      String bank = json.getString("bank");
      tv.append("\n=== Oversigt over " + bank + "s kunder ===\n");
      double totalKredit = 0;

      JSONArray kunder = json.getJSONArray("kunder");
      int antal = kunder.length();
      for (int i = 0; i < antal; i++) {
        JSONObject kunde = kunder.getJSONObject(i);
        System.err.println("obj = " + kunde);
        String navn = kunde.getString("navn");
        double kredit = kunde.getDouble("kredit");
        tv.append(navn + " med " + kredit + " kr.\n");
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
