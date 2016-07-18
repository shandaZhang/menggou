package com.fujianmenggou.atv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import dujc.dtools.FinalDb;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.bean.BankList;
import com.fujianmenggou.util.Base64Coder;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class KaiTongXiaJi2 extends BaseActivity implements OnItemSelectedListener {
	private EditText et7,et9/*,et10*/,et_9;//et_9是后来加上去的开户行
	private Spinner sp_et8;
	private ImageView iv1,iv2;
	private Button btn1;

	private String /*group,*/idcard="",IDCardHand="",IDCardTails="",str2;
	private ImageView lastImageView;
	//改银行卡列表为下拉
	ArrayList<String> bankList=new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private FinalDb db;
	//private String role;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createsub2);

		initFakeTitle();
		db = FinalDb.create(this);
		
		et7=(EditText)findViewById(R.id.et7);
		sp_et8=(Spinner)findViewById(R.id.sp_et8);
		et9=(EditText)findViewById(R.id.et9);
		et_9=(EditText)findViewById(R.id.et_9);
//		et10=(EditText)findViewById(R.id.et10);
		iv1=(ImageView)findViewById(R.id.iv1);
		iv2=(ImageView)findViewById(R.id.iv2);
		btn1=(Button)findViewById(R.id.btn1);

		idcard=getIntent().getStringExtra("idcard");
		setTitle("开通下级");
//		et10.setHint("请输入开户上限");

		initBank();
		adapter=new ArrayAdapter<String>(KaiTongXiaJi2.this, android.R.layout.simple_spinner_item, bankList);
		sp_et8.setAdapter(adapter);
		sp_et8.setOnItemSelectedListener(this);
		iv1.setOnClickListener(this);
		iv2.setOnClickListener(this);
		btn1.setOnClickListener(this);
	}

	private void initBank() {
		// TODO Auto-generated method stub
		AjaxParams params=new AjaxParams("op","GetBanks");
		http.configRequestExecutionRetryCount(3);
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
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
						}
					}else{
						List<BankList> tmp = db.findAll(BankList.class);
						for (BankList bl : tmp) {
							bankList.add(bl.getTitle());
						}
					}
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}


	@Override
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.iv1:
			lastImageView=iv1;
			pickDialog(iv1);
			break;

		case R.id.iv2:
			lastImageView=iv2;
			pickDialog(iv2);
			break;

		case R.id.btn1:
			String str1=et7.getText().toString().trim();
			//String str2=et8.getText().toString().trim();
			String str3=et9.getText().toString().trim();
			String str_3=et_9.getText().toString().trim();
			String str4="";//et10.getText().toString().trim();
			if(str1.isEmpty()||str1==null){
				Tools.showTextToast(KaiTongXiaJi2.this, "银行卡号不能为空");
			}else if(str2.isEmpty()||str2==null){
				Tools.showTextToast(KaiTongXiaJi2.this, "开户行不能为空");
			}else if(str3.isEmpty()||str3==null){
				Tools.showTextToast(KaiTongXiaJi2.this, "结算费率不能为空");
			}else if(str_3.isEmpty()||str_3==null){
				Tools.showTextToast(KaiTongXiaJi2.this, "开户行不能为空");
			}/*else if(str4.isEmpty()||str4==null){
				Tools.showTextToast(KaiTongXiaJi2.this, "开户上限不能为空");
			}*/else if(IDCardHand.isEmpty()||IDCardHand==null){
				Tools.showTextToast(KaiTongXiaJi2.this, "身份证和银行卡的正面照不能为空");
			}else if(IDCardTails.isEmpty()||IDCardTails==null){
				Tools.showTextToast(KaiTongXiaJi2.this, "身份证和银行卡的反面照不能为空");
			}else {
				//CreateNext cNext=new CreateNext();
				GlobalVars.nextLevel.setYinHangKa(str1);
				GlobalVars.nextLevel.setBank(str2);
				GlobalVars.nextLevel.setBank_account(str_3);
				GlobalVars.nextLevel.setJieSuanFeiLv(str3);
				GlobalVars.nextLevel.setKaiHuShangXian(str4);
				GlobalVars.nextLevel.setIDCardHand(IDCardHand);
				GlobalVars.nextLevel.setIDCardTails(IDCardTails);
				//db.update(cNext, "shenFenZheng='"+idcard+"'");
				startActivity(new Intent(KaiTongXiaJi2.this, KaiTongXiaJi3.class)
				.putExtra("idcard", idcard)/*.putExtra("group", group)*/);
				finish();
			}
			break;

		default:
			break;
		}
	}

	private int flag=-1;
	private void pickDialog(ImageView iv) {
		// TODO Auto-generated method stub
		/*AlertDialog.Builder dialog=new AlertDialog.Builder(CreateSubordinate2.this)
		.setTitle("选择图片");
		ListView lv=new ListView(CreateSubordinate2.this);
		ArrayAdapter<String> adapter=new ArrayAdapter<>(CreateSubordinate2.this
				, android.R.layout.simple_list_item_single_choice, pickWay);
		lv.setAdapter(adapter);
		lv.setItemsCanFocus(false);//设为可选中1
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//设为可选中2
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				flag=position;//
			}
		});
		dialog.setView(lv);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (flag) {
				case 0:*/
		startActivityForResult(Tools.doPickPhotoFromGallery(),1);
		/*break;

				case 1:
					startActivityForResult(Tools.doTakePhoto(CreateSubordinate2.this),2);
					break;

				default:
					Tools.showTextToast(CreateSubordinate2.this, "请先选择图片选取方式");
					break;
				}
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.show();*/
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//LogUtils.e("requestCode="+requestCode+" resultCode="+resultCode+" data="+data.getData().toString());
		LogUtils.i("is return to result");
		if (resultCode == -1) {
			Tools.ShowLoadingActivity(KaiTongXiaJi2.this);
			upLoadPhoto(Tools.IntentToBitmap(KaiTongXiaJi2.this, data));
		}  
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void upLoadPhoto(Bitmap photo) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		String strFile = new String(Base64Coder.encodeLines(byteArray));
		lastImageView.setImageBitmap(photo);
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("op", "SingleFile");
		ajaxParams.put("UpFilePath", strFile);
		http.post(GlobalVars.url, ajaxParams, new AjaxCallBack<Object>() {
			@Override
			public void onFailure(Throwable t, int errorNo,
					String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(KaiTongXiaJi2.this);
				Tools.showTextToast(KaiTongXiaJi2.this,
						"上传失败，可能是网络问题");
			}
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				Tools.DismissLoadingActivity(KaiTongXiaJi2.this);
				try {
					JSONObject obj = new JSONObject(t.toString());
					int result = obj.getInt("result");
					//String path = obj.getString("url");
					if(result==1){
						Tools.showTextToast(KaiTongXiaJi2.this,
								"上传成功");
						switch (lastImageView.getId()) {
						case R.id.iv1:
							IDCardHand = obj.getString("url");
							break;

						case R.id.iv2:
							IDCardTails = obj.getString("url");
							break;
						}

					}else{
						Tools.showTextToast(KaiTongXiaJi2.this,
								"上传失败");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Tools.showTextToast(KaiTongXiaJi2.this,
							"JSONException");
				}
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
		// TODO Auto-generated method stub

	}
}
