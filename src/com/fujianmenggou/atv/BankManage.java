package com.fujianmenggou.atv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import dujc.dtools.FinalDb;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.bean.BankList;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Du on 2015/2/10.
 */
public class BankManage extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private Spinner sp1;
    private EditText et1,et2,et3;
    private ArrayList<String> bankList=new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private String str2;
    private FinalDb db;
    private static int defaultBank = 0;
    private boolean isEdit=true;
//    private UserInfo userInfo;
	private TextView tv_back;
	private String oldName;
	private String oldCard;
	private String oldBank;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_mg);
        initFakeTitle();
        setTitle("银行卡");

        initBank();
        db = FinalDb.create(this);

        findViewById(R.id.btn_sure).setOnClickListener(this);
        et1=(EditText)findViewById(R.id.et1);
        sp1=(Spinner)findViewById(R.id.sp1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        
        tv_back = (TextView)findViewById(R.id.tv_back);
		tv_back.setOnClickListener(this);
        initData();

        adapter=new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, bankList);
        sp1.setAdapter(adapter);
        sp1.setOnItemSelectedListener(this);
    }

    private void initData() {
    	oldName = userInfoPreferences.getString("account_name","");
		et1.setText(oldName);
		oldCard = userInfoPreferences.getString("card_number","");
		et2.setText(oldCard);
        oldBank = userInfoPreferences.getString("bank_account","");
		et3.setText(oldBank);
		/*editText6.setText(userInfo.get);
		editText7.setText(userInfo.get);
		editText8.setText(userInfo.get);*/
//		editText9.setText(userInfo.get);
    }

    private void initBank() {
        // TODO Auto-generated method stub
        AjaxParams params=new AjaxParams("op","GetBanks");
        http.configRequestExecutionRetryCount(3);
        http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject=new JSONObject(t);
                    if(jsonObject.getInt("result")==1){
                        JSONArray array=jsonObject.getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            String id=array.getJSONObject(i).getString("id");
                            String title=array.getJSONObject(i).getString("title");
                            bankList.add(title);
                            if(db.findAllByWhere(BankList.class, "id='"+id+"'").size()==0){
                                BankList bank=new BankList();
                                bank.setId(id);
                                bank.setTitle(title);
                                db.save(bank);
                            }
                            if (title.equals(userInfoPreferences.getString("bank_name",""))){
                                defaultBank = i;
                            }
                        }
                    }else{
                        List<BankList> tmp = db.findAll(BankList.class);
                        for (BankList bl : tmp) {
                            bankList.add(bl.getTitle());
                        }
                    }
                    adapter.notifyDataSetChanged();
                    sp1.setSelection(defaultBank,true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
    	//TODO Auto-generated method stub
        switch (v.getId()){
            case R.id.btn_sure:
                if (TextUtils.isEmpty(str2)){
                    Tools.showTextToast(context, "银行名称不能为空");
                    return;
                }
                if (TextUtils.isEmpty(et1.getText().toString().trim())){
                    Tools.showTextToast(context,"开户姓名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(et2.getText().toString().trim())){
                    Tools.showTextToast(context,"银行卡号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(et3.getText().toString().trim())){
                    Tools.showTextToast(context,"开户行不能为空");
                    return;
                }
                isEdit=true;
                changeBank(et2.getText().toString().trim()
                ,et3.getText().toString().trim(),et1.getText().toString().trim(),str2);
                break;
            case R.id.tv_back:
            	back();
            	break;
            default:break;
        }
    }

	private void back() {
		if (!oldName.equals(et1.getText().toString().trim())||
				!oldCard.equals(et2.getText().toString().trim())||
				!oldBank.equals(et3.getText().toString().trim()))
		{
			isEdit=false;
			oldName=et1.getText().toString().trim();
			oldCard=et2.getText().toString().trim();
			oldBank=et3.getText().toString().trim();
		}
		if(!isEdit){
			new AlertDialog.Builder(context)
			.setMessage("资料有改动，是否先保存再退出？")
			.setPositiveButton("提交保存",
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,int which) {
					changeBank(et2.getText().toString().trim()
			                ,et3.getText().toString().trim(),et1.getText().toString().trim(),str2);
					isEdit=true;
				}
			}).setNegativeButton("直接退出", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).create()
			.show();
			return ;
		}else{
			finish();
		}
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (!oldName.equals(et1.getText().toString().trim())||
					!oldCard.equals(et2.getText().toString().trim())||
					!oldBank.equals(et3.getText().toString().trim()))
			{
				isEdit=false;
				oldName=et1.getText().toString().trim();
				oldCard=et2.getText().toString().trim();
				oldBank=et3.getText().toString().trim();
			}
			if(!isEdit){
				new AlertDialog.Builder(context)
				.setMessage("资料有改动，是否先保存再退出？")
				.setPositiveButton("提交保存",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						changeBank(et2.getText().toString().trim()
				                ,et3.getText().toString().trim(),et1.getText().toString().trim(),str2);
						isEdit=true;
					}
				}).setNegativeButton("直接退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).create()
				.show();
				return false;
			}
			break;

		default:
			break;
		}
    	return super.onKeyDown(keyCode, event);
    }
    private void changeBank(final String card_number, final String bank_account, final String bank_name,String bank) {
        //?op=AddBankCard&card_number=10&bank_account=湖东支行&bank_name=刘冠雄&bank=建设银行&user_id=63
        Tools.ShowLoadingActivity(context);
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("op","AddBankCard");
        ajaxParams.put("card_number",card_number);
        ajaxParams.put("bank_account",bank_account);
        ajaxParams.put("bank_name",bank_name);
        ajaxParams.put("bank",bank);
        ajaxParams.put("user_id",GlobalVars.getUid(context));
        http.post(GlobalVars.url, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject object = new JSONObject(s);
                    LogUtils.i(s);
                    if (object.getInt("result")==1){
                        Tools.showTextToast(context,"修改成功");
                        /**
                         et1.setText(userInfo.getAccount_name());
                         et2.setText(userInfo.getCard_number());
                         et3.setText(userInfo.getBank_account());
                         */
                        SharedPreferences.Editor userInfo = userInfoPreferences.edit();
                        userInfo.putString("account_name",bank_name);
                        userInfo.putString("card_number",card_number);
                        userInfo.putString("bank_account",bank_account);
                        userInfo.putString("bank_name",str2);
                        userInfo.commit();
//                        db.update(userInfo,"uid='" + GlobalVars.getUid(context)+"'");
                    }else {
                        Tools.showTextToast(context,object.getString("msg")+"");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Tools.DismissLoadingActivity(context);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Tools.DismissLoadingActivity(context);
            }
        });
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub
        str2=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        str2=parent.getItemAtPosition(0).toString();
    }
}