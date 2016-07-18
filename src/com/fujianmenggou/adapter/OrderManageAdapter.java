package com.fujianmenggou.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.fujianmenggou.bean.OrderList;
import com.fujianmenggou.fm.OrderFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OrderManageAdapter extends FragmentPagerAdapter {
	public static final String[] title = new String[] { "全部", "待付款", "待发货",
			"待收货", "待评价" };

	public OrderManageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return OrderFragment.newInstance(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return title[position % title.length];
	}

	@Override
	public int getCount() {
		return title.length;
	}

}
