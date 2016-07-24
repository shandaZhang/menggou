package com.fujianmenggou.atv;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.fujianmenggou.R;
import com.fujianmenggou.bean.GoodsAssesment;
import com.fujianmenggou.bean.OrderList;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.bitmap.BitmapCommonUtils;
import dujc.dtools.xutils.bitmap.BitmapDisplayConfig;
import dujc.dtools.xutils.util.LogUtils;

public class GoodsAssesmentActivity extends BaseActivity {
	private ArrayList<GoodsAssesment> assesmentsLists = new ArrayList<GoodsAssesment>();
	private LinearLayout layoutGoods;
	private BitmapDisplayConfig displayConfig;
	private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
	private ArrayList<OrderList> data;
	private int goodsNum;
	private int currentNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assesment);
		initFakeTitle();
		setTitle("商品评价");
		initView();
		data = (ArrayList<OrderList>) getIntent().getSerializableExtra("data");
		goodsNum = data.size();
		for (OrderList item : data) {
			GetEvaluate(item);
		}

		// initData();
		updateView();

	}

	// private void initData() {
	// GoodsAssesment one = new GoodsAssesment();
	// one.setContent("双黄香芋月饼 250克*1个 豆沙月饼 60克*2个 蓝莓味月饼 60克*2个 云腿月饼80克*3个");
	// one.setName("潘祥记 七星伴月 广州团购批发香芋云腿椰蓉中秋月饼礼盒全国包邮");
	// assesmentsLists.add(one);
	// GoodsAssesment another = new GoodsAssesment();
	// another.setContent("双黄香芋月饼 250克*1个 豆沙月饼 60克*2个 蓝莓味月饼 60克*2个 云腿月饼80克*3个");
	// another.setName("潘祥记 七星伴月 广州团购批发香芋云腿椰蓉中秋月饼礼盒全国包邮");
	// assesmentsLists.add(one);
	// }

	private void initView() {
		layoutGoods = (LinearLayout) findViewById(R.id.layout_assesment);

		findViewById(R.id.btn_post_assesment).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						for (int i = 0; i < assesmentsLists.size(); i++) {
							GoodsAssesment assesment = assesmentsLists.get(i);
							EditText etAssesment = (EditText) layoutGoods
									.getChildAt(i * 2 + 1).findViewById(
											R.id.et_assesment);
							evaluate(assesment.getGoodsId(),
									assesment.getStarLevel() * 20 + "",
									etAssesment.getText().toString());

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
		for (int i = 0; i < assesmentsLists.size(); i++) {
			final GoodsAssesment data = assesmentsLists.get(i);
			LinearLayout goods = (LinearLayout) LayoutInflater.from(context)
					.inflate(R.layout.goods_assesment_item, null);
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
			TextView tvDetail = (TextView) goods
					.findViewById(R.id.tv_goods_detail);
			View iv = goods.findViewById(R.id.iv_goods);
			ivStar1 = (ImageView) goods.findViewById(R.id.iv_star1);
			ivStar2 = (ImageView) goods.findViewById(R.id.iv_star2);
			ivStar3 = (ImageView) goods.findViewById(R.id.iv_star3);
			ivStar4 = (ImageView) goods.findViewById(R.id.iv_star4);
			ivStar5 = (ImageView) goods.findViewById(R.id.iv_star5);
			OnStarClickListener starListner = new OnStarClickListener(data);
			ivStar1.setOnClickListener(starListner);
			ivStar2.setOnClickListener(starListner);
			ivStar3.setOnClickListener(starListner);
			ivStar4.setOnClickListener(starListner);
			ivStar5.setOnClickListener(starListner);

			tvTitle.setText(data.getName());
			tvDetail.setText(data.getContent());

			if (data.getUrl() != null)
				bmp.display(iv, data.getUrl(), displayConfig);

			layoutGoods.addView(goods);
			if (i < assesmentsLists.size() - 1) {
				layoutGoods.addView(dividor);
			}

		}

	}

	class OnStarClickListener implements OnClickListener {
		private GoodsAssesment assesment;

		public OnStarClickListener(GoodsAssesment assesment) {
			super();
			this.assesment = assesment;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_star1:
				assesment.setStarLevel(1);
				ivStar1.setImageResource(R.drawable.icon_star_selected);
				ivStar2.setImageResource(R.drawable.icon_star_unselected);
				ivStar3.setImageResource(R.drawable.icon_star_unselected);
				ivStar4.setImageResource(R.drawable.icon_star_unselected);
				ivStar5.setImageResource(R.drawable.icon_star_unselected);
				break;

			case R.id.iv_star2:
				assesment.setStarLevel(2);
				ivStar1.setImageResource(R.drawable.icon_star_selected);
				ivStar2.setImageResource(R.drawable.icon_star_selected);
				ivStar3.setImageResource(R.drawable.icon_star_unselected);
				ivStar4.setImageResource(R.drawable.icon_star_unselected);
				ivStar5.setImageResource(R.drawable.icon_star_unselected);

				break;

			case R.id.iv_star3:
				assesment.setStarLevel(3);
				ivStar1.setImageResource(R.drawable.icon_star_selected);
				ivStar2.setImageResource(R.drawable.icon_star_selected);
				ivStar3.setImageResource(R.drawable.icon_star_selected);
				ivStar4.setImageResource(R.drawable.icon_star_unselected);
				ivStar5.setImageResource(R.drawable.icon_star_unselected);
				break;

			case R.id.iv_star4:
				assesment.setStarLevel(4);
				ivStar1.setImageResource(R.drawable.icon_star_selected);
				ivStar2.setImageResource(R.drawable.icon_star_selected);
				ivStar3.setImageResource(R.drawable.icon_star_selected);
				ivStar4.setImageResource(R.drawable.icon_star_selected);
				ivStar5.setImageResource(R.drawable.icon_star_unselected);
				break;

			case R.id.iv_star5:
				assesment.setStarLevel(5);
				ivStar1.setImageResource(R.drawable.icon_star_selected);
				ivStar2.setImageResource(R.drawable.icon_star_selected);
				ivStar3.setImageResource(R.drawable.icon_star_selected);
				ivStar4.setImageResource(R.drawable.icon_star_selected);
				ivStar5.setImageResource(R.drawable.icon_star_selected);

				break;

			}

		}

	}

	private void GetEvaluate(final OrderList item) {
		// http://103.27.7.116:83/json/json.aspx?op=GetEvaluate&orders_id=1&goods_id=1&user_id=98
		Tools.ShowLoadingActivity(this);
		AjaxParams params = new AjaxParams();
		params.put("op", "GetEvaluate");
		params.put("user_id", userInfoPreferences.getString("id", ""));
		params.put("orders_id", getIntent().getStringExtra("orderId"));
		params.put("goods_id", item.getId());

		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
				currentNum++;
				if (currentNum == goodsNum) {
					updateView();
				}
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				LogUtils.i(t);

				// {"result": 0, "msg": "已评价"}
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("result") == 1) {
						Tools.showTextToast(GoodsAssesmentActivity.this,
								"获取评价成功");
					} else {
						Tools.showTextToast(GoodsAssesmentActivity.this,
								"获取评价失败");
						GoodsAssesment assess = new GoodsAssesment();
						assess.setContent(item.getDetail());
						assess.setGoodsId(item.getId());
						assess.setName(item.getTitle());
						assess.setStarLevel(0);
						assess.setUrl(item.getUrl());
						assesmentsLists.add(assess);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				currentNum++;
				if (currentNum == goodsNum) {
					updateView();
				}

			}
		});

	}

	private void evaluate(String goodsId, String score, String evaluate) {
		// http://103.27.7.116:83/json/json.aspx?op=Evaluate&orders_id=1&goods_id=1&user_id=98
		Tools.ShowLoadingActivity(this);
		AjaxParams params = new AjaxParams();
		params.put("op", "Evaluate");
		params.put("user_id", userInfoPreferences.getString("id", ""));
		params.put("orders_id", getIntent().getStringExtra("orderId"));
		params.put("goods_id", goodsId);
		params.put("score", score + "");
		params.put("evaluate", evaluate);

		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
				currentNum--;
				if (currentNum == 0) {
					finish();
				}
			}

			@Override
			public void onSuccess(String t) {

				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				LogUtils.i(t);

				// {"result": 0, "msg": "已评价"}
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("result") == 1) {
						Tools.showTextToast(GoodsAssesmentActivity.this, "评价成功");
					} else
						Tools.showTextToast(GoodsAssesmentActivity.this, "评价失败");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				currentNum--;
				if (currentNum == 0) {
					finish();
				}
			}
		});

	}
}
