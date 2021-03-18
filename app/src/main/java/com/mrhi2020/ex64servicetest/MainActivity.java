package com.mrhi2020.ex64servicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    // Service : Background 작업을 위해 만들어진 클래스 [android의 4대 컴포넌트]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickStart(View view) {
        // 액티비티에서 직접 MediaPlayer를 쓰면 액티비티를 종료한 후 베터리 관리
        // 문제로 잠시 뒤 MediaPlayer도 같이 멈춤. 또한 참조변수가 없어지므로
        // 제어할 수 없음.

        // 백그라운드에서도 동작하는 클래스인 Service를 실행하여 그 곳에서
        // MediaPlayer 실행하도록...
        // [Service 객체를 만드는 것이 아니라 별개의 앱을 실행시켜달라는 느낌의 코드임]
        Intent intent= new Intent(this, MyService.class);

        //Oreo버전부터 그냥 서비스를 실행하면 일정시간 후에 OS에 서비스를 종료시켜버림[베터리 관리 이유로]
        //만약 자동으로 종료되고 싶지 않다면.. "foreground Service" 로 실행시켜야 함
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) startForegroundService(intent);
        else startService(intent);

    }

    public void clickStop(View view) {
        //서비스 객체에 대한 참조변수로 제어하는 것이 아니라
        //Intent를 통해서 제어하므로 액티비티에서 서비스 참조변수 삭제에 대한 문제 없음.
        Intent intent= new Intent(this, MyService.class);
        stopService(intent);

    }
}