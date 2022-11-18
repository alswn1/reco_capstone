package com.example.kmj_reco.DTO;

public class SERVICE_ANSWER {
    private int service_answer_num; //번호
    private String service_answer; //문의 답변
    private int service_num; //문의 번호

    public SERVICE_ANSWER(int service_answer_num, String service_answer, int service_num) {
        this.service_answer_num = service_answer_num;
        this.service_answer = service_answer;
        this.service_num = service_num;
    }

    public int getService_answer_num() {
        return service_answer_num;
    }

    public void setService_answer_num(int service_answer_num) {
        this.service_answer_num = service_answer_num;
    }

    public String getService_answer() {
        return service_answer;
    }

    public void setService_answer(String service_answer) {
        this.service_answer = service_answer;
    }

    public int getService_num() {
        return service_num;
    }

    public void setService_num(int service_num) {
        this.service_num = service_num;
    }
}
