package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.USER_SETTING;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {
    //db
    private DatabaseReference reference;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 뒤로가기 버튼 클릭 이벤트
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 홈화면으로 이동
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        // 고객센터 텍스트 클릭 이벤트
        TextView settingTextToCenter = (TextView) findViewById(R.id.settingTextToCenter);
        settingTextToCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 고객센터로 이동
                Intent intent = new Intent(getApplicationContext(), ServiceCenterActivity.class);
                startActivity(intent);
            }
        });

        // 개인정보처리방침 텍스트 클릭 이벤트
        TextView goToPersonalInfo = (TextView) findViewById(R.id.goToPersonalInfo);
        goToPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 개인정보 처리방침으로 이동
                Intent intent = new Intent(getApplicationContext(), PersonalInfo.class);
                startActivity(intent);
            }
        });

        // 사용자 정보 확인
        FirebaseAuth.getInstance().signInAnonymously();
        // db
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // 사용자의 uid 가져옴
        String user_num = FirebaseAuth.getInstance().getUid();

        // 푸시 알람
        Switch push_switch = findViewById(R.id.push_switch);
        //
        reference.child("USER_SETTING").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 해당 사용자의 USER_SETTING이 존재하지 않을 경우
                // 해당 사용자의 USER_SETTING을 추가
                if(snapshot.child(user_num).exists()==false) {
                    reference.child("USER_SETTING").child(user_num).setValue(new USER_SETTING(user_num, 1, 1));
                }
                USER_SETTING user = snapshot.child(user_num).getValue(USER_SETTING.class);

                // 푸시 알람 설정이 1일 경우
                if (user.getPushalarm()==1){
                    // 스위치를 체크.
                    push_switch.setChecked(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        push_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    // 스위치를 체크했을 경우
                    // 푸시 알람 설정 값을 1로.
                    reference.child("USER_SETTING").child(user_num).child("pushalarm").setValue(1);
                    Toast.makeText(getApplicationContext(),"푸시 알람을 설정하셨습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    // 스위치를 체크하지 않았을 경우
                    // 푸시 알람 설정 값을 0으로.
                    reference.child("USER_SETTING").child(user_num).child("pushalarm").setValue(0);
                    Toast.makeText(getApplicationContext(),"푸시 알람을 해제하셨습니다.",Toast.LENGTH_SHORT).show();}
            }
        });
    }
}