package com.app24announce.dupat.id;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class ServiceBackground extends Service {

    public ServiceBackground()
    {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("ServiceBackground", "Start Service");
                int i = 2;
                while(i>1)
                {
                    Log.d("ServiceBackground", "onStartCommand: "+(i+1));

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        });

        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("ServiceBackground", "Apa");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw  new UnsupportedOperationException("lol");
    }


}
