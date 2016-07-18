package com.fujianmenggou.atv;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.Tools;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import dujc.dtools.xutils.util.LogUtils;

public class WebActivity extends BaseActivity {

	private WebView webView;
	private String URL;
	private ValueCallback<Uri> mUploadMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		initFakeTitle();
//		setTitle("支付");
		if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))){
			setTitle(getIntent().getStringExtra("title"));
		}
		URL = getIntent().getStringExtra("url");

		webView=(WebView) findViewById(R.id.webView);

		try{
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setWebChromeClient(new WebChromeClient());
			webView.setWebViewClient(new WebViewClient(){
				@Override
				public void onPageFinished(WebView view, String url) {
					// TODO Auto-generated method stub
					super.onPageFinished(view, url);
					LogUtils.i(url);
					Tools.DismissLoadingActivity(context);
				}
				
				

				@Override
				public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
					handler.proceed();
					//super.onReceivedSslError(view, handler, error);
				}
			});
			webView.setWebChromeClient(new WebChromeClient(){
				@Override
				public void onReceivedTitle(WebView view, String title) {
					super.onReceivedTitle(view, title);
					setTitle(title);
				}
			});
		}catch (Exception e){
			e.printStackTrace();
		}

		Tools.ShowLoadingActivity(context);
		webView.loadUrl(URL);
	}
	
	

}
