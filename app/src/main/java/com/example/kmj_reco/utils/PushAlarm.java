package com.example.kmj_reco.utils;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.example.kmj_reco.Home;
import com.example.kmj_reco.R;

public class PushAlarm {
    Context context;
    View v;

    public PushAlarm(Context context, View v){
        this.context = context;
        this.v = v;
    }

    public Notification pushAlarm(String title, String messageBody) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this.v.getRootView().getContext(), 0,
                new Intent(this.v.getRootView().getContext(), Home.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification noti =
                new NotificationCompat.Builder(this.context, "AlertChannel")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri).setContentIntent(mPendingIntent).build();

        return noti;
    }

    public NotificationManager createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name="AlertChannel";
            String description="AlertChanneld";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel((String) name, description, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) this.v.getRootView().getContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            channel.enableLights(true);
            channel.setDescription("AlertChanneld");

            NotificationManager mNotificationManager = (NotificationManager) v.getRootView().getContext().getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationManager.createNotificationChannel(channel);
            }
            return  mNotificationManager;
        }
        return null;
    }
}



