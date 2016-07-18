package com.fujianmenggou.atv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fujianmenggou.R;
import com.fujianmenggou.fm.UserFragment;
import com.fujianmenggou.gethttp.GetHttpInfo;
import com.fujianmenggou.service.APPUpdateService;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;

import dujc.dtools.ViewUtils;
import dujc.dtools.xutils.view.annotation.event.OnClick;

/**
 * Created by Just on 2015/3/15.
 */
public class About extends BaseActivity {
	private TextView shifouxinban;
	private boolean isGenXing = false;
	private String newVersion = "";
	@SuppressLint("HandlerLeak")
	private Handler handle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GlobalVars.VERSION_UPDATE:
				newVersion = (String) msg.obj;
				String oldVersion = getCurrVersion();
				if (!TextUtils.isEmpty(newVersion) && newVersion.compareTo(oldVersion) > 0) {//有新版本					
					shifouxinban.setText("最新版本："+newVersion+",点击更新");
					isGenXing = true;
				}else {
					shifouxinban.setText("已是最新版");
					isGenXing = false;
				}
				break;

			default:
				break;
			}
		}};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		ViewUtils.inject(this);
		TextView textVersion = (TextView) findViewById(R.id.about_text_banbenhao);
		textVersion.setText(""+getCurrVersion());
		initFakeTitle();
		setTitle("关于盟购");
		findViewById(R.id.banbengengxin).setOnClickListener(this);
		shifouxinban = (TextView) findViewById(R.id.shifouxinban);
		//获取版本信息
		GetHttpInfo.getVersionAndUpdate(this, handle);
	}
	@OnClick(value = {R.id.jianyifankui,R.id.gongneng,R.id.toupiao})
	public void onClick(View v){
		switch (v.getId()){
			case R.id.jianyifankui:
				startActivity(new Intent(context, AdviceActivity.class));
				break;
			case R.id.gongneng:
				if (!TextUtils.isEmpty(UserFragment.mg_gr_gongneng)){
					startActivity(new Intent(context, WebActivity.class)
									.putExtra("url",UserFragment.mg_gr_gongneng)
									.putExtra("title", "功能介绍")
					);
				}
				break;
			case R.id.toupiao:
				startActivity(new Intent(context,ResetPassword.class));
				break;
			case R.id.banbengengxin:
				if (isGenXing) {
					/** 清除用户数据 **/
					clearUserInfo();
					/** 下载新版本 **/
					String app_name = "menggou_V" + newVersion + ".apk";
					String down_rul = GlobalVars.appDownloadUrl + app_name;
					Intent intent = new Intent(About.this, APPUpdateService.class);
					intent.putExtra("down_url", down_rul);
					intent.putExtra("app_name", app_name);
					startService(intent);
					shifouxinban.setText("更新下载中...");
				}
				break;
			default:break;
		}
	}
	
	/**
	 * 获取当前应用版本号
	 * @return
	 */
	public String getCurrVersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
		}
		return "1.0";
	}
	
}