package com.fujianmenggou.fm;

interface GetPinListener{
	/** 按键事件 */
	void onInputKey(int len,String msg);
	
	/** 发生错误 */
	void onError(int errorCode);
	
	/** 空密码返回NULL,确认 isemptypin表示空密码 */
	void onConfirmInput(in byte[] pin);
	
	/** 取消键 */
	void onCancelKeyPress();
	
	/** 停止获取PIN */
	void onStopGetPin(); 
}