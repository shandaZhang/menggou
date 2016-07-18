package com.fujianmenggou.fm;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import dujc.dtools.ViewUtils;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;
import dujc.dtools.xutils.view.annotation.ViewInject;
import dujc.dtools.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.adapter.BuyLevelAdapter;
import com.fujianmenggou.atv.*;
import com.fujianmenggou.util.BaseFragment;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

public class IndexFragment extends BaseFragment implements OnClickListener{

	private View rootView;//缓存Fragment view 
	TextView tv_jinrishouyi;
	TextView tv_jinrikaihu;
	TextView tv_leijishouyi;
	TextView tv_username;

	@Override
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(rootView==null){
			rootView=layoutInflater.inflate(R.layout.fragment_index, null);
			tv_jinrikaihu=(TextView) rootView.findViewById(R.id.tv_jinrishouyi);
			tv_jinrishouyi=(TextView) rootView.findViewById(R.id.tv_jinrikaihu);
			tv_leijishouyi=(TextView) rootView.findViewById(R.id.tv_leijishouyi);
			tv_username=(TextView) rootView.findViewById(R.id.tv_username);
			
			rootView.findViewById(R.id.ll_jinrishouyi).setOnClickListener(this);
			rootView.findViewById(R.id.ll_leijishouyi).setOnClickListener(this);
			rootView.findViewById(R.id.ll_jinrikaihu).setOnClickListener(this);
			rootView.findViewById(R.id.iv_woyaoshoukuan).setOnClickListener(this);
			rootView.findViewById(R.id.tv_chakanlishishouyi).setOnClickListener(this);
			rootView.findViewById(R.id.tv_mashangtixian).setOnClickListener(this);
			rootView.findViewById(R.id.iv_shanghuguanli).setOnClickListener(this);
			rootView.findViewById(R.id.iv_xinzengshanghu).setOnClickListener(this);
			rootView.findViewById(R.id.iv_wodeshouru).setOnClickListener(this);
			rootView.findViewById(R.id.iv_jiaoyichaxun).setOnClickListener(this);
			rootView.findViewById(R.id.iv_shengjifeilv).setOnClickListener(this);
			ViewUtils.inject(this,rootView);
			initData();

		}
		tv_username.setText(userInfoPreferences.getString("nick_name","")+"("+userInfoPreferences.getString("group_name","")+")");
		tv_jinrishouyi.setText(GlobalVars.getTodayProfit(context));
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}

		return rootView;
	}

	private void initData() {
		// TODO Auto-generated method stub
		AjaxParams params=new AjaxParams();
		params.put("op", "GetIndexInfo");
		params.put("userID", GlobalVars.getUid(context));
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				try {
					JSONObject json=new JSONObject(t);
					if(json.getInt("result")==1){
						JSONArray array=json.getJSONArray("list");
						tv_jinrikaihu.setText(array.getJSONObject(0).getString("TodayLower"));
						tv_leijishouyi.setText(array.getJSONObject(0).getString("amountCount"));
//						tv.setText(array.getJSONObject(0).getString("totalConsumption"));
//						tv.setText(array.getJSONObject(0).getString("amount"));
						tv_jinrishouyi.setText(array.getJSONObject(0).getString("TodayProfit"));
//						tv.setText(array.getJSONObject(0).getString("LowerCount"));
//						tv.setText(array.getJSONObject(0).getString("bankCount"));
						//tv.setText(array.getJSONObject(0).getString("TodayLower"));
					}else{
						Tools.showTextToast(getActivity(), "数据获取失败，可能是网络问题");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	
	public void onClick(View v){
		switch (v.getId()) {
			case R.id.ll_jinrishouyi:

				break;

			case R.id.ll_leijishouyi:

				break;

			case R.id.ll_jinrikaihu:

				break;

			case R.id.iv_woyaoshoukuan:
				startActivity(new Intent(getActivity(), MobilePayActivity.class));
				break;

			case R.id.tv_chakanlishishouyi:
				startActivity(new Intent(getActivity(), FenRunJiLu.class));
				break;

			case R.id.tv_mashangtixian:
				startActivity(new Intent(getActivity(), MyIncomeActivity.class));
				break;

			case R.id.iv_shanghuguanli:
				startActivity(new Intent(getActivity(), XiaJiGuanLi.class));
				break;
			case R.id.iv_xinzengshanghu:
				startActivity(new Intent(getActivity(), Guide4AddBusiness.class));
				break;
			case R.id.iv_wodeshouru:
				startActivity(new Intent(getActivity(),MyIncomeActivity.class));
				break;
			case R.id.iv_jiaoyichaxun:
				startActivity(new Intent(getActivity(),TradeActivity.class));
				break;
			case R.id.iv_shengjifeilv:
				getBuyLevelInfo();
				break;
			default:
				break;
		}
	}
	
	/**
	 * 获取升级信息
	 */
	private void getBuyLevelInfo() {
		String group_id = userInfoPreferences.getString("group_id","");
		AjaxParams params = new AjaxParams();
		params.put("op","Rate");
		params.put("group_id",group_id);
		params.put("number","1");
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				LogUtils.d("------------------- " + s);
				startActivity(new Intent(getActivity(), BuyLevelActivity.class).putExtra("successContent", s));
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.showTextToast(context,strMsg);
			}
		});
	}
	
}
