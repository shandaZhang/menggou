package com.fujianmenggou.fm;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.adapter.OrderAdapter;
import com.fujianmenggou.atv.AddressActivity;
import com.fujianmenggou.bean.OrderList;
import com.fujianmenggou.ckw.PullToRefreshBase;
import com.fujianmenggou.ckw.PullToRefreshBase.OnRefreshListener;
import com.fujianmenggou.ckw.PullToRefreshListView;
import com.fujianmenggou.util.BaseFragment;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;
import com.fujianmenggou.util.XListView;
import com.fujianmenggou.util.XListView.IXListViewListener;

import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class OrderFragment extends BaseFragment implements OnClickListener {

	private XListView listView;
	private static int[] statusAry = { 0, 1, 3, 5, 7 };
	private HashMap<String, ArrayList<OrderList>> orderMap = new HashMap<String, ArrayList<OrderList>>();
	private static int statusCode;// 标明是什么标签下的fragment
	private OrderAdapter adapter;
	private int pageIndex = 1;

	public static OrderFragment newInstance(int status) {
		OrderFragment fragment = new OrderFragment();
		statusCode = statusAry[status];
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// initData();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_order_manage, null);
		listView = (XListView) view.findViewById(R.id.listview);

		// listView.setAdapter(adapter);
		listView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				pageIndex = 1;
				getOrderInfo();

			}

			@Override
			public void onLoadMore() {
				pageIndex++;
				getOrderInfo();

			}
		});
		getOrderInfo();
		return view;

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onClick(View v) {

	}

	private void getOrderInfo() {

		// http://103.27.7.116:83/json/json.aspx?op=myOrders&user_id=465&status=1&pageSize=10&pageIndex=1
		Tools.ShowLoadingActivity(getActivity());
		AjaxParams params = new AjaxParams();
		params.put("op", "myOrders");
		params.put("user_id", userInfoPreferences.getString("id", ""));
		params.put("status", statusCode + "");
		params.put("pageSize", 10 + "");
		params.put("pageIndex", pageIndex + "");

		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
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
				// {"result":"1","totalcount":4,"list":[
				// {"id":59,"order_no":"160710143343092","buy_user_id":465,"sell_user_id":75,"address_id":3,"status":1,"goods_amount":9.00,"add_time":"2016-07-10T14:33:43.583","confirm_time":null,
				// "listOrder":[{"goods_id":2,"order_id":"160710143343092","name":"名表","pic":"/upload/201607/23/201607232249580502.jpg","describe":"展示商品时，商品的描述信息是必不可少的，是对图片信息的重要补充。大家都知道，图片传达给顾客的只是商品的形状、颜色、大小等信息，而关于商品的具体性能、具体尺寸、所用材料、原产地、售后服务保证等，就一定得通过文字作具体说明，这样顾客才能够清楚商品的功能，从而决定是否购买","quantiy":3,"real_amount":3.00,"order_amount":5.00}]},
				// {"id":58,"order_no":"160710143343151","buy_user_id":465,"sell_user_id":98,"address_id":3,"status":1,"goods_amount":2.00,"add_time":"2016-07-10T14:33:43.583","confirm_time":null,
				// "listOrder":[{"goods_id":1,"order_id":"160710143343151","name":"香蕉","pic":"/upload/201606/01/201606011104401384.jpg","describe":"展示商品时，商品的描述信息是必不可少的，是对图片信息的重要补充。大家都知道，图片传达给顾客的只是商品的形状、颜色、大小等信息，而关于商品的具体性能、具体尺寸、所用材料、原产地、售后服务保证等，就一定得通过文字作具体说明，这样顾客才能够清楚商品的功能，从而决定是否购买","quantiy":2,"real_amount":1.00,"order_amount":2.00}]},
				// {"id":57,"order_no":"160621231531382","buy_user_id":465,"sell_user_id":75,"address_id":3,"status":1,"goods_amount":9.00,"add_time":"2016-06-21T23:15:31.893","confirm_time":null,
				// "listOrder":[{"goods_id":2,"order_id":"160621231531382","name":"名表","pic":"/upload/201607/23/201607232249580502.jpg","describe":"展示商品时，商品的描述信息是必不可少的，是对图片信息的重要补充。大家都知道，图片传达给顾客的只是商品的形状、颜色、大小等信息，而关于商品的具体性能、具体尺寸、所用材料、原产地、售后服务保证等，就一定得通过文字作具体说明，这样顾客才能够清楚商品的功能，从而决定是否购买","quantiy":3,"real_amount":3.00,"order_amount":5.00}]},
				// {"id":56,"order_no":"160621231531911","buy_user_id":465,"sell_user_id":98,"address_id":3,"status":1,"goods_amount":2.00,"add_time":"2016-06-21T23:15:31.893","confirm_time":null,
				// "listOrder":[{"goods_id":1,"order_id":"160621231531911","name":"香蕉","pic":"/upload/201606/01/201606011104401384.jpg","describe":"展示商品时，商品的描述信息是必不可少的，是对图片信息的重要补充。大家都知道，图片传达给顾客的只是商品的形状、颜色、大小等信息，而关于商品的具体性能、具体尺寸、所用材料、原产地、售后服务保证等，就一定得通过文字作具体说明，这样顾客才能够清楚商品的功能，从而决定是否购买","quantiy":2,"real_amount":1.00,"order_amount":2.00}]}]}
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("result") == 1) {
						JSONArray list = obj.getJSONArray("list");

						for (int i = 0; i < list.length(); i++) {
							ArrayList<OrderList> orderLists = new ArrayList<OrderList>();
							JSONObject order = list.getJSONObject(i);
							JSONArray listOrder = order
									.getJSONArray("listOrder");
							int status = order.getInt("status");
							int addressId = order.getInt("address_id");
							for (int j = 0; j < listOrder.length(); j++) {
								JSONObject orderObj = listOrder
										.getJSONObject(j);
								OrderList item = new OrderList();
								item.setChecked(false);
								item.setDetail(orderObj.getString("describe"));
								item.setId(orderObj.getString("goods_id"));
								item.setNumber(orderObj.getInt("quantiy"));
								item.setPrice(orderObj.getDouble("real_amount"));
								item.setUrl(orderObj.getString("pic"));
								item.setTitle(orderObj.getString("name"));
								item.setStatus(status);
								item.setAddressId(addressId);
								orderLists.add(item);

							}
							orderMap.put(order.getString("order_no"),
									orderLists);
						}
						Log.e("menggou", "orderMap.size(): " + orderMap.size());
						adapter = new OrderAdapter(getActivity(), orderMap);
						listView.setAdapter(adapter);

					}

					else
						Tools.showTextToast(getActivity(), "获取订单列表失败");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void initData() {
		String[] orderId = new String[] { "00000000001", "00000000002",
				"00000000003", "00000000004" };
		ArrayList<ArrayList<OrderList>> list = new ArrayList<ArrayList<OrderList>>();
		for (int i = 0; i < orderId.length; i++) {
			ArrayList<OrderList> orderLists = new ArrayList<OrderList>();
			OrderList one = new OrderList();
			one.setChecked(false);
			one.setDetail("双黄香芋月饼 250克*1个 豆沙月饼 60克*2个 蓝莓味月饼 60克*2个 云腿月饼80克*3个");
			one.setTitle("潘祥记 七星伴月 广州团购批发香芋云腿椰蓉中秋月饼礼盒全国包邮");
			one.setPrice(168.00);
			one.setPriceAll(168.00 * 2);
			one.setNumber(2);
			orderLists.add(one);
			OrderList another = new OrderList();
			another.setChecked(false);
			another.setDetail("双黄香芋月饼 250克*1个 豆沙月饼 60克*2个 蓝莓味月饼 60克*2个 云腿月饼80克*3个");
			another.setTitle("潘祥记 七星伴月 广州团购批发香芋云腿椰蓉中秋月饼礼盒全国包邮");
			another.setPrice(168.00);
			another.setPriceAll(168.00 * 3);
			another.setNumber(3);
			orderLists.add(another);

			orderMap.put(orderId[i], orderLists);
		}

	}
}
