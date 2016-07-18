package com.fujianmenggou.atv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.IDCard;
import com.fujianmenggou.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;

public class KaiTongXiaJi1 extends BaseActivity implements OnItemSelectedListener {

	private EditText et1,et2,et3,et4,et5,et6;
	private Button btn1;
	private Spinner sp_level;
	
	private ArrayList<HashMap<String, String>> mData;
	private SimpleAdapter mAdapter;
	//@ViewInject(R.id.tv_title)TextView title;
	private String group_id;
	//private String role;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_createsub1);

		initFakeTitle();
		
		et1=(EditText) findViewById(R.id.et1);
		et2=(EditText) findViewById(R.id.et2);
		et3=(EditText) findViewById(R.id.et3);
		et4=(EditText) findViewById(R.id.et4);
		et5=(EditText) findViewById(R.id.et5);
		et6=(EditText) findViewById(R.id.et6);
		btn1=(Button) findViewById(R.id.btn1);
		sp_level=(Spinner) findViewById(R.id.sp_level);
		
		mData = new ArrayList<HashMap<String, String>>();
		mAdapter = new SimpleAdapter(context, mData, android.R.layout.simple_spinner_dropdown_item
				, new String[]{"title"}, new int[]{android.R.id.text1});
		
		sp_level.setAdapter(mAdapter);
		
		initSpinner();
		
		setTitle("新增商户");
		et1.setHint("请输入商户的姓名");
		et2.setHint("请输入商户的身份证号码");
		btn1.setOnClickListener(this);
		
		sp_level.setOnItemSelectedListener(this);
		//et3.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	private void initSpinner() {
		// TODO Auto-generated method stub
		//Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams();
		params.put("op", "GetGroup");
		params.put("group_id", GlobalVars.getGid(context)+"");
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				try {
					JSONObject json=new JSONObject(t);
					JSONArray array = json.getJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("id", array.getJSONObject(i).getString("id"));
						map.put("title", array.getJSONObject(i).getString("title"));
						mData.add(map);
					}
					mAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btn1:
			String str1=et1.getText().toString().trim();
			String str2=et2.getText().toString().trim();
			String str3=et3.getText().toString();
			String str4=et4.getText().toString();
			String str5=et5.getText().toString();
			String str6=et6.getText().toString();
			IDCard idCard=new IDCard(str2);

			if(str1.isEmpty()||str1==null){
				Tools.showTextToast(KaiTongXiaJi1.this, "商户的姓名不能为空");
			}else if(str2.isEmpty()||str2==null||!idCard.validate()){
				Tools.showTextToast(KaiTongXiaJi1.this, "身份证号格式不正确");
			}else if(str3.isEmpty()||str3==null||str3.length()<6||str3.length()>16){
				Tools.showTextToast(KaiTongXiaJi1.this, "登录密码格式不正确");
			}else if(str4.isEmpty()||str4==null||str4.length()<6||str4.length()>16){
				Tools.showTextToast(KaiTongXiaJi1.this, "登录密码格式不正确");
			}else if(str5.isEmpty()||str5==null||str5.length()<6||str5.length()>16){
				Tools.showTextToast(KaiTongXiaJi1.this, "支付密码格式不正确");
			}else if(str6.isEmpty()||str6==null||str6.length()<6||str6.length()>16){
				Tools.showTextToast(KaiTongXiaJi1.this, "支付密码格式不正确");
			}else if(!str3.equals(str4)){
				Tools.showTextToast(KaiTongXiaJi1.this, "两次登录密码不相同");
			}else if(!str5.equals(str6)){
				Tools.showTextToast(KaiTongXiaJi1.this, "两次支付密码不相同");
			}else if(group_id==null||group_id.isEmpty()){
				Tools.showTextToast(KaiTongXiaJi1.this, "级别不能为空");
			}else{
				GlobalVars.nextLevel.setXingMing(str1);
				GlobalVars.nextLevel.setShenFenZheng(str2);
				GlobalVars.nextLevel.setDengLuMiMa(str3);
				GlobalVars.nextLevel.setZhiFuMiMa(str5);
				GlobalVars.nextLevel.setGroup_id(group_id);
				startActivity(new Intent(KaiTongXiaJi1.this
						, KaiTongXiaJi2.class).putExtra("idcard", str2)
						/*.putExtra("group", group)*/ );
				finish();
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
		group_id=mData.get(position).get("id");
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
