package com.fujianmenggou.atv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;

public class RenZheng extends BaseActivity implements AdapterView.OnItemSelectedListener {

	private EditText editText1,editText2,editText3,editText4,editText5;//,editText6,editText7,editText8;
	private Spinner sp1,sp2,sp3;
	private String provinceCode,cityCode,areaCode;
	private String[] from={"name"};
	private int[] to={android.R.id.text1};
	private ArrayList<HashMap<String, String>> data1,data2,data3;
	private SimpleAdapter adapter1,adapter2,adapter3;
	private static int defaultSheng=0,defaultShi=0,defaultXian=0;
//	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_renzheng);
		initFakeTitle();
		setTitle("我的资料");

		editText1 = (EditText) findViewById(R.id.et_text1);
		editText2 = (EditText) findViewById(R.id.et_text2);
		editText3 = (EditText) findViewById(R.id.et_text3);
		editText4 = (EditText) findViewById(R.id.et_text4);
		editText5 = (EditText) findViewById(R.id.et_text5);
//		editText6 = (EditText) findViewById(R.id.et_text6);
//		editText7 = (EditText) findViewById(R.id.et_text7);
//		editText8 = (EditText) findViewById(R.id.et_text8);
		sp1=(Spinner)findViewById(R.id.sp_1);
		sp2=(Spinner)findViewById(R.id.sp_2);
		sp3=(Spinner)findViewById(R.id.sp_3);

		findViewById(R.id.btn_sure).setOnClickListener(this);

		initSpinner();

		sp1.setOnItemSelectedListener(this);
		sp2.setOnItemSelectedListener(this);
		sp3.setOnItemSelectedListener(this);

		initData();
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
					JSONObject jsonObject = new JSONObject(t);
					if (jsonObject.getInt("result") == 1) {
						JSONArray array = jsonObject.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("name", array.getJSONObject(i).getString("name"));
							map.put("code", array.getJSONObject(i).getString("code"));
							data1.add(map);
							if (userInfoPreferences.getString("provinceId", "")
									.equals(array.getJSONObject(i).getString("code"))) {
								defaultSheng = i;
							}
						}
						adapter1.notifyDataSetChanged();
						sp1.setSelection(defaultSheng, true);
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
				Tools.showTextToast(context, "获取省份列表失败，可能是网络问题");
			}
		});

	}

	private void initData() {
//		userInfo = db.findAllByWhere(UserInfo.class, "uid='" + GlobalVars.getUid(context)+"'").get(0);
		editText1.setText(userInfoPreferences.getString("user_name",""));
		editText2.setText(userInfoPreferences.getString("nick_name",""));
		editText3.setText(userInfoPreferences.getString("idCard",""));
		editText4.setText(userInfoPreferences.getString("email",""));
		editText5.setText(userInfoPreferences.getString("newzhifumima",""));

		editText4.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (editText4.getText().length() == 0){
					editText4.setText("@.com");
				}
			}
		});
		/*editText6.setText(userInfo.get);
		editText7.setText(userInfo.get);
		editText8.setText(userInfo.get);*/
//		editText9.setText(userInfo.get);
	}

	@Override
	public void onClick(View v){
		switch (v.getId()) {
			case R.id.btn_sure:
				if (TextUtils.isEmpty(editText4.getText().toString().trim())){
					Tools.showTextToast(context, "邮箱不能为空");
					return;
				}
				if (editText4.getText().toString().trim().equals("@.com")){
					Tools.showTextToast(context, "邮箱不能为空");
					return;
				}
				if (TextUtils.isEmpty(editText2.getText())){
					Tools.showTextToast(context,"姓名不能为空");
					return;
				}
				if (TextUtils.isEmpty(editText3.getText().toString().trim())){
					Tools.showTextToast(context,"身份证不能为空");
					return;
				}
				if (TextUtils.isEmpty(editText5.getText().toString())){
					Tools.showTextToast(context, "支付密码不能为空");
					return;
				}
				if (TextUtils.isEmpty(provinceCode)){
					Tools.showTextToast(context,"省不能为空");
					return;
				}
				if (TextUtils.isEmpty(cityCode)){
					Tools.showTextToast(context,"市不能为空");
					return;
				}
				if (TextUtils.isEmpty(areaCode)){
					Tools.showTextToast(context,"县不能为空");
					return;
				}
				changeInfo(editText2.getText().toString().trim()
						, editText3.getText().toString().trim()
						, editText4.getText().toString().trim()
						, editText5.getText().toString()
//					, editText6.getText().toString().trim()
//					, editText7.getText().toString().trim()
//					, editText8.getText().toString().trim()
				);
				break;
			default:
				break;
		}

	}

	private void changeInfo(final String name, final String card, final String mail, final String pay_passwd/*, String sheng, String shi, String xian*/) {
		//pay_password=123123&provinceCode=350000&cityCode=350100&areaCode=350102&user_id=63
		Tools.ShowLoadingActivity(context);
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("op","EditInfo");
		ajaxParams.put("email",mail);
		ajaxParams.put("idCard",card);
		ajaxParams.put("name",name);
		ajaxParams.put("pay_password",pay_passwd);
		ajaxParams.put("provinceCode",provinceCode);
		ajaxParams.put("cityCode",cityCode);
		ajaxParams.put("areaCode",areaCode);
		ajaxParams.put("user_id",GlobalVars.getUid(context));
		http.post(GlobalVars.url, ajaxParams, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				try {
					JSONObject object = new JSONObject(s);
					LogUtils.i(s);
					if (object.getInt("result")==1){
						Tools.showTextToast(context, "修改成功");
						SharedPreferences.Editor editor = userInfoPreferences.edit();
						editor.putString("email",mail);
						editor.putString("nick_name",name);
						editor.putString("idCard",card);
						editor.putString("newzhifumima",pay_passwd);
						editor.putString("provinceId",provinceCode);
						editor.putString("cityId",cityCode);
						editor.putString("areaId",areaCode);
						editor.commit();
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
									if (userInfoPreferences.getString("cityId","")
											.equals(array.getJSONObject(i).getString("code"))){
										defaultShi = i;
									}
								}
								adapter2.notifyDataSetChanged();
								sp2.setSelection(defaultShi,true);
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
						Tools.showTextToast(context, "获取城市列表失败，可能是网络问题");
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
									if (userInfoPreferences.getString("areaId","")
											.equals(array.getJSONObject(i).getString("code"))){
										defaultXian = i;
									}
								}
								adapter3.notifyDataSetChanged();
								sp3.setSelection(defaultXian,true);
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
						Tools.showTextToast(context, "获取县级列表失败，可能是网络问题");
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


}
