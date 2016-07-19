package com.fujianmenggou.atv;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.fujianmenggou.R;
import com.fujianmenggou.adapter.OrderManageAdapter;
import com.fujianmenggou.util.BaseActivity;
import com.viewpagerindicator.TabPageIndicator;

public class OrderManageActivity extends BaseActivity {
	private OrderManageAdapter mAdapter;
	private ViewPager mPager;
	private TabPageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_manage);
		initFakeTitle();
		setTitle("订单管理");
		initView();
	}

	private void initView() {
		mPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		// 新建FragmentAdapter作为pager的adapter
		mAdapter = new OrderManageAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);

	}

}
