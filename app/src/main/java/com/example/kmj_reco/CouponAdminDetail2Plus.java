package com.example.kmj_reco;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.utils.GifticonAdminDetailAdapter;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CouponAdminDetail2Plus extends AppCompatActivity {
    // db
    private DatabaseReference reference;
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference sTreference;

    String url=null;

    // 갤러리 코드 정의
    private  final int GALLERY_CODE = 10;
    ImageView gifticonBarcode;

    List<GIFTICONADST> gifticonADSTList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_admin_detail2_plus);

        // 이전 Activity에서 전달된 데이터 가져옴
        Intent thisIntent = getIntent();
        int num = thisIntent.getIntExtra("num",0);
        url = thisIntent.getStringExtra("url");

        // db
        FirebaseAuth.getInstance().signInAnonymously();
        storage = FirebaseStorage.getInstance();
        sTreference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // 추가 버튼
        Button btn_p = (Button) findViewById(R.id.gifticonDataaddBtn);

        // 사용자 권한 허용 확인 : 카메라 & 갤러리
        boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasCamPerm || !hasWritePerm)  // 권한 없을 시 권한설정 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        // 기프티콘 재고 데이터 가져오기
        reference.child("GIFTICONADST").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 리스트 초기화
                gifticonADSTList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);

                    gifticonADSTList.add(gif);
                }
                // 추가 버튼 클릭시
                btn_p.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 마지막 일련번호 확인
                        int gifticon_adst_num = GifticonAdminDetailAdapter.lastNum(gifticonADSTList)+1;

                        // xml 요소
                        EditText buydate = findViewById(R.id.buydate);
                        EditText gifticonExpirydateEdit = findViewById(R.id.gifticonExpirydateEdit);

                        // 날짜 형식 정의
                        SimpleDateFormat sdf_dt = new SimpleDateFormat("yyyy-MM-dd");

                        // 추가할 기프티콘 재고 객체 생성
                        GIFTICONADST newGifticondata = null;
                        // 더미 url
                        String finalUrl = "GIFTICONBARCODE/barcode_dummy.png";

                        // url이 존재하지 않을 경우
                        if (url==null|| url==""){
                            // 더미 url로 설정한다
                            url = finalUrl;
                        }
                        // 추가할 기프티콘 재고 객체 데이터 설정
                        try {
                            newGifticondata = new GIFTICONADST(gifticon_adst_num,false,num,sdf_dt.parse(gifticonExpirydateEdit.getText().toString()),url,sdf_dt.parse(buydate.getText().toString()),sdf_dt.parse("2020-01-01"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        // 기프티콘 재고 추가
                        reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("GIFTICONADST").child(String.valueOf(gifticon_adst_num)).setValue(newGifticondata);

                        // 기프티콘 식별번호 전달
                        // 기프티콘 어드민 상세화면으로 이동
                        Intent intent = new Intent(getApplicationContext(), CouponAdminDetail.class);
                        intent.putExtra("num", num);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });
        gifticonBarcode = findViewById(R.id.gifticonImage);

        gifticonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAlbum();
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
        // 기프티콘 식별번호, 기프티콘 재고 식별번호 가져오기
        int num = intent.getIntExtra("num",0);
        int gifticonadst_num = GifticonAdminDetailAdapter.lastNum(gifticonADSTList)+1;
        // db에 저장될 위치 설정
        url = "GIFTICONBARCODE/"+Integer.toString(gifticonadst_num)+".png";
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
                    gifticonBarcode.setImageBitmap(img);
                } catch (Exception e){
                    e.printStackTrace();
                }

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CouponAdminDetail2Plus.this, "사진이 정상적으로 등록되지 않았습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Intent intent = getIntent();
                        // url 전달
                        intent.putExtra("url",url);
                        Toast.makeText(CouponAdminDetail2Plus.this, "사진이 정상적으로 등록되었습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }}
    }
}
