package com.fujianmenggou.atv;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.fujianmenggou.LoginActivity;
import com.fujianmenggou.MainActivity;
import com.fujianmenggou.R;
import com.fujianmenggou.bean.UserAddress;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;
import com.google.gson.JsonArray;

import dujc.dtools.FinalHttp;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;

public class AddressActivity extends BaseActivity {

	private SharedPreferences userInfoPreferences;
	private ArrayList<UserAddress> addressList = new ArrayList<UserAddress>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		initFakeTitle();
		setTitle("收货地址");
		userInfoPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
	}

	private void getAddressList() {

		Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams();
		params.put("op", "AddressList");
		params.put("user_id", userInfoPreferences.getString("uid", ""));
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
			}

			// {"result":"1","totalcount":1,
			// "list":[{"id":1,"user_id":98,"provinceId":"350000"
			// ,"cityId":"350100","areaId":"350103","phone":"13906929659",
			// "address":"台江万达广场89号","name":"李伟"}]}
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				LogUtils.i(t);
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("result") == 1) {
						JSONArray array = obj.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
//							UserAddress item = new UserAddress();
//							item.setName(name);
//							item.setTel(tel);
//							item.setAddress(address);
//							item.setChecked(isChecked);
//							item.isDefault();

						}

					}

					else
						Tools.showTextToast(AddressActivity.this, "获取收货地址列表失败 ");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

}
