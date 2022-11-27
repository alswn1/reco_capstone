package com.example.kmj_reco;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.DTO.GIFTICONDATA;
import com.example.kmj_reco.DTO.NOTICE;
import com.example.kmj_reco.utils.GifticonAdapter;
import com.example.kmj_reco.utils.NoticeAdapter2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoticeDetail extends AppCompatActivity {
    NOTICE selectedNotice;
    private List<NOTICE> noticeList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        // 공지 화면에서 데이터 받아오기
        Intent intent = getIntent();
        int num = intent.getIntExtra("num", 0);

        // 설정 버튼 터치 시 설정 화면으로 이동
        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 설정으로 이동
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼 터치 시 뒤로가기 화면으로 이동
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 공지로 이동
                Intent intent = new Intent(getApplicationContext(), Notice.class);
                startActivity(intent);
            }
        });

        // DB 내 NOTICE 데이터 불러오기
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("NOTICE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()){
                    NOTICE notice = data.getValue(NOTICE.class);

                    if(notice.getNotice_num() == num){ noticeList.add(notice);} // 테이블과 리스트의 데이터가 일치할 경우 불러옴
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setValues();
    }

    // 데이터 설정
    private void setValues(){
        TextView notice_date = findViewById(R.id.notice_date);
        TextView notice_title = findViewById(R.id.notice_title);
        TextView notice_detail = findViewById(R.id.notice_detail);

        // 공지 화면에서 데이터 받아와 설정
        Intent intent = getIntent();

        String date = intent.getStringExtra("date");
        String title = intent.getStringExtra("title");
        String detail = intent.getStringExtra("detail");

        notice_date.setText(date);
        notice_title.setText(title);
        notice_detail.setText(detail);
    }
}
