package ioio.examples.hello_service;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by omerrom on 24/09/16.
 */


public class MetronomActivity extends Activity {

    public static double bpm = 0;
    public static int beats = 0;
    private boolean playPressed = false;
    Metronome metronome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronom);

        Button bmp_70 = (Button)findViewById(R.id.bpm1);
        Button bmp_90 = (Button)findViewById(R.id.bpm2);
        Button bmp_120 = (Button)findViewById(R.id.bpm3);
        Button beats_4 = (Button)findViewById(R.id.beats1);
        Button beats_8 = (Button)findViewById(R.id.beats2);
        Button stop = (Button)findViewById(R.id.stop);
        Button play = (Button)findViewById(R.id.play);

        metronome = new Metronome();

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        bpm = SP.getInt(getString(R.string.MetronomActivity_bpm), 0);
        beats = SP.getInt(getString(R.string.MetronomActivity_beats), 0);
        Log.e("bpm: ", "" + bpm);
        Log.e("beats", "" + beats);
        bmp_70.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bpm = 70;
            }
        });

        bmp_90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bpm = 90;
            }
        });

        bmp_120.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bpm = 120;
            }
        });

        beats_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beats = 4;
            }
        });

        beats_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beats = 8;
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playPressed) {
                    playPressed = false;
                    metronome.stop();
                }

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bpm != 0 && beats != 0) {
                    playPressed = true;
                    playPublic(bpm,beats);
                }
            }
        });

    }

    public void playPublic(final double bpm, final int beats) {
        new Thread(new Runnable() {
            public void run() {
                metronome.setVars(bpm, beats);
                metronome.play();
            }
        }).start();
    }

    @Override
    protected void onStop() {
        setResult(2);
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        setResult(2);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // When the user hits the back button set the resultCode
        // to Activity.RESULT_CANCELED to indicate a failure
        metronome.stop();
        super.onBackPressed();
    }
}
