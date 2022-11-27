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

    // db
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mFirebaseAuth;

    private List<GIFTICONADST> gifticonADSTList = new ArrayList<>();
    private List<USER_GIFTICON> user_gifticonListconList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.buy_success,container);
        // 이전 Activity에서 전달한 기프티콘 이름, 일련번호, 가격 데이터 받아오기
        Intent intent = this.getActivity().getIntent();
        String name = intent.getStringExtra("name");
        int num = intent.getIntExtra("num", 0);
        int price = intent.getIntExtra("price",100);


        // xml 요소
        TextView gifticon_buy_text_success = (TextView) v.findViewById(R.id.gifticon_buy_text_success);
        TextView gif_text = (TextView) v.findViewById(R.id.gif_text);
        // 구매 팝업 기본 텍스트 설정
        gifticon_buy_text_success.setText("");


        // addValueEventListener 내에서 사용할 final 변수 정의
        final GIFTICONADST[] usegift = new GIFTICONADST[1];
        final int[] lastNum = {0};
        final int[] userPoint = new int[1];


        // db
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();


        // 사용자 구매 기록 마지막 일련번호 찾기
        // 새로운 구매 기록 일련번호 : 마지막 일련번호 +1
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



        // 현재 유저 포인트 확인
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


        reference.child("GIFTICONADST").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gifticonADSTList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    GIFTICONADST gif = data.getValue(GIFTICONADST.class);
                    if(gif!=null){
                        // 사용 가능한 기프티콘 재고 확인
                        if (gif.getGifticonNum()==num && gif.isGifticonUsage()==false){
                            gifticonADSTList.add(gif);}
                    }
                }

                if (isStock()) {
                    if (userPointOK(price, userPoint[0])) {
                        // 구매 팝업 텍스트 설정
                        gifticon_buy_text_success.setText(name + "\n를 구매하였습니다.");
                        gif_text.setText("마이 페이지에서 '나의 기프티콘'을 확인하세요 !");

                        // 푸시 알람 설정 확인
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

                        // 푸시 알람이 허용되었을 경우
                        if(push[0] ==1) {
                            // '기프티콘 '기프티콘이름'을/를 구매하셨습니다.' 푸시 알람 송출
                            PushAlarm p = new PushAlarm(v.getContext(), v);
                            NotificationManager mNotificationManager = p.createNotificationChannel();
                            mNotificationManager.notify(1, p.pushAlarm("기프티콘", name + "을/를 구매하셨습니다."));
                        }

                        // 알림 데이터 추가
                        List<ALERT> alertList = new ArrayList<>();
                        reference.child("ALERT").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot data : snapshot.getChildren()){
                                    ALERT alt = data.getValue(ALERT.class);
                                    alertList.add(alt);
                                }
                                // 마지막 일련번호 확인
                                // 새 알림 일련번호 = 마지막 일련번호 +1
                                int alert_num = getlastalertNum(alertList)+1;
                                // 알림 추가
                                ALERT alert = new ALERT(uid, alert_num, "기프티콘",name+"을/를 구매하셨습니다.",new Date());
                                reference.child("ALERT").child(String.valueOf(alert_num)).setValue(alert);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        // 사용자 포인트에서 구매 포인트 차감
                        userPoint[0] = userPoint[0]- price;
                        //차감 후 포인트 변경사항 적용
                        reference.child("USER").child(uid).child("user_point").setValue(userPoint[0]);

                        for (GIFTICONADST data : gifticonADSTList) {
                            // 기프티콘 재고 사용
                            // 사용 가능한 기프티콘 재고 데이터 한 개의 사용됨 여부를
                            // true로 변경
                            if (data.isGifticonUsage() == false) {
                                data.setGifticonUsage(true);
                                break;
                            }
                        }
                        if (gifticonADSTList.get(0) != null) {
                            // 구매한 기프티콘 재고 데이터
                            usegift[0] = gifticonADSTList.get(0);
                            // 변경된 내용 적용
                            reference.child("GIFTICONADST").child(String.valueOf(usegift[0].getad_Num())).setValue(usegift[0]);
                            // 해당 기프티콘 재고 데이터에 구매 날짜(사용 날짜) 기록
                            reference.child("GIFTICONADST").child(String.valueOf(usegift[0].getad_Num())).child("usedate").setValue(new Date());
                            // 사용자 기프티콘 구매 기록 추가
                            USER_GIFTICON user_gifticon = new USER_GIFTICON(uid,lastNum[0], usegift[0].getad_Num());
                            reference.child("USER_GIFTICON").child(String.valueOf(lastNum[0])).setValue(user_gifticon);

                        }
                    } else {
                        // 포인트 부족으로 구매 실패시 텍스트 설정
                        gifticon_buy_text_success.setText(name + " 구매 실패. \n 포인트가 부족합니다.");
                        gif_text.setText("");
                    }

                }else{
                    // 재고 부족으로 구매 실패시 텍스트 설정
                    gifticon_buy_text_success.setText("재고가 부족합니다. 나중에 다시 시도해주세요.");
                    gif_text.setText("");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError","error");
            }
        });

        // x 버튼 클릭 이벤트
        ImageButton exit = (ImageButton) v.findViewById(R.id.btn_x);
        // x 버튼 클릭시 팝업 종료
        exit.setOnClickListener(this);
        return v;
    }
    // 사용 가능한 재고 여부 확인 함수
    public boolean isStock(){
        if (gifticonADSTList.size()!=0){
            return true;
        }else return false;
    }

    // 구매 가능 여부 확인 함수
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

    // 마지막 일련번호 확인 함수
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