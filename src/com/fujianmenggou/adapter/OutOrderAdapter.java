package com.fujianmenggou.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.fujianmenggou.R;
import com.fujianmenggou.adapter.AdapterWithSpace.ViewHolder;
import com.fujianmenggou.bean.OrderList;
import com.fujianmenggou.util.XListView;

import dujc.dtools.FinalHttp;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class OutOrderAdapter extends BaseAdapter {

	private LinkedHashMap<String, ArrayList<OrderList>> orderMap;
	private Context context;
	private ArrayList<String> orderIdList;
	protected FinalHttp http;

	public OutOrderAdapter(Context context,
			LinkedHashMap<String, ArrayList<OrderList>> orderMap) {
		this.context = context;
		this.orderMap = orderMap;
		orderIdList = new ArrayList<String>(orderMap.keySet());
		http = new FinalHttp();
	}

	@Override
	public int getCount() {
		return orderMap.size();
	}

	@Override
	public Object getItem(int position) {
		return orderMap.get(orderIdList);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.order_manage_item, null);
			holder = new ViewHolder();
			holder.lsv = (XListView) convertView.findViewById(R.id.lsv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
//		holder.lsv.setAdapter(adapter);
		
		
		return convertView;
	}

	class ViewHolder {
		private XListView lsv;
	}

}
