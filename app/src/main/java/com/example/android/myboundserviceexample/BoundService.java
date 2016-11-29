package com.example.android.myboundserviceexample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;

/**
 * Created by Manuel on 29.11.2016.
 */

public class BoundService extends Service {

    private MyBinder mBinder = new MyBinder();
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Chronometer mChronometer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d( LOG_TAG, "onCreate" );
        mChronometer = new Chronometer(this);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d( LOG_TAG, "onBind" );
        return mBinder;
    }

    public String getTimeStamp(){
        long elapsedMillis = SystemClock.elapsedRealtime()
                - mChronometer.getBase();
        int hours = (int) (elapsedMillis / 3600000);
        int minutes = (int) (elapsedMillis - hours * 3600000) / 60000;
        int seconds = (int) (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
        int millis = (int) (elapsedMillis - hours * 3600000 - minutes * 60000 - seconds * 1000);
        return hours + ":" + minutes + ":" + seconds + ":" + millis;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d( LOG_TAG, "onDestroy" );
        mChronometer.stop();
    }

    public class MyBinder extends Binder{

        BoundService getService(){
            return BoundService.this;
        }

    }

}
