package com.example.kmj_reco.DTO;

public class USER_SETTING{
	private String user_num;
	private int autologin=0;
	private int locationagree=0;
	private int personalagree=1;
	private int pushalarm=1;

	public void setUser_num(String user_num){
		this.user_num = user_num;
	}

	public String getUser_num(){
		return user_num;
	}

	public void setAutologin(int autologin){
		this.autologin = autologin;
	}

	public int getAutologin(){
		return autologin;
	}

	public void setLocationagree(int locationagree){
		this.locationagree = locationagree;
	}

	public int getLocationagree(){
		return locationagree;
	}

	public void setPersonalagree(int personalagree){
		this.personalagree = personalagree;
	}

	public int getPersonalagree(){
		return personalagree;
	}

	public void setPushalarm(int pushalarm){
		this.pushalarm = pushalarm;
	}

	public int getPushalarm(){
		return pushalarm;
	}

	public USER_SETTING(){

	}

	public USER_SETTING (String user_num, int autologin, int personalagree, int pushalarm, int locationagree){
		this.user_num = user_num;
		this.autologin = autologin;
		this.personalagree = personalagree;
		this.locationagree = locationagree;
		this.pushalarm = pushalarm;
	}
}
