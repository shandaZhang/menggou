package com.fujianmenggou.atv;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.fujianmenggou.R;
import com.fujianmenggou.bean.GoodsAssesment;
import com.fujianmenggou.bean.GoodsDetail;
import com.fujianmenggou.bean.GoodsList;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.bitmap.BitmapDisplayConfig;
import dujc.dtools.xutils.util.LogUtils;

public class GoodsDetailActivity extends BaseActivity {

	private ViewPager viewPager;
	private ArrayList<ImageView> viewContainter = new ArrayList<ImageView>();
	private LinearLayout layoutDots;
	private ImageView ivDots;
	private LinearLayout layoutGoodsAttr;
	private TextView tvGoodsName, tvPriceNow, tvPriceMarket, tvAssesment;
	private TextView tvAdd, tvSubStract, tvNum;
	private Button btnBuy, btnBarrow;
	private int number = 1;
	private BitmapDisplayConfig displayConfig;
	private ArrayList<String> assetsments = new ArrayList<String>();
	private ListView assessList;	//评价列表

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

	private void getGoodsDetail() {
		// http://103.27.7.116:83/json/json.aspx?op=GoodsDetails&id=1

		Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams();
		params.put("op", "GoodsDetails");
		params.put("id", getIntent().getStringExtra("id"));

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

				// {"result":"1","totalcount":1,"list":[{"id":1,"name":"香蕉","content":"艰苦艰苦拉萨东风科技"
				// ,"pic":"/upload/201606/01/201606011104401384.jpg","price":1.00
				// ,"describe":"展示商品时，商品的描述信息是必不可少的，是对图片信息的重要补充。大家都知道"
				// + "，图片传达给顾客的只是商品的形状、颜色、大小等信息，而关于商品的具体性能、"
				// + "具体尺寸、所用材料、原产地、售后服务保证等，就一定得通过文字作具体说明，"
				// + "这样顾客才能够清楚商品的功能，从而决定是否购买",
				// "oldprice":2.00,"stock":9985,"number":51,"address":"河北省秦皇岛市"
				// ,"user_id":98,"add_time":"2016-05-01T00:17:50","sort_id":99
				// ,"ls":[{"id":19,"user_id":98,"article_id":1
				// ,"thumb_path":"/upload/201606/01/thumb_201606011104487859.jpg"
				// ,"original_path":"/upload/201606/01/201606011104487859.jpg"
				// ,"remark":null,"add_time":"2016-07-21T10:45:26.25725+08:00"}
				// ,{"id":20,"user_id":98,"article_id":1,
				// "thumb_path":"/upload/201606/01/thumb_201606011104489216.jpg"
				// ,"original_path":"/upload/201606/01/201606011104489216.jpg"
				// ,"remark":null,"add_time":"2016-07-21T10:45:26.25725+08:00"}
				// ,{"id":21,"user_id":98,"article_id":1,"thumb_path":"/upload/201606/01/thumb_201606011104490320.jpg"
				// ,"original_path":"/upload/201606/01/201606011104490320.jpg","remark":null
				// ,"add_time":"2016-07-21T10:45:26.25725+08:00"},{"id":22,"user_id":98,"article_id":1
				// ,"thumb_path":"/upload/201606/01/thumb_201606011104490896.jpg"
				// ,"original_path":"/upload/201606/01/201606011104490896.jpg","remark":null
				// ,"add_time":"2016-07-21T10:45:26.25725+08:00"}]}]}
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("result") == 1) {
						// 店铺轮播图列表
						// JSONArray shoplist = obj.getJSONArray("shoplist");
						// for (int i = 0; i < shoplist.length(); i++) {
						// JSONObject shopImg = shoplist.getJSONObject(i);
						// ImageView iv = new ImageView(
						// GoodsDetailActivity.this);
						// iv.setScaleType(ScaleType.FIT_XY);
						// viewContainter.add(iv);
						// bmp.display(iv, shopImg.getString("thumb_path"),
						// displayConfig);
						// ImageView dot = new ImageView(
						// GoodsDetailActivity.this);
						// dot.setImageResource(R.drawable.icon_pot_unselected);
						// layoutDots.addView(dot);
						// }
						// ivDots = (ImageView) layoutDots.getChildAt(0);
						// ivDots.setImageResource(R.drawable.icon_pot_selected);

						// 商品列表
						JSONArray list = obj.getJSONArray("list");

						for (int i = 0; i < list.length(); i++) {
							JSONObject goodsObj = list.getJSONObject(i);
							GoodsDetail detail = new GoodsDetail();
							detail.setGoodsId(goodsObj.getString("id"));
							detail.setPriceNow(goodsObj.getDouble("price"));
							detail.setPriceMarket(goodsObj
									.getDouble("oldprice"));
							detail.setName(goodsObj.getString("name"));
						}

						viewPager.getAdapter().notifyDataSetChanged();

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void getAssesment() {
		Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams();
		params.put("op", "GoodsDetails");
		params.put("id", getIntent().getStringExtra("id"));

		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
			}

			// {"result":"1","totalcount":2,"list":[
			// {"id":2,"user_id":75,"goods_id":1,"score":78,"evaluate":"商品很漂亮","add_time":null,"is_lock":null},
			// {"id":1,"user_id":98,"goods_id":1,"score":80,"evaluate":"商品很好","add_time":null,"is_lock":null}]}
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				LogUtils.i(t);
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("result") == 1) {
						// 商品评价列表
						JSONArray list = obj.getJSONArray("list");
						for (int i = 0; i < list.length(); i++) {
							JSONObject assess = list.getJSONObject(i);
							assetsments.add(assess.getString("evaluate"));
						}
						//更新列表
						

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

}
