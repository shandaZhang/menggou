package com.fujianmenggou;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.fujianmenggou.atv.ForgetPassword;
import com.fujianmenggou.atv.Register1;
import com.fujianmenggou.ckw.MyApp;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import dujc.dtools.ViewUtils;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;
import dujc.dtools.xutils.view.annotation.ViewInject;
import dujc.dtools.xutils.view.annotation.event.OnClick;

public class LoginActivity extends BaseActivity implements ViewFactory {
	@ViewInject(R.id.auto_username)
	EditText auto_username;
	@ViewInject(R.id.et_password)
	EditText et_password;
	@ViewInject(R.id.tv_forgotpassword)
	TextView tv_forgotpassword;
	@ViewInject(R.id.btn_register)
	TextView btn_register;

	private long exitTime;// 用于两次单击返回键退出

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
		if (!TextUtils.isEmpty(share.getString("uid", ""))) {
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			finish();
		}

		tv_forgotpassword.setOnClickListener(this);
		btn_register.setOnClickListener(this);

		// initFakeTitle();
		// hideBack();
	}

	@OnClick(R.id.btn_login)
	public void onClick2DengLu(View v) {
		final String yonghuming = auto_username.getText().toString();
		String mima = et_password.getText().toString();
		// 调试时，不判断，直接跳到下一页
		// if (yonghuming.isEmpty() || yonghuming == null) {
		// Tools.showTextToast(LoginActivity.this, "用户名不能为空");
		// } else if (mima.isEmpty() || mima == null) {
		// Tools.showTextToast(LoginActivity.this, "密码不能为空");
		// } else
		{
			Tools.ShowLoadingActivity(context);
			AjaxParams params = new AjaxParams();
			params.put("op", "userLogin");
			// params.put("username", yonghuming);
			// params.put("password", mima);
			params.put("username", "13906929659");
			params.put("password", "000000");
			http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					Tools.DismissLoadingActivity(context);
				}

				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					super.onSuccess(t);
					Tools.DismissLoadingActivity(context);
					LogUtils.i(t);
					try {
						JSONObject jsonObject = new JSONObject(t);
						if (jsonObject.getInt("result") == 1) {
							JSONArray array = jsonObject.getJSONArray("list");
							if (array.length() > 0) {
								String id = array.getJSONObject(0).getString(
										"id");
								int group_id = array.getJSONObject(0).getInt(
										"group_id");
								String group_name = array.getJSONObject(0)
										.getString("group_name");
								String user_name = array.getJSONObject(0)
										.getString("user_name");
								String nick_name = array.getJSONObject(0)
										.getString("nick_name");
								String mobile = array.getJSONObject(0)
										.getString("mobile");

								Editor editor = share.edit();
								editor.putString("uid", id);
								editor.putInt("gid", group_id);
								editor.putString("nick_name", nick_name);
								editor.putString("balance", array
										.getJSONObject(0).getString("balance"));
								editor.putString(
										"card_number",
										array.getJSONObject(0).getString(
												"card_number"));
								editor.putString(
										"lowerCount",
										array.getJSONObject(0).getString(
												"lowerCount"));
								editor.putString(
										"todayProfit",
										array.getJSONObject(0).getString(
												"todayProfit"));
								editor.putString("bank_name", array
										.getJSONObject(0)
										.getString("bank_name"));
								editor.putString("salt", array.getJSONObject(0)
										.getString("salt"));
								editor.commit();

								Editor info = userInfoPreferences.edit();
								info.putString("id", id);
								info.putString("mobile", mobile);
								info.putString("nick_name", nick_name);
								info.putString("user_name", user_name);
								info.putString("group_id",
										String.valueOf(group_id));
								info.putString("group_name", group_name);
								info.putString("idCard", array.getJSONObject(0)
										.getString("idCard"));
								info.putString(
										"id_card_heads",
										array.getJSONObject(0).getString(
												"id_card_heads"));
								info.putString(
										"idCard_tails",
										array.getJSONObject(0).getString(
												"idCard_tails"));
								info.putString(
										"bank_card_heads",
										array.getJSONObject(0).getString(
												"bank_card_heads"));
								info.putString("pKey", array.getJSONObject(0)
										.getString("pKey"));
								info.putString("wKey", array.getJSONObject(0)
										.getString("wKey"));
								info.putString(
										"bank_card_tails",
										array.getJSONObject(0).getString(
												"bank_card_tails"));
								info.putString(
										"id_card_hand",
										array.getJSONObject(0).getString(
												"id_card_hand"));
								info.putString(
										"card_number",
										array.getJSONObject(0).getString(
												"card_number"));
								info.putString("salt", array.getJSONObject(0)
										.getString("salt"));
								info.putString("status", array.getJSONObject(0)
										.getString("status"));
								info.putString("email", array.getJSONObject(0)
										.getString("email"));
								info.putString(
										"account_name",
										array.getJSONObject(0).getString(
												"account_name"));
								info.putString("bank_name", array
										.getJSONObject(0)
										.getString("bank_name"));
								info.putString(
										"bank_account",
										array.getJSONObject(0).getString(
												"bank_account"));
								info.putString(
										"provinceId",
										array.getJSONObject(0).getString(
												"provinceId"));
								info.putString("cityId", array.getJSONObject(0)
										.getString("cityId"));
								info.putString("areaId", array.getJSONObject(0)
										.getString("areaId"));
								info.putString("rate", array.getJSONObject(0)
										.getString("rate"));
								info.commit();
							}
							startActivity(new Intent(LoginActivity.this,
									MainActivity.class));
							finish();
						} else
							Tools.showTextToast(LoginActivity.this, "登录失败 "
									+ jsonObject.getString("msg"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Tools.showTextToast(getApplicationContext(), "再按一次退出程序");
				exitTime = System.currentTimeMillis();
			} else {
				LoginActivity.this.finish();
				MyApp.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_register:
			startActivity(new Intent(context, Register1.class));
			break;
		case R.id.tv_forgotpassword:
			startActivity(new Intent(context, ForgetPassword.class));
			break;
		default:
			break;
		}
	}

	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		ImageView i = new ImageView(LoginActivity.this);
		i.setScaleType(ImageView.ScaleType.CENTER_CROP);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}
}
