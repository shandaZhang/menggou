package com.fujianmenggou.atv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.*;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.bean.NextLevelList;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;

public class XiaJiGuanLi extends BaseActivity implements
ExpandableListView.OnChildClickListener,
ExpandableListView.OnGroupClickListener {

	private AutoCompleteTextView actv_sousuo; 
	private ExpandableListView elv_daililiebiao;
	
	private int pageIndex=1,totalcount=0;
	private long currentTime=0,lastTime=0;
	private boolean isNormal;
	private final int pageSize=10;
	private ArrayList<HashMap<String,String>> groupList;
	private ArrayList<ArrayList<HashMap<String,String>>> childList;
	private MyexpandableListAdapter adapter;
	private String /*group,role,*/nickName,code;
	private TextView tv_loadmore;
	private ProgressBar pb_loading;
	private View moreView;
	private ArrayList<NextLevelList> nll ;
	
	private EditText et3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guanlixiaji);
		
		initFakeTitle();
		
		actv_sousuo=(AutoCompleteTextView)findViewById(R.id.actv_sousuo);
		elv_daililiebiao=(ExpandableListView)findViewById(R.id.elv_daililiebiao);
		
		setTitle("商户管理");
		actv_sousuo.setHint("请输入商户的姓名进行搜索");
		actv_sousuo.setOnKeyListener(onKeyDown);
		
		moreView=inflater.inflate(R.layout.layout_loadmore, null);
		tv_loadmore=(TextView) moreView.findViewById(R.id.tv_loadmore);
		pb_loading=(ProgressBar) moreView.findViewById(R.id.pb_loading);
		tv_loadmore.setOnClickListener(this);
		
		adapter = new MyexpandableListAdapter(this);
		initData();
		elv_daililiebiao.setAdapter(adapter);
	}

	private void initData() {
		// TODO Auto-generated method stub
		elv_daililiebiao.addFooterView(moreView);
		
		nll =new ArrayList<NextLevelList>();
		if(groupList==null || childList==null){
			groupList=new ArrayList<HashMap<String, String>>();
			childList=new ArrayList<ArrayList<HashMap<String, String>>>();
		}else{
			groupList.clear();
			childList.clear();
			adapter.notifyDataSetChanged();
		}
		
		pageIndex=1;
		
		Tools.ShowLoadingActivity(XiaJiGuanLi.this);
		
		AjaxParams params=new AjaxParams();
		params.put("op", "GetMyLowerList");
		params.put("user_id", GlobalVars.getUid(context));
		params.put("group_id", /*group*/"0");
		params.put("pageIndex", ""+pageIndex);
		params.put("pageSize", ""+pageSize);
		params.put("nickName", nickName);

		LogUtils.i(params.getParamString());
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(XiaJiGuanLi.this);
			}
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				LogUtils.i(t);
				Tools.DismissLoadingActivity(XiaJiGuanLi.this);
				try {
					JSONObject json=new JSONObject(t);
					if(json.getInt("result")==1){
						totalcount=json.getInt("totalcount");
						JSONArray array=json.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							//groupList
							HashMap<String, String> map0=new HashMap<String, String>();
							map0.put("name", array.getJSONObject(i).getString("nick_name"));
							map0.put("state", array.getJSONObject(i).getString("status"));
							map0.put("id", array.getJSONObject(i).getString("id"));
							groupList.add(map0);
							//childList
							ArrayList<HashMap<String, String>> tmp=new ArrayList<HashMap<String, String>>();
							HashMap<String, String> map1=new HashMap<String, String>();
							map1.put("riqi", array.getJSONObject(i).getString("reg_time"));
							map1.put("shuliang", array.getJSONObject(i).getString("lowerCount"));
							map1.put("jinrishouyi", array.getJSONObject(i).getString("todayProfit"));
							map1.put("dangyueshouyi", array.getJSONObject(i).getString("monthProfit"));
							tmp.add(map1);
							childList.add(tmp);
							
							NextLevelList nextlevel =new NextLevelList();
							nextlevel.setEmail(array.getJSONObject(i).getString("email"));
							nextlevel.setStatus(array.getJSONObject(i).getString("status"));
							nextlevel.setIdCard(array.getJSONObject(i).getString("idCard"));
							nextlevel.setLower_number(array.getJSONObject(i).getString("lower_number"));
							nextlevel.setIDCardHand(array.getJSONObject(i).getString("id_card_hand"));
							nextlevel.setIDCardHeads(array.getJSONObject(i).getString("id_card_heads"));
							nextlevel.setIDCardTails(array.getJSONObject(i).getString("idCard_tails"));
							nextlevel.setBankCardHeads(array.getJSONObject(i).getString("bank_card_heads"));
							nextlevel.setBankCardTails(array.getJSONObject(i).getString("bank_card_tails"));
							nextlevel.setGroup_id(array.getJSONObject(i).getString("group_id"));
							nextlevel.setName(array.getJSONObject(i).getString("nick_name"));
							nextlevel.setUser_id(array.getJSONObject(i).getString("id"));
							nextlevel.setCard_number(array.getJSONObject(i).getString("card_number"));
							nextlevel.setBank_account(array.getJSONObject(i).getString("bank_account"));
							nextlevel.setBank_name(array.getJSONObject(i).getString("account_name"));
							nextlevel.setProvinceId(array.getJSONObject(i).getString("provinceId"));
							nextlevel.setCityId(array.getJSONObject(i).getString("cityId"));
							nextlevel.setAreaId(array.getJSONObject(i).getString("areaId"));
							nextlevel.setMobile(array.getJSONObject(i).getString("mobile"));
							nll.add(nextlevel);
						}
						adapter.notifyDataSetChanged();
						if(totalcount<=groupList.size()){
							elv_daililiebiao.removeFooterView(moreView);
						}
						pageIndex++;
					}else if(json.getInt("result")==0){
						Tools.showTextToast(XiaJiGuanLi.this, "没有商户");
						elv_daililiebiao.removeFooterView(moreView);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	OnKeyListener onKeyDown=new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if(keyCode==KeyEvent.KEYCODE_ENTER){
				currentTime=System.currentTimeMillis();
				if((currentTime-lastTime>250||currentTime-lastTime<0)){//防止被多次点击
					/*LogUtils.i("key down currentTime="+currentTime
							+" lastTime="+lastTime+" currentTime-lastTime="+(currentTime-lastTime));*/
					String str=actv_sousuo.getText().toString().trim();
					/*if(str.isEmpty()||str==null){
						Tools.showTextToast(XiaJiGuanLi.this, "姓名不能为空");
					}else*/{
						nickName=str;
						initData();
					}
				}
				lastTime=currentTime;
				return true;
			}else{
				return false;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_loadmore:
			loadMore();
			break;
			
		case R.id.btn1:
			AjaxParams params = new AjaxParams();
			params.put("op", "SendMsg");
			params.put("Phone", et3.getText().toString().trim());
			http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					super.onSuccess(t);
					try {
						JSONObject json = new JSONObject(t);
						if(json.getInt("result")==1){
							Tools.showTextToast(context, "短信发送成功");
							code = json.getString("code");
						}else{
							Tools.showTextToast(context, json.getString("msg"));
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
					Tools.showTextToast(context, "获取失败，请重试");
				}
			});
			break;
		default:
			break;
		}
	}

	private void loadMore() {
		// TODO Auto-generated method stub
		AjaxParams params=new AjaxParams();
		params.put("op", "GetMyLowerList");
		params.put("user_id", GlobalVars.getUid(context));
		params.put("group_id", /*group*/"0");
		params.put("pageIndex", ""+pageIndex);
		params.put("pageSize", ""+pageSize);
		params.put("nickName", nickName);

		LogUtils.i(params.getParamString());
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				LogUtils.i(t);
				try {
					JSONObject json=new JSONObject(t);
					if(json.getInt("result")==1){
						totalcount=json.getInt("totalcount");
						JSONArray array=json.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							//groupList
							HashMap<String, String> map0=new HashMap<String, String>();
							map0.put("name", array.getJSONObject(i).getString("nick_name"));
							map0.put("state", array.getJSONObject(i).getString("status"));
							map0.put("id", array.getJSONObject(i).getString("id"));
							groupList.add(map0);
							//childList
							ArrayList<HashMap<String, String>> tmp=new ArrayList<HashMap<String, String>>();
							HashMap<String, String> map1=new HashMap<String, String>();
							map1.put("riqi", array.getJSONObject(i).getString("reg_time"));
							map1.put("shuliang", array.getJSONObject(i).getString("lowerCount"));
							map1.put("jinrishouyi", array.getJSONObject(i).getString("todayProfit"));
							map1.put("dangyueshouyi", array.getJSONObject(i).getString("monthProfit"));
							tmp.add(map1);
							childList.add(tmp);
							
							NextLevelList nextlevel =new NextLevelList();
							nextlevel.setEmail(array.getJSONObject(i).getString("email"));
							nextlevel.setStatus(array.getJSONObject(i).getString("status"));
							nextlevel.setIdCard(array.getJSONObject(i).getString("idCard"));
							nextlevel.setLower_number(array.getJSONObject(i).getString("lower_number"));
							nextlevel.setIDCardHand(array.getJSONObject(i).getString("id_card_hand"));
							nextlevel.setIDCardHeads(array.getJSONObject(i).getString("id_card_heads"));
							nextlevel.setIDCardTails(array.getJSONObject(i).getString("idCard_tails"));
							nextlevel.setBankCardHeads(array.getJSONObject(i).getString("bank_card_heads"));
							nextlevel.setBankCardTails(array.getJSONObject(i).getString("bank_card_tails"));
							nextlevel.setGroup_id(array.getJSONObject(i).getString("group_id"));
							nextlevel.setName(array.getJSONObject(i).getString("nick_name"));
							nextlevel.setUser_id(array.getJSONObject(i).getString("id"));
							nextlevel.setCard_number(array.getJSONObject(i).getString("card_number"));
							nextlevel.setBank_account(array.getJSONObject(i).getString("bank_account"));
							nextlevel.setBank_name(array.getJSONObject(i).getString("account_name"));
							nextlevel.setProvinceId(array.getJSONObject(i).getString("provinceId"));
							nextlevel.setCityId(array.getJSONObject(i).getString("cityId"));
							nextlevel.setAreaId(array.getJSONObject(i).getString("areaId"));
							nextlevel.setMobile(array.getJSONObject(i).getString("mobile"));
							nll.add(nextlevel);
						}
						adapter.notifyDataSetChanged();
						if(totalcount<=groupList.size()){
							elv_daililiebiao.removeFooterView(moreView);
						}
						pageIndex++;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}





	/***
	 * 数据源
	 * 
	 * @author Administrator
	 * 
	 */
	class MyexpandableListAdapter extends BaseExpandableListAdapter {
		private Context context;
		private LayoutInflater inflater;

		public MyexpandableListAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		// 返回父列表个数
		@Override
		public int getGroupCount() {
			return groupList.size();
		}

		// 返回子列表个数
		@Override
		public int getChildrenCount(int groupPosition) {
			return childList.get(groupPosition).size();
		}

		@Override
		public HashMap<String, String> getGroup(int groupPosition) {

			return groupList.get(groupPosition);
		}

		@Override
		public HashMap<String, String> getChild(int groupPosition, int childPosition) {
			return childList.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {

			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupHolder groupHolder = null;
			if (convertView == null) {
				groupHolder = new GroupHolder();
				convertView = inflater.inflate(R.layout.group, null);
				groupHolder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				groupHolder.tv_state = (TextView) convertView
						.findViewById(R.id.tv_state);
				groupHolder.iv_image = (ImageView) convertView
						.findViewById(R.id.iv_image);
				
				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupHolder) convertView.getTag();
			}

			groupHolder.tv_name.setText(getGroup(groupPosition).get("name"));
			groupHolder.tv_state.setText(getGroup(groupPosition).get("state"));
			if (isExpanded)// ture is Expanded or false is not isExpanded
				groupHolder.iv_image.setImageResource(R.drawable.down);
			else
				groupHolder.iv_image.setImageResource(R.drawable.right);
			return convertView;
		}

		@Override
		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item, null);
			}
			TextView kaihuriqi = (TextView) convertView.findViewById(R.id.tv_kaihuriqi);
			TextView shanghushuliang = (TextView) convertView.findViewById(R.id.tv_shanghushuliang);
			TextView jinrishouyi = (TextView) convertView.findViewById(R.id.tv_jinrishouyi);
			TextView dangyueshouyi = (TextView) convertView.findViewById(R.id.tv_dangyueshouyi);
			TextView xiugaiziliao = (TextView) convertView.findViewById(R.id.tv_xiugaidailiziliao);
			final TextView tingyong = (TextView) convertView.findViewById(R.id.tv_tingyong);
			if(groupList.get(groupPosition).get("state").contains("正常")){
				tingyong.setText("禁用");
			}else{
				tingyong.setText("启用");
			}
			kaihuriqi.setText(getChild(groupPosition, childPosition).get("riqi"));
			shanghushuliang.setText(getChild(groupPosition, childPosition).get("shuliang"));
			jinrishouyi.setText(getChild(groupPosition, childPosition).get("jinrishouyi"));
			dangyueshouyi.setText(getChild(groupPosition, childPosition).get("dangyueshouyi"));
			
			xiugaiziliao.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder dialog = new AlertDialog.Builder(context);
					dialog.setTitle("修改"+nll.get(groupPosition).getName()+"的资料");
					
					View view=XiaJiGuanLi.this.inflater.inflate(R.layout.layout1, null);
					final EditText et1=(EditText) view.findViewById(R.id.et1);
					final EditText et2=(EditText) view.findViewById(R.id.et2);
					et3=(EditText) view.findViewById(R.id.et3);
					final EditText et4=(EditText) view.findViewById(R.id.et4);
					final Button btn1=(Button) view.findViewById(R.id.btn1);
					
					et1.setText(nll.get(groupPosition).getEmail());
					et2.setText(nll.get(groupPosition).getLower_number());
					et3.setText(nll.get(groupPosition).getMobile());
					
					btn1.setOnClickListener(XiaJiGuanLi.this);
					dialog.setView(view);
					dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String str1=et1.getText().toString().trim()
									,str2=et2.getText().toString().trim()
									,str3=et3.getText().toString().trim()
									,str4=et4.getText().toString().trim();
							if(str1.isEmpty()||str1==null){
								Tools.showTextToast(context, "邮箱地址不能为空");
							}else /*if(str2.isEmpty()||str2==null){
								Tools.showTextToast(context, "开户上限不能为空");
							}else */if(str3.isEmpty()||str3==null){
								Tools.showTextToast(context, "手机号不能为空");
							}else if(!str4.equals(code)){
								Tools.showTextToast(context, "验证码不正确");
							}else{
								Tools.ShowLoadingActivity(context);
								AjaxParams params =new AjaxParams();
								params.put("op", "editUser");
								params.put("email",str1);
//								params.put("status",nll.get(groupPosition).getStatus());
//								params.put("idCard",nll.get(groupPosition).getIdCard());
								params.put("user_name",str3);
//								params.put("lower_number",str2);
//								params.put("IDCardHand",nll.get(groupPosition).getIDCardHand());
//								params.put("IDCardHeads",nll.get(groupPosition).getIDCardHeads());
//								params.put("IDCardTails",nll.get(groupPosition).getIDCardTails());
//								params.put("bankCardHeads",nll.get(groupPosition).getBankCardHeads());
//								params.put("bankCardTails",nll.get(groupPosition).getBankCardTails());
//								params.put("group_id",nll.get(groupPosition).getGroup_id());
//								params.put("name",nll.get(groupPosition).getName());
								params.put("user_id",nll.get(groupPosition).getUser_id());
//								params.put("card_number",nll.get(groupPosition).getCard_number());
//								params.put("bank_account",nll.get(groupPosition).getBank_account());
//								params.put("bank_name",nll.get(groupPosition).getBank_name());
//								params.put("provinceCode",nll.get(groupPosition).getProvinceId());
//								params.put("cityCode",nll.get(groupPosition).getCityId());
//								params.put("areaCode",nll.get(groupPosition).getAreaId());
								params.put("parent_id",GlobalVars.getUid(context));
								LogUtils.i(params.getParamString());
								http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
									@Override
									public void onSuccess(String t) {
										// TODO Auto-generated method stub
										super.onSuccess(t);
										Tools.DismissLoadingActivity(context);
										LogUtils.i(t);
										try {
											JSONObject json=new JSONObject(t);
											if(json.getInt("result")==1){
												Tools.showTextToast(context, "修改成功");
											}else{
												//String msg=json.getString("msg");
												Tools.showTextToast(context, "修改失败,原因："+json.getString("msg"));
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
										Tools.DismissLoadingActivity(context);
									}
								});
							}
						}
					});
					dialog.setNegativeButton("取消", null);
					dialog.show();
				}
			});
			
			//tingyong.setText(groupList.get(groupPosition).get("state"));
			tingyong.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Tools.showTextToast(XiaJiGuanLi.this, "tingyong...");
					
					AjaxParams params=new AjaxParams();
					params.put("op", "SetUsersStatus");
					params.put("optionUserID", GlobalVars.getUid(context));
					params.put("user_id", groupList.get(groupPosition).get("id"));
					if(groupList.get(groupPosition).get("state").contains("正常")){
						params.put("status", "3");
						isNormal=true;
					}else{
						params.put("status", "0");
						isNormal=false;
					}
					LogUtils.i(params.getParamString());
					
					http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
						@Override
						public void onSuccess(String t) {
							// TODO Auto-generated method stub
							super.onSuccess(t);
							try {
								JSONObject json=new JSONObject(t);
								if(json.getInt("result")==1){
									if(isNormal){
										tingyong.setText("启用");
										HashMap<String, String> map=groupList.get(groupPosition);
										map.put("state", "锁定");
										groupList.set(groupPosition, map);
										isNormal=false;
										adapter.notifyDataSetChanged();
									}else{
										tingyong.setText("禁用");
										HashMap<String, String> map=groupList.get(groupPosition);
										map.put("state", "正常");
										groupList.set(groupPosition, map);
										isNormal=true;
										adapter.notifyDataSetChanged();
									}
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
			});
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	@Override
	public boolean onGroupClick(final ExpandableListView parent, final View v,
			int groupPosition, final long id) {

		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		
		return false;
	}

	class GroupHolder {
		TextView tv_name;
		TextView tv_state;
		ImageView iv_image;
	}
}
