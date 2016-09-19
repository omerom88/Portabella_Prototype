package ioio.examples.hello_service.GuitarActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ioio.examples.hello_service.R;

/**
 * Created by omerrom on 18/09/16.
 */
public class ChooseTheme extends Activity {
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        final LinearLayout themeLayout = (LinearLayout)findViewById(R.id.themeLayout);
        Button rollTheme = (Button)findViewById(R.id.rollBut);
        Button bluesTheme = (Button)findViewById(R.id.bluesBut);
        Button calmTheme = (Button)findViewById(R.id.calmBut);

        rollTheme.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("im here", "roll");
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        themeLayout.setBackground(getResources().getDrawable(R.drawable.thememenu003));
                    }
                    case MotionEvent.ACTION_UP: {
                        GuitarActivity.baseGuitarLayout.setBackground(getResources().getDrawable(R.drawable.guitarscreenroll));
                        finish();
                    }
                }
                return false;
            }
        });

        bluesTheme.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("im here", "blues");
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        themeLayout.setBackground(getResources().getDrawable(R.drawable.thememenu002));
                    }
                    case MotionEvent.ACTION_UP: {
                        GuitarActivity.baseGuitarLayout.setBackground(getResources().getDrawable(R.drawable.guitarscreenblues));
                        finish();
                    }
                }
                return false;
            }
        });

        calmTheme.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("im here", "calm");
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        themeLayout.setBackground(getResources().getDrawable(R.drawable.thememenu001));
                    }
                    case MotionEvent.ACTION_UP: {
                        GuitarActivity.baseGuitarLayout.setBackground(getResources().getDrawable(R.drawable.guitarscreenbase));
                        //TODO: DOSN'T WORK - change samples!!!
                        GuitarActivity.NOTES[0] = R.raw.e_string_low;
                        GuitarActivity.NOTES[1] = R.raw.a_string;
                        GuitarActivity.NOTES[2] = R.raw.d_string;
                        GuitarActivity.NOTES[3] = R.raw.g_string;
                        GuitarActivity.NOTES[4] = R.raw.b_string;
                        GuitarActivity.NOTES[5] = R.raw.e_string_hi;
                                //{R.raw.estringlow, R.raw.astring, R.raw.dstring, R.raw.gstring,R.raw.bstring,
                                //R.raw.estringhi};
                        finish();
                    }
                }
                return false;
            }
        });

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
