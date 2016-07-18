package com.fujianmenggou.atv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.ckw.TcDialog;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

public class Register1 extends BaseActivity {

	private Button btn_register,btn2;
	private EditText editText1,editText2,editText3,et12;
	private long lastTime=0;
	private String code;
	private int mCount = 60;
	private Dialog dialog;
	private CheckBox check_agree;
	private TextView text_agree;

	private Handler  mHandler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			mCount--;
			if (mCount > 0) {
				btn2.setText(""+mCount);
				mHandler.postDelayed(this, 1000);
			} else {
				btn2.setText("获取验证码");
				mHandler.removeCallbacks(this);
			}
		}
	};

	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String str = (String) msg.obj;
				TcDialog dialog = new TcDialog(Register1.this);
				dialog.showTcInfo(str);
				break;

			default:
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		initFakeTitle();
		
		setTitle("注册");

		dialog = new AlertDialog.Builder(context)
				.setPositiveButton("确定",null)
				.create();

		btn_register = (Button) findViewById(R.id.btn_register);
		editText1 = (EditText) findViewById(R.id.et_username);
		editText2 = (EditText) findViewById(R.id.et_password);
		editText3 = (EditText) findViewById(R.id.et_salt);
		btn2=(Button)findViewById(R.id.btn2);
		et12=(EditText)findViewById(R.id.et12);
		check_agree = (CheckBox) findViewById(R.id.checkbox_agree);
		text_agree = (TextView) findViewById(R.id.text_agree);
		
		text_agree.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		btn2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btn_register://注册按钮
			if (check_agree.isChecked()) {				
				if (TextUtils.isEmpty(editText1.getText().toString().trim())){
					Tools.showTextToast(context,"手机号不能为空");
					return;
				}
				if (TextUtils.isEmpty(editText2.getText())){
					Tools.showTextToast(context,"密码不能为空");
					return;
				}
				if (TextUtils.isEmpty(editText3.getText().toString().trim())){
					Tools.showTextToast(context,"邀请码不能为空");
					return;
				}
				if (TextUtils.isEmpty(et12.getText().toString().trim())){
					Tools.showTextToast(context,"验证码不能为空");
					return;
				}
				if(code!=null){
					if (!code.equals(et12.getText().toString().trim())){
						Tools.showTextToast(context,"验证码不正确");
						return;
					}
				}
				register(editText1.getText().toString().trim(),editText2.getText().toString(),editText3.getText().toString().trim());
			}else {
				Toast.makeText(Register1.this, "请先同意用户协议", Toast.LENGTH_LONG).show();
			}
			break;
			case R.id.btn2://获取验证码按钮
				if(TextUtils.isEmpty(editText1.getText().toString().trim()))
					Tools.showTextToast(context, "手机号不能为空");
				else if(System.currentTimeMillis()-lastTime<60*1000){
					Tools.showTextToast(context, "短信已发送，请勿频繁操作");
				}else{
					btn2.setEnabled(false);
					btn2.setText("验证码发送中");
					AjaxParams params=new AjaxParams();
					params.put("op", "SendMsg");
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
									btn2.setEnabled(false);
									mCount = 60;
									mHandler.postDelayed(runnable, 1000);//延迟一秒执行
									lastTime=System.currentTimeMillis();
									//Tools.showTextToast(context, "短信发送成功");
									dialog.setTitle("短信发送成功");
									if (!dialog.isShowing())
										dialog.show();
									code=json.getString("code");
								}else{
									btn2.setEnabled(true);
									btn2.setText("获取验证码");
//									Tools.showTextToast(context, "短信发送失败");
									dialog.setTitle("短信发送失败");
									if (!dialog.isShowing())
										dialog.show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
				break;
			case R.id.text_agree:
				String agreeStr = getResources().getString(R.string.text_agree);
				System.out.println(agreeStr);
				Message msg = handler.obtainMessage(0);
				msg.obj = agreeStr;
				msg.sendToTarget();
				break;
		default:
			break;
		}
		
	}

	private void register(String user_name,String password,String salt) {
	//?op=easyRegister&user_name=18059089733&password=123123&salt=121231
		Tools.ShowLoadingActivity(context);
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("op","easyRegister");
		ajaxParams.put("user_name",user_name);
		ajaxParams.put("password",password);
		ajaxParams.put("mobile",salt);
		http.post(GlobalVars.url, ajaxParams, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				try {
					JSONObject object = new JSONObject(s);
					LogUtils.i(s);
					if (object.getInt("result")==1){
						Tools.showTextToast(context,"注册成功");
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
