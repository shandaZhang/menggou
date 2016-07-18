package com.fujianmenggou.atv;

import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.fujianmenggou.R;
import com.fujianmenggou.adapter.AdapterWithSpace;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.Tools;

public class BianMinActivity extends BaseActivity {

	private ListView lv_listView;
	private WebView wv_webView;
	private TextView tv_textView;
	private int position;
	private TextView tv_title,tv_back;
	private boolean canBack = false,lv = false,wv = false,tv = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bianmingou);
		//initFakeTitle();
		initTitle();

		lv_listView = (ListView) findViewById(R.id.lv_listView);
		wv_webView = (WebView) findViewById(R.id.wv_webView);
		tv_textView = (TextView) findViewById(R.id.tv_textView);

		wv_webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.0.3; fr-fr"
				+ "; MIDC410 Build/IML74K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari"
				+ "/534.30");
		wv_webView.getSettings().setJavaScriptEnabled(true);
		wv_webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				Tools.DismissLoadingActivity(context);
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				if(url.equals("http://m.ctrip.com/wap")){
					//wv_webView.loadUrl("http://m.ctrip.com/html5/");
					return true;
				}else
					return super.shouldOverrideUrlLoading(view, url);
			}
		});

		position = getIntent().getIntExtra("position", -1);
		initUI();
	}

	private void initTitle() {
		tv_title = (TextView)findViewById(R.id.tv_title);
		tv_back = (TextView)findViewById(R.id.tv_back);
		tv_title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(canBack){
					lv_listView.setVisibility(View.VISIBLE);
					if (wv)
						wv_webView.setVisibility(View.GONE);
					if (tv)
						tv_textView.setVisibility(View.GONE);
					canBack = false;
				}else{
					finish();
				}
			}
		});
	}

	protected void setTitle(String title){
		tv_title.setText(title);
	}

	private void initUI() {
		// TODO Auto-generated method stub
		switch (position) {
			case 0:
				setTitle("吃喝玩乐");
				lv_listView.setVisibility(View.GONE);
				wv_webView.setVisibility(View.VISIBLE);
				//wv = true;tv = false;canBack = true;
				Tools.ShowLoadingActivity(context);
				wv_webView.loadUrl("http://m.nuomi.com");
				break;

			case 1:
				setTitle("盟友圈");
			{
				ArrayList<HashMap<String, Object>> mList=new ArrayList<HashMap<String, Object>>();
				int [] image={R.drawable.mengyou_1,R.drawable.mengyou_2
						,R.drawable.mengyou_3,R.drawable.mengyou_4,R.drawable.mengyou_5
						,R.drawable.mengyou_6,R.drawable.mengyou_7,R.drawable.mengyou_8};
				String[] name={"消息列表","盟友动态","我的盟粉","我的盟友","邀请粉丝","添加盟友","附近的人","扫一扫"};
				for (int i = 0; i < name.length; i++) {
					HashMap<String, Object> map=new HashMap<String, Object>();
					map.put("image", image[i]);
					map.put("name", name[i]);
					mList.add(map);
				}
				AdapterWithSpace mAdapter = new AdapterWithSpace(context,mList);
				lv_listView.setAdapter(mAdapter);
			}
			lv_listView.setVisibility(View.VISIBLE);
			lv_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					lv_listView.setVisibility(View.GONE);
					tv_textView.setText(Html.fromHtml("<font color=#0000ff>功能升级中……</font>"));
					tv_textView.setVisibility(View.VISIBLE);
					tv = true;wv = false;canBack = true;
				}
			});
			wv_webView.setVisibility(View.GONE);
				break;

			case 2:
				setTitle("便民服务");
			{
				ArrayList<HashMap<String, Object>> mList=new ArrayList<HashMap<String, Object>>();
				int [] image={R.drawable.kakazhuanzhang,R.drawable.xinyongkahuankuan
						,R.drawable.huafeichongzhi,R.drawable.cheliangweizhang
						,R.drawable.shuidianmei,R.drawable.caipiaoyewu};
				String[] name={"卡卡转账","信用卡还款","话费充值","车辆违章","水电煤"/*,"彩票服务"*/};
				for (int i = 0; i < name.length; i++) {
					HashMap<String, Object> map=new HashMap<String, Object>();
					map.put("image", image[i]);
					map.put("name", name[i]);
					mList.add(map);
				}
				SimpleAdapter mAdapter = new SimpleAdapter(BianMinActivity.this, mList, R.layout.item_simlpe_list
						, new String[]{"image","name"}, new int[]{R.id.iv_image,R.id.tv_name});
				lv_listView.setAdapter(mAdapter);
			}
			lv_listView.setVisibility(View.VISIBLE);
			lv_listView.setOnItemClickListener(this);
			wv_webView.setVisibility(View.GONE);
			//wv = true;tv = false;canBack = true;
			break;

			case 3:
				setTitle("商旅出行");
				lv_listView.setVisibility(View.GONE);
				wv_webView.setVisibility(View.VISIBLE);
				//wv = true;tv = false;canBack = true;
				Tools.ShowLoadingActivity(context);
				wv_webView.loadUrl("http://m.ctrip.com/html5/");
				break;

//		case 3:
//			setTitle("爱心资金");
//			lv_listView.setVisibility(View.GONE);
//			wv_webView.setVisibility(View.VISIBLE);
//			Tools.ShowLoadingActivity(context);
//			wv_webView.loadUrl("http://www.ccafc.org.cn");
//			break;

		/*case 4:	//医院挂号去掉，大于4的都要减一
			setTitle("医院挂号");
			lv_listView.setVisibility(View.GONE);
			wv_webView.setVisibility(View.VISIBLE);
			Tools.ShowLoadingActivity(context);
			wv_webView.loadUrl("http://jiankang.baidu.com/mNew");
			break;*/

			case 4:
				setTitle("金融中心");
			{
				ArrayList<HashMap<String, Object>> mList=new ArrayList<HashMap<String, Object>>();
				int [] image={R.drawable.posyewu,R.drawable.licaichanpin
						,R.drawable.baoxianyewu,R.drawable.licaidaikuan
						,R.drawable.p2plicai};
				String[] name={"POS业务","理财产品","保险业务","理财贷款","P2P理财"};
				for (int i = 0; i < name.length; i++) {
					HashMap<String, Object> map=new HashMap<String, Object>();
					map.put("image", image[i]);
					map.put("name", name[i]);
					mList.add(map);
				}
				SimpleAdapter mAdapter = new SimpleAdapter(BianMinActivity.this, mList, R.layout.item_simlpe_list
						, new String[]{"image","name"}, new int[]{R.id.iv_image,R.id.tv_name});
				lv_listView.setAdapter(mAdapter);
			}
			lv_listView.setVisibility(View.VISIBLE);
			lv_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					lv_listView.setVisibility(View.GONE);
					tv_textView.setText("…详情请咨询400-0889779…");
					tv_textView.setVisibility(View.VISIBLE);
					tv = true; wv = false;canBack = true;
				}
			});
			wv_webView.setVisibility(View.GONE);
				break;
			case 5:
				setTitle("便民购");
				lv_listView.setVisibility(View.GONE);
				wv_webView.setVisibility(View.VISIBLE);
				//wv = true;tv = false;canBack = true;
				Tools.ShowLoadingActivity(context);
				wv_webView.loadUrl(userInfoPreferences.getString("pKey",""));
				break;
			case 6:
				setTitle("微商盟");
				lv_listView.setVisibility(View.GONE);
				wv_webView.setVisibility(View.VISIBLE);
				//wv = true;tv = false;canBack = true;
				Tools.ShowLoadingActivity(context);
				wv_webView.loadUrl(userInfoPreferences.getString("wKey",""));
				break;
			default:
			case -1:
				Tools.showTextToast(context, "界面初始化失败，请返回重试");
				break;
		}
	}

	private long lastTime = 0;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		long currentTime = System.currentTimeMillis();
		if ((keyCode == KeyEvent.KEYCODE_BACK) && wv_webView.canGoBack()) {
			wv_webView.goBack();
			return true;
		}
		if(currentTime-lastTime<=1000){
			finish();
		}
		lastTime = currentTime;
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
		wv = true;tv = false;canBack = true;
		if(parent.getId()==R.id.lv_listView)
			switch (position) {
				case 0:
					Tools.showTextToast(context,"卡卡转账");
					break;
				case 1:
					Tools.showTextToast(context,"信用卡还款");
					break;
				case 2:
					setTitle("话费充值");
					lv_listView.setVisibility(View.GONE);
					wv_webView.setVisibility(View.VISIBLE);
					Tools.ShowLoadingActivity(context);
					wv_webView.loadUrl("http://wvs.m.taobao.com");
					break;
				case 3:
					Tools.showTextToast(context,"车辆违章");
					break;
				case 5:
					setTitle("彩票服务");
					lv_listView.setVisibility(View.GONE);
					wv_webView.setVisibility(View.VISIBLE);
					Tools.ShowLoadingActivity(context);
					wv_webView.loadUrl("http://m.500.com");
					break;
				case 4:
					setTitle("生活缴费");
					lv_listView.setVisibility(View.GONE);
					wv_webView.setVisibility(View.VISIBLE);
					Tools.ShowLoadingActivity(context);
					wv_webView.loadUrl("http://wvs.m.taobao.com");
					break;
				default:
					break;
			}
	}

	@Override
	public void onBackPressed() {
		if(canBack){
			lv_listView.setVisibility(View.VISIBLE);
			if (wv)
				wv_webView.setVisibility(View.GONE);
			if (tv)
				tv_textView.setVisibility(View.GONE);
		}else
			super.onBackPressed();
	}
}
