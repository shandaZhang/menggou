package com.lib.chen.uil;

import android.content.Context;
import android.view.WindowManager;

public final class ScreenUtils {

	public static int getScreenWidth(Context context) {
		WindowManager windowMgr = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return windowMgr.getDefaultDisplay().getWidth();
	}
	
	public static int getScreenHeight(Context context) {
		WindowManager windowMgr = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return windowMgr.getDefaultDisplay().getHeight();
	}

	private ScreenUtils() {
	}
}
