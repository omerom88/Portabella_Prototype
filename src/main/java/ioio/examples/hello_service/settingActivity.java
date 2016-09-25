package ioio.examples.hello_service;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by omerrom on 25/09/16.
 */
public class settingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button bpm_70 = (Button)findViewById(R.id.bpm70);
        Button bpm_90 = (Button)findViewById(R.id.bpm90);
        Button bpm_120 = (Button)findViewById(R.id.bpm120);

        Button beats_4 = (Button)findViewById(R.id.beats4);
        Button beats_8 = (Button)findViewById(R.id.beats8);

        Button roll = (Button)findViewById(R.id.setRoll);
        Button blues = (Button)findViewById(R.id.setBlues);
        Button calm = (Button)findViewById(R.id.setCalm);

        Button clear = (Button)findViewById(R.id.cleanScreen);


                     ///////// bpm  //////////

        bpm_70.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MetronomActivity.bpm = 70;
            }
        });

        bpm_90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MetronomActivity.bpm = 90;
            }
        });

        bpm_120.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MetronomActivity.bpm = 120;
            }
        });

                        ///////// beats  //////////

        beats_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MetronomActivity.beats = 4;
            }
        });

        beats_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MetronomActivity.beats = 8;
            }
        });


                        ///////// themes  //////////

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChooseTheme.changeTheme(R.drawable.guitarscreenroll, GuitarActivity.ROCK_NOTES);
            }
        });

        blues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChooseTheme.changeTheme(R.drawable.guitarscreenblues, GuitarActivity.BLUES_NOTES);
            }
        });

        calm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChooseTheme.changeTheme(R.drawable.guitarscreenbase, GuitarActivity.REG_NOTES);
            }
        });


                       //////  clean screen    ////

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
