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

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        String user_num = FirebaseAuth.getInstance().getUid();

        DatabaseReference finalReference = reference;
        reference.child("USER_GIFTICON").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticondataList_u.clear();
                gifticondataList_hu.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    USER_GIFTICON uig = data.getValue(USER_GIFTICON.class);

                    // uid에 따라 구분
                    String uig_num = (String) uig.getuser_num();
                    if(uig.getuser_num().equals(user_num)){
                        gifticondataList_u.add(uig);
                        Log.d("태그",gifticondataList_u+"가있...다");
                    }
                }
                Log.d("태그",gifticondataList_u+"가있.다");

                finalReference.child("GIFTICONADST").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        gifticonadstList_h.clear();

                        for(DataSnapshot data : snapshot.getChildren()){
                            GIFTICONADST uig = data.getValue(GIFTICONADST.class);
                            gifticonadstList_h.add(uig);
                        }

                        for(USER_GIFTICON u : gifticondataList_u){
                            for(GIFTICONADST h : gifticonadstList_h){
                                if (h.getad_Num() == u.getgifticon_ad_Num()){
                                    gifticondataList_hu.add(h);
                                }
                            }
                        }

                        gifticonListView_h= (ListView) findViewById(R.id.gifticon_listView_history);
                        final GifticonHistoryAdapter gifticonHistoryAdapter = new GifticonHistoryAdapter(getApplicationContext(),R.layout.item_gifticon_history, gifticondataList_hu,gifticonListView_h);
                        gifticonListView_h.setAdapter(gifticonHistoryAdapter);

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

        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });
    }

    public void putItemData(){
        gifticonListView_h.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                GIFTICONADST selectgifticondata = (GIFTICONADST) gifticonListView_h.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), CouponHistoryDetail.class);

                intent.putExtra("gifticonHistoryNum",selectgifticondata.getad_Num());
                startActivity(intent);
            }
        });
    }
}