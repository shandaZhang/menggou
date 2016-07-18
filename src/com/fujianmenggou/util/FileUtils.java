package com.fujianmenggou.util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

public class FileUtils {
	public static final String TAG = "FileUtils";

	/** apk存放目录 */
	private static final String APKDOWNLOAD = getRootFilePath() + "/Download";
	
	/**
	 * 获取系统根目录
	 * @return
	 */
	public static String getRootFilePath() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			return Environment.getDataDirectory().getAbsolutePath();
		}
	}

	
	public static boolean isCreateAPKFileSuccess = false;//用于判断文件是否创建成功
	public static File apkFile;//apk绝对路径，用于apk的安装
	/**
	 * 用于存放下载下来的最新apk文件，存放路径固定为sdcard下的Download目录下
	 * @param fileName 完整文件名
	 */
	public static void createAPKFile(String fileName) {
		//获取download文件对象
		File apkDir = new File(APKDOWNLOAD);
		if (!apkDir.exists()) {//万一不存在，自己创建
			apkDir.mkdirs();
		}
		apkFile = new File(apkDir + "/" + fileName);
		if (apkFile.exists()) {
			try {
				apkFile.createNewFile();
			} catch (IOException e) {
				isCreateAPKFileSuccess = false;
				e.printStackTrace();
				return;
			}
		}
		isCreateAPKFileSuccess = true;
	}
}
