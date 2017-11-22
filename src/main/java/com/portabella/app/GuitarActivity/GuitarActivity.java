package com.portabella.app.GuitarActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.CdcAcmSerialDriver;
import com.hoho.android.usbserial.driver.ProbeTable;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.portabella.app.Hardware.MicroReciver;
import com.portabella.app.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//// OAuth client 747262479409-3isjkbgjvp1el2o6pgfrjqt4aarj059m.apps.googleusercontent.com


/**
 * Created by Tomer on 21/08/2016.
 */
public class GuitarActivity extends Activity{

    public static LinearLayout layout,layout2,layout3,layout4,layout5,layout6,father,cordButs;
    public static boolean[]  play = {true,true,true,true,true,true};
    public static SoundPool mPlayer;
    public static float semiTone = 1.0594631f;
    public static int[] soundStartIdList = {0,0,0,0,0,0};
    public static long[] soundDurationList = {0,0,0,0,0,0};
    private int mAudioStreamType;
    private static final String ACTION_USB_ATTACHED  = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private static final String ACTION_USB_DETACHED  = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    public static int[][] cellsMatrix = {{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1},{-1,-1,-1,-1}};
    public static UsbSerialPort port;
    public static UsbManager manager;
    public static UsbSerialDriver driver;
    private SerialInputOutputManager mSerialIoManager;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    public static String inputString = "";
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    public static Button G_but,D_but,C_but,Em_but, etpM;
    public static char lastCord = ' ';
    public static int fatherH,fatherW;
    public BroadcastReceiver bReceiver;
    public static Boolean etpClicked = false;
    public static int etpCounter = 0;
    static FileWriter writer;
    File gpxfile;
    String DateTimeName;
    String currentTime;


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopIoManager();
        if (port != null) {
            try {
                port.close();
            } catch (IOException e) {
                // Ignore.
            }
            port = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bReceiver);
        //** print data to file and send it to us by email
        try {
            writer.append("\n~~~~~~~~~~~~~~~~~~~~~~~ Finish ~~~~~~~~~~~~~~~~~~~~~~~\n");
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            String finishTime = currentDateTimeString.substring(13, 21);
            String[] finishList = finishTime.split(":");
            String[] startList = currentTime.split(":");
            writer.append("Start Playing: ").append(currentTime).append(" , Finish Playing: ").append(finishTime).append("\n");
            int Fhours = Integer.parseInt(finishList[0]);
            int Shours = Integer.parseInt(startList[0]);
            int Fmin = Integer.parseInt(finishList[1]);
            int Smin = Integer.parseInt(startList[1]);
            int startx = Shours * 60 + Smin;
            int endx = Fhours * 60 + Fmin;
            int duration = endx - startx;
            if (duration < 0) {
                duration = duration + 1440;
            }
            writer.append("TOTAL PLAYED: ____ ").append(Integer.toString(duration)).append(" min ").append(" ____");
            writer.flush();
            writer.close();
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"omerom88@gmail.com", "portabella.hq@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, DateTimeName);
            emailIntent.putExtra(Intent.EXTRA_TEXT, "WE LOVE YOU PORTA");
            Uri uri = Uri.fromFile(gpxfile);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(emailIntent, "omerom88@gmail.com"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //** connect to port and comunicate with arduino pro micro
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        ProbeTable customTable = new ProbeTable();
        customTable.addProduct(6991, 37382, CdcAcmSerialDriver.class);
        UsbSerialProber prober = new UsbSerialProber(customTable);
        List<UsbSerialDriver> drivers = prober.findAllDrivers(manager);
        if (drivers.isEmpty()) {
            Toast.makeText(GuitarActivity.this," Driver empty ", Toast.LENGTH_SHORT).show();
            return;
        }
        driver = drivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            Toast.makeText(GuitarActivity.this," no Connection ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!manager.hasPermission(driver.getDevice())) {
            String ACTION_USB_PERMISSION = "com.blecentral.USB_PERMISSION";
            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            manager.requestPermission(driver.getDevice(), mPermissionIntent);
        }
        else{
            port = driver.getPorts().get(0);
            MicroReciver mr = new MicroReciver();
            mr.init();
            onDeviceStateChange();
        }

    }

    /**
     * Helper functions to open connection with arduino
     */
    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }
    private void stopIoManager() {
        if (mSerialIoManager != null) {
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }
    private void startIoManager() {
        if (GuitarActivity.port != null) {
            mSerialIoManager = new SerialInputOutputManager(GuitarActivity.port, mListener);
            mExecutor.submit(mSerialIoManager);
        }
    }

    /**
     * Function to make accorde - for buttons (without bridge) or for E2P mode
     * @param cord - char of accorde need to be played
     */
    private static void checkOtherCord(char cord)
    {
        synchronized (cellsMatrix) {
            if (lastCord != cord) {
                //** init to zero last accorde in case of changing accorde
                switch (lastCord) {
                    case 'G':
                        cellsMatrix[0][2] = 0;
                        cellsMatrix[1][2] = 0;
                        cellsMatrix[4][1] = 0;
                        cellsMatrix[5][2] = 0;
                        if (G_but.getVisibility() == View.VISIBLE){
                            G_but.setBackgroundColor(Color.parseColor("#00ffffff"));
                        }
                        break;
                    case 'C':
                        cellsMatrix[1][0] = 0;
                        cellsMatrix[3][1] = 0;
                        cellsMatrix[4][2] = 0;
                        if (C_but.getVisibility() == View.VISIBLE) {
                            C_but.setBackgroundColor(Color.parseColor("#00ffffff"));
                        }
                        break;
                    case 'D':
                        cellsMatrix[0][1] = 0;
                        cellsMatrix[1][2] = 0;
                        cellsMatrix[2][1] = 0;
                        if (D_but.getVisibility() == View.VISIBLE) {
                            D_but.setBackgroundColor(Color.parseColor("#00ffffff"));
                        }
                        break;
                    case 'E':
                        cellsMatrix[3][1] = 0;
                        cellsMatrix[4][1] = 0;
                        if (Em_but.getVisibility() == View.VISIBLE) {
                            Em_but.setBackgroundColor(Color.parseColor("#00ffffff"));
                        }
                        break;
                    default:
                        cellsMatrix[0][2] = 0;
                        cellsMatrix[1][2] = 0;
                        cellsMatrix[4][1] = 0;
                        cellsMatrix[5][2] = 0;

                        cellsMatrix[1][0] = 0;
                        cellsMatrix[3][1] = 0;
                        cellsMatrix[4][2] = 0;

                        cellsMatrix[0][1] = 0;
                        cellsMatrix[1][2] = 0;
                        cellsMatrix[2][1] = 0;

                        cellsMatrix[3][1] = 0;
                        cellsMatrix[4][1] = 0;

                }
            }
            //** init to asking corde to 2 in matrix
            switch (cord) {
                case 'G':
                    //2,6,17,22
                    cellsMatrix[0][2] = 2;
                    cellsMatrix[1][2] = 2;
                    cellsMatrix[4][1] = 2;
                    cellsMatrix[5][2] = 2;
                    break;
                case 'C':
                    //4,13,18
                    cellsMatrix[1][0] = 2;
                    cellsMatrix[3][1] = 2;
                    cellsMatrix[4][2] = 2;
                    break;
                case 'D':
                    //1,6,9
                    cellsMatrix[0][1] = 2;
                    cellsMatrix[1][2] = 2;
                    cellsMatrix[2][1] = 2;
                    break;
                case 'E':
                    //13,17
                    cellsMatrix[3][1] = 2;
                    cellsMatrix[4][1] = 2;
                    break;
                default:
            }
        }
    }

    /**
     * Function to print the blockig matrix
     * @return
     */
    public String printMat(){
        String temp = "";
        for (int i = 0; i < cellsMatrix.length; i++) {
            for (int j = 0; j < cellsMatrix[i].length; j++) {
                temp += (cellsMatrix[i][j] + " ");
            }
            temp += "\n";
        }
        return temp;
    }

    /**
     * Get device name for sending email - data collection
     * @return
     */
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
    public static void writeDataToAnalyze(String txt)
    {
        try {
            writer.append(DateFormat.getDateTimeInstance().format(new Date())).append(": ").append(txt).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guitar_layout);

        G_but = (Button) findViewById(R.id.G_button);
        C_but = (Button) findViewById(R.id.C_button);
        D_but = (Button) findViewById(R.id.D_button);
        Em_but = (Button) findViewById(R.id.Em_button);
        father = (LinearLayout) findViewById(R.id.laydp);
        layout6 = (LinearLayout) findViewById(R.id.linearLayoutdp);
        layout5 = (LinearLayout) findViewById(R.id.linearLayout2dp);
        layout4 = (LinearLayout) findViewById(R.id.linearLayout3dp);
        layout3 = (LinearLayout) findViewById(R.id.linearLayout4dp);
        layout2 = (LinearLayout) findViewById(R.id.linearLayout5dp);
        layout = (LinearLayout) findViewById(R.id.linearLayout6dp);
        cordButs = (LinearLayout) findViewById(R.id.lay_butdp);
        fatherH = father.getLayoutParams().height;
        fatherW = father.getLayoutParams().width;
        etpM = (Button)findViewById(R.id.ETPbutton);

        //** open file for data analyzer
        try {
            String myDevice = getDeviceName();
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            DateTimeName = currentDateTimeString + " " + myDevice;
            currentTime = currentDateTimeString.substring(13, 21);
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            gpxfile = new File(root, DateTimeName + ".txt");
            writer = new FileWriter(gpxfile);
            writer.append("~~~~~~~~~~~~~~~~~~~~~~~~~ ").append(DateTimeName).append(" ~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //**  check arduino connection - if connect remove buttons and change backround
        if (ACTION_USB_ATTACHED.equalsIgnoreCase(getIntent().getAction())) {
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.guitarLayout);
            rl.setBackgroundResource(R.drawable.realisticscreen);
            cordButs.setVisibility(View.GONE);
            G_but.setVisibility(View.GONE);
            C_but.setVisibility(View.GONE);
            D_but.setVisibility(View.GONE);
            Em_but.setVisibility(View.GONE);
            father.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            father.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            checkOtherCord(' ');
        }

        //** open reciver to get data from arduino
        bReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ////TODO: check if this is the right place and waht the last line means?
//                Toast.makeText(GuitarActivity.this," attached??? ", Toast.LENGTH_SHORT).show();
                String action = intent.getAction();
                if (action.equalsIgnoreCase(ACTION_USB_DETACHED)) {
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.guitarLayout);
                    rl.setBackgroundResource(R.drawable.realscreen4);
                    cordButs.setVisibility(View.VISIBLE);
                    G_but.setVisibility(View.VISIBLE);
                    C_but.setVisibility(View.VISIBLE);
                    D_but.setVisibility(View.VISIBLE);
                    Em_but.setVisibility(View.VISIBLE);
                    father.getLayoutParams().height = fatherH;
                    father.getLayoutParams().width = fatherW;

                }
                if (action.equalsIgnoreCase(ACTION_USB_ATTACHED)) {
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.guitarLayout);
                    rl.setBackgroundResource(R.drawable.realisticscreen);
                    cordButs.setVisibility(View.GONE);
                    G_but.setVisibility(View.GONE);
                    C_but.setVisibility(View.GONE);
                    D_but.setVisibility(View.GONE);
                    Em_but.setVisibility(View.GONE);
                    father.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    father.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    checkOtherCord(' ');

                }
            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_ATTACHED);
        filter.addAction(ACTION_USB_DETACHED);
        registerReceiver(bReceiver, filter);


        //** buttons as cords
        G_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOtherCord('E');
                cordButs.setBackgroundResource(R.drawable.em);
                lastCord = 'E';
                writeDataToAnalyze("E_button");

            }
        });
        C_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOtherCord('D');
                cordButs.setBackgroundResource(R.drawable.d);
                lastCord = 'D';
                writeDataToAnalyze("D_button");
            }
        });
        D_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOtherCord('C');
                cordButs.setBackgroundResource(R.drawable.c);
                lastCord = 'C';
                writeDataToAnalyze("C_button");
            }
        });
        Em_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOtherCord('G');
                cordButs.setBackgroundResource(R.drawable.g);
                lastCord = 'G';
                writeDataToAnalyze("G_button");
            }
        });
        etpM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etpCounter++;
                if (etpCounter%2 == 1) {
                    etpClicked = true;
                    etpM.setBackgroundColor(Color.argb(170,189,141,143));
                }
                else {
                    etpClicked = false;
                    etpM.setBackgroundColor(Color.argb(167,149,157,191));
                }
                writeDataToAnalyze("E2P_mode_button");
            }
        });


        //**   soundpool init
        mAudioStreamType = AudioManager.STREAM_SYSTEM;
        if (mPlayer == null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mPlayer = new SoundPool.Builder()
                        .setMaxStreams(12)
                        .setAudioAttributes(
                                new AudioAttributes.Builder()
                                        .setLegacyStreamType(mAudioStreamType)
                                        .setContentType(
                                                AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                        .build()).build();
            } else {
                mPlayer = new SoundPool(12, mAudioStreamType, 0);
            }
            soundStartIdList[0] = mPlayer.load(this, R.raw.estringlowo, 1);
            soundDurationList[0] = getSoundDuration(R.raw.estringlowo);
            soundStartIdList[1] = mPlayer.load(this, R.raw.astringo, 1);
            soundDurationList[1] = getSoundDuration(R.raw.astringo);
            soundStartIdList[2] = mPlayer.load(this, R.raw.dstringo, 1);
            soundDurationList[2] = getSoundDuration(R.raw.dstringo);
            soundStartIdList[3] = mPlayer.load(this, R.raw.gstringo, 1);
            soundDurationList[3] = getSoundDuration(R.raw.gstringo);
            soundStartIdList[4] = mPlayer.load(this, R.raw.bstringo, 1);
            soundDurationList[4] = getSoundDuration(R.raw.bstringo);
            soundStartIdList[5] = mPlayer.load(this, R.raw.estringhio, 1);
            soundDurationList[5] = getSoundDuration(R.raw.estringhio);
        }

        //** adding swipe detect to layout
        View.OnTouchListener mOnTouchListener2 = new swipeDetect_2();
        father.setOnTouchListener(mOnTouchListener2);

    }


    //** arduino listener
    public final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Toast.makeText(GuitarActivity.this," Runner stopped ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNewData(final byte[] data) {
                    threadPoolExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            updateReceivedData(data);
                        }
                    });
                }
            };

    //** arduino recive data
    public static void updateReceivedData(byte[] data) {
        try {
            inputString = new String(data);
            if (inputString.length() > 1) {
                List<String> items = Arrays.asList(inputString.split("[\\s,]+"));
                for (int i = 0; i < items.size(); i++) {
                    //** take every input, cut to number and action
                    String cell = items.get(i);
                    String numString = cell.substring(0, 2);
                    String action = cell.substring(2, 3);
                    int cellNumInt = Integer.parseInt(numString);
                    //** calc the row and col in matrix
                    int row = cellNumInt / 4;
                    int col = cellNumInt % 4;
                    synchronized (cellsMatrix) {
                        //** case of relese
                        if (action.equals("0")) {
                            //** case of bridge conncected
                            if (!etpClicked) {
                                writeDataToAnalyze("r_" + row + "_" + col + "");
                                cellsMatrix[row][col] = 0;
                                boolean found = false;
                                //** in relese - check if there other block on meitar
                                for (int j = 3; j >= 0; j--) {
                                    if (cellsMatrix[row][j] == 2) { //TODO: what about 1
                                        if (j < col) {
                                            mPlayer.setRate(swipeDetect_2.meitarPlayMap.get(5 - row), (float) Math.pow(semiTone, j + 1));
                                            found = true;
                                            break;
                                        } else {
                                            found = true;
                                            break;
                                        }
                                    }
                                }
                                //** if didnt find any other block - set rate to zero
                                if (!found) {
                                    mPlayer.setRate(swipeDetect_2.meitarPlayMap.get(5 - row), (float) Math.pow(semiTone, 0));
                                }
                            }
                            //** case of E2P mode - send to accorde function (like buttons)
                            else{
                                writeDataToAnalyze("r_" + "E2P_" + row+"_" + col +"");
                                if (row == 0) {
                                    cellsMatrix[row][col] = 0;
                                    for (int j = 3; j >= 0; j--) {
                                        if (cellsMatrix[row][j] == 2) {
                                            switch (j) {
                                                case 0:
                                                    checkOtherCord('G');
                                                    lastCord = 'G';
                                                    break;
                                                case 1:
                                                    checkOtherCord('C');
                                                    lastCord = 'C';
                                                    break;
                                                case 2:
                                                    checkOtherCord('D');
                                                    lastCord = 'D';
                                                    break;
                                                case 3:
                                                    checkOtherCord('E');
                                                    lastCord = 'E';
                                                    break;
                                                default:
                                                    lastCord = ' ';
                                                    checkOtherCord('N');
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //** case of full block
                        else if (action.equals("2")) {
                            //** case of bridge conncected
                            if (!etpClicked) {
                                writeDataToAnalyze("p_" + row + "_" + col + "");
                                cellsMatrix[row][col] = 2;
                                mPlayer.setRate(swipeDetect_2.meitarPlayMap.get(5 - row), (float) Math.pow(semiTone, col + 1));
                                for (int j = 3; j >= 0; j--) {
                                    if (cellsMatrix[row][j] == 2) { //TODO: what about 1
                                        if (j > col) {
                                            mPlayer.setRate(swipeDetect_2.meitarPlayMap.get(5 - row), (float) Math.pow(semiTone, j + 1));
                                            break;
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            }
                            //** case of E2P mode - send to accorde function (like buttons)
                            else{
                                writeDataToAnalyze("p_" + "E2P_" + row+"_" + col +"");
                               if (row == 0 & col == 0){
                                   checkOtherCord('G');
                                   lastCord = 'G';
                               }
                               else if (row == 0 & col == 1){
                                   checkOtherCord('C');
                                   lastCord = 'C';
                               }
                               else if (row == 0 & col == 2){
                                   checkOtherCord('D');
                                   lastCord = 'D';
                               }
                               else if (row == 0 & col == 3){
                                   checkOtherCord('E');
                                   lastCord = 'E';
                               }
                            }
                        }
                        //** case of half block
                        else if (action.equals("1")) {
                            if (!etpClicked) {
                                writeDataToAnalyze("m_" + row + "_" + col + "");
                                cellsMatrix[row][col] = 1;
                                //TODO: check all that:
                                mPlayer.setRate(swipeDetect_2.meitarPlayMap.get(5 - row), (float) Math.pow(semiTone, col + 1));
                                for (int j = 3; j >= 0; j--) {
                                    if (cellsMatrix[row][j] == 1) { //TODO: what about 1
                                        if (j > col) {
                                            mPlayer.setRate(swipeDetect_2.meitarPlayMap.get(5 - row), (float) Math.pow(semiTone, j + 1));
                                            break;
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //** set the input string to NULL
                inputString = "";
            }
        }
        catch (Exception e)
        {}

    }

    //** get every meitar duration to know when its finish
    private long getSoundDuration(int rawId){
        MediaPlayer player = MediaPlayer.create(GuitarActivity.this, rawId);
        long temp = player.getDuration();
        player.release();
        return temp;
    }
}




