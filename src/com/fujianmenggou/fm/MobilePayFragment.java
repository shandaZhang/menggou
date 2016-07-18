package com.fujianmenggou.fm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fujianmenggou.R;
import com.fujianmenggou.atv.RechargeActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.ListViewForScrollView;
import com.fujianmenggou.util.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

import dujc.dtools.BitmapUtils;
import dujc.dtools.FinalHttp;
import dujc.dtools.ViewUtils;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;
import dujc.dtools.xutils.view.annotation.ViewInject;
import dujc.dtools.xutils.view.annotation.event.OnClick;

public class MobilePayFragment extends Fragment implements AdapterView.OnItemSelectedListener {

	protected FinalHttp http;
	protected BitmapUtils bmp;
	//	protected FinalDb db;
	protected LayoutInflater layoutInflater;
	protected Context context;
	protected SharedPreferences userInfoPreferences;

	private View rootView;//缓存Fragment view 
	@ViewInject(R.id.text) private TextView text;
	@ViewInject(R.id.et_how_much) public EditText et_how_much;
	@ViewInject(R.id.sp_pay_type) private Spinner sp_pay_type;
	@ViewInject(R.id.lv_type) private ListViewForScrollView lv_type;
	@ViewInject(R.id.text_ps) private TextView tv_ps;

	private String[] typeList ={"网购","货款","其他"};
//	private List<HashMap<String,String>> typeList = new ArrayList<>();
	private List<HashMap<String,String>> channelList = new ArrayList<HashMap<String, String>>();
	private ArrayAdapter adapter1;
	private MyAdapter myAdapter;
	private String type,channelId;
	private String rate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		layoutInflater=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		http=new FinalHttp();
		bmp=new BitmapUtils(getActivity(), Tools.getSDPath()+"/.bsetpay/imagecahce");
		bmp.configDiskCacheEnabled(true);
		context=getActivity();

		userInfoPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(rootView==null) {
			rootView = layoutInflater.inflate(R.layout.fragment_moblepay, null);
			ViewUtils.inject(this, rootView);
			et_how_much.setFocusable(false);
			et_how_much.setFocusableInTouchMode(false);
			et_how_much.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), PinInputOffLineDialog.class);
					startActivityForResult(intent, 5);
				}
			});
			adapter1 = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,typeList);
			sp_pay_type.setAdapter(adapter1);
			myAdapter = new MyAdapter();
			lv_type.setAdapter(myAdapter);
			lv_type.setOnItemClickListener(myAdapter);
			
			loadChannel();
			
			sp_pay_type.setOnItemSelectedListener(this);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}

		return rootView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 自定义键盘
		if (requestCode == 5) {
			if (resultCode == 1) {
				if (data != null) {					
					String money = data.getStringExtra("money");
					if (!TextUtils.isEmpty(money)) {					
						et_how_much.setText(money);
					}
				}
			}
		}
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param v
	 */
	public void HideSoftInput(View v) {
		InputMethodManager inputmanger = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputmanger.hideSoftInputFromWindow(v.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void loadChannel() {
		String group_id = userInfoPreferences.getString("group_id","");
		AjaxParams params = new AjaxParams("op","GetPayChannel");
		params.put("op","GetPayChannel");
		params.put("group_id",group_id);
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				LogUtils.d("------------------- " + s);
				try {
					JSONObject jsonObject = new JSONObject(s);
					if (jsonObject.getInt("result")==1){
						JSONArray array = jsonObject.getJSONArray("list");

						for (int i = 0; i < array.length(); i++) {
							HashMap<String,String> map = new HashMap<String, String>();

							String id = array.getJSONObject(i).getString("id");
							String name = array.getJSONObject(i).getString("name");
							String title = "";
							if (array.getJSONObject(i).has("title")) {
								title = array.getJSONObject(i).getString("title");
							}
							String pic = GlobalVars.baseUrl;
							if (array.getJSONObject(i).has("pic")) {
								pic+=array.getJSONObject(i).getString("pic");
							}
							String rate = array.getJSONObject(i).getString("rate");
							String remark = array.getJSONObject(i).getString("remark");
							
							map.put("id", id);
							map.put("name", name);
							map.put("pic", pic);
							map.put("title", title);
							map.put("rate", rate);
							map.put("remark", remark);

							channelList.add(map);
						}
						if (channelList.size() > 0) {
							channelId = channelList.get(0).get("id");
							rate = channelList.get(0).get("rate");
							tv_ps.setText(channelList.get(0).get("remark"));
						}
						myAdapter.notifyDataSetChanged();
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

	@OnClick(value={R.id.btn_recharge})
	public void onClick(View v){
		switch (v.getId()) {
			case R.id.btn_recharge:
				if (TextUtils.isEmpty(et_how_much.getText())){
					Tools.showTextToast(context,"金额不能为空");
					return;
				}
				recharge();
				break;
			default:
				break;
		}
	}

	private void recharge() {//op=Recharge&money=1000&payChannelID=1&userID=15
		if (channelId.equals("10")) {//银联在线通道
			startActivity(new Intent(context, RechargeActivity.class).putExtra("type", type)
					.putExtra("channelId", channelId).putExtra("userID",GlobalVars.getUid(context))
			.putExtra("money",et_how_much.getText().toString().trim()).putExtra("rate", rate));
		}else {
			Tools.ShowLoadingActivity(context);
			AjaxParams params = new AjaxParams();
			params.put("op","Recharge");
			params.put("money",et_how_much.getText().toString().trim());
			params.put("payChannelID",channelId);
			params.put("userID",GlobalVars.getUid(context));
			http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
				@Override
				public void onSuccess(String s) {
					super.onSuccess(s);
					try {
						LogUtils.i(s);
						JSONObject object = new JSONObject(s);
						if (object.getInt("result")==1){
							String orderNo = object.getString("orderNo");
							startActivity(new Intent(context, RechargeActivity.class).putExtra("type", type)
									.putExtra("channelId", channelId).putExtra("orderNo",orderNo)
									.putExtra("money",et_how_much.getText().toString().trim()).putExtra("rate", rate));
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
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()){
			case R.id.sp_pay_type:
				type = typeList[position];//.get(position).get("id");
				break;
			default:break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		switch (parent.getId()){
			case R.id.sp_pay_type:
				type = typeList[0];//.get(0).get("id");
				break;
			default:break;
		}
	}

	private class MyAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{

		private int[] select_state;

		@Override
		public int getCount() {
			int size = channelList.size();
			if (size > 0){
				if (select_state == null){
					select_state = new int[size];
					select_state[0] = 1;
					for (int i = 1; i < select_state.length; i++) {
						select_state[i] = 0;
					}
				}else if (select_state.length != size){
					int[] t = select_state.clone();
					select_state = new int[size];
					if (t.length >= select_state.length){
						System.arraycopy(t, 0, select_state, 0, select_state.length);
					}else{
						System.arraycopy(t, 0, select_state, 0, t.length);
						for (int i = t.length; i < select_state.length; i++) {
							select_state[i] = 0;
						}
					}
				}
			}
			return size;
		}

		@Override
		public HashMap<String,String> getItem(int position) {
			return channelList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null){
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pay_way, parent, false);
			}
			ImageView pic = (ImageView) convertView.findViewById(R.id.iv_pic);
			ImageView checked = (ImageView) convertView.findViewById(R.id.iv_checked);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView title = (TextView) convertView.findViewById(R.id.title); 
			ImageLoader.getInstance().displayImage(getItem(position).get("pic"), pic);
			name.setText(getItem(position).get("name"));
			title.setText(getItem(position).get("title"));
			if (select_state[position] == 1){
				checked.setSelected(true);
				convertView.setBackgroundResource(R.color.DarkGray);
			}else{
				checked.setSelected(false);
				convertView.setBackgroundResource(R.color.dise_bai);
			}
			
			return convertView;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			for (int i = 0; i < select_state.length; i++) {
				if (i == position){
					select_state[i] = 1;
				}else{
					select_state[i] = 0;
				}
			}
			notifyDataSetChanged();
			channelId = getItem(position).get("id");
			rate = getItem(position).get("rate");
			tv_ps.setText(getItem(position).get("remark"));
		}
	}
}
