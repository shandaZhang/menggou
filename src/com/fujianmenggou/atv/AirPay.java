package com.fujianmenggou.atv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import dujc.dtools.ViewUtils;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;
import dujc.dtools.xutils.view.annotation.ViewInject;
import dujc.dtools.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

/**
 * Created by Du on 2015/2/9.
 */
public class AirPay extends BaseActivity {

    @ViewInject(R.id.tv_order_no) private TextView tv_order_no;
    @ViewInject(R.id.tv_expen_type) private TextView tv_expen_type;
    @ViewInject(R.id.tv_moneys) private TextView tv_moneys;
    @ViewInject(R.id.et_phone) private TextView et_phone;

    private String type,channelId,orderNo,money, userID;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airpay);
        ViewUtils.inject(this);
        initFakeTitle();
        setTitle("智能空中收款");

        dialog = new AlertDialog.Builder(context)
                .setPositiveButton("确定",null)
                .create();

        channelId = getIntent().getStringExtra("channelId");
        money = getIntent().getStringExtra("money");
        type = getIntent().getStringExtra("type");
        if (channelId.equals("10")) {
			userID = getIntent().getStringExtra("userID");
			tv_order_no.setText(String.valueOf(System.currentTimeMillis()));
		}else {
			orderNo = getIntent().getStringExtra("orderNo");		
			tv_order_no.setText(orderNo+"");
		}

        tv_moneys.setText(money+"");
        tv_expen_type.setText(type+"");
    }

    @OnClick(value = {R.id.btn_submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                if (TextUtils.isEmpty(et_phone.getText())){
                    Tools.showTextToast(context,"手机号不能为空");
                    return;
                }
                if (channelId.equals("10")) {
                	XMAirPay(et_phone.getText().toString().trim());
				}else {					
					submitRequest(et_phone.getText().toString().trim());
				}
                break;
            default:break;
        }
    }

    private void submitRequest(String phone) {//op=AirPay&orderNo=15012814541114&phone=18059089771&id=1&type=1
        Tools.ShowLoadingActivity(context);
        AjaxParams params = new AjaxParams();
        params.put("op","AirPay");
        params.put("orderNo",orderNo);
        params.put("id",channelId);
        params.put("types","1");
        params.put("phone",phone);
        LogUtils.i(params.toString());
        http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                LogUtils.i(s);
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getInt("result")==1){
                        String msg = object.getString("msg");
//                        Tools.showTextToast(context,msg+"");
                        dialog.setTitle(msg+"");
                        dialog.show();
                    }else{
                        dialog.setTitle("发送支付链接失败，请稍候再试");
                        dialog.show();
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
    /** 银联在线空中支付 **/
    private void XMAirPay(String phone) {//op=AirPay&orderNo=15012814541114&phone=18059089771&id=1&type=1
    	Tools.ShowLoadingActivity(context);
    	AjaxParams params = new AjaxParams();
    	params.put("op","XMAirPay");
    	params.put("id",channelId);
    	params.put("types","1");
    	params.put("Phone",phone);
    	params.put("Money",money);
    	params.put("userID",userID);
    	LogUtils.i(params.toString());
    	http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
    		@Override
    		public void onSuccess(String s) {
    			super.onSuccess(s);
    			LogUtils.i(s);
    			try {
    				JSONObject object = new JSONObject(s);
    				if (object.getInt("result")==1){
    					String msg = object.getString("msg");
    					dialog.setTitle(msg+"");
    					dialog.show();
    				}else{
    					dialog.setTitle("发送支付链接失败，请稍候再试");
    					dialog.show();
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
}
