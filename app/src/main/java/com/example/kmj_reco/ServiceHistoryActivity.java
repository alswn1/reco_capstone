package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.ServiceAccount;
import com.example.kmj_reco.utils.ServiceAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ServiceHistoryActivity extends AppCompatActivity {
    // 데이터베이스 선언
    FirebaseDatabase database;
    DatabaseReference reference;

    ListView ServiceListView;
    static List<ServiceAccount> serviceAccountList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_history);

        // 홈 버튼 클릭 이벤트
        ImageView btn_home = (ImageView) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 홈으로 이동
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        // 설정 버튼 클릭 이벤트
        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 설정으로 이동
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        // 알림 버튼 클릭 이벤트
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
        ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ServiceCenterActivity로 이동
                Intent intent = new Intent(getApplicationContext(), ServiceCenterActivity.class);
                startActivity(intent);
            }
        });

        // xml 요소
        ServiceListView =(ListView) findViewById(R.id.serviceAccountListView);

        // DB 설정
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // ServiceAccount 데이터 가져오기
        reference.child("ServiceAccount").limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceAccountList.clear(); // 문의 리스트 초기화

                for(DataSnapshot data : snapshot.getChildren()){
                    ServiceAccount serviceAccount = data.getValue(ServiceAccount.class);
                    serviceAccountList.add(serviceAccount);
                }

                // ServiceAdapter에서 데이터 불러오기
                final ServiceAdapter serviceAdapter = new ServiceAdapter(getApplicationContext(),R.layout.activity_item_service, serviceAccountList, ServiceListView);
                ServiceListView.setAdapter(serviceAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 더미 데이터
        if (serviceAccountList.size()<=0) {}
        ServiceListView = (ListView) findViewById(R.id.serviceAccountListView);

        setUpOnClickListener();
    }

    // 아이템 클릭 시 ServiceDetailActivity 화면에 각 데이터 전송
    private void setUpOnClickListener() {
        ServiceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 문의 데이터 전송 및 이동
                ServiceAccount selectedService = (ServiceAccount) ServiceListView.getItemAtPosition(position);

                Intent showDetail = new Intent(getApplicationContext(), ServiceDetailActivity.class);
                showDetail.putExtra("num", selectedService.getService_Num());
                showDetail.putExtra("title", selectedService.getService_Title());
                showDetail.putExtra("date", selectedService.getService_Date());
                showDetail.putExtra("publisher", selectedService.getService_Publisher());
                showDetail.putExtra("contents", selectedService.getService_Contents());
                
                startActivity(showDetail);
            }
        });
    }
}