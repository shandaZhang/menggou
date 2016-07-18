package com.fujianmenggou.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import dujc.dtools.BitmapUtils;
import dujc.dtools.FinalHttp;

public class BaseFragment extends Fragment {
	protected FinalHttp http;
	protected BitmapUtils bmp;
//	protected FinalDb db;
	protected LayoutInflater layoutInflater;
	protected Context context;
	protected SharedPreferences userInfoPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		layoutInflater=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		http=new FinalHttp();
		bmp=new BitmapUtils(getActivity(), Tools.getSDPath()+"/.bsetpay/imagecahce");
		bmp.configDiskCacheEnabled(true);
		context=getActivity();

		userInfoPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(bmp!=null)
			bmp.resume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(bmp!=null)
			bmp.pause();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(bmp!=null)
			bmp.cancel();
	}

}
