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
    private FirebaseStorage storage;
    private DatabaseReference reference;
    StorageReference sTreference;
    String url = "";
    String fileurl= "";
    private  final int GALLERY_CODE = 10;
    ImageView gifticonImage;
    List<GIFTICONADST> gifticonADSTList = new ArrayList<>();
    public static final int GALLERY_IMAGE_REQUEST_CODE = 1;
    SimpleDateFormat sdf_dt = new SimpleDateFormat("yyyy-mm-dd");
    SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
    EditText buydate;
    EditText gifticonExpirydateEdit;

    GIFTICONADST oldGifticonADSTdata;
    GIFTICONADST newGifticonADSTdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_admin_detail2_edit);

        Intent thisIntent = getIntent();
        int num = thisIntent.getIntExtra("num",0);
        int gifticonadst_num = thisIntent.getIntExtra("gifticonadst_num",0);

        boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasCamPerm || !hasWritePerm)  // 권한 없을 시 권한설정 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        Button btn_e = (Button) findViewById(R.id.gifticonDataEditBtn);

        storage = FirebaseStorage.getInstance();
        FirebaseAuth.getInstance().signInAnonymously();
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("GIFTICONADST").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticonADSTList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);
                    if (gifticonadst_num== gif.getad_Num()){
                        gifticonADSTList.add(gif);}
                }
                setData();
                btn_e.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                각 기프티콘 리스트에 추가

                        newGifticonADSTdata.setGifticonBarcode(url);
                        try {
                            newGifticonADSTdata.setBuydate(sdf_dt.parse(buydate.getText().toString()));
                            newGifticonADSTdata.setGifticonExpirydate(sdf_dt.parse(gifticonExpirydateEdit.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        reference.child("GIFTICONADST").child(String.valueOf(oldGifticonADSTdata.getad_Num())).setValue(newGifticonADSTdata);

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

        gifticonImage = (ImageView) findViewById(R.id.gifticonImage2);
        gifticonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAlbum();
            }
        });
    }

    void setData(){
        oldGifticonADSTdata = gifticonADSTList.get(0);
        newGifticonADSTdata = oldGifticonADSTdata;

        gifticonImage = (ImageView) findViewById(R.id.gifticonImage2);
        buydate = findViewById(R.id.buydate);
        gifticonExpirydateEdit = findViewById(R.id.gifticonExpirydateEdit);
        GifticonAdapter.imageInsert(gifticonImage, oldGifticonADSTdata.getGifticonBarcode());
        try {
            buydate.setText(sdf_dt.format(dateFormat.parse(oldGifticonADSTdata.getBuydate().toString())));
            gifticonExpirydateEdit.setText(sdf_dt.format(dateFormat.parse(oldGifticonADSTdata.getGifticonExpirydate().toString())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        url = gifticonADSTList.get(0).getGifticonBarcode();
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
        fileurl = "GIFTICONBARCODE/"+Integer.toString(gifticonadst_num)+".png";
        onImage(requestCode,data,url,num);
    }
    private void onImage (int requestCode, Intent data, String url, int num){
        if (requestCode == GALLERY_CODE){
            Uri file = null;
            try{
                file = data.getData();}catch(NullPointerException e){};
            if (file==null){ }else{
                gifticonImage = (ImageView) findViewById(R.id.gifticonImage2);
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
                        Toast.makeText(CouponAdminDetail2Edit.this, "사진이 정상적으로 등록되지 않았습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Intent intent = getIntent();
                        intent.putExtra("url",url);
                        Toast.makeText(CouponAdminDetail2Edit.this, "사진이 정상적으로 등록되었습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }}
    }
}