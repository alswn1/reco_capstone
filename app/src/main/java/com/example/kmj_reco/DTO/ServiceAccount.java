package com.example.kmj_reco.DTO;

public class ServiceAccount {
    private String title; //제목
    private String contents; //내용
    private String publisher; //게시자

    public ServiceAccount(String title, String contents, String publisher) {
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
    }

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getContents() {return contents;}

    public void setContents(String contents) {this.contents = contents;}

    public String getPublisher() {return publisher;}

    public void setPublisher(String publisher) {this.publisher = publisher;}


    public ServiceAccount() {}


}