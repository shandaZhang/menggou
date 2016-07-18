package com.fujianmenggou.atv;

import java.util.Date;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;
import com.lib.chen.uil.RSAUtil;
import com.unionpay.UPPayAssistEx;

import dujc.dtools.ViewUtils;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;
import dujc.dtools.xutils.view.annotation.ViewInject;
import dujc.dtools.xutils.view.annotation.event.OnClick;

public class RechargeActivity extends BaseActivity {

	@ViewInject(R.id.tv_order_no) private TextView tv_order_no;
	@ViewInject(R.id.tv_expen_type) private TextView tv_expen_type;
	@ViewInject(R.id.tv_moneys) private TextView tv_moneys;
	@ViewInject(R.id.ll_air) private LinearLayout ll_air;
	@ViewInject(R.id.ll_local) private LinearLayout ll_local;
	@ViewInject(R.id.iv_type_1) private ImageView iv_type_1;
	@ViewInject(R.id.iv_type_2) private ImageView iv_type_2;
	@ViewInject(R.id.tv_money2) private TextView tv_money2;
	@ViewInject(R.id.tv_rate) private TextView tv_rate;

	private String type,channelId,orderNo,money, rate, userID;
	private boolean isAir = false;
	/*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
    private final String mMode = "00";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);
		ViewUtils.inject(this);
		initFakeTitle();
		setTitle("充值");

		type = getIntent().getStringExtra("type");
		channelId = getIntent().getStringExtra("channelId");
		money = getIntent().getStringExtra("money");
		rate = getIntent().getStringExtra("rate");
		if (channelId.equals("10")) {
			userID = getIntent().getStringExtra("userID");
			tv_order_no.setText(String.valueOf(System.currentTimeMillis()));
			ll_air.setVisibility(View.GONE);
		}else {
			orderNo = getIntent().getStringExtra("orderNo");		
			tv_order_no.setText(orderNo+"");
		}

		
		tv_expen_type.setText(type+"");
		tv_moneys.setText(money+"");
		tv_money2.setText(money+"");
		tv_rate.setText(rate + "%");
	}

	@OnClick(value = {R.id.btn_pay,R.id.ll_air,R.id.ll_local})
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_pay:
				if (isAir){
					if (channelId.equals("10")) {						
						startActivity(new Intent(context, AirPay.class).putExtra("type", type)
								.putExtra("channelId", channelId).putExtra("userID",userID)
								.putExtra("money",money));
					}else {						
						startActivity(new Intent(context, AirPay.class).putExtra("type", type)
								.putExtra("channelId", channelId).putExtra("orderNo",orderNo)
								.putExtra("money",money));
					}
				}else {
					if (channelId.equals("10")) {
						XMlocalPay();
					}else {						
						localPay();
					}
				}
				break;
			case R.id.ll_air:
				isAir = true;
				iv_type_1.setImageResource(R.drawable.new3);
				iv_type_2.setImageResource(R.drawable.new5);
				break;
			case R.id.ll_local:
				isAir = false;
				iv_type_2.setImageResource(R.drawable.new3);
				iv_type_1.setImageResource(R.drawable.new5);
				break;
			default:break;
		}
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
        	msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
	
	private void localPay() {
		Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams();
		params.put("op","LocalPay");
		params.put("orderNo",orderNo);
		params.put("id",channelId);
		params.put("type","2");
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				LogUtils.i(s);
				try {
					JSONObject object = new JSONObject(s);
					if (object.getInt("result")==1){
						String url = object.getString("url");
						startActivity(new Intent(context, WebActivity.class).putExtra("url", url) );
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Tools.showTextToast(context,e.getMessage());
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
	
	/** 用于银联在线 **/
	private void XMlocalPay() {
		Tools.ShowLoadingActivity(context);
		AjaxParams params = new AjaxParams();
		params.put("op","XMLocalPay");
		params.put("id",channelId);
		params.put("type","2");
		params.put("money",money);
		params.put("userID",userID);
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				LogUtils.i(s);
				try {
					JSONObject object = new JSONObject(s);
					if (object.getInt("result")==1){
						String url = object.getString("url");
						getTn(url);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Tools.showTextToast(context,e.getMessage());
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
	
	private void getTn(String url) {
		http.get(url, null, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String s) {
				super.onSuccess(s);
				LogUtils.i(s);
				try {
					JSONObject object = new JSONObject(s);
					if (object.getInt("result")==1){
						String tn = object.getString("tn");
						if (tn == null) {
							handler.sendEmptyMessage(0);
				        } else {
				        	Tools.DismissLoadingActivity(context);
				        	UPPayAssistEx.startPay(RechargeActivity.this, null, null, tn, mMode);
				        }
					}else {
						Tools.showTextToast(context,object.getString("msg"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Tools.showTextToast(context,e.getMessage());
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
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			AlertDialog.Builder builder = new AlertDialog.Builder(RechargeActivity.this);
            builder.setTitle("错误提示");
            builder.setMessage("订单号获取失败,请重试!");
            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
		}
		
	};
}
