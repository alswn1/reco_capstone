package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class QuizMain extends AppCompatActivity {
    TextView question;

    // DB
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.quiz_main);

        // DB 에서 데이터 추출
        database = FirebaseDatabase.getInstance();

        // xml 에서 바뀔 내용 각 id 불러오기
        question = findViewById(R.id.quiz_question);

        // QUIZ DB 불러오기
        database = FirebaseDatabase.getInstance();

        // Quiz content Set
        database.getReference("QUIZ").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 퀴즈 데이터 베이스 리스트 범위 내의 랜덤 설정
                final int i = new Random().nextInt((int) snapshot.getChildrenCount());

                String quiz_question = snapshot.child(String.valueOf(i)).child("quiz_question").getValue(String.class);
                question.setText(quiz_question);

                String quiz_answer = snapshot.child(String.valueOf(i)).child("quiz_answer").getValue(String.class);

                String quiz_answer_content = snapshot.child(String.valueOf(i)).child("quiz_answer_content").getValue(String.class);

                // O 버튼 클릭 시
                ImageButton btn_quiz_o = (ImageButton) findViewById(R.id.btn_quiz_o);
                btn_quiz_o.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (quiz_answer.equals("O")) { // 정답
                            Intent intent = new Intent(getApplicationContext(), QuizSuccess.class);
                            intent
                                    .putExtra("quiz_answer", quiz_answer)
                                    .putExtra("quiz_answer_content", quiz_answer_content);
                            startActivity(intent);
                        } else if (quiz_answer.equals("X")) { // 오답
                            Intent intent = new Intent(getApplicationContext(), QuizFail.class);
                            intent
                                    .putExtra("quiz_answer", quiz_answer)
                                    .putExtra("quiz_answer_content", quiz_answer_content);
                            startActivity(intent);
                        }
                    }
                });

                // X 버튼 클릭 시
                ImageButton btn_quiz_x = (ImageButton) findViewById(R.id.btn_quiz_x);
                btn_quiz_x.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (quiz_answer.equals("O")) { // 오답
                            Intent intent = new Intent(getApplicationContext(), QuizFail.class);
                            intent
                                    .putExtra("quiz_answer", quiz_answer)
                                    .putExtra("quiz_answer_content", quiz_answer_content);
                            startActivity(intent);
                        } else if (quiz_answer.equals("X")) { // 정답
                            Intent intent = new Intent(getApplicationContext(), QuizSuccess.class);
                            intent
                                    .putExtra("quiz_answer", quiz_answer)
                                    .putExtra("quiz_answer_content", quiz_answer_content);
                            startActivity(intent);
                        }
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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

        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });
    }
}