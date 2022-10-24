package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
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
    private DatabaseReference reference;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        FirebaseAuth.getInstance().signInAnonymously();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

//        user_num 혹은 해당 유저의 key 받아오기
        String user_num = FirebaseAuth.getInstance().getUid();

        reference.child("USER_SETTING").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("태그", snapshot.getKey());
                if(snapshot.child(user_num).exists()==false) {
                    reference.child("USER_SETTING").child(user_num).setValue(new USER_SETTING(user_num, 0, 1, 1, 1));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Switch login_switch = findViewById(R.id.login_switch);
        Switch push_switch = findViewById(R.id.push_switch);

        login_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){

                    reference.child("USER_SETTING").child(user_num).child("setAutologin").setValue(1);
                    Toast.makeText(getApplicationContext(),"자동 로그인을 설정하셨습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    reference.child("USER_SETTING").child(user_num).child("setAutologin").setValue(0);
                    Toast.makeText(getApplicationContext(),"자동 로그인을 해제하셨습니다.",Toast.LENGTH_SHORT).show();}
            }});

        push_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    reference.child("USER_SETTING").child(user_num).child("setPushalarm").setValue(1);
                    Toast.makeText(getApplicationContext(),"푸시알림을 설정하셨습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    reference.child("USER_SETTING").child(user_num).child("setPushalarm").setValue(0);
                    Toast.makeText(getApplicationContext(),"푸시알림을 해제하셨습니다.",Toast.LENGTH_SHORT).show();}
            }
        });
    }
}