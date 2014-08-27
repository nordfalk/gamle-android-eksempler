package org.dook.invaders;

import java.util.Random;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;


public class Game extends Activity implements SensorEventListener{
	
	private GameView gameView;
	private Game game = this;			// Reference for the gameView and most game elements
	private int enemyCount = 30;		// Number of enemies to add at a time
    private int enemyShootTimer = 0;    // Used to time enemy shots
    private int score, lives;
    private Random randomizer = new Random();
	private Enemy[] enemies = new Enemy[enemyCount];
    private PlayerBullet[] playerBullets = new PlayerBullet[5];
    private EnemyBullet[] enemyBullets = new EnemyBullet[5];
    private Ship ship;
    private SensorManager sensorManager;
    
    private boolean newGame = true; 	// Used to signal for new placement of game elements
	
    /**
     * An extension of the GraphicsView (SurfaceView) containing the GameThread
     * and central methods, most notably the onUpdate and onDraw methods handling
     * updating all game objects and drawing them on the canvas.
     */
    public class GameView extends GraphicsView {
		Paint color;
		Paint text;
		
		public GameView(Context context) {
			super(context);
		}
		
		/**
		 * Initialize as many fields and objects as possible. Note
		 * that some game elements cannot be initialized here but 
		 * are instead handled the first time the update method
		 * is called.
		 */
		@Override
		public void onInitialize(){
			score = 0;
			lives = 3;
			
			// Init paints - fonts and colors
			color = new Paint();
			text = new Paint();
			text.setTypeface(Typeface.createFromAsset(getAssets(), "aesymatt.ttf"));
			text.setTextSize(30);
			
			// Init bullets
	        for(int i = 0 ; i < 5 ; i++){
	            playerBullets[i] = new PlayerBullet(getResources().getDrawable(R.drawable.shot1));
	            enemyBullets[i] = new EnemyBullet(getResources().getDrawable(R.drawable.shot2));
	        }
	        enemyShootTimer = 0;
	        
	        // Init Enemies
	        for(int i = 0 ; i < enemyCount ; i++){
	            enemies[i] = new Enemy(0,0, getResources().getDrawable(R.drawable.alien3));
	        }
	        
	        // Init ship
	        ship = new Ship(0, 0, getResources().getDrawable(R.drawable.ship));
		}
		
		@Override
		public void onUpdate(){
			/*
			 * First initialize and place elements. Some game elements rely on the
			 * size of the gameView and cannot be initialized in onInitialize as the
			 * gameView has not been fully created yet.
			 */			
			if(newGame){
				spawnEnemies();
				
				ship.moveTo(gameView.getWidth() / 2, gameView.getHeight() - 125);
				ship.setActive(true);
				newGame = false;
			}
						
			ship.update(game);
			
			for(Enemy e : enemies)
				if(e.isActive())
	            	e.update(game);
			
	        for(PlayerBullet b : playerBullets)
	            if(b.isActive())
	                b.update(game);
	        
	        for(EnemyBullet b : enemyBullets)
	            if(b.isActive())
	                b.update(game);
	        
	        // Instruct a random enemy to shoot if the timer is reached
            if(enemyShootTimer > 10){
                int n = randomizer.nextInt(enemyCount);
                if(enemies[n].isActive()){
                    enemyShoot(enemies[n]);
                    enemyShootTimer = 0;
                }
            }
            enemyShootTimer++;
	        
	        // Check if any enemy has reached the edge of the screen
	        checkEdgeHit();
	        
	        // Check if all enemies are dead
	        if(allDead()){
                spawnEnemies();
                addPoints(10);
            }
	        
	        // Check for lives lost
	        if(lives <= 0){
	        	endGame();
	        }
		}
		
		@Override
		public void onDraw(Canvas canvas){
			// Draw background
			color.setColor(getResources().getColor(R.color.game_background));
			canvas.drawRect(0, 0, getWidth(), getHeight(), color);
			
			// Draw Enemies
            for(Enemy e : enemies){
                if(e.isActive())
                    e.draw(canvas);
            }
            
            // Draw bullets
            for(PlayerBullet b : playerBullets)
                if(b.isActive())
                    b.draw(canvas);
            for(EnemyBullet b : enemyBullets)
                if(b.isActive())
                    b.draw(canvas);
                    
            // Draw Ship
            ship.draw(canvas);
            
            // Draw GUI - points, lives etc.
            text.setColor(getResources().getColor(R.color.text_background));
            canvas.drawText("Points: " + score, 12, 25, text);
            canvas.drawText("Points: " + score, 12, 28, text);
            canvas.drawText("Points: " + score, 10, 28, text);
            canvas.drawText("Lives: " + lives, 12, 55, text);
            canvas.drawText("Lives: " + lives, 12, 58, text);
            canvas.drawText("Lives: " + lives, 10, 58, text);
            text.setColor(getResources().getColor(R.color.text_foreground));
            canvas.drawText("Points: " + score, 10, 25, text);
            canvas.drawText("Lives: " + lives, 10, 55, text);
                        
            // Draw the slider at the bottom, empty space for touch input
			color.setColor(getResources().getColor(R.color.slider_background));
			canvas.drawRect(0, getHeight() - 100, getWidth(), getHeight(), color);
			color.setColor(getResources().getColor(R.color.slider_edge));
			canvas.drawLine(0, getHeight() - 100, getWidth(), getHeight() - 100, color);
		}
		
	}
    
    @Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
				
		gameView = new GameView(this);
		setContentView(gameView);
				
		setVolumeControlStream(AudioManager.STREAM_MUSIC);		
	}
	
	@Override
	public void onResume(){	
		super.onResume();
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor orienteringsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(this, orienteringsSensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		sensorManager.unregisterListener(this);
	}
	
	/**
     * Adds five rows of enemies to the top of the screen. Called at games start
     * and whenever the player has shot all the enemies.
     */
    private void spawnEnemies(){
    	Log.d("Invaders", "spawnEnemies");
    	
        int e = 0;
        for (int x = 0 ; x < enemyCount / 5 ; x++){
            for (int y = 0 ; y < 5 ; y++){
                enemies[e].moveTo(gameView.getWidth() / (enemyCount/5) * x + 20, y * 48 + 25);
                enemies[e].setSpeed(1, 0);
                enemies[e].setActive(true);
                e++;

                if(e > enemyCount)
                	return;
            }
        }
    }
    
    /**
     * Checks if any of the active enemy objects have reached the edge of
     * the screen and instructs them all to turn around.
     */
    public void checkEdgeHit(){
        for(Enemy e : enemies){
            if (e.atEdge() && e.isActive()){
                for(Enemy f : enemies){
                    f.uTurn();
                }
                return;
            }
        }
    }
    
    /**
     * Checks if all enemies are dead.
     *
     * @return True if all enemies are dead.
     */
    public boolean allDead(){
        for(Enemy e : enemies){
            if(e.isActive())
                return false;
        }
        return true;
    }
    
    /**
     * Attempts to add a player bullet to the game. Only five bullets can be
     * active at any time.
     */
    public void playerShoot(){
        for(PlayerBullet b : playerBullets){
            if(!b.isActive()){
                b.moveTo(ship.getX() + ship.bounds.width() / 2 - 1, ship.getY());
                b.setActive(true);               
                return;
            }
        }
    }
    
    /**
     * Attempts to add an enemy bullet to the game.
     * 
     * @param ent the enemy to add the bullet by
     */
    public void enemyShoot(Entity ent){
        for(EnemyBullet b : enemyBullets){
            if(!b.isActive()){
                b.moveTo(ent.getX(), ent.getY());
                b.setActive(true);
                return;
            }
        }
    }
	
	public void addPoints(int points){
		score += points;
	}
	
	public void takeLife(int lives){
		this.lives -= lives;
	}
	
	/**
	 * Called when the player has no lives left. Checks if a high score
	 * was made and updates the preferences. A child activity is called
	 * to display a game over dialog and final score to the player.
	 */
	public void endGame(){
		// start the dialog activity
		Intent i = new Intent(game, CustomDialog.class);
		i.putExtra("highscore", saveHighscore());
		i.putExtra("score", score);
		startActivityForResult(i, 0);
		// stop calling update in the thread
		gameView.thread.setRunning(false);
	}
	
	/**
	 * Checks if the player made the high score and updates the list appropriately. 
	 * 
	 * @return true if a high score was made
	 */
	public boolean saveHighscore(){
		SharedPreferences highscore = getSharedPreferences("highscore", 0);
        SharedPreferences.Editor editor = highscore.edit();
        
        int place = 0;
        
        for(int n = 0 ; n < 10 ; n++){
        	if(score > highscore.getInt("score" + n, 0)){
        		//If the current score is higher than a stored highsore push down the remaining values...
        		for(int m = 9 ; m > place ; m--){
        			editor.putString("name" + m, highscore.getString("name" + (m - 1), getString(R.string.no_name)));
        			editor.putInt("score" + m, highscore.getInt("score" + (m - 1), 0));
        		}
        		// ... and put in the new score.
        		editor.putString("name" + place, Prefs.getPlayerName(gameView.getContext()));
        		editor.putInt("score" + place, score);
        		
        		// a highscore was made
        		editor.commit();
        		return true;		
        	}
        	place++;
        }
        
        editor.commit();
		
        // No high score
        return false;
		
	}
	
	/**
	 * 	Whenever a child activity is finished this will be called. In this program
	 *  the only child activity is the game over dialog so we just finish this game 
	 *  activity and return to the menu. 
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		finish();
	}
	
	public Enemy[] getEnemies(){
        return enemies;
    }

    public PlayerBullet[] getPlayerBullets(){
        return playerBullets;
    }

    public EnemyBullet[] getEnemyBullets(){
        return enemyBullets;
    }

    public Ship getShip(){
        return ship;
    }

	public GameView getView(){
		return gameView;
	}
	

	// Input Methods - Keys, touch and sensors -----------------------------------------------
	
	/**
	 * Handles key input. Depending on settings in the game preferences
	 * the player shoots if the right key is pressed. 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		switch(keyCode){
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if(Prefs.getShootSettings(gameView.getContext()).equals("dpad"))
				playerShoot();
			break;
		case KeyEvent.KEYCODE_MENU:
			if(Prefs.getShootSettings(gameView.getContext()).equals("menu"))
				playerShoot();
			break;
		case KeyEvent.KEYCODE_VOLUME_UP:
			if(Prefs.getShootSettings(gameView.getContext()).equals("volume"))
				playerShoot();
			break;
		}
		
		/*
		 * Call super to allow other key events (such as the back button)
		 * If the volume button is chosen don't pass that on, however, as
		 * the player will shoot AND turn the volume up...
		 */
		return (keyCode == KeyEvent.KEYCODE_VOLUME_UP ? true : super.onKeyDown(keyCode, event));
	}
	
	/**
	 * Handles touch input. Very simple here as it simple places the ship
	 * at same x position as the touch. 
	 * 
	 * Consider having the ship move towards the point instead so it can't
	 * move instantly from one place to another... 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(Prefs.getMoveSettings(gameView.getContext()).equals("touch")) {
			ship.moveTo((int) event.getX(), ship.getY());
		}
						
	    return super.onTouchEvent(event);
	}
	
	/**
	 * Handles sensor changes. Only used for orientation: if the phone is
	 * tilted from side to side and only if the move preference "tilt" is
	 * chosen.
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensortype = event.sensor.getType();
		if (Prefs.getMoveSettings(gameView.getContext()).equals("tilt")){
			if (sensortype == Sensor.TYPE_ORIENTATION)  {
				if(event.values[2] < -3)
					ship.setSpeed((event.values[2] < -10 ? 5 : 2), 0);
				else if(event.values[2] > 3)
					ship.setSpeed((event.values[2] > 10 ? -5 : -2), 0);
				else
					ship.setSpeed(0, 0);
			}
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Not used
	}
	
	
	
}
