package com.fujianmenggou.atv;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.Tools;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import dujc.dtools.FinalHttp;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Just on 2015/3/15.
 */
public class Guide4AddBusiness extends BaseActivity {

	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private TextView tv_title;
//	private String url = "http://a.app.qq.com/o/simple.jsp?pkgname=com.fujianmenggou";
	private String url = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_for_new_business);
		initFakeTitle();
		findViewById(R.id.v_bottom).setOnClickListener(this);
		setTitle("新增商户");
//		loadOtherAPI();
		String TelPhone = userInfoPreferences.getString("user_name", "");
		url = "http://103.27.7.116:83/Register.aspx?TelPhone=" + TelPhone;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.v_bottom:
				//startActivity(new Intent(context,KaiTongXiaJi1.class));
				if (url == null) {
					Tools.showTextToast(context,"获取分享链接中……");
//					loadOtherAPI();
				}else{
					addQQPlatform();
					addWXPlatform();
					addQQZonePlatform();
					mController.setShareContent("哎呦，这家店不错哦，赶紧来看看吧。");
					// 设置分享图片, 参数2为图片的url地址
					mController.setShareMedia(new UMImage(context, R.drawable.menggou));
					mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
					mController.openShare(Guide4AddBusiness.this, false);
				}
				break;
			default:break;
		}
	}
	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx93f085b6981f9a12"
				,secret = "00c12ba480e86f4066008c3d098de0e2";//待修改
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(Guide4AddBusiness.this, appId, secret);
		//new UMWXHandler(WeiDian.this, appId);
		wxHandler.setTitle("哎呦，这家店不错哦，赶紧来看看吧。");
		wxHandler.setTargetUrl(url);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(Guide4AddBusiness.this, appId, secret);
		//new UMWXHandler(WeiDian.this, appId);
		wxCircleHandler.setTitle("哎呦，这家店不错哦，赶紧来看看吧。");
		wxCircleHandler.setTargetUrl(url);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		//mController.openShare(WeiDian.this, false);
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
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(Guide4AddBusiness.this,
				"100424468", "c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.setTargetUrl(url);
		qqSsoHandler.addToSocialSDK();
		//mController.openShare(WeiDian.this, false);
	}

	private void addQQZonePlatform() {
		//参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(Guide4AddBusiness.this, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();
	}
	/**
	 * 联网获得分享的链接
	 */
	private void loadOtherAPI() {
		FinalHttp http = new FinalHttp();
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("m","Jump");
		ajaxParams.put("a","share");
		ajaxParams.put("uid", userInfoPreferences.getString("otherUid","451"));
		LogUtils.d("-----" + ajaxParams.getParamString());
		http.get("http://mg.wsmpay.com/menggou.php", ajaxParams, new AjaxCallBack<String>() {
//			http.get("http://www.wsmpay.com/menggou.php", ajaxParams, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				LogUtils.d("-----" + s);
				try {
					JSONObject jsonObject = new JSONObject(s);
					url = jsonObject.getString("mg_fx_kaidian");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}