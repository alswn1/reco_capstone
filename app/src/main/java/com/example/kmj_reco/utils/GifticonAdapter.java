package com.example.kmj_reco.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.kmj_reco.CouponDetail;
import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.DTO.GIFTICONDATA;
import com.example.kmj_reco.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class GifticonAdapter extends ArrayAdapter<GIFTICONDATA> {
    private List gifticonList;
    private List<GIFTICONADST> gifticonadstList = new ArrayList<>();
    private ListView gifticonListView;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;
    class UserViewHolder {
        private ImageView gifticon_Image;
        private TextView gifticon_Price;
        private TextView gifticon_Name;
        private TextView gifticon_Brand;
    }

    public GifticonAdapter(Context context, int resource, List<GIFTICONDATA>gifticonlist, ListView gifticonListView){
        super(context,resource,gifticonlist);
        this.context = context;
        this.gifticonList=gifticonlist;
        this.gifticonListView=gifticonListView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserViewHolder viewHolder;
        String Status;
        View mView = convertView;

        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.item_gifticon, parent, false);

            viewHolder = new UserViewHolder();
            viewHolder.gifticon_Image = (ImageView) mView.findViewById(R.id.gifticonImage);
            viewHolder.gifticon_Brand = (TextView) mView.findViewById(R.id.gifticonBrand);
            viewHolder.gifticon_Name = (TextView) mView.findViewById(R.id.gifticonName);
            viewHolder.gifticon_Price = (TextView) mView.findViewById(R.id.gifticonPrice);

            mView.setTag(viewHolder);

            Status = "created";
        }else{
            viewHolder = (UserViewHolder) mView.getTag();

            Status = "reused";
        }
        TextView gifticonStodk= (TextView) mView.findViewById(R.id.gifticonStodk);

        GIFTICONDATA gifticondata = (GIFTICONDATA) gifticonList.get(position);
        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("GIFTICONADST").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticonadstList.clear();
                for (DataSnapshot data: snapshot.getChildren()){
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);
                    if(gif.getGifticonNum()==gifticondata.getgifticon_Num() && gif.isGifticonUsage()==false){
                        gifticonadstList.add(gif);}
                }
                if (gifticonadstList.size()==0){
                    gifticonStodk.setVisibility(View.VISIBLE);
                    gifticonStodk.setText("재고없음");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        imageInsert(viewHolder.gifticon_Image, gifticondata.getgifticon_Image());
        viewHolder.gifticon_Brand.setText(gifticondata.getgifticon_Brand());
        viewHolder.gifticon_Name.setText(gifticondata.getgifticon_Name());
        viewHolder.gifticon_Price.setText(Integer.toString(gifticondata.getgifticon_Price())+" 원");

        Button button = (Button) mView.findViewById(R.id.gifticonBuyButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showDetail = new Intent(context, CouponDetail.class);
                showDetail.putExtra("num", gifticondata.getgifticon_Num());
                showDetail.putExtra("image", gifticondata.getgifticon_Image());
                showDetail.putExtra("name", gifticondata.getgifticon_Name());
                showDetail.putExtra("price", gifticondata.getgifticon_Price());
                showDetail.putExtra("brand", gifticondata.getgifticon_Brand());
                showDetail.putExtra("detail", gifticondata.getgifticon_Detail());
                showDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(showDetail);
            }
        });
        return mView;
    } private Context context;


    public static void imageInsert(ImageView load, String uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("GIFTICON");
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
