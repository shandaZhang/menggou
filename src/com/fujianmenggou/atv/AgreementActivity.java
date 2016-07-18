package com.fujianmenggou.atv;

import dujc.dtools.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AgreementActivity extends BaseActivity {

	private static int o = 0;
	private String op = "delegate";
	private String potocol;
	private WebView web;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agreement);
		initFakeTitle();
		
		if(o==0){
			setTitle("开通协议");
			op = "delegate";
		}else{
			setTitle("禁售协议");
			op = "GetBan";
		}
		
		web=(WebView) findViewById(R.id.web_agreement);
		web.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				Tools.DismissLoadingActivity(context);
			}
		});
		
		Tools.ShowLoadingActivity(context);
		initAgreement();
	}

	private void initAgreement() {
		// TODO Auto-generated method stub
		http.get(GlobalVars.url+"?op="+op, new AjaxCallBack<String>(){
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				try {
					JSONObject json=new JSONObject(t);
					if(json.getInt("result")==1){
						potocol=json.getString("url");
						
						web.loadUrl(potocol);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void setO(int o){
		AgreementActivity.o = o;
	}
}
