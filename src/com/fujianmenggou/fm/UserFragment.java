package com.fujianmenggou.fm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import dujc.dtools.FinalHttp;
import dujc.dtools.ViewUtils;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;
import dujc.dtools.xutils.view.annotation.ViewInject;
import dujc.dtools.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.LoginActivity;
import com.fujianmenggou.R;
import com.fujianmenggou.atv.*;
import com.fujianmenggou.ckw.MineMessageActivity;
import com.fujianmenggou.ckw.SetMyShopActivity;
import com.fujianmenggou.ckw.WebViewCkwActivity;
import com.fujianmenggou.util.BaseFragment;

public class UserFragment extends BaseFragment{

	private View rootView;//缓存Fragment view
	@ViewInject(R.id.tv_account) private TextView tv_account;
	@ViewInject(R.id.tv_phone) private TextView tv_phone;
	@ViewInject(R.id.iv_head) private ImageView iv_head;
	@ViewInject(R.id.iv_head2) private ImageView iv_head2;
	@ViewInject(R.id.tv_title) private TextView tv_title;
//	private SharedPreferences weishangmeng;
	public static String mg_gr_shoucang,mg_gr_xueyuan,mg_gr_haoyou,mg_gr_gongneng,mg_gr_toupiao;
	private String mg_gr_zhanghao,mg_gr_dianpu,mg_gr_tixian,mg_gr_shezhi,mg_gr_touxiang,mg_gr_mingcheng;

	@Override
	public View onCreateView(LayoutInflater inflater,
	                         @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(rootView==null){
			rootView=layoutInflater.inflate(R.layout.fragment_user, null);
			ViewUtils.inject(this,rootView);
			rootView.findViewById(R.id.tv_back).setVisibility(View.INVISIBLE);
			tv_title.setText("个人中心");
//			weishangmeng = getActivity().getSharedPreferences("weishangmeng", Context.MODE_PRIVATE);
			loadOtherAPI();
//			tv_account.setText(userInfoPreferences.getString("account_name",""));
			tv_phone.setText(userInfoPreferences.getString("user_name",""));
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	@OnClick(value = {R.id.renzhengziliao,R.id.guanyumenggou,R.id.tixian,R.id.tuichu,R.id.dianpu
			,R.id.shoucang,R.id.weixueyuan,R.id.haoyou,R.id.ll_userinfo,R.id.shezhi,R.id.xiugaidenglu})
	public void onClick(View v){
		switch (v.getId()){
			case R.id.renzhengziliao:
				startActivity(new Intent(context, MyInfos.class));
				break;
			case R.id.guanyumenggou:
				startActivity(new Intent(context, About.class));
				break;
			case R.id.tixian:
//				startActivity(new Intent(context, TiXian.class));
				if (!TextUtils.isEmpty(mg_gr_tixian)){
					startActivity(new Intent(context, WebActivity.class)
									.putExtra("url",mg_gr_tixian)
									.putExtra("title", "收入提现")
					);
				}
				break;
			case R.id.tuichu:
				new AlertDialog.Builder(getActivity())
				.setTitle("提醒")
				.setMessage("是否退出当前账号")
				.setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						{//清空保存的数据
							SharedPreferences sharedP = context.getSharedPreferences("GLOBALVARS", Context.MODE_PRIVATE);
							boolean isFirst = sharedP.getBoolean("isFirst", false);
							sharedP.edit().clear().putBoolean("isFirst", isFirst).commit();
						}
						startActivity(new Intent(getActivity(), LoginActivity.class));
						getActivity().finish();
						
					}
				}).setNegativeButton("暂时不了", null)
				.create().show();
			break;
			case R.id.dianpu:
////				startActivity(new Intent(getActivity(), WeiDian.class));
//				if (!TextUtils.isEmpty(mg_gr_dianpu)){
//					startActivity(new Intent(context, WebActivity.class)
//									.putExtra("url",mg_gr_dianpu)
//									.putExtra("title", "我的店铺")
////									startActivity(new Intent(context, WebViewCkwActivity.class)
////									.putExtra("url",mg_gr_dianpu)
////									.putExtra("title", "我的店铺")
//					);
//				}
				//20150522改为源生
//				startActivity(new Intent(getActivity(), SetMyShopActivity.class));
				break;
			case R.id.shoucang:
				if (!TextUtils.isEmpty(mg_gr_shoucang)){
					startActivity(new Intent(context, WebActivity.class)
									.putExtra("url",mg_gr_shoucang)
									.putExtra("title", "我的收藏")
					);
				}
				break;
			case R.id.weixueyuan:
				if (!TextUtils.isEmpty(mg_gr_xueyuan)){
					startActivity(new Intent(context, WebActivity.class)
									.putExtra("url",mg_gr_xueyuan)
									.putExtra("title", "微商学院")
					);
				}
				break;
			case R.id.haoyou:
				if (!TextUtils.isEmpty(mg_gr_haoyou)){
					startActivity(new Intent(context, WebActivity.class)
									.putExtra("url",mg_gr_haoyou)
									.putExtra("title", "邀请好友")
					);
				}
				break;
			case R.id.ll_userinfo:
				//startActivity(new Intent(context,UserCenter.class));
				if (!TextUtils.isEmpty(mg_gr_zhanghao)){
					startActivity(new Intent(context, WebActivity.class)
									.putExtra("url",mg_gr_zhanghao)
									.putExtra("title", "我的等级")
					);
				}
				break;
			case R.id.shezhi:
//				if (!TextUtils.isEmpty(mg_gr_shezhi)){
//					startActivity(new Intent(context, WebActivity.class)
//									.putExtra("url",mg_gr_shezhi)
//									.putExtra("title", "个人空间")
//					);
//				}
				startActivity(new Intent(context, MineMessageActivity.class));
				break;
			case R.id.xiugaidenglu:
				startActivity(new Intent(context,ResetPassword.class));
				break;
			default:break;
		}
	}
	private void loadOtherAPI() {
		FinalHttp http = new FinalHttp();
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("m","Jump");
		ajaxParams.put("a","settings");
		ajaxParams.put("uid", userInfoPreferences.getString("otherUid","451"));
		LogUtils.d("-----" + ajaxParams.getParamString());
		http.get("http://mg.wsmpay.com/menggou.php", ajaxParams, new AjaxCallBack<String>() {
//			http.get("http://www.wsmpay.com/menggou.php", ajaxParams, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				LogUtils.d("-----"+s);

				try {
					JSONObject jsonObject = new JSONObject(s);
					mg_gr_shoucang=jsonObject.getString("mg_gr_shoucang");
					mg_gr_xueyuan=jsonObject.getString("mg_gr_xueyuan");
					mg_gr_haoyou=jsonObject.getString("mg_gr_haoyou");

					mg_gr_gongneng=jsonObject.getString("mg_gr_gongneng");
					mg_gr_toupiao=jsonObject.getString("mg_gr_toupiao");

					mg_gr_zhanghao=jsonObject.getString("mg_gr_zhanghao");
					mg_gr_dianpu=jsonObject.getString("mg_gr_dianpu");
					mg_gr_tixian=jsonObject.getString("mg_gr_tixian");
					mg_gr_shezhi=jsonObject.getString("mg_gr_shezhi");
					mg_gr_touxiang=jsonObject.getString("mg_gr_touxiang");
					mg_gr_mingcheng = jsonObject.getString("mg_gr_mingcheng");

					tv_account.setText(mg_gr_mingcheng);
					bmp.display(iv_head,mg_gr_touxiang);
					bmp.display(iv_head2,mg_gr_touxiang);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
