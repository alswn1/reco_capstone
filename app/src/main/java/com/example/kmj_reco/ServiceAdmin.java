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
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_admin);

        ServiceAdminListView = (ListView) findViewById(R.id.ServiceAdminListView);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference.child("ServiceAccount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServiceAccountList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    ServiceAccount SA = data.getValue(ServiceAccount.class);
                    ServiceAccountList.add(SA);
                }
                final ServiceAdminAdapter serviceAdminAdapter = new ServiceAdminAdapter(getApplicationContext(),0,  ServiceAccountList,ServiceAdminListView);
                ServiceAdminListView.setAdapter(serviceAdminAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });
        setUpOnClickListener();
    }

    private void setUpOnClickListener(){
        ServiceAdminListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ServiceAccount selectServiceAccount = (ServiceAccount) ServiceAdminListView.getItemAtPosition(position);

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