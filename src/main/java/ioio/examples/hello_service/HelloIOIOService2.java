package ioio.examples.hello_service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import ioio.examples.hello_service.GuitarActivity.CordManager;
import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;

/**
 * An example IOIO service. While this service is alive, it will attempt to
 * connect to a IOIO and blink the LED. A notification will appear on the
 * notification bar, enabling the user to stop the service.
 */
public class HelloIOIOService2 extends IOIOService {
    private String LOG_TAG = null;

    private int[] analogPinsArry = {31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42};
    private int[] digitalsPinsArry = {16, 15, 14, 13, 12, 11}; //{22, 21, 20, 19, 18, 17,

    private AnalogInput[] analogInputObjects = new AnalogInput[12];
    private DigitalOutput[] digitalOutputObjects = new DigitalOutput[12];

    private float[] SensorValue = new float[12];
    private PressureBuffer[] pressureBuffer;

    private class PressureBuffer {
        private boolean valueReturned = true;
        private float lastValue = 0f;

        public boolean isValueReturned() {
            return valueReturned;
        }

        public void setValueReturned(boolean valueReturned) {
            this.valueReturned = valueReturned;
        }

        public float getLastValue() {
            return lastValue;
        }

        public void setLastValue(float lastValue) {
            this.lastValue = lastValue;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LOG_TAG = this.getClass().getSimpleName();
        Log.i(LOG_TAG, "In onCreate");
    }

    @Override
    protected IOIOLooper createIOIOLooper() {
        return new BaseIOIOLooper() {
            public int lastX = -1;
//            public float lastPress = 0f;

            @Override
            protected void setup() throws ConnectionLostException,
                    InterruptedException {

                pressureBuffer = new PressureBuffer[CordManager.NUM_OF_MEITARS];
                for (int i = 0; i < CordManager.NUM_OF_MEITARS; i++) {
                    pressureBuffer[i] = new PressureBuffer();
                }
                for (int k = 5; k >= 0; k--) {
                    digitalOutputObjects[k] = ioio_.openDigitalOutput(digitalsPinsArry[k]);
                }

                for (int j = 0; j <= 11; j++) {
                    analogInputObjects[j] = ioio_.openAnalogInput(analogPinsArry[j]);
                }

//                for(int i = 0; i <= 5; i++){
//                    z[i] = new ArrayList<Float>();
//                }

            }

            @Override
            public void loop() throws ConnectionLostException, InterruptedException {

                Intent broadcastIntent = new Intent();
                // i - x, n - y
                for (int i = 0; i <= 5; i++) {
                    digitalOutputObjects[i].write(true);

                    for (int n = 11; n >= 0; n--) {
                        SensorValue[n] = analogInputObjects[n].read();
//                        if (i >= 0 && i <= 5) {
                        if (SensorValue[n] > 0.01) {
                            //                            String msg = "n: " + n + "  i: " + i + "    sen:   " + SensorValue[n];
                            Log.e("Meitar: ", i + "  sarig :" + n + " ---" + SensorValue[n]);
                            broadcastIntent.putExtra("meitar" + i, SensorValue[n]);
                            broadcastIntent.putExtra("srigim" + i, n);
                            broadcastIntent.putExtra("Velbridge" + i, playBridge2(pressureBuffer[i], SensorValue[n]));
                        } else {
                            playBridge2(pressureBuffer[i], 0);
                        }
                        pressureBuffer[i].setLastValue(SensorValue[n]);
//                        }
                        Thread.sleep(1);
                        SensorValue[n] = 0;
                    }
                    digitalOutputObjects[i].write(false);
                }
                broadcastIntent.setAction(MainActivity.mBroadcastStringAction);
                sendBroadcast(broadcastIntent);

            }

            public float playBridge2(PressureBuffer buffer, float press) {
//                Log.e("p", "press:  " + press +"    lastPress:  "+ lastPress);
//                if (press > lastPress + 0.1){
//                    lastPress = press;
//                    return press;
//                }
//                else{
//                    lastPress = press;
//                    return 0;
//                }
                if (!buffer.isValueReturned() && press == 0) {
                    Log.e("1", "press:  " + press);
                    buffer.setValueReturned(true);
                } else if (press > 0.3 + buffer.getLastValue() && buffer.isValueReturned()) {
                    Log.e("2", "press:  " + press);
                    buffer.setValueReturned(false);
                    return press;
                }
                return 0;
            }


//            public float playBridge(int meitar, int sarig, float pressure) {
//                int counter = 0;
//
//                // if the pressure is in the same x and y collact the z
//                if (lastX == sarig) {
//                    if (lastPress > pressure + 0.00001) {
//                        lastPress = 0f;
////                        Log.e("big press","2");
//                        lastX = -1;
//                        return 1f;
//                    }
//                    if (lastPress > pressure + 0.0001) {
////                        Log.e("small press","3");
//                        counter++;
//                    } else {
////                        Log.e("not enough press","4");
//                        counter = 0;
//                    }
//
//                    if (counter == 5) {
////                        Log.e("counter at 5","5");
//                        lastPress = 0f;
//                        lastX = -1;
//                        return pressure;
//                    }
//                    lastPress = pressure;
//                    lastX = sarig;
//                }
//                else {
////                    Log.e("different sarig","6");
//                    lastPress = 0f;
//                    lastX = sarig;
//                }
//                Log.e("r","counter: "+Integer.toString(counter)+" lastPress: "+Float.toString(lastPress) +
//                " press: " + Float.toString(pressure) + " lastX: " + Integer.toString(lastX)+ " sarig: " + Integer.toString(sarig));
//                return 0;
//            }

//                // if z have 5 values check the time
//                if (z[meitar].size() == 5) {
//                    velo = 0f;
//                    for (int i=1;i<=4;i++){
//                        velo += z[meitar].indexOf(i);
//                    }
//                    velo = (float)velo / 5;
//                }
//                velo = (float)(velo / 0.5);
//                return (velo);
//                }
//                return 0f; //stam
//            }
        };

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        return result;


    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(LOG_TAG, "In onBind");
        return null;
    }
}