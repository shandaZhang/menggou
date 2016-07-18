package com.fujianmenggou.fm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.fujianmenggou.R;
import com.fujianmenggou.atv.MyGoodsActivity;
import com.fujianmenggou.atv.OrderManageActivity;
import com.fujianmenggou.atv.ShopInfoActivity;
import com.fujianmenggou.atv.ShopPreViewActivity;
import com.fujianmenggou.util.BaseFragment;

public class MyShopFragment2 extends BaseFragment implements OnClickListener {
	private View rootView;// 用于缓存

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_myshop2, null);
			rootView.findViewById(R.id.iv_dingdanguanli).setOnClickListener(
					this);
			rootView.findViewById(R.id.iv_dianputongji)
					.setOnClickListener(this);
			rootView.findViewById(R.id.iv_dianpuguanli)
					.setOnClickListener(this);
			rootView.findViewById(R.id.iv_wodefenxiao).setOnClickListener(this);

		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	@Override
	public void onClick(View v) {
		Intent it = new Intent();
		switch (v.getId()) {

		case R.id.iv_wodefenxiao:
			it.setClass(MyShopFragment2.this.getActivity(),
					MyGoodsActivity.class);
			startActivity(it);
			break;
		case R.id.iv_dingdanguanli:
			it.setClass(MyShopFragment2.this.getActivity(),
					OrderManageActivity.class);
			startActivity(it);
			break;
		case R.id.iv_dianputongji:
			it.setClass(MyShopFragment2.this.getActivity(),
					ShopInfoActivity.class);
			startActivity(it);
			break;
		case R.id.iv_dianpuguanli:
			it.setClass(MyShopFragment2.this.getActivity(),
					ShopPreViewActivity.class);
			startActivity(it);

		}

	}

}
