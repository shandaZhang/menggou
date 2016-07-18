package com.fujianmenggou.atv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PayChanelActivity extends BaseActivity implements AdapterView.OnItemSelectedListener  {

	@ViewInject(R.id.sp_pay_channel) private Spinner sp_pay_channel;

	private List<HashMap<String,String>> channelList = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter adapter2;
	private String channelId,orderNo,money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paychanel);
		ViewUtils.inject(this);
		initFakeTitle();
		
		setTitle("选择支付通道");

		orderNo = getIntent().getStringExtra("orderNo");
		money = getIntent().getStringExtra("money");

		adapter2 =new SimpleAdapter(context, channelList, android.R.layout.simple_spinner_dropdown_item
				, new String[]{"name"}, new int[]{android.R.id.text1});
		sp_pay_channel.setAdapter(adapter2);


//			loadType();
		loadChannel();

		sp_pay_channel.setOnItemSelectedListener(this);
	}

	private void loadChannel() {
		AjaxParams params = new AjaxParams("op","GetPayChannel");
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				try {
					JSONObject jsonObject = new JSONObject(s);
					if (jsonObject.getInt("result")==1){
						JSONArray array = jsonObject.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							HashMap<String,String> map = new HashMap<String, String>();
							map.put("id", array.getJSONObject(i).getString("id"));
							map.put("name", array.getJSONObject(i).getString("name"));
							channelList.add(map);
						}
						adapter2.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.showTextToast(context, strMsg);
			}
		});
	}
	@OnClick(value={R.id.btn_recharge})
	@Override
	public void onClick(View v) {
//		startActivity(new Intent(context, RechargeActivity.class).putExtra("type", "0")
//				.putExtra("channelId", channelId).putExtra("orderNo", orderNo)
//				.putExtra("money", money));
		localPay();
	}

	private void localPay() {
		Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams();
		params.put("op","LocalPay");
		params.put("orderNo",orderNo);
		params.put("id",channelId);
		params.put("type","2");
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				LogUtils.i(s);
				try {
					JSONObject object = new JSONObject(s);
					if (object.getInt("result")==1){
						String url = object.getString("url");
						startActivity(new Intent(context, WebActivity.class).putExtra("url", url) );
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Tools.showTextToast(context,e.getMessage());
				}
				Tools.DismissLoadingActivity(context);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
				Tools.showTextToast(context,strMsg);
			}
		});
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()){
			case R.id.sp_pay_channel:
				channelId = channelList.get(position).get("id");
				break;
			default:break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		switch (parent.getId()){
			case R.id.sp_pay_channel:
				channelId = channelList.get(0).get("id");
				break;
			default:break;
		}
	}
}
