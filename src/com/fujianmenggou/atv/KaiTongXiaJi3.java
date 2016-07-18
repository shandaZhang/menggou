package com.fujianmenggou.atv;

import java.io.ByteArrayOutputStream;

import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.util.Base64Coder;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class KaiTongXiaJi3 extends BaseActivity {

	//,,,
	private ImageView iv3,/*iv4,*/iv5;
	private Button btn1;

	private String /*group,*/idcard="",bankCardHeads="",IDCardHeads="";
	private ImageView lastImageView;
	//private String role;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createsub3);

		initFakeTitle();
		
		iv3=(ImageView) findViewById(R.id.iv3);
		//iv4=(ImageView) findViewById(R.id.iv4);
		iv5=(ImageView) findViewById(R.id.iv5);
		btn1=(Button) findViewById(R.id.btn1);

		idcard=getIntent().getStringExtra("idcard");
		setTitle("开通下级");
		//et3.setMovementMethod(ScrollingMovementMethod.getInstance());
		iv3.setOnClickListener(this);
		//iv4.setOnClickListener(this);
		iv5.setOnClickListener(this);
		btn1.setOnClickListener(this);
	}


	@Override
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.iv3:
			lastImageView=iv3;
			pickDialog(iv3);
			break;

		/*case R.id.iv4:
			lastImageView=iv4;
			pickDialog(iv4);
			break;*/

		case R.id.iv5:
			lastImageView=iv5;
			pickDialog(iv5);
			break;

		case R.id.btn1:
			if(bankCardHeads.isEmpty()||bankCardHeads==null){
				Tools.showTextToast(KaiTongXiaJi3.this, "手持银行卡照片不能为空");
			}else if(IDCardHeads.isEmpty()||IDCardHeads==null){
				Tools.showTextToast(KaiTongXiaJi3.this, "手持身份证照片不能为空");
			}else {
				//CreateNext cNext=new CreateNext();
				GlobalVars.nextLevel.setIDCardHeads(IDCardHeads);
				GlobalVars.nextLevel.setBankCardHeads(bankCardHeads);
				//db.update(cNext, "shenFenZheng='"+idcard+"'");
				startActivity(new Intent(KaiTongXiaJi3.this, KaiTongXiaJi4.class)
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
		/*AlertDialog.Builder dialog=new AlertDialog.Builder(CreateSubordinate3.this)
		.setTitle("选择图片");
		ListView lv=new ListView(CreateSubordinate3.this);
		ArrayAdapter<String> adapter=new ArrayAdapter<>(CreateSubordinate3.this
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
					startActivityForResult(Tools.doTakePhoto(CreateSubordinate3.this),2);
					break;

				default:
					Tools.showTextToast(CreateSubordinate3.this, "请先选择图片选取方式");
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
		if (resultCode == -1) {  
			switch (requestCode) {
			case 1:
				upLoadPhoto(Tools.IntentToBitmap(KaiTongXiaJi3.this, data));
				break;

			case 2:
				upLoadPhoto(Tools.IntentToBitmap(KaiTongXiaJi3.this, data));
				break;
			}
		}  
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void upLoadPhoto(final Bitmap photo) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		String strFile = new String(Base64Coder.encodeLines(byteArray));
		
		Tools.ShowLoadingActivity(KaiTongXiaJi3.this);
		
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("op", "SingleFile");
		ajaxParams.put("UpFilePath", strFile);
		http.post(GlobalVars.url, ajaxParams, new AjaxCallBack<Object>() {
			@Override
			public void onFailure(Throwable t, int errorNo,
					String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(KaiTongXiaJi3.this);
				Tools.showTextToast(KaiTongXiaJi3.this,
						"上传失败，可能是网络问题");
			}
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				Tools.DismissLoadingActivity(KaiTongXiaJi3.this);
				try {
					JSONObject obj = new JSONObject(t.toString());
					int result = obj.getInt("result");
					//String path = obj.getString("url");
					if(result==1){
						Tools.showTextToast(KaiTongXiaJi3.this,
								"上传成功");
						switch (lastImageView.getId()) {
						case R.id.iv3:
							IDCardHeads = obj.getString("url");
							break;

						/*case R.id.iv4:
							yhkFanMian = obj.getString("url");
							break;*/

						case R.id.iv5:
							bankCardHeads = obj.getString("url");
							break;
						}
						lastImageView.setImageBitmap(photo);
					}else{
						Tools.showTextToast(KaiTongXiaJi3.this,
								"上传失败");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Tools.showTextToast(KaiTongXiaJi3.this,
							"JSONException");
				}
			}
		});
	}
}
