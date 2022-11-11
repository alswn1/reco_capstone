package com.example.kmj_reco;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.kmj_reco.tflite.ClassifierWithModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CameraActivity extends AppCompatActivity {
    public static final String TAG = "[IC]CameraActivity";
    public static final int CAMERA_IMAGE_REQUEST_CODE = 1;
    private static final String KEY_SELECTED_URI = "KEY_SELECTED_URI";

    private ClassifierWithModel cls;

    Uri selectedImageUri;

    // 데이터베이스
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // RECO 글씨
        ImageView btn_home = (ImageView) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        // 톱니바퀴 버튼
        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        // 종 버튼
        ImageView btn_alert = (ImageView) findViewById(R.id.btn_alert);
        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });

        // 카메라 촬영 버튼
        Button takeBtn = findViewById(R.id.takeBtn);
        takeBtn.setOnClickListener(v -> getImageFromCamera());

        cls = new ClassifierWithModel(this);
        try {
            cls.init();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if(savedInstanceState != null) {
            Uri uri = savedInstanceState.getParcelable(KEY_SELECTED_URI);
            if (uri != null)
                selectedImageUri = uri;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_SELECTED_URI, selectedImageUri);
    }

    // 카메라에서 이미지 가져오기
    private void getImageFromCamera(){
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "picture.jpg");
        selectedImageUri = FileProvider.getUriForFile(this, getPackageName(), file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
        startActivityForResult(intent, CAMERA_IMAGE_REQUEST_CODE);
    }

    // 사진 인증
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK &&
                requestCode == CAMERA_IMAGE_REQUEST_CODE) {

            Bitmap bitmap = null;
            try {
                if(Build.VERSION.SDK_INT >= 29) {
                    ImageDecoder.Source src = ImageDecoder.createSource(
                            getContentResolver(), selectedImageUri);
                    bitmap = ImageDecoder.decodeBitmap(src);
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), selectedImageUri);
                }
            } catch (IOException ioe) {
                Log.e(TAG, "Failed to read Image", ioe);
            }

            if(bitmap != null) {
                Pair<String, Float> output = cls.classify(bitmap);

                Character c = output.first.charAt(0);
                if (c == '7') {
                    //Toast.makeText(getApplicationContext(), "첫번째 : " + c, Toast.LENGTH_SHORT).show();

                    // 다시 사진을 찍을 수 있게 지도로 이동
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);

                    Toast.makeText(this, "해당 제품은 재활용품이 아니어서 인증이 불가합니다.", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "첫번째 : " + c, Toast.LENGTH_SHORT).show();

                    // db
                    database = FirebaseDatabase.getInstance();
                    mFirebaseAuth = FirebaseAuth.getInstance();

                    String userId = mFirebaseAuth.getCurrentUser().getUid(); // 유저 인덱스 번호
                    String ref = "USER/" + userId;
                    myRef = database.getReference(ref);

                    // 사진 인증 횟수 검사 및 증가
                    increaseUserPoint(myRef);
                }
            }

        }
    }

    // 사용자 포인트 증가
    // 사진 인증 : 1 포인트 증가
    private void increaseUserPoint(DatabaseReference myRef) {
        // 포인트 업데이트
        myRef.child("user_point").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 사용자의 포인트를 받아온다
                int point = snapshot.getValue(Integer.class);
                // addValueEventListener의 무한루프를 막는다.
                myRef.child("user_point").removeEventListener(this);
                // 사용자 포인트를 1 증가한다.
                myRef.child("user_point").setValue(point + 2);

                // 성공하여 CameraSuccess로 이동
                Intent intent = new Intent(getApplicationContext(), CameraSuccess.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 에러문 출력
                Log.e("CameraSuccessActivity", String.valueOf(error.toException()));
            }
        });
    }

    @Override
    protected void onDestroy() {
        cls.finish();
        super.onDestroy();
    }
}