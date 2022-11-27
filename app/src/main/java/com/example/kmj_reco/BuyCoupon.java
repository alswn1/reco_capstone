package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class BuyCoupon extends DialogFragment implements View.OnClickListener {
    public BuyCoupon() {
    }
    public static BuyCoupon getInstance(){
        BuyCoupon e = new BuyCoupon();
        return e;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // View 정의, TextView 받아오기
        View v = inflater.inflate(R.layout.buy_coupon,container);
        TextView gifticon_buy_text = (TextView) v.findViewById(R.id.gifticon_buy_text);
        TextView gifticon_buy_price_text = (TextView) v.findViewById(R.id.gifticon_buy_price_text);

        // 이전 Activity에서 전달한 기프티콘 이름과 기프티콘 가격 데이터 받아오기
        Intent intent = this.getActivity().getIntent();
        String name = intent.getStringExtra("name");
        int price = intent.getIntExtra("price",100);

        // TextView 내용 설정
        gifticon_buy_text.setText(name+"\n를 구매하시겠습니까?");
        gifticon_buy_price_text.setText("구매: " +Integer.toString(price)+" p");

        // 예, 아니오 버튼 클릭 이벤트
        Button n_Btn = (Button) v.findViewById(R.id.btn_y);
        Button btn_n = (Button) v.findViewById(R.id.btn_n);
        //예를 클릭할 경우 이 프래그먼트의 onClick 함수를 실행
        n_Btn.setOnClickListener(this);
        // 아니오를 클릭할 경우 창 닫힘
        btn_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        // x 버튼 클릭 이벤트
        ImageButton exit = (ImageButton)v.findViewById(R.id.btn_x);
        // x Button을 클릭할 경우 창 닫힘
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
        BuyFinish e = BuyFinish.getInstance();
        e.show(getActivity().getSupportFragmentManager(), "");

    }
}