package com.example.kmj_reco.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.kmj_reco.CouponAdminDetail;
import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.DTO.GIFTICONDATA;
import com.example.kmj_reco.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GifticonAdminAdapter extends ArrayAdapter<GIFTICONDATA> {
    private Context context;
    private List gifticonList;
    private ListView gifticonListView;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<GIFTICONADST> gifticonadstList = new ArrayList<>();
    class UserViewHolder {
        private TextView gifticon_id;
        private ImageView gifticon_Image;
        private TextView gifticon_Price;
        private TextView gifticon_Name;
        private TextView gifticon_Brand;
    }

    public GifticonAdminAdapter(Context context, int resource, List<GIFTICONDATA>gifticonlist, ListView gifticonListView){
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
            mView = LayoutInflater.from(getContext()).inflate(R.layout.item_coupon_admin, parent, false);

            viewHolder = new UserViewHolder();
            viewHolder.gifticon_Image = (ImageView) mView.findViewById(R.id.gifticonImage);
            viewHolder.gifticon_Name = (TextView) mView.findViewById(R.id.gifticonName);

            mView.setTag(viewHolder);

            Status = "created";
        }else{
            viewHolder = (UserViewHolder) mView.getTag();

            Status = "reused";
        }
        TextView gifticonStock = (TextView) mView.findViewById(R.id.gifticonStock);
        gifticonStock.setText("재고-");
        GIFTICONDATA gifticondata = (GIFTICONDATA) gifticonList.get(position);

        viewHolder.gifticon_Name.setText(""+ gifticondata.getgifticon_Name());
        GifticonAdapter.imageInsert(viewHolder.gifticon_Image, gifticondata.getgifticon_Image());

        Button btn_d = (Button) mView.findViewById(R.id.adminDeleteButton);
        btn_d.setFocusable(false);
        btn_d.setClickable(false);
        View finalMView = mView;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("GIFTICONDATA").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    GIFTICONDATA gif = data.getValue(GIFTICONDATA.class);
                    if(gifticondata.getgifticon_Num()== gif.getgifticon_Num()){}
                    btn_d.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(finalMView.getRootView().getContext());
                            builder.setTitle("삭제").setMessage("정말로 " + gifticondata.getgifticon_Name().toString()+ " 을 삭제하시겠습니까?");
                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    databaseReference.child("GIFTICONDATA").child(String.valueOf(gifticondata.getgifticon_Num())).removeValue();

                                    Toast.makeText(context, gifticondata.getgifticon_Name().toString()+" 삭제 완료",Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("GIFTICONADST").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticonadstList.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);
                    if(gif.getGifticonNum() == gifticondata.getgifticon_Num() && gif.isGifticonUsage()==false){
                        gifticonadstList.add(gif);
                    }
                }
                if(gifticonadstList.size()>0){
                    gifticonStock.setText("재고 :"+gifticonadstList.size()+"개");
                }else {gifticonStock.setText("재고 없음");}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button btn_ac = (Button) mView.findViewById(R.id.adminModiButton);
        btn_ac.setFocusable(false);
        btn_ac.setClickable(false);

        btn_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                수정하러가기
                Intent intent = new Intent(context, CouponAdminDetail.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("num",gifticondata.getgifticon_Num());
                context.startActivity(intent);
            }
        });
        return mView;
    }
}
