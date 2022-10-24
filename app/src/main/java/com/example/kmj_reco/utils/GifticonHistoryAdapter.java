package com.example.kmj_reco.utils;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GifticonHistoryAdapter extends ArrayAdapter<GIFTICONADST> {
    private Context context;
    private List gifticonList;
    private ListView gifticonListView;
    private List<GIFTICONDATA> gifticondataList = new ArrayList<>();
    class UserViewHolder {
        private TextView id;
        private TextView gifticonUsage;
        private TextView gifticonNum;
        private TextView gifticonExpirydate;
        private ImageView gifticonBarcode;
        private ImageView gifticon_Image;
        private TextView gifticon_Name;
        private TextView gifticon_Brand;
        private TextView gifticonBuyDate ;
        private TextView gifticondetail;}

    public GifticonHistoryAdapter(Context context, int resource, List<GIFTICONADST>gifticonlist, ListView gifticonListView){
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
            mView = LayoutInflater.from(getContext()).inflate(R.layout.item_gifticon_history, parent, false);

            viewHolder = new UserViewHolder();
            viewHolder.gifticon_Image = (ImageView) mView.findViewById(R.id.gifticonImage);
            viewHolder.gifticon_Brand = (TextView) mView.findViewById(R.id.gifticonBrand);
            viewHolder.gifticon_Name = (TextView) mView.findViewById(R.id.gifticonName);
            viewHolder.gifticonExpirydate= mView.findViewById(R.id.gifticonExpiration);

            mView.setTag(viewHolder);

            Status = "created";
        }else{
            viewHolder = (UserViewHolder) mView.getTag();

            Status = "reused";
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        GIFTICONADST gifticonadst = (GIFTICONADST) gifticonList.get(position);

        reference.child("GIFTICONDATA").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticondataList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONDATA gif = data.getValue(GIFTICONDATA.class);
                    if (gif.getgifticon_Num()==gifticonadst.getGifticonNum()){
                        gifticondataList.add(gif);}
                }
                GIFTICONDATA gifticondata = (GIFTICONDATA) gifticondataList.get(0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

                imageInsert(viewHolder.gifticon_Image, gifticondata.getgifticon_Image());
                viewHolder.gifticon_Brand.setText(gifticondata.getgifticon_Brand());
                viewHolder.gifticon_Name.setText(gifticondata.getgifticon_Name());
                try {
                    viewHolder.gifticonExpirydate.setText(sdf.format(dateFormat.parse(gifticonadst.getGifticonExpirydate().toString())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });

        return mView;
    }
    private static void imageInsert(ImageView load, String uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("GIFTICON");
        if (pathReference==null){
            Toast.makeText(load.getContext(), "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT);

        }else{
            StorageReference submitProfile;
            if(uri!=null && uri!=""){
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
