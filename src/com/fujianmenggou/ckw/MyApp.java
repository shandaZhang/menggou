package com.fujianmenggou.ckw;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApp extends Application {
	private static MyApp apl;
	/** activity列表，暂时貌似没什么卵用 */
	private ArrayList<Activity> activities;
	
	public static MyApp getInstance() {
		return apl;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		apl = this;
		activities = new ArrayList<Activity>();
		initImageLoader(this);
	}
	public void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)//线程个数
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	/**
	 * 添加一个页面
	 * 
	 * @param ac
	 */
	public void addtoStack(Activity ac) {
		activities.add(ac);
	}

	/**
	 * 移除某个页面
	 * 
	 * @param ac
	 */
	public void removefromStack(Activity ac) {
		if (activities.contains(ac))
			activities.remove(ac);
	}

	/**
	 * 退出
	 */
	public void exit() {
		// clear();
		for (int i = (activities.size() - 1); i >= 0; i--) {
			activities.get(i).finish();
		}
		System.exit(0);
	}
}
