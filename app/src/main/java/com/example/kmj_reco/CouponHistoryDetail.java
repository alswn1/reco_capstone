package com.example.kmj_reco;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.DTO.GIFTICONDATA;
import com.example.kmj_reco.DTO.USER_GIFTICON;
import com.example.kmj_reco.utils.GifticonAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class CouponHistoryDetail extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference sTreference;

    static List<GIFTICONADST> gifticondataList_h = new ArrayList<>();
    static List<GIFTICONDATA> gifticondataList_d = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_history_detail);

        Intent intent = getIntent();
        int adNum = intent.getIntExtra("gifticonHistoryNum",0);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference.child("GIFTICONADST").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticondataList_h.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONADST uig1 = data.getValue(GIFTICONADST.class);
                    if (uig1.getad_Num() == adNum){
                        gifticondataList_h.add(uig1);}
                }

                reference.child("GIFTICONDATA").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        gifticondataList_d.clear();

                        for(DataSnapshot data : snapshot.getChildren()){
                            GIFTICONDATA uig2 = data.getValue(GIFTICONDATA.class);
                            if (uig2.getgifticon_Num()==gifticondataList_h.get(0).getGifticonNum()){
                                gifticondataList_d.add(uig2);
                                Log.d("태그",gifticondataList_d+"이다");}
                        }
                        try {
                            setData();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
    }

    public void setData() throws ParseException {
        ImageView gifticonBarcode = findViewById(R.id.gifticonImage);
        TextView gifticonBrand = findViewById(R.id.gifticonBrand);
        TextView gifticonName  = findViewById(R.id.gifticonName);
        TextView gifticonBuyDate = findViewById(R.id.gifticonBuyDate);
        TextView gifticonExpirydate = findViewById(R.id.gifticonExpirydate);
        TextView gifticondetail = findViewById(R.id.gifticondetail);

        if(gifticondataList_h.size()!=0) {
            GIFTICONADST selectedgifticonhistorydata = gifticondataList_h.get(0);
            GIFTICONDATA gifticondata = gifticondataList_d.get(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            Log.d("태그",selectedgifticonhistorydata.getGifticonBarcode()+"이다");
            if(selectedgifticonhistorydata.getGifticonBarcode()!=null && selectedgifticonhistorydata.getGifticonBarcode()!=""){
                imageInsert(gifticonBarcode, selectedgifticonhistorydata.getGifticonBarcode());}
            else{
                imageInsert(gifticonBarcode, "GIFTICONBARCODE/barcode_dummy.png");}
            gifticonBrand.setText(gifticondata.getgifticon_Brand());
            gifticonName.setText(gifticondata.getgifticon_Name());
            gifticonExpirydate.setText(sdf.format(dateFormat.parse(selectedgifticonhistorydata.getGifticonExpirydate().toString())));
            gifticonBuyDate.setText(sdf.format(dateFormat.parse(selectedgifticonhistorydata.getBuydate().toString())));
            gifticondetail.setText(gifticondata.getgifticon_Detail());
        }
    }

    public static void imageInsert(ImageView load, String uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("GIFTICONBARCODE");
        if (pathReference==null){
            Toast.makeText(load.getContext(), "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT);
        }else{
            StorageReference submitProfile;
            if(uri!=null || uri!="" || uri!=" "){
                submitProfile = storageReference.child(uri);}
            else {
                submitProfile = storageReference.child("gifticon_dummy.png");}
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(load.getContext()).load(uri).into(load);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }
}