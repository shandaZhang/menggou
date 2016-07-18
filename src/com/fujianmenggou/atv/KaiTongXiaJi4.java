package com.fujianmenggou.atv;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class KaiTongXiaJi4 extends BaseActivity/* implements Callback */ implements OnItemSelectedListener{//不用shareSDK的短信了

	private EditText et11,et12;
	private Button btn1,btn2;
	private TextView tv1;
	private Spinner sp1,sp2,sp3;

	private String /*group,*/idcard="",str1,str2,phone,code,provinceCode,cityCode,areaCode;
	//private String role;
	private String[] from={"name"};
	private int[] to={android.R.id.text1};
	private ArrayList<HashMap<String, String>> data1,data2,data3;
	private SimpleAdapter adapter1,adapter2,adapter3;
	private long lastTime=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createsub4);

		initFakeTitle();
		
		http.configRequestExecutionRetryCount(3);
		
		et11=(EditText)findViewById(R.id.et11);
		et12=(EditText)findViewById(R.id.et12);
		tv1=(TextView)findViewById(R.id.tv1);
		btn1=(Button)findViewById(R.id.btn1);
		btn2=(Button)findViewById(R.id.btn2);

		sp1=(Spinner)findViewById(R.id.sp_1);
		sp2=(Spinner)findViewById(R.id.sp_2);
		sp3=(Spinner)findViewById(R.id.sp_3);

		initSpinner();

		sp1.setOnItemSelectedListener(this);
		sp2.setOnItemSelectedListener(this);
		sp3.setOnItemSelectedListener(this);

		tv1.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);

		idcard=getIntent().getStringExtra("idcard");
		setTitle("开通下级");
	}

	private void initSpinner() {
		// TODO Auto-generated method stub
		data1=new ArrayList<HashMap<String, String>>();
		data2=new ArrayList<HashMap<String, String>>();
		data3=new ArrayList<HashMap<String, String>>();

		adapter1=new SimpleAdapter(this, data1, android.R.layout.simple_spinner_dropdown_item
				, from, to);
		adapter2=new SimpleAdapter(this, data2, android.R.layout.simple_spinner_dropdown_item
				, from, to);
		adapter3=new SimpleAdapter(this, data3, android.R.layout.simple_spinner_dropdown_item
				, from, to);

		sp1.setAdapter(adapter1);
		sp2.setAdapter(adapter2);
		sp3.setAdapter(adapter3);
		
		AjaxParams params =new AjaxParams();
		params.put("op", "GetCity");
		params.put("levelID", "1");
		params.put("parentCode", "");
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				try {
					JSONObject jsonObject=new JSONObject(t);
					if(jsonObject.getInt("result")==1){
						JSONArray array=jsonObject.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							HashMap<String, String> map=new HashMap<String, String>();
							map.put("name", array.getJSONObject(i).getString("name"));
							map.put("code", array.getJSONObject(i).getString("code"));
							data1.add(map);
						}
						adapter1.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Tools.showTextToast(KaiTongXiaJi4.this, "获取省份列表失败，可能是网络问题");
			}
		});
		
	}

	@Override
	public void onClick(View v){
		str1=et11.getText().toString().trim();
		str2=et12.getText().toString().trim();
		switch (v.getId()) {
		case R.id.tv1://开通协议
			AgreementActivity.setO(0);
			startActivity(new Intent(KaiTongXiaJi4.this, AgreementActivity.class));
			break;

		case R.id.btn1:
			//SMSSDK.submitVerificationCode(country, str1, str2);//不用shareSDK的短信了
			if(provinceCode==null){
				Tools.showTextToast(KaiTongXiaJi4.this, "省份不能为空");
				return;
			}
			if(cityCode==null){
				Tools.showTextToast(KaiTongXiaJi4.this, "城市不能为空");
				return;
			}
			if(areaCode==null){
				Tools.showTextToast(KaiTongXiaJi4.this, "县级不能为空");
				return;
			}
			if(!str1.equals(phone)){
				Tools.showTextToast(KaiTongXiaJi4.this, "手机号已更改，请重新获取验证码");
				return;
			}
			if(str2.equals(code)){
				GlobalVars.nextLevel.setShouJiHao(str1);
				//List<UserInfo> tmp2 = db.findAllByWhere(UserInfo.class, "uid='"+GlobalVars.getUid(context)+"'");
				//UserInfo user = tmp2.get(tmp2.size()-1);
				AjaxParams params=new AjaxParams();
				params.put("op", "addUser");
				params.put("email", GlobalVars.nextLevel.getJieSuanFeiLv());
				params.put("status", "0");
				params.put("mobile", str1);
				params.put("idCard", idcard);
				params.put("user_name", str1);
				params.put("lower_number", GlobalVars.nextLevel.getKaiHuShangXian());
				params.put("IDCardHand", GlobalVars.nextLevel.getIDCardHand());
				params.put("IDCardHeads", GlobalVars.nextLevel.getIDCardHeads());
				params.put("IDCardTails", GlobalVars.nextLevel.getIDCardTails());
				params.put("bankCardHeads", GlobalVars.nextLevel.getBankCardHeads());
				params.put("group_id", GlobalVars.nextLevel.getGroup_id());
				params.put("name", GlobalVars.nextLevel.getXingMing());
				params.put("user_id", GlobalVars.getUid(context));
				params.put("password", GlobalVars.nextLevel.getDengLuMiMa());
				params.put("pay_password", GlobalVars.nextLevel.getZhiFuMiMa());
				params.put("card_number", GlobalVars.nextLevel.getYinHangKa());
				params.put("bank_account", GlobalVars.nextLevel.getBank_account());
				params.put("bank", GlobalVars.nextLevel.getBank());
				params.put("bank_name", GlobalVars.nextLevel.getXingMing());
				params.put("provinceCode", provinceCode+"");
				params.put("cityCode", cityCode+"");
				params.put("areaCode", areaCode+"");

				http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						try {
							JSONObject json=new JSONObject(t);
							if(json.getInt("result")==1){
								Tools.showTextToast(KaiTongXiaJi4.this, "开通成功");
								finish();
							}else if(json.getInt("result")==-2){
								Tools.showTextToast(KaiTongXiaJi4.this, json.getString("msg"));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}else{
				Tools.showTextToast(KaiTongXiaJi4.this, "请输入正确的验证码");
				LogUtils.i("str2="+str2+"    code="+code);
			}
			break;

		case R.id.btn2:
			if(str1.isEmpty()||str1==null)
				Tools.showTextToast(KaiTongXiaJi4.this, "手机号不能为空");
			else if(System.currentTimeMillis()-lastTime<60*1000){
				Tools.showTextToast(KaiTongXiaJi4.this, "短信已发送，请勿频繁操作");
			}else{
				//SMSSDK.getSupportedCountries();//= 852 86 61//不用shareSDK的短信了
				//SMSSDK.getVerificationCode(country, str1);//不用shareSDK的短信了
				AjaxParams params=new AjaxParams();
				params.put("op", "SendMsg");
				params.put("Phone", str1);
				LogUtils.i(params.getParamString());
				http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						LogUtils.i(t);
						try {
							JSONObject json=new JSONObject(t);
							if(json.getInt("result")==1){
				/*btn2.setEnabled(false);
				thread.start();*/
								lastTime=System.currentTimeMillis();
								Tools.showTextToast(KaiTongXiaJi4.this, "短信发送成功");
								code=json.getString("code");
								phone=str1;
							}else{
								Tools.showTextToast(KaiTongXiaJi4.this, "短信发送失败");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.sp_1:
			provinceCode=data1.get(position).get("code");
			data2.clear();
			adapter2.notifyDataSetChanged();
		{
			AjaxParams params =new AjaxParams();
			params.put("op", "GetCity");
			params.put("levelID", "2");
			params.put("parentCode", provinceCode);
			http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					super.onSuccess(t);
					try {
						JSONObject jsonObject=new JSONObject(t);
						if(jsonObject.getInt("result")==1){
							JSONArray array=jsonObject.getJSONArray("list");
							for (int i = 0; i < array.length(); i++) {
								HashMap<String, String> map=new HashMap<String, String>();
								map.put("name", array.getJSONObject(i).getString("name"));
								map.put("code", array.getJSONObject(i).getString("code"));
								data2.add(map);
							}
							adapter2.notifyDataSetChanged();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					Tools.showTextToast(KaiTongXiaJi4.this, "获取城市列表失败，可能是网络问题");
				}
			});
		}
			break;

		case R.id.sp_2:
			cityCode=data2.get(position).get("code");
			data3.clear();
			adapter3.notifyDataSetChanged();
		{
			AjaxParams params =new AjaxParams();
			params.put("op", "GetCity");
			params.put("levelID", "3");
			params.put("parentCode", cityCode);
			http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					super.onSuccess(t);
					try {
						JSONObject jsonObject=new JSONObject(t);
						if(jsonObject.getInt("result")==1){
							JSONArray array=jsonObject.getJSONArray("list");
							for (int i = 0; i < array.length(); i++) {
								HashMap<String, String> map=new HashMap<String, String>();
								map.put("name", array.getJSONObject(i).getString("name"));
								map.put("code", array.getJSONObject(i).getString("code"));
								data3.add(map);
							}
							adapter3.notifyDataSetChanged();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					Tools.showTextToast(KaiTongXiaJi4.this, "获取县级列表失败，可能是网络问题");
				}
			});
		}
			break;

		case R.id.sp_3:
			areaCode=data3.get(position).get("code");
			break;

		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.sp_1:
			data2.clear();
			adapter2.notifyDataSetChanged();
		case R.id.sp_2:
			data3.clear();
			adapter3.notifyDataSetChanged();
		default:
			break;
		}
	}

	/*Thread thread =new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1000);
				restTime--;
				handler.sendEmptyMessage(restTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	});
	Handler handler =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			btn2.setText(msg.what+"");
			if(msg.what==0){
				btn2.setText("获取验证码");
				btn2.setEnabled(true);
			}
		}
	};*/
}
