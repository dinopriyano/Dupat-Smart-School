package com.app24announce.dupat.id;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;
import android.widget.Toast;

public class JobServiceNotif extends JobService {

    private static String TAG = "JobServiceNotif";
    private boolean jobCancel = false;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started");
        doBackgroundWork(jobParameters);
        return true;
    }

    private void doBackgroundWork(final JobParameters params)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1 ;i>0;i++)
                {
                    //Toast.makeText(getApplicationContext(), "job schedule run : "+i, Toast.LENGTH_SHORT).show();

                    if(jobCancel)
                    {
                        return;
                    }

                    try{
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "job finished");
                    jobFinished(params,false);
                }
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "job cancelled before completion");
        jobCancel = true;
        return true;
    }
}
