package com.himanshu.nautiyal.studyguru.Utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

@SuppressLint("Registered")
public class NotificationUpdateService extends Service {

    public static final String ACTION_START = "com.himanshu.nautiyal.studyguru.START_NOTIFY_SERVICE";
    public static final String ACTION_STOP = "com.himanshu.nautiyal.studyguru.STOP_NOTIFY_SERVICE";

    private ChronometerApplication mApplication;
    private long mStartTime;
    private boolean mThreadCanRun;

    private UpdateNotification mUpdateNotification;
    private Thread mUpdateThread;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication =(ChronometerApplication)getApplication();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent.getAction().equalsIgnoreCase(ACTION_START)) {
            //todo make sure this is OK! e.g. check against current time
            mStartTime = intent.getLongExtra("START_TIME", System.currentTimeMillis());
            start();
            return START_REDELIVER_INTENT;
        } else { //currently other case is only stop!
            stop();
            stopSelf();
        }

        return START_NOT_STICKY;
    }



    private void start() {

        mThreadCanRun = true;

        if(mUpdateNotification == null) {
            mUpdateNotification = new UpdateNotification();
            mUpdateThread = new Thread(mUpdateNotification);
            mUpdateThread.start();
        } else {
            stop();
            start();
        }

    }

    private void stop() {

        mThreadCanRun = false;

        if(mUpdateThread != null) {
            mUpdateThread.interrupt();
        }

        mUpdateThread = null;
        mUpdateNotification = null;
        mApplication.cancelNotification();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class UpdateNotification implements Runnable {

        @Override
        public void run() {
            while(mThreadCanRun) {

                long since = System.currentTimeMillis() - mStartTime;
                mApplication.showNotification(ChronometerState.IN_BACKGROUND,
                        Helpers.ConvertTimeToString(since));

                //sleep for 1 second
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
