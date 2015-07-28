package lekt06_data;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Jacob Nordfalk
 */
public class JsonGenerering extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView tv = new TextView(this);

    try {

      JSONObject json = new JSONObject(); // { }
      json.put("bank", "Merkur");  // {  "bank": "Merkur" }
      JSONArray kunder = new JSONArray();
      json.put("kunder", kunder);  // {  "bank": "Merkur", "kunder": [] }
      JSONObject k = new JSONObject();
      k.put("navn", "Jacob");
      k.put("kredit", 1000);
      kunder.put(k); // {  "bank": "Merkur", "kunder": [  { "navn": "Jacob", "kredit": 1000 }] }
      kunder.put(new JSONObject().put("navn", "Søren").put("kredit", 1007).put("alder", 29));
      kunder.put(new JSONObject("{ \"navn\": \"Bent\", \"kredit\": 5, \"kreditværdighed\": \"LAV\" }"));

      tv.append("\n\nHele JSONobjektet på en linje " + kunder.toString());
      tv.append("\n\nHele JSONobjektet på flere linjer " + kunder.toString(2));

    } catch (Exception ex) {
      ex.printStackTrace();
      tv.append("FEJL:" + ex.toString());
    }

    setContentView(tv);
  }
}
