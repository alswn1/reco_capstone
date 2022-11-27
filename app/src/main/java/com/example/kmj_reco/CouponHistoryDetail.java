package com.example.kmj_reco;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.DTO.GIFTICONDATA;
import com.example.kmj_reco.DTO.USER_GIFTICON;
import com.example.kmj_reco.utils.GifticonAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CouponHistoryDetail extends AppCompatActivity {
    // db
    FirebaseDatabase database;
    DatabaseReference reference;


    static List<GIFTICONADST> gifticondataList_h = new ArrayList<>();
    static List<GIFTICONDATA> gifticondataList_d = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_history_detail);

        // 이전 Activity에서 전달된 데이터 가져오기
        Intent intent = getIntent();
        int adNum = intent.getIntExtra("gifticonHistoryNum",0);

        // db
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // 기프티콘 재고 데이터
        reference.child("GIFTICONADST").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 리스트 초기화
                gifticondataList_h.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONADST uig1 = data.getValue(GIFTICONADST.class);
                    // 기프티콘 재고 번호로 찾기
                    if (uig1.getad_Num() == adNum){
                        gifticondataList_h.add(uig1);}
                }

                reference.child("GIFTICONDATA").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // 리스트 초기화
                        gifticondataList_d.clear();

                        for(DataSnapshot data : snapshot.getChildren()){
                            GIFTICONDATA uig2 = data.getValue(GIFTICONDATA.class);
                            // 해당 기프티콘 재고의 기프티콘 데이터 가져오기
                            if (uig2.getgifticon_Num()==gifticondataList_h.get(0).getGifticonNum()){
                                gifticondataList_d.add(uig2);}
                        }
                        try {
                            // xml 요소 데이터 설정
                            setData();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("OncalledError","error");
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });
    }
    // xml 데이터 설정 함수
    public void setData() throws ParseException {

        // xml 요소
        ImageView gifticonBarcode = findViewById(R.id.gifticonImage);
        TextView gifticonBrand = findViewById(R.id.gifticonBrand);
        TextView gifticonName  = findViewById(R.id.gifticonName);
        TextView gifticonBuyDate = findViewById(R.id.gifticonBuyDate);
        TextView gifticonExpirydate = findViewById(R.id.gifticonExpirydate);
        TextView gifticondetail = findViewById(R.id.gifticondetail);

        // 해당 기프티콘 재고 객체
        GIFTICONADST selectedgifticonhistorydata = gifticondataList_h.get(0);
        // 해당 기프티콘 데이터 객체
        GIFTICONDATA gifticondata = gifticondataList_d.get(0);

        // 날짜 형식 정의
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        // 기프티콘 바코드가 비어있지 않을 경우
        if(selectedgifticonhistorydata.getGifticonBarcode()!=null && selectedgifticonhistorydata.getGifticonBarcode()!=""){
            // 이미지 출력
            imageInsert(gifticonBarcode, selectedgifticonhistorydata.getGifticonBarcode());}
        else{
            // 비어있을 경우 더미 이미지 url 사용
            imageInsert(gifticonBarcode, "GIFTICONBARCODE/barcode_dummy.png");}
        // xml 요소 데이터 설정
        gifticonBrand.setText(gifticondata.getgifticon_Brand());
        gifticonName.setText(gifticondata.getgifticon_Name());
        gifticonExpirydate.setText(sdf.format(dateFormat.parse(selectedgifticonhistorydata.getGifticonExpirydate().toString())));
        gifticonBuyDate.setText(sdf.format(dateFormat.parse(selectedgifticonhistorydata.getBuydate().toString())));
        gifticondetail.setText(gifticondata.getgifticon_Detail());
    }

    // 이미지 등록 함수: url을 받으면 db에서 이미지를 가져와 등록하는 함수
    public static void imageInsert(ImageView load, String uri){
        // db
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("GIFTICONBARCODE");
        if (pathReference==null){
            // 경로에 사진이 없을 경우
            Toast.makeText(load.getContext(), "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
        }else{
            StorageReference submitProfile;
            if(uri!=null || uri!="" || uri!=" "){
                // url이 존재할 경우
                submitProfile = storageReference.child(uri);}
            else {
                // url이 존재하지 않을 경우
                // 더미 이미지 url로 대체한다.
                submitProfile = storageReference.child("gifticon_dummy.png");}
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // db에서 이미지 다운로드에 성공한 경우
                    // ImageView에 이미지를 설정한다.
                    Glide.with(load.getContext()).load(uri).into(load);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }
}