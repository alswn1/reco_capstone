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
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);

        database = FirebaseDatabase.getInstance(); //db
        database.getReference("ServiceAccount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int service_num = (int) (snapshot.getChildrenCount());

                Button btn_service_submit = (Button) findViewById(R.id.btn_submit);
                btn_service_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText et_title = findViewById(R.id.et_title);
                        EditText et_content = findViewById(R.id.et_content);
                        EditText et_publisher = findViewById(R.id.et_publisher);
                        EditText et_date = findViewById(R.id.et_date);

                        ServiceAccount serviceAccount = new ServiceAccount(et_title.getText().toString(), et_content.getText().toString(), et_publisher.getText().toString(), service_num, et_date.getText().toString());
                        database.getReference("ServiceAccount").child(String.valueOf(service_num)).setValue(serviceAccount);

                        Toast.makeText(ServiceCenterActivity.this, "새로운 문의사항을 등록했습니다.",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), ServiceHistoryActivity.class);
                        startActivity(i);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 취소 시 홈 화면 이동
        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(i);
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