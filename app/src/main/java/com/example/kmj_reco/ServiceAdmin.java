package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.kmj_reco.DTO.ServiceAccount;
import com.example.kmj_reco.utils.ServiceAdminAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdmin extends AppCompatActivity {
    ListView ServiceAdminListView;
    private List<ServiceAccount> ServiceAccountList = new ArrayList<ServiceAccount>();

    // 데이터베이스 선언
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_admin);

        // xml 요소
        ServiceAdminListView = (ListView) findViewById(R.id.ServiceAdminListView);

        // DB 설정
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // ServiceAccount 데이터 가져오기
        reference.child("ServiceAccount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServiceAccountList.clear(); // 리스트 초기화

                for(DataSnapshot data : snapshot.getChildren()){
                    ServiceAccount SA = data.getValue(ServiceAccount.class);
                    ServiceAccountList.add(SA);
                }

                // ServiceAdminAdapter 불러오기
                final ServiceAdminAdapter serviceAdminAdapter = new ServiceAdminAdapter(getApplicationContext(),0,  ServiceAccountList,ServiceAdminListView);
                ServiceAdminListView.setAdapter(serviceAdminAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });
        setUpOnClickListener(); // item 클릭 이벤트 함수 호출
    }

    // ServiceAccount 목록 item 클릭 이벤트
    private void setUpOnClickListener(){
        ServiceAdminListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 해당 item 데이터
                ServiceAccount selectServiceAccount = (ServiceAccount) ServiceAdminListView.getItemAtPosition(position);

                // 해당 item 데이터를 다음 activity로 전송
                // 해당 서비스 어드민 상세화면으로 이동
                Intent showDetail = new Intent(getApplicationContext(), ServiceAdminDetail.class);
                showDetail.putExtra("num", selectServiceAccount.getService_Num());
                showDetail.putExtra("title", selectServiceAccount.getService_Title());
                showDetail.putExtra("contents", selectServiceAccount.getService_Contents());
                showDetail.putExtra("date", selectServiceAccount.getService_Date());
                showDetail.putExtra("publisher", selectServiceAccount.getService_Publisher());
                startActivity(showDetail);
            }
        });
    }
}