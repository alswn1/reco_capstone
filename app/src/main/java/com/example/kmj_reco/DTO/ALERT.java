package com.example.kmj_reco.DTO;

import org.checkerframework.checker.propkey.qual.PropertyKey;

import java.util.Date;

public class ALERT {
    private String user_num;
    private Date alert_date;
    private String alert_text;
    private int alert_num;
    private String alert_title;

    public void setuser_Name(String user_num) {
        this.user_num = user_num;
    }
    public String getuser_Name(){
        return user_num;
    }


    public void setalert_Date(Date alert_date) {
        this.alert_date = alert_date;
    }

    public Date getalert_Date(){
        return alert_date;
    }

    public void setAlert_text(String alert_text) {
        this.alert_text = alert_text;
    }

    public String getAlert_text(){
        return alert_text;
    }
    @PropertyKey
    public int getalert_Num(){
        return alert_num;
    }

    public void setalert_Num(int alert_num) {
        this.alert_num = alert_num;
    }

    public String getalert_Title(){
        return alert_title;
    }

    public void setalert_Title(String alert_title) {
        this.alert_title = alert_title;
    }

    public ALERT(){

    }

    public ALERT(String user_num, int alert_num, String alert_title, String alert_text, Date alert_date){
        this.user_num = user_num;
        this.alert_num = alert_num;
        this.alert_title=alert_title;
        this.alert_text=alert_text;
        this.alert_date=alert_date;
    }
}

