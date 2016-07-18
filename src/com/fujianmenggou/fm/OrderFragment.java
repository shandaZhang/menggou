package com.fujianmenggou.fm;

import java.util.ArrayList;
import java.util.HashMap;

import com.fujianmenggou.R;
import com.fujianmenggou.adapter.OrderAdapter;
import com.fujianmenggou.bean.OrderList;
import com.fujianmenggou.ckw.PullToRefreshBase;
import com.fujianmenggou.ckw.PullToRefreshBase.OnRefreshListener;
import com.fujianmenggou.ckw.PullToRefreshListView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class OrderFragment extends Fragment implements OnClickListener {

	private PullToRefreshListView listView;
	private HashMap<String, ArrayList<OrderList>> orderMap = new HashMap<String, ArrayList<OrderList>>();
	private int position;// 标明是什么标签下的fragment

	public static OrderFragment newInstance(int position) {
		OrderFragment fragment = new OrderFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		getOrderInfo();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_order_manage, null);
		listView = (PullToRefreshListView) view.findViewById(R.id.listview);
		OrderAdapter adapter = new OrderAdapter(getActivity(), orderMap);
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				Log.e("mengou", "onRefresh");
			}
		});
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
