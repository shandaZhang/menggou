package com.fujianmenggou.atv;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import dujc.dtools.ViewUtils;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;
import dujc.dtools.xutils.view.annotation.ViewInject;
import dujc.dtools.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;

public class TradeActivity extends BaseActivity implements OnCheckedChangeListener
, OnItemClickListener, OnClickListener {

	@ViewInject(R.id.lv_jiaoyimingxi)ListView lv_jiaoyimingxi;
	@ViewInject(R.id.tv_zongshouyi)TextView tv_zongshouyi;
	@ViewInject(R.id.rg_qiehuan)RadioGroup rg_qiehuan;
	@ViewInject(R.id.tv_shaixuan1)TextView tv_shaixuan1;
	@ViewInject(R.id.tv_shaixuan2)TextView tv_shaixuan2;

	private ArrayList<HashMap<String, String>> dataList=new ArrayList<HashMap<String, String>>();
	private int pageIndex=1,totalcount=0;
	private final String pageSize="10";
	private String starTime,endTime;
	private TextView tv_loadmore;
	private ProgressBar pb_loading;
	private View moreView;
	private View loadingView,loadingProg;
	private TextView loadingText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade);
		loadingView = findViewById(R.id.loading_View);
		loadingProg = findViewById(R.id.loading_prog);
		loadingText = (TextView) findViewById(R.id.loading_text);
		ViewUtils.inject(this);
		initFakeTitle();
		setTitle("交易");
		moreView=inflater.inflate(R.layout.layout_loadmore, null);
		tv_loadmore=(TextView) moreView.findViewById(R.id.tv_loadmore);
		pb_loading=(ProgressBar) moreView.findViewById(R.id.pb_loading);
		tv_loadmore.setOnClickListener(this);
		initData();

		lv_jiaoyimingxi.setAdapter(adapter);
		lv_jiaoyimingxi.setOnItemClickListener(this);
		rg_qiehuan.setOnCheckedChangeListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		
		lv_jiaoyimingxi.addFooterView(moreView);
//		if(dataList.size()>0){
//			dataList.clear();
//			adapter.notifyDataSetChanged();
//		}
		pageIndex=1;
		
//		Tools.ShowLoadingActivity(context);
		if(loadingView.getVisibility()!=View.VISIBLE){
			loadingView.setVisibility(View.VISIBLE);
			if(loadingProg.getVisibility()!=View.VISIBLE){
				loadingProg.setVisibility(View.VISIBLE);
			}
			if(!loadingText.getText().toString().trim().equals("数据加载中...")){
				loadingText.setText("数据加载中...");
			}
		}
		AjaxParams params=new AjaxParams();
		params.put("op", "GetOrder");
		params.put("user_id", GlobalVars.getUid(context));
		params.put("pageIndex", pageIndex+"");
		params.put("pageSize", pageSize);
		params.put("starTime", starTime);
		params.put("endTime", endTime);
		params.put("categroty", "1");
		switch (rg_qiehuan.getCheckedRadioButtonId()) {
		case R.id.rb_xianchangzhifu:
			params.put("pay_categroty", "2");
			break;
		case R.id.rb_kongzhongzhifu:
		default:
			params.put("pay_categroty", "1");
			break;
		}
		
//		LogUtils.i(params.getParamString());
		Log.e("params.toString()", ""+params.toString());
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
//				Log.e("交易记录数据获取", "网络出错");
//				Tools.DismissLoadingActivity(context);

				if(loadingView.getVisibility()!=View.VISIBLE){
					loadingView.setVisibility(View.VISIBLE);
				}
				if(loadingProg.getVisibility()==View.VISIBLE){
					loadingProg.setVisibility(View.GONE);
				}
				loadingText.setText("数据获取失败\n请检查一下网络或稍后重试");
			}
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				LogUtils.i(t);
				if(dataList.size()>0){
					dataList.clear();
					adapter.notifyDataSetChanged();
				}
				try {
					JSONObject jsonObject=new JSONObject(t);
					if(jsonObject.getInt("result")==1){
						tv_zongshouyi.setText(jsonObject.getString("amountCount"));
						totalcount=jsonObject.getInt("totalcount");
						JSONArray array=jsonObject.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("statusName", array.getJSONObject(i).getString("statusName"));
							map.put("amount", array.getJSONObject(i).getString("order_amount"));
							map.put("add_time", array.getJSONObject(i).getString("add_time"));
							dataList.add(map);
						}
						adapter.notifyDataSetChanged();
						pageIndex++;
						if(totalcount<=dataList.size()){
							lv_jiaoyimingxi.removeFooterView(moreView);
						}
//						Log.e("交易记录数据获取", "获取数据成功...");
						if(loadingView.getVisibility()==View.VISIBLE){
							loadingView.setVisibility(View.GONE);
						}
					}else{
						Tools.showTextToast(context, "获取数据失败...");
//						Log.e("交易记录数据获取", "获取数据失败...11111");
						if(loadingView.getVisibility()!=View.VISIBLE){
						}
						loadingView.setVisibility(View.VISIBLE);
						if(loadingProg.getVisibility()==View.VISIBLE){
							loadingProg.setVisibility(View.GONE);
						}
						if(!loadingText.getText().toString().trim().equals("数据加载失败")){
							loadingText.setText("数据加载失败");
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
//					Log.e("交易记录数据获取", "获取数据失败...22222");
					if(loadingView.getVisibility()!=View.VISIBLE){
					}
					loadingView.setVisibility(View.VISIBLE);
					if(loadingProg.getVisibility()==View.VISIBLE){
						loadingProg.setVisibility(View.GONE);
					}
					if(!loadingText.getText().toString().trim().equals("数据加载失败")){
						loadingText.setText("数据加载失败");
					}
				}finally{
//					Tools.DismissLoadingActivity(context);
				}
			}
		});
	}

	BaseAdapter adapter=new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder; 
			if (convertView == null) { 
				convertView = inflater.inflate(R.layout.item_mingxi, null);
				holder = new ViewHolder(); 
				holder.leixing= (TextView) convertView.findViewById(R.id.tv_leixing);
				holder.shijian= (TextView) convertView.findViewById(R.id.tv_shijian);
				holder.jiner= (TextView) convertView.findViewById(R.id.tv_jiner);
				convertView.setTag(holder); 
			} else { 
				holder = (ViewHolder) convertView.getTag(); 
			} 
			holder.leixing.setText(dataList.get(position).get("statusName"));
			holder.shijian.setText(dataList.get(position).get("add_time"));
			holder.jiner.setText(dataList.get(position).get("amount"));
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList.size();
		}
	};
	

	static class ViewHolder { 
		TextView leixing;
		TextView shijian;
		TextView jiner;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		initData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@OnClick(value={R.id.tv_shaixuan1, R.id.tv_shaixuan2})
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Time t=new Time();
		t.setToNow();
		switch (v.getId()) {
		case R.id.tv_loadmore:
			loadMore();
			break;
		case R.id.tv_shaixuan1:
			new TmDialog1(context, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					starTime=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
					tv_shaixuan1.setText(starTime);
					initData();
				}
			}, t.year, t.month, t.monthDay);
//			DatePickerDialog dialog1=new DatePickerDialog(context, new OnDateSetListener() {
//				
//				@Override
//				public void onDateSet(DatePicker view, int year, int monthOfYear,
//						int dayOfMonth) {
//					// TODO Auto-generated method stub
//					starTime=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
//					tv_shaixuan1.setText(starTime);
//					initData();
//				}
//			}, t.year, t.month, t.monthDay);
//			dialog1.setTitle("日期");
//			dialog1.setButton(DialogInterface.BUTTON_NEGATIVE, "清除", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					starTime="";
//					tv_shaixuan1.setText(starTime);
//					initData();
//				}
//			});
//			dialog1.show();
			break;
		case R.id.tv_shaixuan2:
			new TmDialog2(context, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					endTime=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
					tv_shaixuan2.setText(endTime);
					initData();
				}
			}, t.year, t.month, t.monthDay);
//			DatePickerDialog dialog2=new DatePickerDialog(context, new OnDateSetListener() {
//				
//				@Override
//				public void onDateSet(DatePicker view, int year, int monthOfYear,
//						int dayOfMonth) {
//					// TODO Auto-generated method stub
//					endTime=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
//					tv_shaixuan2.setText(endTime);
//					initData();
//				}
//			}, t.year, t.month, t.monthDay);
//			dialog2.setButton(DialogInterface.BUTTON_NEGATIVE, "清除", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					endTime="";
//					tv_shaixuan2.setText(endTime);
//					initData();
//				}
//			});
//			dialog2.setTitle("日期");
//			dialog2.show();
			break;
		default:
			break;
		}
	} 
	public class TmDialog1 extends DatePickerDialog{
		public TmDialog1(Context context, OnDateSetListener callBack, int year,
				int monthOfYear, int dayOfMonth) {
			super(context, callBack, year, monthOfYear, dayOfMonth);
			this.setButton(DialogInterface.BUTTON_NEGATIVE, "清除", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					starTime="";
					tv_shaixuan1.setText("起始日期");
				}
			});
			this.show();
		}
		@Override
		protected void onStop() {
		}
	} 
	public class TmDialog2 extends DatePickerDialog{
		public TmDialog2(Context context, OnDateSetListener callBack, int year,
				int monthOfYear, int dayOfMonth) {
			super(context, callBack, year, monthOfYear, dayOfMonth);
			this.setButton(DialogInterface.BUTTON_NEGATIVE, "清除", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					endTime="";
					tv_shaixuan2.setText("结束日期");
				}
			});
			this.show();
		}
		@Override
		protected void onStop() {
//			super.onStop();
		}
	} 
	private void loadMore() {
		// TODO Auto-generated method stub
//		Tools.ShowLoadingActivity(context);
		AjaxParams params=new AjaxParams();
		params.put("op", "GetOrder");
		params.put("user_id", GlobalVars.getUid(context));
		params.put("pageIndex", pageIndex+"");
		params.put("pageSize", pageSize);
		params.put("starTime", starTime);
		params.put("endTime", endTime);
		params.put("categroty", "1");
		switch (rg_qiehuan.getCheckedRadioButtonId()) {
		case R.id.rb_xianchangzhifu:
			params.put("pay_categroty", "2");
			break;
		case R.id.rb_kongzhongzhifu:
		default:
			params.put("pay_categroty", "1");
			break;
		}
		
		LogUtils.i(params.getParamString());
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
					JSONObject jsonObject=new JSONObject(t);
					if(jsonObject.getInt("result")==1){
						tv_zongshouyi.setText(jsonObject.getString("amountCount"));
						totalcount=jsonObject.getInt("totalcount");
						JSONArray array=jsonObject.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("statusName", array.getJSONObject(i).getString("statusName"));
							map.put("amount", array.getJSONObject(i).getString("order_amount"));
							map.put("add_time", array.getJSONObject(i).getString("add_time"));
							dataList.add(map);
						}
						adapter.notifyDataSetChanged();
						if(totalcount<=dataList.size()){
							lv_jiaoyimingxi.removeFooterView(moreView);
						}
						pageIndex++;
					}else{
//						Tools.showTextToast(context, "获取数据失败...");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
