package com.portabella.app.MenuFeatures;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.portabella.app.R;

/**
 * Created by omerrom on 25/09/16.
 */
public class SettingActivity extends Activity {

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
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
                changeSettingsInt(getString(R.string.MetronomActivity_bpm), 70);
            }
        });

        bpm_90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSettingsInt(getString(R.string.MetronomActivity_bpm), 90);
            }
        });

        bpm_120.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSettingsInt(getString(R.string.MetronomActivity_bpm), 120);
            }
        });

                        ///////// beats  //////////

        beats_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSettingsInt(getString(R.string.MetronomActivity_beats), 4);
            }
        });

        beats_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSettingsInt(getString(R.string.MetronomActivity_beats), 8);
            }
        });


                        ///////// themes  //////////

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSettingsInt(getString(R.string.GuitarActivity_initNotes), 2);

            }
        });

        blues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSettingsInt(getString(R.string.GuitarActivity_initNotes), 1);
            }
        });


        calm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSettingsInt(getString(R.string.GuitarActivity_initNotes), 0);
            }
        });

                       //////  clean screen    ////

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void changeSettingsInt(String name, int value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(name, value);
        editor.apply();
    }
}
