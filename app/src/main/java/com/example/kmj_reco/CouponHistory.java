package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.DTO.USER_GIFTICON;
import com.example.kmj_reco.utils.GifticonHistoryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CouponHistory extends AppCompatActivity {

    // db
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference sTreference;

    ListView gifticonListView_h;
    static List<GIFTICONADST> gifticonadstList_h = new ArrayList<>();
    static List<USER_GIFTICON> gifticondataList_u = new ArrayList<>();
    static List<GIFTICONADST> gifticondataList_hu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_history);

        // db
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        DatabaseReference finalReference = reference;

        // 사용자 uid
        String user_num = FirebaseAuth.getInstance().getUid();


        // 사용자 기프티콘 구매 목록 데이터 가져오기
        reference.child("USER_GIFTICON").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 리스트 초기화
                gifticondataList_u.clear();
                gifticondataList_hu.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    USER_GIFTICON uig = data.getValue(USER_GIFTICON.class);

                    // 현재 사용자의 구매 목록만 가져오기
                    if(uig.getuser_num().equals(user_num)){
                        gifticondataList_u.add(uig);
                    }
                }

                // 기프티콘 바코드 데이터 가져오기
                finalReference.child("GIFTICONADST").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // 리스트 초기화
                        gifticonadstList_h.clear();

                        for(DataSnapshot data : snapshot.getChildren()){
                            GIFTICONADST uig = data.getValue(GIFTICONADST.class);
                            gifticonadstList_h.add(uig);
                        }

                        // 구매 목록에 기록된 기프티콘 바코드만 가져오기
                        for(USER_GIFTICON u : gifticondataList_u){
                            for(GIFTICONADST h : gifticonadstList_h){
                                if (h.getad_Num() == u.getgifticon_ad_Num()){
                                    gifticondataList_hu.add(h);
                                }
                            }
                        }

                        // 목록에 표시
                        gifticonListView_h= (ListView) findViewById(R.id.gifticon_listView_history);
                        final GifticonHistoryAdapter gifticonHistoryAdapter = new GifticonHistoryAdapter(getApplicationContext(),R.layout.item_gifticon_history, gifticondataList_hu,gifticonListView_h);
                        gifticonListView_h.setAdapter(gifticonHistoryAdapter);
                        // 목록 item 클릭 이벤트
                        // 클릭 시 개별 기프티콘 바코드 상세화면으로 이동
                        putItemData();
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
                // 마이페이지로 이동
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
            }
        });
    }

    // 목록 item 클릭 이벤트
    public void putItemData(){
        gifticonListView_h.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 해당 item 데이터
                GIFTICONADST selectgifticondata = (GIFTICONADST) gifticonListView_h.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), CouponHistoryDetail.class);
                // 해당 item의 기프티콘 식별번호를 다음 activity로 넘김
                // 기프티콘 내역 상세화면으로 이동
                intent.putExtra("gifticonHistoryNum",selectgifticondata.getad_Num());
                startActivity(intent);
            }
        });
    }
}