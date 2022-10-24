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

        Intent thisIntent = getIntent();
        int gifticonDatanum = thisIntent.getIntExtra("num",0);
        int gifticonADSTnum = thisIntent.getIntExtra("gifticonadst_num",0);

        // 데이터 전달
        FirebaseAuth.getInstance().signInAnonymously();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference.child("GIFTICONADST").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticonADSTList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);
                    if (gif.getad_Num()==gifticonADSTnum){
                        gifticonADSTList.add(gif);}
                }
                setValues(gifticonDatanum, gifticonADSTnum);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });

        Button btn_d = (Button) findViewById(R.id.gifticonADDeleteBtn);
        btn_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 삭제
                AlertDialog.Builder builder = new AlertDialog.Builder(CouponAdminDetail2.this);
                builder.setTitle("삭제").setMessage("정말 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reference.child("GIFTICONADST").child(String.valueOf(gifticonADSTnum)).removeValue();
                        Intent intent=new Intent(getApplicationContext(), CouponAdminDetail.class);
                        intent.putExtra("num",gifticonDatanum);
                        Log.d("태그",gifticonDatanum+"이다");
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

        Button btn_e = (Button) findViewById(R.id.gifticonADEditButton);
        btn_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 수정
                Intent intent = new Intent(getApplicationContext(), CouponAdminDetail2Edit.class);
                intent.putExtra("num", gifticonDatanum);
                intent.putExtra("gifticonadst_num", gifticonADSTnum);
                startActivity(intent);
            }
        });
    }

    private void setValues(int gifticonDatanum, int gifticonADSTnum){
        TextView gifticonadst_num = findViewById(R.id.gifticonadst_num);
        ImageView gifticonBarcode = findViewById(R.id.gifticonImage);
        TextView gifticon_usage = findViewById(R.id.gifticon_usage);
        TextView buydate = findViewById(R.id.buydate);
        TextView usedate = findViewById(R.id.usedate);
        TextView gifticonExpirydate = findViewById(R.id.gifticonExpirydate);

        GIFTICONADST selectedgifticondata = gifticonADSTList.get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 mm월 dd일");

        gifticonadst_num.setText("재고 번호 :" + Integer.toString(gifticonADSTnum));
        GifticonAdapter.imageInsert(gifticonBarcode, selectedgifticondata.getGifticonBarcode());
        if(selectedgifticondata.isGifticonUsage()){
            gifticon_usage.setText("O");
        }else{
            gifticon_usage.setText("X");
        }
        buydate.setText(sdf.format(selectedgifticondata.getBuydate()));
        usedate.setText(sdf.format(selectedgifticondata.getUsedate()));
        if(selectedgifticondata.getGifticonExpirydate()==null){
            gifticonExpirydate.setText("기록 없음");
        }else{
            gifticonExpirydate.setText(sdf.format(selectedgifticondata.getGifticonExpirydate()));}
    }
}