package com.fujianmenggou.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.atv.GoodsAssesmentActivity;
import com.fujianmenggou.bean.OrderList;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import dujc.dtools.FinalHttp;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;
import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
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
	protected FinalHttp http;

	public OrderAdapter(Context context,
			HashMap<String, ArrayList<OrderList>> orderMap) {
		this.context = context;
		this.orderMap = orderMap;
		Log.e("menggou", "ordermao,size: " + orderMap.size());
		orderIdList = new ArrayList<String>(orderMap.keySet());
		http = new FinalHttp();
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

	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// ViewHolder holder;
	//
	// ArrayList<OrderList> orderLists = orderMap.get(orderIdList
	// .get(position));
	//
	// int status = 0;
	//
	// double moneyPay = 0;
	// if (convertView == null) {
	// holder = new ViewHolder();
	// convertView = LayoutInflater.from(context).inflate(
	// R.layout.order_mange_item, null);
	// LinearLayout layoutGoods = (LinearLayout) convertView
	// .findViewById(R.id.layout_goods);
	// holder.tvTitles = new ArrayList<TextView>();
	// holder.tvPrices = new ArrayList<TextView>();
	// holder.tvPriceAlls = new ArrayList<TextView>();
	// holder.tvDetails = new ArrayList<TextView>();
	// holder.cbIsChecked = new ArrayList<CheckBox>();
	// holder.ivGoods = new ArrayList<ImageView>();
	// for (int i = 0; i < orderLists.size(); i++) {
	// LinearLayout goods = (LinearLayout) LayoutInflater
	// .from(context).inflate(R.layout.goods_order_item, null);
	// View dividor = new View(context);
	// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.MATCH_PARENT, 1);
	// float density = context.getResources().getDisplayMetrics().density;
	// params.bottomMargin = (int) (5 * density);
	// params.topMargin = (int) (5 * density);
	// dividor.setLayoutParams(params);
	// dividor.setBackgroundResource(R.color.hui_gray);
	//
	// goods.setLayoutParams(new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.MATCH_PARENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT));
	// holder.tvTitles.add((TextView) goods
	// .findViewById(R.id.tv_goods_title));
	// holder.tvPrices.add((TextView) goods
	// .findViewById(R.id.tv_goods_price));
	// holder.tvPriceAlls.add((TextView) goods
	// .findViewById(R.id.tv_price_all));
	// holder.tvDetails.add((TextView) goods
	// .findViewById(R.id.tv_goods_detail));
	// holder.cbIsChecked.add((CheckBox) goods.findViewById(R.id.cb));
	// View layout1 = goods.findViewById(R.id.layout1);
	// View layout2 = layout1.findViewById(R.id.layout2);
	// View iv = layout1.findViewById(R.id.iv_goods);
	//
	// holder.ivGoods.add((ImageView) layout2
	// .findViewById(R.id.iv_goods));
	//
	// layoutGoods.addView(goods);
	// if (i < orderLists.size() - 1) {
	// layoutGoods.addView(dividor);
	// }
	//
	// }
	// holder.tvOrderId = (TextView) convertView
	// .findViewById(R.id.order_number);
	// holder.tvGoodsAccount = (TextView) convertView
	// .findViewById(R.id.tv_goods_accout);
	// holder.tvMoneyPay = (TextView) convertView
	// .findViewById(R.id.tv_money_pay);
	// holder.btnLeft = (Button) convertView
	// .findViewById(R.id.btn_del_order);
	// holder.btnRight = (Button) convertView.findViewById(R.id.btn_pay);
	// convertView.setTag(holder);
	//
	// } else {
	// holder = (ViewHolder) convertView.getTag();
	// }
	// for (int i = 0; i < orderLists.size(); i++) {
	// OrderList data = orderLists.get(i);
	// status = data.getStatus();
	// holder.tvDetails.get(i).setText(data.getDetail());
	// holder.tvTitles.get(i).setText(data.getTitle());
	// holder.tvPrices.get(i).setText(
	// "¥" + data.getPrice() + "x" + data.getNumber());
	// holder.tvPriceAlls.get(i).setText(
	// data.getPrice() * data.getNumber() + "");
	// holder.cbIsChecked.get(i).setChecked(data.isChecked());
	// holder.ivGoods.get(i).setImageResource(R.drawable.goods);
	// moneyPay += data.getPriceAll();
	// }
	// holder.tvOrderId.setText(orderIdList.get(position));
	// holder.tvGoodsAccount.setText("共" + orderLists.size() + "件商品");
	// holder.tvMoneyPay.setText("¥" + moneyPay);
	//
	// setBtnTextAndClicker(holder.btnLeft, holder.btnRight, status,
	// orderIdList.get(position));
	// // holder.btnLeft.setOnClickListener(new
	// // LeftBtnClickListener(orderIdList
	// // .get(position)));
	// // holder.btnRight.setOnClickListener(new RightBtnClickListener());
	//
	// return convertView;
	// }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		ArrayList<OrderList> orderLists = orderMap.get(orderIdList
				.get(position));

		int status = 0;

		double moneyPay = 0;

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
			LinearLayout goods = (LinearLayout) LayoutInflater.from(context)
					.inflate(R.layout.goods_order_item, null);
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

			holder.ivGoods.add((ImageView) layout2.findViewById(R.id.iv_goods));

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
		holder.btnLeft = (Button) convertView.findViewById(R.id.btn_del_order);
		holder.btnRight = (Button) convertView.findViewById(R.id.btn_pay);
		convertView.setTag(holder);

		for (int i = 0; i < orderLists.size(); i++) {
			OrderList data = orderLists.get(i);
			status = data.getStatus();
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

		setBtnTextAndClicker(holder.btnLeft, holder.btnRight, status,
				orderIdList.get(position));
		// holder.btnLeft.setOnClickListener(new
		// LeftBtnClickListener(orderIdList
		// .get(position)));
		// holder.btnRight.setOnClickListener(new RightBtnClickListener());

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
		private Button btnLeft;
		private Button btnRight;
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
				buy(orderId);
				break;
			case 5:
				ReceiveGoods(orderId);
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

	private void buy(String orderid) {
		// http://103.27.7.116:83/json/json.aspx?op= buyGoodsPay&orders_id=1

		Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams();
		params.put("op", "buyGoodsPay");
		params.put("orders_id", orderid);

		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				LogUtils.i(t);
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("result") == 1) {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						Uri content_url = Uri.parse("");
						intent.setData(content_url);
						context.startActivity(intent);

					} else {
						Tools.showTextToast(context, "购买商品失败");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void ReceiveGoods(String ordersId) {
		// http://103.27.7.116:83/json/json.aspx?op=order_Receiving&orders_id=1
		Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams();
		params.put("op", "order_Receiving");
		params.put("orders_id", ordersId);

		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				LogUtils.i(t);
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("result") == 1) {
						Tools.showTextToast(context, "收货成功");
					} else {
						Tools.showTextToast(context, "收货失败");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

}
