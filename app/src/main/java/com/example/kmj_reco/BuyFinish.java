package com.example.kmj_reco;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.kmj_reco.DTO.ALERT;
import com.example.kmj_reco.DTO.GIFTICONADST;
import com.example.kmj_reco.DTO.USER;
import com.example.kmj_reco.DTO.USER_GIFTICON;
import com.example.kmj_reco.utils.PushAlarm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BuyFinish extends DialogFragment implements View.OnClickListener{
    public BuyFinish() {
        // Required empty public constructor
    }
    public static BuyFinish getInstance(){
        BuyFinish e = new BuyFinish();
        return e;
    }

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mFirebaseAuth;
    private List<GIFTICONADST> gifticonADSTList = new ArrayList<>();
    private List<USER> userList = new ArrayList<>();
    private List<USER_GIFTICON> user_gifticonListconList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.buy_success,container);
//      intent로 데이터받아오기
        Intent intent = this.getActivity().getIntent();
        String name = intent.getStringExtra("name");
        int num = intent.getIntExtra("num", 0);
//       view가져오기
        TextView gifticon_buy_text_success = (TextView) v.findViewById(R.id.gifticon_buy_text_success);
        TextView gif_text = (TextView) v.findViewById(R.id.gif_text);

        final GIFTICONADST[] usegift = new GIFTICONADST[1];

        final int[] lastNum = {0};

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        reference.child("USER").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    USER gif = data.getValue(USER.class);
                    userList.add(gif);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });

        reference.child("USER_GIFTICON").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_gifticonListconList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    USER_GIFTICON gif = data.getValue(USER_GIFTICON.class);
                    user_gifticonListconList.add(gif);
                    lastNum[0] = getlastNum(user_gifticonListconList)+1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });

        int price = intent.getIntExtra("price",100);

        final int[] userPoint = new int[1];
        String uid = mFirebaseAuth.getCurrentUser().getUid();
        reference.child("USER").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer point = snapshot.child("user_point").getValue(Integer.class);
                userPoint[0] = point;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        gifticon_buy_text_success.setText("");

        reference.child("GIFTICONADST").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticonADSTList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);
                    if(gif!=null){
                        if (gif.getGifticonNum()==num && gif.isGifticonUsage()==false){
                            gifticonADSTList.add(gif);}
                    }
                }
                Log.i("바이피니시",gifticonADSTList+"이다");

                if (isStock()) {
                    if (userPointOK(price, userPoint[0])) {
                        gifticon_buy_text_success.setText(name + "\n를 구매하였습니다.");
                        gif_text.setText("마이 페이지에서 '나의 기프티콘'을 확인하세요 !");

                        //      푸시알림 발생
//                        user_setting에서 알림 받아오기

                        final int[] push = {0};
                        reference.child("USER_SETTING").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                push[0] = (Integer) snapshot.child("pushalarm").getValue(Integer.class);

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        if(push[0] ==1) {
                            PushAlarm p = new PushAlarm(v.getContext(), v);
                            NotificationManager mNotificationManager = p.createNotificationChannel();

                            mNotificationManager.notify(1, p.pushAlarm("기프티콘", name + "을/를 구매하셨습니다."));
                        }
                        List<ALERT> alertList = new ArrayList<>();
                        reference.child("ALERT").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot data : snapshot.getChildren()){
                                    ALERT alt = data.getValue(ALERT.class);
                                    alertList.add(alt);
                                }
                                int alert_num = getlastalertNum(alertList)+1;
                                ALERT alert = new ALERT(uid, alert_num, "기프티콘",name+"을/를 구매하셨습니다.",new Date());
                                reference.child("ALERT").child(String.valueOf(alert_num)).setValue(alert);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

//            포인트 차감
                        userPoint[0] = userPoint[0]- price;
//            차감 후 user 데이터베이스로 포인트 변경사항 보냄
                        reference.child("USER").child(uid).child("user_point").setValue(userPoint[0]);

                        for (GIFTICONADST data : gifticonADSTList) {
                            if (data.isGifticonUsage() == false) {
                                data.setGifticonUsage(true);

                                break;
                            }
                        }
                        if (gifticonADSTList.get(0) != null) {
                            usegift[0] = gifticonADSTList.get(0);
                            reference.child("GIFTICONADST").child(String.valueOf(usegift[0].getad_Num())).setValue(usegift[0]);

                            // user gifticon에 user_num 따라 추가하기
                            USER_GIFTICON user_gifticon = new USER_GIFTICON(uid,lastNum[0], usegift[0].getad_Num());
                            reference.child("USER_GIFTICON").child(String.valueOf(lastNum[0])).setValue(user_gifticon);

                        }
                    } else {
                        gifticon_buy_text_success.setText(name + " 구매 실패. \n 포인트가 부족합니다.");
                        gif_text.setText("");
                    }

                }else{
                    gifticon_buy_text_success.setText("재고가 부족합니다. 나중에 다시 시도해주세요.");
                    gif_text.setText("");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });
        ImageButton exit = (ImageButton) v.findViewById(R.id.btn_x);
        exit.setOnClickListener(this);
        return v;
    }

    public boolean isStock(){
        if (gifticonADSTList.size()!=0){
            return true;
        }else return false;
    }
    public boolean userPointOK(int price, int userPoint){
        if (price<=userPoint){
            return true;
        }
        else {return false;}
    }

    @Override
    public void onClick(View view) {
        dismiss();

    }

    int getlastalertNum(List<ALERT> alertList){
        int k=0;
        int i=0;
        for (ALERT a : alertList){
            i=a.getalert_Num();
            if(i>=k){
                k=i;
            }
        }
        return k;
    }

    int getlastNum(List<USER_GIFTICON> user_gifticonListconList){
        int k = 0;
        int i = 0;
        for (USER_GIFTICON data : user_gifticonListconList){
            k=data.getuser_gifticon_num();
            if(k>=i){i=k;}
        }
        return k;
    }
}