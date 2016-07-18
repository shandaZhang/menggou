package com.lib.chen.uil;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.fujianmenggou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public final class UILUtils {
	private UILUtils() {
	}
	private static UILUtils uilUtils=new UILUtils();
	public static UILUtils getUilUtils(){
		return uilUtils;
	}
	
	private int cornerRadiusPixels=0;
	private  DisplayImageOptions options;

	public void displayImage(Context context, String imgurl,
			ImageView imageView) {
		initOptions();
		ImageLoader.getInstance()
				.displayImage(imgurl, imageView, options, null);
		if(cornerRadiusPixels!=0){
			cornerRadiusPixels=0;
			options=null;
//			Log.e("2222", "cornerRadiusPixels:"+cornerRadiusPixels);
		}
	}

	private  void initOptions() {
		if(cornerRadiusPixels!=0){
//			Log.e("0000", "cornerRadiusPixels:"+cornerRadiusPixels);
			options=null;
		}
		if (options == null) {
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.default_ptr_rotate)
					.showImageForEmptyUri(R.drawable.new2)
					.showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true)//在内存中缓存
					.cacheOnDisk(true)//在外存中缓存
					.considerExifParams(true)//考虑Exif参数
					//设置圆角的
					.displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)).build();
//			Log.e("1111", "cornerRadiusPixels:"+cornerRadiusPixels);
		}
//		Log.e("3333", "cornerRadiusPixels:"+cornerRadiusPixels);
	}
	public void setRounded(int corner){
		this.cornerRadiusPixels=corner;
	}
}
