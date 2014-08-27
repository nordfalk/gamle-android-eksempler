package dk.firma.mitprojekt;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import dk.firma.mitprojekt.util.JsonIndlaesning;
import dk.firma.mitprojekt.util.Log;
import java.net.URL;
import java.net.URLEncoder;

public class Chat_akt extends Activity implements OnClickListener {
	private Button k1;
	private TextView chatmeddelelser;
	private EditText kaldenavn, besked;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		k1 = (Button) findViewById(R.id.knap1);
		chatmeddelelser = (TextView) findViewById(R.id.chatmeddelelser);
		kaldenavn = (EditText) findViewById(R.id.kaldenavn);
		besked = (EditText) findViewById(R.id.besked);

		k1.setOnClickListener(this);
		this.onClick(k1); // Tryk på knap så vi får opdateret status

		String gammmelChattekst = getPreferences(MODE_PRIVATE).getString("chat", "");
		chatmeddelelser.setText(gammmelChattekst);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick(View knappen) {
		final String b = URLEncoder.encode(kaldenavn.getText().toString());
		final String t = URLEncoder.encode(besked.getText().toString());
		besked.setText("");
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... arg0) {
				if (b.length()>0 && t.length()>0) {
					try {
						String url = MinApp.stamdata.chatUrl+"k=chat&b="+b+"&t="+t;
						Log.d("Sender besked med "+url);
						new URL(url).openStream().close(); // Bare hent, vi er ikke interesseret i svaret

						url = MinApp.stamdata.chatUrl+"k=hent";
						Log.d("Henter chatlog med "+url);
						String json = JsonIndlaesning.hentUrlSomStreng(url);
						getPreferences(MODE_PRIVATE).edit().putString("chat", json);
						Log.d("Fik chatlog "+json);
						return json;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				String json = (String) result;
				if (json != null) chatmeddelelser.setText(json);
			}
		}.execute();
	}

}
