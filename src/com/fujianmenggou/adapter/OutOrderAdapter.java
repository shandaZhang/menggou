package com.fujianmenggou.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.fujianmenggou.R;
import com.fujianmenggou.atv.GoodsAssesmentActivity;
import com.fujianmenggou.bean.OrderList;
import com.fujianmenggou.fm.GetInnerListDataListener;
import com.fujianmenggou.fm.OrderManageOperator;
import com.fujianmenggou.util.XListView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class OutOrderAdapter extends BaseAdapter {

	private LinkedHashMap<String, ArrayList<OrderList>> orderMap;
	private Context context;
	private ArrayList<String> orderIdList;
	private OrderManageOperator operator;

	public OutOrderAdapter(Context context,
			LinkedHashMap<String, ArrayList<OrderList>> orderMap,
			OrderManageOperator operator) {
		this.context = context;
		this.orderMap = orderMap;
		orderIdList = new ArrayList<String>(orderMap.keySet());
		this.operator = operator;
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
		int status = 0;
		double moneyPay = 0;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.order_manage_item, null);
			holder = new ViewHolder();
			holder.lsv = (ListView) convertView.findViewById(R.id.lsv);
			holder.tvOrderId = (TextView) convertView
					.findViewById(R.id.order_number);
			holder.tvGoodsAccount = (TextView) convertView
					.findViewById(R.id.tv_goods_accout);
			holder.tvMoneyPay = (TextView) convertView
					.findViewById(R.id.tv_money_pay);
			holder.btnLeft = (Button) convertView
					.findViewById(R.id.btn_del_order);
			holder.btnRight = (Button) convertView.findViewById(R.id.btn_pay);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		GetInnerListDataListener listener = new GetInnerListDataListener(holder) {

			@Override
			public void updateView(int status, double moneyPay) {
				holder.tvMoneyPay.setText("¥" + moneyPay);
				setBtnTextAndClicker(holder.btnLeft, holder.btnRight, status,
						orderIdList.get(this.position));
			}

		};
		ArrayList<OrderList> orderLists = orderMap.get(orderIdList
				.get(position));
		InOrderAdapter adapter = new InOrderAdapter(context, orderLists);
		holder.lsv.setAdapter(adapter);
		holder.tvOrderId.setText(orderIdList.get(position));
		holder.tvGoodsAccount.setText("共" + orderLists.size() + "件商品");
		for (OrderList item : orderLists) {
			status = item.getStatus();
			moneyPay += item.getPrice() * item.getNumber();

		}
		holder.tvMoneyPay.setText("¥" + moneyPay);
		setBtnTextAndClicker(holder.btnLeft, holder.btnRight, status,
				orderIdList.get(position));

		return convertView;
	}

	class LeftBtnClickListener implements OnClickListener {
		private String orderId;

		public LeftBtnClickListener(String orderId) {
			this.orderId = orderId;
		}

		@Override
		public void onClick(View v) {
			Log.e("mengou", "delete click");
			orderMap.remove(orderId);
			orderIdList.remove(orderId);
			notifyDataSetChanged();

		}

	}

	class RightBtnClickListener implements OnClickListener {
		private String orderId;
		private int status;

		public RightBtnClickListener(String orderId, int status) {
			this.orderId = orderId;
			this.status = status;
		}

		@Override
		public void onClick(View v) {
			switch (status) {
			case 1:
				operator.buy(orderId);
				break;
			case 5:
				operator.receiveGoods(orderId);
				break;
			case 7:
				ArrayList<OrderList> data = orderMap.get(orderId);
				Intent intent = new Intent(context,
						GoodsAssesmentActivity.class);
				intent.putExtra("orderId", orderId);
				intent.putExtra("data", data);
				context.startActivity(intent);
				break;

			}

		}
	}

	private void setBtnTextAndClicker(Button btnLeft, Button btnRight,
			int status, String orderId) {
		switch (status) {
		// 待付款
		case 1:
			btnLeft.setOnClickListener(new LeftBtnClickListener(orderId));
			btnRight.setOnClickListener(new RightBtnClickListener(orderId, 1));
			break;
		// 待发货
		case 3:
			btnLeft.setVisibility(View.GONE);
			btnRight.setVisibility(View.GONE);
			break;
		// 待收货
		case 5:
			btnLeft.setVisibility(View.GONE);
			btnRight.setText("确认收货");
			btnLeft.setOnClickListener(new RightBtnClickListener(orderId, 5));
			break;
		// 待评价
		case 7:
			btnLeft.setVisibility(View.GONE);
			btnRight.setText("评价");
			btnLeft.setOnClickListener(new RightBtnClickListener(orderId, 7));
			break;

		}

	}

	public class ViewHolder {
		private ListView lsv;
		private TextView tvOrderId;
		private TextView tvGoodsAccount;
		private TextView tvMoneyPay;
		private Button btnLeft;
		private Button btnRight;
	}

}
