package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyPageActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간데이터베이스
    FirebaseDatabase database;
    TextView user_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        mFirebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        user_point = findViewById(R.id.user_point);

        mDatabaseRef = database.getReference("USER");
        String uid = mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer point = snapshot.child("user_point").getValue(Integer.class);
                user_point.setText(point.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        ImageView btn_home = (ImageView) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        ImageView btn_alert = (ImageView) findViewById(R.id.btn_alert);
        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });

        // 상단바 설정 버튼 클릭 시 설정 액티비티 전환
        ImageView btn_setting = (ImageView) findViewById(R.id.btn_settings);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        // 상단바 뒤로가기 버튼 클릭 시 홈 액티비티 전환
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        // 개인정보 수정 버튼 클릭 시 개인정보 액티비티 전환
        Button btn_modification = (Button) findViewById(R.id.btn_modification);
        btn_modification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InformationActivity.class);
                startActivity(intent);
            }
        });

        // 기프티콘 버튼 클릭 시 기프티콘 액티비티 전환
        Button btn_gifticon = (Button) findViewById(R.id.btn_gifticon);
        btn_gifticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CouponHistory.class);
                startActivity(intent);
            }
        });

        // 고객센터 버튼 클릭 시 고객센터 액티비티 전환
        Button btn_service = (Button) findViewById(R.id.btn_service);
        btn_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceCenterActivity.class);
                startActivity(intent);
            }
        });

        // 공지사항 버튼 클릭 시 공지사항 액티비티 전환
        Button btn_notice = (Button) findViewById(R.id.btn_notice);
        btn_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Notice.class);
                startActivity(intent);
            }
        });

        // 로그아웃 버튼 클릭 시 로그아웃 후 로그인 화면으로 이동
        Button btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFirebaseAuth.getCurrentUser() != null) {
                    mFirebaseAuth.signOut();
                }
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}