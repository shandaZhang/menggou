package com.fujianmenggou.atv;

import java.util.ArrayList;

import android.os.Bundle;

import com.fujianmenggou.R;
import com.fujianmenggou.adapter.GoodsShoppingMallAdapter;
import com.fujianmenggou.bean.GoodsList;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.XListView;
import com.fujianmenggou.util.XListView.IXListViewListener;

public class MyGoodsActivity extends BaseActivity {
	private ArrayList<GoodsList> goodsLists = new ArrayList<GoodsList>();
	private XListView listView;
	private GoodsShoppingMallAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_goods);
		initFakeTitle();
		setTitle("我的商品");
		listView = (XListView) findViewById(R.id.listview);
		adapter = new GoodsShoppingMallAdapter(this, bmp, goodsLists);
		listView.setAdapter(adapter);
		listView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub

			}

			//
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub

			}
		});
		initData();

	}

	private void initData() {
		for (int i = 0; i < 4; i++) {
			GoodsList item;
			item = new GoodsList();
			item.setPrice("25.00");
			item.setTitle("【果美美】智利黑布林李子1kg");
			goodsLists.add(item);
			item = new GoodsList();
			item.setPrice("43.50");
			item.setTitle("果美美台湾新鲜欂栌2个/凤梨");
			goodsLists.add(item);
		}

	}

}
