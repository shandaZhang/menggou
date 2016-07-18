package com.fujianmenggou.atv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.fujianmenggou.R;
import com.fujianmenggou.adapter.BuyLevelAdapter;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;
import com.viewpagerindicator.TabPageIndicator;

import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;

public class BuyLevelActivity extends BaseActivity {
	private TextView text_userName, text_levelName, text_rbdiscount, text_ybdiscount, text_xmunionpaydiscount;
	
	private BuyLevelAdapter mAdapter;
	private ViewPager mPager;
	private TabPageIndicator mIndicator;
	
	private List<HashMap<String,String>> levelList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buylevel);
		initFakeTitle();
		setTitle("升级费率");
		initView();
	}
	
	private void initView() {
		text_userName = (TextView) findViewById(R.id.text_userName);//用户名
		text_userName.setText(userInfoPreferences.getString("nick_name", ""));
		text_levelName = (TextView) findViewById(R.id.text_levelName);//用户等级
		String group_id = userInfoPreferences.getString("group_id","");
		switch (Integer.parseInt(group_id)) {
		case 2:
			text_levelName.setText("特级代理");
			break;
		case 3:
			text_levelName.setText("区域代理");
			break;
		case 4:
			text_levelName.setText("合作机构");
			break;
		case 5:
			text_levelName.setText("钻石微店");
			break;
		case 8:
			text_levelName.setText("普通微店");
			break;

		default:
			break;
		}
		text_rbdiscount = (TextView) findViewById(R.id.text_rbdiscount);//融宝
		text_ybdiscount = (TextView) findViewById(R.id.text_ybdiscount);//易宝
		text_xmunionpaydiscount = (TextView) findViewById(R.id.text_xmunionpaydiscount);//银联
		
		getBuyLevelInfo();
		
		mPager = (ViewPager)findViewById(R.id.pager);
		mIndicator = (TabPageIndicator)findViewById(R.id.indicator);
		//新建FragmentAdapter作为pager的adapter
		mAdapter = new BuyLevelAdapter(getSupportFragmentManager(), levelList);
		//viewpager需要adapter才能显示，类似listview那种设置
		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);
		
	}

	private void getBuyLevelInfo() {
		try {
			String s = this.getIntent().getStringExtra("successContent");
			JSONObject jsonObject = new JSONObject(s);
			if (jsonObject.getInt("result")==1){
				JSONArray array = jsonObject.getJSONArray("list");
				text_rbdiscount.setText(array.getJSONObject(0).getString("rbdiscount")+"%");
				text_ybdiscount.setText(array.getJSONObject(0).getString("ybdiscount")+"%");
				text_xmunionpaydiscount.setText(array.getJSONObject(0).getString("xmunionpaydiscount")+"%");
				
				for (int i = 1; i < array.length(); i++) {//从第二条开始记录
					HashMap<String,String> map = new HashMap<String, String>();

					String rbdiscount = array.getJSONObject(i).getString("rbdiscount");//融宝
					String ybdiscount = array.getJSONObject(i).getString("ybdiscount");//易宝
					String xmunionpaydiscount = array.getJSONObject(i).getString("xmunionpaydiscount");//银联在线
					
					map.put("rbdiscount", rbdiscount);
					map.put("ybdiscount", ybdiscount);
					map.put("xmunionpaydiscount", xmunionpaydiscount);
					
					levelList.add(map);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
