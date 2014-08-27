/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.games.boulderdash;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AH
 */
public class HighScoreActivity extends Activity {

    private File theFile;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        try {

        this.setTitle("HIGH SCORE");
        TextView highscore=new TextView(this);
        highscore.setText(readFile());
        highscore.setPadding(0, 100, 0,0);
        highscore.setGravity(Gravity.CENTER);
        highscore.setTextSize(30);
        LinearLayout ll = new LinearLayout(this);
        ll.addView(highscore,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
        setContentView(ll);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(HighScoreActivity.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(HighScoreActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public String readFile()throws FileNotFoundException, IOException{

        File privateFolder = this.getFilesDir();//data/data/pakkenavn/filnavn

        theFile=new File(privateFolder,"HighScore.txt");

        BufferedReader reader = new BufferedReader(new FileReader(theFile));

        String line= "";

        String tmp="";
        while((line=reader.readLine())!=null){
            tmp=tmp+line+"\n";

        }
        reader.close();

            return tmp;

        }


    }


