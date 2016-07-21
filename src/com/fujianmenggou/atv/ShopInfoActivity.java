package com.fujianmenggou.atv;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.fujianmenggou.R;
import com.fujianmenggou.bean.ShopInfo;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.bitmap.BitmapCommonUtils;
import dujc.dtools.xutils.bitmap.BitmapDisplayConfig;
import dujc.dtools.xutils.util.LogUtils;

public class ShopInfoActivity extends BaseActivity {

	private MapView mMapView;
	private BitmapDisplayConfig displayConfig;
	private BaiduMap mBaiduMap;
	private ShopInfo shopInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_shop_info);
		initFakeTitle();
		setTitle("店铺信息");
		initView();
		displayConfig = new BitmapDisplayConfig();
		// displayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用,
		// 图片太大时容易OOM。
		displayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(this));
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(500);
		displayConfig.setAnimation(animation);
		getShopInfo();
		// showShopInfo(initData());
	}

	private ShopInfo initData() {
		ShopInfo info = new ShopInfo();
		info.setCompanyName("果美美水果店");
		info.setType1("购物");
		info.setType2("便利购物");
		info.setType3("水果干果");
		info.setArea1("福建");
		info.setArea2("厦门");
		info.setArea3("海沧区");
		info.setPhone("13030883835");
		info.setName("黄老板");
		info.setAddress("滨海上城沧林东三路334号");

		return info;
	}

	private void initView() {
		mMapView = (MapView) findViewById(R.id.map);
		mBaiduMap = mMapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	private void locateInMap(double lat, double lng) {
		// 定义Maker坐标点
		LatLng point = new LatLng(39.963175, 116.400244);
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(20)
				.build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);
	}

	private void showShopInfo(ShopInfo info) {
		((TextView) findViewById(R.id.tv_company_name)).setText(info
				.getCompanyName());
		((TextView) findViewById(R.id.layout_type1).findViewById(R.id.tv_1))
				.setText(info.getType1());
		((TextView) findViewById(R.id.layout_area1).findViewById(R.id.tv_1))
				.setText(info.getArea1());
		((TextView) findViewById(R.id.layout_area2).findViewById(R.id.tv_1))
				.setText(info.getArea2());
		((TextView) findViewById(R.id.layout_area3).findViewById(R.id.tv_1))
				.setText(info.getArea3());
		((TextView) findViewById(R.id.tv_contact_phone)).setText(info
				.getPhone());
		((TextView) findViewById(R.id.tv_contact_name)).setText(info.getName());
		((TextView) findViewById(R.id.tv_address)).setText(info.getAddress());
		ArrayList<ImageView> ivShopList = new ArrayList<ImageView>();
		ArrayList<ImageView> ivGoodsList = new ArrayList<ImageView>();
		ivShopList.add((ImageView) findViewById(R.id.iv_shop1));
		ivGoodsList.add((ImageView) findViewById(R.id.iv_goods1));
		ivGoodsList.add((ImageView) findViewById(R.id.iv_goods2));
		ivGoodsList.add((ImageView) findViewById(R.id.iv_goods3));

		if (info.getShopIcon() != null) {
			for (int i = 0; i < info.getShopIcon().size()
					&& i < ivShopList.size(); i++) {
				bmp.display(ivShopList.get(i), info.getShopIcon().get(i),
						displayConfig);
			}
		}
		if (info.getShowIcon() != null) {
			for (int i = 0; i < info.getShowIcon().size()
					&& i < ivGoodsList.size(); i++) {
				ivGoodsList.get(i).setVisibility(View.VISIBLE);
				bmp.display(ivGoodsList.get(i), info.getShowIcon().get(i),
						displayConfig);
			}
		}
		locateInMap(info.getLat(), info.getLng());

	}

	private void getShopInfo() {
		Tools.ShowLoadingActivity(context);
		// http://103.27.7.116:83/json/json.aspx?op=myShop&user_id=98
		AjaxParams params = new AjaxParams();
		params.put("op", "myShop");
		Log.e("menggou", userInfoPreferences.getString("uid", ""));
		params.put("user_id", userInfoPreferences.getString("uid", "98"));
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				LogUtils.i(t);
				// {"result":"1","totalcount":1,"list":[
				// {"id":1,"is_lock":0,"add_time":"2016-04-30T23:59:58",
				// "title":"美美水果店","user_id":98,"sort_id":99,"sold_type":"水果",
				// "provinceId":"福建省","cityId":"福州市","areaId":"台江区",
				// "phone":"13906929659","name":"刘伟","address":"台江万达广场89号",
				// "Lon":"119.34474","Lat":"26.05046","remark":null,
				// "pic":"http://103.27.7.116:83/upload/201606/07/201606070043074121.png",
				// "ls":[{"id":16,"user_id":98,"article_id":1,
				// "thumb_path":"/upload/201606/07/thumb_201606070043204599.png",
				// "original_path":"/upload/201606/07/201606070043204599.png",
				// "remark":null,"add_time":"2016-07-21T14:12:46.9261953+08:00"},
				// {"id":17,"user_id":98,"article_id":1,
				// "thumb_path":"/upload/201606/07/thumb_201606070043235185.png",
				// "original_path":"/upload/201606/07/201606070043235185.png",
				// "remark":null,"add_time":"2016-07-21T14:12:46.9261953+08:00"}]}]}
				// list 数据集
				// list-pic 店铺图
				// list-ls 展示图也等于店铺头部轮循图
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("result") == 1) {
						JSONObject shopInfoObj = obj.getJSONArray("list")
								.getJSONObject(0);
						shopInfo = new ShopInfo();
						shopInfo.setShopId(shopInfoObj.getString("id"));
						shopInfo.setCompanyName(shopInfoObj.getString("title"));
						shopInfo.setType1(shopInfoObj.getString("sold_type"));
						shopInfo.setArea1(shopInfoObj.getString("provinceId"));
						shopInfo.setArea2(shopInfoObj.getString("cityId"));
						shopInfo.setArea3(shopInfoObj.getString("areaId"));
						shopInfo.setPhone(shopInfoObj.getString("phone"));
						shopInfo.setName(shopInfoObj.getString("name"));
						shopInfo.setAddress(shopInfoObj.getString("address"));
						locateInMap(shopInfoObj.getDouble("Lon"),
								shopInfoObj.getDouble("Lat"));
						ArrayList<String> shopIcons = new ArrayList<String>();
						ArrayList<String> showIcons = new ArrayList<String>();
						shopIcons.add(shopInfoObj.getString("pic"));
						JSONArray ls = shopInfoObj.getJSONArray("ls");
						for (int i = 0; i < ls.length(); i++) {
							JSONObject lsObj = ls.getJSONObject(i);
							showIcons.add(GlobalVars.baseUrl
									+ lsObj.getString("thumb_path"));
						}
						shopInfo.setShopIcon(shopIcons);
						shopInfo.setShowIcon(showIcons);
						showShopInfo(shopInfo);
					} else {
						Tools.showTextToast(ShopInfoActivity.this, "获取店铺信息失败 ");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}

}
