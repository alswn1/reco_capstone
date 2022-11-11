package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mFirebaseAuth;

    // textview 선언
    TextView user_point;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.home);

        // DB에서 유저 포인트 불러오기
        mFirebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        user_point = findViewById(R.id.user_point);

        // DB에서 user_point 데이터 받아와 user_point textview 에 적용
        myRef = database.getReference("USER");
        String uid = mFirebaseAuth.getCurrentUser().getUid();
        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer point = snapshot.child("user_point").getValue(Integer.class);
                user_point.setText(point.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 각 버튼 클릭 시 화면 이동
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

        ImageButton btn_map = (ImageButton) findViewById(R.id.btn_map);
        btn_map.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), Map.class);
        startActivity(intent);
        }
        });

        ImageButton btn_coupon = (ImageButton) findViewById(R.id.btn_coupon);
        btn_coupon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), Coupon.class);
        startActivity(intent);
        }
        });

        ImageButton btn_mypage = (ImageButton) findViewById(R.id.btn_mypage);
        btn_mypage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
        startActivity(intent);
        }
        });
    }
}