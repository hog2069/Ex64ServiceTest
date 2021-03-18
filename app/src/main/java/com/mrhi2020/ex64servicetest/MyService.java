package com.mrhi2020.ex64servicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

//Service는 안드로이드의 4대 컴포넌트여서 Mainfest.xml 에 등록
public class MyService extends Service {

    MediaPlayer mp;

    //서비스가 시작되면(startService()로 실행된) 자동으로 호출되는 메소드
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();

        //Oreo 버전이상에서는 "foreground Service"로 실행해야함
        // "foreground Service"로 실행되는 서비스는 사용자가 동작중임을 강제로
        // 인지할 수 있도록 하기위해 반드시 알림(Notification)을 보여줘야만 함.
//        if(Build.VERSION.SDK_INT>=26){
//        }else{
//        }
        //원래는 버전에 따라 포어그라운드 서비스를 실행할지를 결정해야 하지만
        //예전버전 디바이스에 startForegroundService() 명령을 내려도 문제가 없기에
        //그냥 편하게 무조건 알림을 보이도록 코딩!!

        NotificationManager notificationManager= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel("ch1","channel 1", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

            builder= new NotificationCompat.Builder(this, "ch1");
        }else{
            builder= new NotificationCompat.Builder(this, null);
        }

        builder.setSmallIcon(R.drawable.ic_noti);
        builder.setContentTitle("Music Service");
        builder.setContentText("뮤직서비스가 실행됩니다.");

        //알림(Notification)을 클릭하여 서비스를 제어할 수 있는
        //버튼이 있는 MainActivity 가 실행될 수 있도록..보류중인 인텐트 설정
        Intent intent1= new Intent(this, MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification= builder.build();
        //포어그라운드로 실행되도록..
        startForeground(1, notification);

        //MediaPlayer 생성 및 실행
        if(mp==null){
            mp= MediaPlayer.create(this, R.raw.kalimba);
            mp.setVolume(0.7f, 0.7f);
            mp.setLooping(true);
        }

        mp.start();

        //메모리문제로 프로세스가 강제로 killed 되는 경우가 있음.
        // 다시 메모리 문제가 해결되면 자동으로 다시 서비스가 실행됨..
        //이때 자동으로 다시 실행할 것인지..아닌지에 대한 여부를 상수값으로 리턴
        return START_STICKY;
    }


    //서비스가 종료되면 자동으로 호출되는 메소드 [stopService() 실행]
    @Override
    public void onDestroy() {

        //MediaPlayer를 종료
        if(mp!=null){
            mp.stop();
            mp.release();
            mp= null;
        }

        //포어그라운드 서비스를 멈추도록 하며 알림을 제거하도록 true 전달
        stopForeground(true);

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
