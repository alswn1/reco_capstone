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
    FirebaseDatabase database;
    DatabaseReference reference;
    ListView alertListView;

    static List<ALERT> alertList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        String uid = FirebaseAuth.getInstance().getUid();

        reference.child("ALERT").limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alertList.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    ALERT alert = data.getValue(ALERT.class);
                    if(alert.getuser_Name().equals(uid)){
                        alertList.add(0, alert);}
                }
                final NoticeAdapter noticeAdapter = new NoticeAdapter(getApplicationContext(),R.layout.item_notice, alertList,alertListView);
                alertListView.setAdapter(noticeAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        ImageButton btn_quiz = (ImageButton) findViewById(R.id.btn_quiz);
        btn_quiz.setVisibility(View.VISIBLE);
        btn_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuizMain.class);
                startActivity(intent);
            }
        });
        // 더미 데이터
        if (alertList.size()<=0) {}
        alertListView= (ListView) findViewById(R.id.notice2ListView);
    }
}