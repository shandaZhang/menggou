package com.fujianmenggou.ckw;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.VolleyError;
import com.fujianmenggou.R;
import com.fujianmenggou.ckw.PullToRefreshBase.OnRefreshListener;
import com.fujianmenggou.util.BaseFragment;
import com.fujianmenggou.util.Tools;
import com.lib.chen.uil.UILUtils;
import com.lib.chen.volley.HTTPUtils;
import com.lib.chen.volley.VolleyListener;

import dujc.dtools.FinalHttp;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;

public class SupplierFragment extends BaseFragment implements OnClickListener, OnScrollListener{

	private View rootView;//缓存Fragment view 
	private TextView btn_search;
	private View search;
	private View classify;
	private boolean isSearch;
	private ListView lstView_classify;
	
	private View menu_classify;
	private boolean isClassify;
	private EditText edit_search;
	private View toTop;
	private PullToRefreshListView mListView;
	private listAdapter listAdapter;//数据
	private TextView text_order_newest,text_order_price,text_order_sales,text_order_com;
	private ImageView image_order_price,image_order_sales,image_order_com;
	private int price,sales,com;
	ArrayList<ListInfo> allGoods=new ArrayList<ListInfo>();
	ArrayList<ListInfoClassify> listClassify=new ArrayList<ListInfoClassify>();
	
	private View loading;
	private View loading_refresh;
	private TextView loading_text;
	private BaseAdapter classifyadapter;//分类
	
	private String refresh=null;
	private String goods=MyConstants.URL.PATH_SUPPLINER;
	private String classif="&tid=0";//分类默认字段
	private View search_null;
	private String imageurl=null;
	private String itemurl=null;

	@Override
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(rootView==null){
			rootView=layoutInflater.inflate(R.layout.fragment_suppliner, null);
			/*refresh=MyConstants.URL.PATH_SUPPLINER+"&uid="+userInfoPreferences.getString("otherUid","451");
			initLoading();
			btn_search = (TextView) rootView.findViewById(R.id.btn_search);
			btn_search.setOnClickListener(this);
			rootView.findViewById(R.id.btn_search_image).setOnClickListener(this);
			search = rootView.findViewById(R.id.search);//搜索
			classify = rootView.findViewById(R.id.classify);//排序
			
			rootView.findViewById(R.id.btn_search_sure).setOnClickListener(this);
			edit_search = (EditText) rootView.findViewById(R.id.edit_search);
			search_null = rootView.findViewById(R.id.search_null);
			search_null.setOnClickListener(this);
			
			initOrder();
			initListView();
			loading.setVisibility(View.VISIBLE);
			initData(MyConstants.URL.PATH_SUPPLINER+"&uid="+userInfoPreferences.getString("otherUid","451"));
			initClassify();//分类菜单
			initDataClassify();*/
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void initDataClassify() {
		HTTPUtils.getVolley(getActivity(), MyConstants.URL.PATH_CLASSIFY, new VolleyListener() {
			public void onResponse(String result) {
				try {
					JSONObject object = new JSONObject(result);
					JSONObject object2 = object.getJSONObject(MyConstants.TAG.DATA);
					JSONArray jsonArray = object2.getJSONArray(MyConstants.TAG.DATALIST);
					listClassify.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						ListInfoClassify listInfo = new ListInfoClassify();
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						listInfo.setId(jsonObject.getInt(MyConstants.TAG.ID));
						listInfo.setName(jsonObject.getString(MyConstants.TAG.NAME));
						listClassify.add(listInfo);
					}
					classifyadapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
					Tools.showTextToast(context, "分类数据解析出错");
				}
			}
			public void onErrorResponse(VolleyError arg0) {
			}
		});
	}
	//+userInfoPreferences.getString("otherUid","451")

	private void initLoading() {
		loading = rootView.findViewById(R.id.loading);
		loading_refresh = rootView.findViewById(R.id.loading_refresh);
		loading_refresh.setOnClickListener(this);
		loading_text = (TextView) rootView.findViewById(R.id.loading_text);
	}
	private void initData2(){
		loading.setVisibility(View.VISIBLE);
		search.setVisibility(View.INVISIBLE);
		classify.setVisibility(View.VISIBLE);
		classif="&tid=0";
		
		String editTxt = edit_search.getText().toString().trim();
		FinalHttp http = new FinalHttp();
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("m","Supplier");
		ajaxParams.put("a","search");
		ajaxParams.put("os","2");
		ajaxParams.put("uid", userInfoPreferences.getString("otherUid","451"));
		ajaxParams.put("kw", editTxt);
		http.get(MyConstants.URL.PATH, ajaxParams, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				try {
					JSONObject object = new JSONObject(s);
					JSONObject object2 = object.getJSONObject(MyConstants.TAG.DATA);
					JSONArray jsonArray = object2.getJSONArray(MyConstants.TAG.DATALIST);
					imageurl = object2.getString(MyConstants.TAG.IMAGEURL);
					itemurl = object2.getString(MyConstants.TAG.ITEMURL);
					allGoods.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						ListInfo listInfo = new ListInfo();
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						listInfo.setId(jsonObject.getInt(MyConstants.TAG.ID));
						listInfo.setName(jsonObject.getString(MyConstants.TAG.NAME));
						listInfo.setBrokerage(jsonObject.getDouble(MyConstants.TAG.BROKERAGE));
						listInfo.setPrice(jsonObject.getDouble(MyConstants.TAG.PRICE));
						listInfo.setIs(jsonObject.getInt(MyConstants.TAG.IS));
						listInfo.setImage(jsonObject.getString(MyConstants.TAG.IMAGE));
						listInfo.setSid(jsonObject.getString(MyConstants.TAG.SID));
						allGoods.add(listInfo);
					}
					
					loading.setVisibility(View.GONE);
					loading_refresh.setVisibility(View.GONE);
					search_null.setVisibility(View.GONE);
					listAdapter.notifyDataSetChanged();
//					ToastUtils.showst(context, "数据加载完成");
				} catch (JSONException e) {
					e.printStackTrace();
					search_null.setVisibility(View.VISIBLE);
					classify.setVisibility(View.INVISIBLE);
					search.setVisibility(View.VISIBLE);
					ObjectAnimator.ofFloat(search, "translationY", -search.getHeight(),0).setDuration(100).start();
				}
			}
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.showTextToast(context, "网络连接失败\n请点击重试");
				loading_text.setText("网络连接失败\n请点击重试");
				loading.setVisibility(View.GONE);
				loading_refresh.setVisibility(View.VISIBLE);
			}
		});
//		goods=MyConstants.URL.PATH_SUPPLINER+"&uid="+userInfoPreferences.getString("otherUid","451");
//		refresh=goods;
//		initData(refresh);
		edit_search.setText("");
		ObjectAnimator.ofFloat(classify, "translationY", -classify.getHeight(),0).setDuration(100).start();
		isSearch=false;
	}
	private void initData(String path) {
		// TODO Auto-generated method stub
		HTTPUtils.getVolley(getActivity(), path, new VolleyListener() {
			public void onResponse(String result) {
				try {
					JSONObject object = new JSONObject(result);
					JSONObject object2 = object.getJSONObject(MyConstants.TAG.DATA);
					JSONArray jsonArray = object2.getJSONArray(MyConstants.TAG.DATALIST);
					imageurl = object2.getString(MyConstants.TAG.IMAGEURL);
					itemurl = object2.getString(MyConstants.TAG.ITEMURL);
					allGoods.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						ListInfo listInfo = new ListInfo();
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						listInfo.setId(jsonObject.getInt(MyConstants.TAG.ID));
						listInfo.setName(jsonObject.getString(MyConstants.TAG.NAME));
						listInfo.setBrokerage(jsonObject.getDouble(MyConstants.TAG.BROKERAGE));
						listInfo.setPrice(jsonObject.getDouble(MyConstants.TAG.PRICE));
						listInfo.setIs(jsonObject.getInt(MyConstants.TAG.IS));
						listInfo.setImage(jsonObject.getString(MyConstants.TAG.IMAGE));
						listInfo.setSid(jsonObject.getString(MyConstants.TAG.SID));
						allGoods.add(listInfo);
					}
					loading.setVisibility(View.GONE);
					loading_refresh.setVisibility(View.GONE);
					search_null.setVisibility(View.GONE);
					listAdapter.notifyDataSetChanged();
//					ToastUtils.showToast(context, "数据加载完成");
					mListView.onRefreshComplete();
				} catch (JSONException e) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							search_null.setVisibility(View.VISIBLE);
							classify.setVisibility(View.INVISIBLE);
							search.setVisibility(View.VISIBLE);
							ObjectAnimator.ofFloat(search, "translationY", -search.getHeight(),0).setDuration(100).start();
						}
					});
					e.printStackTrace();
				}
			}
			public void onErrorResponse(VolleyError arg0) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Tools.showTextToast(context, "网络连接失败\n请点击重试");
						loading_text.setText("网络连接失败\n请点击重试");
						loading.setVisibility(View.GONE);
						loading_refresh.setVisibility(View.VISIBLE);
					}
				});
			}
		});
	}
	//排序控件
	private void initOrder() {
		rootView.findViewById(R.id.btn_order_newest).setOnClickListener(this);
		rootView.findViewById(R.id.btn_order_price).setOnClickListener(this);
		rootView.findViewById(R.id.btn_order_sales).setOnClickListener(this);
		rootView.findViewById(R.id.btn_order_com).setOnClickListener(this);
		text_order_newest = (TextView) rootView.findViewById(R.id.text_order_newest);
		text_order_newest.setSelected(true);
		text_order_price = (TextView) rootView.findViewById(R.id.text_order_price);
		text_order_sales = (TextView) rootView.findViewById(R.id.text_order_sales);
		text_order_com = (TextView) rootView.findViewById(R.id.text_order_com);
		image_order_price = (ImageView) rootView.findViewById(R.id.image_order_price);
		image_order_sales = (ImageView) rootView.findViewById(R.id.image_order_sales);
		image_order_com = (ImageView) rootView.findViewById(R.id.image_order_com);
	}

	private void initListView() {
		toTop = rootView.findViewById(R.id.image_toTOP);
		toTop.setOnClickListener(this);
		mListView = (PullToRefreshListView) rootView.findViewById(R.id.listView);
		listAdapter = new listAdapter();
		mListView.setAdapter(listAdapter);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				initData(refresh);
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				if(itemurl!=null){
					Intent intent=new Intent(getActivity(), DetailsActivity.class);
					intent.putExtra("url", itemurl+"&gid="+allGoods.get(pos-1).getId());
					intent.putExtra("sid", allGoods.get(pos-1).getSid());
					startActivity(intent);
				}
			}
		});
		mListView.setOnScrollListener(this);
	}

	private void initClassify() {//分类
		rootView.findViewById(R.id.btn_classify).setOnClickListener(this);
		menu_classify = rootView.findViewById(R.id.menu_classify);
		menu_classify.setOnClickListener(this);
		lstView_classify = (ListView) rootView.findViewById(R.id.lstView_classify);
		classifyadapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView inflate = (TextView) getActivity().getLayoutInflater().inflate(R.layout.list_supplier_classsify_item, null);
				ListInfoClassify listInfoClassify = listClassify.get(position);
				inflate.setText(listInfoClassify.getName());
				return inflate;
			}
			@Override
			public long getItemId(int position) {
				return 0;
			}
			@Override
			public Object getItem(int position) {
				return null;
			}
			@Override
			public int getCount() {
				return listClassify.size();
			}
		};
		lstView_classify.setAdapter(classifyadapter);
		lstView_classify.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				ListInfoClassify listInfoClassify = listClassify.get(pos);
				classif="&tid="+listInfoClassify.getId();
				refresh=MyConstants.URL.PATH_SUPPLINER+classif+"&uid="+userInfoPreferences.getString("otherUid","451");
				initData(refresh);
//				Toast.makeText(getActivity(), "tid="+listInfoClassify.getId()+"类别"+listInfoClassify.getName(), Toast.LENGTH_SHORT).show();
				menu_classify.setVisibility(View.GONE);
				isClassify=false;
			}
		});
	}

	class listAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return allGoods.size();
		}
		@Override
		public Object getItem(int arg0) {
			return null;
		}
		@Override
		public long getItemId(int arg0) {
			return 0;
		}
		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			View inflate=null;
			Bag bag=null;
			if(convertView==null){
				inflate = getActivity().getLayoutInflater().inflate(R.layout.list_suppliner_list_item, null);
				bag=new Bag();
				bag.list_image=(ImageView) inflate.findViewById(R.id.list_image);
				bag.list_text_title=(TextView) inflate.findViewById(R.id.list_text_title);
				bag.list_text_com=(TextView) inflate.findViewById(R.id.list_text_com);
				bag.list_text_price=(TextView) inflate.findViewById(R.id.list_text_price);
				bag.list_btn_angency=(ToggleButton) inflate.findViewById(R.id.list_btn_angency);
				inflate.setTag(bag);
			}else{
				inflate=convertView;
				bag=(Bag) inflate.getTag();
			}
			bag.list_btn_angency.setChecked(false);
			final ListInfo listInfo = allGoods.get(pos);
			final int id = listInfo.getId();
			String name = listInfo.getName();
			double price = listInfo.getPrice();
			double brokerage = listInfo.getBrokerage();
			String image = listInfo.getImage();
			String price2 = new DecimalFormat("#0.00").format(price);
			String brokerage2 = new DecimalFormat("#0.00").format(brokerage);
			
			bag.list_text_title.setText(name);
			bag.list_text_price.setText(MyConstants.TXT.PRICE+price2);
			bag.list_text_com.setText(MyConstants.TXT.BROKERAGE+brokerage2);
			if(imageurl!=null){
				UILUtils.getUilUtils().displayImage(getActivity(), imageurl+image, bag.list_image);
			}
			
			if(1==listInfo.getIs()){
				bag.list_btn_angency.setChecked(true);
			}else{
				bag.list_btn_angency.setChecked(false);
			}
			bag.list_btn_angency.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(1==listInfo.getIs()){
						changeIs(""+id, ""+0);
					}else{
						changeIs(""+id, ""+1);
					}
				}
			});
			return inflate;
		}
		private void changeIs(String gid,String is){
			AjaxParams ajaxParams=new AjaxParams();
			ajaxParams.put("m", "Supplier");
			ajaxParams.put("a", "agent");
			ajaxParams.put("uid", userInfoPreferences.getString("otherUid","451"));
			ajaxParams.put("gid", gid);
			ajaxParams.put("is", is);
//			Log.e("代理操作发送", ajaxParams.toString());
			http.get(MyConstants.URL.PATH, ajaxParams, new AjaxCallBack<Object>() {
				@Override
				public void onSuccess(Object t) {
					super.onSuccess(t);
					try {
						JSONObject obj = new JSONObject(t.toString());
						int code = obj.getInt("code");
						String message = obj.getString("message");
						JSONObject data = obj.getJSONObject("data");
						initData(refresh);
					} catch (JSONException e) {
						Log.e("代理操作失败", ""+e.getMessage());
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_search_image:
			btnSearchImage();
			break;
		case R.id.btn_search_sure:
//			searchsure();
			initData2();
			break;
		case R.id.search_null:
			searchNull();
			break;
		case R.id.btn_classify:
			btnClassify();
			break;
		case R.id.menu_classify:
			menuNull();
			break;
		case R.id.image_toTOP:
			mListView.getRefreshableView().smoothScrollToPositionFromTop(0, 0);
			break;
		case R.id.btn_order_newest:
			btnOrderNewest();
			break;
		case R.id.btn_order_price:
			btnOrderPrice();
			break;
		case R.id.btn_order_sales:
			btnOrderSales();
			break;
		case R.id.btn_order_com:
			btnOrderCom();
			break;
		case R.id.loading_refresh:
			loadingRefresh();
			break;

		default:
			break;
		}
	}

	private void loadingRefresh() {
		initData(refresh);
	}

	private void btnOrderCom() {
		loading.setVisibility(View.VISIBLE);
		text_order_newest.setSelected(false);
		text_order_price.setSelected(false);
		text_order_sales.setSelected(false);
		text_order_com.setSelected(true);
		setSort(image_order_price,0);
		setSort(image_order_sales,0);
		if(com==2){
			refresh=goods+MyConstants.VAR.PATH_SUPPLINER_COM_DOWN+classif;
			initData(refresh);
			setSort(image_order_com,2);
			com=1;
		}else{
			refresh=goods+MyConstants.VAR.PATH_SUPPLINER_COM_UP+classif;
			initData(refresh);
			setSort(image_order_com,1);
			com=2;
		}
		price=0;
		sales=0;
	}

	private void btnOrderSales() {
		loading.setVisibility(View.VISIBLE);
		text_order_newest.setSelected(false);
		text_order_price.setSelected(false);
		text_order_sales.setSelected(true);
		text_order_com.setSelected(false);
		setSort(image_order_price,0);
		setSort(image_order_com,0);
		if(sales==2){
			refresh=goods+MyConstants.VAR.PATH_SUPPLINER_SALES_DOWN+classif;
			initData(refresh);
			setSort(image_order_sales,2);
			sales=1;
		}else{
			refresh=goods+MyConstants.VAR.PATH_SUPPLINER_SALES_UP+classif;
			initData(refresh);
			setSort(image_order_sales,1);
			sales=2;
		}
		price=0;
		com=0;
	}

	private void btnOrderPrice() {
		loading.setVisibility(View.VISIBLE);
		text_order_newest.setSelected(false);
		text_order_price.setSelected(true);
		text_order_sales.setSelected(false);
		text_order_com.setSelected(false);
		setSort(image_order_sales,0);
		setSort(image_order_com,0);
		if(price==2){
			refresh=goods+MyConstants.VAR.PATH_SUPPLINER_PRICE_DOWN+classif;
			initData(refresh);
			setSort(image_order_price,2);
			price=1;
		}else{
			refresh=goods+MyConstants.VAR.PATH_SUPPLINER_PRICE_UP+classif;
			initData(refresh);
			setSort(image_order_price,1);
			price=2;
		}
		sales=0;
		com=0;
	}

	private void btnOrderNewest() {
		loading.setVisibility(View.VISIBLE);
		refresh=goods+MyConstants.VAR.PATH_SUPPLINER_NEWEST+classif;
		initData(refresh);
		text_order_newest.setSelected(true);
		text_order_price.setSelected(false);
		text_order_sales.setSelected(false);
		text_order_com.setSelected(false);
		setSort(image_order_price,0);
		setSort(image_order_sales,0);
		setSort(image_order_com,0);
		price=0;
		sales=0;
		com=0;
	}
	private void setSort(ImageView imageorder, int i) {
		if(i==1){
			imageorder.setImageDrawable(getResources().getDrawable(R.drawable.sort_up));
		}else if(i==2){
			imageorder.setImageDrawable(getResources().getDrawable(R.drawable.sort_down));
		}else{
			imageorder.setImageDrawable(getResources().getDrawable(R.drawable.sort));
		}
	}
	private void searchNull() {
		loading.setVisibility(View.VISIBLE);
		goods=MyConstants.URL.PATH_SUPPLINER+"&uid="+userInfoPreferences.getString("otherUid","451");
		refresh=goods;
		initData(refresh);
		search.setVisibility(View.INVISIBLE);
		classify.setVisibility(View.VISIBLE);
		ObjectAnimator.ofFloat(classify, "translationY", -classify.getHeight(),0).setDuration(100).start();
	}

	private void searchsure() {
		String editTxt = edit_search.getText().toString().trim();
		if(!TextUtils.isEmpty(editTxt)){
			goods=MyConstants.URL.PATH_SEARCH+
					"&uid="+userInfoPreferences.getString("otherUid","451")+"&kw="+editTxt;
			refresh=goods;
			initData(refresh);
		}else{
			goods=MyConstants.URL.PATH_SUPPLINER+"&uid="+userInfoPreferences.getString("otherUid","451");
			refresh=goods;
			initData(refresh);
		}
		loading.setVisibility(View.VISIBLE);
		classif="&tid=0";
		edit_search.setText("");
		search.setVisibility(View.INVISIBLE);
		classify.setVisibility(View.VISIBLE);
		ObjectAnimator.ofFloat(classify, "translationY", -classify.getHeight(),0).setDuration(100).start();
		isSearch=false;
	}
	private void btnSearchImage() {
		isSearch=!isSearch;
		if(isSearch){
			classify.setVisibility(View.INVISIBLE);
			search.setVisibility(View.VISIBLE);
			ObjectAnimator.ofFloat(search, "translationY", -search.getHeight(),0).setDuration(100).start();
		}else{
			search.setVisibility(View.INVISIBLE);
			classify.setVisibility(View.VISIBLE);
			ObjectAnimator.ofFloat(classify, "translationY", -classify.getHeight(),0).setDuration(100).start();
		}
	}
	private void menuNull() {
		ObjectAnimator.ofFloat(menu_classify, "translationY", 0, -menu_classify.getHeight()).setDuration(300).start();
		menu_classify.postDelayed(new Runnable() {
			@Override
			public void run() {
				menu_classify.setVisibility(View.INVISIBLE);
				isClassify=false;
			}
		}, 300);
	}

	private void btnClassify() {
		isClassify=!isClassify;
		if(isClassify){
			ObjectAnimator.ofFloat(menu_classify, "translationY",  -menu_classify.getHeight(),0).setDuration(300).start();
			menu_classify.setVisibility(View.VISIBLE);
		}else{
			ObjectAnimator.ofFloat(menu_classify, "translationY", 0, -menu_classify.getHeight()).setDuration(300).start();
			menu_classify.postDelayed(new Runnable() {
				@Override
				public void run() {
					menu_classify.setVisibility(View.INVISIBLE);
				}
			}, 300);
		}
		
	}

	private class Bag{
		ImageView list_image;
		TextView list_text_title,list_text_com,list_text_price;
		ToggleButton list_btn_angency;
	}
	
	
	@Override
	public void onDestroy() {
		rootView=null;
		super.onDestroy();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(firstVisibleItem>3){
			toTop.setVisibility(View.VISIBLE);
		}else{
			toTop.setVisibility(View.GONE);
		}
	}
}
