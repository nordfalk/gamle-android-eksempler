/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.andreas.tabvejrny.aktiviteter;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 *
 * @author asc
 */
public class WebTV extends Activity implements
        OnBufferingUpdateListener, OnCompletionListener,
        OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

    private static final String TAG = "MediaPlayerDemo";
    private int mVideoWidth;
    private int mVideoHeight;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    private String path;
    private Bundle extras;
    private static final String MEDIA = "media";
    private static final int LOCAL_AUDIO = 1;
    private static final int STREAM_AUDIO = 2;
    private static final int RESOURCES_AUDIO = 3;
    private static final int LOCAL_VIDEO = 4;
    private static final int STREAM_VIDEO = 5;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;

    /**
     *
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
//        WebView webview = new WebView(this);
//        setContentView(webview);
//        String content = "<!DOCTYPE html PUBLIC &#34;-//W3C//DTD XHTML 1.0 Transitional//EN&#34; &#34;http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd&#34;><html xmlns=&#34;http://www.w3.org/1999/xhtml&#34; >";
//        content += "<html><head><title></title></head><body>";
//        content += "<script language='JavaScript'> if ((navigator.mimeTypes && navigator.mimeTypes['application/x-shockwave-flash']) || (navigator.userAgent && navigator.userAgent.indexOf('MSIE') >= 0))";
//        content += "document.write(&#34;<embed src='http://tv.dmi.dk/595495.swf' type='application/x-shockwave-flash' allowscriptaccess='always' allowfullscreen='true' width='640' height='390' flashvars='token=ebb723ef1026f39f5d12dea18973916f&photo_id=921928&showLogo=0&autoPlay=1&showDescriptions=0&showShare=0'/>&#34;&#59;";
//        content += "else document.write(&#34;<video src='http://tv.dmi.dk/889267/921928/ebb723ef1026f39f5d12dea18973916f/video_medium/vejret-video.mp4' controls autoplay width='640' height='360'></video>&#34;);";
//        content += "</script></body></html>";
//
//        webview.loadData(content, "text/html", "utf-8");
        
//        webview.loadUrl("http://dmi.netrum.dk/test2.htm/");
//        setContentView(R.layout.webtv);
//        mPreview = (SurfaceView) findViewById(R.id.surface);
//        holder = mPreview.getHolder();
//        holder.addCallback(this);
//        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        extras = getIntent().getExtras();


       Intent intent = new Intent(Intent.ACTION_VIEW);
       //intent.setData(Uri.parse("http://dmi.netrum.dk/test2.htm"));
       //intent.setData(Uri.parse("http://tv.dmi.dk/889267/921928/ebb723ef1026f39f5d12dea18973916f/video_medium/vejret-video.mp4"));
       intent.setData(Uri.parse("http://tv.dmi.dk/v.ihtml?token=45754b97cc80a795b5adff14d57806e4&photo_id=1365878&autoPlay=1&fullScreen=0"));
       startActivity(intent);

//        MediaPlayer mp = new MediaPlayer();
//        try {
//            mp.setDataSource("http://commonsware.com/misc/test2.3gp");
//            mp.prepare();
//            mp.start();
//        } catch (IOException ex) {
//            Logger.getLogger(WebTV.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(WebTV.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalStateException ex) {
//            Logger.getLogger(WebTV.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void playVideo(Integer Media) {
        doCleanUp();
        try {

            switch (Media) {
                case LOCAL_VIDEO:
                    /*
                     * TODO: Set the path variable to a local media file path.
                     */
                    path = "";
                    if (path == "") {
                        // Tell the user to provide a media file URL.
                        Toast
                                .makeText(
                                        WebTV.this,
                                        "Please edit MediaPlayerDemo_Video Activity, "
                                                + "and set the path variable to your media file path."
                                                + " Your media file must be stored on sdcard.",
                                        Toast.LENGTH_LONG).show();

                    }
                    break;
                case 2:
                    /*
                     * TODO: Set path variable to progressive streamable mp4 or
                     * 3gpp format URL. Http protocol should be used.
                     * Mediaplayer can only play "progressive streamable
                     * contents" which basically means: 1. the movie atom has to
                     * precede all the media data atoms. 2. The clip has to be
                     * reasonably interleaved.
                     *
                     */
                    path = "http://tv.dmi.dk/889267/921928/ebb723ef1026f39f5d12dea18973916f/video_medium/vejret-video.mp4";
                    if (path == "") {
                        // Tell the user to provide a media file URL.
                        Toast
                                .makeText(
                                        WebTV.this,
                                        "Please edit MediaPlayerDemo_Video Activity,"
                                                + " and set the path variable to your media file URL.",
                                        Toast.LENGTH_LONG).show();

                    }

                    break;


            }

            // Create a new media player and set the listeners
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        } catch (Exception e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
        }
    }

    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
        Log.d(TAG, "onBufferingUpdate percent:" + percent);

    }

    public void onCompletion(MediaPlayer arg0) {
        Log.d(TAG, "onCompletion called");
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.v(TAG, "onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        Log.d(TAG, "onPrepared called");
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
        Log.d(TAG, "surfaceChanged called");

    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        Log.d(TAG, "surfaceDestroyed called");
    }


    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated called");
        playVideo(2);


    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        doCleanUp();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideoPlayback() {
        Log.v(TAG, "startVideoPlayback");
        holder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
    }
}
