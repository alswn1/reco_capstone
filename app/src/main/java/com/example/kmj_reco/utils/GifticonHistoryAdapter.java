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

    // xml에서 받아올 변수 담은 클래스 정의
    class UserViewHolder {
        private TextView gifticonExpirydate;
        private ImageView gifticon_Image;
        private TextView gifticon_Name;
        private TextView gifticon_Brand;}

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
        View mView = convertView;

        // 뷰를 받아오지 않았을 경우 받아온다.
        // xml에서 요소를 받아온다.
        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.item_gifticon_history, parent, false);

            viewHolder = new UserViewHolder();
            viewHolder.gifticon_Image = (ImageView) mView.findViewById(R.id.gifticonImage);
            viewHolder.gifticon_Brand = (TextView) mView.findViewById(R.id.gifticonBrand);
            viewHolder.gifticon_Name = (TextView) mView.findViewById(R.id.gifticonName);
            viewHolder.gifticonExpirydate= mView.findViewById(R.id.gifticonExpiration);

            mView.setTag(viewHolder);

        }else{
            viewHolder = (UserViewHolder) mView.getTag();
        }

        // db
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        // Couponhistory 목록에 들어갈 개별 기프티콘의 정보 가져오기
        GIFTICONADST gifticonadst = (GIFTICONADST) gifticonList.get(position);


        // 목록에 표시할 기프티콘 데이터 받아오기
        reference.child("GIFTICONDATA").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticondataList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONDATA gif = data.getValue(GIFTICONDATA.class);

                    // 기프티콘 식별번호에 따른 데이터만 가져오기
                    if (gif.getgifticon_Num()==gifticonadst.getGifticonNum()){
                        gifticondataList.add(gif);}
                }
                GIFTICONDATA gifticondata = (GIFTICONDATA) gifticondataList.get(0);

                // 날짜 형식 정의
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

                // 개별 기프티콘의 정보를 각 목록 item에 표시하기
                // 개별 기프티콘 재고 정보를 표시하기
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

    // 이미지 등록 함수: url을 받으면 db에서 이미지를 가져와 등록하는 함수
    private static void imageInsert(ImageView load, String uri){
        // db
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("GIFTICON");
        if (pathReference==null){
            // 경로에 사진이 없을 경우
            Toast.makeText(load.getContext(), "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT);

        }else{
            StorageReference submitProfile;
            if(uri!=null && uri!=""){
                // url이 존재할 경우
                submitProfile = storageReference.child(uri);}
            else {
                // url이 존재하지 않을 경우
                // 더미 이미지 url로 대체한다.
                submitProfile = storageReference.child("gifticon_dummy.png");}
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // db에서 이미지 다운로드에 성공한 경우
                    // ImageView에 이미지를 설정한다.
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
