package com.fujianmenggou.ckw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.Tools;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;

public class AddGoodsActivity extends BaseActivity implements OnClickListener {

	private View view_menu;
	private View View_menu_null;

	/** 获取到的图片路径 */
	private String picPath;
	private Uri photoUri;

	// 使用照相机拍照获取图片
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	// 使用相册中的图片
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;

	private GridView gridView;
	private GridAdapter mAdapter;
	private String imagePath[] = null;
	private ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
	private ArrayList<String> bitmappaths = new ArrayList<String>();
	private ArrayList<Integer> imageIds = new ArrayList<Integer>();
	private TextView text_title;
	private EditText edit_name;
	private EditText edit_des;
	private EditText edit_price;
	private EditText edit_num;
	private Editor edit;
	private boolean isSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		setContentView(R.layout.fragment_add_goods);

		SharedPreferences sp = getSharedPreferences("addGoodsShared",
				Context.MODE_PRIVATE);
		edit = sp.edit();

		initData(sp);

		initMenu();
		findViewById(R.id.btn_finish).setOnClickListener(this);// 返回
		text_title = (TextView) findViewById(R.id.text_title);
		text_title.setOnClickListener(this);
		findViewById(R.id.btn_save).setOnClickListener(this);
		findViewById(R.id.btn_putaway).setOnClickListener(this);

		ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
		gridView = (GridView) findViewById(R.id.grid);
		mAdapter = new GridAdapter();
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == bitmappaths.size()) {
					menuShow();
				} else {

				}
				if (bitmappaths.size() >= 5) {

				}
			}
		});
		scrollView.smoothScrollTo(0, 0);
	}

	private void initData(SharedPreferences sp) {
		edit_name = (EditText) findViewById(R.id.edit_name);
		edit_des = (EditText) findViewById(R.id.edit_des);
		edit_price = (EditText) findViewById(R.id.edit_price);
		edit_num = (EditText) findViewById(R.id.edit_num);

		String name = sp.getString("name", "");
		if (!"".equals(name)) {
			edit_name.setText(name);
		}
		String des = sp.getString("des", "");
		if (!"".equals(des)) {
			edit_des.setText(des);
		}
		String price = sp.getString("price", "");
		if (!"".equals(price)) {
			edit_price.setText(price);
		}
		String num = sp.getString("num", "");
		if (!"".equals(num)) {
			edit_num.setText(num);
		}
		String path1 = sp.getString("path1", "");
		if (!"".equals(path1)) {
			int int1 = sp.getInt("ids1", 0);
			imageIds.add(int1);
			bitmappaths.add(path1);
			Bitmap Bitmap1 = BitmapFactory.decodeFile(path1);
			bitmaps.add(Bitmap1);
		}
		String path2 = sp.getString("path2", "");
		if (!"".equals(path2)) {
			int int2 = sp.getInt("ids2", 0);
			imageIds.add(int2);
			bitmappaths.add(path2);
			Bitmap Bitmap2 = BitmapFactory.decodeFile(path2);
			bitmaps.add(Bitmap2);
		}
		String path3 = sp.getString("path3", "");
		if (!"".equals(path3)) {
			int int3 = sp.getInt("ids3", 0);
			imageIds.add(int3);
			bitmappaths.add(path3);
			Bitmap Bitmap3 = BitmapFactory.decodeFile(path3);
			bitmaps.add(Bitmap3);
		}
		String path4 = sp.getString("path4", "");
		if (!"".equals(path4)) {
			int int4 = sp.getInt("ids4", 0);
			imageIds.add(int4);
			bitmappaths.add(path4);
			Bitmap Bitmap4 = BitmapFactory.decodeFile(path4);
			bitmaps.add(Bitmap4);
		}
		String path5 = sp.getString("path5", "");
		if (!"".equals(path5)) {
			int int5 = sp.getInt("ids5", 0);
			imageIds.add(int5);
			bitmappaths.add(path5);
			Bitmap Bitmap5 = BitmapFactory.decodeFile(path5);
			bitmaps.add(Bitmap5);
		}
		// mAdapter.notifyDataSetChanged();
		edit.clear();
		edit.commit();
	}

	class GridAdapter extends BaseAdapter {

		// private List<String> imagePath;
		//
		// public GridAdapter(ArrayList<String> imagePath) {
		// this.imagePath=new ArrayList<String>();
		//
		// for (String image : imagePath) {
		// this.imagePath.add(image);
		// }
		// }

		public void reorder(int from, int to) {
			if (from != to) {
				Integer removeIds = imageIds.remove(from);
				imageIds.add(to, removeIds);

				String removePaths = bitmappaths.remove(from);
				bitmappaths.add(to, removePaths);

				Bitmap removeBitmaps = bitmaps.remove(from);
				bitmaps.add(to, removeBitmaps);
				// String remove = imagePath.remove(from);
				// imagePath.add(to, remove);
				Log.e("reorder", removeIds + "---" + removePaths + "---"
						+ removeBitmaps);
				notifyDataSetChanged();
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflate = getLayoutInflater().inflate(
					R.layout.list_addgoods_image_item, null);
			ImageView btn_cangle_image = (ImageView) inflate
					.findViewById(R.id.btn_cangle_image);
			View btn_del_image = inflate.findViewById(R.id.btn_del_image);
			final int pos = position;
			if (position != bitmappaths.size()) {
				btn_del_image.setVisibility(View.VISIBLE);
				btn_cangle_image.setImageBitmap(bitmaps.get(position));
			}
			if (bitmappaths.size() == 0) {
				btn_del_image.setVisibility(View.INVISIBLE);
				btn_cangle_image
						.setImageResource(R.drawable.ring_make_add_unable);
			}
			if (bitmappaths.size() >= 5) {
				btn_del_image.setVisibility(View.INVISIBLE);
				btn_cangle_image.setVisibility(View.INVISIBLE);
			}
			btn_del_image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(context)
							.setMessage("确认要删除这张图片吗？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											imageIds.remove(pos);
											bitmappaths.remove(pos);
											bitmaps.remove(pos);
											mAdapter.notifyDataSetChanged();
										}
									}).setNegativeButton("取消", null).create()
							.show();
				}
			});
			return inflate;
		}

		@Override
		public int getCount() {
			return bitmappaths.size() + 1;
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

	private void initMenu() {
		view_menu = findViewById(R.id.View_menu);
		View_menu_null = findViewById(R.id.View_menu_null);
		View_menu_null.setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		findViewById(R.id.btn_camer).setOnClickListener(this);
		findViewById(R.id.btn_photo).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.add_goods, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_finish:
			btnFinish();
			break;
		case R.id.View_menu_null:
		case R.id.btn_cancel:
			menuShow();
			break;
		case R.id.btn_camer:
			btnCamer();
			break;
		case R.id.btn_photo:
			btnPhoto();
			break;
		case R.id.btn_save:// 保存到仓库
			putaway(1);
			break;
		case R.id.btn_putaway:// 直接上架
			putaway(2);
			break;
		case R.id.text_title://

			break;

		default:
			break;
		}
	}

	/**
	 * 将商品上架或者保存仓库，保存完之后将页面内容清空，并清除缓存内容
	 * 
	 * @param k
	 *            1,保存到商品库2，直接上架
	 */
	private void putaway(final int k) {
		String name = edit_name.getText().toString().trim();
		String des = edit_des.getText().toString().trim();
		String price = edit_price.getText().toString().trim();
		String num = edit_num.getText().toString().trim();
		AjaxParams ajaxParams = new AjaxParams();
		if ("".equals(name)) {
			Tools.showTextToast(context, "商品名不能为空");
			return;
		} else {
			ajaxParams.put("name", name);
		}
		if (!"".equals(des)) {
			ajaxParams.put("des", des);
		}
		if ("".equals(price)) {
			Tools.showTextToast(context, "价格不能为空");
			return;
		} else {
			ajaxParams.put("price", price);
		}
		if ("".equals(num)) {
			Tools.showTextToast(context, "库存不能为空");
			return;
		} else {
			ajaxParams.put("num", num);
		}
		if (imageIds.size() == 0) {
			Tools.showTextToast(context, "至少上传一张图片");
			return;
		} else {
			for (int i = 0; i < imageIds.size(); i++) {
				ajaxParams.put("image" + (i + 1), "" + imageIds.get(i));
			}
		}
		Tools.ShowLoadingActivity(context);
		http.post(
				MyConstants.URL.PATH_APPEND + "&uid="
						+ userInfoPreferences.getString("otherUid", "451")
						+ "&action=" + k, ajaxParams,
				new AjaxCallBack<Object>() {
					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Tools.DismissLoadingActivity(context);
						Tools.showTextToast(context, "上架失败，请检查网络");
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
							int id = data.getInt("id");
							if (k == 2) {
								Tools.showTextToast(context, "商品上架成功\nid:" + id);
							} else {
								Tools.showTextToast(context, "已成功保存到商品库中\nid:"
										+ id);
							}
							edit_name.setText("");
							edit_des.setText("");
							edit_price.setText("");
							edit_num.setText("");
							bitmappaths.clear();
							edit.clear();
							edit.commit();

							mAdapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
							Tools.showTextToast(context,
									"上架失败" + e.getMessage());
						}
					}
				});
	}

	private void btnPhoto() {// 相册
		startActivityForResult(Tools.doPickPhotoFromGallery(),
				SELECT_PIC_BY_PICK_PHOTO);
		menuShow();
	}

	private void btnCamer() {// 拍照
		// TODO Auto-generated method stub
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
			photoUri = this.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			/** ----------------- */
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		} else {
			Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
		}
		menuShow();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {// 相册
				upLoadPhoto(Tools.IntentTopath(this, data),
						Tools.IntentToBitmap(this, data));
			} else {
				doPhoto(requestCode, data);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 将选择后的图片上传，上传成功后的ID，及图片路径、图片本身保存到容器，
	 * 
	 * @param pas
	 * @param maps
	 */
	private void upLoadPhoto(final String pas, final Bitmap maps) {
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
							int id = data.getInt("id");
							// Tools.showTextToast(context,
							// "code:"+code+"+++message:"+message+"+++id:"+id);
							imageIds.add(id);// ID
							bitmappaths.add(pas);// 路径
							bitmaps.add(maps);// 图片
							mAdapter.notifyDataSetChanged();
							Tools.showTextToast(context, "上传成功\n图片ID:" + id);
							Log.e("图片上传", "上传成功\n图片ID:" + id);
						} catch (JSONException e) {
							e.printStackTrace();
							Tools.showTextToast(context,
									"图片上传失败" + e.getMessage());
						}
					}
				});
	}

	/**
	 * 选择图片后，获取图片的路径，现在只用在获取拍照的图片
	 * 
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode, Intent data) {
		String[] pojo = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			// cursor.close();//选择一张图片没问题，但如果多张，不把这句话注释会出错
		}
		if (picPath != null) {
			Bitmap bm = BitmapFactory.decodeFile(picPath);
			upLoadPhoto(picPath, bm);
			mAdapter.notifyDataSetChanged();
		} else {
			Toast.makeText(this, "选择文件不正确!", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 显示及隐藏图片选择菜单
	 */
	private void menuShow() {
		if (view_menu.isShown()) {
			ObjectAnimator
					.ofFloat(view_menu, "translationY", 0,
							view_menu.getHeight()).setDuration(500).start();
			ObjectAnimator
					.ofFloat(View_menu_null, "translationY", 0,
							-View_menu_null.getHeight()).setDuration(500)
					.start();
			view_menu.postDelayed(new Runnable() {
				@Override
				public void run() {
					view_menu.setVisibility(View.INVISIBLE);
					View_menu_null.setVisibility(View.INVISIBLE);
				}
			}, 500);
		} else {
			view_menu.setVisibility(View.VISIBLE);
			View_menu_null.setVisibility(View.VISIBLE);
			ObjectAnimator
					.ofFloat(view_menu, "translationY", view_menu.getHeight(),
							0).setDuration(500).start();
			ObjectAnimator
					.ofFloat(View_menu_null, "translationY",
							-View_menu_null.getHeight(), 0).setDuration(500)
					.start();
		}
	}

	/**
	 * 退出界面前，先判断是否要保存到本地
	 */
	private void btnFinish() {
		// TODO Auto-generated method stub
		final String name = edit_name.getText().toString().trim();
		final String des = edit_des.getText().toString().trim();
		final String price = edit_price.getText().toString().trim();
		final String num = edit_num.getText().toString().trim();
		if (!"".equals(name) || !"".equals(des) || !"".equals(price)
				|| !"".equals(num) || imageIds.size() > 0) {
			isSave=true;
			new AlertDialog.Builder(this)
					.setTitle("当前内容未保存，\n是否保存后再退出")
					.setPositiveButton("保存",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									edit.putString("name", name);
									edit.putString("des", des);
									edit.putString("price", price);
									edit.putString("num", num);
									switch (imageIds.size()) {
									case 5:
										edit.putInt("ids5", imageIds.get(4));
										edit.putString("path5",
												bitmappaths.get(4));
									case 4:
										edit.putInt("ids4", imageIds.get(3));
										edit.putString("path4",
												bitmappaths.get(3));
									case 3:
										edit.putInt("ids3", imageIds.get(2));
										edit.putString("path3",
												bitmappaths.get(2));
									case 2:
										edit.putInt("ids2", imageIds.get(1));
										edit.putString("path2",
												bitmappaths.get(1));
									case 1:
										edit.putInt("ids1", imageIds.get(0));
										edit.putString("path1",
												bitmappaths.get(0));
										break;
									default:
										break;
									}
									edit.commit();
									isSave=false;
									finish();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									edit.clear();
									edit.commit();
									isSave=false;
									finish();
								}
							}).create().show();
		}else{
			isSave=false;
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			btnFinish();
			if(isSave){
				return false;
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
