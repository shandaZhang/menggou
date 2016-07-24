package com.fujianmenggou.util;

import java.util.ArrayList;

import com.fujianmenggou.bean.OrderList;
import com.fujianmenggou.bean.UserAddress;

public class Barrows {
	private static Barrows barrows;
	private static ArrayList<OrderList> barrowList;
	private UserAddress address;

	public UserAddress getAddress() {
		return address;
	}

	public void setAddress(UserAddress addr) {
		address = addr;
	}

	public static Barrows getInstance() {
		synchronized (Barrows.class) {
			if (barrows == null) {
				barrows = new Barrows();
				barrowList = new ArrayList<OrderList>();
			}
		}
		return barrows;
	}

	private Barrows() {
	}

	public ArrayList<OrderList> getBarrowList() {
		return barrowList;
	}

	public static void add(OrderList data) {
		barrowList.add(data);
	}

	public static void remove(int index) {
		barrowList.remove(index);
	}

}
