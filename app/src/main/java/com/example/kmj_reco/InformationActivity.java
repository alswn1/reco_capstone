package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InformationActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스

    // 데이터베이스 선언
    FirebaseDatabase database;
    DatabaseReference myRef;

    EditText et_name;
    EditText et_birth;
    EditText et_id;
    EditText et_pass;
    EditText et_phone;
    EditText et_email;

    // 현재 로그인한 사용자 정보 불러오는 법
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        mFirebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("USER");

        et_name = findViewById(R.id.et_name);
        et_birth = findViewById(R.id.et_birth);
        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uid = mFirebaseAuth.getCurrentUser().getUid();

                String username = snapshot.child(uid).child("user_name").getValue(String.class);
                et_name.setText(username);

                String userbirth = snapshot.child(uid).child("user_birth").getValue(String.class);
                et_birth.setText(userbirth);

                String userpwd = snapshot.child(uid).child("user_pwd").getValue(String.class);
                et_pass.setText(userpwd);

                String userid = snapshot.child(uid).child("user_id").getValue(String.class);
                et_id.setText(userid);

                String useremail = snapshot.child(uid).child("user_email").getValue(String.class);
                et_email.setText(useremail);

                String userphone = snapshot.child(uid).child("user_phone").getValue(String.class);
                et_phone.setText(userphone);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
            }
        });
        ImageView btn_home = (ImageView) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
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

        ImageView btn_alert = (ImageView) findViewById(R.id.btn_alert);
        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_check).setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener =(v) ->{
        switch (v.getId()){
            case R.id.btn_check:
                informationUpdate();
                break;
        }
    };
    private void informationUpdate(){
        String userName = ((EditText) findViewById(R.id.et_name)).getText().toString();
        String userBirth = ((EditText) findViewById(R.id.et_birth)).getText().toString();
        String userID = ((EditText) findViewById(R.id.et_id)).getText().toString();
        String userPass = ((EditText) findViewById(R.id.et_pass)).getText().toString();
        String userPass2 = ((EditText) findViewById(R.id.et_pass2)).getText().toString();
        String userPhone = ((EditText) findViewById(R.id.et_phone)).getText().toString();
        String userEmail = ((EditText) findViewById(R.id.et_email)).getText().toString();

        if (userName.length() > 0 && userBirth.length() >0 && userID.length() > 0 && userPass.length() > 5 && userPass2.length() > 5 && userPhone.length() >9 && userEmail.length() > 9){
            FirebaseUser user = mFirebaseAuth.getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName("userName")
                    .build();

            if(user !=null){
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    startToast("회원정보 수정에 성공하였습니다.");
                                }
                            }
                        });
            }
        } else{
            startToast("회원정보를 입력해 주세요.");
        }
    }
    private void startToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();}
}
