package com.example.kmj_reco;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.NOTICE;
import com.example.kmj_reco.DTO.SERVICE_ANSWER;
import com.example.kmj_reco.DTO.ServiceAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdminDetail extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth; // 파이어 베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private FirebaseUser user;
    ServiceAccount selectedService;
    private List<ServiceAccount> serviceAccountList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_admin_detail);
        Intent intent = getIntent();

        int num = intent.getIntExtra("num", 0);

        // 뒤로가기 버튼 클릭 이벤트
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 고객센터 어드민으로 이동
                Intent intent = new Intent(getApplicationContext(), ServiceAdmin.class);
                startActivity(intent);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ServiceAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int service_num = (int) (snapshot.getChildrenCount());

                for (DataSnapshot data: snapshot.getChildren()){
                    ServiceAccount service = data.getValue(ServiceAccount.class);
                    if(service.getService_Num()==num){
                        serviceAccountList.add(service);
                    }

                    // 답변 남기기
                    FirebaseDatabase database;
                    database = FirebaseDatabase.getInstance();  // db
                    database.getReference("SERVICE_ANSWER").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // answer_num을 length로 설정
                            final int answer_num = (int) snapshot.getChildrenCount();

                            // 작성 버튼 클릭 이벤트
                            Button btn_service_admin_submit = (Button) findViewById(R.id.btn_service_admin_submit);
                            btn_service_admin_submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 어드민이 남긴 답변
                                    EditText service_admin_answer = findViewById(R.id.service_admin_answer);

                                    // SERVICE_ANSWER 생성자에 인덱스, 답변, 문의번호를 저장
                                    SERVICE_ANSWER service_answer = new SERVICE_ANSWER((answer_num+1), service_admin_answer.getText().toString(), num);
                                    // SERVICE_ANSWER에 데이터 삽입
                                    database.getReference("SERVICE_ANSWER").child(String.valueOf(answer_num)).setValue(service_answer);

                                    Toast.makeText(ServiceAdminDetail.this, "답변을 작성했습니다.", Toast.LENGTH_SHORT).show();
                                    // 답변 작성 성공하면 고객센터 어드민으로 이동
                                    Intent i = new Intent(getApplicationContext(), ServiceAdmin.class);
                                    startActivity(i);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // 에러문 출력
                            Log.e("ServiceAdminDetail", String.valueOf(error.toException()));
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 에러문 출력
                Log.e("ServiceAdminDetail", String.valueOf(error.toException()));
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
