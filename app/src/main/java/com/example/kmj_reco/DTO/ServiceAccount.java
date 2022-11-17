package com.example.kmj_reco.DTO;

public class ServiceAccount {
    private String service_title; //제목
    private String service_contents; //내용
    private String service_publisher; //게시자
    private String service_date; //날짜
    private int service_num; //번호

    public ServiceAccount(String service_title, String service_contents, String service_publisher, int service_num, String service_date) {
        this.service_title = service_title;
        this.service_contents = service_contents;
        this.service_publisher = service_publisher;
        this.service_date = service_date;
        this.service_num = service_num;
    }

    public ServiceAccount() {}

    public String getService_Title() {return service_title;}

    public void setService_Title(String service_title) {this.service_title = service_title;}

    public String getService_Contents() {return service_contents;}

    public void setService_Contents(String service_contents) {this.service_contents = service_contents;}

    public String getService_Publisher() {return service_publisher;}

    public void setService_Publisher(String service_publisher) {this.service_publisher = service_publisher;}

    public String getService_Date() {return service_date;}

    public void setService_Date(String service_date) {this.service_date = service_date;}

    public int getService_Num() {return service_num;}

    public void setService_Num(int service_num) {this.service_num = service_num;}
}