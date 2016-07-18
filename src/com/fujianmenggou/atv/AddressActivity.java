package com.fujianmenggou.atv;

import android.os.Bundle;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;

public class AddressActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		initFakeTitle();
		setTitle("收货地址");

	}

}
