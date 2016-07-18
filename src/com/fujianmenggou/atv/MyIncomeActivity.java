package com.fujianmenggou.atv;

import java.util.ArrayList;
import java.util.HashMap;

import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import dujc.dtools.xutils.util.LogUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MyIncomeActivity extends BaseActivity {

	private ListView lv_myincome;
	private ArrayList<HashMap<String, String>> dataList=new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_income);
		initFakeTitle();
		
		setTitle("我的收入");
		
		lv_myincome=(ListView)findViewById(R.id.lv_myincome);
		
		initData();
		lv_myincome.setAdapter(adapter);
//		lv_myincome.setEnabled(false);//2015-8-10 12:50:31 by djc，不知为何加了这句，但是有这句就没法滚动
	}

	private void initData() {
		// TODO Auto-generated method stub
		Tools.ShowLoadingActivity(MyIncomeActivity.this);
		
		AjaxParams params=new AjaxParams();
		params.put("op", "GetAmount");
		params.put("userID", GlobalVars.getUid(context));
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
				
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				Tools.DismissLoadingActivity(MyIncomeActivity.this);
				try {
					JSONObject json=new JSONObject(t);
					if(json.getInt("result")==1){
						JSONArray array=json.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							HashMap<String, String> map=new HashMap<String, String>();
							map.put("id", array.getJSONObject(i).getString("id"));
							map.put("name", array.getJSONObject(i).getString("name"));
							map.put("balance", array.getJSONObject(i).getString("balance"));
							map.put("nameCount", array.getJSONObject(i).getString("nameCount"));
							map.put("balanceCount", array.getJSONObject(i).getString("balanceCount"));
							map.put("isEnable", array.getJSONObject(i).getString("isEnable"));
							dataList.add(map);
						}
						adapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	BaseAdapter adapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			onClickListener listener;
			if(convertView==null){
				convertView=inflater.inflate(R.layout.layout_myincome, null);
				holder=new ViewHolder();
				holder.tv1=(TextView) convertView.findViewById(R.id.tv1);
				holder.tv2=(TextView) convertView.findViewById(R.id.tv2);
				holder.tv3=(TextView) convertView.findViewById(R.id.tv3);
				holder.tv4=(TextView) convertView.findViewById(R.id.tv4);
				holder.btn1=(Button) convertView.findViewById(R.id.btn1);
				listener = new onClickListener();//在这里新建监听对象  
				holder.btn1.setOnClickListener(listener);
				convertView.setTag(holder);
				convertView.setTag(holder.btn1.getId(), listener);
			}else{
				holder=(ViewHolder) convertView.getTag();
				listener = (onClickListener) convertView.getTag(holder.btn1.getId());//重新获得监听对象  
			}
			holder.tv1.setText(dataList.get(position).get("name"));
			holder.tv2.setText(dataList.get(position).get("balance")+"  元");
			holder.tv3.setText(dataList.get(position).get("nameCount"));
			holder.tv4.setText(dataList.get(position).get("balanceCount")+"  元");
			if(dataList.get(position).get("isEnable").contains("0")){
				//因为只有1和0，担心有空格之类的，所以就用contain，不用equals
				holder.btn1.setVisibility(View.GONE);
			}
			listener.setPosition(position);
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

	class ViewHolder{
		TextView tv1,tv2,tv3,tv4;
		Button btn1;
	}
	class onClickListener implements OnClickListener{

		int position;  

		public void setPosition(int position) {  
			this.position = position;  
		} 
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			Tools.showTextToast(getApplicationContext(), ""+dataList.get(position).get("id"));
			startActivity(new Intent(MyIncomeActivity.this, TiXian.class)
			.putExtra("id", dataList.get(position).get("id"))
			.putExtra(TiXian.MONEY_COUNT, dataList.get(position).get("balance")));
		}

	}
}
