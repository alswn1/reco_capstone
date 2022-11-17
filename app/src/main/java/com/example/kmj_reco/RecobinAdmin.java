package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

    private List<RECOBIN> recobinList = new ArrayList<RECOBIN>();
    private List<RECOBIN> recobinList2 = new ArrayList<RECOBIN>();
    private List<RECOBIN> recobinList3= new ArrayList<RECOBIN>();

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_recobin_admin);

        recobinListView = (ListView) findViewById(R.id.recobin_listView_admin);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference.child("RECOBIN").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recobinList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    RECOBIN recoBin = data.getValue(RECOBIN.class);
                    recobinList.add(recoBin);
                }

                searchfun(recobinList);
                final RecobinAdapter recobinAdapter = new RecobinAdapter(getApplicationContext(),0, recobinList,recobinListView);

                recobinListView.setAdapter(recobinAdapter);
                setUpOnClickListener();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });

        ImageView btn_new_recobin = (ImageView) findViewById(R.id.btn_new_recobin);
        btn_new_recobin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecobinAdminAdd.class);
                startActivity(intent);
            }
        });
    }

    private void setUpOnClickListener(){
        recobinListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                RECOBIN selectedRecobin = (RECOBIN) recobinListView.getItemAtPosition(position);

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

    private void searchfun(List<RECOBIN> recobinList){
        SearchView searchView = (SearchView) findViewById(R.id.recobin_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { return false; }

            @Override
            public boolean onQueryTextChange(String s) {
                List<RECOBIN> filterRecobin = new ArrayList<>();
                for (RECOBIN data : recobinList){
                    if(data.getRecobin_fulladdress().toLowerCase().contains(s.toLowerCase())){
                        filterRecobin.add(data);
                    }
                }
                final RecobinAdapter recobinAdapter = new RecobinAdapter(getApplicationContext(),0, filterRecobin,recobinListView);

                recobinListView.setAdapter(recobinAdapter);
                return false;
            }
        });
    }
}