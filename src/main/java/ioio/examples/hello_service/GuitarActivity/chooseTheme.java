package ioio.examples.hello_service.GuitarActivity;

import android.app.Activity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        Button rollTheme = (Button)findViewById(R.id.rollBut);
        Button bluesTheme = (Button)findViewById(R.id.bluesBut);
        Button calmTheme = (Button)findViewById(R.id.calmBut);

        rollTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTheme(R.drawable.guitarscreenroll, GuitarActivity.ROCK_NOTES);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        bluesTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTheme(R.drawable.guitarscreenblues, GuitarActivity.BLUES_NOTES);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        calmTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTheme(R.drawable.guitarscreenbase, GuitarActivity.REG_NOTES);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

    }

    private void changeTheme(int drawable, int[] notes) {
        GuitarActivity.baseGuitarLayout.setBackground(getResources().getDrawable(drawable));
        CordManager.setNewCords(notes);
    }

    @Override
    public void onBackPressed() {
        // When the user hits the back button set the resultCode
        // to Activity.RESULT_CANCELED to indicate a failure
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}
