package com.fujianmenggou.fm;

import com.fujianmenggou.adapter.OutOrderAdapter.ViewHolder;

public abstract class GetInnerListDataListener {
	public ViewHolder holder;
	public int position;

	public GetInnerListDataListener(ViewHolder holder) {
		this.holder = holder;
		this.position = position;
	}

	public abstract void updateView(int status, double moneyPay);

}
