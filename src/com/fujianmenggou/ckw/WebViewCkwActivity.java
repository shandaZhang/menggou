package com.fujianmenggou.ckw;

import com.fujianmenggou.R;
import com.fujianmenggou.R.id;
import com.fujianmenggou.R.layout;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.Tools;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewCkwActivity extends BaseActivity implements OnClickListener {

	private WebView web;

	private ValueCallback<Uri> mUploadMessage;

	private String URL;  
	 private final static int FILECHOOSER_RESULTCODE=1;  

	 @Override  
	 protected void onActivityResult(int requestCode, int resultCode,  
	                                    Intent intent) {  
	  if(requestCode==FILECHOOSER_RESULTCODE)  
	  {  
	   if (null == mUploadMessage) return;  
	            Uri result = intent == null || resultCode != RESULT_OK ? null  
	                    : intent.getData();  
	            mUploadMessage.onReceiveValue(result);  
	            mUploadMessage = null;  
	  }
	  }  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_web_view_ckw);
		
		URL = getIntent().getStringExtra("url");
		
//		web = (WebView) findViewById(R.id.webView);
		findViewById(R.id.btn_finish).setOnClickListener(this);
		
//		web = new WebView(this);  
	    web.getSettings().setJavaScriptEnabled(true);
//	    web.loadUrl("http://www.script-tutorials.com/demos/199/index.html");
	    Tools.ShowLoadingActivity(context);
	    web.loadUrl(URL);
	    web.setWebViewClient(new myWebClient());
	    web.setWebChromeClient(new WebChromeClient()  
	    {  
	           //The undocumented magic method override  
	           //Eclipse will swear at you if you try to put @Override here  
	        // For Android 3.0+
	        public void openFileChooser(ValueCallback<Uri> uploadMsg) {  

	            mUploadMessage = uploadMsg;  
	            Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
	            i.addCategory(Intent.CATEGORY_OPENABLE);  
	            i.setType("image/*");
	            WebViewCkwActivity.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FILECHOOSER_RESULTCODE);  

	           }

	        // For Android 3.0+
	           public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
	           mUploadMessage = uploadMsg;
	           Intent i = new Intent(Intent.ACTION_GET_CONTENT);
	           i.addCategory(Intent.CATEGORY_OPENABLE);
	           i.setType("*/*");
	           WebViewCkwActivity.this.startActivityForResult(
	           Intent.createChooser(i, "File Browser"),
	           FILECHOOSER_RESULTCODE);
	           }

	        //For Android 4.1
	           public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
	               mUploadMessage = uploadMsg;  
	               Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
	               i.addCategory(Intent.CATEGORY_OPENABLE);  
	               i.setType("image/*");  
	               WebViewCkwActivity.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), WebViewCkwActivity.FILECHOOSER_RESULTCODE );

	           }
	           @Override
	        public void onProgressChanged(WebView view, int newProgress) {
	        	super.onProgressChanged(view, newProgress);
	        	if(newProgress==100){
	        		Tools.DismissLoadingActivity(context);
				}
	        }
	    });  

//	    setContentView(web); 
	}

	public class myWebClient extends WebViewClient
	{
		
	    @Override
	    public void onPageStarted(WebView view, String url, Bitmap favicon) {
	        // TODO Auto-generated method stub
	        super.onPageStarted(view, url, favicon);
	    }

	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        // TODO Auto-generated method stub

	        view.loadUrl(url);
	        return true;

	    }

	    @Override
	    public void onPageFinished(WebView view, String url) {
	        // TODO Auto-generated method stub
	        super.onPageFinished(view, url);

//	        progressBar.setVisibility(View.GONE);
//	        Tools.ShowLoadingActivity(this);
	    }
	}

	//flipscreen not loading again
	@Override
	public void onConfigurationChanged(Configuration newConfig){        
	    super.onConfigurationChanged(newConfig);
	}

	// To handle "Back" key press event for WebView to go back to previous screen.
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
	        web.goBack();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.web_view_ckw, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_finish:
			finish();
			break;

		default:
			break;
		}
	}
	
	
}
