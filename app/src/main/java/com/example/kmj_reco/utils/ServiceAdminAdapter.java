package com.example.kmj_reco.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kmj_reco.DTO.ServiceAccount;
import com.example.kmj_reco.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ServiceAdminAdapter extends ArrayAdapter<ServiceAccount> {
    private Context context = null;
    private final List ServiceAccountList;
    private final ListView ServiceAdminListView;

    // 데이터베이스 선언
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    // xml에서 받아올 변수 담은 클래스 정의
    class ViewHolder {
        private TextView service_title;
        private TextView service_contents;
        private TextView service_num;
        private TextView service_publisher;
        private TextView service_date;
    }

    // Adapter content
    public ServiceAdminAdapter(Context context, int resource, List<ServiceAccount> ServiceAccountList, ListView ServiceAdminListView){
        super(context,resource,ServiceAccountList);
        this.context = context;
        this.ServiceAdminListView=ServiceAdminListView;
        this.ServiceAccountList=ServiceAccountList;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ServiceAdminAdapter.ViewHolder viewHolder;
        String Status;
        View mView = convertView;

        // 뷰를 받아오지 않았을 경우
        // xml에서 요소를 받아온다.
        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.item_service_admin, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.service_title = (TextView) mView.findViewById(R.id.et_title);
            viewHolder.service_contents = (TextView) mView.findViewById(R.id.et_contents);

            mView.setTag(viewHolder);

            Status = "created";
        } else {
            viewHolder = (ViewHolder) mView.getTag();

            Status = "reused";
        }

        // 리스트에 추가될 ServiceAccount 개별 정보 받아오기
        ServiceAccount serviceAccount = (ServiceAccount) ServiceAccountList.get(position);

        // 뷰 데이터 설정
        viewHolder.service_title.setText("" + serviceAccount.getService_Title());
        viewHolder.service_contents.setText(""+ serviceAccount.getService_Contents());

        // DB 에서 ServiceAccount 데이터 가져오기
        ImageButton btn_service_delete = (ImageButton) mView.findViewById(R.id.btn_service_delete);
        btn_service_delete.setFocusable(false);
        btn_service_delete.setClickable(false);
        View finalMView = mView;
        databaseReference = FirebaseDatabase.getInstance().getReference(); // DB 설정
        databaseReference.child("ServiceAccount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    ServiceAccount SA = data.getValue(ServiceAccount.class);
                    if(SA.getService_Num()== SA.getService_Num()){} // DB와 리스트의 데이터가 같을 경우만 불러옴

                    // 버튼 터치 시 해당 문의 삭제
                    btn_service_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(finalMView.getRootView().getContext());
                            builder.setTitle("삭제").setMessage("정말로 " + serviceAccount.getService_Num()+ " 을 삭제하시겠습니까?");
                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // DB 내 ServiceAccount 테이블에서 INDEX 받아와 삭제
                                    databaseReference.child("ServiceAccount").child(String.valueOf(serviceAccount.getService_Num())).removeValue();
                                    Toast.makeText(context, serviceAccount.getService_Num() +" 삭제 완료",Toast.LENGTH_SHORT).show();
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return mView;
    }
}