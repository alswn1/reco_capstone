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
    private FirebaseDatabase database;
    private DatabaseReference reference;
    class UserViewHolder {
        private TextView id;
        private TextView gifticonUsage;
        private TextView gifticonNum;
        private TextView gifticonExpirydate;
        private ImageView gifticonBarcode;
        private TextView usedate ;
        private TextView buydate ;
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
        String Status;
        View mView = convertView;

        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.item_coupon_admin_detail, parent, false);

            viewHolder = new UserViewHolder();
            viewHolder.gifticonBarcode = (ImageView) mView.findViewById(R.id.gifticonImage);
            viewHolder.id  = (TextView) mView.findViewById(R.id.gifticonName);
            viewHolder.gifticonExpirydate  = (TextView) mView.findViewById(R.id.gifticonExpiration);
            viewHolder.gifticonUsage  = (TextView) mView.findViewById(R.id.gifticon_usage);

            mView.setTag(viewHolder);

            Status = "created";
        }else{
            viewHolder = (UserViewHolder) mView.getTag();

            Status = "reused";
        }

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        GIFTICONADST gifticonadst = (GIFTICONADST) gifticonDetailList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        GifticonAdapter.imageInsert(viewHolder.gifticonBarcode, gifticonadst.getGifticonBarcode()
        );
        viewHolder.id.setText("번호 : "+gifticonadst.getad_Num());
        if(gifticonadst.getGifticonExpirydate()==null){
            viewHolder.gifticonExpirydate.setText("기록 없음");
        }else{
            try {
                viewHolder.gifticonExpirydate.setText(sdf.format(dateFormat.parse(gifticonadst.getGifticonExpirydate().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(gifticonadst.isGifticonUsage()){
            viewHolder.gifticonUsage.setText("사용함");
        }else{
            viewHolder.gifticonUsage.setText("사용안함");
        }

        Button btn_d = (Button) mView.findViewById(R.id.gifticonADDeleteBtn);
        btn_d.setFocusable(false);
        btn_d.setClickable(false);
        View finalMView = mView;
        btn_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(finalMView.getRootView().getContext());
                builder.setTitle("삭제").setMessage("정말로" + gifticonadst.getad_Num()+ " 번을 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reference.child("GIFTICONADST").child(String.valueOf(gifticonadst.getad_Num())).removeValue();
                        Toast.makeText(context, gifticonadst.getad_Num()+" 번 삭제 완료",Toast.LENGTH_SHORT);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return mView;
    }

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
