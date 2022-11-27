package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.RECOBIN;
import com.example.kmj_reco.utils.RecobinAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecobinAdmin extends AppCompatActivity {
    ListView recobinListView;

    // 레코빈 리스트 생성
    private List<RECOBIN> recobinList = new ArrayList<RECOBIN>();
    private List<RECOBIN> recobinList2 = new ArrayList<RECOBIN>();
    private List<RECOBIN> recobinList3= new ArrayList<RECOBIN>();

    // 데이터베이스 선언
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_recobin_admin);

        recobinListView = (ListView) findViewById(R.id.recobin_listView_admin);

        // DB 설정
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // DB 내 RECOBIN 테이블에서 데이터 불러오기
        reference.child("RECOBIN").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recobinList.clear(); // 레코빈 리스트 초기화

                for(DataSnapshot data : snapshot.getChildren()){
                    RECOBIN recoBin = data.getValue(RECOBIN.class);
                    recobinList.add(recoBin);
                }

                searchfun(recobinList); // 레코빈 리스트 중 검색 가능하게 설정

                // RecobinAdapter 불러오기
                final RecobinAdapter recobinAdapter = new RecobinAdapter(getApplicationContext(),0, recobinList,recobinListView);
                recobinListView.setAdapter(recobinAdapter);

                // 레코빈 리스트 item 클릭 이벤트
                setUpOnClickListener();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });

        // 레코빈 추가 버튼 터치 시 레코빈 추가 화면으로 이동
        ImageView btn_new_recobin = (ImageView) findViewById(R.id.btn_new_recobin);
        btn_new_recobin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecobinAdminAdd.class);
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼 터치 시 어드민 홈 화면으로 이동
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                startActivity(intent);
            }
        });
    }

    // 아이템 터치 이벤트
    private void setUpOnClickListener(){
        recobinListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 해당 item 데이터
                RECOBIN selectedRecobin = (RECOBIN) recobinListView.getItemAtPosition(position);

                // 레코빈 수정/추가 화면으로 데이터 전송 및 이동
                Intent showDetail = new Intent(getApplicationContext(), RecobinAdminEdit.class);
                showDetail.putExtra("num", selectedRecobin.getRecobin_num());
                showDetail.putExtra("indexNum", position);

                Intent showDetail2 = new Intent(getApplicationContext(), RecobinAdminAdd.class);
                showDetail2.putExtra("num", selectedRecobin.getRecobin_num());
                showDetail2.putExtra("indexNum", position);

                startActivity(showDetail);
            }
        });
    }

    // 검색 기능
    private void searchfun(List<RECOBIN> recobinList){
        SearchView searchView = (SearchView) findViewById(R.id.recobin_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { return false; }

            @Override
            public boolean onQueryTextChange(String s) {
                List<RECOBIN> filterRecobin = new ArrayList<>();

                // 작성한 주소가 리스트에 있는 레코빈의 상세주소에 포함된 경우 해당 데이터를 보여줌
                for (RECOBIN data : recobinList){
                    if(data.getRecobin_fulladdress().toLowerCase().contains(s.toLowerCase())){
                        filterRecobin.add(data);
                    }
                }

                // 리스트에 표시
                final RecobinAdapter recobinAdapter = new RecobinAdapter(getApplicationContext(),0, filterRecobin,recobinListView);
                recobinListView.setAdapter(recobinAdapter);

                return false;
            }
        });
    }
}