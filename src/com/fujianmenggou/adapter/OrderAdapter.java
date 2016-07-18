package com.fujianmenggou.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.fujianmenggou.R;
import com.fujianmenggou.bean.OrderList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderAdapter extends BaseAdapter {
	private HashMap<String, ArrayList<OrderList>> orderMap;
	private Context context;
	private ArrayList<String> orderIdList;

	public OrderAdapter(Context context,
			HashMap<String, ArrayList<OrderList>> orderMap) {
		this.context = context;
		this.orderMap = orderMap;
		orderIdList = new ArrayList<String>(orderMap.keySet());
	}

	@Override
	public int getCount() {
		return orderMap.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		ArrayList<OrderList> orderLists = orderMap.get(orderIdList
				.get(position));
		double moneyPay = 0;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.order_mange_item, null);
			LinearLayout layoutGoods = (LinearLayout) convertView
					.findViewById(R.id.layout_goods);
			holder.tvTitles = new ArrayList<TextView>();
			holder.tvPrices = new ArrayList<TextView>();
			holder.tvPriceAlls = new ArrayList<TextView>();
			holder.tvDetails = new ArrayList<TextView>();
			holder.cbIsChecked = new ArrayList<CheckBox>();
			holder.ivGoods = new ArrayList<ImageView>();
			for (int i = 0; i < orderLists.size(); i++) {
				LinearLayout goods = (LinearLayout) LayoutInflater
						.from(context).inflate(R.layout.goods_order_item, null);
				View dividor = new View(context);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, 1);
				float density = context.getResources().getDisplayMetrics().density;
				params.bottomMargin = (int) (5 * density);
				params.topMargin = (int) (5 * density);
				dividor.setLayoutParams(params);
				dividor.setBackgroundResource(R.color.hui_gray);

				goods.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				holder.tvTitles.add((TextView) goods
						.findViewById(R.id.tv_goods_title));
				holder.tvPrices.add((TextView) goods
						.findViewById(R.id.tv_goods_price));
				holder.tvPriceAlls.add((TextView) goods
						.findViewById(R.id.tv_price_all));
				holder.tvDetails.add((TextView) goods
						.findViewById(R.id.tv_goods_detail));
				holder.cbIsChecked.add((CheckBox) goods.findViewById(R.id.cb));
				View layout1 = goods.findViewById(R.id.layout1);
				View layout2 = layout1.findViewById(R.id.layout2);
				View iv = layout1.findViewById(R.id.iv_goods);

				holder.ivGoods.add((ImageView) layout2
						.findViewById(R.id.iv_goods));

				layoutGoods.addView(goods);
				if (i < orderLists.size() - 1) {
					layoutGoods.addView(dividor);
				}

			}
			holder.tvOrderId = (TextView) convertView
					.findViewById(R.id.order_number);
			holder.tvGoodsAccount = (TextView) convertView
					.findViewById(R.id.tv_goods_accout);
			holder.tvMoneyPay = (TextView) convertView
					.findViewById(R.id.tv_money_pay);
			holder.btnDelete = (Button) convertView
					.findViewById(R.id.btn_del_order);
			holder.btnPay = (Button) convertView.findViewById(R.id.btn_pay);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		for (int i = 0; i < orderLists.size(); i++) {
			OrderList data = orderLists.get(i);
			holder.tvDetails.get(i).setText(data.getDetail());
			holder.tvTitles.get(i).setText(data.getTitle());
			holder.tvPrices.get(i).setText(
					"¥" + data.getPrice() + "x" + data.getNumber());
			holder.tvPriceAlls.get(i).setText(
					data.getPrice() * data.getNumber() + "");
			holder.cbIsChecked.get(i).setChecked(data.isChecked());
			holder.ivGoods.get(i).setImageResource(R.drawable.goods);
			moneyPay += data.getPriceAll();
		}
		holder.tvOrderId.setText(orderIdList.get(position));
		holder.tvGoodsAccount.setText("共" + orderLists.size() + "件商品");
		holder.tvMoneyPay.setText("¥" + moneyPay);
		holder.btnDelete.setOnClickListener(new DeleteClickListener(orderIdList
				.get(position)));
		holder.btnPay.setOnClickListener(new PayClickListener());

		return convertView;
	}

	class ViewHolder {
		private ArrayList<TextView> tvTitles;
		private ArrayList<TextView> tvDetails;
		private ArrayList<CheckBox> cbIsChecked;
		private ArrayList<TextView> tvPrices;
		private ArrayList<TextView> tvPriceAlls;
		private ArrayList<ImageView> ivGoods;
		private TextView tvOrderId;
		private TextView tvGoodsAccount;
		private TextView tvMoneyPay;
		private Button btnDelete;
		private Button btnPay;
	}

	class DeleteClickListener implements OnClickListener {
		private String tvOrderId;

		public DeleteClickListener(String tvOrderId) {
			this.tvOrderId = tvOrderId;
		}

		@Override
		public void onClick(View v) {
			Log.e("mengou", "delete click");
			orderMap.remove(tvOrderId);
			orderIdList.remove(tvOrderId);
			notifyDataSetChanged();

		}

	}

	class PayClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Log.e("mengou", "pay clicked");

		}

	}

}
