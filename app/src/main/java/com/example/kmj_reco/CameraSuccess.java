package com.example.kmj_reco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class CameraSuccess extends AppCompatActivity {

    // 뒤로 가기 막기
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(this, "이 화면에서는 뒤로가기 버튼을 사용하실 수 없습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_success);

        // 홈 화면으로 돌아가기 버튼 클릭 이벤트
        Button backHome = findViewById(R.id.backHome);
        backHome.setOnClickListener(view -> {
            // 홈으로 이동
            Intent i = new Intent(CameraSuccess.this, Home.class);
            startActivity(i);
        });

        // RECO 글씨 클릭 이벤트
        ImageView btn_home = (ImageView) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 홈으로 이동
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        // 톱니바퀴 버튼 클릭 이벤트
        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 설정으로 이동
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        // 종 버튼 클릭 이벤트
        ImageView btn_alert = (ImageView) findViewById(R.id.btn_alert);
        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 알림창으로 이동
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });
    }
}