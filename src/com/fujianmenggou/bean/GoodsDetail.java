package com.fujianmenggou.bean;

import java.util.HashMap;

public class GoodsDetail {
	private double priceNow;
	private double priceMarket;
	private String url;
	private String name;
	private String remainningTime;
	private int assessmentNum;
	private String content;
	// private HashMap<String, String> attrs;
	private String attrs;

	private String goodsId;

	public double getPriceMarket() {
		return priceMarket;
	}

	public void setPriceMarket(double priceMarket) {
		this.priceMarket = priceMarket;
	}

	public double getPriceNow() {
		return priceNow;
	}

	public void setPriceNow(double priceNow) {
		this.priceNow = priceNow;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemainningTime() {
		return remainningTime;
	}

	public void setRemainningTime(String remainningTime) {
		this.remainningTime = remainningTime;
	}

	public int getAssessmentNum() {
		return assessmentNum;
	}

	public void setAssessmentNum(int assessmentNum) {
		this.assessmentNum = assessmentNum;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	// public HashMap<String, String> getAttrs() {
	// return attrs;
	// }

	// public void setAttrs(HashMap<String, String> attrs) {
	// this.attrs = attrs;
	// }

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
