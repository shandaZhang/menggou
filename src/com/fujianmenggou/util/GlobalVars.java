package com.fujianmenggou.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.fujianmenggou.bean.CreateNextLevel;

/**
 * 全局变量
 * @author Administrator
 *
 */
public class GlobalVars {
	public final static String baseUrl= "http://103.27.7.116:83";
	public final static String url= baseUrl+"/json/json.aspx";
	public final static String upUrl= baseUrl+"/web/RBRecharge.aspx?userID=";//升级费率url
	public final static String versionUrl= baseUrl+"/json/json.aspx?op=Version&Version_Type=1" ;
	public final static String appDownloadUrl= baseUrl+"/app/" ;
	
	public final static String agree_url= baseUrl+"/json/json.aspx?op=Agreement";
	
	public static CreateNextLevel nextLevel=new CreateNextLevel();

	public static final int cradnums = 1;
	// 填写从短信SDK应用后台注册得到的APPKEY
	public final static String APPKEY = "313f9e0c4e8f";
	// 填写从短信SDK应用后台注册得到的APPSECRET
	public final static String APPSECRET = "d2f58b041611f1f2560cb189353e9cbd";
	
	/** 版本更新 **/
	public final static int VERSION_UPDATE = 100;
	
	public static String getUid(Context context) {
		SharedPreferences share = context.getSharedPreferences("GLOBALVARS", Context.MODE_PRIVATE);
		return share.getString("uid", null);
	}

	public static String getNickname(Context context) {
		SharedPreferences share = context.getSharedPreferences("GLOBALVARS", Context.MODE_PRIVATE);
		return share.getString("nick_name", null);
	}

	public static int getGid(Context context) {
		SharedPreferences share = context.getSharedPreferences("GLOBALVARS", Context.MODE_PRIVATE);
		return share.getInt("gid", 0);
	}

	public static String getBalance(Context context) {
		SharedPreferences share = context.getSharedPreferences("GLOBALVARS", Context.MODE_PRIVATE);
		return share.getString("balance", null);
	}

	public static String getCard_number(Context context) {
		SharedPreferences share = context.getSharedPreferences("GLOBALVARS", Context.MODE_PRIVATE);
		return share.getString("card_number", null);
	}

	public static String getLowerCount(Context context) {
		SharedPreferences share = context.getSharedPreferences("GLOBALVARS", Context.MODE_PRIVATE);
		return share.getString("lowerCount", null);
	}

	public static String getTodayProfit(Context context) {
		SharedPreferences share = context.getSharedPreferences("GLOBALVARS", Context.MODE_PRIVATE);
		return share.getString("todayProfit", null);
	}

	public static String getBank_name(Context context) {
		SharedPreferences share = context.getSharedPreferences("GLOBALVARS", Context.MODE_PRIVATE);
		return share.getString("bank_name", null);
	}

	public static String getSafe(Context context) {
		SharedPreferences share = context.getSharedPreferences("GLOBALVARS", Context.MODE_PRIVATE);
		return share.getString("salt", null);
	}
	
}
