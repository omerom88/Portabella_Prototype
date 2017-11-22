//package com.portabella.app.Hardware;
//
//import android.content.Intent;
//import android.os.IBinder;
//import android.widget.TextView;
//
//import com.portabella.app.MainActivity;
//
//import ioio.lib.api.AnalogInput;
//import ioio.lib.api.DigitalOutput;
//import ioio.lib.api.exception.ConnectionLostException;
//import ioio.lib.util.BaseIOIOLooper;
//import ioio.lib.util.IOIOLooper;
//import ioio.lib.util.android.IOIOService;
//
////import com.portabella.app.GuitarActivity.CordManager;
//
///**
// * An example IOIO service. While this service is alive, it will attempt to
// * connect to a IOIO and blink the LED. A notification will appear on the
// * notification bar, enabling the user to stop the service.
// */
//public class HelloIOIOService2 extends IOIOService {
//
//    private int[] analogPinsArry = {31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42};
//    private int[] digitalsPinsArry = {11,13,10,7,4,14}; //{22, 21, 20, 19, 18, 17,
//
//    private TextView tx;
//    private AnalogInput[] analogInputObjects = new AnalogInput[12];
//    private DigitalOutput[] digitalOutputObjects = new DigitalOutput[6];
//
//    private float[] SensorValue = new float[12];
//    private PressureBuffer[] pressureBuffer;
//
//    private class PressureBuffer {
//        private boolean valueReturned = true;
//        private float lastValue = 0f;
//
//        public boolean isValueReturned() {
//            return valueReturned;
//        }
//
//        public void setValueReturned(boolean valueReturned) {
//            this.valueReturned = valueReturned;
//        }
//
//        public float getLastValue() {
//            return lastValue;
//        }
//
//        public void setLastValue(float lastValue) {
//            this.lastValue = lastValue;
//        }
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Override
//    protected IOIOLooper createIOIOLooper() {
//        return new BaseIOIOLooper() {
//            public int lastX = -1;
////            public float lastPress = 0f;
//
//            @Override
//            protected void setup() throws ConnectionLostException,
//                    InterruptedException {
//
////                pressureBuffer = new PressureBuffer[CordManager.NUM_OF_MEITARS];
////                for (int i = 0; i < CordManager.NUM_OF_MEITARS; i++) {
////                    pressureBuffer[i] = new PressureBuffer();
////                }
//                for (int k = 5; k >= 0; k--) {
//                    digitalOutputObjects[k] = ioio_.openDigitalOutput(digitalsPinsArry[k]);
//                }
//
//                for (int j = 0; j <= 11; j++) {
//                    analogInputObjects[j] = ioio_.openAnalogInput(analogPinsArry[j]);
//                }
//
////                for(int i = 0; i <= 5; i++){
////                    z[i] = new ArrayList<Float>();
////                }
//
//            }
//
//            @Override
//            public void loop() throws ConnectionLostException, InterruptedException {
//                Intent broadcastIntent = new Intent();
//                // i - x, n - y
//                for (int i = 0; i <= 5; i++) {
//                    digitalOutputObjects[i].write(true);
//
//                    for (int n = 0; n <= 11; n++) {
//                        SensorValue[n] = analogInputObjects[n].read();
////                        if (i >= 0 && i <= 5) {
//                        if (SensorValue[n] > 0.08) {
//                            SensorValue[n] = SensorValue[n] + (float)(i*0.04);
//                            broadcastIntent.putExtra("meitar" + i, SensorValue[n]);
//                            broadcastIntent.putExtra("srigim" + i, n);
////                            broadcastIntent.putExtra("Velbridge" + i, playBridge2(pressureBuffer[i], SensorValue[n]));
//                        } else {
//                            //playBridge2(pressureBuffer[i], 0);
//                        }
////                        pressureBuffer[i].setLastValue(SensorValue[n]);
////                        }
//                        Thread.sleep(1);
//                        SensorValue[n] = 0;
//                    }
//                    digitalOutputObjects[i].write(false);
//                }
//
//                broadcastIntent.setAction(MainActivity.mBroadcastStringAction);
//
//                sendBroadcast(broadcastIntent);
//
//            }
//
//            public float playBridge2(PressureBuffer buffer, float press) {
//                if (!buffer.isValueReturned() && press == 0) {
//                    buffer.setValueReturned(true);
//                } else if (press > 0.2 + buffer.getLastValue() && buffer.isValueReturned()) {
//                    buffer.setValueReturned(false);
//                    return press;
//                }
//                return 0;
//            }
//        };
//
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        int result = super.onStartCommand(intent, flags, startId);
//        return result;
//
//
//    }
//
//    @Override
//    public IBinder onBind(Intent arg0) {
//        return null;
//    }
//}