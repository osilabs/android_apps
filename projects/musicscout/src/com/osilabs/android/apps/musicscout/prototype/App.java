



package com.osilabs.android.apps.musicscout.prototype;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

// import com.example.android.apis.R;

public class App extends Activity {

    private static final String TAG = "MediaPlayerDemo";
    private MediaPlayer mMediaPlayer;
    private static final String MEDIA = "media";
    private static final int LOCAL_AUDIO = 1;
    private static final int STREAM_AUDIO = 2;
    private static final int RESOURCES_AUDIO = 3;
    private static final int LOCAL_VIDEO = 4;
    private static final int STREAM_VIDEO = 5;
    private String path;

    //private TextView tx;

    private ImageView ivPlayButton;
    private TextView tvReview;
    private TextView tvSong;
    private TextView tvDuration;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //tx = new TextView(this);
        //setContentView(tx);
//        Bundle extras = getIntent().getExtras();
        //playAudio(extras.getInt(MEDIA));
       
        tvSong = (TextView) findViewById(R.id.song);
        tvSong.setText(Config.BAND_NAME + " - " + Config.BAND_SONG);

        tvReview = (TextView) findViewById(R.id.review);
        tvReview.setText("(" + Config.BAND_GENRE + ") " + Config.REVIEW);
        
        ivPlayButton= (ImageView) findViewById(R.id.play_button);
        ivPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	playAudio(RESOURCES_AUDIO);
            }
        });

    }

    public void onStart() {
    	super.onStart();
    	
        mMediaPlayer = null;
    	mMediaPlayer = MediaPlayer.create(this, R.raw.track1);
        tvDuration = (TextView) findViewById(R.id.duration);
        tvDuration.setText(Integer.toString(mMediaPlayer.getDuration()));   
    }
    
    private void playAudio(Integer media) {
        try {
            switch (media) {
//                case LOCAL_AUDIO:
//                    /**
//                     * TODO: Set the path variable to a local audio file path.
//                     */
//                    path = "";
//                    if (path == "") {
//                        // Tell the user to provide an audio file URL.
//                        Toast
//                                .makeText(
//                                        App.this,
//                                        "Please edit MediaPlayer_Audio Activity, "
//                                                + "and set the path variable to your audio file path."
//                                                + " Your audio file must be stored on sdcard.",
//                                        Toast.LENGTH_LONG).show();
//
//                    }
//                    mMediaPlayer = new MediaPlayer();
//                    mMediaPlayer.setDataSource(path);
//                    mMediaPlayer.prepare();
//                    mMediaPlayer.start();
//                    break;
                case RESOURCES_AUDIO:
                	
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        //mMediaPlayer.reset(); // Set to un initialized
                    } else {
                        mMediaPlayer.start();
                	}
            }
            //tx.setText("Playing audio...");

        } catch (Exception e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO Auto-generated method stub
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

    }
}