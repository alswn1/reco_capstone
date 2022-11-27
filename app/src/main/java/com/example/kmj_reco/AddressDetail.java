package com.example.kmj_reco;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

public class AddressDetail extends FragmentActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map_detail);

        // Map Activity에서 trashcan 상세주소 받아옴
        String trashcan_ad = getIntent().getStringExtra("trashcan_fulladdress");
        TextView trashcan_fulladdress = (TextView) findViewById(R.id.trashcan_fulladdress);
        trashcan_fulladdress.setText(trashcan_ad); // 받아온 데이터 설정 후 보여줌

        // 팝업 레이아웃 설정
        getWindow().getAttributes().gravity = Gravity.CENTER;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
}