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
import com.example.kmj_reco.DTO.NOTICE;
import com.example.kmj_reco.Event;
import com.example.kmj_reco.Notice;
import com.example.kmj_reco.NoticeDetail;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter2 extends ArrayAdapter<NOTICE> {
    private DatabaseReference databaseReference; // 데이터베이스 선언

    private Context context2;
    private List notice2list;
    private ListView notice2ListView;

    // xml에서 받아올 변수 담은 클래스 정의
    class UserViewHolder {
        private TextView notice_num;
        private TextView notice_title;
        private TextView notice_detail;
        private TextView notice_date;
    }

    // Adapter content
    public NoticeAdapter2(Context context2, int resource2, List<NOTICE> notice2list, ListView notice2ListView) {
        super(context2, resource2, notice2list);
        this.context2 = context2;
        this.notice2list = notice2list;
        this.notice2ListView = notice2ListView;
    }

    @NonNull
    @Override
    public View getView(int position2, @Nullable View convertView2, @NonNull ViewGroup parent) {
        UserViewHolder viewHolder2;
        String Status2;
        View m2View = convertView2;

        // 뷰를 받아오지 않았을 경우
        // xml에서 요소를 받아온다.
        if (m2View == null) {
            m2View = LayoutInflater.from(getContext()).inflate(R.layout.item_notice2, parent, false);

            // 뷰 변수 선언
            viewHolder2 = new UserViewHolder();
            viewHolder2.notice_title = (TextView) m2View.findViewById(R.id.notice2title);
            viewHolder2.notice_detail = (TextView) m2View.findViewById(R.id.notice2detail);

            m2View.setTag(viewHolder2);

            Status2 = "created";
        } else {
            viewHolder2 = (UserViewHolder) m2View.getTag();

            Status2 = "reused";
        }

        NOTICE notice = (NOTICE) notice2list.get(position2); // 리스트에 추가될 공지 개별 정보 받아오기

        // DB 에서 NOTICE 데이터 가져오기
        databaseReference= FirebaseDatabase.getInstance().getReference(); // DB 설정
        databaseReference.child("NOTICE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notice2list.clear(); // 리스트 초기화

                for (DataSnapshot data: snapshot.getChildren()){
                        NOTICE notice = data.getValue(NOTICE.class);
                        notice2list.add(notice);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 뷰 데이터 설정
        viewHolder2.notice_title.setText(notice.getNotice_title());
        viewHolder2.notice_detail.setText(notice.getNotice_detail());

        return m2View;
    } private Context context;
}