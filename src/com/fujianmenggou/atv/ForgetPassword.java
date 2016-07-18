package com.fujianmenggou.atv;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

public class ForgetPassword extends BaseActivity {

	private Button btn_register,btn2;
	private EditText editText1,editText2,editText3,et12;
	private long lastTime=0;
	private String code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		initFakeTitle();
		
		setTitle("忘记密码");

		btn_register = (Button) findViewById(R.id.btn_register);
		editText1 = (EditText) findViewById(R.id.et_username);
		editText2 = (EditText) findViewById(R.id.et_password);
		editText3 = (EditText) findViewById(R.id.et_salt);
		btn2=(Button)findViewById(R.id.btn2);
		et12=(EditText)findViewById(R.id.et12);
		editText3.setVisibility(View.GONE);
		btn_register.setOnClickListener(this);
		btn_register.setText("找回");
		btn2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btn_register:
			if (TextUtils.isEmpty(editText1.getText().toString().trim())){
				Tools.showTextToast(context,"手机号不能为空");
				return;
			}
			if (TextUtils.isEmpty(editText2.getText())){
				Tools.showTextToast(context,"密码不能为空");
				return;
			}
//			if (TextUtils.isEmpty(editText3.getText().toString().trim())){
//				Tools.showTextToast(context,"邀请码不能为空");
//				return;
//			}
			if (TextUtils.isEmpty(et12.getText().toString().trim())){
				Tools.showTextToast(context,"验证码不能为空");
				return;
			}
			if (!code.equals(et12.getText().toString().trim())){
				Tools.showTextToast(context,"验证码不正确");
				return;
			}
			register(editText1.getText().toString().trim(),editText2.getText().toString());
			break;
			case R.id.btn2:
				if(TextUtils.isEmpty(editText1.getText().toString().trim()))
					Tools.showTextToast(context, "手机号不能为空");
				else if(System.currentTimeMillis()-lastTime<60*1000){
					Tools.showTextToast(context, "短信已发送，请勿频繁操作");
				}else{
					//SMSSDK.getSupportedCountries();//= 852 86 61//不用shareSDK的短信了
					//SMSSDK.getVerificationCode(country, str1);//不用shareSDK的短信了
					AjaxParams params=new AjaxParams();
					params.put("op", "SendMsg");
					params.put("typeID", "1");
					params.put("Phone", editText1.getText().toString().trim());
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
									Tools.showTextToast(context, "短信发送成功");
									code=json.getString("code");
								}else{
									Tools.showTextToast(context, "短信发送失败");
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

	private void register(String user_name,String password) {
	//?op=easyRegister&user_name=18059089733&password=123123&salt=121231
		Tools.ShowLoadingActivity(context);
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("op","EditPassword");
		ajaxParams.put("userName",user_name);
		ajaxParams.put("newPassword",password);
		ajaxParams.put("code",code);
		http.post(GlobalVars.url, ajaxParams, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				try {
					JSONObject object = new JSONObject(s);
					LogUtils.i(s);
					if (object.getInt("result")==1){
						Tools.showTextToast(context,"找回密码成功");
					}else {
						Tools.showTextToast(context,object.getString("msg")+"");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Tools.DismissLoadingActivity(context);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
			}
		});
	}

}
