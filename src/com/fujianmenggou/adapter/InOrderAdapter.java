package com.fujianmenggou.adapter;

import java.util.ArrayList;



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
	private GetInnerListDataListener listener;
	private int status = 0;

	public InOrderAdapter(Context context, ArrayList<OrderList> orderLists,
			GetInnerListDataListener listener) {
		this.context = context;
		this.orderLists = orderLists;
		this.listener = listener;
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
		status = data.getStatus();
//		holder.tvDetails.get(i).setText(data.getDetail());
//		holder.tvTitles.get(i).setText(data.getTitle());
//		holder.tvPrices.get(i).setText(
//				"¥" + data.getPrice() + "x" + data.getNumber());
//		holder.tvPriceAlls.get(i).setText(
//				data.getPrice() * data.getNumber() + "");
//		holder.cbIsChecked.get(i).setChecked(data.isChecked());
//		holder.ivGoods.get(i).setImageResource(R.drawable.goods);
//		moneyPay += data.getPriceAll();
		

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
