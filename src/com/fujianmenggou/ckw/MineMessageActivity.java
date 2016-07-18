package com.fujianmenggou.ckw;

import java.io.File;
import java.io.FileNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.Tools;
import com.lib.chen.uil.UILUtils;

import dujc.dtools.FinalHttp;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;

public class MineMessageActivity extends BaseActivity implements OnClickListener {

	private ImageView imageView;
	private TextView textPhone;
	private EditText editNickName,editQQ,editSignature;
	private RadioGroup radioGroup;
	
	/** 获取到的图片路径 */
	private String picPath;
	private Uri photoUri;

	// 使用照相机拍照获取图片
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	// 使用相册中的图片
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	private RadioButton radio0;
	private RadioButton radio1;

	private int imageId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_message);
		findViewById(R.id.back).setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.imageview);
		imageView.setOnClickListener(this);
		textPhone = (TextView) findViewById(R.id.text_phone);
		editNickName = (EditText) findViewById(R.id.edit_nickName);
		editQQ = (EditText) findViewById(R.id.edit_qq);
		editSignature= (EditText) findViewById(R.id.edit_signature);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radio0 = (RadioButton) findViewById(R.id.radio0);
		radio1 = (RadioButton) findViewById(R.id.radio1);
		findViewById(R.id.btn_sure).setOnClickListener(this);
		initData();
	}
	
	private void initData() {
		FinalHttp http = new FinalHttp();
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("m","User");
		ajaxParams.put("a","getInfo");
		ajaxParams.put("uid", userInfoPreferences.getString("otherUid","451"));
		http.get(MyConstants.URL.PATH, ajaxParams, new AjaxCallBack<Object>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.showTextToast(context, "数据加载失败,请检查网络后重试");
			}
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				try {
					JSONObject obj = new JSONObject(t.toString());
					int code = obj.getInt("code");
					String message = obj.getString("message");
					JSONObject data = obj.getJSONObject("data");
					String phone = data.getString("phone");
					String nikename = data.getString("nickname");
					String qq = data.getString("qq");
					int sex = data.getInt("sex");
					String sign = data.getString("sign");
					String image=data.getString("image");
					if(!TextUtils.isEmpty(phone)){
						textPhone.setText(phone);
					}
					if(!TextUtils.isEmpty(nikename)){
						editNickName.setText(nikename);
					}
					if(!TextUtils.isEmpty(qq)){
						editQQ.setText(qq);
					}
					if(1==sex||2==sex){
						if(1==sex){
							radio0.setChecked(true);
						}else if(2==sex){
							radio1.setChecked(true);
						}
					}
					if(!TextUtils.isEmpty(sign)){
						editSignature.setText(sign);
//						Log.e("", "sign:"+sign);
					}
					if(!TextUtils.isEmpty(image)){
//						BitmapUtils bmp=new BitmapUtils(MineMessageActivity.this, Tools.getSDPath()+"/.bsetpay/imagecahce");;
//						bmp.display(imageView, image);
						UILUtils.getUilUtils().displayImage(MineMessageActivity.this, image, imageView);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Tools.showTextToast(context,
							"数据加载失败" + e.getMessage());
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.mine_message, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.imageview:
			getPhotos();
			break;
		case R.id.btn_sure:
			btnSure();
			break;

		default:
			break;
		}
	}

	private void btnSure() {
		AjaxParams ajaxParams = new AjaxParams();
//		ajaxParams.put("m", "User");
//		ajaxParams.put("a", "setInfo");
//		ajaxParams.put("uid", userInfoPreferences.getString("otherUid","451"));
		String nickname = editNickName.getText().toString().trim();
		String qq = editQQ.getText().toString().trim();
		String signature1 = editSignature.getText().toString().trim();
		String signature = editSignature.getText().toString();
		int id = radioGroup.getCheckedRadioButtonId();
		RadioButton radioButton = (RadioButton) findViewById(id);
		if(radioButton!=null){
			String sex = radioButton.getText().toString().trim();
			if("女".equals(sex )){
				ajaxParams.put("sex", "2");
			}else{
				ajaxParams.put("sex", "1");
			}
		}
		if(TextUtils.isEmpty(nickname)){
			Tools.showTextToast(this, "昵称不能为空");
			return;
		}else{
			ajaxParams.put("nickname", nickname);
		}
		if(TextUtils.isEmpty(qq)){
			Tools.showTextToast(this, "qq不能为空");
			return;
		}else{
			ajaxParams.put("qq", qq);
		}
		if(TextUtils.isEmpty(signature1)){
			Tools.showTextToast(this, "个性签名不能为空");
			return;
		}else{
			ajaxParams.put("sign", signature);
		}
		if(imageId!=0){
			ajaxParams.put("image", ""+imageId);
		}
//		Log.e("", "ajaxParams"+ajaxParams.toString());
			Tools.ShowLoadingActivity(context);
			http.post(
					MyConstants.URL.PATH+"?m=User&a=setInfo&uid="+userInfoPreferences.getString("otherUid","451")+"&", ajaxParams,
					new AjaxCallBack<Object>() {
						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							// TODO Auto-generated method stub
							super.onFailure(t, errorNo, strMsg);
							Tools.DismissLoadingActivity(context);
							Tools.showTextToast(context, "提交失败，请检查网络");
						}

						@Override
						public void onSuccess(Object t) {
							super.onSuccess(t);
							Tools.DismissLoadingActivity(context);
							try {
								JSONObject obj = new JSONObject(t.toString());
								int code = obj.getInt("code");
								String message = obj.getString("message");
								String data = obj.getString("data");
								Tools.showTextToast(context, "信息提交成功");
							} catch (JSONException e) {
								e.printStackTrace();
								Tools.showTextToast(context,
										"提交失败" + e.getMessage());
							}
						}
					});
	}

	/**
	 * 弹出对话框菜单，根据传入值判断是通过照相还是选择照片来设置标志或背景
	 */
	private void getPhotos() {
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialogWindowAnim);
		window.setContentView(R.layout.dialog_item_addphotos);
		window.findViewById(R.id.dialog_addphotos_cancle).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				dialog.cancel();
			}
		});
		window.findViewById(R.id.dialog_addphotos_btn_cancle).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				dialog.cancel();
			}
		});
		window.findViewById(R.id.dialog_addphotos_btn_cammer).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) 
			{
				dialog.cancel();
				// 执行拍照前，应该先判断SD卡是否存在
				String SDState = Environment.getExternalStorageState();
				if (SDState.equals(Environment.MEDIA_MOUNTED)) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// "android.media.action.IMAGE_CAPTURE"
					/***
					 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
					 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
					 */
					ContentValues values = new ContentValues();
					photoUri = MineMessageActivity.this.getContentResolver().insert(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
					/** ----------------- */
					startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
				} else {
					Toast.makeText(MineMessageActivity.this, "内存卡不存在", Toast.LENGTH_LONG).show();
				}
			}
		});
		window.findViewById(R.id.dialog_addphotos_btn_photo).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				dialog.cancel();
//				startActivityForResult(Tools.doPickPhotoFromGallery(),
//						SELECT_PIC_BY_PICK_PHOTO);
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == Activity.RESULT_OK){
			doPhoto(requestCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 选择图片后，获取图片的路径
	 * 
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode, Intent data){
		if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {// 从相册取图片，有些手机有异常情况，请注意
			if (data == null){
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if (photoUri == null){
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
		}
		String[] pojo = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
		if (cursor != null)
		{
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
//			cursor.close();
			//4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)  
            if(Integer.parseInt(Build.VERSION.SDK) < 14)  
            {  
                cursor.close();  
            } 
		}
		if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))){
//			Bitmap bm = BitmapFactory.decodeFile(picPath);
//			imageView.setImageBitmap(bm);
			upLoadPhoto(picPath);
		} else{
			Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * 将选择后的图片上传，上传成功后的ID，及图片路径、图片本身保存到容器，
	 * 
	 * @param pas
	 * @param maps
	 */
	private void upLoadPhoto(final String pas) {
		Tools.ShowLoadingActivity(context);
		AjaxParams ajaxParams = new AjaxParams();
		try {
			ajaxParams.put("image", new File(pas));
		} catch (FileNotFoundException e1) {
			Tools.showTextToast(context, "图片路径错误");
			e1.printStackTrace();
		}
		http.post(MyConstants.URL.PATH + "?m=File&a=upload&uid="
				+ userInfoPreferences.getString("otherUid", "451"), ajaxParams,
				new AjaxCallBack<Object>() {

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						Tools.DismissLoadingActivity(context);
						Tools.showTextToast(context, "上传失败，请检查网络");
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Tools.DismissLoadingActivity(context);
						try {
							JSONObject obj = new JSONObject(t.toString());
							int code = obj.getInt("code");
							String message = obj.getString("message");
							JSONObject data = obj.getJSONObject("data");
							imageId = data.getInt("id");
							getImageUrl(""+imageId);
						} catch (JSONException e) {
							e.printStackTrace();
							Tools.showTextToast(context,
									"图片上传失败" + e.getMessage());
						}
					}
				});
	}
	private void getImageUrl(final String id) {
		FinalHttp http = new FinalHttp();
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("m","File");
		ajaxParams.put("a","image");
		ajaxParams.put("uid", userInfoPreferences.getString("otherUid","451"));
//		ajaxParams.put("uid", "451");
		ajaxParams.put("pid", id);
		http.get(MyConstants.URL.PATH, ajaxParams, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				try {
					JSONObject obj = new JSONObject(t.toString());
					int code = obj.getInt("code");
					String message = obj.getString("message");
					JSONObject data = obj.getJSONObject("data");
					String image=data.getString("image");
					UILUtils.getUilUtils().displayImage(MineMessageActivity.this,image , imageView);
					Tools.DismissLoadingActivity(context);
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("SetMyShopActivity", "通过Id获取图片地址失败"+e.getMessage());
				}
			}
		});
	}
}
