package com.fujianmenggou.atv;

import java.util.ArrayList;

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

import dujc.dtools.xutils.bitmap.BitmapCommonUtils;
import dujc.dtools.xutils.bitmap.BitmapDisplayConfig;

public class GoodsAssesmentActivity extends BaseActivity {
	private ArrayList<GoodsAssesment> assesmentsLists = new ArrayList<GoodsAssesment>();
	private LinearLayout layoutGoods;
	private BitmapDisplayConfig displayConfig;
	private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assesment);
		initFakeTitle();
		setTitle("商品评价");
		initView();
		initData();
		updateView();

	}

	private void initData() {
		GoodsAssesment one = new GoodsAssesment();
		one.setContent("双黄香芋月饼 250克*1个 豆沙月饼 60克*2个 蓝莓味月饼 60克*2个 云腿月饼80克*3个");
		one.setName("潘祥记 七星伴月 广州团购批发香芋云腿椰蓉中秋月饼礼盒全国包邮");
		assesmentsLists.add(one);
		GoodsAssesment another = new GoodsAssesment();
		another.setContent("双黄香芋月饼 250克*1个 豆沙月饼 60克*2个 蓝莓味月饼 60克*2个 云腿月饼80克*3个");
		another.setName("潘祥记 七星伴月 广州团购批发香芋云腿椰蓉中秋月饼礼盒全国包邮");
		assesmentsLists.add(one);
	}

	private void initView() {
		layoutGoods = (LinearLayout) findViewById(R.id.layout_assesment);
		ivStar1 = (ImageView) findViewById(R.id.iv_star1);
		ivStar2 = (ImageView) findViewById(R.id.iv_star2);
		ivStar3 = (ImageView) findViewById(R.id.iv_star3);
		ivStar4 = (ImageView) findViewById(R.id.iv_star4);
		ivStar5 = (ImageView) findViewById(R.id.iv_star5);

		findViewById(R.id.btn_post_assesment).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

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

}
