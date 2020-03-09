package com.app24announce.dupat.id;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {

//    private final String CHANNEL_ID = "personal_notifications";
//    private final int NOTIFICATION_ID = 001;

    private final String CHANNEL_ID = "personal_notifications";
    private final int NOTIFICATION_ID = 001;
    private final String NOTIFICATION_CHANNEL_ID = "my_channel_01";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("pengumuman");
    TextToSpeech tts;
    String kataAwal= "hula";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        onTaskRemoved(intent);

        // text to speech
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR)
                {
                    tts.setLanguage(new Locale("id","ID"));
                }
            }
        });


        myRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String inf = null;

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    SetGetListInfo info = ds.getValue(SetGetListInfo.class);



                }

                //Toast.makeText(getApplicationContext(),inf,Toast.LENGTH_SHORT).show();

                //notification
                long[] pattern = {500,500,500,500,500,500,500,500,500};
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, CHANNEL_ID);
//                builder.setSmallIcon(R.drawable.school);
//                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.school));
//                builder.setContentTitle("Informasi Baru");
//                builder.setContentText(inf);
//                builder.setVibrate(pattern);
//                builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
//                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
//
//                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MyService.this);
//                notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());

//                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this);
//                builder.setSmallIcon(R.drawable.ic_announcement);
//                builder.setContentTitle("Informasi Baru");
//                builder.setContentText(inf);
//                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                builder.setChannelId(NOTIFICATION_CHANNEL_ID);
//                builder.setVibrate(pattern);
//                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
//                builder.setSmallIcon(R.drawable.school);
//                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.school));
//
//                CharSequence name = "panda";
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//                NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,name,importance);
//                mChannel.setLightColor(Color.YELLOW);
//                mChannel.enableVibration(true);
//                mChannel.enableLights(true);
//                mChannel.setVibrationPattern(pattern);

                showNotification(MyService.this,"Informasi Baru",inf);

                //NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(pengumumanPage.this);
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.createNotificationChannel(mChannel);
//                notificationManager.notify(1,builder.build());
                //notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());

                tts.speak(inf,TextToSpeech.QUEUE_FLUSH,null);

                if(dataSnapshot.exists())
                {
                    int totalInfo = (int) dataSnapshot.getChildrenCount();
                }






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return START_STICKY;
    }

    public void showNotification(Context context, String title, String messageBody) {


        String channel_id = createNotificationChannel(context);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channel_id)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.school))
                .setSmallIcon(R.drawable.ic_school) //needs white icon with transparent BG (For all platforms)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .setVibrate(new long[]{1000, 1000})
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setPriority(Notification.PRIORITY_MAX)
                .setLights(Color.YELLOW,300,100)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) ((new Date(System.currentTimeMillis()).getTime() / 1000L) % Integer.MAX_VALUE) /* ID of notification */, notificationBuilder.build());
    }

    public static String createNotificationChannel(Context context) {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            String channelId = "Channel_id";

            // The user-visible name of the channel.
            CharSequence channelName = "Dupat SmartSc";
            // The user-visible description of the channel.
            String channelDescription = "Application_name Alert";
            int channelImportance = NotificationManager.IMPORTANCE_HIGH;
            boolean channelEnableVibrate = true;
            int channelLockscreenVisibility = Notification.VISIBILITY_PUBLIC;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.setVibrationPattern(new long[]{0});
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.enableLights(true);
            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //startService(new Intent(MyService.this,MyService.class));
        Intent restartService = new Intent(getApplicationContext(),this.getClass());
        restartService.setPackage(getPackageName());
        startService(restartService);
        super.onTaskRemoved(rootIntent);
    }
}