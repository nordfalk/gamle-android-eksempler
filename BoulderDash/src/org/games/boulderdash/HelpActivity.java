/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.games.boulderdash;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 *
 * @author AH
 */
public class HelpActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        WebView help =new WebView(this);
        //læser filen lokalt fra telefon
        help.loadUrl("file:///android_asset/BOULDERDASH_HELP.htm");
        LinearLayout help_layout= new LinearLayout(this);
        help_layout.addView(help,LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
        this.setTitle("Boulder Dash Help");
        setContentView(help_layout);
    }

}
