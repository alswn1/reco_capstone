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
    private List<GIFTICONADST> gifticonadstList = new ArrayList<>();

    // 데이터베이스 정의
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    // xml에서 받아올 변수 담은 클래스 정의
    class UserViewHolder {
        private ImageView gifticon_Image;
        private TextView gifticon_Name;
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
        View mView = convertView;

        // 뷰를 받아오지 않았을 경우 받아온다.
        // xml에서 요소를 받아온다.
        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.item_coupon_admin, parent, false);

            viewHolder = new UserViewHolder();
            viewHolder.gifticon_Image = (ImageView) mView.findViewById(R.id.gifticonImage);
            viewHolder.gifticon_Name = (TextView) mView.findViewById(R.id.gifticonName);

            mView.setTag(viewHolder);

        }else{
            viewHolder = (UserViewHolder) mView.getTag();
        }
        TextView gifticonStock = (TextView) mView.findViewById(R.id.gifticonStock);

        // 재고 텍스트 기본 설정
        gifticonStock.setText("재고-");

        // CouponAdmin 목록에 들어갈 개별 기프티콘의 정보 가져오기
        GIFTICONDATA gifticondata = (GIFTICONDATA) gifticonList.get(position);

        // 개별 기프티콘의 정보를 각 목록 item에 표시하기
        viewHolder.gifticon_Name.setText(""+ gifticondata.getgifticon_Name());
        GifticonAdapter.imageInsert(viewHolder.gifticon_Image, gifticondata.getgifticon_Image());

        // 목록 item 삭제 버튼
        Button btn_d = (Button) mView.findViewById(R.id.adminDeleteButton);
        // 목록 바깥 버튼 클릭이 되지 않는 오류 해결
        btn_d.setFocusable(false);
        btn_d.setClickable(false);
        View finalMView = mView;

        // 목록 item 삭제 버튼 클릭 이벤트
        // 삭제 확인 팝업창을 띄우고
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

                            // 예를 클릭할 경우 db에서 해당 기프티콘 데이터를 삭제한다.
                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    databaseReference.child("GIFTICONDATA").child(String.valueOf(gifticondata.getgifticon_Num())).removeValue();
                                    // 해당 기프티콘의 재고도 함께 삭제한다.
                                    deleteAdst(gifticondata.getgifticon_Num());
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

        // 재고 확인
        // db에서 개별 기프티콘의 재고 개수를 세고
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
                    // 개별 기프티콘의 재고 개수가 0이라면 재고없음 표시.
                    gifticonStock.setText("재고 :"+gifticonadstList.size()+"개");
                }else {gifticonStock.setText("재고 없음");}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 목록 item 수정 버튼
        Button btn_ac = (Button) mView.findViewById(R.id.adminModiButton);
        // 목록 바깥 버튼 클릭이 되지 않는 오류 해결
        btn_ac.setFocusable(false);
        btn_ac.setClickable(false);

        btn_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 데이터 수정이 가능한 개별 기프티콘 상세화면으로 이동
                Intent intent = new Intent(context, CouponAdminDetail.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("num",gifticondata.getgifticon_Num());
                context.startActivity(intent);
            }
        });
        return mView;
    }

    // 개별 기프티콘 재고 삭제 함수 : 기프티콘 식별번호를 받아 해당 기프티콘의 재고를 전부 삭제
    private void deleteAdst(int gifticonnum){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("GIFTICONADST").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);
                    // 기프티콘 식별번호가 같을 경우
                    if (gif.getGifticonNum()==gifticonnum){
                        // 삭제
                        databaseReference.child("GIFTICONADST").child(String.valueOf(gif.getad_Num())).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
