package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
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
    FirebaseDatabase database;
    DatabaseReference reference;
    ListView notice2ListView;

    static List<NOTICE> noticeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        ImageButton btn_event = (ImageButton) findViewById(R.id.btn_event);
        btn_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Event.class);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference.child("NOTICE").limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noticeList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    NOTICE notice = data.getValue(NOTICE.class);
                    noticeList.add(notice);
                }
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
                NOTICE selectedNotice = (NOTICE) notice2ListView.getItemAtPosition(position2);
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