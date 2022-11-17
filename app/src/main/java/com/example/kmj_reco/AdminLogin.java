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
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    private EditText admin_id, admin_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("ADMIN");

        admin_id = findViewById(R.id.admin_id);
        admin_pass = findViewById(R.id.admin_pass);

        // 로그인 버튼 클릭시 수행
        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adminID = admin_id.getText().toString();
                Log.v("ADMINID", adminID);
                String adminPass = admin_pass.getText().toString();
                Log.v("ADMINPASS", adminPass);

                if (adminID.getBytes().length <= 0 | adminPass.getBytes().length <= 0) {
                    Toast.makeText(AdminLogin.this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (int i=0; i < snapshot.getChildrenCount(); i++) {
                                String admin_ID = snapshot.child(String.valueOf(i)).child("admin_id").getValue(String.class);

                                if (adminID.equals(admin_ID)) { // 로그인 성공
                                    Intent intent = new Intent(AdminLogin.this, AdminHome.class);
                                    startActivity(intent);
                                    finish(); // 현재 액티비티 파괴

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
