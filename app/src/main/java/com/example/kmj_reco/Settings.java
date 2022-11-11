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

        TextView settingTextToCenter = (TextView) findViewById(R.id.settingTextToCenter);
        settingTextToCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceCenterActivity.class);
                startActivity(intent);
            }
        });

        TextView goToPersonalInfo = (TextView) findViewById(R.id.goToPersonalInfo);
        goToPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PersonalInfo.class);
                startActivity(intent);
            }
        });

        FirebaseAuth.getInstance().signInAnonymously();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // user_num 혹은 해당 유저의 key 받아오기
        String user_num = FirebaseAuth.getInstance().getUid();
        Switch push_switch = findViewById(R.id.push_switch);

        reference.child("USER_SETTING").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user_num).exists()==false) {
                    reference.child("USER_SETTING").child(user_num).setValue(new USER_SETTING(user_num, 1, 1));
                }

                USER_SETTING user = snapshot.child(user_num).getValue(USER_SETTING.class);

                if (user.getPushalarm()==1){
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
                    reference.child("USER_SETTING").child(user_num).child("pushalarm").setValue(1);
                    Toast.makeText(getApplicationContext(),"알림을 설정하셨습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    reference.child("USER_SETTING").child(user_num).child("pushalarm").setValue(0);
                    Toast.makeText(getApplicationContext(),"알림을 해제하셨습니다.",Toast.LENGTH_SHORT).show();}
            }
        });
    }
}