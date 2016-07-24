package com.fujianmenggou.atv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.fujianmenggou.LoginActivity;
import com.fujianmenggou.MainActivity;
import com.fujianmenggou.R;
import com.fujianmenggou.bean.UserAddress;
import com.fujianmenggou.util.Barrows;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;
import com.google.gson.JsonArray;

import dujc.dtools.FinalHttp;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;

public class AddressActivity extends BaseActivity implements
		AdapterView.OnItemSelectedListener {

	private ArrayList<UserAddress> addressList = new ArrayList<UserAddress>();
	private String defaultId;
	private UserAddress defaultAddress;
	private CheckBox cbLastClicked;
	private TextView tvSetDefaultAddr;
	private TextView tvAddAddr;
	private LinearLayout layout;
	private ArrayList<HashMap<String, String>> data1, data2, data3;
	private SimpleAdapter adapter1, adapter2, adapter3;
	private String[] from = { "name" };
	private int[] to = { android.R.id.text1 };
	private static int defaultSheng = 0, defaultShi = 0, defaultXian = 0;
	private Spinner sp1, sp2, sp3;
	private EditText etName;
	private EditText etPhone;
	private EditText etAddress;
	private String provinceCode, cityCode, areaCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		initView();
		getAddressList();

	}

	private void initView() {
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("收货地址");
		TextView tv_back = (TextView) findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("address", defaultAddress);
				setResult(1, intent);
				finish();
			}
		});
		layout = (LinearLayout) findViewById(R.id.layout_address);
		tvAddAddr = (TextView) findViewById(R.id.tv_add_address);
		tvSetDefaultAddr = (TextView) findViewById(R.id.tv_defalt_address);
		tvAddAddr.setOnClickListener(new OnClickListener() {

			Dialog alertDialog;

			@Override
			public void onClick(View v) {
				View view = LayoutInflater.from(AddressActivity.this).inflate(
						R.layout.address_order_add_item, null);
				etName = (EditText) view.findViewById(R.id.et_name);
				etPhone = (EditText) view.findViewById(R.id.et_phone);
				etAddress = (EditText) view.findViewById(R.id.et_address);
				Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
				Button btnConfirm = (Button) view
						.findViewById(R.id.btn_confirm);
				btnCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});

				btnConfirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						addAddress();
						alertDialog.dismiss();

					}
				});

				sp1 = (Spinner) view.findViewById(R.id.sp_1);
				sp2 = (Spinner) view.findViewById(R.id.sp_2);
				sp3 = (Spinner) view.findViewById(R.id.sp_3);
				initSpinner();
				sp1.setOnItemSelectedListener(AddressActivity.this);
				sp2.setOnItemSelectedListener(AddressActivity.this);
				sp3.setOnItemSelectedListener(AddressActivity.this);

				alertDialog = new AlertDialog.Builder(AddressActivity.this)
						.setView(view).create();
				alertDialog.show();

			}
		});
		tvSetDefaultAddr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cbLastClicked != null) {
					userInfoPreferences.edit().putString("addressid",
							defaultAddress.getId());
					userInfoPreferences.edit().commit();
					Barrows.getInstance().setAddress(defaultAddress);
					Tools.showTextToast(AddressActivity.this, "设置默认地址成功");
				} else
					Tools.showTextToast(AddressActivity.this, "请选择默认地址");

			}
		});

	}

	private void initSpinner() {
		// TODO Auto-generated method stub
		data1 = new ArrayList<HashMap<String, String>>();
		data2 = new ArrayList<HashMap<String, String>>();
		data3 = new ArrayList<HashMap<String, String>>();

		adapter1 = new SimpleAdapter(this, data1,
				android.R.layout.simple_spinner_dropdown_item, from, to);
		adapter2 = new SimpleAdapter(this, data2,
				android.R.layout.simple_spinner_dropdown_item, from, to);
		adapter3 = new SimpleAdapter(this, data3,
				android.R.layout.simple_spinner_dropdown_item, from, to);

		sp1.setAdapter(adapter1);
		sp2.setAdapter(adapter2);
		sp3.setAdapter(adapter3);

		AjaxParams params = new AjaxParams();
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
							map.put("name",
									array.getJSONObject(i).getString("name"));
							map.put("code",
									array.getJSONObject(i).getString("code"));
							data1.add(map);
							if (userInfoPreferences.getString("provinceId", "")
									.equals(array.getJSONObject(i).getString(
											"code"))) {
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

	private void addAddress() {
		// http://103.27.7.116:83/json/json.aspx?op=alertAddress& user_id=98
		// &provinceId=350000&cityId=350100&areaId=350103&phone=13906929659&address=台江万达广场112号&name=张三
		Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams();
		params.put("op", "alertAddress");
		params.put("user_id", userInfoPreferences.getString("id", ""));
		params.put("provinceId", provinceCode);
		params.put("cityId", cityCode);
		params.put("areaId", areaCode);
		params.put("phone", etPhone.getText().toString());
		params.put("name", etName.getText().toString());
		params.put("address", etAddress.getText().toString());

		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
			}

			@Override
			public void onSuccess(String t) {

				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				LogUtils.i(t);

				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("result") == 1) {
						getAddressList();
						Tools.showTextToast(AddressActivity.this, "添加地址成功 ");
					}

					else
						Tools.showTextToast(AddressActivity.this, "添加地址失败 ");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void getAddressList() {

		// http://103.27.7.116:83/json/json.aspx?op=AddressList&user_id=98
		Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams();
		params.put("op", "AddressList");
		params.put("user_id", userInfoPreferences.getString("id", ""));
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
			}

			@Override
			public void onSuccess(String t) {

				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				LogUtils.i(t);
				// {"result":"1","totalcount":5,"list":[{"id":7,"user_id":98,"provinceId":"350000","cityId":"350100","areaId":"350103","phone":"13906929659","address":"台江万达广场112号","name":"张三"},
				// {"id":6,"user_id":98,"provinceId":"350000","cityId":"350100","areaId":"350103","phone":"13906929659","address":"台江万达广场112号","name":"张三"},
				// {"id":5,"user_id":98,"provinceId":"000000","cityId":"000000","areaId":"000000","phone":"15655555555","address":"哦哦哦哦哦哦哦哦哦\n","name":"啊啊"},
				// {"id":2,"user_id":98,"provinceId":"350000","cityId":"350100","areaId":"350103","phone":"13906929659","address":"台江万达广场1122号","name":"张三2"},
				// {"id":1,"user_id":98,"provinceId":"350000","cityId":"350100","areaId":"350103","phone":"13906929659","address":"台江万达广场89号","name":"李伟"}]}
				try {
					JSONObject obj = new JSONObject(t);
					defaultId = userInfoPreferences.getString("addressid", "");
					if (obj.getInt("result") == 1) {
						addressList.clear();
						JSONArray array = obj.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							JSONObject addressObj = array.getJSONObject(i);
							UserAddress item = new UserAddress();
							item.setId(addressObj.getString("id"));
							item.setName(addressObj.getString("name"));
							item.setTel(addressObj.getString("phone"));
							item.setAddress(addressObj.getString("provinceId")
									+ " " + addressObj.getString("cityId")
									+ " " + addressObj.getString("areaId")
									+ " " + addressObj.getString("address"));
							if (addressObj.getString("id").equals(defaultId)) {
								item.setChecked(true);
								item.setDefault(true);
							} else {
								item.setChecked(false);
								item.setDefault(false);
							}
							addressList.add(item);
						}
						updateView();

					}

					else
						Tools.showTextToast(AddressActivity.this, "获取收货地址列表失败 ");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	protected void updateView() {

		for (int i = 0; i < addressList.size(); i++) {
			View view = LayoutInflater.from(this).inflate(
					R.layout.address_order_item2, null);
			UserAddress data = addressList.get(i);
			CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
			TextView tvName = (TextView) view.findViewById(R.id.tv_name);
			TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);
			TextView tvPhone = (TextView) view.findViewById(R.id.tv_phone);
			tvName.setText(data.getName());
			tvPhone.setText(data.getTel());
			tvAddress.setText(data.getAddress());
			cb.setTag(data);
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						defaultId = ((UserAddress) buttonView.getTag()).getId();
						defaultAddress = (UserAddress) buttonView.getTag();
						if (cbLastClicked != null)
							cbLastClicked.setChecked(false);
						cbLastClicked = (CheckBox) buttonView;
					}
				}
			});
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			float density = getResources().getDisplayMetrics().density;
			params.topMargin = (int) (10 * density);
			params.leftMargin = (int) (10 * density);
			params.rightMargin = (int) (10 * density);
			params.bottomMargin = (int) (10 * density);

			layout.addView(view, params);
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.sp_1:
			provinceCode = data1.get(position).get("code");
			data2.clear();
			adapter2.notifyDataSetChanged();
			{
				AjaxParams params = new AjaxParams();
				params.put("op", "GetCity");
				params.put("levelID", "2");
				params.put("parentCode", provinceCode);
				http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						try {
							JSONObject jsonObject = new JSONObject(t);
							if (jsonObject.getInt("result") == 1) {
								JSONArray array = jsonObject
										.getJSONArray("list");
								for (int i = 0; i < array.length(); i++) {
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("name", array.getJSONObject(i)
											.getString("name"));
									map.put("code", array.getJSONObject(i)
											.getString("code"));
									data2.add(map);
									if (userInfoPreferences.getString("cityId",
											"").equals(
											array.getJSONObject(i).getString(
													"code"))) {
										defaultShi = i;
									}
								}
								adapter2.notifyDataSetChanged();
								sp2.setSelection(defaultShi, true);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						Tools.showTextToast(context, "获取城市列表失败，可能是网络问题");
					}
				});
			}
			break;

		case R.id.sp_2:
			cityCode = data2.get(position).get("code");
			data3.clear();
			adapter3.notifyDataSetChanged();
			{
				AjaxParams params = new AjaxParams();
				params.put("op", "GetCity");
				params.put("levelID", "3");
				params.put("parentCode", cityCode);
				http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						try {
							JSONObject jsonObject = new JSONObject(t);
							if (jsonObject.getInt("result") == 1) {
								JSONArray array = jsonObject
										.getJSONArray("list");
								for (int i = 0; i < array.length(); i++) {
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("name", array.getJSONObject(i)
											.getString("name"));
									map.put("code", array.getJSONObject(i)
											.getString("code"));
									data3.add(map);
									if (userInfoPreferences.getString("areaId",
											"").equals(
											array.getJSONObject(i).getString(
													"code"))) {
										defaultXian = i;
									}
								}
								adapter3.notifyDataSetChanged();
								sp3.setSelection(defaultXian, true);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						Tools.showTextToast(context, "获取县级列表失败，可能是网络问题");
					}
				});
			}
			break;

		case R.id.sp_3:
			areaCode = data3.get(position).get("code");
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

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("address", defaultAddress);
		setResult(1, intent);
		finish();
	}

}
