package com.example.kmj_reco;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.DTO.GIFTICONDATA;
import com.example.kmj_reco.utils.GifticonAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CouponDetail extends AppCompatActivity {
    private List<GIFTICONADST> gifticonadstList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);

        // 이전 Activity에서 전달된 데이터 가져오기
        Intent intent = getIntent();
        int num = intent.getIntExtra("num", 0);

        // 톱니바퀴 버튼 클릭 이벤트
        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 설정으로 이동
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        // 종 모양 버튼 클릭 이벤트
        ImageView btn_alert = (ImageView) findViewById(R.id.btn_alert);
        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 알림으로 이동
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼 클릭 이벤트
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 홈 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), Coupon.class);
                startActivity(intent);
            }
        });

        // 구매 버튼 클릭 이벤트
        Button btn_buy = (Button) findViewById(R.id.btn_buy);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 쿠폰 구매 팝업 표시
                BuyCoupon e = BuyCoupon.getInstance();
                e.show(getSupportFragmentManager(),"");
            }
        });

        // 재고 확인
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("GIFTICONADST").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()){
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);
                    // 해당 기프티콘의 사용 가능한 기프티콘 재고 데이터를 리스트에 추가
                    if(gif.getGifticonNum()==num && gif.isGifticonUsage()==false){
                        gifticonadstList.add(gif);}
                }
                // 재고 수가 0 이하일 경우
                if(gifticonadstList.size()<=0){
                    // 재고없음 표시
                    btn_buy.setText("재고없음");
                    btn_buy.setBackgroundColor(Color.GRAY);
                    btn_buy.setEnabled(false);
                }else{}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // xml 요소 데이터 설정
        setValues();
    }


    // xml 데이터 설정 함수
    private void setValues(){

        //xml 요소
        ImageView gifticon_Image = findViewById(R.id.gifticonImage);
        TextView gifticon_Brand = findViewById(R.id.gifticonBrand);
        TextView gifticon_Name = findViewById(R.id.gifticonName);
        TextView gifticon_Price = findViewById(R.id.gifticonPrice);
        TextView gifticon_Detail = findViewById(R.id.gifticondetail);

        // 이전 Activity에서 데이터 전달받기
        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        String name = intent.getStringExtra("name");
        int price = intent.getIntExtra("price",100);
        String detail = intent.getStringExtra("detail");
        String brand = intent.getStringExtra("brand");

        // xml 요소 데이터 설정
        GifticonAdapter.imageInsert(gifticon_Image,image);
        gifticon_Brand.setText(brand);
        gifticon_Name.setText(name);
        gifticon_Price.setText(Integer.toString(price) + " p");
        gifticon_Detail.setText(detail);
    }
}