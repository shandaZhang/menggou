package com.fujianmenggou.atv;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import dujc.dtools.ViewUtils;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.view.annotation.ViewInject;
import dujc.dtools.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

/**
 * Created by Du on 2014/12/30.
 */
public class AdviceActivity extends BaseActivity {

    @ViewInject(R.id.et_advice_contact) private EditText et_advice_contact;
    @ViewInject(R.id.et_advice_content) private EditText et_advice_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        ViewUtils.inject(this);
        initFakeTitle();
        setTitle("意见反馈");
    }

    @OnClick(value = {R.id.btn_submit})
    public void onClick(View v){
        String content = et_advice_content.getText().toString().trim();
        String contact = et_advice_contact.getText().toString().trim();
        if(contact==null || contact.equals("")){
            Tools.showTextToast(context,"联系方式不能为空");
        }else if(content==null || content.equals("")){
            Tools.showTextToast(context,"内容不能为空");
        }else{
            Tools.ShowLoadingActivity(context);
            AjaxParams ajaxParams = new AjaxParams();
            ajaxParams.put("op","Feedback");
            ajaxParams.put("contact",contact);
            ajaxParams.put("userID", GlobalVars.getUid(context));
            ajaxParams.put("userAgent","1");
            ajaxParams.put("content",content);
            http.get(GlobalVars.url, ajaxParams, new AjaxCallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    super.onSuccess(s);
                    Tools.DismissLoadingActivity(context);
                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.getInt("result")==1){
                            Tools.showTextToast(context,"提交成功，感谢您的反馈");
                        }else{
                            Tools.showTextToast(context,"提交失败，请稍候再试");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    Tools.DismissLoadingActivity(context);
                    Tools.showTextToast(context,"提交失败，请检测网络情况后再试");
                }
            });
        }
    }
}
