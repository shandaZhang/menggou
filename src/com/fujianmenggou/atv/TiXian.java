package com.fujianmenggou.atv;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
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

public class TiXian extends BaseActivity/* implements AdapterView.OnItemSelectedListener*/ {

	public static final String MONEY_COUNT = "MONEY_COUNT";

	private String id/*,type*/, moneyCount;
	@ViewInject(R.id.et_zhuanchujine)TextView et_zhuanchujine;
	@ViewInject(R.id.et_zhifumima)TextView et_zhifumima;
	@ViewInject(R.id.tv_yu_e)TextView tv_yu_e;
	@ViewInject(R.id.tv_bankname)TextView tv_bankname;
	@ViewInject(R.id.tv_bankcode)TextView tv_bankcode;
	@ViewInject(R.id.tv_notice) private TextView tv_notice;
	@ViewInject(R.id.tv_pay_type) private TextView tv_pay_type;

//	private List<HashMap<String,String>> typeList = new ArrayList<HashMap<String, String>>();
//	private SimpleAdapter adapter1;
	private String typeId = "", typeName = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tixian);
		initFakeTitle();
		
		id=getIntent().getStringExtra("id");
		moneyCount = getIntent().getStringExtra(MONEY_COUNT);
		if (TextUtils.isEmpty(moneyCount)){
			moneyCount = "0";
		}

		setTitle("提  现");
		ViewUtils.inject(this);
		
		try {
			tv_yu_e.setText(moneyCount);
			tv_bankname.setText(GlobalVars.getBank_name(context));
			tv_bankcode.setText("*"+GlobalVars.getCard_number(context).substring(GlobalVars.getCard_number(context).length()-4, GlobalVars.getCard_number(context).length()));
		}catch (Exception e){
			e.printStackTrace();
			Tools.showTextToast(context,"请在认证资料中绑定银行卡后再进行提现！");
		}
//		adapter1 =new SimpleAdapter(context, typeList, android.R.layout.simple_spinner_dropdown_item
//					, new String[]{"name"}, new int[]{android.R.id.text1});
//		sp_pay_type.setAdapter(adapter1);
//		sp_pay_type.setOnItemSelectedListener(this);

		loadType();
	}

	@OnClick(value={R.id.tv_back,R.id.btn_zhuanchu})
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;

		case R.id.btn_zhuanchu:
			String str1=et_zhuanchujine.getText().toString().trim();
			String str2=et_zhifumima.getText().toString();
			if(str1.isEmpty()||str1==null){
				Tools.showTextToast(TiXian.this, "金额不能为空");
			}else if(str1.startsWith(".")){
				Tools.showTextToast(TiXian.this, "金额不能以.开头");
			}else if(str2.isEmpty()||str2==null){
				Tools.showTextToast(TiXian.this, "支付密码不能为空");
			}else{
				Tools.ShowLoadingActivity(context);
				AjaxParams params=new AjaxParams();
				params.put("op", "withdraw");
				params.put("userID", GlobalVars.getUid(context));
				params.put("channelID", id);
				params.put("orderAmount", str1);
				params.put("payPwd", str2);
				params.put("typeID", typeId);
				http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						Tools.DismissLoadingActivity(context);
						Tools.showTextToast(TiXian.this, "出现错误，请查看输入参数，或尝试重新登陆");
					}
					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Tools.DismissLoadingActivity(context);
						LogUtils.i(t);
						try {
							JSONObject json=new JSONObject(t);
							if(json.getInt("result")==1){
								Tools.showTextToast(TiXian.this, "转出成功，请注意账户金额变动");
								finish();
							}else if(json.getInt("result")==-2){
								Tools.showTextToast(TiXian.this, json.getString("msg"));
							}else{
								Tools.showTextToast(TiXian.this, "出现错误，请查看输入参数");
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Tools.showTextToast(TiXian.this, "出现错误，请查看输入参数，或尝试重新登陆");
						}
					}
				});
			}
			
			break;
		default:
			break;
		}
	}

	private void loadType() {
		Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams("op","GetAccountType");
		params.put("payChannelID", id);//2015-8-10 12:50:03 by djc
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				try {
					JSONObject jsonObject = new JSONObject(s);
					if (jsonObject.getInt("result")==1){
						tv_notice.setText(jsonObject.getString("prompt")+"");
						JSONArray array = jsonObject.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
//							HashMap<String,String> map = new HashMap<String, String>();
//							map.put("id", array.getJSONObject(i).getString("id"));
//							map.put("name", array.getJSONObject(i).getString("name"));
//							typeList.add(map);
							typeId = array.getJSONObject(i).getString("id");
							typeName = array.getJSONObject(i).getString("name");
							tv_pay_type.setText(typeName);
						}
						//adapter1.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Tools.DismissLoadingActivity(context);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.showTextToast(context, strMsg);
				Tools.DismissLoadingActivity(context);
			}
		});
	}

//	@Override
//	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//		switch (parent.getId()){
//			case R.id.sp_pay_type:
//				type = typeList.get(position).get("id");
//				break;
//			default:break;
//		}
//	}
//
//	@Override
//	public void onNothingSelected(AdapterView<?> parent) {
//		switch (parent.getId()){
//			case R.id.sp_pay_type:
//				type = typeList.get(0).get("id");
//				break;
//			default:break;
//		}
//	}
}
