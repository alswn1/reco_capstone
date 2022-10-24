package com.example.kmj_reco.DTO;

public class USER_GIFTICON{
	private String user_num;
	private int user_gifticon_Num;
	private int gifticon_ad_num;

	public void setuser_num(String user_num){
		this.user_num = user_num;
	}

	public String getuser_num(){
		return user_num;
	}

	public void setuser_gifticon_num(int user_gifticon_Num){
		this.user_gifticon_Num = user_gifticon_Num;
	}

	public int getuser_gifticon_num(){
		return user_gifticon_Num;
	}

	public void setgifticon_ad_Num(int gifticon_ad_num){
		this.gifticon_ad_num = gifticon_ad_num;
	}

	public int getgifticon_ad_Num(){
		return gifticon_ad_num;
	}
	public USER_GIFTICON(){

	}
	public USER_GIFTICON (String user_num, int user_gifticon_Num, int gifticon_ad_num){
		this.user_gifticon_Num = user_gifticon_Num;
		this.user_num = user_num;
		this.gifticon_ad_num = gifticon_ad_num;
	}
}
