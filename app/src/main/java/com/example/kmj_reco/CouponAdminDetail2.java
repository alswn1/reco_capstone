package com.example.kmj_reco;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.utils.GifticonAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CouponAdminDetail2 extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference reference;

    List<GIFTICONADST> gifticonADSTList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_admin_detail2);
        // 이전 Activity에서 전달된 데이터 가져오기
        Intent thisIntent = getIntent();
        int gifticonDatanum = thisIntent.getIntExtra("num",0);
        int gifticonADSTnum = thisIntent.getIntExtra("gifticonadst_num",0);

        // db
        FirebaseAuth.getInstance().signInAnonymously();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();


        // 기프티콘 재고 데이터 가져오기
        reference.child("GIFTICONADST").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticonADSTList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);
                    // 해당 기프티콘의 재고만 가져오기
                    if (gif.getad_Num()==gifticonADSTnum){
                        gifticonADSTList.add(gif);}
                }
                // xml 요소 설정
                setValues(gifticonDatanum, gifticonADSTnum);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });

        // 삭제 버튼 클릭 이벤트
        Button btn_d = (Button) findViewById(R.id.gifticonADDeleteBtn);
        btn_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 삭제 확인 팝업
                AlertDialog.Builder builder = new AlertDialog.Builder(CouponAdminDetail2.this);
                builder.setTitle("삭제").setMessage("정말 삭제하시겠습니까?");

                // 예 클릭시
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 해당 기프티콘 재고 삭제
                        reference.child("GIFTICONADST").child(String.valueOf(gifticonADSTnum)).removeValue();
                        // 해당 기프티콘 데이터 전달
                        // 해당 기프티콘 어드민 상세화면으로 이동
                        Intent intent=new Intent(getApplicationContext(), CouponAdminDetail.class);
                        intent.putExtra("num",gifticonDatanum);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // 수정 버튼 클릭 이벤트
        Button btn_e = (Button) findViewById(R.id.gifticonADEditButton);
        btn_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 해당 기프티콘 식별번호, 기프티콘 재고 식별번호 전달
                // 기프티콘 재고 수정 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), CouponAdminDetail2Edit.class);
                intent.putExtra("num", gifticonDatanum);
                intent.putExtra("gifticonadst_num", gifticonADSTnum);
                startActivity(intent);
            }
        });
    }


    // xml 데이터 설정 함수
    private void setValues(int gifticonDatanum, int gifticonADSTnum){

        //xml 요소
        TextView gifticonadst_num = findViewById(R.id.gifticonadst_num);
        ImageView gifticonBarcode = findViewById(R.id.gifticonImage);
        TextView gifticon_usage = findViewById(R.id.gifticon_usage);
        TextView buydate = findViewById(R.id.buydate);
        TextView usedate = findViewById(R.id.usedate);
        TextView gifticonExpirydate = findViewById(R.id.gifticonExpirydate);


        // 해당 기프티콘 재고 객체
        GIFTICONADST selectedgifticondata = gifticonADSTList.get(0);

        // 날짜 형식 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");

        // xml요소 데이터 설정
        gifticonadst_num.setText("재고 번호 :" + Integer.toString(gifticonADSTnum));
        GifticonAdapter.imageInsert(gifticonBarcode, selectedgifticondata.getGifticonBarcode());
        // 사용됨 여부가 true일 경우 O
        if(selectedgifticondata.isGifticonUsage()){
            gifticon_usage.setText("O");
        }else{
            // 사용됨 여부가 false일 경우 X
            gifticon_usage.setText("X");
        }
        buydate.setText(sdf.format(selectedgifticondata.getBuydate()));
        usedate.setText(sdf.format(selectedgifticondata.getUsedate()));

        // 만료 기한 데이터가 없을 경우 기록없음
        if(selectedgifticondata.getGifticonExpirydate()==null){
            gifticonExpirydate.setText("기록 없음");
        }else{
            // 존재할 경우 표시
            gifticonExpirydate.setText(sdf.format(selectedgifticondata.getGifticonExpirydate()));}
    }
}