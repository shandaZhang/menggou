package com.lib.chen.uil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public final class ConnectedUtils {
	/**
	 * ��������Ƿ����
	 * @param context 
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager)
			context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * ��ȡ��ǰ��������
	 * 
	 * @return 0��û������ 1��WIFI���� 2��WAP���� 3��NET����
	 */

	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;
	private static final int NETTYPE_2G = 0x0001;
	private static final int NETTYPE_3G = 0x0010;
	private static final int NETTYPE_4G = 0x0100;
	private static final int NETTYPE_UNKOWN = 0x1000;
	private static final int NETTYPE_NO = 0x10000;
	
	/**
     * �ж�sim������Ϊ��������
     */
    public static int getNetGeneration(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            switch (telephonyManager.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return NETTYPE_2G;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return NETTYPE_3G;
                case TelephonyManager.NETWORK_TYPE_LTE:

                    return NETTYPE_4G;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return NETTYPE_UNKOWN;
                default:
                    return NETTYPE_UNKOWN;
            }
        }
        else {
            return NETTYPE_NO;
        }
    }
	
	public static String getNetworkTypeName(Context context){
		int type = getNetworkType(context);
		switch (type) {
		case NETTYPE_WIFI:
			return "WIFI";
		case NETTYPE_CMWAP:
			return "WAP";
		case NETTYPE_CMNET:
			String typeName=null;
			int netGeneration = getNetGeneration(context);
            switch (netGeneration){
                case NETTYPE_2G:
                    typeName = "2G";
                    break;
                case NETTYPE_3G:
                    typeName = "3G";
                    break;
                case NETTYPE_4G:
                    typeName = "4G";
                    break;
                case NETTYPE_UNKOWN:
                    typeName = "unknown";
                    break;
                case NETTYPE_NO:
                    typeName = "none";
                    break;
            }
            return typeName;
		default:
			break;
		}
		return null;
	}
	
	public static int getNetworkType(Context context) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!TextUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
	private ConnectedUtils(){}
}
