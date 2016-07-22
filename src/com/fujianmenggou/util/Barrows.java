package com.fujianmenggou.util;

import java.util.ArrayList;

import com.fujianmenggou.bean.OrderList;

public class Barrows {

	private static ArrayList<OrderList> barrows;

	public static ArrayList<OrderList> getInstance() {
		synchronized (Barrows.class) {
			if (barrows == null) {
				barrows = new ArrayList<OrderList>();
			}
		}
		return barrows;
	}

	private Barrows() {
	}

	public static void add(OrderList data) {
		barrows.add(data);
	}
	
	public static void remove(int index){
		barrows.remove(index);
	}

}
