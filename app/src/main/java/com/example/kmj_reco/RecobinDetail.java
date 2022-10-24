package com.example.kmj_reco;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

public class RecobinDetail extends FragmentActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recobin_detail);

        // recobin 상세주소
        String recobin_ad = getIntent().getStringExtra("recobin_fulladdress");

        TextView recobin_fulladdress = (TextView) findViewById(R.id.recobin_fulladdress);
        recobin_fulladdress.setText(recobin_ad);

        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView btn_qr_scan = (ImageView) findViewById(R.id.btn_qr_scan);
        btn_qr_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QrActivity.class);
                startActivity(intent);
            }
        });
    }
}
