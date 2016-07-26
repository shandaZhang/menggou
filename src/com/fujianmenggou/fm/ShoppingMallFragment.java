package com.fujianmenggou.fm;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.fujianmenggou.R;
import com.fujianmenggou.adapter.GoodsShoppingMallAdapter;
import com.fujianmenggou.atv.BarrowActivity;
import com.fujianmenggou.bean.GoodsList;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;
import com.fujianmenggou.util.XListView;
import com.fujianmenggou.util.XListView.IXListViewListener;

import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.bitmap.BitmapCommonUtils;
import dujc.dtools.xutils.bitmap.BitmapDisplayConfig;
import dujc.dtools.xutils.bitmap.core.BitmapDecoder;
import dujc.dtools.xutils.bitmap.factory.BitmapFactory;
import dujc.dtools.xutils.util.LogUtils;

public class ShoppingMallFragment extends ViewPagerFragment implements
		OnClickListener {
	private View rootView;// 缓存的界面
	private ViewPager viewPager;
	private ArrayList<GoodsList> goodsLists = new ArrayList<GoodsList>();
	// private ArrayList<ImageView> dots = new ArrayList<ImageView>();
	private ArrayList<ImageView> viewContainter = new ArrayList<ImageView>();
	private LinearLayout layoutDots;
	private ImageView ivDots;
	private XListView listView;
	private EditText etSearch;
	private int pageIndex = 1;
	private BitmapDisplayConfig displayConfig;

	private GoodsShoppingMallAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_shopingmall, null);
			rootView.findViewById(R.id.iv_barrow).setOnClickListener(this);
			layoutDots = (LinearLayout) rootView.findViewById(R.id.layout_dots);
			etSearch = (EditText) rootView.findViewById(R.id.et_search);
			etSearch.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_ENTER) {
						((InputMethodManager) getActivity().getSystemService(
								Context.INPUT_METHOD_SERVICE))
								.hideSoftInputFromWindow(getActivity()
										.getCurrentFocus().getWindowToken(),
										InputMethodManager.HIDE_NOT_ALWAYS);
						pageIndex = 1;
						getShopMallInfo();
						return true;
					}

					return false;
				}
			});
			listView = (XListView) rootView.findViewById(R.id.listview);
			adapter = new GoodsShoppingMallAdapter(getActivity(), bmp,
					goodsLists);
			listView.setAdapter(adapter);
			listView.setXListViewListener(new IXListViewListener() {

				@Override
				public void onRefresh() {
					pageIndex = 1;
					getShopMallInfo();

				}

				//
				@Override
				public void onLoadMore() {
					pageIndex++;
					getShopMallInfo();

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
					Log.e("menggou", "destory item");
					((ViewPager) container).removeView(viewContainter
							.get(position));
				}

				// 每次滑动的时候生成的组件
				@Override
				public Object instantiateItem(ViewGroup container, int position) {
					Log.e("menggou", "instantiateItem");
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
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}

		displayConfig = new BitmapDisplayConfig();
		// displayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用,
		// 图片太大时容易OOM。
		displayConfig
				.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(context));
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(500);
		displayConfig.setAnimation(animation);

		layzyLoad();
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

	public void getShopMallInfo() {
		Tools.ShowLoadingActivity(context);
		// http://103.27.7.116:83/json/json.aspx?op=allGoods
		// &pageSize=10&pageIndex=1
		AjaxParams params = new AjaxParams();
		params.put("op", "allGoods");
		params.put("pageSize", "10");
		params.put("pageIndex", pageIndex + "");
		params.put("GoodsName", etSearch.getText().toString());

		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
				listView.stopLoadMore();
				listView.stopRefresh();
			}

			// {"result":"1","totalcount":15,
			// "shoplist":[{"user_id":null,"thumb_path":"http://103.27.7.116:83/upload/201607/17/201607172345389544.png"},
			// {"user_id":null,"thumb_path":"http://103.27.7.116:83/upload/201607/18/201607181817424838.jpg"}],
			// "list":[{"id":15,"name":"羽绒衣","price":110.00,"oldprice":220.00,"stock":10000,"user_id":98,"add_time":"2016-07-13T23:09:08","sort_id":1,"pic":"http://103.27.7.116:83/upload/201607/13/201607132312093782.png"},
			// {"id":14,"name":"尤溪土鸡","price":150.00,"oldprice":200.00,"stock":20,"user_id":75,"add_time":"2016-07-05T00:15:57","sort_id":2,"pic":"http://103.27.7.116:83/upload/201607/14/201607141517351224.jpg"},
			// {"id":13,"name":"本鑫","price":2.00,"oldprice":2.00,"stock":10000,"user_id":75,"add_time":"2016-07-05T00:15:56","sort_id":3,"pic":"http://103.27.7.116:83/upload/201607/13/201607132312093782.png"},
			// {"id":12,"name":"测试8","price":46.00,"oldprice":56.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:29:32","sort_id":4,"pic":"http://103.27.7.116:83/upload/201606/24/201606240029282123.png"},
			// {"id":11,"name":"测试7","price":87.00,"oldprice":978.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:29:12.2","sort_id":5,"pic":"http://103.27.7.116:83/upload/201606/24/201606240029091518.png"},
			// {"id":10,"name":"测试7","price":111.00,"oldprice":154.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:28:52.273","sort_id":6,"pic":"http://103.27.7.116:83/upload/201606/24/201606240028494662.png"},
			// {"id":9,"name":"测试6","price":443.00,"oldprice":467.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:28:33.377","sort_id":7,"pic":"http://103.27.7.116:83/upload/201606/24/201606240028297446.png"},
			// {"id":8,"name":"测试5","price":77.00,"oldprice":87.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:28:13.4","sort_id":8,"pic":"http://103.27.7.116:83/upload/201606/24/201606240028097641.png"},
			// {"id":7,"name":"测试4","price":55.00,"oldprice":65.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:27:52.267","sort_id":9,"pic":"http://103.27.7.116:83/upload/201606/24/201606240027489321.png"},
			// {"id":6,"name":"测试3","price":33.00,"oldprice":53.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:27:32.66","sort_id":10,"pic":"http://103.27.7.116:83/upload/201606/24/201606240027256977.png"}]}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				listView.stopLoadMore();
				listView.stopRefresh();
				LogUtils.i(t);
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("result") == 1) {
						// 店铺轮播图列表
						JSONArray shoplist = obj.getJSONArray("shoplist");
						Log.e("menggou", "开始处理店铺轮播图");
						for (int i = 0; i < shoplist.length(); i++) {
							Log.e("menggou", "i : " + i);
							JSONObject shopImg = shoplist.getJSONObject(i);
							ImageView iv = new ImageView(getActivity());
							iv.setScaleType(ScaleType.FIT_XY);
							viewContainter.add(iv);
							Log.e("menggou", "step 0");
							Log.e("menggou",
									"iv:" + iv + " url:"
											+ shopImg.getString("thumb_path")
											+ " disconfig:" + displayConfig);
							// bmp.display(iv, shopImg.getString("thumb_path"),
							// displayConfig);
							//bmp.display(iv, shopImg.getString("thumb_path"));
							Glide.with(context).load(shopImg.getString("thumb_path")).into(iv);
							Log.e("menggou", "step 1");
							ImageView dot = new ImageView(getActivity());
							dot.setImageResource(R.drawable.icon_pot_unselected);
							layoutDots.addView(dot);
						}
						Log.e("menggou", "开始处理轮播图下面的点");
						ivDots = (ImageView) layoutDots.getChildAt(0);
						ivDots.setImageResource(R.drawable.icon_pot_selected);

						Log.e("menggou", "开始处理商品列表");
						// 商品列表
						JSONArray list = obj.getJSONArray("list");
						if (pageIndex == 1)
							goodsLists.clear();
						for (int i = 0; i < list.length(); i++) {
							JSONObject goodsObj = list.getJSONObject(i);
							GoodsList goods = new GoodsList();
							goods.setGoodsId(goodsObj.getString("id"));
							goods.setPrice(goodsObj.getString("price"));
							goods.setTitle(goodsObj.getString("name"));
							goods.setUrl(goodsObj.getString("pic"));
							goodsLists.add(goods);
						}
						Log.e("menggou", "获取的回报处理了，等待界面更新");
						adapter.notifyDataSetChanged();
						viewPager.getAdapter().notifyDataSetChanged();

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	protected void layzyLoad() {
		if (rootView != null && isVisible) {
			getShopMallInfo();
		}
	}
}
