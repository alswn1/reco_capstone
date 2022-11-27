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
    // db
    FirebaseStorage storage;
    StorageReference sTreference;
    FirebaseDatabase database;
    DatabaseReference reference;


    ImageView gifticon_Image;
    String url;
    Button btn_cp;

    // 갤러리 코드 정의
    private  final int GALLERY_CODE = 10;

    List<GIFTICONADST> gifticonADSTList = new ArrayList<>();
    List<GIFTICONDATA> gifticondataList = new ArrayList<GIFTICONDATA>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_admin_detail);

        Context context = getApplicationContext();
        // 이전 Activity에서 전달된 데이터 가져옴
        Intent intent = getIntent();
        int num = intent.getIntExtra("num",0);
        int indexNum = intent.getIntExtra("indexNum",0);

        // db
        storage = FirebaseStorage.getInstance();
        sTreference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        gifticonDetailListView = (ListView) findViewById(R.id.gifticon_admin_detail_listview);


        // 사용자 권한 허용 확인 : 카메라 & 갤러리
        boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasCamPerm || !hasWritePerm)  // 권한 없을 시 권한설정 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


        // 기프티콘 재고 데이터 가져오기
        reference.child("GIFTICONADST").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticonADSTList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);

                    // 해당 기프티콘의 재고만 가져오기
                    if(gif.getGifticonNum()==num){
                        gifticonADSTList.add(gif);
                    }
                }
                // 목록에 표시
                final GifticonAdminDetailAdapter gifticonAdminDetailAdapter = new GifticonAdminDetailAdapter(getApplicationContext(),0, gifticonADSTList,gifticonDetailListView);
                gifticonDetailListView.setAdapter(gifticonAdminDetailAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });

        // 삭제 버튼 클릭 이벤트
        Button btn_d = (Button) findViewById(R.id.deleteButton);
        // 버튼 클릭시 해당 기프티콘 삭제
        reference.child("GIFTICONDATA").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 리스트 초기화
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

                            // 예 클릭 시
                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    reference = FirebaseDatabase.getInstance().getReference();
                                    reference.child("GIFTICONDATA").child(String.valueOf(gifticondataList.get(0).getgifticon_Num())).removeValue();
                                    // 해당 기프티콘 삭제
                                    Toast.makeText(context, "삭제완료", Toast.LENGTH_SHORT).show();
                                    // 해당 기프티콘 재고 삭제
                                    deleteAdst(gifticondataList.get(0).getgifticon_Num());
                                    // 기프티콘 어드민 화면으로 이동
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
                // xml 요소 데이터 설정
                setValues();
                // 수정 버튼 클릭 이벤트
                btn_cp = findViewById(R.id.gifticonADaddBtn);
                btn_cp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 적힌 내용 기반 수정
                        // 이미지 url이 변경되었을 경우 적용
                        modiValues(url);
                        // 기프티콘 어드민 화면으로 이동
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

        // 기프티콘 이미지
        ImageView gifticon_Image = findViewById(R.id.gifticonImage);
        // db
        storage = FirebaseStorage.getInstance();
        sTreference = storage.getReference();

        // 이미지 클릭 이벤트
        gifticon_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 갤러리 열기
                loadAlbum();
            }
        });

        // 기프티콘 재고 추가 버튼 클릭 이벤트
        Button btn_adad = (Button) findViewById(R.id.newCouponadButton);
        btn_adad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 기프티콘 재고 추가 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), CouponAdminDetail2Plus.class);
                // 기프티콘 식별번호 전달
                intent.putExtra("num",num);
                startActivity(intent);
            }
        });
        setUpOnClickListener();
    }

    // xml 데이터 설정 함수
    private void setValues(){

        // xml 요소
        TextView gifticon_Num = findViewById(R.id.gifticon_Num);
        gifticon_Image = findViewById(R.id.gifticonImage);
        EditText gifticon_Brand = findViewById(R.id.gifticon_Brand_edit);
        EditText gifticon_Name = findViewById(R.id.gifticon_Name_edit);
        EditText gifticon_Price = findViewById(R.id.gifticon_Price_edit);
        EditText gifticon_Detail = findViewById(R.id.gifticon_Detail_edit);
        EditText gifticon_Type_edit = findViewById(R.id.gifticon_Type_edit);

        // 기프티콘 식별번호 전달받기
        Intent intent = getIntent();
        int num = intent.getIntExtra("num",0);

        // 해당 기프티콘 데이터
        GIFTICONDATA selectedgifticondata = gifticondataList.get(0);

        // xml 요소 데이터 설정
        gifticon_Num.setText("번호 :" + Integer.toString(num));
        GifticonAdapter.imageInsert(gifticon_Image, selectedgifticondata.getgifticon_Image());
        gifticon_Detail.setText(selectedgifticondata.getgifticon_Detail());
        gifticon_Brand.setText(selectedgifticondata.getgifticon_Brand());
        gifticon_Price.setText(Integer.toString(selectedgifticondata.getgifticon_Price()));
        gifticon_Name.setText(selectedgifticondata.getgifticon_Name());
        gifticon_Type_edit.setText(selectedgifticondata.getgifticon_Type());
    }

    // 데이터 수정 함수
    private void modiValues(String url){

        // xml 요소
        ImageView gifticon_Image = findViewById(R.id.gifticonImage);
        EditText gifticon_Brand = findViewById(R.id.gifticon_Brand_edit);
        EditText gifticon_Name = findViewById(R.id.gifticon_Name_edit);
        EditText gifticon_Price = findViewById(R.id.gifticon_Price_edit);
        EditText gifticon_Detail = findViewById(R.id.gifticon_Detail_edit);
        EditText gifticon_Type_edit = findViewById(R.id.gifticon_Type_edit);

        // 기프티콘 식별번호 전달받기
        Intent intent = getIntent();
        int num = intent.getIntExtra("num",0);

        // 해당 기프티콘 데이터
        GIFTICONDATA selectedgifticondata = gifticondataList.get(0);

        // 변경된 데이터를 해당 기프티콘 데이터에 적용
        selectedgifticondata.setgifticon_Name(gifticon_Name.getText().toString());
        String nowImage = selectedgifticondata.getgifticon_Image();
        selectedgifticondata.setgifticon_Image(nowImage);
        // 새로운 이미지 url이 존재하는 경우 이미지 url 데이터 변경
        if (url != null&& url!=""){
            selectedgifticondata.setgifticon_Image(url);
        }
        selectedgifticondata.setgifticon_Brand(gifticon_Brand.getText().toString());
        selectedgifticondata.setgifticon_Price(Integer.parseInt(gifticon_Price.getText().toString()));
        selectedgifticondata.setgifticon_Detail(gifticon_Detail.getText().toString());
        selectedgifticondata.setgifticon_Type(gifticon_Type_edit.getText().toString());

        // 변경된 데이터 db 적용
        reference.child("GIFTICONDATA").child(String.valueOf(selectedgifticondata.getgifticon_Num())).setValue(selectedgifticondata);

    }

    // 목록 item 클릭 이벤트
    private void setUpOnClickListener(){
        gifticonDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 해당 item 데이터
                GIFTICONADST selectgifticondata = (GIFTICONADST) gifticonDetailListView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), CouponAdminDetail2.class);

                // 해당 item의 기프티콘 식별번호, 기프티콘 재고 식별번호를 다음 activity로 넘김
                // 기프티콘 재고 어드민 상세화면으로 이동
                showDetail.putExtra("gifticonadst_num", selectgifticondata.getad_Num());
                showDetail.putExtra("num", selectgifticondata.getGifticonNum());
                startActivity(showDetail);
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
        url = "GIFTICON/"+Integer.toString(num)+".png";
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
                        Intent intent = getIntent();
                        // url 전달
                        intent.putExtra("url",url);
                        Toast.makeText(CouponAdminDetail.this, "사진이 정상적으로 등록되었습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }}
    }

    // 개별 기프티콘 재고 삭제 함수 : 기프티콘 식별번호를 받아 해당 기프티콘의 재고를 전부 삭제
    private void deleteAdst(int gifticonnum){
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("GIFTICONADST").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);
                    // 기프티콘 식별번호가 같을 경우
                    if (gif.getGifticonNum()==gifticonnum){
                        // 삭제
                        reference.child("GIFTICONADST").child(String.valueOf(gif.getad_Num())).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}