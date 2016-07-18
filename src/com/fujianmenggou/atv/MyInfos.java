package com.fujianmenggou.atv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;

public class MyInfos extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinfos);
		initFakeTitle();
		setTitle("我的资料");

		findViewById(R.id.tv_text0).setOnClickListener(this);
		findViewById(R.id.tv_text1).setOnClickListener(this);
		findViewById(R.id.tv_text2).setOnClickListener(this);

	}

	@Override
	public void onClick(View v){
		Intent intent = null;
		switch (v.getId()) {
			case R.id.tv_text0:
				intent = new Intent(context, RenZheng.class);
				break;
			case R.id.tv_text1:
				intent = new Intent(context, BankManage.class);
				break;
			case R.id.tv_text2:
				intent = new Intent(context, Zhengjian.class);
				break;
			default:
				break;
		}
		if (intent != null){
			startActivity(intent);
		}
	}

}
