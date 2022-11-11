package com.example.kmj_reco.DTO;

public class RECOBIN {
    public String recobin_num;
    public String recobin_roadname;
    public String recobin_address;
    public String recobin_fulladdress;
    public String recobin_locate;
    public Double recobin_latitude;
    public Double recobin_longitude;

    public RECOBIN() {

    }

    public RECOBIN(String recobin_num, String recobin_roadname, String recobin_address, String recobin_fulladdress,String recobin_locate,Double recobin_latitude, Double recobin_longitude) {
        this.recobin_num = recobin_num;
        this.recobin_roadname = recobin_roadname;
        this.recobin_address = recobin_address;
        this.recobin_fulladdress = recobin_fulladdress;
        this.recobin_locate = recobin_locate;
        this.recobin_latitude = recobin_latitude;
        this.recobin_longitude = recobin_longitude;
    }

    public String getRecobin_num() {
        return recobin_num;
    }
    public void setRecobin_num(String recobin_num) {
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

    @Override
    public String toString() {
        return "RECOBIN{" +
                "recobin_num=" + recobin_num +
                ", recobin_roadname='" + recobin_roadname + '\'' +
                ", recobin_address='" + recobin_address + '\'' +
                '}';
    }
}




