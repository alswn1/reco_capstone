package com.example.kmj_reco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class QuizAnswer extends Activity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.quiz_answer);

        // 전 화면에서 퀴즈 정답 받아오기
        String quizAnswer = getIntent().getStringExtra("quiz_answer");
        TextView quiz_answer = (TextView) findViewById(R.id.quiz_answer);
        quiz_answer.setText(quizAnswer); // 받아온 데이터로 설정

        // 전 화면에서 퀴즈 정답 설명 받아오기
        String quizAnswerContent = getIntent().getStringExtra("quiz_answer_content");
        TextView quiz_answer_content = (TextView) findViewById(R.id.quiz_answer_content);
        quiz_answer_content.setText(quizAnswerContent);

        // 홈 버튼 터치 시 홈 화면으로 이동
        ImageView btn_home = (ImageView) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        // 설정 버튼 터치 시 설정 화면으로 이동
        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        // 알림 버튼 터치 시 알림 화면으로 이동
        ImageView btn_alert = (ImageView) findViewById(R.id.btn_alert);
        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });

        // 확인 버튼 터치 시 알림 화면으로 이동
        Button btn_quiz_answer_check = (Button) findViewById(R.id.btn_quiz_answer_check);
        btn_quiz_answer_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });
    }
}
