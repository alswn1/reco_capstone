package com.example.kmj_reco.DTO;

public class EVENT {
    public int event_num; // pk
    public String event_startdate;
    public String event_enddate;
    public String event_content;
    public String event_image;
    public int event_random;

    public EVENT() {}

    public EVENT(int event_num, String event_startdate, String event_enddate, String event_content,String event_image, int event_random) {
        this.event_num = event_num;
        this.event_startdate = event_startdate;
        this.event_enddate = event_enddate;
        this.event_content = event_content;
        this.event_image = event_image;
        this.event_random = event_random;
    }

    public int getEvent_num() {
        return event_num;
    }
    public void setEvent_num(int event_num) { this.event_num = event_num; }

    public String getEvent_startdate() {
        return event_startdate;
    }
    public void setEvent_startdate(String event_startdate) { this.event_startdate = event_startdate; }

    public String getEvent_enddate() {
        return event_enddate;
    }
    public void setEvent_enddate(String event_enddate) { this.event_enddate = event_enddate; }

    public String getEvent_content() {
        return event_content;
    }
    public void setEvent_content(String event_content) { this.event_content = event_content; }

    public String getEvent_image() {
        return event_image;
    }
    public void setEvent_image(String event_image) { this.event_image = event_image; }

    public int getEvent_random() {
        return event_random;
    }
    public void setEvent_random(int event_random) { this.event_random = event_random; }
}
