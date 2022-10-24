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

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private EditText et_id, et_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);

        // 자동 로그인
        if (mFirebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, Home.class);
            startActivity(intent);
            finish();
        }

        // 회원가입 버튼을 클릭 시 수행
        Button btn_register = (Button) findViewById(R.id.btn_check);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼 클릭시 수행
        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = et_id.getText().toString();
                Log.v("USERID", userID);
                String userPass = et_pass.getText().toString();
                Log.v("USERPASS", userPass);

                if (userID.getBytes().length <= 0 | userPass.getBytes().length <= 0) {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {

                        mFirebaseAuth.signInWithEmailAndPassword(userID, userPass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).isEmailVerified()) {
                                    Log.v("Email", "이메일 인증 확인");
                                    if (task.isSuccessful()) {
                                        // 로그인 성공
                                        Intent intent = new Intent(LoginActivity.this, Home.class);
                                        startActivity(intent);
                                        finish(); // 현재 액티비티 파괴

                                    } else {// 로그인 실패
                                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Log.v("Email", "이메일 인증 미확인");
                                    Toast.makeText(LoginActivity.this, "이메일 인증을 진행해주세요.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
        });
    }
}