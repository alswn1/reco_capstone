package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    FirebaseStorage storage;
    StorageReference sTreference;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_admin);

        gifticonListView = (ListView) findViewById(R.id.gifticon_listView_admin
        );
        storage = FirebaseStorage.getInstance();
        sTreference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference.child("GIFTICONDATA").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticondataList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONDATA gif = data.getValue(GIFTICONDATA.class);
                    gifticondataList.add(gif);
                }

                searchfun(gifticondataList);
                final GifticonAdminAdapter gifticonAdminAdapter = new GifticonAdminAdapter(getApplicationContext(),0, gifticondataList,gifticonListView);

                gifticonListView.setAdapter(gifticonAdminAdapter);
                setUpOnClickListener();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });

        ImageView btn_home = (ImageView) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        ImageView btn_alert = (ImageView) findViewById(R.id.btn_alert);
        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });

        Button btn_cp = (Button) findViewById(R.id.couponAdminPlusbtn);
        btn_cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CouponAdminDetailPlus.class);
                startActivity(intent);
            }
        });
    }

    private void setUpOnClickListener(){
        gifticonListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                GIFTICONDATA selectgifticondata = (GIFTICONDATA) gifticonListView.getItemAtPosition(position);
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
                    if(data.getgifticon_Name().toLowerCase().contains(s.toLowerCase())){
                        filtergifticon.add(data);
                    }
                }
                final GifticonAdminAdapter gifticonAdminAdapter = new GifticonAdminAdapter(getApplicationContext(),0, filtergifticon,gifticonListView);

                gifticonListView.setAdapter(gifticonAdminAdapter);
                return false;
            }
        });
    }
}