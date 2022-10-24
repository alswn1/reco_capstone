package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.fragment.app.FragmentActivity;

public class QuizFail extends FragmentActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quiz_fail);

        String quizAnswer = getIntent().getStringExtra("quiz_answer");
        String quizAnswerContent = getIntent().getStringExtra("quiz_answer_content");

        ImageButton btn_fail_check = (ImageButton) findViewById(R.id.btn_fail_check);
        btn_fail_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuizAnswer.class);
                intent
                        .putExtra("quiz_answer", quizAnswer)
                        .putExtra("quiz_answer_content", quizAnswerContent);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}
