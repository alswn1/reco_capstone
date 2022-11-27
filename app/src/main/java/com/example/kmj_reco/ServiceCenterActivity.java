package com.example.kmj_reco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kmj_reco.DTO.ServiceAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ServiceCenterActivity extends AppCompatActivity {
    FirebaseDatabase database; // 데이터베이스 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);

        database = FirebaseDatabase.getInstance(); //db

        // ServiceAccount 데이터 받아오기
        database.getReference("ServiceAccount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int service_num = (int) (snapshot.getChildrenCount());

                // 문의 제출 버튼 클릭 이벤트
                Button btn_service_submit = (Button) findViewById(R.id.btn_submit);
                btn_service_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // xml 요소
                        EditText et_title = findViewById(R.id.et_title);
                        EditText et_content = findViewById(R.id.et_content);
                        EditText et_publisher = findViewById(R.id.et_publisher);
                        EditText et_date = findViewById(R.id.et_date);

                        // ServiceAccount 생성자에 저장
                        ServiceAccount serviceAccount = new ServiceAccount(et_title.getText().toString(), et_content.getText().toString(), et_publisher.getText().toString(), service_num, et_date.getText().toString());
                        database.getReference("ServiceAccount").child(String.valueOf(service_num)).setValue(serviceAccount); // 테이블에 데이터 삽입

                        Toast.makeText(ServiceCenterActivity.this, "새로운 문의사항을 등록했습니다.",Toast.LENGTH_SHORT).show();

                        // 작성 완료 시 문의내역 화면으로 이동
                        Intent i = new Intent(getApplicationContext(), ServiceHistoryActivity.class);
                        startActivity(i);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 뒤로가기 버튼 터치 시 마이 페이지 화면으로 이동
        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(i);
            }
        });

        // 홈 버튼 터치 시 홈 화면으로 이동
        ImageView btn_home = (ImageView) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        // 설정 버튼 터치 시 설정 화면으로 이동
        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        // 알림 버튼 터치 시 알림 화면으로 이동
        ImageView btn_alert = (ImageView) findViewById(R.id.btn_alert);
        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });

        // 게시판 버튼 터치 시 게시판 화면으로 이동
        Button btn_history = (Button) findViewById(R.id.btn_history);
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ServiceHistoryActivity.class);
                startActivity(i);
            }
        });
    }
}