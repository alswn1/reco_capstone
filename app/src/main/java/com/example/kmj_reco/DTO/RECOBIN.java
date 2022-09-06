package com.example.kmj_reco.DTO;

public class RECOBIN {
    public int recobin_num;
    public String recobin_roadname;
    public String recobin_address;
    public int star_score;

    public RECOBIN() {

    }

    public RECOBIN(int recobin_num, String recobin_roadname, String recobin_address, int star_score) {
        this.recobin_num = recobin_num;
        this.recobin_roadname = recobin_roadname;
        this.recobin_address = recobin_address;
        this.star_score = star_score;
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

    public void setRecobin_roadname(String recobin_roadname) {
        this.recobin_roadname = recobin_roadname;
    }

    public String getRecobin_address() {
        return recobin_address;
    }

    public void setRecobin_address(String recobin_address) {
        this.recobin_address = recobin_address;
    }

    public int getStar_score() {
        return star_score;
    }

    public void setStar_score(int star_score) {
        this.star_score = star_score;
    }

    @Override
    public String toString() {
        return "RECOBIN{" +
                "recobin_num=" + recobin_num +
                ", recobin_roadname='" + recobin_roadname + '\'' +
                ", recobin_address='" + recobin_address + '\'' +
                ", star_score=" + star_score +
                '}';
    }
}




