package com.fujianmenggou.ckw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.Tools;
import com.lib.chen.uil.UILUtils;

import dujc.dtools.FinalHttp;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
/**
 * 个人中心.我的店铺界面
 * @author Ckw  20150522
 *	
 */
public class SetMyShopActivity extends BaseActivity {
	private EditText editShopName,editPhone,editName,editAdds;
	/** 获取到的图片路径 */
	private String picPath;
	private Uri photoUri;

	// 使用照相机拍照获取图片
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	// 使用相册中的图片
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	
	private ImageView imageShop;
	
	private MyAdapter mAdapter;
	private int isCheck=-1;
	private int imageIdshop;
	private ArrayList<String> imageIdbac=new ArrayList<String>();
//	private ArrayList<String> imageUrl=new ArrayList<String>();
	private HashMap<String, String> imageUrl=new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_my_shop);
		findViewById(R.id.btn_finish).setOnClickListener(this);
		
		editShopName = (EditText) findViewById(R.id.edit_shop_name);
		editPhone = (EditText) findViewById(R.id.Edit_phone);
		editName = (EditText) findViewById(R.id.edit_name);
		editAdds = (EditText) findViewById(R.id.Edit_adds);
		
		imageShop = (ImageView) findViewById(R.id.setMyshop_image_shop);
		imageShop.setOnClickListener(this);
		GridView mGridView = (GridView) findViewById(R.id.setMyshop_grid);
		mAdapter = new MyAdapter();
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				isCheck=position;
//				getPhotos();
			}
		});
		mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
//				new AlertDialog.Builder(SetMyShopActivity.this)
//				.setMessage("您确定要删除该图片吗")
//				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						imageIdbac.remove(position);
//						mAdapter.notifyDataSetChanged();
//					}
//				}).setNegativeButton("取消", null)
//				.create().show();
				return false;
			}
		});
		findViewById(R.id.btn_sare).setOnClickListener(this);
//		initData();
	}
	
	private void initData() {
		Tools.ShowLoadingActivity(context);
		FinalHttp http = new FinalHttp();
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("m","Store");
		ajaxParams.put("a","getInfo");
		ajaxParams.put("uid", userInfoPreferences.getString("otherUid","451"));
		http.get(MyConstants.URL.PATH, ajaxParams, new AjaxCallBack<Object>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
				Tools.showTextToast(context, "数据加载失败,请检查网络后重试");
			}
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				try {
					JSONObject obj = new JSONObject(t.toString());
					int code = obj.getInt("code");
					String message = obj.getString("message");
					JSONObject data = obj.getJSONObject("data");
					String name = data.getString("name");
					String linkman = data.getString("linkman");
					String phone = data.getString("phone");
					String address = data.getString("address");
					String logo = data.getString("logo");
					String image=data.getString("image");
					if(!TextUtils.isEmpty(name)){
						editShopName.setText(name);
					}
					if(!TextUtils.isEmpty(linkman)){
						editName.setText(linkman);
					}
					if(!TextUtils.isEmpty(phone)){
						editPhone.setText(phone+"");
					}
					if(!TextUtils.isEmpty(address)){
						editAdds.setText(address);
					}
					if(!TextUtils.isEmpty(logo)){
						UILUtils.getUilUtils().displayImage(SetMyShopActivity.this, logo, imageShop);
					}
					if(!TextUtils.isEmpty(image)&&!"null".equals(image)&&!"--".equals(image)){
						String[] split = image.split("-");
						int length = split.length;
						if(1==length){
							imageIdbac.add(split[0]);
							getImageUrl(split[0]);
						}else if(2==length){
							imageIdbac.add(split[0]);
							getImageUrl(split[0]);
							imageIdbac.add(split[1]);
							getImageUrl(split[1]);
						}else if(3==length){
							imageIdbac.add(split[0]);
							getImageUrl(split[0]);
							imageIdbac.add(split[1]);
							getImageUrl(split[1]);
							imageIdbac.add(split[2]);
							getImageUrl(split[2]);
						}
					}else{
						Tools.DismissLoadingActivity(context);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("数据加载失败", ""+e.getMessage());
					Tools.DismissLoadingActivity(context);
					Tools.showTextToast(context,
							"数据加载失败" + e.getMessage());
				}
			}
		});
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View inflate = getLayoutInflater().inflate(R.layout.item_setmyshop_grid, null);
			ImageView image = (ImageView) inflate.findViewById(R.id.item_setmyshop_image);
			if(imageIdbac.size()>0){
				if(imageIdbac.size()<3){
					if(imageIdbac.size()==position){
					}else{
						UILUtils.getUilUtils().displayImage(SetMyShopActivity.this, imageUrl.get(imageIdbac.get(position)), image);
					}
				}else{
					UILUtils.getUilUtils().displayImage(SetMyShopActivity.this, imageUrl.get(imageIdbac.get(position)), image);
				}
			}
			return inflate;
		}
		@Override
		public int getCount() {
			if(imageIdbac.size()<3){
				return imageIdbac.size()+1;
			}else{
				return imageIdbac.size();
			}
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_finish:
			finish();
			break;
		case R.id.setMyshop_image_shop:
//			isCheck=9;
//			getPhotos();
			break;
		case R.id.btn_sare:
//			btnSare();
			break;

		default:
			break;
		}
	}
	
	private void btnSare() {
		// TODO Auto-generated method stub
		AjaxParams ajaxParams = new AjaxParams();
		
		String trimShopName = editShopName.getText().toString().trim();
		String trimPhone = editPhone.getText().toString().trim();
		String trimName = editName.getText().toString().trim();
		String trimAdds = editAdds.getText().toString().trim();
		
		if(TextUtils.isEmpty(trimShopName)){
			Tools.showTextToast(this, "店铺名称不能为空");
			return;
		}else{
			ajaxParams.put("name", trimShopName);
		}
		if(TextUtils.isEmpty(trimPhone)){
			Tools.showTextToast(this, "电话不能为空");
			return;
		}else{
			ajaxParams.put("phone", trimPhone);
		}
		if(TextUtils.isEmpty(trimName)){
			Tools.showTextToast(this, "联系人不能为空");
			return;
		}else{
			ajaxParams.put("linkman", trimName);
		}
		if(TextUtils.isEmpty(trimShopName)){
			Tools.showTextToast(this, "店铺名称不能为空");
			return;
		}else{
			ajaxParams.put("name", trimShopName);
		}
		if(TextUtils.isEmpty(trimAdds)){
			Tools.showTextToast(this, "地址不能为空");
			return;
		}else{
			ajaxParams.put("address", trimAdds);
		}
		if(imageIdshop!=0){
			ajaxParams.put("logo", imageIdshop+"");
		}
		if(0<imageIdbac.size()){
			StringBuffer image=new StringBuffer();
			image.append(imageIdbac.get(0));
			if(1<imageIdbac.size()){
				image.append("-"+imageIdbac.get(1));
			}
			if(2<imageIdbac.size()){
				image.append("-"+imageIdbac.get(2));
			}
			ajaxParams.put("image", image.toString());
		}else{
			ajaxParams.put("image", "--");
		}
		Tools.ShowLoadingActivity(context);
//		Log.e("1", ""+MyConstants.URL.PATH+"?m=Store&a=setInfo" +"&uid=451");
//		Log.e("2", ""+ajaxParams.toString());
		http.post(
				MyConstants.URL.PATH+"?m=Store&a=setInfo" +
						"&uid="+userInfoPreferences.getString("otherUid","451"), ajaxParams,
//						"&uid=451", ajaxParams,
//						"&uid=451&linkman="+trimName, ajaxParams,
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

//							Log.e("信息提交", "code:"+code+"_message:"+message+"_data:"+data);
							Tools.showTextToast(context, "信息修改成功");
						} catch (JSONException e) {
							e.printStackTrace();
							Log.e("提交失败", e.getMessage());
							Tools.showTextToast(context,
									"提交失败" + e.getMessage());
						}
					}
				});
	}

	/**
	 * 弹出对话框菜单，根据传入值判断是通过照相还是选择照片来设置标志或背景
	 * @param i 
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
					photoUri = SetMyShopActivity.this.getContentResolver().insert(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
					/** ----------------- */
					startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
				} else {
					Toast.makeText(SetMyShopActivity.this, "内存卡不存在", Toast.LENGTH_LONG).show();
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
			upLoadPhoto(picPath);//上传图片
			
//			Bitmap bm = BitmapFactory.decodeFile(picPath);
//			imageView.setImageBitmap(bm);
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
						super.onFailure(t, errorNo, strMsg);
						Tools.DismissLoadingActivity(context);
						Tools.showTextToast(context, "上传失败，请检查网络");
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
//						Log.e("", ""+t.toString());
						try {
							JSONObject obj = new JSONObject(t.toString());
							int code = obj.getInt("code");
							String message = obj.getString("message");
							JSONObject data = obj.getJSONObject("data");
							int imageId = data.getInt("id");
							if(9==isCheck){
								imageIdshop=imageId;
								getImageUrl(""+imageId);
							}else{
								if(imageIdbac.size()==isCheck){
									imageIdbac.add(imageId+"");
									getImageUrl(""+imageId);
								}else{
									imageIdbac.set(isCheck, imageId+"");
									getImageUrl(""+imageId);
								}
							}
//							Log.e("图片上传","code:"+code+"message:"+message+ "图片ID:" + imageId);
							Tools.showTextToast(context, "图片上传成功"+imageId);
						} catch (JSONException e) {
							e.printStackTrace();
							Tools.DismissLoadingActivity(context);
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
					imageUrl.put(id, image);
					if(9==isCheck){
						UILUtils.getUilUtils().displayImage(SetMyShopActivity.this,imageUrl.get(""+imageIdshop) , imageShop);
					}
					mAdapter.notifyDataSetChanged();
					Tools.DismissLoadingActivity(context);
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("SetMyShopActivity", "通过Id获取图片地址失败"+e.getMessage());
				}
			}
		});
	}
	
}
