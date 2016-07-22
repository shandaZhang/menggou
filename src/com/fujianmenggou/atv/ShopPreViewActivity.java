package com.fujianmenggou.atv;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.fujianmenggou.R;
import com.fujianmenggou.adapter.GoodsShoppingMallAdapter;
import com.fujianmenggou.bean.GoodsList;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;
import com.fujianmenggou.util.XListView;
import com.fujianmenggou.util.XListView.IXListViewListener;

import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.bitmap.BitmapDisplayConfig;
import dujc.dtools.xutils.util.LogUtils;

public class ShopPreViewActivity extends BaseActivity {

	private ViewPager viewPager;
	private ArrayList<GoodsList> goodsLists = new ArrayList<GoodsList>();
	private ArrayList<ImageView> viewContainter = new ArrayList<ImageView>();
	private LinearLayout layoutDots;
	private ImageView ivDots;
	private XListView listView;
	private GoodsShoppingMallAdapter adapter;
	private int pageIndex = 1;

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
		// initData();
		getShopPreviewInfo();

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

	public void getShopPreviewInfo() {
		Tools.ShowLoadingActivity(context);
		// http://103.27.7.116:83/json/json.aspx?op=myShopGoods&user_id=98&pageSize=10&pageIndex=1&shop_id=1
		AjaxParams params = new AjaxParams();
		params.put("op", "myShopGoods");
		params.put("user_id", userInfoPreferences.getString("uid", ""));
		params.put("shop_id", "1");
		params.put("pageSize", "10");
		params.put("pageIndex", pageIndex + "");

		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			private BitmapDisplayConfig displayConfig;

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
				listView.stopLoadMore();
				listView.stopRefresh();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				listView.stopLoadMore();
				listView.stopRefresh();
				LogUtils.i(t);

				// {"result":"1","totalcount":12,"shoplist":
				// ["/upload/201606/07/201606070043074121.png","美美水果店",
				// [{"id":16,"user_id":98,"article_id":1,"thumb_path":"/upload/201606/07/thumb_201606070043204599.png","original_path":"/upload/201606/07/201606070043204599.png","remark":null,"add_time":"2016-07-21T15:36:07.7445547+08:00"},
				// {"id":17,"user_id":98,"article_id":1,"thumb_path":"/upload/201606/07/thumb_201606070043235185.png","original_path":"/upload/201606/07/201606070043235185.png","remark":null,"add_time":"2016-07-21T15:36:07.7445547+08:00"}]]
				// ,"list":[{"id":15,"name":"羽绒衣","price":110.00,"oldprice":220.00,"stock":10000,"user_id":98,"add_time":"2016-07-13T23:09:08","sort_id":1,"pic":"http://103.27.7.116:83/upload/201607/13/201607132312093782.png"}
				// ,{"id":12,"name":"测试8","price":46.00,"oldprice":56.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:29:32","sort_id":4,"pic":"http://103.27.7.116:83/upload/201606/24/201606240029282123.png"},
				// {"id":11,"name":"测试7","price":87.00,"oldprice":978.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:29:12.2","sort_id":5,"pic":"http://103.27.7.116:83/upload/201606/24/201606240029091518.png"},
				// {"id":10,"name":"测试7","price":111.00,"oldprice":154.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:28:52.273","sort_id":6,"pic":"http://103.27.7.116:83/upload/201606/24/201606240028494662.png"},{"id":9,"name":"测试6","price":443.00,"oldprice":467.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:28:33.377","sort_id":7,"pic":"http://103.27.7.116:83/upload/201606/24/201606240028297446.png"},{"id":8,"name":"测试5","price":77.00,"oldprice":87.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:28:13.4","sort_id":8,"pic":"http://103.27.7.116:83/upload/201606/24/201606240028097641.png"},{"id":7,"name":"测试4","price":55.00,"oldprice":65.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:27:52.267","sort_id":9,"pic":"http://103.27.7.116:83/upload/201606/24/201606240027489321.png"},{"id":6,"name":"测试3","price":33.00,"oldprice":53.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:27:32.66","sort_id":10,"pic":"http://103.27.7.116:83/upload/201606/24/201606240027256977.png"},{"id":5,"name":"测试2","price":500.00,"oldprice":5.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:27:10.563","sort_id":11,"pic":"http://103.27.7.116:83/upload/201606/24/201606240027032192.png"},{"id":4,"name":"测试1","price":33.00,"oldprice":2.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:26:46.467","sort_id":12,"pic":"http://103.27.7.116:83/upload/201606/24/201606240026392201.png"}]}
				// Shoplist(1,店铺图片2,店铺名称3,店铺滚动图片) 店铺数据集
				// list 商品集合
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("result") == 1) {
						// 店铺轮播图列表
						JSONArray shoplist = obj.getJSONArray("shoplist");
						TextView tvShopName = (TextView) findViewById(R.id.tv_shop_name);
						ImageView ivShopIcon = (ImageView) findViewById(R.id.iv_shop_icon);
						bmp.display(ivShopIcon,
								GlobalVars.baseUrl + shoplist.getString(0),
								displayConfig);
						tvShopName.setText(shoplist.getString(1));
						for (int i = 0; i < shoplist.getJSONArray(2).length(); i++) {
							JSONObject shopImg = shoplist.getJSONArray(2)
									.getJSONObject(i);
							ImageView iv = new ImageView(
									ShopPreViewActivity.this);
							iv.setScaleType(ScaleType.FIT_XY);
							viewContainter.add(iv);
							bmp.display(iv, shopImg.getString("thumb_path"),
									displayConfig);
							ImageView dot = new ImageView(
									ShopPreViewActivity.this);
							dot.setImageResource(R.drawable.icon_pot_unselected);
							layoutDots.addView(dot);
						}
						ivDots = (ImageView) layoutDots.getChildAt(0);
						ivDots.setImageResource(R.drawable.icon_pot_selected);

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
						adapter.notifyDataSetChanged();
						viewPager.getAdapter().notifyDataSetChanged();

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

}
