package com.fujianmenggou.ckw;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Toast;

import com.fujianmenggou.LoginActivity;
import com.fujianmenggou.R;
import com.fujianmenggou.gethttp.GetHttpInfo;
import com.fujianmenggou.service.APPUpdateService;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;

public class SplashActivity extends BaseActivity {

	@SuppressLint("HandlerLeak")
	private Handler handle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GlobalVars.VERSION_UPDATE:
				final String newVersion = (String) msg.obj;
				String oldVersion = getVersion(SplashActivity.this);
				if (!TextUtils.isEmpty(newVersion) && newVersion.compareTo(oldVersion) > 0) {//有新版本
					AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
					dialog.setTitle("更新提醒");
					dialog.setMessage("更安全，更优惠的银联在线接口正式上线，欢迎用户更新后使用!");
					dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							nextPage();
						}
					});
					dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							/** 清除用户数据 **/
							clearUserInfo();
							/** 下载新版本 **/
							String app_name = "menggou_V" + newVersion + ".apk";
							String down_rul = GlobalVars.appDownloadUrl + app_name;
							Intent intent = new Intent(SplashActivity.this, APPUpdateService.class);
							intent.putExtra("down_url", down_rul);
							intent.putExtra("app_name", app_name);
							startService(intent);
						}
					});
					dialog.create().show();
					
				}else {
					nextPage();
				}
				break;

			default:
				break;
			}
		}};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		GetHttpInfo.getVersionAndUpdate(this, handle);
	}
	
	/**
	 * 下一个页面
	 */
	private void nextPage() {
		try {
			SharedPreferences sp = getSharedPreferences("menggouNew", Context.MODE_PRIVATE);
			boolean b = sp.getBoolean("isFirstNew", true);
			Intent intent=new Intent();
			if(b){
				intent.setClass(SplashActivity.this, GuideActivity.class);
				Editor edit = sp.edit();
				edit.putBoolean("isFirstNew", false);
				edit.commit();
			}else{
				intent.setClass(SplashActivity.this, LoginActivity.class);
				
			}
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
		} catch (Exception e) {
			Toast.makeText(SplashActivity.this, e.toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前版本号
	 * @param con
	 * @return
	 */
	public static String getVersion(Context con) {
		String version = "";
		PackageManager packageManager = con.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(con.getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			
			return true;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}
