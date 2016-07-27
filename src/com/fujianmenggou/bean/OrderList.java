package com.fujianmenggou.bean;

import java.io.Serializable;

import android.R.integer;

public class OrderList implements Serializable{
	private String title;// 商品名
	private String id;// 商品id
	private String detail;// 商品详情
	private double price;// 商品单价
	private double priceAll;// 合计
	private boolean isChecked;// 是否被选中
	private int number;// 数量
	private String url;
	private int status;	
	private int addressId;

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getPriceAll() {
		return priceAll;
	}

	public void setPriceAll(double priceAll) {
		this.priceAll = priceAll;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}
