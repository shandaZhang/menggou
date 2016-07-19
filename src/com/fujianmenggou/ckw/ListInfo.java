package com.fujianmenggou.ckw;

public class ListInfo {
	int id;//Id
	String name;//名称
	double brokerage;//佣金
	String image;//图片地址
	double price;//价格
	int is;//代理
	String sid;//供应商ID
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getBrokerage() {
		return brokerage;
	}
	public void setBrokerage(double brokerage) {
		this.brokerage = brokerage;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getIs() {
		return is;
	}
	public void setIs(int is) {
		this.is = is;
	}
	
}
