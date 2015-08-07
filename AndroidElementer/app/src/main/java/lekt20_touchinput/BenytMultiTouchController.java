/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt20_touchinput;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author Jacob Nordfalk
 */
public class BenytMultiTouchController extends Activity {

  MultitouchView view;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    view = new MultitouchView(this);
    setContentView(view);
    Toast.makeText(this, "Knib sammen eller træk ud med to fingre", Toast.LENGTH_LONG).show();
  }
}
