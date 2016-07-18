package com.fujianmenggou.util;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.fujianmenggou.R;

import dujc.dtools.BitmapUtils;
import dujc.dtools.FinalHttp;

/**
 * 在此类中实现了，基本的actionbar定义
 * 
 * @author Administrator
 *
 */
public class BaseActivity extends FragmentActivity implements OnClickListener,
		OnItemClickListener {
	protected LayoutInflater inflater;
	protected TextView search;
	protected FinalHttp http;
	protected BitmapUtils bmp;
	protected Context context;
	private TextView tv_title, tv_back;
	protected SharedPreferences userInfoPreferences;
	protected SharedPreferences share;
	String tag = "menggou";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.e(tag, getClass().getSimpleName());
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		http = new FinalHttp();
		bmp = new BitmapUtils(this, Tools.getSDPath() + "/.bsetpay/imagecahce");
		bmp.configDiskCacheEnabled(true);
		userInfoPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
		share = getSharedPreferences("GLOBALVARS", MODE_PRIVATE);
		context = this;

	}

	protected void initFakeTitle() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_back = (TextView) findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	protected void hideBack() {
		tv_back.setVisibility(View.INVISIBLE);
	}

	protected void setTitle(String title) {
		tv_title.setText(title);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(config);
		setPortrait();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (bmp != null)
			bmp.resume();
		setPortrait();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (bmp != null)
			bmp.pause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (bmp != null)
			bmp.cancel();
	}

	private void setPortrait() {
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
		}
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	/**
	 * 清除用户数据
	 */
	public void clearUserInfo() {
		Editor e1 = share.edit();
		e1.clear();
		e1.commit();
		Editor e2 = userInfoPreferences.edit();
		e2.clear();
		e2.commit();
	}

	/**
	 * 貌似没什么用——王君注
	 */
	public static ArrayList<Activity> listActivities = new ArrayList<Activity>();

	/** 为了删除没用的activity,防止回退页面过多而使用 */
	public void addActivity() {
		listActivities.add(this);
		for (int i = 1; i < listActivities.size(); i++) {
			listActivities.get(i).finish();
			listActivities.remove(i);
		}
	}

}
