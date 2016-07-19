package com.fujianmenggou.service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.fujianmenggou.R;
import com.fujianmenggou.ckw.MyApp;
import com.fujianmenggou.util.FileUtils;

/**
 * 升级服务
 * 
 * @author wangjun
 *
 */
public class APPUpdateService extends Service {
	/** 通知的Id，不同的Id代表不同的通知 */
	private static final int notificationId = 0;
//	public static final String Install_Apk = "Install_Apk";
	/******** download progress step *********/
	private static final int down_step_custom = 3;
	/** 超时时间 */
	private static final int TIMEOUT = 10 * 1000;// 超时
	/** apk下载地址 */
	private static String down_url;
	/** 下载成功 */
	private static final int DOWN_OK = 1;
	/** 下载失败 */
	private static final int DOWN_ERROR = 0;
	/** apk名称 */
	private String app_name;

	private NotificationManager notificationManager;
	private Notification notification;
	private PendingIntent pendingIntent;
	private RemoteViews contentView;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * 方法描述：onStartCommand方法
	 * 
	 * @param Intent
	 *            intent, int flags, int startId
	 * @return int
	 * @see UpdateService
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		app_name = intent.getStringExtra("app_name");
		down_url = intent.getStringExtra("down_url");
		// create file,应该在这个地方加一个返回值的判断SD卡是否准备好，文件是否创建成功，等等！
		FileUtils.createAPKFile(app_name);
		if (FileUtils.isCreateAPKFileSuccess) {//成功			
			createNotification();
			createThread();
		}else {//失败
			stopSelf();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/********* update UI ******/
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_OK:
				/********* 下载完成，点击安装 ***********/
				Uri uri = Uri.fromFile(FileUtils.apkFile);//获取apk绝对路径
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(uri, "application/vnd.android.package-archive");
				pendingIntent = PendingIntent.getActivity(APPUpdateService.this,
						0, intent, 0);

				notification = new Notification.Builder(APPUpdateService.this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(app_name)
				.setContentText("下载成功")
				.setWhen(System.currentTimeMillis())
				.setAutoCancel(true)
				.setContentIntent(pendingIntent).getNotification();
				notificationManager.notify(notificationId, notification);

				/***** 安装APK ******/
				 installApk();

				/*** stop service *****/
				stopSelf();
				break;

			case DOWN_ERROR:
				notification = new Notification.Builder(APPUpdateService.this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(app_name)
				.setContentText("下载失败")
				.setWhen(System.currentTimeMillis())
				.setAutoCancel(true).getNotification();
				notificationManager.notify(notificationId, notification);

				/*** stop service *****/
				// onDestroy();
				stopSelf();
				break;

			default:
				// stopService(updateIntent);
				/****** Stop service ******/
				// stopService(intentname)
				// stopSelf();
				break;
			}
		}
	};

	private void installApk() {
		// TODO Auto-generated method stub
		/********* 下载完成，点击安装 ***********/
		Uri uri = Uri.fromFile(FileUtils.apkFile);
		Intent intent = new Intent(Intent.ACTION_VIEW);

		/********** 加这个属性是因为使用Context的startActivity方法的话，就需要开启一个新的task **********/
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		APPUpdateService.this.startActivity(intent);
//		MyApp.getInstance().exit();
	}

	/**
	 * 方法描述：createThread方法, 开线程下载
	 * 
	 * @param
	 * @return
	 * @see UpdateService
	 */
	public void createThread() {
		new DownLoadThread().start();
	}

	private class DownLoadThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message message = new Message();
			try {
				long downloadSize = downloadUpdateFile(down_url,
						FileUtils.apkFile.toString());
				if (downloadSize > 0) {
					// down success
					message.what = DOWN_OK;
					handler.sendMessage(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
				message.what = DOWN_ERROR;
				handler.sendMessage(message);
			}
		}
	}

	/**
	 * 方法描述：createNotification方法
	 * 
	 * @param
	 * @return
	 * @see UpdateService
	 */
	public void createNotification() {
		/*** 自定义 Notification 的显示 ****/
		contentView = new RemoteViews(getPackageName(), R.layout.apkupdate_notification_item);
		contentView.setTextViewText(R.id.notificationTitle, app_name + " 正在下载");
		contentView.setTextViewText(R.id.notificationPercent, "0%");
		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
		
		/** 初始状态 */
		notification = new Notification.Builder(APPUpdateService.this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContent(contentView).setOngoing(true).getNotification();
		
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, notification);
	}

	/***
	 * down file
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public long downloadUpdateFile(String down_url, String file)
			throws Exception {

		int down_step = down_step_custom;// 提示step
		int totalSize;// 文件总大小
		int downloadCount = 0;// 已经下载好的大小
		int updateCount = 0;// 已经上传的文件大小

		InputStream inputStream;
		OutputStream outputStream;

		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		// 获取下载文件的size
		totalSize = httpURLConnection.getContentLength();

		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
			// 这个地方应该加一个下载失败的处理，但是，因为我们在外面加了一个try---catch，已经处理了Exception,
			// 所以不用处理
		}

		inputStream = httpURLConnection.getInputStream();
		outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉

		byte buffer[] = new byte[1024];
		int readsize = 0;

		while ((readsize = inputStream.read(buffer)) != -1) {

			// /*********如果下载过程中出现错误，就弹出错误提示，并且把notificationManager取消*********/
			// if (httpURLConnection.getResponseCode() == 404) {
			// notificationManager.cancel(R.layout.notification_item);
			// throw new Exception("fail!");
			// //这个地方应该加一个下载失败的处理，但是，因为我们在外面加了一个try---catch，已经处理了Exception,
			// //所以不用处理
			// }

			outputStream.write(buffer, 0, readsize);
			downloadCount += readsize;// 时时获取下载到的大小
			/*** 每次增张3% **/
			if (updateCount == 0
					|| (downloadCount * 100 / totalSize - down_step) >= updateCount) {
				updateCount += down_step;
				// 改变通知栏
				contentView.setTextViewText(R.id.notificationPercent,
						updateCount + "%");
				contentView.setProgressBar(R.id.notificationProgress, 100,
						updateCount, false);
				notification.contentView = contentView;
				notificationManager.notify(notificationId, notification);
			}
		}
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		inputStream.close();
		outputStream.close();

		return downloadCount;
	}

}
