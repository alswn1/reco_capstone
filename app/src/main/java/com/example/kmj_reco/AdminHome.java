package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Notice 버튼 터치 시 공지사항 관리 화면으로 이동
        ImageButton btn_notice_admin = (ImageButton) findViewById(R.id.btn_user_notice);
        btn_notice_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticeAdmin.class);
                startActivity(intent);
            }
        });

        // User 버튼 터치 시 사용자 관리 화면으로 이동
        ImageButton btn_user_admin = (ImageButton) findViewById(R.id.btn_user_admin);
        btn_user_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginAdmin.class);
                startActivity(intent);
            }
        });

        // Coupon 버튼 터치 시 기프티콘 관리 화면으로 이동
        ImageButton btn_coupon_admin = (ImageButton) findViewById(R.id.btn_coupon_admin);
        btn_coupon_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CouponAdmin.class);
                startActivity(intent);
            }
        });

        // Recobin 버튼 터치 시 레코빈 관리 화면으로 이동
        ImageButton btn_admin_recobin = (ImageButton) findViewById(R.id.btn_admin_recobin);
        btn_admin_recobin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecobinAdmin.class);
                startActivity(intent);
            }
        });

        // Service Center 버튼 터치 시 문의 관리 화면으로 이동
        ImageButton btn_service_admin = (ImageButton) findViewById(R.id.btn_service_admin);
        btn_service_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceAdmin.class);
                startActivity(intent);
            }
        });
    }
}
