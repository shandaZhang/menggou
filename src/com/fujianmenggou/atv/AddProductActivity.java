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

public class AddProductActivity extends BaseActivity {
	private String _id,name,pic,stock,price,content;
	private ImageView iv_pic;
	private EditText et_name,et_stock,et_price,et_content;
	private boolean isAdd =true;
	private ImageView iv_actionBar_add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addshop);
		
		initFakeTitle();
		
		isAdd=getIntent().getBooleanExtra("isAdd", true);
		
		iv_actionBar_add = (ImageView)findViewById(R.id.iv_actionbar_add);
		iv_actionBar_add.setImageResource(R.drawable.ok);
		iv_actionBar_add.setVisibility(View.VISIBLE);
		iv_actionBar_add.setOnClickListener(this);

		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		et_name = (EditText) findViewById(R.id.et_name);
		et_stock = (EditText) findViewById(R.id.et_stock);
		et_price = (EditText) findViewById(R.id.et_price);
		et_content = (EditText) findViewById(R.id.et_content);

		if(!isAdd){
			_id=getIntent().getStringExtra("id");
			name=getIntent().getStringExtra("name");
			pic=getIntent().getStringExtra("pic");
			stock=getIntent().getStringExtra("stock");
			price=getIntent().getStringExtra("price");
			content=getIntent().getStringExtra("content");
			
			LogUtils.i(pic);
			bmp.display(iv_pic, pic);
			et_content.setText(content);
			et_name.setText(name);
			et_price.setText(price);
			et_stock.setText(stock);
		}
		iv_pic.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//LogUtils.e("requestCode="+requestCode+" resultCode="+resultCode+" data="+data.getData().toString());
		if (resultCode == -1) {  
			upLoadPhoto(Tools.IntentToBitmap(context, data));
		}  
		super.onActivityResult(requestCode, resultCode, data);
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
						iv_pic.setImageBitmap(photo);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_pic:
			startActivityForResult(Tools.doPickPhotoFromGallery(),1);
			break;
		case R.id.iv_actionbar_add:
			if(isAdd)
				addProduct();
			else
				editProduct();
			break;

		default:
			break;
		}
	}
	
	private void addProduct(){
		name= et_name.getText().toString().trim();
		stock=et_stock.getText().toString().trim();
		price=et_price.getText().toString().trim();
		content=et_content.getText().toString().trim();

		if(name==null||name.isEmpty()){
			Tools.showTextToast(context, "商品名称不能为空");
		}else if(pic==null||pic.isEmpty()){
			Tools.showTextToast(context, "商品图片不能为空");
		}else if(stock==null||stock.isEmpty()){
			Tools.showTextToast(context, "商品库存不能为空");
		}else if(price==null||price.isEmpty()){
			Tools.showTextToast(context, "商品价格不能为空");
		}else if(content==null||content.isEmpty()){
			Tools.showTextToast(context, "商品描述不能为空");
		}else {
			Tools.ShowLoadingActivity(context);
			AjaxParams params = new AjaxParams();
			params.put("op", "AddProduct");
			params.put("userID", GlobalVars.getUid(context));
			params.put("name", name);
			params.put("pic", pic);
			params.put("stock", stock);
			params.put("price", price);
			params.put("content", content);
			http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					super.onSuccess(t);
					LogUtils.i(t);
					Tools.DismissLoadingActivity(context);
					try {
						JSONObject json = new JSONObject(t);
						if(json.getInt("result")==1){
							Tools.showTextToast(context, "添加成功");
							Intent data = getIntent();
							Bundle bundle = new Bundle();
							bundle.putString("id", json.getString("id"));
							bundle.putString("name", name);
							bundle.putString("content", content);
							bundle.putString("pic", pic);
							bundle.putString("stock", stock);
							bundle.putString("price", price);
							data.putExtra("newItem", bundle);
							setResult(321, data);
							finish();
						}else{
							Tools.showTextToast(context, "添加失败，请稍后重试");
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
	}
	private void editProduct(){
		name= et_name.getText().toString().trim();
		stock=et_stock.getText().toString().trim();
		price=et_price.getText().toString().trim();
		content=et_content.getText().toString().trim();
		pic=pic.contains(GlobalVars.baseUrl)?pic.replace(GlobalVars.baseUrl, ""):pic;
		
		if(name==null||name.isEmpty()){
			Tools.showTextToast(context, "商品名称不能为空");
		}else if(pic==null||pic.isEmpty()){
			Tools.showTextToast(context, "商品图片不能为空");
		}else if(stock==null||stock.isEmpty()){
			Tools.showTextToast(context, "商品库存不能为空");
		}else if(price==null||price.isEmpty()){
			Tools.showTextToast(context, "商品价格不能为空");
		}else if(content==null||content.isEmpty()){
			Tools.showTextToast(context, "商品描述不能为空");
		}else {
			Tools.ShowLoadingActivity(context);
			AjaxParams params = new AjaxParams();
			params.put("op", "EditProduct");
			params.put("id", _id);
			params.put("name", name);
			params.put("pic", pic);
			params.put("stock", stock);
			params.put("price", price);
			params.put("content", content);
			http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					super.onSuccess(t);
					LogUtils.i(t);
					Tools.DismissLoadingActivity(context);
					try {
						JSONObject json = new JSONObject(t);
						if(json.getInt("result")==1){
							Tools.showTextToast(context, "修改成功");
							Intent data = getIntent();
							Bundle bundle = new Bundle();
							bundle.putString("id", _id);
							bundle.putString("name", name);
							bundle.putString("content", content);
							bundle.putString("pic", pic);
							bundle.putString("stock", stock);
							bundle.putString("price", price);
							data.putExtra("newItem", bundle);
							setResult(321, data);
							finish();
						}else{
							Tools.showTextToast(context, "修改失败，请稍后重试");
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
	}
}
