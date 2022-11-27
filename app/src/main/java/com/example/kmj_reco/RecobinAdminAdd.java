package com.example.kmj_reco;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.kmj_reco.DTO.GIFTICONDATA;
import com.example.kmj_reco.DTO.RECOBIN;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RecobinAdminAdd extends AppCompatActivity {
    private DatabaseReference reference; // 데이터베이스 선언

    String url;

    List<RECOBIN> recobinList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recobin_admin_add);

        // DB 설정
        FirebaseAuth.getInstance().signInAnonymously();
        reference = FirebaseDatabase.getInstance().getReference();

        // RecobinAdmin Activity에서 데이터 받아오기
        Intent intent = getIntent();
        int num = intent.getIntExtra("num",0);

        // 데이터 소환
        final int[] recobin_num = {0};
        reference.child("RECOBIN").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    RECOBIN recoBin = data.getValue(RECOBIN.class);
                    recobinList2.add(recoBin);}

                recobin_num[0] = RECOBIN.lastNum(recobinList2)+1;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 추가 버튼 터치 이벤트
        ImageButton btn_add = (ImageButton) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // xml 데이터 변수 선언
                EditText recobin_address = findViewById(R.id.recobin_address);
                EditText recobin_roadname = findViewById(R.id.recobin_roadname);
                EditText recobin_locate = findViewById(R.id.recobin_locate);
                EditText recobin_fulladdress = findViewById(R.id.recobin_fulladdress);
                EditText recobin_latitude = findViewById(R.id.recobin_latitude);
                EditText recobin_longitude = findViewById(R.id.recobin_longitude);

                // 리스트에 데이터가 들어왔을 경우에만 새 레코빈 데이터 추가
                try {
                    if (url == null || url =="") {
                        RECOBIN newRecobin = new RECOBIN(recobin_num[0], recobin_address.getText().toString(), recobin_roadname.getText().toString(), recobin_locate.getText().toString(), recobin_fulladdress.getText().toString(),
                                Double.valueOf(recobin_latitude.getText().toString()),  Double.valueOf(recobin_longitude.getText().toString()));
                        reference.child("RECOBIN").child(String.valueOf(recobin_num[0])).setValue(newRecobin);
                    } else {
                        // EditText에 작성한 정보를 리스트 및 DB에 추가
                        RECOBIN newRecobin = new RECOBIN(recobin_num[0], recobin_address.getText().toString(), recobin_roadname.getText().toString(), recobin_locate.getText().toString(), recobin_fulladdress.getText().toString(),
                                Double.valueOf(recobin_latitude.getText().toString()),  Double.valueOf(recobin_longitude.getText().toString()));
                        reference.child("RECOBIN").child(String.valueOf(recobin_num[0])).setValue(newRecobin);
                    }
                    // RecobinAdmin Activity에 추가된 데이터 전송
                    Intent intent2 = new Intent(getApplicationContext(), RecobinAdmin.class);
                    intent2.putExtra("num", num);
                    startActivity(intent2);
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "숫자로 적어주세요.", Toast.LENGTH_SHORT);
                }
            }
        });

        // 뒤로가기 버튼 터치 시 RecobinAdmin 화면으로 이동
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecobinAdmin.class);
                startActivity(intent);
            }
        });
    }

    // 액티비티 결과
    public void onActivityResult(int requestCode, final int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = getIntent();
        int num = intent.getIntExtra("num",0);
    }
}
