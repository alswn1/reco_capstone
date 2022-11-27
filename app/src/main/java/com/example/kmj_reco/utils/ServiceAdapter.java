package com.example.kmj_reco.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kmj_reco.DTO.ServiceAccount;
import com.example.kmj_reco.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ServiceAdapter extends ArrayAdapter<ServiceAccount> {
    // 데이터베이스 선
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;

    private Context context;
    private List serviceAccountList;
    private ListView ServiceListView;

    // xml에서 받아올 변수 담은 클래스 정의
    class UserViewHolder {
        private TextView service_num;
        private TextView service_title;
        private TextView service_contents;
        private TextView service_date;
        private TextView service_publisher;
    }

    // Adapter content
    public ServiceAdapter(Context context, int resource, List<ServiceAccount> serviceAccountList, ListView ServiceListView) {
        super(context, resource, serviceAccountList);
        this.context = context;
        this.serviceAccountList = serviceAccountList;
        this.ServiceListView = ServiceListView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserViewHolder viewHolder;
        String Status;
        View m2View = convertView;

        // 뷰를 받아오지 않았을 경우
        // xml에서 요소를 받아온다.
        if (m2View == null) {
            m2View = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_service, parent, false);

            viewHolder = new UserViewHolder();
            viewHolder.service_title = (TextView) m2View.findViewById(R.id.et_title);
            viewHolder.service_contents = (TextView) m2View.findViewById(R.id.et_contents);

            m2View.setTag(viewHolder);

            Status = "created";
        } else {
            viewHolder = (UserViewHolder) m2View.getTag();

            Status = "reused";
        }

        // 리스트에 추가될 ServiceAccount 개별 정보 받아오기
        ServiceAccount service = (ServiceAccount) serviceAccountList.get(position);

        // DB 에서 ServiceAccount 데이터 가져오기
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ServiceAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceAccountList.clear(); // 리스트 초기화

                for (DataSnapshot data: snapshot.getChildren()){
                    ServiceAccount service = data.getValue(ServiceAccount.class);
                    serviceAccountList.add(service);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 뷰 데이터 설정
        viewHolder.service_title.setText(service.getService_Title());
        viewHolder.service_contents.setText(service.getService_Contents());

        return m2View;
    }
}