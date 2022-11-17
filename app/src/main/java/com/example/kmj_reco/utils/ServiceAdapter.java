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
    private DatabaseReference databaseReference;
    private Context context;
    private List serviceAccountList;
    private ListView ServiceListView;
    private FirebaseDatabase database;

    class UserViewHolder {
        private TextView service_num;
        private TextView service_title;
        private TextView service_contents;
        private TextView service_date;
        private TextView service_publisher;
    }

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

        ServiceAccount service = (ServiceAccount) serviceAccountList.get(position);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ServiceAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceAccountList.clear();
                for (DataSnapshot data: snapshot.getChildren()){
                    ServiceAccount service = data.getValue(ServiceAccount.class);
                    serviceAccountList.add(service);}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        viewHolder.service_title.setText(service.getService_Title());
        viewHolder.service_contents.setText(service.getService_Contents());

        return m2View;
    }
}