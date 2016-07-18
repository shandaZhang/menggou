package com.fujianmenggou.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

import com.fujianmenggou.fm.BuyLevelFragment;

public class BuyLevelAdapter extends FragmentPagerAdapter {
	protected static final String[] CONTENT = new String[] { "普通微店", "合作机构",
			"区域代理" };
	protected static final String[] levelRoad = new String[] { "开通微店", "交易一笔",
			"推荐一人" };
	String rbdiscount;
	String ybdiscount;
	String xmunionpaydiscount;

	private int mCount = CONTENT.length;

	private List<HashMap<String, String>> levelList;

	public BuyLevelAdapter(FragmentManager fm) {
		super(fm);
	}

	public BuyLevelAdapter(FragmentManager fm,
			List<HashMap<String, String>> levelList) {
		super(fm);
		this.levelList = levelList;
	}

	@Override
	public Fragment getItem(int position) {
		if (levelList.size() > 0) {
			rbdiscount = levelList.get(position).get("rbdiscount");
			ybdiscount = levelList.get(position).get("ybdiscount");
			xmunionpaydiscount = levelList.get(position).get(
					"xmunionpaydiscount");
		} else {
			rbdiscount = " ";
			ybdiscount = " ";
			xmunionpaydiscount = " ";
		}
		return BuyLevelFragment.newInstance(CONTENT[position % CONTENT.length],
				levelRoad[position % levelRoad.length], rbdiscount, ybdiscount,
				xmunionpaydiscount);
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return BuyLevelAdapter.CONTENT[position % CONTENT.length];
	}

	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}

	public void setList(List<HashMap<String, String>> levelList) {
		this.levelList = levelList;
		notifyDataSetChanged();
	}
}