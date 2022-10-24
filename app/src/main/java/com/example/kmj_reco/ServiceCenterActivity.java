package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.ServiceAccount;
import com.example.kmj_reco.utils.MyRecyclerViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ServiceCenterActivity extends AppCompatActivity {
    private static final String TAG = "ServiceCenterActivity";

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간데이터베이스
    private FirebaseUser user;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    private ServiceAccount ServiceAccount;
    private Object User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);


        Button btn_history = (Button) findViewById(R.id.btn_history);
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceHistoryActivity.class);
                startActivity(intent);
            }
        });

        // 제출하기 버튼 누름
        findViewById(R.id.btn_submit).setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.btn_submit:
                    serviceWrite();
                    break;
            }

        }
    };

    private void serviceWrite() { // 고객센터 글쓰기
        final String title = ((EditText) findViewById(R.id.et_title)).getText().toString();
        final String contents = ((EditText) findViewById(R.id.et_inquiry)).getText().toString();

        if (title.length() >0 && contents.length() >0){
            user =FirebaseAuth.getInstance().getCurrentUser();
            ServiceAccount serviceAccount = new ServiceAccount(title, contents, user.getUid());
            uploader(ServiceAccount);
        } else{
            startToast("회원정보를 입력해주세요.");
        }
    }

    private void uploader(ServiceAccount ServiceAccount){ // 업로드
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("posts").add(ServiceAccount)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    // 보여주기
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();}
}