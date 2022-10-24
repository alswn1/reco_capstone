package com.example.kmj_reco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class QuizAnswer extends Activity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.quiz_answer);

        // 퀴즈 정답 O or X
        String quizAnswer = getIntent().getStringExtra("quiz_answer");

        TextView quiz_answer = (TextView) findViewById(R.id.quiz_answer);
        quiz_answer.setText(quizAnswer);

        // 퀴즈 정답 설명
        String quizAnswerContent = getIntent().getStringExtra("quiz_answer_content");

        TextView quiz_answer_content = (TextView) findViewById(R.id.quiz_answer_content);
        quiz_answer_content.setText(quizAnswerContent);

        // 각 버튼 클릭 시 화면 이동
        ImageView btn_home = (ImageView) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), Settings.class);
        startActivity(intent);
        }
        });

        ImageView btn_alert = (ImageView) findViewById(R.id.btn_alert);
        btn_alert.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), Alert.class);
        startActivity(intent);
        }
        });

        ImageButton btn_quiz_answer_check = (ImageButton) findViewById(R.id.btn_quiz_answer_check);
        btn_quiz_answer_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });
    }
}
