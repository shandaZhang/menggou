package com.fujianmenggou.atv;

import java.io.ByteArrayOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.util.Base64Coder;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ModifyShopInfoActivity extends BaseActivity {

	private ImageView imageView_touxiang;
	private EditText et_name,et_wechat;
	private String name,wechat,pic;
	private Intent intent;
	private ImageView iv_actionBar_add;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifyshop);
		
		initFakeTitle();
		
		setTitle("店铺修改");
		
		intent = getIntent();
		name = intent.getStringExtra("name");
		pic = intent.getStringExtra("pic");
		wechat = intent.getStringExtra("wechat");
		
		iv_actionBar_add = (ImageView)findViewById(R.id.iv_actionbar_add);
		iv_actionBar_add.setImageResource(R.drawable.ok);
		iv_actionBar_add.setVisibility(View.VISIBLE);
		iv_actionBar_add.setOnClickListener(this);
		
		imageView_touxiang = (ImageView) findViewById(R.id.imageView_touxiang);
		et_name = (EditText) findViewById(R.id.et_name);
		et_wechat = (EditText) findViewById(R.id.et_wechat);
		
		imageView_touxiang.setOnClickListener(this);
		
		et_name.setText(name);
		et_wechat.setText(wechat);
		bmp.display(imageView_touxiang, pic);
	}
	
	private void upLoadPhoto(final Bitmap photo) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		String strFile = new String(Base64Coder.encodeLines(byteArray));

		Tools.ShowLoadingActivity(context);
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("op", "SingleFile");
		ajaxParams.put("UpFilePath", strFile);
		http.post(GlobalVars.url, ajaxParams, new AjaxCallBack<Object>() {
			@Override
			public void onFailure(Throwable t, int errorNo,
					String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
				Tools.showTextToast(context,
						"上传失败，可能是网络问题");
			}
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				try {
					JSONObject obj = new JSONObject(t.toString());
					int result = obj.getInt("result");
					//String path = obj.getString("url");
					if(result==1){
						Tools.showTextToast(context, "上传成功");
						pic = obj.getString("url");
						LogUtils.i(pic);
						imageView_touxiang.setImageBitmap(photo);
					}else{
						Tools.showTextToast(context, "上传失败");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Tools.showTextToast(context,
							"JSONException");
				}
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//LogUtils.e("requestCode="+requestCode+" resultCode="+resultCode+" data="+data.getData().toString());
		if (resultCode == -1) {  
			upLoadPhoto(Tools.IntentToBitmap(context, data));
		}  
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.imageView_touxiang:
			startActivityForResult(Tools.doPickPhotoFromGallery(),1);
			break;
		case R.id.iv_actionbar_add:
			name = et_name.getText().toString().trim();
			wechat = et_wechat.getText().toString().trim();
			if(pic==null||pic.isEmpty()){
				Tools.showTextToast(context, "头像不能为空");
			} else if(name==null||name.isEmpty()){
				Tools.showTextToast(context, "店铺名称不能为空");
			} else if(wechat==null||wechat.isEmpty()){
				Tools.showTextToast(context, "微信号不能为空");
			} else {
				Tools.ShowLoadingActivity(context);
				AjaxParams params =new AjaxParams();
				params.put("op", "EditUserShop");
				params.put("userID", GlobalVars.getUid(context));
				params.put("name", name);
				params.put("pic", pic);
				params.put("wechat", wechat);
				http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Tools.DismissLoadingActivity(context);
						try {
							JSONObject json = new JSONObject(t);
							if(json.getInt("result")==1){
								intent.putExtra("name", name);
								intent.putExtra("pic", GlobalVars.baseUrl+pic);
								setResult(32, intent);
								finish();
							}else{
								Tools.showTextToast(context, "修改失败，请稍候重试");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						Tools.DismissLoadingActivity(context);
					}
				});
			}
			break;
		default:
			break;
		}
	}
}
