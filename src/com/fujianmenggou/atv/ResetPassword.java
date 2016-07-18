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

public class ResetPassword extends BaseActivity {

	private Button btn_submit;
	private EditText editText1,editText2,editText3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reset_password);

		initFakeTitle();
		
		setTitle("修改密码");

		editText1 = (EditText) findViewById(R.id.et_text1);
		editText2 = (EditText) findViewById(R.id.et_text2);
		editText3 = (EditText) findViewById(R.id.et_text3);
		btn_submit=(Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btn_submit:
			if (TextUtils.isEmpty(editText1.getText().toString().trim())){
				Tools.showTextToast(context,"旧密码不能为空");
				return;
			}
			if (TextUtils.isEmpty(editText2.getText())){
				Tools.showTextToast(context,"新密码不能为空");
				return;
			}
			if (TextUtils.isEmpty(editText3.getText().toString().trim())){
				Tools.showTextToast(context,"请确认新密码");
				return;
			}
			if (!editText2.getText().toString().equals(editText3.getText().toString())){
				Tools.showTextToast(context,"两次输入的密码不相同");
				return;
			}
			reset(editText1.getText().toString().trim(),editText2.getText().toString());
			break;
		default:
			break;
		}
		
	}

	private void reset(String oldpassword,String password) {
	//?op=easyRegister&user_name=18059089733&password=123123&salt=121231
		Tools.ShowLoadingActivity(context);
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("op","SetUsersPwd");
		ajaxParams.put("user_id",GlobalVars.getUid(context));
		ajaxParams.put("password",password);
		ajaxParams.put("oldpassword",oldpassword);
		LogUtils.d("-----"+ajaxParams.toString());
		http.get(GlobalVars.url, ajaxParams, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				try {
					JSONObject object = new JSONObject(s);
					LogUtils.i("-----------"+s);
					if (object.getInt("result")==1){
						Tools.showTextToast(context,"修改登录密码成功");
						finish();
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
