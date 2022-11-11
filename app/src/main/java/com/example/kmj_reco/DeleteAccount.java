package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.kmj_reco.DTO.USER_SETTING;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DeleteAccount extends DialogFragment implements View.OnClickListener {


    public DeleteAccount() {
    }
    public static DeleteAccount getInstance(){
        DeleteAccount e = new DeleteAccount();
        return e;
    }

    public static DeleteAccount newInstance(String param1, String param2) {
        DeleteAccount fragment = new DeleteAccount();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.delete_account, container, false);
        String uid = FirebaseAuth.getInstance().getUid();
        Button btn_y = (Button) v.findViewById(R.id.btn_y);
        Button btn_n = (Button) v.findViewById(R.id.btn_n);
        Intent intent = new Intent(this.getContext(), DeleteAccountFinish.class);

        btn_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원삭제
                deleteId();
                startActivity(intent);


            }
        });
        btn_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        ImageButton exit = (ImageButton)v.findViewById(R.id.btn_x);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onClick(View view) {
        dismiss();

    }

    private void deleteId(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getUid();
        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(this.getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseAuth.getInstance().signOut();
                //데이터베이스의 USER 제거
                reference.child("USER").child(uid).removeValue();
                //데이터베이스에 USER_SETTING이 있을경우 제거
                reference.child("USER_SETTING").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for( DataSnapshot s : snapshot.getChildren()){
                        USER_SETTING set = s.getValue(USER_SETTING.class);
                        if (set.getUser_num().equals(uid)){
                            reference.child("USER_SETTING").child(uid).removeValue();
                            break;
                        }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Log.d("USERDELETE","탈퇴 완료");
                return;
            }
        });
    }
}