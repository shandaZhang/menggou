package com.fujianmenggou.ckw;

import com.fujianmenggou.R;
import com.fujianmenggou.R.id;
import com.fujianmenggou.R.layout;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.Tools;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.os.Build;

public class DetailsActivity extends BaseActivity {

	private View progressBar;
	private WebView mWebView;
	private String sid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_details);
		
		String url = getIntent().getStringExtra("url");
		sid = getIntent().getStringExtra("sid");
		
		findViewById(R.id.btn_finish).setOnClickListener(this);
		findViewById(R.id.btn_shop).setOnClickListener(this);
		progressBar = findViewById(R.id.progressBar);
		mWebView = (WebView) findViewById(R.id.webView);
		
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		if(url!=null){
			progressBar.setVisibility(View.VISIBLE);
			mWebView.loadUrl(url);
		}
		mWebView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if(newProgress==100){
					progressBar.setVisibility(View.GONE);
				}
			}
		});
		mWebView.setWebViewClient(new WebViewClient());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.details, menu);
		return true;
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_finish:
			finish();
			break;
		case R.id.btn_shop:
			btnShop();
			break;
		default:
			break;
		}
	}
	private void btnShop() {
		Intent intent = new Intent(DetailsActivity.this, ShopActivity.class);
		intent.putExtra("sid", sid);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
			mWebView.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
