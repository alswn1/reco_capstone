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
import com.example.kmj_reco.utils.GifticonAdminAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CouponAdmin extends AppCompatActivity {
    ListView gifticonListView;

    private List<GIFTICONDATA> gifticondataList = new ArrayList<GIFTICONDATA>();
    private List<GIFTICONDATA> gifticondataList2 = new ArrayList<GIFTICONDATA>();

    // db
    FirebaseStorage storage;
    StorageReference sTreference;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_admin);

        // xml 요소
        gifticonListView = (ListView) findViewById(R.id.gifticon_listView_admin);

        // db
        storage = FirebaseStorage.getInstance();
        sTreference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // 기프티콘 데이터 가져오기
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
                final GifticonAdminAdapter gifticonAdminAdapter = new GifticonAdminAdapter(getApplicationContext(),0, gifticondataList,gifticonListView);
                gifticonListView.setAdapter(gifticonAdminAdapter);
                // 기프티콘 어드민 목록 item 클릭 이벤트
                // 클릭 시 개별 기프티콘 어드민 상세화면으로 이동
                setUpOnClickListener();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });

        // 추가 버튼 클릭 이벤트
        Button btn_cp = (Button) findViewById(R.id.couponAdminPlusbtn);
        btn_cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 기프티콘 추가 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), CouponAdminDetailPlus.class);
                startActivity(intent);
            }
        });
        // 뒤로가기 버튼 클릭 이벤트
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 어드민 홈 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                startActivity(intent);
            }
        });
    }


    // 기프티콘 목록 item 클릭 이벤트
    private void setUpOnClickListener(){
        gifticonListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 해당 item 데이터
                GIFTICONDATA selectgifticondata = (GIFTICONDATA) gifticonListView.getItemAtPosition(position);
                // 해당 item 데이터를 다음 activity로 넘김
                // 해당 기프티콘 어드민 상세화면으로 이동
                Intent showDetail = new Intent(getApplicationContext(),CouponAdminDetail.class);
                showDetail.putExtra("num", selectgifticondata.getgifticon_Num());
                showDetail.putExtra("indexNum", position);
                Intent showDetail2 = new Intent(getApplicationContext(), CouponAdminDetailPlus.class);
                showDetail2.putExtra("num", selectgifticondata.getgifticon_Num());
                showDetail2.putExtra("indexNum", position);

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
                final GifticonAdminAdapter gifticonAdminAdapter = new GifticonAdminAdapter(getApplicationContext(),0, filtergifticon,gifticonListView);
                gifticonListView.setAdapter(gifticonAdminAdapter);
                return false;
            }
        });
    }
}