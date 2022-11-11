package com.example.kmj_reco;

import android.content.Intent;
import static android.content.ContentValues.TAG;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kmj_reco.DTO.ServiceAccount;
import com.example.kmj_reco.utils.ServiceAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceHistoryActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    ListView serviceAccountListView;

    static List<ServiceAccount> serviceAccountList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_history);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        FirebaseFirestore fs = FirebaseFirestore.getInstance();

        /*
        fs.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<ServiceAccount> postList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                    postList.add(new ServiceAccount(
                                            document.getData().get("title").toString(),
                                            (ArrayList<String>) document.getData().get("contents"),
                                            document.getData().get("publisher").toString()));

                                    }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/
/*
        reference.child("Service").limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceAccountList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    ServiceAccount Service = data.getValue(ServiceAccount.class);
                    serviceAccountList.add(Service);
                }
                final ServiceAdapter serviceAdapter = new ServiceAdapter(getApplicationContext(),R.layout.activity_item_service, serviceAccountList, serviceAccountListView);
                serviceAccountListView.setAdapter(serviceAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });*/

        // 더미 데이터
        if (serviceAccountList.size()<=0) {}
        serviceAccountListView = (ListView) findViewById(R.id.serviceAccountListView);

        setUpOnClickListener();
    }

    private void setValues(){
        TextView title = findViewById(R.id.et_title);
        TextView contents = findViewById(R.id.et_contents);

        Intent intent = getIntent();

        String title1 = intent.getStringExtra("title");
        String contents1 = intent.getStringExtra("contents");

        title.setText(title1);
        contents.setText(contents1);
    }

    // 아이템 클릭 시 ServiceDetailActivity 화면에 각 데이터 전송
    private void setUpOnClickListener() {
        serviceAccountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                ServiceAccount selectedService = (ServiceAccount) serviceAccountListView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), ServiceDetailActivity.class);
                showDetail.putExtra("num", selectedService.getService_Num());
                showDetail.putExtra("title", selectedService.getService_Title());
                showDetail.putExtra("date", selectedService.getService_Date());
                showDetail.putExtra("contents", selectedService.getService_Contents());
                startActivity(showDetail);
            }
        });
    }
}