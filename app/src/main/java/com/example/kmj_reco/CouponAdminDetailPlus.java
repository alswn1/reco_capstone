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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.kmj_reco.DTO.GIFTICONDATA;
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

public class CouponAdminDetailPlus extends AppCompatActivity {
    private DatabaseReference reference;
    // db
    FirebaseStorage storage;
    StorageReference sTreference;

    String url;

    // 갤러리 코드 정의
    private  final int GALLERY_CODE = 10;

    ImageView gifticonImage;
    List<GIFTICONDATA> gifticondataList2 = new ArrayList<>();

    public static final int GALLERY_IMAGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_admin_detail_plus);


        // ddb
        FirebaseAuth.getInstance().signInAnonymously();
        reference= FirebaseDatabase.getInstance().getReference();

        // 이전 Activity에서 전달된 데이터 가져옴
        Intent intent = getIntent();
        int num = intent.getIntExtra("num",0);

        // 사용자 권한 허용 확인 : 카메라 & 갤러리
        boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasCamPerm || !hasWritePerm)  // 권한 없을 시 권한설정 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        // 기프티콘 데이터 가져오기
        storage = FirebaseStorage.getInstance();
        sTreference= storage.getReference();
        final int[] gifticon_num = {0};
        reference.child("GIFTICONDATA").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONDATA gif = data.getValue(GIFTICONDATA.class);
                    gifticondataList2.add(gif);}

                // 마지막 식별번호 확인 및 추가할 식별번호 가져오기
                gifticon_num[0] = GIFTICONDATA.lastNum(gifticondataList2)+1;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 이미지 버튼 클릭 이벤트
        gifticonImage = findViewById(R.id.gifticonImage);
        gifticonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 갤러리 열기
                loadAlbum();
            }
        });


        // 추가 버튼 클릭 이벤트
        Button btn_p = (Button) findViewById(R.id.gifticonADaddBtn);
        btn_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // xml 요소
                EditText gifticon_Name_edit = findViewById(R.id.gifticon_Name_edit);
                EditText gifticon_Type_edit = findViewById(R.id.gifticon_Type_edit);
                EditText gifticon_Brand_edit = findViewById(R.id.gifticon_Brand_edit);
                EditText gifticon_Price_edit = findViewById(R.id.gifticon_Price_edit);
                EditText gifticon_Detail_edit = findViewById(R.id.gifticon_Detail_edit);

                // 비어있는 항목이 있을 경우 추가되지 않음
                if(gifticon_Name_edit.getText().toString()==""||gifticon_Type_edit.getText().toString()==""||gifticon_Brand_edit.getText().toString()==""
                        ||gifticon_Detail_edit.getText().toString()==""||gifticon_Price_edit.getText().toString()==""){
                    Toast.makeText(getApplicationContext(), "비어있는 항목이 있습니다.", Toast.LENGTH_SHORT);

                } else {
                    try {
                        if (url == null || url == "") {
                            // url 데이터가 존재하지 않을 경우
                            // 더미 이미지 데이터 설정
                            GIFTICONDATA newGifticondata = new GIFTICONDATA(Integer.parseInt(gifticon_Price_edit.getText().toString()), gifticon_Detail_edit.getText().toString(), gifticon_num[0], "GIFTICON/gifticon_dummy.png", gifticon_Name_edit.getText().toString(), gifticon_Type_edit.getText().toString(),
                                    gifticon_Brand_edit.getText().toString());
                            reference.child("GIFTICONDATA").child(String.valueOf(gifticon_num[0])).setValue(newGifticondata);
                        } else {
                            // url 데이터가 존재할 경우
                            GIFTICONDATA newGifticondata = new GIFTICONDATA(Integer.parseInt(gifticon_Price_edit.getText().toString()), gifticon_Detail_edit.getText().toString(), gifticon_num[0], url, gifticon_Name_edit.getText().toString(), gifticon_Type_edit.getText().toString(),
                                    gifticon_Brand_edit.getText().toString());
                            reference.child("GIFTICONDATA").child(String.valueOf(gifticon_num[0])).setValue(newGifticondata);
                        }
                        // 기프티콘 식별번호 데이터 전달
                        // 기프티콘 어드민 화면으로 이동
                        Intent intent2 = new Intent(getApplicationContext(), CouponAdmin.class);
                        intent2.putExtra("num", num);
                        startActivity(intent2);
                    } catch (NumberFormatException e) {
                        // price에 문자를 입력했을 경우
                        Toast.makeText(getApplicationContext(), "가격에 문자를 적으면 안됩니다.", Toast.LENGTH_SHORT);
                    }
                }
            }
        });
    }

    // 갤러리 열기 함수
    private void loadAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        // 휴대전화 갤러리 열기
        startActivityForResult(intent, GALLERY_CODE);
    }

    // 갤러리를 열었다가 종료하면 실행되는 함수
    public void onActivityResult(int requestCode, final int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = getIntent();
        // 기프티콘 식별번호 가져오기
        int num = intent.getIntExtra("num",0);
        // db에 저장될 위치 설정
        url = "GIFTICONDATA/"+Integer.toString(num)+".png";
        // ImageView에 이미지 설정
        onImage(requestCode,data,url,num);
    }

    // 갤러리를 열고 선택한 이미지를 db에 저장하는 함수

    private void onImage (int requestCode, Intent data, String url, int num){

        // 갤러리를 여는 코드가 맞다면
        if (requestCode == GALLERY_CODE){
            Uri file = null;
            try{
                // 현재 intent의 데이터 가져옴
                // 갤러리를 열어 이미지를 선택했을 경우 해당 이미지가 저장되어 있음
                file = data.getData();}catch(NullPointerException e){};
            if (file==null){
                // 현재 intent의 데이터가 없을 경우 실행하지 않음
                // 갤러리를 열었어도 이미지를 선택하지 않을 경우 실행하지 않음
            }else{
                // db
                StorageReference storageRef = storage.getReference();
                StorageReference riversRef = storageRef.child(url);
                // 설정한 url에 선택한 이미지 파일을 업로드
                UploadTask uploadTask = riversRef.putFile(file);
                riversRef.getDownloadUrl();
                try{
                    // 업로드한 이미지 다운로드
                    InputStream in =
                            getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // ImageView에 해당 이미지 설정
                    gifticonImage.setImageBitmap(img);
                } catch (Exception e){
                    e.printStackTrace();
                }

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CouponAdminDetailPlus.this, "사진이 정상적으로 등록되지 않았습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Intent intent = getIntent();
                        // url 전달
                        intent.putExtra("url",url);
                        Toast.makeText(CouponAdminDetailPlus.this, "사진이 정상적으로 등록되었습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }}
    }
}