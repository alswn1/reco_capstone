package com.example.kmj_reco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CameraSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_success);

        // 홈 화면으로 돌아가기 클릭했을 때
        Button backHome = findViewById(R.id.backHome);
        backHome.setOnClickListener(view -> {
            // Home으로 이동
            Intent i = new Intent(CameraSuccess.this, Home.class);
            startActivity(i);
        });

        // RECO 클릭했을 때
        ImageView btn_home = (ImageView) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        // 톱니바퀴 클릭했을 때
        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        // 종 클릭했을 때
        ImageView btn_alert = (ImageView) findViewById(R.id.btn_alert);
        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });
    }
}