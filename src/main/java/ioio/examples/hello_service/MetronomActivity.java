package ioio.examples.hello_service;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by omerrom on 24/09/16.
 */


public class MetronomActivity extends Activity {

    private double bpm = 0;
    private int beats = 0;
    private boolean bpmPressed = false;
    private boolean beatsPressed = false;
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

        metronome = new Metronome();

        bmp_70.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bpmPressed = true;
                bpm = 70;
                if (bpmPressed && beatsPressed) {
                    playPublic(bpm,beats);
                }
            }
        });

        bmp_90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bpmPressed = true;
                bpm = 90;
                if (bpmPressed && beatsPressed) {
                    playPublic(bpm,beats);
                }
            }
        });

        bmp_120.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bpmPressed = true;
                bpm = 120;
                if (bpmPressed && beatsPressed) {
                    playPublic(bpm,beats);
                }
            }
        });

        beats_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beatsPressed = true;
                beats = 4;
                if (bpmPressed && beatsPressed) {
                    playPublic(bpm,beats);
                }
            }
        });

        beats_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beatsPressed = true;
                beats = 8;
                if (bpmPressed && beatsPressed) {
                    playPublic(bpm,beats);
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bpmPressed && beatsPressed) {
                    bpmPressed = false;
                    beatsPressed = false;
                    metronome.stop();
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
}
