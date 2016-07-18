package com.fujianmenggou.ckw;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.VolleyError;
import com.fujianmenggou.R;
import com.fujianmenggou.ckw.PullToRefreshBase.OnRefreshListener;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.Tools;
import com.lib.chen.uil.UILUtils;
import com.lib.chen.volley.HTTPUtils;
import com.lib.chen.volley.VolleyListener;

import dujc.dtools.FinalHttp;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;

public class ShopActivity extends BaseActivity implements OnClickListener, TextWatcher, OnScrollListener {

	private EditText editSearch;
	private View btnSearch,btnClassify;
	private boolean isShopNull=true;
	private View viewShopMess,btnPhone;
	private ImageView imageLogo;
	private TextView textName,textAdds,textPhone;
	
	private View viewClassify;
	private BaseAdapter mAdapterClassify;
	private boolean isClassify;

	private View loading;
	private View loadingProg;
	private Button loadingBtn;
	private TextView loadingText;
	
	private View viewOrders;
	private TextView text_order_newest,text_order_price,text_order_sales,text_order_com;
	private ImageView image_order_price,image_order_sales,image_order_com;
	private int price,sales,com;
	private FinalHttp http;
	
	private String numPhone;//电话号码
	private String sid;

	private String imageurl=null;
	private String itemurl=null;
	/** 产品数据存储list */
	ArrayList<ListInfo> listGoods=new ArrayList<ListInfo>();
	/** 分类信息list */
	ArrayList<ListInfoClassify> listClassify=new ArrayList<ListInfoClassify>();
	
	private int tid=0,st=1,rs=1;
	private View toTop;
	private PullToRefreshListView mListView;
	private myAdapter mAdapter;
	private int mlastpos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		http = new FinalHttp();
		sid = getIntent().getStringExtra("sid");
		initViewTitle();
		initViewShop();
		initViewLoading();
		initViewOrder();
		initViewClassify();
		initViewListview();
		if(!TextUtils.isEmpty(sid)){
			initDataShop();
			initData(null);
			initDataClassify();
		}
	}

	private void initData(String searchStr) {
		if(View.VISIBLE!=loading.getVisibility()){
			loading.setVisibility(View.VISIBLE);
			editSearch.setEnabled(false);
		}
		if(View.VISIBLE==loadingBtn.getVisibility()){
			loading.setVisibility(View.INVISIBLE);
		}
		if(!"数据加载中...".equals(loadingText.getText().toString().trim())){
			loadingText.setText("数据加载中...");
		}
		AjaxParams ajaxParams=new AjaxParams();
		ajaxParams.put("m", "Supplier");
		if(!TextUtils.isEmpty(searchStr)){
			ajaxParams.put("a", "search");
			ajaxParams.put("kw", searchStr);
		}else{
			ajaxParams.put("a", "goods");
			ajaxParams.put("st", ""+st);
			if(st!=1){
				ajaxParams.put("rs", ""+rs);
			}
			
		}
		ajaxParams.put("uid", userInfoPreferences.getString("otherUid","451"));
		ajaxParams.put("os", "2");
		ajaxParams.put("sid", sid);
		ajaxParams.put("tid", ""+tid);
		
//		Log.e("goods发送的数据", ""+ajaxParams.toString());
		http.get(MyConstants.URL.PATH, ajaxParams, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				try {
					JSONObject object = new JSONObject(t.toString());
//					Log.e("goods数据加载", ""+object.toString());
					JSONObject object2 = object.getJSONObject(MyConstants.TAG.DATA);
					JSONArray jsonArray = object2.getJSONArray(MyConstants.TAG.DATALIST);
					imageurl = object2.getString(MyConstants.TAG.IMAGEURL);
					itemurl = object2.getString(MyConstants.TAG.ITEMURL);
					listGoods.clear();
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
						listGoods.add(listInfo);
					}
					if(View.VISIBLE!=viewOrders.getVisibility()){
						viewOrders.setVisibility(View.VISIBLE);
					}
					if(View.VISIBLE!=mListView.getVisibility()){
						mListView.setVisibility(View.VISIBLE);
					}
					loading.setVisibility(View.GONE);
					mAdapter.notifyDataSetChanged();
					if(View.VISIBLE==btnSearch.getVisibility()){
						btnSearch.setVisibility(View.GONE);
						btnClassify.setVisibility(View.VISIBLE);
					}
//					Log.e("goods", "数据加载完成");
				} catch (JSONException e) {
					e.printStackTrace();
					if(View.VISIBLE==mListView.getVisibility()){
						mListView.setVisibility(View.INVISIBLE);
					}
					loadingProg.setVisibility(View.INVISIBLE);
					loadingText.setText("抱歉,没找到你想要的商品");
					if(View.VISIBLE!=loadingBtn.getVisibility()){
						loadingBtn.setText("显示全部");
						loadingBtn.setVisibility(View.VISIBLE);
					}
					mAdapter.notifyDataSetChanged();
					Log.e("goods", "数据加载失败"+e.toString());
				}finally{
					editSearch.setEnabled(true);
					mListView.onRefreshComplete();
				}
			}
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				loadingProg.setVisibility(View.INVISIBLE);
				loadingText.setText("网络连接失败\n先去检查看看吧");
				loadingBtn.setText("刷新试试");
				loadingBtn.setVisibility(View.VISIBLE);
			}
		});
	}
	
	/** 加载店铺信息*/
	private void initDataShop() {
		AjaxParams ajaxParams=new AjaxParams();
		ajaxParams.put("m", "Supplier");
		ajaxParams.put("a", "getInfo");
		ajaxParams.put("sid", sid);
//		Log.e("shop发送", ajaxParams.toString());
		http.get(MyConstants.URL.PATH, ajaxParams, new AjaxCallBack<Object>() {

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				try {
					JSONObject obj = new JSONObject(t.toString());
//					Log.e("shop加载", obj.toString());
					int code = obj.getInt("code");
					String message = obj.getString("message");
					JSONObject data = obj.getJSONObject("data");
					if(null!=data){
//						Log.e("shop加载", "data为空");
						String name = data.getString("name");
						String phone = data.getString("phone");
						String address = data.getString("address");
						String logo = data.getString("logo");
						if(!TextUtils.isEmpty(name)){
							if(View.VISIBLE!=viewShopMess.getVisibility()){
								viewShopMess.setVisibility(View.VISIBLE);
								isShopNull=false;
							}
							textName.setText(name);
						}
						textAdds.setText(TextUtils.isEmpty(address)?"":address);
						if(phone.length()<7){
							btnPhone.setVisibility(View.GONE);
						}else{
							btnPhone.setVisibility(View.VISIBLE);
						}
						textPhone.setText(TextUtils.isEmpty(phone)?"":phone);
						if(!TextUtils.isEmpty(logo)){
							UILUtils.getUilUtils().displayImage(ShopActivity.this, logo, imageLogo);
						}
//						Log.e("shop数据加载完成", "_code:"+code+"_message:"+message);
					}
				} catch (JSONException e) {
					Log.e("shop加载加载失败", ""+e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}
	/** 加载分类信息*/
	private void initDataClassify() {
		HTTPUtils.getVolley(ShopActivity.this, MyConstants.URL.PATH_CLASSIFY, new VolleyListener() {
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
					mAdapterClassify.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
					Tools.showTextToast(context, "分类数据解析出错");
				}
			}
			public void onErrorResponse(VolleyError arg0) {
			}
		});
	}
	private void initViewListview() {
		toTop = findViewById(R.id.image_toTOP);
		toTop.setOnClickListener(this);
		mListView = (PullToRefreshListView) findViewById(R.id.listView);
		mAdapter = new myAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				initData(null);
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				if(itemurl!=null){
					Intent intent=new Intent(ShopActivity.this, DetailsActivity.class);
					intent.putExtra("url", itemurl+"&gid="+listGoods.get(pos-1).getId());
					intent.putExtra("sid", listGoods.get(pos-1).getSid());
					startActivity(intent);
					addActivity();
				}
			}
		});
		mListView.setOnScrollListener(this);
	}
	private void initViewTitle() {
		findViewById(R.id.btn_finish).setOnClickListener(this);
		editSearch = (EditText) findViewById(R.id.edit_search);
		editSearch.addTextChangedListener(this);
		btnSearch = findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(this);
		btnClassify = findViewById(R.id.btn_classify);
		btnClassify.setOnClickListener(this);
	}
	private void initViewShop() {
		viewShopMess = findViewById(R.id.view_shopMess);
		imageLogo = (ImageView) findViewById(R.id.image_logo);
		textName = (TextView) findViewById(R.id.text_name);
		textAdds = (TextView) findViewById(R.id.text_adds);
		textPhone = (TextView) findViewById(R.id.text_phone);
		btnPhone = findViewById(R.id.btn_phone);
		btnPhone.setOnClickListener(this);
	}
	private void initViewLoading() {
		loading = findViewById(R.id.view_loading);
		loading.setOnClickListener(this);
		loadingProg = findViewById(R.id.loading_prog_loading);
		loadingText = (TextView) findViewById(R.id.loading_text_loading);
		loadingBtn = (Button) findViewById(R.id.loading_btn_refresh);
		loadingBtn.setOnClickListener(this);
	}
	private void initViewOrder() {
		viewOrders = findViewById(R.id.view_orders);
		findViewById(R.id.btn_order_newest).setOnClickListener(this);
		findViewById(R.id.btn_order_price).setOnClickListener(this);
		findViewById(R.id.btn_order_sales).setOnClickListener(this);
		findViewById(R.id.btn_order_com).setOnClickListener(this);
		text_order_newest = (TextView) findViewById(R.id.text_order_newest);
		text_order_newest.setSelected(true);
		text_order_price = (TextView) findViewById(R.id.text_order_price);
		text_order_sales = (TextView) findViewById(R.id.text_order_sales);
		text_order_com = (TextView) findViewById(R.id.text_order_com);
		image_order_price = (ImageView) findViewById(R.id.image_order_price);
		image_order_sales = (ImageView) findViewById(R.id.image_order_sales);
		image_order_com = (ImageView) findViewById(R.id.image_order_com);
	}
	private void initViewClassify() {
		viewClassify = findViewById(R.id.view_classify);
		viewClassify.setOnClickListener(this);
		ListView listviewClassify = (ListView) findViewById(R.id.lstView_classify);
		mAdapterClassify = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView inflate = (TextView)getLayoutInflater().inflate(R.layout.list_supplier_classsify_item, null);
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
		listviewClassify.setAdapter(mAdapterClassify);
		listviewClassify.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				ListInfoClassify listInfoClassify = listClassify.get(pos);
				tid=listInfoClassify.getId();
				initData(null);
				viewClassify();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.shop, menu);
		return true;
	}
	class myAdapter extends BaseAdapter{
		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View inflate=null;
			Bag bag=null;
			if(convertView==null){
				inflate = getLayoutInflater().inflate(R.layout.list_suppliner_list_item, null);
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
			final ListInfo listInfo = listGoods.get(pos);
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
				UILUtils.getUilUtils().displayImage(ShopActivity.this, imageurl+image, bag.list_image);
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
		@Override
		public int getCount() {
			return listGoods.size();
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
//						Log.e("代理操作加载22", obj.toString());
						int code = obj.getInt("code");
						String message = obj.getString("message");
						JSONObject data = obj.getJSONObject("data");
						initData(null);
					} catch (JSONException e) {
						Log.e("代理操作失败", ""+e.getMessage());
						e.printStackTrace();
					}
				}
			});
		}
		@Override
		public Object getItem(int arg0) {
			return null;
		}
		@Override
		public long getItemId(int arg0) {
			return 0;
		}
	}
	private class Bag{
		ImageView list_image;
		TextView list_text_title,list_text_com,list_text_price;
		ToggleButton list_btn_angency;
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	@Override
	public void afterTextChanged(Editable s) {
		if(s.length()>0){
			btnSearch.setVisibility(View.VISIBLE);
			btnClassify.setVisibility(View.GONE);
//			Log.e("s.length()", ""+s.length());
		}else{
//			Log.e("s.length()", ""+s.length());
			btnSearch.setVisibility(View.GONE);
			btnClassify.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_finish:
			finish();
			break;
		case R.id.btn_search:
			btnSearch();
			break;
		case R.id.btn_classify:
			btnClassify();
			break;
		case R.id.view_classify:
			viewClassify();
			break;
		case R.id.view_loading:
			break;
		case R.id.image_toTOP:
			mListView.getRefreshableView().smoothScrollToPositionFromTop(0, 0);
			break;
		case R.id.loading_btn_refresh:
			loadingRefresh();
			break;
		case R.id.btn_phone:
			btnPhone();
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
		default:
			break;
		}
	}

	


	private void loadingRefresh() {
		if("刷新试试".equals(loadingBtn.getText().toString().trim())){
		}else{
			st=1;
			rs=1;
		}
		initData(null);
		
	}
	/** 搜索按钮*/
	private void btnSearch() {
		String strScrean = editSearch.getText().toString().trim();
		if(!TextUtils.isEmpty(strScrean)){
			initData(strScrean);
		}
	}
	/** 分类按钮*/
	private void btnClassify() {
		isClassify=!isClassify;
		if(isClassify){
			ObjectAnimator.ofFloat(viewClassify, "translationY",  -viewClassify.getHeight(),0).setDuration(300).start();
			viewClassify.setVisibility(View.VISIBLE);
		}else{
			ObjectAnimator.ofFloat(viewClassify, "translationY", 0, -viewClassify.getHeight()).setDuration(300).start();
			viewClassify.postDelayed(new Runnable() {
				@Override
				public void run() {
					viewClassify.setVisibility(View.INVISIBLE);
				}
			}, 300);
		}
	}
	/**隐藏分类菜单*/
	private void viewClassify() {
		ObjectAnimator.ofFloat(viewClassify, "translationY", 0, -viewClassify.getHeight()).setDuration(300).start();
		viewClassify.postDelayed(new Runnable() {
			@Override
			public void run() {
				viewClassify.setVisibility(View.INVISIBLE);
				isClassify=false;
			}
		}, 300);
	}

	/** *用intent启动拨打电话  */
	private void btnPhone() {
		numPhone = textPhone.getText().toString().trim();
		if(!TextUtils.isEmpty(numPhone)){
			new AlertDialog.Builder(this)
				.setTitle("提醒")
				.setMessage("是否拨打电话"+numPhone)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+numPhone));  
						startActivity(intent); 
					}
				}).setNeutralButton("取消", null)
				.create().show();
		}
	}
	/** 排序佣金*/
	private void btnOrderCom() {
		text_order_newest.setSelected(false);
		text_order_price.setSelected(false);
		text_order_sales.setSelected(false);
		text_order_com.setSelected(true);
		setSort(image_order_price,0);
		setSort(image_order_sales,0);
		st=4;
		if(com==2){
			rs=2;
			initData(null);
			setSort(image_order_com,2);
			com=1;
		}else{
			rs=1;
			initData(null);
			setSort(image_order_com,1);
			com=2;
		}
		price=0;
		sales=0;
	}
	/** 排序销量*/
	private void btnOrderSales() {
		text_order_newest.setSelected(false);
		text_order_price.setSelected(false);
		text_order_sales.setSelected(true);
		text_order_com.setSelected(false);
		setSort(image_order_price,0);
		setSort(image_order_com,0);
		st=3;
		if(sales==2){
			rs=2;
			initData(null);
			setSort(image_order_sales,2);
			sales=1;
		}else{
			rs=1;
			initData(null);
			setSort(image_order_sales,1);
			sales=2;
		}
		price=0;
		com=0;
	}
	/** 排序价格*/
	private void btnOrderPrice() {
		text_order_newest.setSelected(false);
		text_order_price.setSelected(true);
		text_order_sales.setSelected(false);
		text_order_com.setSelected(false);
		setSort(image_order_sales,0);
		setSort(image_order_com,0);
		st=2;
		if(price==2){
			rs=2;
			initData(null);
			setSort(image_order_price,2);
			price=1;
		}else{
			rs=1;
			initData(null);
			setSort(image_order_price,1);
			price=2;
		}
		sales=0;
		com=0;
	}
	/** 排序最新*/
	private void btnOrderNewest() {
		st=1;
		initData(null);
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
	/** 设置排序图标样式的变化*/
	private void setSort(ImageView imageorder, int i) {
		if(i==1){
			imageorder.setImageDrawable(getResources().getDrawable(R.drawable.sort_up));
		}else if(i==2){
			imageorder.setImageDrawable(getResources().getDrawable(R.drawable.sort_down));
		}else{
			imageorder.setImageDrawable(getResources().getDrawable(R.drawable.sort));
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(firstVisibleItem<mlastpos){
			if(View.VISIBLE!=viewShopMess.getVisibility()&&!isShopNull){
				viewShopMess.setVisibility(View.VISIBLE);
			}
			if(View.VISIBLE!=viewOrders.getVisibility()){
				viewOrders.setVisibility(View.VISIBLE);
			}
		}else if(firstVisibleItem>mlastpos){
			if(View.VISIBLE==viewShopMess.getVisibility()&&!isShopNull){
				viewShopMess.setVisibility(View.GONE);
			}
			if(View.VISIBLE==viewOrders.getVisibility()){
				viewOrders.setVisibility(View.GONE);
			}
		}
		mlastpos = firstVisibleItem;
		if(firstVisibleItem>3){
			toTop.setVisibility(View.VISIBLE);
		}else{
			toTop.setVisibility(View.GONE);
		}
	}

}
