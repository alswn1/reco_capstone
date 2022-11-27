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
import com.example.kmj_reco.utils.GifticonAdapter;
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
import java.util.Locale;

public class CouponAdminDetail2Edit extends AppCompatActivity {
    // db
    private FirebaseStorage storage;
    private DatabaseReference reference;

    String url = "";

    // 갤러리 코드 설정
    private  final int GALLERY_CODE = 10;

    ImageView gifticonImage;
    List<GIFTICONADST> gifticonADSTList = new ArrayList<>();
    public static final int GALLERY_IMAGE_REQUEST_CODE = 1;

    // 날짜 형식 정의
    SimpleDateFormat sdf_dt = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

    EditText buydate;
    EditText gifticonExpirydateEdit;

    GIFTICONADST oldGifticonADSTdata;
    GIFTICONADST newGifticonADSTdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_admin_detail2_edit);

        // 이전 Activity에서 전달된 데이터 가져오기
        Intent thisIntent = getIntent();
        int num = thisIntent.getIntExtra("num",0);
        int gifticonadst_num = thisIntent.getIntExtra("gifticonadst_num",0);

        // 사용자 권한 허용 확인 : 카메라 & 갤러리
        boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasCamPerm || !hasWritePerm)  // 권한 없을 시 권한설정 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


        // 수정 버튼
        Button btn_e = (Button) findViewById(R.id.gifticonDataEditBtn);

        // db
        storage = FirebaseStorage.getInstance();
        FirebaseAuth.getInstance().signInAnonymously();

        // 기프티콘 재고 데이터 가져오기
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("GIFTICONADST").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticonADSTList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);

                    // 해당 기프티콘 재고 데이터
                    if (gifticonadst_num== gif.getad_Num()){
                        gifticonADSTList.add(gif);}
                }
                // xml 요소 설정
                setData();

                // 수정 버튼 클릭 이벤트
                btn_e.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 수정하는 기프티콘 재고 데이터 설정
                        // 변경되었을 경우 변경된 내용 적용
                        newGifticonADSTdata.setGifticonBarcode(url);
                        try {
                            newGifticonADSTdata.setBuydate(sdf_dt.parse(buydate.getText().toString()));
                            newGifticonADSTdata.setGifticonExpirydate(sdf_dt.parse(gifticonExpirydateEdit.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        // 기프티콘 재고 데이터 수정
                        reference.child("GIFTICONADST").child(String.valueOf(oldGifticonADSTdata.getad_Num())).setValue(newGifticonADSTdata);

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

        // 기프티콘 바코드 이미지 클릭 이벤트
        gifticonImage = (ImageView) findViewById(R.id.gifticonImage2);
        gifticonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 갤러리 열기
                loadAlbum();
            }
        });
    }
    // xml 데이터 설정 함수
    void setData(){
        // 기존 기프티콘 재고 데이터 가져오기
        oldGifticonADSTdata = gifticonADSTList.get(0);
        newGifticonADSTdata = oldGifticonADSTdata;

        // xml 요소
        gifticonImage = (ImageView) findViewById(R.id.gifticonImage2);
        buydate = findViewById(R.id.buydate);
        gifticonExpirydateEdit = findViewById(R.id.gifticonExpirydateEdit);

        // xml 요소 데이터 설정
        GifticonAdapter.imageInsert(gifticonImage, oldGifticonADSTdata.getGifticonBarcode());
        try {
            buydate.setText(sdf_dt.format(dateFormat.parse(oldGifticonADSTdata.getBuydate().toString())));
            gifticonExpirydateEdit.setText(sdf_dt.format(dateFormat.parse(oldGifticonADSTdata.getGifticonExpirydate().toString())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        url = gifticonADSTList.get(0).getGifticonBarcode();
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
        int gifticonadst_num = intent.getIntExtra("gifticonadst_num",0);
        // db에 저장될 위치 설정
        url = "GIFTICONBARCODE/"+Integer.toString(gifticonadst_num)+".png";
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
                // ImageView
                gifticonImage = (ImageView) findViewById(R.id.gifticonImage2);
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
                        Toast.makeText(CouponAdminDetail2Edit.this, "사진이 정상적으로 등록되지 않았습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Intent intent = getIntent();
                        // url 전달
                        intent.putExtra("url",url);
                        Toast.makeText(CouponAdminDetail2Edit.this, "사진이 정상적으로 등록되었습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }}
    }
}