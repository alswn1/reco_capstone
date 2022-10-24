package com.example.kmj_reco.DTO;

public class GIFTICONDATA {
    private int gifticon_Num = 0;
    private String gifticon_Type = "";
    private String gifticon_Name = "";
    private int gifticon_Price=0;
    private String gifticon_Image="";
    private String gifticon_Detail="";
    private String gifticon_Brand="";

    public void setgifticon_Image(String gifticon_Image){
        this.gifticon_Image = gifticon_Image;
    }

    public String getgifticon_Image(){
        return gifticon_Image;
    }

    public void setgifticon_Price(int gifticon_Price){
        this.gifticon_Price = gifticon_Price;
    }

    public int getgifticon_Price(){
        return gifticon_Price;
    }

    public void setgifticon_Type(String gifticon_Type){
        this.gifticon_Type = gifticon_Type;
    }

    public String getgifticon_Type(){
        return gifticon_Type;
    }

    public void setgifticon_Name(String gifticon_Name){
        this.gifticon_Name = gifticon_Name;
    }

    public String getgifticon_Name(){
        return gifticon_Name;
    }

    public void setgifticon_Num(int gifticon_Num){
        this.gifticon_Num = gifticon_Num;
    }

    public int getgifticon_Num(){
        return gifticon_Num;
    }

    public void setgifticon_Detail(String gifticon_Detail){
        this.gifticon_Detail = gifticon_Detail;
    }

    public String getgifticon_Detail(){
        return gifticon_Detail;
    }

    public void setgifticon_Brand(String gifticon_Brand){
        this.gifticon_Brand = gifticon_Brand;
    }

    public String getgifticon_Brand(){
        return gifticon_Brand;
    }

    public GIFTICONDATA() {}

    public GIFTICONDATA(int gifticon_Price, String gifticon_Detail, int gifticon_Num,String gifticon_Image, String gifticon_Name, String gifticon_Type, String gifticon_Brand){
        this.gifticon_Num = gifticon_Num;
        this.gifticon_Image = gifticon_Image;
        this.gifticon_Price=gifticon_Price;
        this.gifticon_Name=gifticon_Name;
        this.gifticon_Brand=gifticon_Brand;
        this.gifticon_Type=gifticon_Type;
        this.gifticon_Detail=gifticon_Detail;
    }

    public static int lastNum(java.util.List<GIFTICONDATA> gifticondataList){
            int k=0;
            int i;
            for(GIFTICONDATA data:gifticondataList){
                i = data.getgifticon_Num();
                if (k<=i) {k=i;}
            }
            return k;
    }
}
