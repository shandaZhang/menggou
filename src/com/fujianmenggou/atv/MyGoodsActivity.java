package com.fujianmenggou.atv;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

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
import dujc.dtools.xutils.util.LogUtils;

public class MyGoodsActivity extends BaseActivity {
	private ArrayList<GoodsList> goodsLists = new ArrayList<GoodsList>();
	private XListView listView;
	private GoodsShoppingMallAdapter adapter;
	private int pageIndex = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_goods);
		initFakeTitle();
		setTitle("我的商品");
		listView = (XListView) findViewById(R.id.listview);
		adapter = new GoodsShoppingMallAdapter(this, bmp, goodsLists);
		listView.setAdapter(adapter);
		listView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				pageIndex = 1;
				getMyGoodsInfo();

			}

			//
			@Override
			public void onLoadMore() {
				pageIndex++;
				getMyGoodsInfo();

			}
		});
		getMyGoodsInfo();

	}

	private void initData() {
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

	private void getMyGoodsInfo() {
		// http://103.27.7.116:83/json/json.aspx?op=myShopGoods&user_id=98&pageSize=10&pageIndex=1&shop_id=0
		Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams();
		params.put("op", "myShopGoods");
		params.put("user_id", userInfoPreferences.getString("id", ""));
		params.put("pageSize", 10 + "");
		params.put("pageIndex", pageIndex + "");
		params.put("shop_id", 0 + "");
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
				listView.stopLoadMore();
				listView.stopRefresh();
			}

			// {"result":"1","totalcount":12,"list":[{"id":15,"name":"羽绒衣","price":110.00,"oldprice":220.00,"stock":10000,"user_id":98,"add_time":"2016-07-13T23:09:08","sort_id":1,"pic":"http://103.27.7.116:83/upload/201607/13/201607132312093782.png"},
			// {"id":12,"name":"测试8","price":46.00,"oldprice":56.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:29:32","sort_id":4,"pic":"http://103.27.7.116:83/upload/201606/24/201606240029282123.png"},
			// {"id":11,"name":"测试7","price":87.00,"oldprice":978.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:29:12.2","sort_id":5,"pic":"http://103.27.7.116:83/upload/201606/24/201606240029091518.png"},
			// {"id":10,"name":"测试7","price":111.00,"oldprice":154.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:28:52.273","sort_id":6,"pic":"http://103.27.7.116:83/upload/201606/24/201606240028494662.png"},
			// {"id":9,"name":"测试6","price":443.00,"oldprice":467.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:28:33.377","sort_id":7,"pic":"http://103.27.7.116:83/upload/201606/24/201606240028297446.png"},
			// {"id":8,"name":"测试5","price":77.00,"oldprice":87.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:28:13.4","sort_id":8,"pic":"http://103.27.7.116:83/upload/201606/24/201606240028097641.png"},
			// {"id":7,"name":"测试4","price":55.00,"oldprice":65.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:27:52.267","sort_id":9,"pic":"http://103.27.7.116:83/upload/201606/24/201606240027489321.png"},
			// {"id":6,"name":"测试3","price":33.00,"oldprice":53.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:27:32.66","sort_id":10,"pic":"http://103.27.7.116:83/upload/201606/24/201606240027256977.png"},
			// {"id":5,"name":"测试2","price":500.00,"oldprice":5.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:27:10.563","sort_id":11,"pic":"http://103.27.7.116:83/upload/201606/24/201606240027032192.png"},
			// {"id":4,"name":"测试1","price":33.00,"oldprice":2.00,"stock":10000,"user_id":98,"add_time":"2016-06-24T00:26:46.467","sort_id":12,"pic":"http://103.27.7.116:83/upload/201606/24/201606240026392201.png"}]}
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				listView.stopLoadMore();
				listView.stopRefresh();
				LogUtils.i(t);
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("result") == 1) {
						JSONArray array = obj.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							JSONObject goods = array.getJSONObject(i);
							GoodsList item = new GoodsList();
							item.setPrice(goods.getString("price"));
							item.setTitle(goods.getString("name"));
							item.setGoodsId(goods.getString("id"));
							item.setUrl(goods.getString("pic"));
							goodsLists.add(item);
						}
						adapter.notifyDataSetChanged();

					}

					else
						Tools.showTextToast(MyGoodsActivity.this, "获取我的商品列表失败 ");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

}
