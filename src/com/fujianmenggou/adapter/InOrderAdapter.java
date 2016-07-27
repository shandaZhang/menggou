package com.fujianmenggou.adapter;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.fujianmenggou.R;
import com.fujianmenggou.bean.OrderList;
import com.fujianmenggou.fm.GetInnerListDataListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InOrderAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<OrderList> orderLists;

	public InOrderAdapter(Context context, ArrayList<OrderList> orderLists) {
		this.context = context;
		this.orderLists = orderLists;
	}

	@Override
	public int getCount() {
		return orderLists.size();
	}

	@Override
	public Object getItem(int position) {
		return orderLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.goods_order_item, null);
			holder = new ViewHolder();

			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_goods_title);
			holder.tvPrice = (TextView) convertView
					.findViewById(R.id.tv_goods_price);
			holder.tvPriceAll = (TextView) convertView
					.findViewById(R.id.tv_price_all);
			holder.tvDetail = (TextView) convertView
					.findViewById(R.id.tv_goods_detail);
			holder.cbIsChecked = (CheckBox) convertView.findViewById(R.id.cb);
		} else
			holder = (ViewHolder) convertView.getTag();
		OrderList data = orderLists.get(position);
		holder.tvDetail.setText(data.getDetail());
		holder.tvTitle.setText(data.getTitle());
		holder.tvPrice.setText("¥" + data.getPrice() + "x" + data.getNumber());
		holder.tvPriceAll.setText(data.getPrice() * data.getNumber() + "");
		holder.cbIsChecked.setChecked(data.isChecked());
		// holder.ivGoods.setImageResource(R.drawable.goods);
		Glide.with(context).load(data.getUrl()).into(holder.ivGoods);
		return convertView;
	}

	class ViewHolder {
		private TextView tvTitle;
		private TextView tvDetail;
		private CheckBox cbIsChecked;
		private TextView tvPrice;
		private TextView tvPriceAll;
		private ImageView ivGoods;

	}

}
