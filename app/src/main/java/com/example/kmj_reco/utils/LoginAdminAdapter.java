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
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Object USER;

    class UserViewHolder {
        private TextView Name;
        private TextView Email;
        private TextView password;
        private TextView idToken;
    }

    public LoginAdminAdapter(Context context, int resource, List<USER>UserAccountList, ListView loginAdminListView){
        super(context,resource,UserAccountList);
        this.context = context;
        this.UserAccountList=UserAccountList;
        this.loginAdminListView=loginAdminListView;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserViewHolder viewHolder;
        String Status;
        View mView = convertView;

        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_login, parent, false);

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

        USER user = (USER) UserAccountList.get(position);
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
                    if(user.getUser_name()== UA.getUser_name()){}

                    btn_d.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(finalMView.getRootView().getContext());
                            builder.setTitle("삭제").setMessage("정말로 " + user.getUser_name().toString()+ " 을 삭제하시겠습니까?");
                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    databaseReference.child("USER").child(String.valueOf(user.getIdToken())).removeValue();

                                    Toast.makeText(context, user.getUser_name().toString() +" 삭제 완료",Toast.LENGTH_SHORT).show();
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
        return mView;
    }
}