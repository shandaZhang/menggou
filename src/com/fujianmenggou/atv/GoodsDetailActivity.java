package com.fujianmenggou.atv;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.fujianmenggou.R;
import com.fujianmenggou.bean.GoodsDetail;
import com.fujianmenggou.util.BaseActivity;

public class GoodsDetailActivity extends BaseActivity {

	private ViewPager viewPager;
	private ArrayList<String> ivPagerUrls = new ArrayList<String>();
	private ArrayList<ImageView> viewContainter = new ArrayList<ImageView>();
	private LinearLayout layoutDots;
	private ImageView ivDots;
	private LinearLayout layoutGoodsAttr;
	private TextView tvGoodsName, tvPriceNow, tvPriceMarket, tvRemainingTime,
			tvAssesment;
	private TextView tvAdd, tvSubStract, tvNum;
	private Button btnBuy, btnBarrow;
	private int number = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail);
		initFakeTitle();
		setTitle("商品详情");
		initView();
		initData();
	}

	private void initView() {

		layoutDots = (LinearLayout) findViewById(R.id.layout_dots);
		layoutGoodsAttr = (LinearLayout) findViewById(R.id.layout_goods_attribute);
		tvGoodsName = (TextView) findViewById(R.id.tv_goods_title);
		tvPriceNow = (TextView) findViewById(R.id.tv_price_now);
		tvPriceMarket = (TextView) findViewById(R.id.tv_price_market);
		tvRemainingTime = (TextView) findViewById(R.id.tv_remaining_time);
		tvAdd = (TextView) findViewById(R.id.tv_add);
		tvNum = (TextView) findViewById(R.id.tv_number);
		tvSubStract = (TextView) findViewById(R.id.tv_subtract);
		tvAssesment = (TextView) findViewById(R.id.tv_assessment);
		btnBuy = (Button) findViewById(R.id.btn_buy);
		btnBarrow = (Button) findViewById(R.id.btn_barrow);
		tvPriceMarket.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

		tvAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				number = number + 1;
				tvNum.setText(number + "");
			}
		});

		tvSubStract.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (number > 1) {
					number = number - 1;
					tvNum.setText(number + "");
				}

			}
		});
		tvAssesment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(GoodsDetailActivity.this,
						GoodsAssesmentActivity.class);
				startActivity(it);

			}
		});

		btnBuy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		btnBarrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(new PagerAdapter() {
			// viewpager中的组件数量
			@Override
			public int getCount() {
				return viewContainter.size();
			}

			// 滑动切换的时候销毁当前的组件
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				((ViewPager) container).removeView(viewContainter.get(position));
			}

			// 每次滑动的时候生成的组件
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				((ViewPager) container).addView(viewContainter.get(position));
				return viewContainter.get(position);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

		});
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				ivDots.setImageResource(R.drawable.icon_pot_unselected);
				ivDots = (ImageView) layoutDots.getChildAt(position);
				ivDots.setImageResource(R.drawable.icon_pot_selected);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void initData() {
		ImageView iv;
		ImageView dot;
		for (int i = 0; i < 2; i++) {
			iv = new ImageView(this);
			iv.setImageResource(R.drawable.pager3);
			iv.setScaleType(ScaleType.FIT_XY);
			viewContainter.add(iv);
			dot = new ImageView(this);
			dot.setImageResource(R.drawable.icon_pot_unselected);
			layoutDots.addView(dot);
		}
		ivDots = (ImageView) layoutDots.getChildAt(0);
		ivDots.setImageResource(R.drawable.icon_pot_selected);
		viewPager.getAdapter().notifyDataSetChanged();

		GoodsDetail detail = new GoodsDetail();
		detail.setName("沙发防滑 布艺时尚坐垫沙发套沙发巾罩");
		detail.setAssessmentNum(0);
		detail.setPriceNow(29.00);
		detail.setPriceMarket(68.00);
		// detail.setRemainningTime();
		HashMap<String, String> attrs = new HashMap<String, String>();
		attrs.put("颜色分类", "巴洛克（卡其色） 巴洛克（金色）");
		attrs.put("适用对象", "组合沙发");
		attrs.put("材质", "布");
		attrs.put("品牌", "赛丽尔");
		attrs.put("货号", "SD10356-2");
		attrs.put("图案", "格子");
		attrs.put("风格", "欧式");
		detail.setAttrs(attrs);
		updateView(detail);

	}

	private void updateView(GoodsDetail detail) {
		tvGoodsName.setText(detail.getName());
		tvPriceNow.setText("¥" + detail.getPriceNow());
		tvPriceMarket.setText("¥" + detail.getPriceMarket());
		tvAssesment.setText("评价（" + detail.getAssessmentNum() + ")");
		ArrayList<String> keys = new ArrayList<String>(detail.getAttrs()
				.keySet());
		for (String key : keys) {
			TextView attr = new TextView(this);
			LinearLayout.LayoutParams params = new LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			attr.setLayoutParams(params);
			attr.setText(key + ": " + detail.getAttrs().get(key));
			layoutGoodsAttr.addView(attr);

		}

	}
}
