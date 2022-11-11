package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.USER;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간데이터베이스

    private EditText et_name, et_birth, et_id, et_pass, et_pass2, et_phone, et_email; //회원가입입력필드
    private Button btn_check; //회원가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {//액티비티 시작시 처음으로 실행되는 생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        TextView goToPersonalInfo = (TextView) findViewById(R.id.goToPersonalInfo);
        goToPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), PersonalInfo.class);
                startActivity(intent);
            }
        });

        mFirebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("USER");

        //아이디 값 찾아주기
        et_name = findViewById(R.id.et_name);
        et_birth = findViewById(R.id.et_birth);
        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_pass2 = findViewById(R.id.et_pass2);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);

        //회원가입 버튼 클릭 시 수행
        Button btn_check=(Button) findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리 시작
                //EditText에 현재 입력되어있는 값을 get(가져온다)
                String userName = et_name.getText().toString();
                String userBirth = et_birth.getText().toString();
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
                String userPass2 = et_pass2.getText().toString();
                String userPhone = et_phone.getText().toString();
                String userEmail = et_email.getText().toString();

                mFirebaseAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendEmailVerification();

                            if (userPass.equals(userPass2)) {
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                USER account = new USER();
                                account.setIdToken(firebaseUser.getUid());
                                account.setUser_name(userName);
                                account.setUser_birth(userBirth);
                                account.setUser_id(userID);
                                account.setUser_pwd(userPass);
                                account.setUser_phone(userPhone);
                                account.setUser_email(firebaseUser.getEmail());

                                //setValue는 database에 insert
                                mDatabaseRef.child(firebaseUser.getUid()).setValue(account);
                                Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show();

                                // 회원가입 성공 후 버튼 클릭 시 로그인 화면으로 이동
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });
    }

    private void sendEmailVerification() {
        mFirebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "확인메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, task.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}