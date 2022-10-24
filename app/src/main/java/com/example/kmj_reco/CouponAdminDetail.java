package com.example.kmj_reco;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.DTO.GIFTICONDATA;
import com.example.kmj_reco.utils.GifticonAdapter;
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
import java.util.ArrayList;
import java.util.List;

public class CouponAdminDetail extends AppCompatActivity {
    ListView gifticonDetailListView;

    FirebaseStorage storage;
    StorageReference sTreference;
    FirebaseDatabase database;
    DatabaseReference reference;
    ImageView gifticon_Image;
    String url;
    Button btn_cp;
    private  final int GALLERY_CODE = 10;
    List<GIFTICONADST> gifticonADSTList = new ArrayList<>();
    List<GIFTICONDATA> gifticondataList = new ArrayList<GIFTICONDATA>();
    // num 기반으로 데이터 불러옴

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_admin_detail);

        Context context = getApplicationContext();
        Intent intent = getIntent();
        int num = intent.getIntExtra("num",0);
        int indexNum = intent.getIntExtra("indexNum",0);

        storage = FirebaseStorage.getInstance();
        sTreference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        FirebaseAuth.getInstance().signInAnonymously();
        gifticonDetailListView = (ListView) findViewById(R.id.gifticon_admin_detail_listview);

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

                    Log.d("태그", data.getValue()+"이다1");
                    if(gif.getGifticonNum()==num){
                        gifticonADSTList.add(gif);
                    }
                }
                final GifticonAdminDetailAdapter gifticonAdminDetailAdapter = new GifticonAdminDetailAdapter(getApplicationContext(),0, gifticonADSTList,gifticonDetailListView);
                gifticonDetailListView.setAdapter(gifticonAdminDetailAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });

        Button btn_d = (Button) findViewById(R.id.deleteButton);

        reference.child("GIFTICONDATA").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticondataList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONDATA gif = data.getValue(GIFTICONDATA.class);
                    if(gif.getgifticon_Num()==num) {
                        gifticondataList.add(gif);
                    }

                    btn_d.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CouponAdminDetail.this);
                            builder.setTitle("알림").setMessage("삭제하시겠습니까?");
                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    reference = FirebaseDatabase.getInstance().getReference();
                                    reference.child("GIFTICONDATA").child(String.valueOf(gifticondataList.get(0).getgifticon_Num())).removeValue();
                                    // 삭제
                                    Toast.makeText(context, "삭제완료", Toast.LENGTH_SHORT);
                                    Intent intent = new Intent(getApplicationContext(), CouponAdmin.class);
                                    intent.putExtra("num", num);
                                    startActivity(intent);
                                }
                            });
                            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // 종료
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                }
                setValues();
                btn_cp = findViewById(R.id.gifticonADaddBtn);
                btn_cp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                수정
                        modiValues(url);
                        Intent intent = new Intent(getApplicationContext(), CouponAdmin.class);
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

        ImageView gifticon_Image = findViewById(R.id.gifticonImage);
        storage = FirebaseStorage.getInstance();
        sTreference = storage.getReference();

        gifticon_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAlbum();
            }
        });

        Button btn_adad = (Button) findViewById(R.id.newCouponadButton);
        btn_adad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                추가
                Intent intent = new Intent(getApplicationContext(), CouponAdminDetail2Plus.class);
//                data 데이터
                intent.putExtra("num",num);
                startActivity(intent);
            }
        });
        setUpOnClickListener();
    }
    private void setValues(){
        TextView gifticon_Num = findViewById(R.id.gifticon_Num);
        gifticon_Image = findViewById(R.id.gifticonImage);
        EditText gifticon_Brand = findViewById(R.id.gifticon_Brand_edit);
        EditText gifticon_Name = findViewById(R.id.gifticon_Name_edit);
        EditText gifticon_Price = findViewById(R.id.gifticon_Price_edit);
        EditText gifticon_Detail = findViewById(R.id.gifticon_Detail_edit);
        EditText gifticon_Type_edit = findViewById(R.id.gifticon_Type_edit);

        Intent intent = getIntent();
        int num = intent.getIntExtra("num",0);

        GIFTICONDATA selectedgifticondata = gifticondataList.get(0);

        gifticon_Num.setText("번호 :" + Integer.toString(num));
        GifticonAdapter.imageInsert(gifticon_Image, selectedgifticondata.getgifticon_Image());
        gifticon_Detail.setText(selectedgifticondata.getgifticon_Detail());
        gifticon_Brand.setText(selectedgifticondata.getgifticon_Brand());
        gifticon_Price.setText(Integer.toString(selectedgifticondata.getgifticon_Price()));
        gifticon_Name.setText(selectedgifticondata.getgifticon_Name());
        gifticon_Type_edit.setText(selectedgifticondata.getgifticon_Type());
    }
    private void modiValues(String url){

        ImageView gifticon_Image = findViewById(R.id.gifticonImage);
        EditText gifticon_Brand = findViewById(R.id.gifticon_Brand_edit);
        EditText gifticon_Name = findViewById(R.id.gifticon_Name_edit);
        EditText gifticon_Price = findViewById(R.id.gifticon_Price_edit);
        EditText gifticon_Detail = findViewById(R.id.gifticon_Detail_edit);
        EditText gifticon_Type_edit = findViewById(R.id.gifticon_Type_edit);

        Intent intent = getIntent();
        int num = intent.getIntExtra("num",0);
        GIFTICONDATA selectedgifticondata = gifticondataList.get(0);

        selectedgifticondata.setgifticon_Name(gifticon_Name.getText().toString());

        if (url!=null||url!=""){
            selectedgifticondata.setgifticon_Image(url);
        }else {selectedgifticondata.setgifticon_Image("GIFTICON/gifticon_dummy.png");}
        selectedgifticondata.setgifticon_Brand(gifticon_Brand.getText().toString());
        selectedgifticondata.setgifticon_Price(Integer.parseInt(gifticon_Price.getText().toString()));
        selectedgifticondata.setgifticon_Detail(gifticon_Detail.getText().toString());
        selectedgifticondata.setgifticon_Type(gifticon_Type_edit.getText().toString());

        reference.child("GIFTICONDATA").child(String.valueOf(selectedgifticondata.getgifticon_Num())).setValue(selectedgifticondata);

    }
    private void setUpOnClickListener(){
        gifticonDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                GIFTICONADST selectgifticondata = (GIFTICONADST) gifticonDetailListView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), CouponAdminDetail2.class);
                showDetail.putExtra("gifticonadst_num", selectgifticondata.getad_Num());
                showDetail.putExtra("num", selectgifticondata.getGifticonNum());

                startActivity(showDetail);
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
        url = "GIFTICON/"+Integer.toString(num)+".png";
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
                    gifticon_Image.setImageBitmap(img);
                } catch (Exception e){
                    e.printStackTrace();
                }

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CouponAdminDetail.this, "사진이 정상적으로 등록되지 않았습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(CouponAdminDetail.this, "사진이 정상적으로 등록되었습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }}
    }
}