package com.fujianmenggou.fm;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fujianmenggou.R;
import com.fujianmenggou.R.id;
import com.fujianmenggou.adapter.GoodsShoppingMallAdapter;
import com.fujianmenggou.atv.BarrowActivity;
import com.fujianmenggou.bean.GoodsList;
import com.fujianmenggou.util.BaseFragment;
import com.fujianmenggou.util.XListView;
import com.fujianmenggou.util.XListView.IXListViewListener;

public class ShoppingMallFragment extends BaseFragment implements
		OnClickListener {
	private View rootView;// 缓存的界面
	private ViewPager viewPager;
	private ArrayList<GoodsList> goodsLists = new ArrayList<GoodsList>();
	// private ArrayList<ImageView> dots = new ArrayList<ImageView>();
	private ArrayList<String> ivPagerUrls = new ArrayList<String>();
	private ArrayList<ImageView> viewContainter = new ArrayList<ImageView>();
	private LinearLayout layoutDots;
	private ImageView ivDots;
	private XListView listView;

	private GoodsShoppingMallAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_shopingmall, null);
			rootView.findViewById(R.id.iv_barrow).setOnClickListener(this);
			layoutDots = (LinearLayout) rootView.findViewById(R.id.layout_dots);
			listView = (XListView) rootView.findViewById(R.id.listview);
			adapter = new GoodsShoppingMallAdapter(getActivity(), bmp,
					goodsLists);
			listView.setAdapter(adapter);
			listView.setXListViewListener(new IXListViewListener() {

				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub

				}

				//
				@Override
				public void onLoadMore() {
					// TODO Auto-generated method stub

				}
			});

			viewPager = (ViewPager) rootView.findViewById(R.id.pager);
			viewPager.setAdapter(new PagerAdapter() {
				// viewpager中的组件数量
				@Override
				public int getCount() {
					return viewContainter.size();
				}

				// 滑动切换的时候销毁当前的组件
				@Override
				public void destroyItem(ViewGroup container, int position,
						Object object) {
					((ViewPager) container).removeView(viewContainter
							.get(position));
				}

				// 每次滑动的时候生成的组件
				@Override
				public Object instantiateItem(ViewGroup container, int position) {
					((ViewPager) container).addView(viewContainter
							.get(position));
					return viewContainter.get(position);
				}

				@Override
				public boolean isViewFromObject(View arg0, Object arg1) {
					return arg0 == arg1;
				}

			});
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					ivDots.setImageResource(R.drawable.icon_pot_unselected);
					ivDots = (ImageView) layoutDots.getChildAt(position);
					ivDots.setImageResource(R.drawable.icon_pot_selected);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub

				}
			});

		}
		initData();
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;

	}

	private void initData() {
		ImageView iv;
		ImageView dot;
		for (int i = 0; i < 4; i++) {
			iv = new ImageView(getActivity());
			iv.setImageResource(R.drawable.pager1);
			iv.setScaleType(ScaleType.FIT_XY);
			viewContainter.add(iv);
			dot = new ImageView(getActivity());
			dot.setImageResource(R.drawable.icon_pot_unselected);
			layoutDots.addView(dot);
		}
		ivDots = (ImageView) layoutDots.getChildAt(0);
		ivDots.setImageResource(R.drawable.icon_pot_selected);
		viewPager.getAdapter().notifyDataSetChanged();
		for (int i = 0; i < 4; i++) {
			GoodsList item;
			item = new GoodsList();
			item.setPrice("189.00");
			item.setTitle("美国产安利蛋白粉 纽崔莱多种纯植物蛋白质粉");
			goodsLists.add(item);
			item = new GoodsList();
			item.setPrice("199.00");
			item.setTitle("herbalife美国产康宝莱奶昔快速减肥减重瘦身套餐 蛋白");
			goodsLists.add(item);
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(), BarrowActivity.class);
		startActivity(intent);

	}

}
