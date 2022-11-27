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

    //데이터베이스 정의
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;

    // xml에서 받아올 변수 담은 클래스 정의
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
        View mView = convertView;

        // 뷰를 받아오지 않았을 경우 받아온다.
        // xml에서 요소를 받아온다.
        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.item_gifticon, parent, false);

            viewHolder = new UserViewHolder();
            viewHolder.gifticon_Image = (ImageView) mView.findViewById(R.id.gifticonImage);
            viewHolder.gifticon_Brand = (TextView) mView.findViewById(R.id.gifticonBrand);
            viewHolder.gifticon_Name = (TextView) mView.findViewById(R.id.gifticonName);
            viewHolder.gifticon_Price = (TextView) mView.findViewById(R.id.gifticonPrice);

            mView.setTag(viewHolder);

        }else{
            viewHolder = (UserViewHolder) mView.getTag();
        }
        TextView gifticonStock= (TextView) mView.findViewById(R.id.gifticonStock);

        // Coupon 목록에 들어갈 개별 기프티콘의 정보 가져오기
        GIFTICONDATA gifticondata = (GIFTICONDATA) gifticonList.get(position);

        // 재고 확인
        // db에서 개별 기프티콘의 재고 개수를 세고
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
                    gifticonStock.setVisibility(View.VISIBLE);
                    // 개별 기프티콘의 재고 개수가 0이라면 재고없음 표시
                    gifticonStock.setText("재고없음");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // 개별 기프티콘의 정보를 각 목록 item에 표시하기
        imageInsert(viewHolder.gifticon_Image, gifticondata.getgifticon_Image());
        viewHolder.gifticon_Brand.setText(gifticondata.getgifticon_Brand());
        viewHolder.gifticon_Name.setText(gifticondata.getgifticon_Name());
        viewHolder.gifticon_Price.setText(Integer.toString(gifticondata.getgifticon_Price())+" 원");


        // 목록 item의 구매 버튼 클릭 이벤트
        Button button = (Button) mView.findViewById(R.id.gifticonBuyButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 해당 기프티콘 상세화면으로 이동
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
    }

    private Context context;

    // 이미지 등록 함수: url을 받으면 db에서 이미지를 가져와 등록하는 함수
    public static void imageInsert(ImageView load, String uri){
        // db
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("GIFTICON");

        if (pathReference==null){
            // 경로에 사진이 없을 경우
            Toast.makeText(load.getContext(), "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
        }else{
            StorageReference submitProfile;
            if(uri!=null && uri!="" && uri!=" "){
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