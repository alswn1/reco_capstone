package com.example.kmj_reco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kmj_reco.DTO.NOTICE;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NoticeAdmin extends AppCompatActivity {
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_admin);

        database = FirebaseDatabase.getInstance(); // db
        // "NOTICE"로 경로 설정
        database.getReference("NOTICE").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // notice_num = (length) + 1
                int notice_num = (int) (snapshot.getChildrenCount() + 1);

                // 작성 버튼 클릭 이벤트
                Button btn_notice_submit = (Button) findViewById(R.id.btn_notice_submit);
                btn_notice_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText txt_date = findViewById(R.id.txt_date);            // 날짜
                        EditText txt_title = findViewById(R.id.txt_title);          // 제목
                        EditText txt_contents = findViewById(R.id.txt_contents);    // 내용

                        // NOTICE 생성자에 저장
                        NOTICE notice = new NOTICE(txt_date.getText().toString(), txt_contents.getText().toString(), notice_num, txt_title.getText().toString());
                        // 테이블에 데이터 삽입
                        database.getReference("NOTICE").child(String.valueOf(notice_num-1)).setValue(notice);

                        Toast.makeText(NoticeAdmin.this, "새로운 공지사항을 등록했습니다.", Toast.LENGTH_SHORT).show();
                        // 작성 완료 하면 어드민 홈으로 이동
                        Intent i = new Intent(getApplicationContext(), AdminHome.class);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 뒤로가기 버튼 클릭 이벤트
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 어드민 홈으로 이동
                Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                startActivity(intent);
            }
        });

        // 취소 버튼 클릭 이벤트
        Button btn_notice_cancel = (Button) findViewById(R.id.btn_notice_cancel);
        btn_notice_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 어드민 홈으로 이동
                Intent i = new Intent(getApplicationContext(), AdminHome.class);
                startActivity(i);
            }
        });
    }
}