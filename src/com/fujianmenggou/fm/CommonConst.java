package com.fujianmenggou.fm;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;
import android.view.KeyEvent;

public class CommonConst {
	public static final int TIME_OUT_DELAY = 30*1000;				//延迟时间
	public static final int TIME_OUT_SEARCHCARD = 3;				//寻卡超时时间
	public static final int NCCARD_TIMEOUT = 10;

	public static final String DEVICE_SERVICE_VERSION = "1.0.0";	//设备服务接口版本号
	public final static String TERMINAL_TYPE = "IM81";
	public final static String EQUIPMENT_MANUFACTURERS = "NEWLAND";
	public final static String PLATFORM = "ANDROID";
	
	public final static int TMS_ERRORCODE = -1;
	
	public final static String PACKAGENAME = "com.newland.deviceservice";//设备服务包名
	public final static String SN = "YP660000000001";
	public final static String HardWireVersion = "P3183";
    public static final class UPDATE_FILE{
		public static final String FID = "NEWLAND";
		public static final String MID = "NLIM81";
	}
    /**
     * 密码键盘相关
     */
    public static final int OPEN_KEYBOARD = 0x01;						//打开密码键盘Handler值
    public static final int OPEN_OFFLINE_KEYBOARD = 0x06;						//打开脱机密码键盘Handler值
    public static final String DEVICESERVICE_PININPUTDIALOG_ACTION = "deviceservice_pininputdialog_action";//DeviceService和PinInputDialog通知action
    public static final String DEVICESERVICE_PININPUTOFFLINEDIALOG_ACTION = "deviceservice_pininputofflinedialog_action";//DeviceService和PinInputOfflineDialog通知action
    public static class PinInputConst{
    	public final static int PININPUT_INPUTKEY = 0x02; //正在输入
    	public final static int PININPUT_SUCCESS = 0x03; //完成
    	public final static int PININPUT_CANCEL = 0x04; //取消
    	public final static int PININPUT_ERROR = 0x05; //错误
    }
	
	/**
	 * #define K_ZERO		0x30    //"0"
	 * #define K_ONE		0x31    //"1"
	 * #define K_TWO		0x32    //"2"
	 * #define K_THREE		0x33    //"3"
	 * #define K_FOUR		0x34    //"4"
	 * #define K_FIVE		0x35    //"5"
	 * #define K_SIX		0x36    //"6"
	 * #define K_SEVEN		0x37    //"7"
	 * #define K_EIGHT		0x38    //"8"
	 * #define K_NINE		0x39    //"9"
	 * #define K_ZMK		0x1C	//"#"
	 * #define K_DOT		0x2E	//"*"
	 * #define  K_BASP		0x0a    退格键
	 * #define  K_ENTER		0x0D	取消键
	 * #define  K_ESC		0x1B	确认键
	 */
	public static final Map<Integer,Integer> K21_KEYBOARD_KEYMAPPINGS = new HashMap<Integer,Integer>();
	static{
		K21_KEYBOARD_KEYMAPPINGS.put(0x30, KeyEvent.KEYCODE_0);
		K21_KEYBOARD_KEYMAPPINGS.put(0x31, KeyEvent.KEYCODE_1);
		K21_KEYBOARD_KEYMAPPINGS.put(0x32, KeyEvent.KEYCODE_2);
		K21_KEYBOARD_KEYMAPPINGS.put(0x33, KeyEvent.KEYCODE_3);
		K21_KEYBOARD_KEYMAPPINGS.put(0x34, KeyEvent.KEYCODE_4);
		K21_KEYBOARD_KEYMAPPINGS.put(0x35, KeyEvent.KEYCODE_5);
		K21_KEYBOARD_KEYMAPPINGS.put(0x36, KeyEvent.KEYCODE_6);
		K21_KEYBOARD_KEYMAPPINGS.put(0x37, KeyEvent.KEYCODE_7);
		K21_KEYBOARD_KEYMAPPINGS.put(0x38, KeyEvent.KEYCODE_8);
		K21_KEYBOARD_KEYMAPPINGS.put(0x39, KeyEvent.KEYCODE_9);
		K21_KEYBOARD_KEYMAPPINGS.put(0x1C, KeyEvent.KEYCODE_POUND);
		K21_KEYBOARD_KEYMAPPINGS.put(0x2E, KeyEvent.KEYCODE_STAR);
		K21_KEYBOARD_KEYMAPPINGS.put(0x0a, KeyEvent.KEYCODE_DEL);
		K21_KEYBOARD_KEYMAPPINGS.put(0x0d, KeyEvent.KEYCODE_ENTER);
		K21_KEYBOARD_KEYMAPPINGS.put(0x1b, KeyEvent.KEYCODE_ESCAPE);
	}
    public static final Map<Integer,Character> K21_KEYBOARD_KEYMAPPINGS2 = new HashMap<Integer,Character>();
    static{
        K21_KEYBOARD_KEYMAPPINGS2.put(0x30, '0');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x31, '1');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x32, '2');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x33, '3');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x34, '4');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x35, '5');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x36, '6');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x37, '7');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x38, '8');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x39, '9');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x1C, '#');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x2E, '*');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x0a, '-');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x0d, '-');
        K21_KEYBOARD_KEYMAPPINGS2.put(0x1b, '-');
    }
}
