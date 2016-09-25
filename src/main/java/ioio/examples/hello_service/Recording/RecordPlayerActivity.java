package ioio.examples.hello_service.Recording;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ioio.examples.hello_service.GuitarActivity.GuitarActivity;
import ioio.examples.hello_service.R;

/**
 * Created by Tomer on 02/09/2016.
 */
public class RecordPlayerActivity extends Activity {
    private Button stopButton, pauseButton, playButton;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private TextView tx1,tx2,tx3;
    private boolean mediaPlayerInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_player);

        stopButton = (Button) findViewById(R.id.stopButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        playButton = (Button)findViewById(R.id.playButton);

        tx1=(TextView)findViewById(R.id.textView1);
        tx2=(TextView)findViewById(R.id.textView2);
        tx3=(TextView)findViewById(R.id.textView3);
        File song = GuitarActivity.recordOutput;
        Log.e("song: ", song.length() + "");
        tx3.setText(getIntent().getStringExtra("recordName"));

        mediaPlayer = new MediaPlayer();
        try {
            // resetting mediaplayer instance to evade problems
            mediaPlayer.reset();

            // In case you run into issues with threading consider new instance like:

            // Tried passing path directly, but kept getting
            // "Prepare failed.: status=0x1"
            // so using file descriptor instead
            FileInputStream fis = new FileInputStream(song);
            mediaPlayer.setDataSource(fis.getFD());
            Log.e("getFD: ", fis.getFD() + "");
            mediaPlayer.prepare();
            mediaPlayerInit = true;
        } catch (IOException e) {
            e.printStackTrace();
        }


        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayerInit) {
//                Toast.makeText(getApplicationContext(), "Playing sound",Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();

                    finalTime = mediaPlayer.getDuration();
                    startTime = mediaPlayer.getCurrentPosition();

                    tx2.setText(String.format("%d sec",
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                    );

                    tx1.setText(String.format("%d sec",
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                    );

                    myHandler.postDelayed(UpdateSongTime, 100);
                    stopButton.setEnabled(true);
                    pauseButton.setEnabled(true);
                    playButton.setEnabled(false);
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
                if (mediaPlayerInit) {
                    mediaPlayer.pause();
                    pauseButton.setEnabled(false);
                    playButton.setEnabled(true);
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayerInit) {
//                Toast.makeText(getApplicationContext(), "Stopping sound",Toast.LENGTH_SHORT).show();
                    stopButton.setEnabled(false);
                    pauseButton.setEnabled(false);
                    playButton.setEnabled(true);
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
            }
        });
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if (mediaPlayerInit) {
                startTime = mediaPlayer.getCurrentPosition();
                tx1.setText(String.format("%d sec",
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
                );
                myHandler.postDelayed(this, 100);
            }
        }
    };

    @Override
    public void onBackPressed()
    {
        mediaPlayerInit = false;
        mediaPlayer.stop();
        mediaPlayer.release();
        setResult(2);
        super.onBackPressed();  // optional depending on your needs
    }
}
