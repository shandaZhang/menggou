package com.fujianmenggou.gethttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import dujc.dtools.FinalHttp;
import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;

public class GetHttpInfo {

	/**
	 * 自动更新
	 */
	public static void getVersionAndUpdate(final Context context,
			final Handler handle) {
		AjaxParams params = new AjaxParams();
		params.put("op", "Version");
		params.put("Version_Type", "1");
		new FinalHttp().get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(context);
			}

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				Tools.DismissLoadingActivity(context);
				LogUtils.i(t);
				try {
					JSONObject jsonObject = new JSONObject(t);
					if (jsonObject.getInt("result") == 1) {
						JSONArray array = jsonObject.getJSONArray("list");
						if (array.length() > 0) {
							String version_number = array.getJSONObject(0)
									.getString("version_number");
							Message msg = handle.obtainMessage(
									GlobalVars.VERSION_UPDATE, version_number);
							msg.sendToTarget();
						}
					} else
						Tools.showTextToast(context, "获取失败 ");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
