package com.example.kmj_reco;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.ServiceAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ServiceDetailActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간데이터베이스
    private FirebaseUser user;
    ServiceAccount selectedService;
    private List<ServiceAccount> serviceAccountList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        Intent intent = getIntent();
        int num = intent.getIntExtra("num", 0);

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

        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceHistoryActivity.class);
                startActivity(intent);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ServiceAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()){
                    ServiceAccount service = data.getValue(ServiceAccount.class);
                    if(service.getService_Num()==num){
                        serviceAccountList.add(service);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        databaseReference.child("SERVICE_ANSWER").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i<snapshot.getChildrenCount(); i++) {
                    TextView service_admin_answer = findViewById(R.id.service_admin_answer);

                    if (snapshot.child(String.valueOf(i)).child("service_num").getValue(Integer.class).equals(num)) {
                        Log.v("ANSWER", "같은 문의 번호 찾음");
                        String answer = snapshot.child(String.valueOf(i)).child("service_answer").getValue(String.class);
                        service_admin_answer.setText(answer);
                        Log.v("ANSWER", "답변 바꾸기 성공");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setValues();
    }

    private void setValues(){
        TextView date = findViewById(R.id.et_date);
        TextView title = findViewById(R.id.et_title);
        TextView contents = findViewById(R.id.et_content);
        TextView publisher = findViewById(R.id.et_publisher);

        Intent intent = getIntent();

        String title1 = intent.getStringExtra("title");
        String date1 = intent.getStringExtra("date");
        String contents1 = intent.getStringExtra("contents");
        String publisher1 = intent.getStringExtra("publisher");

        date.setText(date1);
        title.setText(title1);
        contents.setText(contents1);
        publisher.setText(publisher1);
    }
}