package com.example.kmj_reco.DTO;

public class USER {
    private String idToken; //Firebase Uid (고유 토큰 정보)
    private String user_name; //이름
    private String user_birth; //생일
    private String user_id; //아이디
    private String user_pwd; //패스워드
    private String user_phone; //전화번호
    private String user_email; //이메일
    private int user_point;

    public USER() {}

    public USER(String idToken, String user_name, String user_phone, String user_id, String user_pwd, String user_email, String user_birth, int user_point) {
        this.idToken = idToken;
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.user_id = user_id;
        this.user_pwd = user_pwd;
        this.user_email = user_email;
        this.user_birth = user_birth;
        this.user_point = user_point;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_birth() {
        return user_birth;
    }

    public void setUser_birth(String user_birth) {
        this.user_birth = user_birth;
    }

    public int getUser_point() {
        return user_point;
    }

    public void setUser_point(int user_point) {
        this.user_point = user_point;
    }


}
