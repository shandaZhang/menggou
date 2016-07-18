package com.fujianmenggou.atv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import dujc.dtools.ViewUtils;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;
import dujc.dtools.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JiesuanActicity extends BaseActivity implements AdapterView.OnItemSelectedListener/*
, OnItemSelectedListener */{
	private String id,name,pic,stock,price,content;
	private int counts = 1;
	private ImageView image_Vtop;
	private TextView textview_title,textview_huodongjia,jisuan_button_textview,textview_heji,textview_kucun;
	private EditText edittext_suan,edittext_shouhuoren,edittext_shoujihao
	,edittext_xiangxidizhi,edittext_beizhu,edittext_weixinhao,editText_shengfen,editText_chengshi,editText_diqu;
	private ImageView imageView_jian,imageView_jia;
//	private RadioGroup radiogroup_jie;
	//private Spinner spinner_sheng,spinner_shi,spinner_qu;
	private int all = 0, payCategroty =0;
	//private ArrayAdapter<String> sheng,shi,qu;
	//private String[] _sheng,_shi,_qu;

	@ViewInject(R.id.sp_pay_channel) private Spinner sp_pay_channel;
	private List<HashMap<String,String>> channelList = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter adapter2;
	private String channelId;
	private DecimalFormat df = new DecimalFormat("###.00"); 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jiesuan);
		ViewUtils.inject(this);
		initFakeTitle();
		adapter2 =new SimpleAdapter(context, channelList, android.R.layout.simple_spinner_dropdown_item
				, new String[]{"name"}, new int[]{android.R.id.text1});
		sp_pay_channel.setAdapter(adapter2);
		loadChannel();

		sp_pay_channel.setOnItemSelectedListener(this);
		counts = getIntent().getIntExtra("counts", 0);
		id=getIntent().getStringExtra("id");
		name=getIntent().getStringExtra("name");
		pic=getIntent().getStringExtra("pic");
		stock=getIntent().getStringExtra("stock");
		price=getIntent().getStringExtra("price");
		content=getIntent().getStringExtra("content");
		
		edittext_suan = (EditText) findViewById(R.id.editText_suan);
		image_Vtop = (ImageView) findViewById(R.id.imageV_top);
		imageView_jian = (ImageView) findViewById(R.id.imageView_jian);
		imageView_jia = (ImageView) findViewById(R.id.imageView_jia);
		textview_title = (TextView) findViewById(R.id.textView_title);
		textview_huodongjia = (TextView) findViewById(R.id.textView_huodongjiage);
		jisuan_button_textview = (TextView) findViewById(R.id.jishu_button_textview);
		edittext_shouhuoren = (EditText) findViewById(R.id.editText_shouhuoren);
		edittext_shoujihao = (EditText) findViewById(R.id.editText_shoujihaoma);
		edittext_xiangxidizhi = (EditText) findViewById(R.id.editText_xiangxidizhi);
		edittext_beizhu = (EditText) findViewById(R.id.editText_beizhu);
		edittext_weixinhao = (EditText) findViewById(R.id.editText_weixinhao);
		textview_heji = (TextView) findViewById(R.id.textView_heji);
		textview_kucun = (TextView) findViewById(R.id.textView_kuncun);
//		radiogroup_jie = (RadioGroup) findViewById(R.id.radiogroup_jie);
		editText_shengfen = (EditText) findViewById(R.id.edittext_shengfen);
		editText_chengshi = (EditText) findViewById(R.id.edittext_chengshi);
		editText_diqu = (EditText) findViewById(R.id.edittext_diqu);
		
		/*spinner_sheng = (Spinner) findViewById(R.id.Spinner_shengfen);
		spinner_shi = (Spinner) findViewById(R.id.Spinner_chengshi);
		spinner_qu = (Spinner) findViewById(R.id.Spinner_diqu);*/
		
		bmp.display(image_Vtop, pic);
		textview_title.setText(name);
		textview_huodongjia.setText(price);
		edittext_suan.setText(counts+"");
		textview_kucun.setText(stock+"件");
		
		textview_heji.setText(df.format(Double.parseDouble(price)*counts)+" 元");
		//sheng = ArrayAdapter.createFromResource(context, R.array.province_item
				//, android.R.layout.simple_spinner_dropdown_item);
		//shi=ArrayAdapter.createFromResource(context, meigeshengdeshi[0], android.R.layout.simple_spinner_dropdown_item);
		//qu=shi=ArrayAdapter.createFromResource(context, meigeshidequ[0], android.R.layout.simple_spinner_dropdown_item);
		/*_sheng = getResources().getStringArray(R.array.province_item);
		_shi = getResources().getStringArray(meigeshengdeshi[0]);
		_qu = getResources().getStringArray(meigeshidequ[0]);*/
		
		/*sheng = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, _sheng);
		shi = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, _shi);
		qu = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, _qu);*/
		
		imageView_jia.setOnClickListener(this);
		imageView_jian.setOnClickListener(this);
		jisuan_button_textview.setOnClickListener(this);
//		radiogroup_jie.setOnCheckedChangeListener(this);
		
		/*spinner_sheng.setAdapter(sheng);
		spinner_shi.setAdapter(shi);
		spinner_qu.setAdapter(qu);
		
		spinner_sheng.setOnItemSelectedListener(this);
		spinner_shi.setOnItemSelectedListener(this);
		spinner_qu.setOnItemSelectedListener(this);*/
	}

	private void loadChannel() {
		AjaxParams params = new AjaxParams("op","GetPayChannel");
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				try {
					JSONObject jsonObject = new JSONObject(s);
					if (jsonObject.getInt("result")==1){
						JSONArray array = jsonObject.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							HashMap<String,String> map = new HashMap<String, String>();
							map.put("id", array.getJSONObject(i).getString("id"));
							map.put("name", array.getJSONObject(i).getString("name"));
							channelList.add(map);
						}
						adapter2.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.showTextToast(context,strMsg);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		counts = Integer.parseInt(edittext_suan.getText().toString().trim());
		all  = Integer.parseInt(stock);
		super.onClick(v);
		switch (v.getId()) {
		case R.id.imageView_jia:
			if(counts<all)
				counts++;
			else{
				Tools.showTextToast(context, "数量不能高于库存");
			}
			edittext_suan.setText(counts+"");
			textview_heji.setText(df.format(Double.parseDouble(price)*counts)+" 元");
			break;

		case R.id.imageView_jian:
			if(counts>1)
				counts--;
			else{
				Tools.showTextToast(context, "数量不能少于1");
			}
			edittext_suan.setText(counts+"");
			textview_heji.setText(df.format(Double.parseDouble(price)*counts)+" 元");
			break;

		case R.id.jishu_button_textview:
			recharge();
			/*String str1 = edittext_shouhuoren.getText().toString().trim();
			String str2 = edittext_shoujihao.getText().toString().trim();
			String str3 = edittext_xiangxidizhi.getText().toString().trim();
			String str4 = edittext_beizhu.getText().toString().trim();
			String str5 = edittext_weixinhao.getText().toString().trim();
			String str6 = editText_shengfen.getText().toString().trim();
			String str7 = editText_chengshi.getText().toString().trim();
			String str8 = editText_diqu.getText().toString().trim();
			if(str1.isEmpty()){
				Tools.showTextToast(context, "收货人不能为空");
			}else if(str2.isEmpty()){
				Tools.showTextToast(context, "手机号不能为空");
			}else if(str6.isEmpty()){
				Tools.showTextToast(context, "省份不能为空");
			}else if(str7.isEmpty()){
				Tools.showTextToast(context, "城市不能为空");
			}else if(str8.isEmpty()){
				Tools.showTextToast(context, "地区不能为空");
			}else if(str3.isEmpty()){
				Tools.showTextToast(context, "详细地址不能为空");
			}else if(str4.isEmpty()){
				Tools.showTextToast(context, "备注不能为空");
			}else if(str5.isEmpty()){
				Tools.showTextToast(context, "微信号不能为空");
			}else{
				startActivity(new Intent(context, RechargeActivity.class)
				.putExtra("pureRecharge", false)//纯粹不是为了充值
				.putExtra("payCategroty", payCategroty+"")
				.putExtra("money", df.format(Double.parseDouble(price)*counts))
						);
//				startActivity(new Intent(context, PreRechargeActivity.class).putExtra("position", payCategroty));
//				finish();
			}*/
			break;

		default:
			break;
		}
	}

	private void recharge() {//op=Recharge&money=1000&payChannelID=1&userID=15
		Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams();
		params.put("op","Recharge");
		params.put("money",textview_heji.getText().toString().trim());
		params.put("payChannelID",channelId);
		params.put("userID",GlobalVars.getUid(context));
		LogUtils.i(params.getParamString());
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				LogUtils.i(s);
				try {
					LogUtils.i(s);
					JSONObject object = new JSONObject(s);
					if (object.getInt("result")==1){
						String orderNo = object.getString("orderNo");
						startActivity(new Intent(context, RechargeActivity.class).putExtra("type", name)
								.putExtra("channelId", channelId).putExtra("orderNo",orderNo)
								.putExtra("money",textview_heji.getText().toString().trim()));
					}else {
						Tools.showTextToast(context,object.getString("msg"));
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
				Tools.showTextToast(context,strMsg);
			}
		});
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()){
			case R.id.sp_pay_channel:
				channelId = channelList.get(position).get("id");
				break;
			default:break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		switch (parent.getId()){
			case R.id.sp_pay_channel:
				channelId = channelList.get(0).get("id");
				break;
			default:break;
		}
	}

}
