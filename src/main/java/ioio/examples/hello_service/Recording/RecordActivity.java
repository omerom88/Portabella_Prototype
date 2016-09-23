package ioio.examples.hello_service.Recording;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ioio.examples.hello_service.R;

/**
 * Created by Tomer on 02/09/2016.
 */
public class RecordActivity extends Activity {

    Button start;
    Button pause;
    Button stop;
    EditText duration;
    EditText loops;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        start = (Button)findViewById(R.id.startButton);
        pause = (Button)findViewById(R.id.pauseButton);
        stop = (Button)findViewById(R.id.stopButton);
        duration = (EditText)findViewById(R.id.timeText);
        loops = (EditText)findViewById(R.id.loopNumText);
    }

    public void start(View v) {
//        long d = Long.parseLong(duration.getText().toString());
//        int l = Integer.parseInt(loops.getText().toString());
//        if(d != 0) {
//            Record.getInstance(this).start(d, l);
//        } else {
//            Record.getInstance(this).start();
//        }
//        pause.setText("Pause");
    }

    public void pause(View v) {
//        if (Record.getInstance(this).isRecording()) {
//            Record.getInstance(this).pause();
//            pause.setText("Resume");
//        } else {
//            Record.getInstance(this).resume();
//            pause.setText("Pause");
//        }
    }

    public void stop(View v) {
//        Record.getInstance(this).stop();
    }
}
