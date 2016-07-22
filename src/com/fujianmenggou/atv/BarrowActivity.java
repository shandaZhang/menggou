package com.fujianmenggou.atv;

import java.util.ArrayList;
import java.util.regex.Matcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fujianmenggou.R;
import com.fujianmenggou.adapter.BarrowAdapter;
import com.fujianmenggou.bean.OrderList;
import com.fujianmenggou.fm.InfoMationChangedListener;
import com.fujianmenggou.util.Barrows;
import com.fujianmenggou.util.BaseActivity;

public class BarrowActivity extends BaseActivity implements
		InfoMationChangedListener {
	private BarrowAdapter adapter;
	private ListView listview;
	private TextView tvCount, tvPriceAll, tvComfirm;
	private ArrayList<OrderList> dataList = new ArrayList<OrderList>();
	private ArrayList<OrderList> barrows;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barrow);
		initFakeTitle();
		setTitle("购物车");
		barrows = Barrows.getInstance();

		listview = (ListView) findViewById(R.id.listview);
		View view = new View(this);
		view.setLayoutParams(new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT,
				(int) (70 * getResources().getDisplayMetrics().density)));
		listview.addFooterView(view);
		adapter = new BarrowAdapter(this, barrows, bmp, 15 * 168.00, 15, this);
		listview.setAdapter(adapter);
		tvCount = (TextView) findViewById(R.id.tv_count);
		tvPriceAll = (TextView) findViewById(R.id.tv_price_all);
		tvComfirm = (TextView) findViewById(R.id.tv_comfirm);
		tvComfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BarrowActivity.this,
						SubmitOrderActivity.class);
				startActivity(intent);

			}
		});
		tvCount.setText("共" + 15 + "件商品");
		tvPriceAll.setText("合计：¥" + 15 * 168.00 + "元");

	}

	@Override
	public void updateView(int number, double moneyPay) {
		tvCount.setText("共" + number + "件商品");
		tvPriceAll.setText("合计：¥" + moneyPay + "元");
	}

	public void initData() {
		OrderList item = new OrderList();
		item.setDetail("双黄香芋月饼 250克*1个 豆沙月饼 60克*2个 蓝莓味月饼 60克*2个 云腿月饼80克*3个");
		item.setTitle("潘祥记 七星伴月 广州团购批发香芋云腿椰蓉中秋月饼礼盒全国包邮");
		item.setNumber(1);
		item.setPrice(168.00);
		item.setPriceAll(168.00 * 1);
		dataList.add(item);

		item = new OrderList();
		item.setDetail("双黄香芋月饼 250克*1个 豆沙月饼 60克*2个 蓝莓味月饼 60克*2个 云腿月饼80克*3个");
		item.setTitle("潘祥记 七星伴月 广州团购批发香芋云腿椰蓉中秋月饼礼盒全国包邮");
		item.setNumber(2);
		item.setPrice(168.00);
		item.setPriceAll(168.00 * 2);
		dataList.add(item);
		item = new OrderList();
		item.setDetail("双黄香芋月饼 250克*1个 豆沙月饼 60克*2个 蓝莓味月饼 60克*2个 云腿月饼80克*3个");
		item.setTitle("潘祥记 七星伴月 广州团购批发香芋云腿椰蓉中秋月饼礼盒全国包邮");
		item.setNumber(3);
		item.setPrice(168.00);
		item.setPriceAll(168.00 * 3);
		dataList.add(item);
		item = new OrderList();
		item.setDetail("双黄香芋月饼 250克*1个 豆沙月饼 60克*2个 蓝莓味月饼 60克*2个 云腿月饼80克*3个");
		item.setTitle("潘祥记 七星伴月 广州团购批发香芋云腿椰蓉中秋月饼礼盒全国包邮");
		item.setNumber(4);
		item.setPrice(168.00);
		item.setPriceAll(168.00 * 4);
		dataList.add(item);
		item = new OrderList();
		item.setDetail("双黄香芋月饼 250克*1个 豆沙月饼 60克*2个 蓝莓味月饼 60克*2个 云腿月饼80克*3个");
		item.setTitle("潘祥记 七星伴月 广州团购批发香芋云腿椰蓉中秋月饼礼盒全国包邮");
		item.setNumber(5);
		item.setPrice(168.00);
		item.setPriceAll(168.00 * 5);
		dataList.add(item);

	}

}
