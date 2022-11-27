package com.example.kmj_reco.DTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GIFTICONADST{
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd"); // 날짜 데이터 형식 지정
	private boolean gifticonUsage = false; // 해당 기프티콘 바코드 사용 여부
	private int gifticonNum = 0; // 기프티콘 식별번호
	private int ad_num = 0; // 기프티콘 바코드 식별번호
	private Date gifticonExpirydate; // 기프티콘 만료 기한
	private String gifticonBarcode = ""; // 기프티콘 바코드 이미지 url
	private Date buydate = sdf.parse("2000-01-01"); // 기프티콘 구매 날짜 (관리자 구매 날짜)
	private Date usedate = sdf.parse("2000-01-01"); // 기프티콘 사용 날짜 (사용자 구매 날짜)

	public void setGifticonUsage(boolean gifticonUsage){
		this.gifticonUsage = gifticonUsage;
	}

	public boolean isGifticonUsage(){
		return gifticonUsage;
	}

	public void setGifticonNum(int gifticonNum){
		this.gifticonNum = gifticonNum;
	}

	public int getGifticonNum(){
		return gifticonNum;
	}

	public void setad_Num(int ad_num){
		this.ad_num = ad_num;
	}

	public int getad_Num(){
		return ad_num;
	}

	public void setGifticonExpirydate(Date gifticonExpirydate){ this.gifticonExpirydate = gifticonExpirydate; }

	public Date getGifticonExpirydate(){
		return gifticonExpirydate;
	}

	public void setGifticonBarcode(String gifticonBarcode){
		this.gifticonBarcode = gifticonBarcode;
	}

	public String getGifticonBarcode(){
		return gifticonBarcode;
	}


	public void setBuydate(Date buydate){
		this.buydate = buydate;
	}

	public Date getBuydate(){
		return buydate;
	}

	public void setUsedate(Date usedate){
		this.usedate = usedate;
	}

	public Date getUsedate(){
		return usedate;
	}

	public GIFTICONADST() throws ParseException {

	}

	public GIFTICONADST(int ad_num, boolean gifticonUsage, int gifticonNum, Date gifticonExpirydate, String gifticonBarcode, Date buydate, Date usedate) throws ParseException {
		this.ad_num = ad_num;
		this.gifticonBarcode = gifticonBarcode;
		this.gifticonExpirydate = gifticonExpirydate;
		this.gifticonUsage = gifticonUsage;
		this.gifticonNum = gifticonNum;
		this.buydate = buydate;
		this.usedate = usedate;
	}
}
