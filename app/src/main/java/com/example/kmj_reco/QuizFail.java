package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.FragmentActivity;

public class QuizFail extends FragmentActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quiz_fail);

        // QuizMain Activity에서 데이터 받아오기
        String quizAnswer = getIntent().getStringExtra("quiz_answer");
        String quizAnswerContent = getIntent().getStringExtra("quiz_answer_content");

        // 확인 버튼 터치 시 퀴즈 정답 화면으로 이동
        Button btn_fail_check = (Button) findViewById(R.id.btn_fail_check);
        btn_fail_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 퀴즈 정답 화면으로 데이터 전송 및 이동
                Intent intent = new Intent(getApplicationContext(), QuizAnswer.class);
                intent
                        .putExtra("quiz_answer", quizAnswer)
                        .putExtra("quiz_answer_content", quizAnswerContent);
                startActivity(intent);
            }
        });
    }

    // 화면 터치 시에도 팝업이 닫히지 않게 설정
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}
