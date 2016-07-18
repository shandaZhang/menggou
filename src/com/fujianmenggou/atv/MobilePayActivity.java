package com.fujianmenggou.atv;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.fujianmenggou.R;
import com.fujianmenggou.fm.MobilePayFragment;
import com.fujianmenggou.util.BaseActivity;

/**
 * Created by Just on 2015/3/17.
 */
public class MobilePayActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moblepay);
		initFakeTitle();
		setTitle("我要收款");
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.frame,new MobilePayFragment()).commit();
	}

}
