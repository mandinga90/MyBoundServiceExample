package com.example.android.myboundserviceexample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.myboundserviceexample.BoundService.MyBinder;

public class MainActivity extends AppCompatActivity {

    BoundService mBoundService;
    boolean mServiceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.time_stamp);

        Button printTimeStamp = (Button) findViewById(R.id.print_time_stamp);
        final Button stopService    = (Button) findViewById(R.id.stop_service);

        printTimeStamp.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if( mServiceBound ){
                    textView.setText(mBoundService.getTimeStamp());
                }
            }
        });

        stopService.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if( mServiceBound ){
                    unbindService(mServiceConnection);
                    mServiceBound = false;
                }
                Intent intent = new Intent(MainActivity.this, BoundService.class);
                stopService(intent);
                textView.setText("Service stopped.");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BoundService.class);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( mServiceBound ){
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d( getClass().getSimpleName(), "Connected" );
            MyBinder myBinder = (MyBinder) service;
            mBoundService = myBinder.getService();
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d( getClass().getSimpleName(), "Disconnected" );
            mServiceBound = false;
        }
    };
}
