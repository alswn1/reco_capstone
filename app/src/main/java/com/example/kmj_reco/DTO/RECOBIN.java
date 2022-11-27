package com.example.kmj_reco.DTO;

public class RECOBIN {
    public int recobin_num; // recobin Index
    public String recobin_roadname; // 도로명
    public String recobin_address; // 주소
    public String recobin_fulladdress; // 상세주소
    public String recobin_locate; // 위치
    public Double recobin_latitude; // 위도
    public Double recobin_longitude; // 경도

    public RECOBIN() {}

    public RECOBIN(int recobin_num, String recobin_roadname, String recobin_address, String recobin_fulladdress,String recobin_locate, Double recobin_latitude, Double recobin_longitude) {
        this.recobin_num = recobin_num;
        this.recobin_roadname = recobin_roadname;
        this.recobin_address = recobin_address;
        this.recobin_fulladdress = recobin_fulladdress;
        this.recobin_locate = recobin_locate;
        this.recobin_latitude = recobin_latitude;
        this.recobin_longitude = recobin_longitude;
    }

    public int getRecobin_num() {
        return recobin_num;
    }
    public void setRecobin_num(int recobin_num) {
        this.recobin_num = recobin_num;
    }

    public String getRecobin_roadname() {
        return recobin_roadname;
    }
    public void setRecobin_roadname(String recobin_roadname) { this.recobin_roadname = recobin_roadname; }

    public String getRecobin_address() {
        return recobin_address;
    }
    public void setRecobin_address(String recobin_address) { this.recobin_address = recobin_address; }

    public String getRecobin_fulladdress() {
        return recobin_fulladdress;
    }
    public void setRecobin_fulladdress(String recobin_fulladdress) { this.recobin_fulladdress = recobin_fulladdress; }

    public String getRecobin_locate() {
        return recobin_locate;
    }
    public void setRecobin_locate(String recobin_locate) { this.recobin_locate = recobin_locate; }

    public Double getRecobin_latitude() {
        return recobin_latitude;
    }
    public void setRecobin_latitude(Double recobin_latitude) { this.recobin_latitude = recobin_latitude; }

    public Double getRecobin_longitude() {
        return recobin_longitude;
    }
    public void setRecobin_longitude(Double recobin_longitude) { this.recobin_longitude = recobin_longitude; }

    public static int lastNum(java.util.List<RECOBIN> recobinList){
        int k=0;
        int i;
        for(RECOBIN data:recobinList){
            i = data.getRecobin_num();
            if (k<=i) {k=i;}
        }
        return k;
    }
}




