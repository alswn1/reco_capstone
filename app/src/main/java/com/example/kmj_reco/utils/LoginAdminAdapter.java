package com.example.kmj_reco.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kmj_reco.DTO.USER;
import com.example.kmj_reco.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LoginAdminAdapter extends ArrayAdapter<USER> {
    private final Context context;
    private final List UserAccountList;
    private final ListView loginAdminListView;

    // 데이터베이스 선언
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private Object USER;

    // xml에서 받아올 변수 담은 클래스 정의
    class UserViewHolder {
        private TextView Name;
        private TextView Email;
        private TextView password;
        private TextView idToken;
    }

    // Adapter content
    public LoginAdminAdapter(Context context, int resource, List<USER>UserAccountList, ListView loginAdminListView){
        super(context,resource,UserAccountList);
        this.context = context;
        this.UserAccountList = UserAccountList;
        this.loginAdminListView = loginAdminListView;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserViewHolder viewHolder;
        String Status;
        View mView = convertView;

        // 뷰를 받아오지 않았을 경우
        // xml에서 요소를 받아온다.
        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_login, parent, false);

            // 뷰 변수 선언
            viewHolder = new UserViewHolder();
            viewHolder.Name = (TextView) mView.findViewById(R.id.et_name);
            viewHolder.Email = (TextView) mView.findViewById(R.id.et_email);
            viewHolder.password = (TextView) mView.findViewById(R.id.et_pass);

            mView.setTag(viewHolder);

            Status = "created";
        } else {
            viewHolder = (UserViewHolder) mView.getTag();

            Status = "reused";
        }

        USER user = (USER) UserAccountList.get(position); // 리스트에 추가될 유저별 정보 받아오기

        // 뷰 데이터 설정
        viewHolder.Name.setText(""+ user.getUser_name());
        viewHolder.Email.setText(""+ user.getUser_email());
        viewHolder.password.setText(""+ user.getUser_pwd());

        @SuppressLint("WrongViewCast") Button btn_d = (Button) mView.findViewById(R.id.login_delete);
        btn_d.setFocusable(false);
        btn_d.setClickable(false);
        View finalMView = mView;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("USER").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    USER UA = data.getValue(USER.class);

                    if(user.getUser_name() == UA.getUser_name()){} // DB와 리스트의 데이터가 같을 경우만 불러옴

                    // 버튼 터치 시 사용자 삭제
                    btn_d.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(finalMView.getRootView().getContext());
                            builder.setTitle("삭제").setMessage("정말로 " + user.getUser_name().toString()+ " 을 삭제하시겠습니까?");
                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // DB 내 USER에서 사용자 고유 토큰을 받아와 삭제
                                    databaseReference.child("USER").child(String.valueOf(user.getIdToken())).removeValue();
                                    Toast.makeText(context, user.getUser_name().toString() + " 삭제 완료", Toast.LENGTH_SHORT).show();
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