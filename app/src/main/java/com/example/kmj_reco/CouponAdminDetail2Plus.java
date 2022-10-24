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
    private DatabaseReference reference;
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference sTreference;
    StorageReference ref;
    String url=null;
    private  final int GALLERY_CODE = 10;
    ImageView gifticonBarcode;

    List<GIFTICONADST> gifticonADSTList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_admin_detail2_plus);
        Intent thisIntent = getIntent();
        int num = thisIntent.getIntExtra("num",0);
        url = thisIntent.getStringExtra("url");

        FirebaseAuth.getInstance().signInAnonymously();
        storage = FirebaseStorage.getInstance();
        sTreference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        Button btn_p = (Button) findViewById(R.id.gifticonDataaddBtn);
        boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasCamPerm || !hasWritePerm)  // 권한 없을 시 권한설정 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        reference.child("GIFTICONADST").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticonADSTList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);

                    gifticonADSTList.add(gif);
                }
                btn_p.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int gifticon_adst_num = GifticonAdminDetailAdapter.lastNum(gifticonADSTList)+1;
                        EditText buydate = findViewById(R.id.buydate);
                        EditText gifticonExpirydateEdit = findViewById(R.id.gifticonExpirydateEdit);

                        // 이미지 데이터 형태와 내용 수정
                        // 각 기프티콘 리스트에 추가
                        SimpleDateFormat sdf_dt = new SimpleDateFormat("yyyy-mm-dd");
                        GIFTICONADST newGifticondata = null;
                        String finalUrl = "GIFTICONBARCODE/barcode_dummy.png";
                        if (url==null|| url==""){
                            url = finalUrl;
                        }
                        try {
                            newGifticondata = new GIFTICONADST(gifticon_adst_num,false,num,sdf_dt.parse(gifticonExpirydateEdit.getText().toString()),url,sdf_dt.parse(buydate.getText().toString()),sdf_dt.parse("2020-01-01"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        FirebaseAuth.getInstance().signInAnonymously();
                        reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("GIFTICONADST").child(String.valueOf(gifticon_adst_num)).setValue(newGifticondata);

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
    private void loadAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    }

    public void onActivityResult(int requestCode, final int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = getIntent();
        int num = intent.getIntExtra("num",0);
        int gifticonadst_num = intent.getIntExtra("gifticonadst_num",0);
        url = "GIFTICONBARCODE/"+Integer.toString(gifticonadst_num)+".png";
        onImage(requestCode,data,url,num);
    }
    private void onImage (int requestCode, Intent data, String url, int num){
        if (requestCode == GALLERY_CODE){
            Uri file = null;
            try{
                file = data.getData();}catch(NullPointerException e){};
            if (file==null){ }else{
                StorageReference storageRef = storage.getReference();
                StorageReference riversRef = storageRef.child(url);
                UploadTask uploadTask = riversRef.putFile(file);
                riversRef.getDownloadUrl();

                try{
                    InputStream in =
                            getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
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
                        intent.putExtra("url",url);
                        Toast.makeText(CouponAdminDetail2Plus.this, "사진이 정상적으로 등록되었습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }}
    }
}
