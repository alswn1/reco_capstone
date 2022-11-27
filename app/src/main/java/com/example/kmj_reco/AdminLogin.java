package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLogin extends AppCompatActivity {
    private DatabaseReference mDatabaseRef; // 데이터베이스 선언
    private EditText admin_id, admin_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // DB 내 ADMIN에서 데이터 받아오기
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("ADMIN");

        admin_id = findViewById(R.id.admin_id);
        admin_pass = findViewById(R.id.admin_pass);

        // 로그인 버튼 클릭 시 수행
        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // xml 데이터 변수 선언
                String adminID = admin_id.getText().toString();
                Log.v("ADMINID", adminID);
                String adminPass = admin_pass.getText().toString();
                Log.v("ADMINPASS", adminPass);
                
                if (adminID.getBytes().length <= 0 | adminPass.getBytes().length <= 0) { // 빈칸이 없을 경우
                    Toast.makeText(AdminLogin.this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (int i=0; i < snapshot.getChildrenCount(); i++) {
                                // ADMIN 테이블에서 ID, 비밀번호 데이터 받아오기
                                String admin_ID = snapshot.child(String.valueOf(i)).child("admin_id").getValue(String.class);
                                String admin_Pass = snapshot.child(String.valueOf(i)).child("admin_pass").getValue(String.class);

                                // 로그인 성공
                                if (adminID.equals(admin_ID) && adminPass.equals(admin_Pass)) { // ADMIN 테이블 내 데이터와 일치할 경우
                                    Intent intent = new Intent(AdminLogin.this, AdminHome.class);
                                    startActivity(intent);
                                    finish(); // 현재 액티비티 종료

                                } else { // 로그인 실패
                                    Toast.makeText(AdminLogin.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }
        });
    }
}
