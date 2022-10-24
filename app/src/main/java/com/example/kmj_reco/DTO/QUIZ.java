package com.example.kmj_reco.DTO;

public class QUIZ {
    public int quiz_num; // pk
    public String quiz_answer_context;
    public String quiz_answer;
    public String quiz_question;

    public QUIZ() {}

    public QUIZ(int quiz_num, String quiz_answer_context, String quiz_answer, String quiz_question) {
        this.quiz_num = quiz_num;
        this.quiz_answer_context = quiz_answer_context;
        this.quiz_answer = quiz_answer;
        this.quiz_question = quiz_question;
    }

    public int getQuiz_num() {
        return quiz_num;
    }
    public void setQuiz_num(int quiz_num) { this.quiz_num = quiz_num; }

    public String getQuiz_answer_context() {
        return quiz_answer_context;
    }
    public void setQuiz_answer_context(String quiz_answer_context) { this.quiz_answer_context = quiz_answer_context; }

    public String getQuiz_answer() {
        return quiz_answer;
    }
    public void setQuiz_answer(String quiz_answer) { this.quiz_answer = quiz_answer; }

    public String getQuiz_question() {
        return quiz_question;
    }
    public void setQuiz_question(String quiz_question) { this.quiz_question = quiz_question; }
}