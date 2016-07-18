package com.fujianmenggou.atv;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

import com.fujianmenggou.R;
import com.fujianmenggou.adapter.GoodsShoppingMallAdapter;
import com.fujianmenggou.bean.GoodsList;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.XListView;
import com.fujianmenggou.util.XListView.IXListViewListener;

public class ShopPreViewActivity extends BaseActivity {

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_preview);
		initFakeTitle();
		setTitle("店铺预览");
		layoutDots = (LinearLayout) findViewById(R.id.layout_dots);
		listView = (XListView) findViewById(R.id.listview);
		adapter = new GoodsShoppingMallAdapter(this, bmp, goodsLists);
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

		viewPager = (ViewPager) findViewById(R.id.pager);
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
				((ViewPager) container).removeView(viewContainter.get(position));
			}

			// 每次滑动的时候生成的组件
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				((ViewPager) container).addView(viewContainter.get(position));
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
		initData();

	}

	private void initData() {
		ImageView iv;
		ImageView dot;
		for (int i = 0; i < 2; i++) {
			iv = new ImageView(this);
			iv.setImageResource(R.drawable.pager2);
			iv.setScaleType(ScaleType.FIT_XY);
			viewContainter.add(iv);
			dot = new ImageView(this);
			dot.setImageResource(R.drawable.icon_pot_unselected);
			layoutDots.addView(dot);
		}
		ivDots = (ImageView) layoutDots.getChildAt(0);
		ivDots.setImageResource(R.drawable.icon_pot_selected);
		viewPager.getAdapter().notifyDataSetChanged();
		for (int i = 0; i < 4; i++) {
			GoodsList item;
			item = new GoodsList();
			item.setPrice("25.00");
			item.setTitle("【果美美】智利黑布林李子1kg");
			goodsLists.add(item);
			item = new GoodsList();
			item.setPrice("43.50");
			item.setTitle("果美美台湾新鲜欂栌2个/凤梨");
			goodsLists.add(item);
		}

	}

}
