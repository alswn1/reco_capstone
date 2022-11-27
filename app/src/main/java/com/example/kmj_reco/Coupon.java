package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.GIFTICONDATA;
import com.example.kmj_reco.utils.GifticonAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Coupon extends AppCompatActivity {
    ListView gifticonListView;
    AdView mAdView;

    private List<GIFTICONDATA> gifticondataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        // Google AD
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        // RECO 로고 클릭 이벤트
        ImageView btn_home = (ImageView) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 홈 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

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
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        // xml 요소
        gifticonListView = (ListView) findViewById(R.id.gifticon_listView);


        // db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();

        // 기프티콘 데이터 받아오기
        reference.child("GIFTICONDATA").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 리스트 초기화
                gifticondataList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONDATA gif = data.getValue(GIFTICONDATA.class);
                    gifticondataList.add(gif);
                }
                // 검색 기능 : 기프티콘 이름으로 검색
                searchfun(gifticondataList);
                // 목록에 기프티콘 표시
                final GifticonAdapter gifticonAdapter = new GifticonAdapter(getApplicationContext(), R.layout.item_gifticon, gifticondataList, gifticonListView);
                gifticonListView.setAdapter(gifticonAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("태그","error");
            }
        });
        // 기프티콘 목록 item 클릭 이벤트
        // 클릭 시 개별 기프티콘 상세화면으로 이동
        setUpOnClickListener();
    }

    // 기프티콘 목록 item 클릭 이벤트
    private void setUpOnClickListener() {
        gifticonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // 해당 item 데이터
                GIFTICONDATA selectgifticondata = (GIFTICONDATA) gifticonListView.getItemAtPosition(position);
                // 해당 item 데이터를 다음 activity로 넘김
                // 해당 기프티콘 상세화면으로 이동
                Intent showDetail = new Intent(getApplicationContext(), CouponDetail.class);
                showDetail.putExtra("num", selectgifticondata.getgifticon_Num());
                showDetail.putExtra("image", selectgifticondata.getgifticon_Image());
                showDetail.putExtra("name", selectgifticondata.getgifticon_Name());
                showDetail.putExtra("price", selectgifticondata.getgifticon_Price());
                showDetail.putExtra("brand", selectgifticondata.getgifticon_Brand());
                showDetail.putExtra("detail", selectgifticondata.getgifticon_Detail());
                startActivity(showDetail);
            }
        });
    }

    // 검색 기능 추가 함수
    private void searchfun(List<GIFTICONDATA> gifticondataList){
        SearchView searchView = (SearchView) findViewById(R.id.gifticon_searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<GIFTICONDATA> filtergifticon = new ArrayList<>();
                for (GIFTICONDATA data : gifticondataList){
                    // 적은 글자가 기프티콘 이름에 포함된 경우 기프티콘 정보를 가져온다.
                    if(data.getgifticon_Name().toLowerCase().contains(s.toLowerCase())){
                        filtergifticon.add(data);
                    }
                }
                // 목록에 표시
                GifticonAdapter gifticonAdapter = new GifticonAdapter(getApplicationContext(), R.layout.item_gifticon, filtergifticon, gifticonListView);
                gifticonListView.setAdapter(gifticonAdapter);
                return false;
            }
        });
    }
}

