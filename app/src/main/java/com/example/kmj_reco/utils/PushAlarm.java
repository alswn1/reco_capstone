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


// 푸시 알림 설정
public class PushAlarm {
    Context context;
    View v;

    public PushAlarm(Context context, View v){
        this.context = context;
        this.v = v;
    }


    // 푸시 알람 생성 함수:
    // 제목과 메시지를 받아 푸시 알람을 생성한다.
    public Notification pushAlarm(String title, String messageBody) {
        // 푸시 알림 소리 기본 설정
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // 푸시 알람을 클릭할 경우 홈 화면으로 이동하도록 설정
        PendingIntent mPendingIntent = PendingIntent.getActivity(this.v.getRootView().getContext(), 0,
                new Intent(this.v.getRootView().getContext(), Home.class), PendingIntent.FLAG_UPDATE_CURRENT);
        // 알림 클래스 생성
        // icon 설정
        // 사용자가 알림을 터치하면 삭제되게 설정
        // 알림 채널을 설정하고 받은 제목과 내용 set
        Notification noti =
                new NotificationCompat.Builder(this.context, "AlertChannel")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri).setContentIntent(mPendingIntent).build();

        return noti;
    }

    // NotificationChannel을 생성하는 함수
    // 푸시알람이 필요한 구간에서 실행 후 NotificationManager를 return
    // NotificationManager 이용해 생성된 푸시 알람 실행
    public NotificationManager createNotificationChannel() {
        // 핸드폰 버전이 일정 수준 이상일 경우 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 채널 이름과 아아디
            CharSequence name="AlertChannel";
            String description="AlertChanneld";
            // 중요도 수준
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            // 채널 객체 생성
            NotificationChannel channel = new NotificationChannel((String) name, description, importance);
            channel.setDescription(description);

            // 함수 자가 실행을 위한 NotificationManager 객체 생성
           NotificationManager notificationManager = (NotificationManager) this.v.getRootView().getContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            channel.enableLights(true);
            channel.setDescription("AlertChanneld");

            // NotificationManager 객체 생성
            NotificationManager mNotificationManager = (NotificationManager) v.getRootView().getContext().getSystemService(NOTIFICATION_SERVICE);
                mNotificationManager.createNotificationChannel(channel);
            return  mNotificationManager;
        }
        return null;
    }
}



