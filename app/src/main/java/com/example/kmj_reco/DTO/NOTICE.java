package com.example.kmj_reco.DTO;

import org.checkerframework.checker.propkey.qual.PropertyKey;

import java.util.Date;

public class NOTICE{
    private String notice_date;
    private String notice_detail;
    private int notice_num;
    private String notice_title;

    public NOTICE() {}

    public void setNotice_date(String notice_date) {
        this.notice_date = notice_date;
    }
    public String getNotice_date(){ return notice_date;}

    public void setNotice_detail(String notice_detail) { this.notice_detail = notice_detail; }
    public String getNotice_detail(){
        return notice_detail;
    }

    @PropertyKey
    public int getNotice_num(){
        return notice_num;
    }
    public void setNotice_num(int notice_num) {
        this.notice_num = notice_num;
    }

    public String getNotice_title(){ return notice_title; }
    public void setNotice_title(String notice_title) { this.notice_title = notice_title; }

    public NOTICE(String notice_date, String notice_detail, int notice_num , String notice_title){
        this.notice_date=notice_date;
        this.notice_detail=notice_detail;
        this.notice_num=notice_num;
        this.notice_title=notice_title;
    }
}

