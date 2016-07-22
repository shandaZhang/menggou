package com.fujianmenggou.bean;

public class OrderList {
	private String title;// 商品名
	private String id;// 商品id
	private String detail;// 商品详情
	private double price;// 商品单价
	private double priceAll;// 合计
	private boolean isChecked;// 是否被选中
	private int number;// 数量
	private String url;

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
