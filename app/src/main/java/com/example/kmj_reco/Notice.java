package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.GIFTICONDATA;
import com.example.kmj_reco.DTO.NOTICE;
import com.example.kmj_reco.utils.NoticeAdapter2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Notice extends AppCompatActivity {
    // 데이터베이스 선언
    FirebaseDatabase database;
    DatabaseReference reference;

    ListView notice2ListView;

    static List<NOTICE> noticeList = new ArrayList<>();

    public int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        // 설정 버튼 터치 시 설정 화면으로 이동
        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼 터치 시 마이 페이지 화면으로 이동
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
            }
        });

        // 이벤트 버튼 터치 시 이벤트 화면으로 이동
        ImageButton btn_event = (ImageButton) findViewById(R.id.btn_event);
        btn_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Event.class);
                startActivity(intent);
            }
        });

        // DB 설정
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // DB 내 NOTICE 테이블에서 데이터 불러오기
        reference.child("NOTICE").limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noticeList.clear(); // 공지 리스트 초기화

                count = (int) snapshot.getChildrenCount(); // 테이블 내 데이터 수

                for(DataSnapshot data : snapshot.getChildren()){
                    NOTICE notice = data.getValue(NOTICE.class);
                    noticeList.add(0, notice);
                }

                // 어댑터에서 데이터 불러오기
                final NoticeAdapter2 noticeAdapter2 = new NoticeAdapter2(getApplicationContext(),R.layout.item_notice2, noticeList, notice2ListView);
                notice2ListView.setAdapter(noticeAdapter2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 더미 데이터
        if (noticeList.size()<=0) {}
        notice2ListView = (ListView) findViewById(R.id.notice2ListView);

        setUpOnClickListener();
    }

    // 아이템 클릭 시 NoticeDetail 화면에 각 데이터 전송
    private void setUpOnClickListener() {
        notice2ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position2, long l) {
                // 공지 데이터 전송 및 이동
                NOTICE selectedNotice = (NOTICE) notice2ListView.getItemAtPosition(count - position2 - 1);
                Intent showDetail = new Intent(getApplicationContext(), NoticeDetail.class);
                showDetail.putExtra("num", selectedNotice.getNotice_num());
                showDetail.putExtra("title", selectedNotice.getNotice_title());
                showDetail.putExtra("date", selectedNotice.getNotice_date());
                showDetail.putExtra("detail", selectedNotice.getNotice_detail());
                startActivity(showDetail);
            }
        });
    }
}