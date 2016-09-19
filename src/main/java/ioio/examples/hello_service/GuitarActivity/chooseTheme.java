package ioio.examples.hello_service.GuitarActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

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

        Button rollTheme = (Button)findViewById(R.id.rollBut);
        Button bluesTheme = (Button)findViewById(R.id.bluesBut);
        Button calmTheme = (Button)findViewById(R.id.calmBut);

        rollTheme.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("im here", "roll");
                GuitarActivity.baseGuitarLayout.setBackground(getResources().getDrawable(R.drawable.guitarscreenroll));
                finish();
                return false;
            }
        });

        bluesTheme.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("im here", "blues");
                GuitarActivity.baseGuitarLayout.setBackground(getResources().getDrawable(R.drawable.guitarscreenblues));
                finish();
                return false;
            }
        });

        calmTheme.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("im here", "calm");
                GuitarActivity.baseGuitarLayout.setBackground(getResources().getDrawable(R.drawable.guitarscreenbase));
                finish();
                return false;
            }
        });
    }
}
