package com.portabella.app.GuitarActivity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by omerrom on 18/09/16.
 */
public class ChooseTheme extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.lib.app.R.layout.activity_theme);
        Button rollTheme = (Button)findViewById(com.lib.app.R.id.rollBut);
        Button bluesTheme = (Button)findViewById(com.lib.app.R.id.bluesBut);
        Button calmTheme = (Button)findViewById(com.lib.app.R.id.calmBut);

        rollTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheme(com.lib.app.R.drawable.guitarscreenroll, GuitarActivity.ROCK_NOTES, getResources());
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        bluesTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheme(com.lib.app.R.drawable.guitarscreenblues, GuitarActivity.BLUES_NOTES, getResources());
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        calmTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheme(com.lib.app.R.drawable.guitarscreenbase, GuitarActivity.REG_NOTES, getResources());
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

    }

    public static void setTheme(int drawable, int[] notes, Resources res) {
        GuitarActivity.baseGuitarLayout.setBackground(res.getDrawable(drawable));
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
