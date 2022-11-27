package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.utils.NoticeAdapter;
import com.example.kmj_reco.DTO.ALERT;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Alert extends AppCompatActivity {
    ListView alertListView;

    // DB
    FirebaseDatabase database;
    DatabaseReference reference;

    static List<ALERT> alertList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        // db
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // 현재 로그인한 사용자 uid 가져오기
        String uid = FirebaseAuth.getInstance().getUid();

        alertListView = findViewById(R.id.notice2ListView);

        // 알림 데이터 가져오기
        reference.child("ALERT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 리스트 초기화
                alertList.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    ALERT alert = data.getValue(ALERT.class);
                    // 해당 사용자의 알림만 가져오기
                    if(alert.getuser_Name().equals(uid)){
                        // 최신순으로 표시되게끔 추가
                        alertList.add(0, alert);}
                }
                // 목록에 알림 표시
                final NoticeAdapter noticeAdapter = new NoticeAdapter(getApplicationContext(),R.layout.item_notice, alertList, alertListView);
                alertListView.setAdapter(noticeAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        // 뒤로가기 버튼 클릭 이벤트
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        // 퀴즈 버튼 클릭 이벤트
        ImageButton btn_quiz = (ImageButton) findViewById(R.id.btn_quiz);
        btn_quiz.setVisibility(View.VISIBLE);
        btn_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 퀴즈 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), QuizMain.class);
                startActivity(intent);
            }
        });
    }
}