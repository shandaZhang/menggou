package com.fujianmenggou.atv;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.AndroidAuthenticator;
import com.fujianmenggou.R;
import com.fujianmenggou.adapter.OrderAdapter;
import com.fujianmenggou.bean.OrderList;
import com.fujianmenggou.ckw.PullToRefreshBase;
import com.fujianmenggou.ckw.PullToRefreshListView;
import com.fujianmenggou.ckw.PullToRefreshBase.OnRefreshListener;
import com.fujianmenggou.util.BaseActivity;

import dujc.dtools.xutils.bitmap.BitmapCommonUtils;
import dujc.dtools.xutils.bitmap.BitmapDisplayConfig;

public class SubmitOrderActivity extends BaseActivity {
	private ArrayList<OrderList> orderLists = new ArrayList<OrderList>();
	private LinearLayout layoutGoods, layoutAddress;
	private BitmapDisplayConfig displayConfig;
	private double moneyPay = 0;
	private int countAll = 0;
	private TextView tvDelete;
	private Spinner postStyle;
	private TextView tvMoneyRemain, tvGoodsCount, tvPriceAll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_order);
		initFakeTitle();
		setTitle("提交订单");
		initView();
		initData();
		updateView();

	}

	private void initData() {
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
	}

	private void initView() {
		layoutGoods = (LinearLayout) findViewById(R.id.layout_goods);
		tvDelete = (TextView) findViewById(R.id.tv_delete);
		postStyle = (Spinner) findViewById(R.id.spinner);
		tvMoneyRemain = (TextView) findViewById(R.id.tv_money_remained);
		tvGoodsCount = (TextView) findViewById(R.id.tv_goods_count);
		tvPriceAll = (TextView) findViewById(R.id.tv_price_all);
		layoutAddress = (LinearLayout) findViewById(R.id.layout_address);
		layoutAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SubmitOrderActivity.this,
						AddressActivity.class);
				startActivity(intent);

			}
		});

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				new String[] { "快递", "自取" });
		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 添加事件Spinner事件监听
		postStyle.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		// 将adapter 添加到spinner中
		postStyle.setAdapter(adapter);

		tvDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = orderLists.size() - 1; i >= 0; i--) {
					OrderList data = orderLists.get(i);
					if (data.isChecked()) {
						moneyPay = moneyPay - data.getPriceAll();
						countAll = countAll - data.getNumber();
						orderLists.remove(data);
						tvGoodsCount.setText("共" + countAll + "件商品");
						tvPriceAll.setText("合计 ：" + moneyPay);
						layoutGoods.removeViewAt(i);

					}
				}

			}
		});

		displayConfig = new BitmapDisplayConfig();
		// displayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用,
		// 图片太大时容易OOM。
		displayConfig
				.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(context));
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(500);
		displayConfig.setAnimation(animation);
	}

	private void updateView() {
		for (int i = 0; i < orderLists.size(); i++) {
			final OrderList data = orderLists.get(i);
			LinearLayout goods = (LinearLayout) LayoutInflater.from(context)
					.inflate(R.layout.goods_order_item, null);
			View dividor = new View(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 1);
			float density = context.getResources().getDisplayMetrics().density;
			params.bottomMargin = (int) (10 * density);
			params.topMargin = (int) (10 * density);

			dividor.setLayoutParams(params);
			dividor.setBackgroundResource(R.color.hui_gray);

			LinearLayout.LayoutParams paramsGoods = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			paramsGoods.leftMargin = (int) (10 * density);
			paramsGoods.rightMargin = (int) (10 * density);
			goods.setLayoutParams(paramsGoods);
			TextView tvTitle = (TextView) goods
					.findViewById(R.id.tv_goods_title);
			TextView tvPrice = (TextView) goods
					.findViewById(R.id.tv_goods_price);
			TextView tvPriceAll = (TextView) goods
					.findViewById(R.id.tv_price_all);
			TextView tvDetail = (TextView) goods
					.findViewById(R.id.tv_goods_detail);
			CheckBox cbIsChecked = (CheckBox) goods.findViewById(R.id.cb);
			View layout1 = goods.findViewById(R.id.layout1);
			View layout2 = layout1.findViewById(R.id.layout2);
			View iv = layout1.findViewById(R.id.iv_goods);
			cbIsChecked
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (!isChecked) {
								moneyPay = moneyPay - data.getPriceAll();
								countAll = countAll - data.getNumber();
								data.setChecked(false);
								tvGoodsCount.setText("共" + countAll + "件商品");
								SubmitOrderActivity.this.tvPriceAll
										.setText("合计 ：" + moneyPay);

							} else {
								moneyPay = moneyPay + data.getPriceAll();
								countAll = countAll + data.getNumber();
								data.setChecked(true);
								tvGoodsCount.setText("共" + countAll + "件商品");
								SubmitOrderActivity.this.tvPriceAll
										.setText("合计 ：" + moneyPay);
							}
						}
					});

			tvTitle.setText(data.getTitle());
			tvDetail.setText(data.getDetail());
			tvPrice.setText("¥" + data.getPrice() + "x" + data.getNumber());
			tvPriceAll.setText(data.getPrice() * data.getNumber() + "");
			cbIsChecked.setChecked(data.isChecked());
			if (data.getUrl() != null)
				bmp.display(iv, data.getUrl(), displayConfig);
			if (data.isChecked()) {
				moneyPay += data.getPriceAll();
				countAll += data.getNumber();
			}
			layoutGoods.addView(goods);
			if (i < orderLists.size() - 1) {
				layoutGoods.addView(dividor);
			}

		}
		tvGoodsCount.setText("共" + countAll + "件商品");
		tvPriceAll.setText("合计 ：" + moneyPay);

	}
}
