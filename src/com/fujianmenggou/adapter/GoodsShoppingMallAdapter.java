package com.fujianmenggou.adapter;

import java.util.ArrayList;

import com.baidu.mapapi.common.Logger;
import com.fujianmenggou.R;
import com.fujianmenggou.atv.GoodsDetailActivity;
import com.fujianmenggou.bean.GoodsList;

import dujc.dtools.BitmapUtils;
import dujc.dtools.xutils.bitmap.BitmapCommonUtils;
import dujc.dtools.xutils.bitmap.BitmapDisplayConfig;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsShoppingMallAdapter extends BaseAdapter implements
		OnClickListener {

	private Context context;
	private ArrayList<GoodsList> goodsLists;
	private BitmapUtils bmp;
	private BitmapDisplayConfig displayConfig;

	public GoodsShoppingMallAdapter(Context context, BitmapUtils bmp,
			ArrayList<GoodsList> goodsLists) {
		this.context = context;
		this.goodsLists = goodsLists;
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
		return (int) Math.ceil(goodsLists.size() / 2.0);
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.goods_list_item, null);
			holder = new ViewHolder();
			holder.ivGoods1 = (ImageView) convertView.findViewById(
					R.id.layout_goods1).findViewById(R.id.iv_goods);
			holder.tvPrice1 = (TextView) convertView.findViewById(
					R.id.layout_goods1).findViewById(R.id.tv_goods_price);
			holder.tvTitle1 = (TextView) convertView.findViewById(
					R.id.layout_goods1).findViewById(R.id.tv_goods_title);

			holder.ivGoods2 = (ImageView) convertView.findViewById(
					R.id.layout_goods2).findViewById(R.id.iv_goods);
			holder.tvPrice2 = (TextView) convertView.findViewById(
					R.id.layout_goods2).findViewById(R.id.tv_goods_price);
			holder.tvTitle2 = (TextView) convertView.findViewById(
					R.id.layout_goods2).findViewById(R.id.tv_goods_title);
			convertView.findViewById(R.id.layout_goods1).setTag(position * 2);
			convertView.findViewById(R.id.layout_goods2).setTag(
					position * 2 + 1);
			convertView.findViewById(R.id.layout_goods1).setOnClickListener(
					this);
			convertView.findViewById(R.id.layout_goods2).setOnClickListener(
					this);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GoodsList goods1, goods2 = null;
		goods1 = goodsLists.get(position * 2);
		if (position * 2 + 1 < goodsLists.size()) {
			goods2 = goodsLists.get(position * 2 + 1);
		}

		holder.tvPrice1.setText("¥" + goods1.getPrice());
		holder.tvTitle1.setText(goods1.getTitle());
		bmp.display(holder.ivGoods1, goods1.getUrl(), displayConfig);
		if (goods1.getUrl() == null) {
			holder.ivGoods1.setImageResource(R.drawable.goods1);
		}
		if (goods2 != null) {
			holder.tvPrice2.setText("¥" + goods2.getPrice());
			holder.tvTitle2.setText(goods2.getTitle());
			bmp.display(holder.ivGoods2, goods2.getUrl(), displayConfig);
			if (goods2.getUrl() == null) {
				holder.ivGoods2.setImageResource(R.drawable.goods2);
			}
		}

		return convertView;
	}

	class ViewHolder {
		TextView tvTitle1, tvTitle2;
		TextView tvPrice1, tvPrice2;
		ImageView ivGoods1, ivGoods2;

	}

	@Override
	public void onClick(View v) {
		Log.e("manggou", "onClick" + " position: " + v.getTag().toString());
		Intent intent = new Intent();
		intent.putExtra("goodsId", goodsLists.get((Integer) v.getTag())
				.getGoodsId());
		intent.setClass(context, GoodsDetailActivity.class);
		context.startActivity(intent);

	}
}
