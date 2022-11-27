package com.example.kmj_reco;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class QrActivity extends AppCompatActivity {
    private Button scanBtn;
    private TextView recobin_num, recobin_roadname, recobin_address;

    private IntentIntegrator qrScan;

    // 데이터베이스 선언
    FirebaseDatabase database;
    DatabaseReference myRef;

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        // Google AD
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // 홈 버튼 클릭 이벤트
        ImageView btn_home = (ImageView) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 홈으로 이동
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        // 설정 버튼 클릭 이벤트
        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 설정으로 이동
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        // 알림 버튼 클릭 이벤트
        ImageView btn_alert = (ImageView) findViewById(R.id.btn_alert);
        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 알림창으로 이동
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });

        // QR 스캔 버튼
        scanBtn = findViewById(R.id.scanBtn);
        qrScan = new IntentIntegrator(this);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.setBeepEnabled(false);       // beep 소리 끔
                qrScan.setPrompt("Scanning...");    // Scanning... 글씨 화면에 표시
                qrScan.initiateScan();              // 스캔
            }
        });
    }

    // qr 스캔 결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // qrcode가 없을 때
            if (result.getContents() == null) {
                // 홈으로 이동
                Intent i = new Intent(QrActivity.this, Home.class);
                startActivity(i);
            // qrcode가 있을 때
            }else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());

                    // qr코드가 db에 있는 qr 코드인지 확인
                    database = FirebaseDatabase.getInstance();  // db
                    String recobinNum = obj.getString("recobin_num");

                    // RECOBIN의 데이터를 for문으로 돌음
                    for (int j=0; j<database.getReference("RECOBIN").getKey().length(); j++) {
                        myRef = database.getReference("RECOBIN/" + j);

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // qr의 recobin_num을 firebase에 저장되어 있는 recobin_num과 비교
                                int recobin_num = snapshot.child("recobin_num").getValue(Integer.class);
                                String fir_recobin_num = String.valueOf(recobin_num);
                                // 맞다면 화면 이동
                                if (recobinNum.equals(fir_recobin_num)) {
                                    Intent i = new Intent(QrActivity.this, CameraActivity.class);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // 에러문 출력
                                Log.e("QrActivity", String.valueOf(error.toException()));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}