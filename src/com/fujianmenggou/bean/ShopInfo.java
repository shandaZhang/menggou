package com.fujianmenggou.bean;

import java.util.ArrayList;

public class ShopInfo {
	private String shopId;

	private String companyName;
	private String type1;
	private String type2;
	private String type3;
	private String area1;
	private String area2;
	private String area3;
	private String phone;// 联系方式
	private String name;// 联系姓名

	private String address;
	private String businessArea;// 所在商圈
	private double lat;
	private double lng;
	private ArrayList<String> shopIcon;// 店面图
	private ArrayList<String> showIcon;// 展示图

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public String getType3() {
		return type3;
	}

	public void setType3(String type3) {
		this.type3 = type3;
	}

	public String getArea1() {
		return area1;
	}

	public void setArea1(String area1) {
		this.area1 = area1;
	}

	public String getArea2() {
		return area2;
	}

	public void setArea2(String area2) {
		this.area2 = area2;
	}

	public String getArea3() {
		return area3;
	}

	public void setArea3(String area3) {
		this.area3 = area3;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public ArrayList<String> getShopIcon() {
		return shopIcon;
	}

	public void setShopIcon(ArrayList<String> shopIcon) {
		this.shopIcon = shopIcon;
	}

	public ArrayList<String> getShowIcon() {
		return showIcon;
	}

	public void setShowIcon(ArrayList<String> showIcon) {
		this.showIcon = showIcon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

}
