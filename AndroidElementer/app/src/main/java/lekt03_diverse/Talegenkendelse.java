package lekt03_diverse;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.Locale;

import dk.nordfalk.android.elementer.R;

public class Talegenkendelse extends AppCompatActivity implements View.OnClickListener {
  EditText udtaleTekst;
  Button udtalKnap;

  private static final int REQ_CODE_SPEECH_INPUT = 100;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    udtaleTekst = new EditText(this);
    udtaleTekst.setText("Genkendt tekst kommer her.");
    udtaleTekst.setId(R.id.editText); // sæt ID så den redigerede tekst bliver genskabt ved skærmvending

    udtalKnap = new Button(this);
    udtalKnap.setOnClickListener(this);
    udtalKnap.setText("Talekenkendelse");
    udtalKnap.setEnabled(false);

    TableLayout ll = new TableLayout(this);
    ll.addView(udtaleTekst, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
    ll.addView(udtalKnap);
    setContentView(ll);
  }

  public void onClick(View view) {
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
    try {
      startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
    } catch (ActivityNotFoundException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case REQ_CODE_SPEECH_INPUT: {
        if (resultCode == RESULT_OK && null != data) {
          ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
          udtaleTekst.setText(result.get(0));
        }
        break;
      }

    }
  }
}