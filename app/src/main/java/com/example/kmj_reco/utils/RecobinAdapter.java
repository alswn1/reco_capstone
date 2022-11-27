package com.example.kmj_reco.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kmj_reco.DTO.RECOBIN;
import com.example.kmj_reco.R;
import com.example.kmj_reco.RecobinAdminEdit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecobinAdapter extends ArrayAdapter<RECOBIN> {
    private Context context;
    private List recobinList;
    private ListView recobinListView;

    // 데이터베이스 선언
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    // xml에서 받아올 변수 담은 클래스 정의
    class ViewHolder {
        private TextView recobin_num;
        private TextView recobin_roadname;
        private TextView recobin_address;
        private TextView recobin_fulladdress;
        private TextView recobin_locate;
        private TextView recobin_latitude;
        private TextView recobin_longitude;
    }

    // Adapter content
    public RecobinAdapter(Context context, int resource, List<RECOBIN>recobinList, ListView recobinListView){
        super(context,resource,recobinList);
        this.context = context;
        this.recobinList=recobinList;
        this.recobinListView=recobinListView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        String Status;
        View mView = convertView;

        // 뷰를 받아오지 않았을 경우
        // xml에서 요소를 받아온다.
        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.item_recobin_admin, parent, false);

            // 뷰 변수 선언
            viewHolder = new ViewHolder();
            viewHolder.recobin_num = (TextView) mView.findViewById(R.id.recobin_index);
            viewHolder.recobin_fulladdress = (TextView) mView.findViewById(R.id.recobin_fulladdress);

            mView.setTag(viewHolder);

            Status = "created";
        }else{
            viewHolder = (ViewHolder) mView.getTag();

            Status = "reused";
        }

        RECOBIN recobin = (RECOBIN) recobinList.get(position); // 리스트에 추가될 레코빈 개별 정보 받아오기

        // 뷰 데이터 설정
        viewHolder.recobin_num.setText("" + recobin.getRecobin_num());
        viewHolder.recobin_fulladdress.setText("" + recobin.getRecobin_fulladdress());

        // DB 에서 RECOBIN 데이터 가져오기
        ImageButton btn_recobin_delete = (ImageButton) mView.findViewById(R.id.btn_recobin_delete);
        btn_recobin_delete.setFocusable(false);
        btn_recobin_delete.setClickable(false);
        View finalMView = mView;
        databaseReference = FirebaseDatabase.getInstance().getReference(); // DB 설정
        databaseReference.child("RECOBIN").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    RECOBIN recoBin = data.getValue(RECOBIN.class);

                    if(recobin.getRecobin_num() == recoBin.getRecobin_num()){}  // DB와 리스트의 데이터가 같을 경우만 불러옴

                    // 버튼 터치 시 레코빈 삭제
                    btn_recobin_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(finalMView.getRootView().getContext());
                            builder.setTitle("삭제").setMessage("정말로 " + recobin.getRecobin_num()+ "번째 레코빈을 삭제하시겠습니까?");
                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // DB 내 RECOBIN에서 레코빈 INDEX 받아와 삭제
                                    databaseReference.child("RECOBIN").child(String.valueOf(recobin.getRecobin_num())).removeValue();
                                    Toast.makeText(context, recobin.getRecobin_num()+"번째 레코빈 삭제 완료",Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            // 팝업 생성 및 show
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 수정 버튼 터치 시 레코빈 정보 수정 화면으로 이동
        ImageButton btn_recobin_admin_detail = (ImageButton) mView.findViewById(R.id.btn_recobin_admin_detail);
        btn_recobin_admin_detail.setFocusable(false);
        btn_recobin_admin_detail.setClickable(false);
        btn_recobin_admin_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 레코빈 정보 수정 화면에 데이터 전송 및 이동
                Intent intent = new Intent(context, RecobinAdminEdit.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("num", recobin.getRecobin_num());
                intent.putExtra("indexNum", position);
                context.startActivity(intent);
            }
        });
        return mView;
    }
}