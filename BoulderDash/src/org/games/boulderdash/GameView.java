/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.games.boulderdash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 *
 * @author AH
 */
public class GameView extends View {

   private final View keys[] = new View[1];
   private float shiftViewX=0;
   private float shiftViewY=0;
   boolean isAlive;
   static boolean runThread;



  ArrayList<Coord> redraw = new ArrayList<Coord>();
  //ArrayList sounds = new ArrayList();
  private static final int SLEEP_DELAY = 1750;
  int maxtimer=1,timer,level;
  int score,accumulatedScore,scorePerDiamond=5,scorePerExtraDiamond=10;
  
  public static final int UP     = 0;
  public static final int RIGHT  = 1;
  public static final int DOWN   = 2;
  public static final int LEFT   = 3;


 // Game variables
  Coord coordPlayer,coordExit;
  int numNeededDiamonds,numFulfilledDiamonds;
  boolean levelDone,playerAlmostDead,playerDead;

  // Arrays to handle input from APPLET tag
  String[] arrNumNeededDiamonds,arrScorePerDiamond,arrScorePerExtraDiamond;
  int[] arrMaxtimer;
  String[] arrLevelTitle,arrLevelAuthor,arrImageFilename;

    // Image and icon data
  private Bitmap[] img;
  private static final int NUM_IMAGES = 15;
  private static final String imgRep = ".:#WMPO+*BFA][R";//string to set up map
  private String imageFilename = "images.gif";
  private static int[][] imgCoord =
    {{  0, 0,20,20},   // Space
     { 20, 0,20,20},   // Dirt
     { 40, 0,20,20},   // Titanium Wall
     {  0,20,20,20},   // Brick Wall
     { 20,20,20,20},   // Magic Wall
     { 40,20,20,20},   // Player
     {  0,40,20,20},   // Rock
     { 20,40,20,20},   // Diamond
     { 40,40,20,20},   // Explosion
     {  0,60,20,20},   // Butterfly
     { 20,60,20,20},   // Firefly
     { 40,60,20,20},   // Amoeba
     {  0,80,20,20},   // Closed Exit
     { 20,80,20,20},   // Open Exit
     { 40,80,20,20}};  // Robot (not programmed)


  // Map and level data
  private static final int X_DIMEN = 20;
  private static final int Y_DIMEN = 20;
  private static float SQUARE_DIMEN = 20;
  private char[][] levelMap;
  private int NUM_LEVELS;
  private String[][] levelData;
  ArrayList <BotPosition> bots ;
  Drawable theImage;

  GameActivity parent;


    

   public String updateScore(){
       String scoreBoardTxt="Score: "+score+"   ";
       scoreBoardTxt=scoreBoardTxt+"Level: "+level+" / "+NUM_LEVELS;
       scoreBoardTxt=scoreBoardTxt+"\nDiamonds: "+numFulfilledDiamonds+" / "+(numFulfilledDiamonds<numNeededDiamonds ? numNeededDiamonds:0);
       scoreBoardTxt=scoreBoardTxt+"   Diamond Points: "+(numFulfilledDiamonds<numNeededDiamonds ? scorePerDiamond : scorePerExtraDiamond);
       
       return scoreBoardTxt;

   }


    public GameView(Context context, AttributeSet as) {
        super(context,as);
        parent=((GameActivity)context);
        loadImages();
    

        // Get parameters from the HTML file
        String numlevels ="2";// getResources().getString( R.string.levels);

        if (numlevels == null) return;
        NUM_LEVELS = Integer.parseInt(numlevels);
        levelData = new String[NUM_LEVELS][Y_DIMEN];

        arrNumNeededDiamonds = getResources().getStringArray(R.array.NEEDED_DIAMONDS);
        arrScorePerDiamond = getResources().getStringArray(R.array.POINTS_PER_DIAMOND);
        arrScorePerExtraDiamond = getResources().getStringArray(R.array.POINTS_PER_EXTRA_DIAMOND);


        arrMaxtimer = new int[NUM_LEVELS];
        arrLevelTitle = new String[NUM_LEVELS];
        arrLevelAuthor = new String[NUM_LEVELS];
        arrImageFilename = new String[NUM_LEVELS];


        int[]level_mapping={R.array.level_data_1,R.array.level_data_2};

        for (int i=0; i<NUM_LEVELS; i++) {
            for (int u=0; u<Y_DIMEN; u++) {
                String level_Data[]=getResources().getStringArray(level_mapping[i]);

                levelData[i][u] =level_Data[u];
                                    
            }



            arrLevelTitle[i] = getResources().getString(R.string.LEVEL_TITLE_1);
            if (arrLevelTitle[i] == null) arrLevelTitle[i] = "Unnamed";
                arrLevelAuthor[i] = getResources().getString(R.string.LEVEL_AUTHOR_1);
            if (arrLevelAuthor[i] == null) arrLevelAuthor[i] = "Anonymous";
                arrImageFilename[i] = getResources().getString(R.string.IMAGEFILE_1);
            if (arrImageFilename[i] == null)
                arrImageFilename[i] = "images.gif";

             }



        // Set up player variables
        coordPlayer = new Coord();
        coordExit = new Coord();

        // Just in case someone forgets to define these, let's make them exist
        coordPlayer.x = 2; coordPlayer.y = 2;
        coordExit.x = 1; coordExit.y = 1;

        // Set up the map
        if(levelMap==null){
            levelMap = new char[X_DIMEN][];
            for (int i=0; i<X_DIMEN; i++) {
                levelMap[i] = new char[Y_DIMEN];
            }
        }
        else
            isAlive=true;

        level = 1;
        setupLevel(level);


    }

   private void setupLevel(int level) {

   /*
   if(parent.scoreBoard!=null)
    parent.scoreBoard.setText(updateScore());
*/
    numNeededDiamonds = Integer.parseInt(arrNumNeededDiamonds[level-1]);
    scorePerDiamond = Integer.parseInt(arrScorePerDiamond[level-1]);
    scorePerExtraDiamond = Integer.parseInt(arrScorePerExtraDiamond[level-1]);

   

    /* Set up the level */
    shiftViewX=0;
    levelDone = false;
    playerAlmostDead = false;
    playerDead = false;
    bots = new ArrayList<BotPosition>();
    numFulfilledDiamonds = 0;
    for (int y=0; y<Y_DIMEN; y++) {
      for (int x=0; x<X_DIMEN; x++) {

        levelMap[x][y] = levelData[level-1][y].charAt(x);

        drawCoord(x,y);
        if ((levelMap[x][y] == '[') || (levelMap[x][y] == ']')) {
          coordExit.x = x;
          coordExit.y = y;
        } else
        if (levelMap[x][y] == 'P') {
          coordPlayer.x = x;
          coordPlayer.y = y;
        } else
        if ((levelMap[x][y] == 'B') || (levelMap[x][y] == 'F')) {
            int dir = (levelMap[x][y] == 'F')?0:1;
            BotPosition botpos = new BotPosition(x,y,1,dir);
            bots.add(botpos);
        }
         
         
      }
    }

    
  }

    


  private void loadImages()
  {
        // Break up source image into individual images
        // First, load the large image
       Bitmap sourceImage = BitmapFactory.decodeStream(getResources().openRawResource(
                    R.drawable.images));

          // Now break it up into pieces
        img = new Bitmap[NUM_IMAGES];
        for (int i=0; i<NUM_IMAGES; i++) {
          img[i] = cropSourceImage(sourceImage,imgCoord[i]);
          
        }
  }
  private Bitmap cropSourceImage(Bitmap sImage, int[] iCoord){

        //then Crop the obtained 'bitmap'
        Bitmap  cropedBitmap= Bitmap.createBitmap(sImage,iCoord[0],iCoord[1],iCoord[2],iCoord[3]);
        return cropedBitmap;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int x = 0; x < X_DIMEN; x++) {
            for (int y = 0; y < Y_DIMEN; y++) {
                int index;
                for (index = 0; index < imgRep.length(); index++) {
                    if (imgRep.charAt(index) == levelMap[x][y]) {
                        canvas.drawBitmap(img[index], (x * SQUARE_DIMEN) - shiftViewX, (y * SQUARE_DIMEN) + 40, null);
                    }
                }
            }
        }
    }

   


  public boolean controls(int controlID, int scrWidth, int scrHeight) {
    
    
    if (playerAlmostDead) {
      return false;
    }

    Coord delta = new Coord();
    Coord delta2 = new Coord();
    Coord move  = new Coord();
    Coord move2  = new Coord();
    boolean validMove = false;

    // Interpret the keycode
    if (controlID == R.id.up) {
      delta.x = 0;   delta.y = -1;
    } else
    if (controlID == R.id.right) {
      delta.x = 1;   delta.y = 0;
    } else
    if (controlID == R.id.down) {
      delta.x = 0;   delta.y = 1;
    } else
    if (controlID == R.id.left) {
      delta.x = -1;  delta.y = 0;
    }
    

    // Make move relative to player
    move.x = coordPlayer.x + delta.x;
    move.y = coordPlayer.y + delta.y;

    //move screen to right-side mode
    if(move.x>=(scrWidth/20)){
    	shiftViewX=400-scrWidth;
    }
    //move screen back to initail mode
    if(move.x<=(20-(scrWidth/20))&&shiftViewX!=0){
    	shiftViewX=0;
    }
    
    
    // Range checking on the move
    if (move.x < 0) { move.x = 0; } else
    if (move.x > X_DIMEN) { move.x = X_DIMEN; }
    if (move.y < 0) { move.y = 0; } else
    if (move.y > Y_DIMEN) { move.y = Y_DIMEN; }

    // Create a "look-ahead" position
    delta2.x = delta.x*2;
    delta2.y = delta.y*2;
    move2.x = coordPlayer.x + delta2.x;
    move2.y = coordPlayer.y + delta2.y;

    // Moving into an empty space?  "I'll allow it!" -- Mills Lane
    if ((levelMap[move.x][move.y] == '.') || (levelMap[move.x][move.y] == ':')) {
      if (levelMap[move.x][move.y] == '.') {
        redraw(move); // SPACE sound
      } else {
        redraw(move); // DIRT sound
      }
      validMove = true;
    } else

    // Pushing a rock?
    if ((levelMap[move.x][move.y] == 'O') && (move2.y != move.y-1)) {
      if (((move2.x >= 0) && (move2.x <= X_DIMEN)) &&
          ((move2.y >= 0) && (move2.y <= Y_DIMEN))) {
        if (levelMap[move2.x][move2.y] == '.') {
          // "look-ahead" is an empty space.  Move the rock.
          levelMap[move2.x][move2.y] = levelMap[move.x][move.y];
          redraw(move2);
          redraw(move);
          validMove = true;
        }
      }
    } else

    // Grabbing a Diamond?
    // Add one to the Number of Fulfilled Diamonds,
    // and see if the exit should be opened
    if (levelMap[move.x][move.y] == '+') {
       numFulfilledDiamonds++;
       score += (numFulfilledDiamonds<numNeededDiamonds ? scorePerDiamond : scorePerExtraDiamond);
       updateScore();
       redraw(move);
       parent.playSound(R.raw.diamond_sound);
       if (numFulfilledDiamonds == numNeededDiamonds) {
         levelMap[coordExit.x][coordExit.y] = '[';
         redraw(coordExit);
       }
       validMove = true;
    } else

    // If moving into an exit, make sure it's open!
    if (levelMap[move.x][move.y] == '[') {
      redraw(move);
      validMove = true;
    }

   
    if (validMove) {
      levelMap[move.x][move.y] = 'P';
      levelMap[coordPlayer.x][coordPlayer.y] = '.';
      drawCoord(coordPlayer.x,coordPlayer.y);
      coordPlayer.x = move.x;
      coordPlayer.y = move.y;
    }

    // Redraw coordinates that need to be redrawn
    redrawCoords();

    // Is the level over?
    if ((coordPlayer.x == coordExit.x) &&
        (coordPlayer.y == coordExit.y)) {
      
      levelDone = true;
            // All done with the level!  Give a little pause...
      try {
        Thread.sleep(SLEEP_DELAY);
      } catch (Exception e) {
        Log.e("CONTROLS()", e.getMessage());
      }

      accumulatedScore = score;
      if (level+1 < NUM_LEVELS+1) {
        level++;
      } else {
        level = 1;
      }
      setupLevel(level);
    }

    return true;
  }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
        return super.onTouchEvent(event);


       // SQUARE_DIMEN = - event.getX();
        //y = - event.getX()/SQUARE_DIMEN;
        Log.d("onTouchEvent", "onTouchEvent: x " + event.getX() +"onTouchEvent: y "+ event.getY());
       // postInvalidate();
        return true;






    }

   

    public void drawCoord(int x, int y)
  {
    int index;
    for (index=0; index<imgRep.length(); index++) {
        if (imgRep.charAt(index) == levelMap[x][y]) {
            postInvalidate();//redraw canvas
        }
    }
    
  }

    public void redrawCoords() {
    // Redraw any coordinates that should be redrawn
    try {
        for (int i=0; i<redraw.size(); i++) {
            Coord display = (Coord)redraw.get(i);
            drawCoord(display.x,display.y);



        }
    } catch (Exception e) {
        Log.e("REDRAWCOORDS()", e.getMessage());
    }
    redraw.clear();
    
  }

  public void redraw(Coord coord) {
    redraw.add(coord);
    

  }

   

    GraphicsThread newThread() {
      return new GraphicsThread();
    }

    public String getScore(){
        return score+""; //implicit cast to String
    }


    public class GraphicsThread extends Thread {
        public boolean active=true;

    public GraphicsThread(){
        super("Graphics thread");
    }

    public void setActive(boolean bool){

        active=bool;

    }

    @Override
     public void run()  {


    // This will take care of animation
    

    boolean botSmash,rockSmash,amoebaSmash,explosionsQuenched=false;
    Coord smashCoord = new Coord();
    char me = '.';

    while (this.isAlive() && active) {
      if (playerAlmostDead && explosionsQuenched) {
        for (int y=Y_DIMEN-2; (y>=0) && (explosionsQuenched); y--) {
          for (int x=0; (x<X_DIMEN) && (explosionsQuenched); x++) {
            if (levelMap[x][y] == '*') {
              explosionsQuenched = false;
            }
          }
        }
        if (explosionsQuenched) {
          playerAlmostDead = false;
          playerDead = true;
        }
      }

      explosionsQuenched = false;
      for (int y=Y_DIMEN-2; y>=0; y--) {
        for (int x=0; x<X_DIMEN; x++) {
          botSmash = false;
          rockSmash = false;
          amoebaSmash = false;

          // Is this item smashed by amoeba?
          if (((y-1 > 0) && (levelMap[x][y-1] == 'A')) ||
              ((x+1 < X_DIMEN) && (levelMap[x+1][y] == 'A')) ||
              ((y+1 < Y_DIMEN) && (levelMap[x][y+1] == 'A')) ||
              ((x-1 > 0) && (levelMap[x-1][y] == 'A')) &&
              ((levelMap[x][y] == 'B') || (levelMap[x][y] == 'F'))) {
            amoebaSmash = true;
            smashCoord = new Coord(x,y);
          }

          // Quench all explosions
          if (levelMap[x][y] == '*') {
            levelMap[x][y] = '.';
            redraw(new Coord(x,y));
            explosionsQuenched = true;
          } else

    // If, for any reason, the player is dead, end Game
          // Do this AFTER the explosions take place
          if (playerDead) {

            //give a little pause before returning
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
            }

            parent.finish();//kill aktivity and return to menu.
            
          }

          // Is the player standing next to a bot?
          if (levelMap[x][y] == 'P') {
              if (((y-1 > 0) && ((levelMap[x][y-1] == 'B') || (levelMap[x][y-1] == 'F'))) ||
                  ((x+1 < X_DIMEN) && ((levelMap[x+1][y] == 'B') || (levelMap[x+1][y] == 'F'))) ||
                  ((y+1 < Y_DIMEN) && ((levelMap[x][y+1] == 'B') || (levelMap[x][y+1] == 'F'))) ||
                  ((x-1 > 0) && ((levelMap[x-1][y] == 'B') || (levelMap[x-1][y] == 'F')))) {
                  botSmash = true;
                  smashCoord = new Coord(x,y);
                  playerAlmostDead = true;
                  
              }
          } else

          // Work with rocks and diamonds
          if ((levelMap[x][y] == 'O') || (levelMap[x][y] == '+') || (amoebaSmash || botSmash)) {
            if (levelMap[x][y] == 'O') {
                me = 'O';
            } else
            if (levelMap[x][y] == '+') {
                me = '+';
            } else {
                me = '?';
            }

            // Gravity first...
            if ((me != '?') && (levelMap[x][y+1] == '.')) {
              levelMap[x][y+1] = me;
              levelMap[x][y] = '.';
              redraw(new Coord(x,y));
              redraw(new Coord(x,y+1));
               //parent.playSound(R.raw.boing);
              if ((y+2 < Y_DIMEN) && ((levelMap[x][y+2] == 'B') || (levelMap[x][y+2] == 'F') || (levelMap[x][y+2] == 'P'))) {
                rockSmash = true;
                smashCoord = new Coord(x,y+2);
              }
            } else

            // ... then handle stack collapsing
            if ((me != '?') && ((levelMap[x][y+1] == 'O') || (levelMap[x][y+1] == '+'))) {
                if ((levelMap[x+1][y+1] == '.') && (levelMap[x+1][y] == '.')) {
                    levelMap[x+1][y+1] = me;
                    levelMap[x][y] = '.';
                    redraw(new Coord(x,y));
                    redraw(new Coord(x+1,y+1));
                    if ((y+2 < Y_DIMEN) && ((levelMap[x+1][y+2] == 'B') && (levelMap[x+1][y+2] == 'F') || (levelMap[x+1][y+2] == 'P'))) {
                      rockSmash = true;
                      smashCoord = new Coord(x+1,y+2);
                    }
                } else
                if ((levelMap[x-1][y+1] == '.') && (levelMap[x-1][y] == '.')) {
                    levelMap[x-1][y+1] = me;
                    levelMap[x][y] = '.';
                    redraw(new Coord(x,y));
                    redraw(new Coord(x-1,y+1));
                    if ((y+2 < Y_DIMEN) && ((levelMap[x-1][y+2] == 'B') && (levelMap[x-1][y+2] == 'F') || (levelMap[x-1][y+2] == 'P'))) {
                      rockSmash = true;
                      smashCoord = new Coord(x-1,y+2);
                    }
                }
            }
          }

            // Smash a butterfly, fly, or player
label1:     if ((botSmash || amoebaSmash || rockSmash) && ((levelMap[smashCoord.x][smashCoord.y] == 'B') || (levelMap[smashCoord.x][smashCoord.y] == 'F') || (levelMap[smashCoord.x][smashCoord.y] == 'P'))) {
              boolean diamond = (levelMap[smashCoord.x][smashCoord.y] == 'B')?true:false;
              if (levelMap[smashCoord.x][smashCoord.y] == 'P') {
			if (botSmash || rockSmash) {
	              playerAlmostDead = true;
                      parent.playSound(R.raw.explosion_sound);
                      Vibrator v = (Vibrator) parent.getSystemService(Context.VIBRATOR_SERVICE);
                      v.vibrate(300);
                      

                  } else break label1;
              }
              for (int i=smashCoord.x-1;i<=smashCoord.x+1;i++) {
                for (int u=smashCoord.y-1;u<=smashCoord.y+1;u++) {
                    if ((levelMap[i][u] == '.') || (levelMap[i][u] == ':') || (levelMap[i][u] == 'W') || (levelMap[i][u] == 'M') || (levelMap[i][u] == '+') || (levelMap[i][u] == 'A')
                        || ((i==smashCoord.x) && (u==smashCoord.y-1)) || ((i==smashCoord.x) && (u==smashCoord.y)) && (i > 0) && (i < X_DIMEN) && (u > 0) && (u < Y_DIMEN)) {
                        if (diamond) {
                            levelMap[i][u]='+';
                        } else {
                            levelMap[i][u]='*';
                        }
                        redraw(new Coord(i,u));
                        for (int b=0; b<bots.size(); b++) {
                            BotPosition bot = (BotPosition)bots.get(b);
                            if ((bot.x == smashCoord.x) && (bot.y == smashCoord.y)) {
                                bots.remove(b);
                            }
                        }
                    }
                }
              }
            }

            // Go into a magic wall
            if ((levelMap[x][y] == 'M') && (y > 1) && ((levelMap[x][y-1] == 'O') || (levelMap[x][y-1] == '+'))) {
              if (levelMap[x][y+1] == '.') {
                if (levelMap[x][y-1] == 'O') {
                  levelMap[x][y+1] = '+';
                  redraw(new Coord(x,y+1));
                } else {
                  levelMap[x][y+1] = 'O';
                  redraw(new Coord(x,y+1));
                }
              }
              levelMap[x][y-1] = '.';
              redraw(new Coord(x,y-1));
            }


          // Grow the amoeba
          if (levelMap[x][y] == 'A') {
             if (timer >= maxtimer) {
                if (Math.random() < 0.10) {
                    int dx=0,dy=0;
                    if (Math.random() < 0.5) {
                        dx = (int)((Math.round(Math.random()*2))-1);
                        dy = 0;
                    } else {
                        dy = (int)((Math.round(Math.random()*2))-1);
                        dx = 0;
                    }
                    // Spread it only to places that are empty or have dirt
                    if ((x+dx < X_DIMEN) && (x+dx > 0) && (y+dy < Y_DIMEN) && (y+dy > 0)) {
                        if ((levelMap[x+dx][y+dy] == '.') || (levelMap[x+dx][y+dy] == ':')) {
                            levelMap[x+dx][y+dy] = 'A';
                            redraw(new Coord(x+dx,y+dy));
                        }
                    }
                }
             }
          }
        } // for
      } // for

      // Move any Fireflies clockwise, and Butterflies counterclockwise
      if (timer >= maxtimer) {
        for (int b = 0; b < bots.size(); b++) {
          BotPosition bot = (BotPosition)bots.get(b);
          Coord c = new Coord(bot.x,bot.y);

          if (bot.heading == UP) {
            if ((bot.direction == UP) && (levelMap[bot.x+1][bot.y] == '.')) {
                bot.x++;
                bot.heading = RIGHT;
            } else
            if ((bot.direction == RIGHT) && (levelMap[bot.x-1][bot.y] == '.')) {
                bot.x--;
                bot.heading = LEFT;
            } else
            if (levelMap[bot.x][bot.y-1] == '.') {
              bot.y--;
            } else {
              bot.heading = DOWN;
            }
          }

          if (bot.heading == RIGHT) {
            if ((bot.direction == UP) && (levelMap[bot.x][bot.y+1] == '.')) {
                bot.y++;
                bot.heading = DOWN;
            } else
            if ((bot.direction == RIGHT) && (levelMap[bot.x][bot.y-1] == '.')) {
                bot.y--;
                bot.heading = UP;
            } else
            if (levelMap[bot.x+1][bot.y] == '.') {
              bot.x++;
            } else {
              bot.heading = LEFT;
            }
          }

          if (bot.heading == DOWN) {
            if ((bot.direction == UP) && (levelMap[bot.x-1][bot.y] == '.')) {
                bot.x--;
                bot.heading = LEFT;
            } else
            if ((bot.direction == RIGHT) && (levelMap[bot.x+1][bot.y] == '.')) {
                bot.x++;
                bot.heading = RIGHT;
            } else
            if (levelMap[bot.x][bot.y+1] == '.') {
              bot.y++;
            } else {
              bot.heading = UP;
            }
          }

          if (bot.heading == LEFT) {
            if ((bot.direction == UP) && (levelMap[bot.x][bot.y-1] == '.')) {
                bot.y--;
                bot.heading = UP;
            } else
            if ((bot.direction == RIGHT) && (levelMap[bot.x][bot.y+1] == '.')) {
                bot.y++;
                bot.heading = DOWN;
            } else
            if (levelMap[bot.x-1][bot.y] == '.') {
              bot.x--;
            } else {
              bot.heading = RIGHT;
            }
          }


          bots.set(b,bot);

          if ((bot.x != c.x) || (bot.y != c.y)) {
               levelMap[c.x][c.y] = '.';
              levelMap[bot.x][bot.y] = (bot.direction==0)?'F':'B';
              redraw(new Coord(c.x,c.y));
              redraw(new Coord(bot.x,bot.y));
              
          }



        }

      } // timer


      // Check if amoeba is suffocated
      boolean amoebaSuffocated = true;
      for (int i=1; i<X_DIMEN-1 && amoebaSuffocated; i++) {
        for (int u=1; u<Y_DIMEN-1 && amoebaSuffocated; u++) {
            if (levelMap[i][u] == 'A') {
                if ((levelMap[i][u-1] == '.') || (levelMap[i][u-1] == ':') ||
                    (levelMap[i-1][u] == '.') || (levelMap[i-1][u] == ':') ||
                    (levelMap[i][u+1] == '.') || (levelMap[i][u+1] == ':') ||
                    (levelMap[i+1][u] == '.') || (levelMap[i+1][u] == ':')) {
                    amoebaSuffocated = false;
                }
            }
        }
      }

      // If the amoeba is suffocated, convert all amoeba into diamonds
      if (amoebaSuffocated) {
          for (int i=1; i<X_DIMEN-1; i++) {
            for (int u=1; u<Y_DIMEN-1; u++) {
                if (levelMap[i][u] == 'A') {
                    levelMap[i][u] = '+';
                    redraw(new Coord(i,u));
                }
            }
          }
      }


      redrawCoords();

      if (timer >= maxtimer) {
        timer = 0;
      }
      timer++;
      try {
        Thread.sleep(150);
      } catch (InterruptedException e) {
        Log.e("RUN()", e.getMessage());
      }
     } // while
  } // run


}

}
class Coord {
    int x,y;

    public Coord() {
        x = 0;
        y = 0;
    }

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }


}

class BotPosition {
    int x,y;
    int heading,direction;

    public BotPosition() {
        x = 0;
        y = 0;
        heading = 1;
        direction = 0;
    }

    public BotPosition(int x, int y, int heading, int direction) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.direction = direction;
    }




}