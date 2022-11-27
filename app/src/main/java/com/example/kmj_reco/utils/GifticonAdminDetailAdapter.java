package com.example.kmj_reco.utils;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;

import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class GifticonAdminDetailAdapter extends ArrayAdapter<GIFTICONADST> {
    private Context context;
    private List gifticonDetailList;
    private ListView gifticonDetailListView;

    //데이터베이스 정의
    private FirebaseDatabase database;
    private DatabaseReference reference;

    // xml에서 받아올 변수 담은 클래스 정의
    class UserViewHolder {
        private TextView id;
        private TextView gifticonUsage;
        private TextView gifticonExpirydate;
        private ImageView gifticonBarcode;
    }

    public GifticonAdminDetailAdapter(Context context, int resource, List<GIFTICONADST>gifticonDetailList, ListView gifticonDetailListView){
        super(context,resource,gifticonDetailList);
        this.context = context;
        this.gifticonDetailList=gifticonDetailList;
        this.gifticonDetailListView=gifticonDetailListView;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserViewHolder viewHolder;
        View mView = convertView;

        // 뷰를 받아오지 않았을 경우 받아온다.
        // xml에서 요소를 받아온다.
        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.item_coupon_admin_detail, parent, false);

            viewHolder = new UserViewHolder();
            viewHolder.gifticonBarcode = (ImageView) mView.findViewById(R.id.gifticonImage);
            viewHolder.id  = (TextView) mView.findViewById(R.id.gifticonName);
            viewHolder.gifticonExpirydate  = (TextView) mView.findViewById(R.id.gifticonExpiration);
            viewHolder.gifticonUsage  = (TextView) mView.findViewById(R.id.gifticon_usage);

            mView.setTag(viewHolder);

        }else{
            viewHolder = (UserViewHolder) mView.getTag();

        }

        //db
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // CouponAdminDetail의 재고 목록에 들어갈 개별 기프티콘 바코드의 정보 가져오기
        GIFTICONADST gifticonadst = (GIFTICONADST) gifticonDetailList.get(position);
        // 기본 날짜 포맷 정의
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        // 개별 바코드 데이터 표시
        GifticonAdapter.imageInsert(viewHolder.gifticonBarcode, gifticonadst.getGifticonBarcode()
        );
        viewHolder.id.setText("번호 : "+gifticonadst.getad_Num());
        // 만료 날짜가 null일 경우 기록 없음 표시
        if(gifticonadst.getGifticonExpirydate()==null){
            viewHolder.gifticonExpirydate.setText("기록 없음");
        }else{
            try {
                // null이 아닐 경우 만료 날짜 표시
                viewHolder.gifticonExpirydate.setText(sdf.format(dateFormat.parse(gifticonadst.getGifticonExpirydate().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // 바코드를 사용자가 구매했을 경우 사용함 표시
        if(gifticonadst.isGifticonUsage()){
            viewHolder.gifticonUsage.setText("사용함");
        }else{
            // 구매하지 않았을 경우 사용안함 표시
            viewHolder.gifticonUsage.setText("사용안함");
        }

        // 목록 item 삭제 버튼
        Button btn_d = (Button) mView.findViewById(R.id.gifticonADDeleteBtn);
        // 목록 바깥 버튼 클릭이 되지 않는 오류 해결
        btn_d.setFocusable(false);
        btn_d.setClickable(false);
        View finalMView = mView;
        // 목록 item 삭제 버튼 클릭 이벤트
        // 삭제 확인 팝업창을 띄우고
        btn_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(finalMView.getRootView().getContext());
                builder.setTitle("삭제").setMessage("정말로" + gifticonadst.getad_Num()+ " 번을 삭제하시겠습니까?");
                // 예를 클릭할 경우 db에서 해당 기프티콘 재고 데이터를 삭제한다.
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reference.child("GIFTICONADST").child(String.valueOf(gifticonadst.getad_Num())).removeValue();
                        Toast.makeText(context, gifticonadst.getad_Num()+" 번 삭제 완료",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return mView;
    }


    // 기프티콘 재고 식별번호 중 마지막 번호 찾는 함수
    public static int lastNum(List<GIFTICONADST> GIFTICONADST){
        int k =0;
        int i;
        for(GIFTICONADST data:GIFTICONADST){
            i=data.getad_Num();
            if(k<=i){
                k=i;
            }
        }
        return k;
    }
}
