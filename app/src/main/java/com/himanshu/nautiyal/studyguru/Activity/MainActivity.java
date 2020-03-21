package com.himanshu.nautiyal.studyguru.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.himanshu.nautiyal.studyguru.Database.DataList;
import com.himanshu.nautiyal.studyguru.Database.EachDayDatabase;
import com.himanshu.nautiyal.studyguru.Database.MyDatabaseHelper;
import com.himanshu.nautiyal.studyguru.R;
import com.himanshu.nautiyal.studyguru.Utils.Chronometer;
import com.himanshu.nautiyal.studyguru.Utils.ChronometerApplication;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class MainActivity extends AppCompatActivity {

    AlertDialog.Builder alertDialoge;

    public static final String startTime = "START_TIME";
    public static final String isChronoRunning = "CHRONO_WAS_RUNNING";
    SharedPreferences sharedPreferences;
    public static final String timerText = "TV_TIMER_TEXT";
    public static final String TAG = "MAIN";

    public static final String LAP_COUNTER = "LAP_COUNTER";
    int state = 0;
    long tBuff = 0;
    //Member variables to access UI Elements
    ImageView btnStart, btnStop, btnPause; //buttons
    TextView tvTimer; //timer textview
    //    EditText mEtLaps; //laps text view
    ScrollView mSvLaps; //scroll view which wraps the et_laps

    //keep track of how many times btn_lap was clicked
    int lapCounter = 1;
    ViewGroup parent;
    SQLiteDatabase db;
    //Instance of Chronometer
    Chronometer mChrono;
    CircularProgressBar circularProgressBar;
    //Thread for chronometer
    Thread mThreadChrono;
    Button btnNext;

    //Reference to the MainActivity (this class!)
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);

        //Instantiating all member variables

        mContext = this;

        parent = (ViewGroup) findViewById(android.R.id.content);
        createAlertDialog();
        MyDatabaseHelper myDataBaseHelper=new MyDatabaseHelper(this);
        db = myDataBaseHelper.getWritableDatabase();

        btnStart = findViewById(R.id.btn_start);
//        mBtnLap = findViewById(R.id.btn_lap);
        btnStop = findViewById(R.id.btn_stop);
        btnPause = findViewById(R.id.btn_pause);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.packageName), MODE_PRIVATE);
        sharedPreferences.edit().putInt("goal", 6).apply();
        tvTimer = (TextView) findViewById(R.id.tv_timer);
        btnNext=findViewById(R.id.btnNext);
         state=sharedPreferences.getInt("state",0);

        Log.d(TAG, "onCreate: "+state);
        if(state==0){
            btnStart.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.GONE);
        }
        else{

            btnStart.setVisibility(View.GONE);
            btnStop.setVisibility(View.GONE);
            btnPause.setVisibility(View.VISIBLE);
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DataShowActivity.class);
                startActivity(intent);
            }
        });



//        mEtLaps = (EditText) findViewById(R.id.et_laps);
//        mEtLaps.setEnabled(false); //prevent the et_laps to be editable

//        mSvLaps = (ScrollView) findViewById(R.id.sv_lap);


        //btn_start click handler
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the chronometer has not been instantiated before...
                if (mChrono == null) {
                    if(tvTimer.getText().equals("00:00:00:00")){

                    }
                    tBuff = getSharedPreferences(getResources().getString(R.string.packageName), MODE_PRIVATE).getLong("tBuff", 0);
                    //instantiate the chronometer
                    Log.d(TAG, "onClick: " + tBuff);
                    mChrono = new Chronometer(mContext);
                    mChrono.settBuff(tBuff);
                    //run the chronometer on a separate thread
                    mThreadChrono = new Thread(mChrono);
                    mThreadChrono.start();
                    //start the chronometer!
                    mChrono.start();
                    state = 1;

                    //clear the perilously populated et_laps
//                    mEtLaps.setText(""); //empty string!

                    //reset the lap counter
                    lapCounter = 1;

                    Animation slideNegativeRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_negative_right);
                    Animation slideNegativeLeft=AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_negative_left);

                    Animation scaleUp=AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_positive);


                    btnStart.setVisibility(View.GONE);
                    btnStart.setAnimation(slideNegativeLeft);
                    btnStop.setVisibility(View.GONE);
                    btnStop.setAnimation(slideNegativeRight);
                    btnPause.setVisibility(View.VISIBLE);
                    btnPause.setAnimation(scaleUp);


                }
            }
        });


        btnPause.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onClick(View v) {
                if (mChrono != null) {
                    long val = System.currentTimeMillis() - mChrono.getStartTime();
                    mChrono.pause(val);
                    val += sharedPreferences.getLong("tBuff", 0);
                    sharedPreferences.edit().putLong("tBuff", val).apply();
                    Log.d(TAG, "onClick: "+val+"  "+getSharedPreferences(getResources().getString(R.string.packageName),MODE_PRIVATE).getLong("tBuff",0));

                    mThreadChrono.interrupt();
                    mThreadChrono = null;
                    mChrono = null;
                    state = 0;
                    Animation slideRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_right);
                    Animation slideLeft=AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_left);

                    Animation scaleDown=AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_negative);
                    btnStart.setVisibility(View.VISIBLE);
                    btnStart.setAnimation(slideLeft);
                    btnStop.setVisibility(View.VISIBLE);
                    btnStop.setAnimation(slideRight);
                    btnPause.setVisibility(View.GONE);
                    btnPause.setAnimation(scaleDown);

                }
            }
        });
        //btn_stop click handler
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the chronometer had been instantiated before...
//                if (mChrono != null) {
//                    //stop the chronometer
//
//                    mChrono.stop();
//                    //stop the thread
//                    tvTimer.setText("00:00:00:00");
//                    mThreadChrono.interrupt();
//                    state = 0;
//
//                    mThreadChrono = null;
//                    //kill the chrono class
//                    mChrono = null;
//
//                    getSharedPreferences(getResources().getString(R.string.packageName), MODE_PRIVATE).edit().putLong("tBuff", 0).apply();
//                } else {
//                    tvTimer.setText("00:00:00:00");
//                    getSharedPreferences(getResources().getString(R.string.packageName), MODE_PRIVATE).edit().putLong("tBuff", 0).apply();
//                }
//                btnStart.setVisibility(View.VISIBLE);
//                btnStop.setVisibility(View.VISIBLE);
//                btnPause.setVisibility(View.GONE);
            if (mChrono!=null)
                alertDialoge.show();
            }
        });

//        mBtnLap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //if chrono is not running we shouldn't capture the lap!
//                if(mChrono == null) {
//                    Toast.makeText(MainActivity.this, "NULl", Toast.LENGTH_SHORT).show();;
//                    return; //do nothing!
//                }
//
//                //we just simply copy the current text of tv_timer and append it to et_laps
//                mEtLaps.append("LAP " + String.valueOf(mLapCounter++)
//                        + "   " + mTvTimer.getText() + "\n");
//
//                //scroll to the bottom of et_laps
//                mSvLaps.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mSvLaps.smoothScrollTo(0, mEtLaps.getBottom());
//                    }
//                });
//            }
//        });
    }

    public void updateTimerText(final String timeAsText, final int hr, final int min) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTimer.setText(timeAsText);
                int goalHr = sharedPreferences.getInt("goal", 1);
                int currentValue = (hr * 60) + (min);
                float percentage = (currentValue / (float) (goalHr * 60)) * 100;
                Log.d(TAG, "run: " + "Here");
                circularProgressBar.setProgress(percentage);
                Log.d(TAG, "run: " + "here");


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadInstance();

        //stop background services and notifications
        Log.d("MAIN", getApplication().toString());
        ((ChronometerApplication) getApplication()).stopBackgroundServices();
        ((ChronometerApplication) getApplication()).cancelNotification();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveInstance();

        if (mChrono != null && mChrono.isRunning()) {
            //start background notification and timer
            ((ChronometerApplication) getApplication())
                    .startBackgroundServices(mChrono.getStartTime());
        }
    }

    @Override
    protected void onDestroy() {

        saveInstance();

        //When back button is pressed, app will be destoyed by OS. We do not want this to stop us
        //from showing the notification if the chronometer is running!
        if (mChrono == null || !mChrono.isRunning()) {
            //stop background services and notifications
            ((ChronometerApplication) getApplication()).stopBackgroundServices();
            ((ChronometerApplication) getApplication()).cancelNotification();
        }

        super.onDestroy();
    }

    /**
     * If the application goes to background or orientation change or any other possibility that
     * will pause the application, we save some instance values, to resume back from last state
     */
    private void saveInstance() {
        SharedPreferences pref = sharedPreferences;
        SharedPreferences.Editor editor = pref.edit();

        //TODO move tags to a static class
        if (mChrono != null && mChrono.isRunning()) {
            editor.putBoolean(isChronoRunning, mChrono.isRunning());
            editor.putInt("state", state);
            editor.putLong(startTime, mChrono.getStartTime());
            editor.putInt(LAP_COUNTER, lapCounter);
        } else {
            editor.putBoolean(isChronoRunning, false);
            editor.putLong(startTime, 0); //0 means chronometer was not active! a redundant check!
            editor.putInt(LAP_COUNTER, 1);
            editor.putInt("state", state);
        }

        editor.putString(timerText, tvTimer.getText().toString());

        editor.apply();
    }

    /**
     * Load the shared preferences to resume last known state of the application
     */
    private void loadInstance() {

        SharedPreferences pref = sharedPreferences;
        sharedPreferences.getLong(getResources().getString(R.string.packageName), MODE_PRIVATE);
        long pausedTime = sharedPreferences.getLong("tBuff", 0);
        Log.d(TAG, "loadInstance: "+pausedTime);
        //if chronometer was running
        if (pref.getBoolean(isChronoRunning, false)) {
            //get the last start time from the bundle
            long lastStartTime = pref.getLong(startTime, 0);
            //if the last start time is not 0
            if (lastStartTime != 0) { //because 0 means value was not saved correctly!

                if (mChrono == null) { //make sure we dont create new instance and thread!

                    if (mThreadChrono != null) { //if thread exists...first interrupt and nullify it!
                        mThreadChrono.interrupt();
                        mThreadChrono = null;
                    }

                    //start chronometer with old saved time
                    mChrono = new Chronometer(mContext, lastStartTime);
                    mChrono.settBuff(pausedTime);
                    mThreadChrono = new Thread(mChrono);
                    mThreadChrono.start();
                    mChrono.start();
                }
            }
        }

        //we will load the lap text anyway in any case!
        //set the old value of lap counter
        lapCounter = pref.getInt(LAP_COUNTER, 1);


        String oldTvTimerText = pref.getString(timerText, "");
        if (!oldTvTimerText.isEmpty()) {
            tvTimer.setText(oldTvTimerText);
        }
    }
    void createAlertDialog(){
        alertDialoge=new AlertDialog.Builder(this);
        alertDialoge.setTitle("Reset and Save");
        alertDialoge.setMessage("Are you Done  studying and want to call it a day ");
        alertDialoge.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mChrono != null) {
                    //stop the chronometer

                    mChrono.stop();
                    //stop the thread
                    tvTimer.setText("00:00:00:00");
                    mThreadChrono.interrupt();
                    state = 0;

                    mThreadChrono = null;
                    //kill the chrono class
                    mChrono = null;

                    getSharedPreferences(getResources().getString(R.string.packageName), MODE_PRIVATE).edit().putLong("tBuff", 0).apply();
                } else {
                    tvTimer.setText("00:00:00:00");
                    getSharedPreferences(getResources().getString(R.string.packageName), MODE_PRIVATE).edit().putLong("tBuff", 0).apply();
                }
//                EachDayDatabase.insertPlayer(db,new DataList(""));
                btnStart.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "Ok, Keep Studying", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
