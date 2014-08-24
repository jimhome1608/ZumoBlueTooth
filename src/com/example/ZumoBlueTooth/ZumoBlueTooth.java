package com.example.ZumoBlueTooth;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import java.util.Locale;
import java.util.Random;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.*;
import android.graphics.Color;
import android.widget.*;
import android.view.View.OnClickListener;
import android.graphics.Bitmap;
import android.view.ViewGroup.LayoutParams;
import android.util.DisplayMetrics;


public class ZumoBlueTooth extends BlunoLibrary
        implements View.OnClickListener, SensorEventListener, TextToSpeech.OnInitListener  {

    static int upLeft = 0;
    static String LED_OFF =         "0";
    static String LED_ON =          "1";
    static String LEFT_FORWARD =    "2";
    static String LEFT_BACKWARD =   "3";
    static String LEFT_STOP =       "4";
    static String RIGHT_FORWARD =   "5";
    static String RIGHT_BACKWARD =  "6";
    static String RIGHT_STOP =      "7";
    static String BOTH_FORWARD =    "8";
    static String BOTH_BACKWARD =   "9";
    static String BOTH_STOP =       "A";

    private static final int INVALID_POINTER_ID = -1;

    private int ContentId = 0;

    private TextToSpeech tts;

    private Drawable mIcon;
    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    private Button buttonScan;
    private Button btnTouch;
    private TextView serialReceivedText;
    private TextView textView2;

    private int AlreadyDone;

    ToggleButton btnLeftForward;
    ToggleButton btnLeftBackward;
    ToggleButton btnRightFoward;
    ToggleButton btnRightBackward;
    ToggleButton btnForward;
    ToggleButton btnBackward;
    CheckBox checkBox;
    Button btnStop;
    Vibrator vibrator;
    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_SYSTEM,100);

    String strReadData  = new String();
    String  EventFlag = new String();

    private SensorManager mSensorManager;
    private Sensor mGyroSensor;
    private  Sensor myAccellor;
    private Sensor myLight;

    ImageView iv[] = new ImageView[6];

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.buttonScan:
                buttonScanOnClickProcess();
                break;
            case R.id.btnStop:
                serialSend(LEFT_STOP);
                serialSend(RIGHT_STOP);
                btnLeftBackward.setChecked(false);
                btnLeftForward.setChecked(false);
                btnRightFoward.setChecked(false);
                btnRightBackward.setChecked(false);
                btnForward.setChecked(false);
                btnBackward.setChecked(false);
                break;
            case R.id.btnBackward:
                if (btnBackward.isChecked()) {
                    serialSend(RIGHT_BACKWARD);
                    serialSend(LEFT_BACKWARD);
                    btnLeftBackward.setChecked(true);
                    btnLeftForward.setChecked(false);
                    btnRightFoward.setChecked(false);
                    btnRightBackward.setChecked(true);
                    btnForward.setChecked(false);
                }
                else {
                    serialSend(LEFT_STOP);
                    serialSend(RIGHT_STOP);
                    btnLeftBackward.setChecked(false);
                    btnLeftForward.setChecked(false);
                    btnRightFoward.setChecked(false);
                    btnRightBackward.setChecked(false);
                    btnForward.setChecked(false);
                }
                break;
            case R.id.btnForward:
                if (btnForward.isChecked()) {
                    serialSend(RIGHT_FORWARD);
                    serialSend(LEFT_FORWARD);
                    btnLeftBackward.setChecked(false);
                    btnLeftForward.setChecked(true);
                    btnRightFoward.setChecked(true);
                    btnRightBackward.setChecked(false);
                    btnBackward.setChecked(false);
                }
                else {
                    serialSend(LEFT_STOP);
                    serialSend(RIGHT_STOP);
                    btnLeftBackward.setChecked(false);
                    btnLeftForward.setChecked(false);
                    btnRightFoward.setChecked(false);
                    btnRightBackward.setChecked(false);
                    btnBackward.setChecked(false);
                }
                break;
            case R.id.btnLeftForward:
                if (btnLeftForward.isChecked()) {
                    serialSend(LEFT_FORWARD);
                    btnLeftBackward.setChecked(false);
                }
                else {
                    serialSend(LEFT_STOP);
                    btnForward.setChecked(false);
                    btnBackward.setChecked(false);
                }
                break;
            case R.id.btnLeftBackward:
                if (btnLeftBackward.isChecked()) {
                    serialSend(LEFT_BACKWARD);
                    btnLeftForward.setChecked(false);
                }
                else {
                    serialSend(LEFT_STOP);
                    btnForward.setChecked(false);
                    btnBackward.setChecked(false);
                }
                break;
            case R.id.btnRightFoward:
                if (btnRightFoward.isChecked()) {
                    serialSend(RIGHT_FORWARD);
                    btnRightBackward.setChecked(false);
                }
                else {
                    serialSend(RIGHT_STOP);
                    btnForward.setChecked(false);
                    btnBackward.setChecked(false);
                }
                break;
            case R.id.btnRightBackward:
                if (btnRightBackward.isChecked()) {
                    serialSend(RIGHT_BACKWARD);
                    btnRightFoward.setChecked(false);
                }
                else{
                    serialSend(RIGHT_STOP);
                    btnForward.setChecked(false);
                    btnBackward.setChecked(false);
                }
                break;

        }

    }


    private void setupMainConentView() {
        iv[0] = (ImageView)findViewById(R.id.iv1);
        iv[1] = (ImageView)findViewById(R.id.iv2);
        iv[2] = (ImageView)findViewById(R.id.iv3);
        iv[3] = (ImageView)findViewById(R.id.iv4);
        iv[4] = (ImageView)findViewById(R.id.iv5);
        iv[5] = (ImageView)findViewById(R.id.iv6);
        for (int i=0;i<=5;i++)
            iv[i].setBackgroundColor(0xFFFF0000);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        btnForward = (ToggleButton)findViewById(R.id.btnForward);
        btnForward.setOnClickListener(this);
        btnLeftForward = (ToggleButton)findViewById(R.id.btnLeftForward);
        btnLeftForward.setOnClickListener(this);
        btnLeftBackward = (ToggleButton)findViewById(R.id.btnLeftBackward);
        btnLeftBackward.setOnClickListener(this);
        btnRightFoward = (ToggleButton)findViewById(R.id.btnRightFoward);
        btnRightFoward.setOnClickListener(this);
        btnRightBackward = (ToggleButton)findViewById(R.id.btnRightBackward);
        btnRightBackward.setOnClickListener(this);
        btnForward= (ToggleButton)findViewById(R.id.btnForward);
        btnForward.setOnClickListener(this);
        btnBackward= (ToggleButton)findViewById(R.id.btnBackward);
        btnBackward.setOnClickListener(this);
        btnStop = (Button)findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);
        serialReceivedText=(TextView) findViewById(R.id.serialReveicedText);
        textView2=(TextView) findViewById(R.id.textView2);

        buttonScan = (Button) findViewById(R.id.buttonScan);					//initial the button for scanning the BLE device
        buttonScan.setOnClickListener(this);
    }


    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.
                    FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            setContentView(R.layout.main);
        onCreateProcess();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyroSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        myAccellor= mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        myLight= mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        serialBegin(38400);
        tts = new TextToSpeech(this, this);
        AlreadyDone =0;
        setupMainConentView();
        }

    private void speakOut(String _text) {
        tts.speak(_text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.FRENCH);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
            else {
                //speakOut("hello my friend");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, myAccellor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, myLight, SensorManager.SENSOR_DELAY_NORMAL);
        System.out.println("BlUNOActivity onResume");
        onResumeProcess();														//onResume Process by BlunoLibrary
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        final int type = event.sensor.getType();

        String NewEventFlag = new String();
        NewEventFlag = "";
       /* if(type==Sensor.TYPE_LIGHT) {
            final float currentReading = event.values[0];
            tvRight.setText("light :"+ Float.toString(currentReading));
            return;
        }
        */
        if (type != Sensor.TYPE_ACCELEROMETER)
            return;
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;
        float Gx = event.values[0];
        float Gy = event.values[1];
        float Gz = event.values[2];
        textView2.setText("  x:"+ String.format("%.2f", Gx) +" "+ //String.format("%.2f", floatValue)  Float.toString(Gx)
                " y:"+ String.format("%.2f", Gy) +"\n"+
                "  z:"+ String.format("%.2f", Gz));
        if (! checkBox.isChecked())
            return;
        if (Gy > 2)
            NewEventFlag = "BACK";
        if (Gy < -2)
            NewEventFlag = "FORWARD";
        if (Math.abs(Gy) < 2)
            NewEventFlag = "STOP";
        if (Gx > 2)
            NewEventFlag = "HALF_RIGHT";
        if (Gx <  -2)
            NewEventFlag = "HALF_LEFT";
        if (Gx <  -5)
            NewEventFlag = "FULL_LEFT";
        if (Gx > 5)
            NewEventFlag = "FULL_RIGHT";
        if ( NewEventFlag != EventFlag){
            EventFlag = NewEventFlag;
            if (EventFlag == "BACK") {
                serialSend(RIGHT_BACKWARD);
                serialSend(LEFT_BACKWARD);
            }
            if (EventFlag == "FORWARD") {
                serialSend(RIGHT_FORWARD);
                serialSend(LEFT_FORWARD);
            }
            if (EventFlag == "STOP") {
                serialSend(RIGHT_STOP);
                serialSend(LEFT_STOP);
            }
            ;
            if (EventFlag == "HALF_RIGHT") {
                serialSend(LEFT_STOP);
            }
            ;
            if (EventFlag == "HALF_LEFT") {
                serialSend(RIGHT_STOP);
            }
            ;
            if (EventFlag == "FULL_RIGHT") {
                serialSend(RIGHT_FORWARD);
                serialSend(LEFT_BACKWARD);
            }
            ;
            if (EventFlag == "FULL_LEFT") {
                serialSend(RIGHT_BACKWARD);
                serialSend(LEFT_FORWARD);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();
        mSensorManager.unregisterListener(this);
    }

    protected void onStop() {
        super.onStop();
        onStopProcess();														//onStop Process by BlunoLibrary
    }

    @Override
    protected void onDestroy() {
        //if (tts != null) {
        //    tts.stop();
        //    tts.shutdown();
        // }
        super.onDestroy();
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }

    @Override
    public void onConectionStateChange(BlunoLibrary.connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
        switch (theConnectionState) {											//Four connection state
            case isConnected:
                buttonScan.setText("Connected");
                break;
            case isConnecting:
                buttonScan.setText("Connecting");
                break;
            case isToScan:
                buttonScan.setText("Scan");
                break;
            case isScanning:
                buttonScan.setText("Scanning");
                break;
            case isDisconnecting:
                buttonScan.setText("isDisconnecting");
                break;
            default:
                break;
        }
    }

    public  int scale(final double valueIn, final double baseMin, final double baseMax, final double limitMin, final double limitMax) {
        double _result;
        _result = ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
        return (int) Math.round(_result);
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }


    @Override
    public void onSerialReceived(String theString) {							//Once connection data received, this function will be called
        // TODO Auto-generated method stub
        strReadData = strReadData+theString;
        Integer _Pos = strReadData.indexOf('}');
        if ( _Pos>= 0 ) {
            if (serialReceivedText.getText().length() > 500)
                serialReceivedText.setText("");
            String strMessage = new String();
            strMessage = strReadData.substring(0,_Pos);
            if (isNumeric(strMessage) ==true) {
                double d =  Double.parseDouble(strMessage);
                int _red = 255- scale(d,0,2000,0,255);
                int _color = Color.argb(255,_red,0,0);
                for (int i=0;i<=5;i++)
                    iv[i].setBackgroundColor(_color);
            }
            serialReceivedText.setText(strMessage+"\n"+serialReceivedText.getText());
            strReadData = "";
            if (strMessage.indexOf('z') >=0) {
                btnLeftBackward.setChecked(false);
                btnLeftForward.setChecked(false);
                btnRightFoward.setChecked(false);
                btnRightBackward.setChecked(false);
                btnForward.setChecked(false);
                btnBackward.setChecked(false);
                if (AlreadyDone == 0) {
                    AlreadyDone = 1;
                    speakOut("the end is near, we had to stop");
                };
                vibrator.vibrate(400);
                toneG.startTone(ToneGenerator.TONE_CDMA_ANSWER, 200);
            }
        }
    }

}
