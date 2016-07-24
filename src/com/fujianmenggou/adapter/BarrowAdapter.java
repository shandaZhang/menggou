package com.fujianmenggou.adapter;

import java.util.ArrayList;

import com.fujianmenggou.R;
import com.fujianmenggou.bean.OrderList;
import com.fujianmenggou.fm.InfoMationChangedListener;

import dujc.dtools.BitmapUtils;
import dujc.dtools.xutils.bitmap.BitmapCommonUtils;
import dujc.dtools.xutils.bitmap.BitmapDisplayConfig;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BarrowAdapter extends BaseAdapter {
	private ArrayList<OrderList> dataList;
	private Context context;
	private BitmapUtils bmp;
	private BitmapDisplayConfig displayConfig;
	private double moneyPay;
	private int numberPay;
	private InfoMationChangedListener listener;

	public BarrowAdapter(Context context, ArrayList<OrderList> dataList,
			BitmapUtils bmp, double moneyPay, int numberPay,
			InfoMationChangedListener listener) {
		this.dataList = dataList;
		this.context = context;
		this.bmp = bmp;
		this.moneyPay = moneyPay;
		this.numberPay = numberPay;
		this.listener = listener;

		this.bmp = bmp;
		displayConfig = new BitmapDisplayConfig();
		// displayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用,
		// 图片太大时容易OOM。
		displayConfig
				.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(context));
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(500);
		displayConfig.setAnimation(animation);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.e("menggou", "getview: position: " + position + " convertView: "
				+ convertView);
		final ViewHolder holder;
		final int index = position;
		final OrderList data = dataList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.barrow_goods_item, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_goods_title);
			holder.tvDetail = (TextView) convertView
					.findViewById(R.id.tv_goods_detail);
			holder.tvPrice = (TextView) convertView
					.findViewById(R.id.tv_goods_price);
			holder.tvPriceAll = (TextView) convertView
					.findViewById(R.id.tv_price_all);
			holder.tvNumber = (TextView) convertView
					.findViewById(R.id.tv_number);
			holder.tvAdd = (TextView) convertView.findViewById(R.id.tv_add);
			holder.tvSubStract = (TextView) convertView
					.findViewById(R.id.tv_subtract);
			holder.tvDelete = (TextView) convertView
					.findViewById(R.id.tv_delete);
			holder.ivGoods = (ImageView) convertView
					.findViewById(R.id.iv_goods);
			holder.tvAdd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					data.setNumber(data.getNumber() + 1);
					data.setPriceAll(data.getNumber() * data.getPrice());
					moneyPay += data.getPrice();
					numberPay++;
					listener.updateView(numberPay, moneyPay);

					holder.tvPrice.setText("¥" + data.getPrice() + "x"
							+ data.getNumber());
					holder.tvPriceAll.setText("¥" + data.getPriceAll());
					holder.tvNumber.setText(data.getNumber() + "");

				}
			});

			holder.tvSubStract.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					OrderList data = dataList.get(index);
					numberPay--;
					moneyPay -= data.getPrice();
					listener.updateView(numberPay, moneyPay);
					if (data.getNumber() == 1) {
						boolean result = dataList.remove(data);
						Log.e("menggou", "result: " + result);
						notifyDataSetChanged();
						return;
					}
					data.setNumber(data.getNumber() - 1);
					data.setPriceAll(data.getNumber() * data.getPrice());
					holder.tvPrice.setText("¥" + data.getPrice() + "x"
							+ data.getNumber());
					holder.tvPriceAll.setText("¥" + data.getPriceAll());
					holder.tvNumber.setText(data.getNumber() + "");

				}
			});

			holder.tvDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					OrderList data = dataList.get(index);
					numberPay -= data.getNumber();
					moneyPay -= data.getPriceAll();
					listener.updateView(numberPay, moneyPay);
					boolean result = dataList.remove(data);
					Log.e("menggou", "result: " + result);
					notifyDataSetChanged();
				}
			});

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvTitle.setText(data.getTitle());
		holder.tvDetail.setText(data.getDetail());
		holder.tvPrice.setText("¥" + data.getPrice() + "x" + data.getNumber());
		holder.tvPriceAll.setText("¥" + data.getPrice() * data.getNumber());
		holder.tvNumber.setText(data.getNumber() + "");
		Log.e("menggou", "number: " + data.getNumber());
		if (data.getUrl() != null)
			bmp.display(holder.ivGoods, data.getUrl(), displayConfig);

		return convertView;
	}

	class ViewHolder {
		TextView tvTitle;
		TextView tvDetail;
		TextView tvPrice;
		TextView tvPriceAll;
		TextView tvNumber;
		TextView tvAdd;
		TextView tvSubStract;
		TextView tvDelete;
		ImageView ivGoods;

	}

}
