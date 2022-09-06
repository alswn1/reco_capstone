package com.example.kmj_reco;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class QrActivity extends AppCompatActivity {
    private Button scanBtn;
    private TextView recobin_num, recobin_roadname, recobin_address, star_score;

    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        scanBtn = findViewById(R.id.scanBtn);
        recobin_num = findViewById(R.id.recobin_num);
        recobin_roadname = findViewById(R.id.recobin_roadname);
        recobin_address = findViewById(R.id.recobin_address);
        star_score = findViewById(R.id.star_score);


        qrScan = new IntentIntegrator(this);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.setPrompt("Scanning...");
                qrScan.initiateScan();
            }
        });
    }

    // Getting the scan result to textView

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // qrcode가 없을 때
            if (result.getContents() == null) {
                Toast.makeText(this, "취소!", Toast.LENGTH_SHORT).show();
            // qrcode가 있을 때
            }else {
                Toast.makeText(this, "스캔 완료!", Toast.LENGTH_SHORT).show();

                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    recobin_num.setText(obj.getString("recobin_num"));
                    recobin_roadname.setText(obj.getString("recobin_roadname"));
                    recobin_address.setText(obj.getString("recobin_address"));
                    star_score.setText(obj.getString("star_score"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "실패!!!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}