package dk.firma.mitprojekt.logik;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class Stamdata  {
  /** Grunddata */
  public JSONObject json;

	public String chatUrl;
	public List<String> kanalkoder = new ArrayList<String>();

  /**
   * Slår en streng op efter en nøgle. Giver "" i fald nøglen ikke findes
   */
  public String s(String nøgle) {
		return json.optString(nøgle);
  }

}
