package com.example.kmj_reco;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.DTO.RECOBIN;
import com.example.kmj_reco.utils.GifticonAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecobinAdminEdit extends AppCompatActivity {
    private DatabaseReference reference;

    List<RECOBIN> recobinList3 = new ArrayList<>();

    RECOBIN oriRecobinData;
    RECOBIN newRecobinData;

    EditText recobin_num;
    EditText recobin_address;
    EditText recobin_roadname;
    EditText recobin_locate;
    EditText recobin_fulladdress;
    EditText recobin_latitude;
    EditText recobin_longitude;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recobin_admin_edit);

        Intent thisIntent = getIntent();
        int num = thisIntent.getIntExtra("num",0);
        int recobin_num = thisIntent.getIntExtra("indexNum",0);

        ImageButton btn_update = (ImageButton) findViewById(R.id.btn_update);

        FirebaseAuth.getInstance().signInAnonymously();
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("RECOBIN").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recobinList3.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    RECOBIN recoBin = data.getValue(RECOBIN.class);
                    if (recobin_num == recoBin.getRecobin_num()){
                        recobinList3.add(recoBin);}
                }
                setData();
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            newRecobinData.setRecobin_address(recobin_address.getText().toString());
                            newRecobinData.setRecobin_roadname(recobin_roadname.getText().toString());
                            newRecobinData.setRecobin_fulladdress(recobin_fulladdress.getText().toString());
                            newRecobinData.setRecobin_locate(recobin_locate.getText().toString());
                            newRecobinData.setRecobin_latitude(Double.valueOf(recobin_latitude.getText().toString()));
                            newRecobinData.setRecobin_longitude(Double.valueOf(recobin_longitude.getText().toString()));

                            Intent intent = new Intent(getApplicationContext(), RecobinAdmin.class);
                            intent.putExtra("num", num);
                            startActivity(intent);
                        } catch (NumberFormatException e) {
                            Toast.makeText(getApplicationContext(), "숫자로 적어주세요.", Toast.LENGTH_SHORT);
                        }
                        reference.child("RECOBIN").child(String.valueOf(oriRecobinData.getRecobin_num())).setValue(newRecobinData);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });
    }

    void setData() {
        oriRecobinData = recobinList3.get(0);
        newRecobinData = oriRecobinData;

        recobin_num = findViewById(R.id.recobin_num);
        recobin_address = findViewById(R.id.recobin_address);
        recobin_roadname = findViewById(R.id.recobin_roadname);
        recobin_locate = findViewById(R.id.recobin_locate);
        recobin_fulladdress = findViewById(R.id.recobin_fulladdress);
        recobin_latitude = findViewById(R.id.recobin_latitude);
        recobin_longitude = findViewById(R.id.recobin_longitude);

        try {
            RECOBIN selectedRecobin = recobinList3.get(0);
            recobin_num.setText(Integer.toString(selectedRecobin.getRecobin_num()));
            recobin_address.setText(selectedRecobin.getRecobin_address());
            recobin_roadname.setText(selectedRecobin.getRecobin_roadname());
            recobin_locate.setText(selectedRecobin.getRecobin_locate());
            recobin_fulladdress.setText(selectedRecobin.getRecobin_fulladdress());
            recobin_latitude.setText(selectedRecobin.getRecobin_latitude().toString());
            recobin_longitude.setText(selectedRecobin.getRecobin_longitude().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "숫자로 적어주세요.", Toast.LENGTH_SHORT);
        }
    }

    public void onActivityResult(int requestCode, final int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = getIntent();
        int num = intent.getIntExtra("num",0);
        int recobin_num = intent.getIntExtra("indexNum",0);
    }
}
