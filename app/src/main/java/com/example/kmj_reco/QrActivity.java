package com.example.kmj_reco;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView recobin_num, recobin_roadname, recobin_address, star_score;

    private IntentIntegrator qrScan;

    // 데이터베이스
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        scanBtn = findViewById(R.id.scanBtn);
        /*recobin_num = findViewById(R.id.recobin_num);
        recobin_roadname = findViewById(R.id.recobin_roadname);
        recobin_address = findViewById(R.id.recobin_address);
        star_score = findViewById(R.id.star_score);*/


        qrScan = new IntentIntegrator(this);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.setBeepEnabled(false);
                qrScan.setPrompt("Scanning...");
                qrScan.initiateScan();
            }
        });
        //scanBtn.performClick();
    }

    // qr 스캔 결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // qrcode가 없을 때
            if (result.getContents() == null) {
                //Toast.makeText(this, "취소!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(QrActivity.this, Home.class);
                startActivity(i);
                // qrcode가 있을 때
            }else {
                //Toast.makeText(this, "스캔 완료!", Toast.LENGTH_SHORT).show();

                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    /*recobin_num.setText(obj.getString("recobin_num"));
                    recobin_roadname.setText(obj.getString("recobin_roadname"));
                    recobin_address.setText(obj.getString("recobin_address"));
                    star_score.setText(obj.getString("star_score"));*/
                    Log.v("test : ", "recobin_num : " + obj.getString("recobin_num"));

                    // qr코드가 db에 있는 qr코드인지 확인

                    // db
                    database = FirebaseDatabase.getInstance();
                    String recobinNum = obj.getString("recobin_num");

                    for (int j=0; j<database.getReference("RECOBIN").getKey().length(); j++) {
                        myRef = database.getReference("RECOBIN/" + j);

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // qr의 recobin_num을 firebase에 저장되어 있는 recobin_num과 비교
                                // 맞다면 화면 이동
                                String recobin_num = snapshot.child("recobin_num").getValue(String.class);
                                //Log.v("RECOBIN_NUM", recobin_num);
                                if (recobinNum.equals(recobin_num)) {
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

                    /*if (obj.getString("recobin_num").equals("1")) {
                        Intent i = new Intent(QrActivity.this, CameraActivity.class);
                        startActivity(i);
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(this, "실패!!!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}