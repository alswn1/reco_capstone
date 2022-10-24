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
    FirebaseStorage storage;
    StorageReference sTreference;
    String url;
    private  final int GALLERY_CODE = 10;
    ImageView gifticonImage;
    List<GIFTICONDATA> gifticondataList2 = new ArrayList<>();

    public static final int GALLERY_IMAGE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_admin_detail_plus);

        FirebaseAuth.getInstance().signInAnonymously();
        reference= FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        int num = intent.getIntExtra("num",0);

        boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasCamPerm || !hasWritePerm)  // 권한 없을 시 권한설정 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        // 데이터 소환
        storage = FirebaseStorage.getInstance();
        sTreference= storage.getReference();
        final int[] gifticon_num = {0};
        reference.child("GIFTICONDATA").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONDATA gif = data.getValue(GIFTICONDATA.class);
                    gifticondataList2.add(gif);}

                gifticon_num[0] = GIFTICONDATA.lastNum(gifticondataList2)+1;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        gifticonImage = findViewById(R.id.gifticonImage);
        gifticonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAlbum();
            }
        });

        Button btn_p = (Button) findViewById(R.id.gifticonADaddBtn);
        btn_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText gifticon_Name_edit = findViewById(R.id.gifticon_Name_edit);
                EditText gifticon_Type_edit = findViewById(R.id.gifticon_Type_edit);
                EditText gifticon_Brand_edit = findViewById(R.id.gifticon_Brand_edit);
                EditText gifticon_Price_edit = findViewById(R.id.gifticon_Price_edit);
                EditText gifticon_Detail_edit = findViewById(R.id.gifticon_Detail_edit);

                // 리스트에 데이터가 들어왔을 경우에만
                try {
                    if (url == null || url =="") {
                        GIFTICONDATA newGifticondata = new GIFTICONDATA(Integer.parseInt(gifticon_Price_edit.getText().toString()), gifticon_Detail_edit.getText().toString(), gifticon_num[0], "GIFTICON/gifticon_dummy.png", gifticon_Name_edit.getText().toString(), gifticon_Type_edit.getText().toString(),
                                gifticon_Brand_edit.getText().toString());
                        reference.child("GIFTICONDATA").child(String.valueOf(gifticon_num[0])).setValue(newGifticondata);
                    } else {
                        GIFTICONDATA newGifticondata = new GIFTICONDATA(Integer.parseInt(gifticon_Price_edit.getText().toString()), gifticon_Detail_edit.getText().toString(), gifticon_num[0], url, gifticon_Name_edit.getText().toString(), gifticon_Type_edit.getText().toString(),
                                gifticon_Brand_edit.getText().toString());
                        reference.child("GIFTICONDATA").child(String.valueOf(gifticon_num[0])).setValue(newGifticondata);
                    }
                    Intent intent2 = new Intent(getApplicationContext(), CouponAdmin.class);
                    intent2.putExtra("num", num);
                    startActivity(intent2);
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "가격에 문자를 적으면 안됩니다.", Toast.LENGTH_SHORT);
                }
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
        url = "GIFTICONDATA/"+Integer.toString(num)+".png";
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
                        intent.putExtra("url",url);
                        Toast.makeText(CouponAdminDetailPlus.this, "사진이 정상적으로 등록되었습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }}
    }
}