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
    static String LED_OFF =         "A";
    static String LED_ON =          "B";
    static String LEFT_FORWARD =    "C";
    static String LEFT_BACKWARD =   "D";
    static String LEFT_STOP =       "E";
    static String RIGHT_FORWARD =   "F";
    static String RIGHT_BACKWARD =  "G";
    static String RIGHT_STOP =      "H";
    static String BOTH_FORWARD =    "I";
    static String BOTH_BACKWARD =   "J";
    static String BOTH_STOP =       "K";
    static String GO_LEFT =         "L";
    static String GO_RIGHT =        "M";

    int yBefore = 0;
    int xBefore = 0;

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

    NumberPicker numberPicker;

    Button btnLeft1;
    Button btnLeft2;
    Button btnRight1;
    Button btnRight2;

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
    String strSpeed = new String("2");
    String strLAST_SENT = new String();
    int intSpeed = 2;

    private SensorManager mSensorManager;
    private Sensor mGyroSensor;
    private  Sensor myAccellor;
    private Sensor myLight;

    ImageView iv[] = new ImageView[6];

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            intSpeed = intSpeed-1;
            if (intSpeed < 0)
                intSpeed = 0;
            strSpeed = Integer.toString(intSpeed);
            serialReceivedText.setText(">Speed:"+Integer.toString(intSpeed*10)+"%\n"+serialReceivedText.getText());
            serialSend(strLAST_SENT+strSpeed);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            intSpeed = intSpeed+1;
            if (intSpeed > 9)
                intSpeed = 9;
            strSpeed = Integer.toString(intSpeed);
            serialSend(strLAST_SENT+strSpeed);
            serialReceivedText.setText(">Speed:"+Integer.toString(intSpeed*10)+"%\n"+serialReceivedText.getText());
            return true;
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) { //this is just to kill the default beep sound
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP) || (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
       }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonScan:
                buttonScanOnClickProcess();
                break;
            case R.id.btnStop:
                serialSend(BOTH_STOP+strSpeed);
                strLAST_SENT = BOTH_STOP;
                btnLeftBackward.setChecked(false);
                btnLeftForward.setChecked(false);
                btnRightFoward.setChecked(false);
                btnRightBackward.setChecked(false);
                btnForward.setChecked(false);
                btnBackward.setChecked(false);
                break;
            case R.id.btnBackward:
                if (btnBackward.isChecked()) {
                    serialSend(BOTH_BACKWARD+strSpeed);
                    strLAST_SENT = BOTH_BACKWARD;
                    btnLeftBackward.setChecked(true);
                    btnLeftForward.setChecked(false);
                    btnRightFoward.setChecked(false);
                    btnRightBackward.setChecked(true);
                    btnForward.setChecked(false);
                }
                else {
                    serialSend(BOTH_STOP+strSpeed);
                    strLAST_SENT = BOTH_STOP;
                    btnLeftBackward.setChecked(false);
                    btnLeftForward.setChecked(false);
                    btnRightFoward.setChecked(false);
                    btnRightBackward.setChecked(false);
                    btnForward.setChecked(false);
                }
                break;
            case R.id.btnForward:
                if (btnForward.isChecked()) {
                    serialSend(BOTH_FORWARD+strSpeed);
                    strLAST_SENT = BOTH_FORWARD;
                    btnLeftBackward.setChecked(false);
                    btnLeftForward.setChecked(true);
                    btnRightFoward.setChecked(true);
                    btnRightBackward.setChecked(false);
                    btnBackward.setChecked(false);
                }
                else {
                    serialSend(BOTH_STOP+strSpeed);
                    strLAST_SENT = BOTH_STOP;
                    btnLeftBackward.setChecked(false);
                    btnLeftForward.setChecked(false);
                    btnRightFoward.setChecked(false);
                    btnRightBackward.setChecked(false);
                    btnBackward.setChecked(false);
                }
                break;
            case R.id.btnLeftForward:
                if (btnLeftForward.isChecked()) {
                    serialSend(LEFT_FORWARD+strSpeed);
                    strLAST_SENT = LEFT_FORWARD;
                    btnLeftBackward.setChecked(false);
                }
                else {
                    serialSend(LEFT_STOP+strSpeed);
                    strLAST_SENT = LEFT_STOP;
                    btnForward.setChecked(false);
                    btnBackward.setChecked(false);
                }
                break;
            case R.id.btnLeftBackward:
                if (btnLeftBackward.isChecked()) {
                    serialSend(LEFT_BACKWARD+strSpeed);
                    strLAST_SENT = LEFT_BACKWARD;
                    btnLeftForward.setChecked(false);
                }
                else {
                    serialSend(LEFT_STOP+strSpeed);
                    strLAST_SENT = LEFT_STOP;
                    btnForward.setChecked(false);
                    btnBackward.setChecked(false);
                }
                break;
            case R.id.btnRightFoward:
                if (btnRightFoward.isChecked()) {
                    serialSend(RIGHT_FORWARD+strSpeed);
                    strLAST_SENT = RIGHT_FORWARD;
                    btnRightBackward.setChecked(false);
                }
                else {
                    serialSend(RIGHT_STOP+strSpeed);
                    strLAST_SENT = RIGHT_STOP;
                    btnForward.setChecked(false);
                    btnBackward.setChecked(false);
                }
                break;
            case R.id.btnRightBackward:
                if (btnRightBackward.isChecked()) {
                    serialSend(RIGHT_BACKWARD+strSpeed);
                    strLAST_SENT = RIGHT_BACKWARD;
                    btnRightFoward.setChecked(false);
                }
                else{
                    serialSend(RIGHT_STOP+strSpeed);
                    strLAST_SENT = RIGHT_STOP;
                    btnForward.setChecked(false);
                    btnBackward.setChecked(false);
                }
                break;
            case R.id.btnLeft1:
                serialSend(GO_LEFT+strSpeed);
                strLAST_SENT = GO_LEFT;
                break;
            case R.id.btnLeft2:
                serialSend(GO_LEFT+strSpeed);
                strLAST_SENT = GO_LEFT;
                break;
            case R.id.btnRight1:
                serialSend(GO_RIGHT+strSpeed);
                strLAST_SENT = GO_RIGHT;
                break;
            case R.id.btnRight2:
                serialSend(GO_RIGHT+strSpeed);
                strLAST_SENT = GO_RIGHT;
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
        btnLeft1  = (Button) findViewById(R.id.btnLeft1);
        btnLeft2  = (Button) findViewById(R.id.btnLeft2);
        btnRight1  = (Button) findViewById(R.id.btnRight1);
        btnRight2  = (Button) findViewById(R.id.btnRight2);
        btnLeft1.setOnClickListener(this);
        btnLeft2.setOnClickListener(this);
        btnRight1.setOnClickListener(this);
        btnRight2.setOnClickListener(this);
        serialReceivedText.setText(">Speed:"+Integer.toString(intSpeed*10)+"%\n");
        serialReceivedText.setText("Use Volume Buttons to change speed\n"+serialReceivedText.getText());
        serialReceivedText.setText("Tilt 90 degress to stop\n"+serialReceivedText.getText());
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
        int y = 0;
        int x = 0;
        int leftMotorSpeed = 0;
        int righMotorSpeed = 0;
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
        if ( Gz < 0) {
            serialSend(LEFT_STOP);
            serialSend(RIGHT_STOP);
            btnLeftBackward.setChecked(false);
            btnLeftForward.setChecked(false);
            btnRightFoward.setChecked(false);
            btnRightBackward.setChecked(false);
            btnForward.setChecked(false);
            btnBackward.setChecked(false);
            return;
        }
        y = (int)Gy*2;
        x = (int)Gx;
        leftMotorSpeed = 0;
        righMotorSpeed = 0;
        if (x < 0)
            leftMotorSpeed = Math.abs(x);
        if (x > 0 )
            righMotorSpeed = Math.abs(x);
        for (int i=0;i<Math.abs(y);i++) {
            leftMotorSpeed++;
            righMotorSpeed++;
            if ((righMotorSpeed > 9) || (leftMotorSpeed > 9)  ) {
                righMotorSpeed --;
                leftMotorSpeed--;
                break;
            }
        }
        textView2.setText("x:"+ String.format("%.1f", Gx) +"  y:"+ String.format("%.1f", Gy) + " z:"+ String.format("%.1f", Gz)+"\n" +
                "left M:"+ String.format("%d", leftMotorSpeed)+" right M:"+ String.format("%d",righMotorSpeed) );
        /*textView2.setText("x:"+ String.format("%.1f", Gx) +"\n" + //String.format("%.2f", floatValue)  Float.toString(Gx)
                "y:"+ String.format("%.1f", Gy) +"\n" +
                "z:"+ String.format("%.1f", Gz));*/
        if (! checkBox.isChecked())
            return;
        if (y == yBefore && x == xBefore)
            return;
        yBefore = y;
        xBefore = x;
        if ((righMotorSpeed ==0) && (leftMotorSpeed ==0) ) {
            serialSend(BOTH_STOP +"0");
            return;
        }
        if (y < 0) {
            serialSend(LEFT_FORWARD + Integer.toString(leftMotorSpeed));
            serialSend(RIGHT_FORWARD + Integer.toString(righMotorSpeed));
            return;
        }
        if (y > 0) {
            serialSend(LEFT_BACKWARD + Integer.toString(leftMotorSpeed));
            serialSend(RIGHT_BACKWARD + Integer.toString(righMotorSpeed));
            return;
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

    public  int scale( double valueIn, final double baseMin, final double baseMax, final double limitMin, final double limitMax) {
        double _result;
        if (valueIn < baseMin ) valueIn = baseMin;
        if (valueIn > baseMax ) valueIn = baseMax;
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
        strReadData = strReadData+theString;
        Integer _Pos = strReadData.indexOf('}');
        if ( _Pos>= 0 ) {
            if (serialReceivedText.getText().length() > 500)
                serialReceivedText.setText("");
            String strMessage = new String();
            strMessage = strReadData.substring(0,_Pos);
            Log.d("DATA",strMessage);
            strReadData = "";
            String[] separated = strMessage.split(",");
            for (int i=0; i<separated.length;i++ ) {
                if (isNumeric(separated[i])) {
                    double d =  Double.parseDouble(separated[i]);
                    int _red = 255- scale(d,0,2000,0,255);
                    int _color = Color.argb(255,_red,0,0);
                    if (i < 6)  iv[i].setBackgroundColor(_color);
                }
            }

            if (separated.length > 4) {
                return; // is numbers from sensors
            }

            serialReceivedText.setText(strMessage.replace("z ", ">")+"\n"+serialReceivedText.getText());
            if (strMessage.indexOf('z') >=0) {
                btnLeftBackward.setChecked(false);
                btnLeftForward.setChecked(false);
                btnRightFoward.setChecked(false);
                btnRightBackward.setChecked(false);
                btnForward.setChecked(false);
                btnBackward.setChecked(false);
                if (AlreadyDone == 0) {
                    AlreadyDone = 1;
                   // speakOut("the end is near, we had to stop");
                };
                vibrator.vibrate(400);
                toneG.startTone(ToneGenerator.TONE_CDMA_ANSWER, 200);
            }
        }
    }

}
