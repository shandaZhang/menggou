package com.fujianmenggou.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import com.fujianmenggou.atv.LoadingActivity;

public class Tools {
	public int id = 1;
	private static Tools tools = null;
	private static Toast toast = null;

	private static long lastShowTime = 0 ;

	public static Tools getInstance() {
		if (tools == null) {
			tools = new Tools();
		}
		return tools;
	}

	/**
	 * 将jpg图片转为二进制流，再转换为String返回<br>此函数用到了<b>Base64Coder</b>，只为了与后台相结合
	 *，在别处不一定适用
	 * @param jpgPath
	 * @return strFile
	 */
	public static String getStringFromJpeg(String jpgPath){
		Bitmap photo =BitmapFactory.decodeFile(jpgPath);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return new String(Base64Coder.encodeLines(byteArray));
	}

	/** 从相册获取图片 获取成功后resultcode=-1**/
	public static Intent doPickPhotoFromGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK
				, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		//intent.putExtra("crop", "true"); // 滑动选中图片区域
		//intent.putExtra("aspectX", 4); // 裁剪框比例1:1
		//intent.putExtra("aspectY", 3);
		//intent.putExtra("outputX", 96); // 输出图片大小
		//intent.putExtra("outputY", 72);
		//intent.putExtra("return-data", true); // 有返回值
		return intent;
	}

//	/** 拍照获取相片 获取成功后resultcode=-1**/
//	public Intent doTakePhoto(Context context) {
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机
//		Uri imageUri = Uri.fromFile(new File(Constants.PHOTO_PATH, AppConfig
//				.getAccountPhone(context) + "_head_img.jpg"));
//		// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//		intent.putExtra("crop", "true"); // 滑动选中图片区域
//		intent.putExtra("aspectX", 1); // 裁剪框比例1:1
//		intent.putExtra("aspectY", 1);
//		intent.putExtra("outputX", 94); // 输出图片大小
//		intent.putExtra("outputY", 94);
//		intent.putExtra("return-data", true); // 有返回值
//		return intent;
//
//	}

	/**
	 * 调用图库时，将onActivityResult返回的data转换成Bitmap
	 * @param context
	 * @param data
	 * @return 获取到的图片
	 */
	public static Bitmap IntentToBitmap(Context context,Intent data){

		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = context.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();

		return ScalePicture(picturePath);
	}
	/**
	 * 调用图库时，将onActivityResult返回的data转换成图片路径
	 * @param context
	 * @param data
	 * @return
	 */
	public static String IntentTopath(Context context,Intent data){
		
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		
		Cursor cursor = context.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();
		
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		
		return picturePath;
	}
	
	/**
	 * 调用系统相机,将onActivityResult返回的data转换成Bitmap
	 * @param context
	 * @param data
	 * @return
	 */
	public static Bitmap IntentToBitmapCamer(Context context,Intent data){
		 Uri uri = data.getData();
			Bitmap photo = null;
			if (uri != null) {
//				photo = BitmapFactory.decodeFile(uri.getPath());
				photo = ScalePicture(uri.getPath());
			}
			if (photo == null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					photo = (Bitmap) bundle.get("data");
				} else {
					Toast.makeText(context,
							"照片出错",
							Toast.LENGTH_LONG).show();
					return null;
				}
			}
			return photo;
	}
	
	/** 缩放Bitmap图片 **/
	public static Bitmap zoomBitmap(Bitmap bitmap, float width, float height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	public static Bitmap ScalePicture(String filename) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filename, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			int desWidth = 0;
			int desHeight = 0;
			// 缩放比例
			double ratio = 0.0;
			if (srcWidth > srcHeight) {
				ratio = srcWidth / 200;
				desWidth = 200;
				desHeight = (int) (srcHeight / ratio);
			} else {
				ratio = srcHeight / 180;
				desHeight = 180;
				desWidth = (int) (srcWidth / ratio);
			}
			// 设置输出宽度、高度
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inSampleSize = (int) (ratio + 0.9);
			newOpts.inJustDecodeBounds = false;
			newOpts.outWidth = desWidth;
			newOpts.outHeight = desHeight;
			FileInputStream fis = null;
			BufferedInputStream bos = null;
			fis = new FileInputStream(filename);
			bos = new BufferedInputStream(fis);
			bitmap = BitmapFactory.decodeStream(bos, null, newOpts);
			// bitmap = BitmapFactory.decodeFile(filename, newOpts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

//	/**
//	 * 获取时间
//	 * @param type year/month/day/hour/minute/second/date
//	 * @return
//	 */
//	public static String getTime(String type){
//		String time = null;
//		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
//		t.setToNow(); // 取得系统时间。
//		switch (type) {
//			case "year":
//				time=""+t.year;
//				break;
//
//			case "month":
//				time=""+(t.month+1);
//				break;
//
//			case "day":
//				time=""+t.monthDay;
//				break;
//
//			case "hour":
//				time=""+t.hour;
//				break;
//
//			case "minute":
//				time=""+t.minute;
//				break;
//
//			case "second":
//				time=""+t.second;
//				break;
//
//			case "date":
//				time=t.year+"-"+(t.month+1)+"-"+t.monthDay;
//				break;
//
//			default:
//				time=t.year+"-"+(t.month+1)+"-"+t.monthDay+" "+t.hour+":"+t.minute+":"+t.second;
//				break;
//		}
//		return time;
//	}


	/**
	 * 设置下划线
	 *
	 * @param view
	 */
	public void setTxtThru(TextView view) {
		view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		view.getPaint().setAntiAlias(true);
	}

	public static void showTextToast(Context context, String str) {
		if (toast == null) {
			toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
		} else {
			toast.setText(str);
		}
		toast.show();
	}

	/**
	 * 判断网络连接是否可用
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			// 当前网络不可用
			Tools.showTextToast(context.getApplicationContext(), "网络异常，请先连接网络！");
			return false;
		}

		return true;
	}

	/**
	 * 获取SD卡路径
	 * @return SD卡路径，在后面加上斜杠，在加上文件名
	 */
	public static String getSDPath(){
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 
		if (sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();//获取跟目录
		}
		return sdDir.toString();
	}


	/**
	 * 显示进度加载
	 * @param mContext
	 */
	public static void ShowLoadingActivity(Context mContext)
	{
		lastShowTime=System.currentTimeMillis();
		Intent intent = new Intent();
		intent.setClass(mContext,LoadingActivity.class);//跳转到加载界面
		mContext.startActivity(intent);
	}

	/**
	 * 隐藏进度加载
	 * @param mContext
	 */
	public static void DismissLoadingActivity(final Context mContext) {
		Intent mIntent = new Intent(LoadingActivity.ACTION_NAME);
		//发送广播
		mContext.sendBroadcast(mIntent);
	}
}
