package com.fujianmenggou.fm;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fujianmenggou.R;
import com.fujianmenggou.atv.OrderManageActivity;
import com.fujianmenggou.atv.ShopInfoActivity;
import com.fujianmenggou.atv.WebActivity;
import com.fujianmenggou.bean.ShopInfo;
import com.fujianmenggou.ckw.AddGoodsActivity;
import com.fujianmenggou.ckw.CircleImageView;
import com.fujianmenggou.ckw.MineMessageActivity;
import com.fujianmenggou.ckw.SetMyShopActivity;
import com.fujianmenggou.util.BaseFragment;
import com.fujianmenggou.util.Tools;
import com.lib.chen.uil.UILUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import dujc.dtools.FinalHttp;
import dujc.dtools.ViewUtils;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;
import dujc.dtools.xutils.view.annotation.ViewInject;
import dujc.dtools.xutils.view.annotation.event.OnClick;

public class MyShopFragment extends BaseFragment {

	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private View rootView;// 缓存Fragment view
	// private SharedPreferences weishangmeng;
	public static String mg_wd_fengxiao, mg_wd_kehu, mg_wd_dianpu,
			mg_wd_tianjia, mg_wd_dingdan, mg_wd_tongji, mg_wd_geren,
			mg_wd_dianpuyulan;
	private String mg_wd_beijingtu, mg_wd_touxiang;
	@ViewInject(R.id.iv_background)
	private ImageView iv_background;
	// @ViewInject(R.id.civ_avatar)private CircleImageView civ_avatar;
	// @ViewInject(R.id.icon1)private CircleImageView icon1;
	@ViewInject(R.id.civ_avatar)
	private ImageView civ_avatar;
	@ViewInject(R.id.icon1)
	private ImageView icon1;
	@ViewInject(R.id.tv_kehuguanli)
	private TextView tv_kehuguanli;
	@ViewInject(R.id.tv_dianpuguanli)
	private TextView tv_dianpuguanli;
	@ViewInject(R.id.tv_dingdanguanli)
	private TextView tv_dingdanguanli;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	private String url;
	private String consumer, goods, order;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			rootView = layoutInflater.inflate(R.layout.fragment_myshop, null);
			ViewUtils.inject(this, rootView);
			rootView.findViewById(R.id.tv_back).setVisibility(View.INVISIBLE);
			tv_title.setText("我的商铺");
			// weishangmeng = getActivity().getSharedPreferences("weishangmeng",
			// Context.MODE_PRIVATE);
			loadOtherAPI();
			loadOtherAPI2();
			loadOtherAPI3();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void loadOtherAPI() {
		FinalHttp http = new FinalHttp();
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("m", "Jump");
		ajaxParams.put("a", "store");
		ajaxParams.put("uid", userInfoPreferences.getString("otherUid", "451"));
		// LogUtils.d("-----" + ajaxParams.getParamString());
		// http.get("http://www.wsmpay.com/menggou.php", ajaxParams, new
		// AjaxCallBack<String>() {
		http.get("http://mg.wsmpay.com/menggou.php", ajaxParams,
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String s) {
						super.onSuccess(s);
						// LogUtils.d("-----"+s);
						try {
							JSONObject jsonObject = new JSONObject(s);
							// Editor editor = weishangmeng.edit();
							// editor.putString("mg_wd_fengxiao",jsonObject.getString("mg_wd_fengxiao"));
							// editor.putString("mg_wd_kehu",jsonObject.getString("mg_wd_kehu"));
							// editor.putString("mg_wd_dianpu",jsonObject.getString("mg_wd_dianpu"));
							// editor.putString("mg_wd_tianjia",jsonObject.getString("mg_wd_tianjia"));
							// editor.putString("mg_wd_dingdan",jsonObject.getString("mg_wd_dingdan"));
							// editor.putString("mg_wd_tongji",jsonObject.getString("mg_wd_tongji"));
							// editor.putString("mg_wd_geren",jsonObject.getString("mg_wd_geren"));
							// editor.apply();
							mg_wd_fengxiao = jsonObject
									.getString("mg_wd_fengxiao");
							mg_wd_kehu = jsonObject.getString("mg_wd_kehu");
							mg_wd_dianpu = jsonObject.getString("mg_wd_dianpu");
							mg_wd_tianjia = jsonObject
									.getString("mg_wd_tianjia");
							mg_wd_dingdan = jsonObject
									.getString("mg_wd_dingdan");
							mg_wd_tongji = jsonObject.getString("mg_wd_tongji");
							mg_wd_geren = jsonObject.getString("mg_wd_geren");
							mg_wd_beijingtu = jsonObject
									.getString("mg_wd_beijingtu");
							mg_wd_touxiang = jsonObject
									.getString("mg_wd_touxiang");
							mg_wd_dianpuyulan = jsonObject
									.getString("mg_wd_dianpuyulan");
							bmp.display(iv_background, mg_wd_beijingtu);
							// bmp.display(icon1,mg_wd_touxiang);
							// Log.e("mg_wd_touxiang", ""+mg_wd_touxiang);
							// bmp.display(civ_avatar,mg_wd_touxiang);
							UILUtils.getUilUtils().setRounded(200);
							UILUtils.getUilUtils().displayImage(getActivity(),
									mg_wd_touxiang, civ_avatar);
							UILUtils.getUilUtils().setRounded(50);
							UILUtils.getUilUtils().displayImage(getActivity(),
									mg_wd_touxiang, icon1);

							// Bitmap decodeFile =
							// BitmapFactory.decodeFile(mg_wd_touxiang);
							// civ_avatar.setImageBitmap(decodeFile);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void loadOtherAPI3() {
		FinalHttp http = new FinalHttp();
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("m", "Store");
		ajaxParams.put("a", "statistics");
		ajaxParams.put("uid", userInfoPreferences.getString("otherUid", "451"));
		LogUtils.d("-----" + ajaxParams.getParamString());
		http.get("http://mg.wsmpay.com/menggou.php", ajaxParams,
				new AjaxCallBack<String>() {
					// http.get("http://www.wsmpay.com/menggou.php", ajaxParams,
					// new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String s) {
						super.onSuccess(s);
						LogUtils.d("-----" + s);
						try {
							JSONObject jsonObject = new JSONObject(s);

							consumer = jsonObject.getString("consumer");
							goods = jsonObject.getString("goods");
							order = jsonObject.getString("order");

							tv_kehuguanli.setText("共" + consumer + "个客户");
							tv_dianpuguanli.setText("共" + goods + "件商品");
							tv_dingdanguanli.setText("待处理订单 " + order);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	@OnClick(value = { R.id.iv_wodefenxiao, R.id.iv_kehuguanli,
			R.id.iv_dianpuguanli, R.id.iv_tianjiashangpin,
			R.id.iv_dingdanguanli, R.id.iv_dianputongji, R.id.btn_fabu,
			R.id.icon1, R.id.icon2, R.id.civ_avatar })
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(context, WebActivity.class);
		switch (v.getId()) {
		case R.id.iv_wodefenxiao:
			if (!TextUtils.isEmpty(mg_wd_fengxiao)) {
				intent.putExtra("url", mg_wd_fengxiao);
				intent.putExtra("title", "我的分销");
				startActivity(intent);
			}
			break;
		case R.id.iv_kehuguanli:
			if (!TextUtils.isEmpty(mg_wd_kehu)) {
				intent.putExtra("url", mg_wd_kehu);
				intent.putExtra("title", "客户管理");
				startActivity(intent);
			}
			break;
		case R.id.iv_dianpuguanli:
			if (!TextUtils.isEmpty(mg_wd_dianpu)) {
				intent.putExtra("url", mg_wd_dianpu);
				intent.putExtra("title", "店铺管理");
				startActivity(intent);
			}
			break;
		case R.id.iv_tianjiashangpin:
			// tianjiashangpin();
			break;
		case R.id.btn_fabu:
			if (!TextUtils.isEmpty(mg_wd_dianpuyulan)) {
				// if (!TextUtils.isEmpty(mg_wd_tianjia)){
				// intent.putExtra("url",mg_wd_tianjia);
				// intent.putExtra("title","添加商品");
				intent.putExtra("url", mg_wd_dianpuyulan);
				intent.putExtra("title", "店铺预览");
				startActivity(intent);
			}
			break;
		case R.id.iv_dingdanguanli:
			// if (!TextUtils.isEmpty(mg_wd_dingdan)){
			// intent.putExtra("url",mg_wd_dingdan);
			// intent.putExtra("title","订单管理");
			// startActivity(intent);
			// }
			intent.setClass(context, OrderManageActivity.class);
			startActivity(intent);

			break;
		case R.id.iv_dianputongji:
			// if (!TextUtils.isEmpty(mg_wd_tongji)) {
			// intent.putExtra("url", mg_wd_tongji);
			// intent.putExtra("title", "店铺统计");
			// startActivity(intent);
			// }
			intent.setClass(context, ShopInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.icon1:
			// if (!TextUtils.isEmpty(mg_wd_geren)){
			// intent.putExtra("url",mg_wd_geren);
			// intent.putExtra("title","个人设置");
			// startActivity(intent);
			// }
			startActivity(new Intent(context, MineMessageActivity.class));
			break;
		case R.id.civ_avatar:// 店铺设置
			startActivity(new Intent(context, SetMyShopActivity.class));
			break;
		case R.id.icon2:
			if (url == null) {
				Tools.showTextToast(context, "获取分享链接中……");
				loadOtherAPI2();
			} else {
				addQQPlatform();
				addWXPlatform();
				addQQZonePlatform();
				mController.setShareContent("哎呦，这家店不错哦，赶紧来看看吧。" + "\n 地址 "
						+ url);
				// 设置分享图片, 参数2为图片的url地址
				mController.setShareMedia(new UMImage(context,
						R.drawable.ic_launcher));
				mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
						SHARE_MEDIA.DOUBAN);
				mController.openShare(getActivity(), false);
			}
			break;
		default:
			break;
		}
	}

	private void tianjiashangpin() {
		Intent intent = new Intent(getActivity(), AddGoodsActivity.class);
		startActivity(intent);
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {

		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx93f085b6981f9a12", secret = "00c12ba480e86f4066008c3d098de0e2";// 待修改
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(getActivity(), appId, secret);
		// new UMWXHandler(WeiDian.this, appId);
		wxHandler.setTitle("哎呦，这家店不错哦，赶紧来看看吧。");
		wxHandler.setTargetUrl(url);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appId,
				secret);
		// new UMWXHandler(WeiDian.this, appId);
		wxCircleHandler.setTitle("哎呦，这家店不错哦，赶紧来看看吧。");
		wxCircleHandler.setTargetUrl(url);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// mController.openShare(WeiDian.this, false);
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQPlatform() {
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(),
				"100424468", "c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.setTargetUrl(url);
		qqSsoHandler.addToSocialSDK();
		// mController.openShare(WeiDian.this, false);
	}

	private void addQQZonePlatform() {
		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
				"100424468", "c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();
	}

	private void loadOtherAPI2() {
		FinalHttp http = new FinalHttp();
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("m", "Jump");
		ajaxParams.put("a", "share");
		ajaxParams.put("uid", userInfoPreferences.getString("otherUid", "451"));
		LogUtils.d("-----" + ajaxParams.getParamString());
		http.get("http://mg.wsmpay.com/menggou.php", ajaxParams,
				new AjaxCallBack<String>() {
					// http.get("http://www.wsmpay.com/menggou.php", ajaxParams,
					// new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String s) {
						super.onSuccess(s);
						LogUtils.d("-----" + s);
						try {
							JSONObject jsonObject = new JSONObject(s);
							url = jsonObject.getString("mg_fx_dianpu");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
}
