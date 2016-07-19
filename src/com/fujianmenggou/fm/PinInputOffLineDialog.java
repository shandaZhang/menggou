package com.fujianmenggou.fm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;

import com.fujianmenggou.R;

public class PinInputOffLineDialog extends Activity {
	private static final String TAG = "PinInputOffLineDialog";
	private static PinInputOffLineDialog pinInputOffLine;
	/** 键盘 */
	private KeyboardNumber keyBoardNumber;
	private EditText et_money;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CommonConst.PinInputConst.PININPUT_CANCEL: //取消
				finish();
				overridePendingTransition(R.anim.translate_bottom_in, R.anim.translate_bottom_out);
				break;
			case CommonConst.PinInputConst.PININPUT_SUCCESS: //完成
				String money = (String) msg.obj;
				Intent intent = new Intent();
				intent.putExtra("money", money);
				setResult(1, intent);
				finish();
				overridePendingTransition(R.anim.translate_bottom_in, R.anim.translate_bottom_out);
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_pin_offline_fragment);
		overridePendingTransition(R.anim.translate_bottom_in, R.anim.translate_bottom_out);
		pinInputOffLine = this;
		init();
	}
	
	private void init() {
		keyBoardNumber = (KeyboardNumber) findViewById(R.id.key_board_number);
		et_money = (EditText) findViewById(R.id.et_how_much);
		initData();
	}
	
	private void initData(){
		/**
		 * 设置按下确认键不隐藏键盘
		 */
		keyBoardNumber.setEnterNotGone();
		
		AbstractKeyBoardListener listener = new AbstractKeyBoardListener(){
			@Override
			public void onChangeText(String text) {
				super.onChangeText(text);
				et_money.setText(text);
			}
			@Override
			public void onEnter() {
				int len = getValue().trim().length();
				if (len > 0) {					
					// 输入成功
					String money = getValue();
					Message msg = mHandler.obtainMessage(CommonConst.PinInputConst.PININPUT_SUCCESS);
					msg.obj = money;
					msg.sendToTarget();
				}else {
					mHandler.sendEmptyMessage(CommonConst.PinInputConst.PININPUT_CANCEL);
				}
			}
			/**
			 * 重写取消事件
			 */
			@Override
			public void onCancel() {
				Log.i(TAG, "Is UserCanceled");
				mHandler.sendEmptyMessage(CommonConst.PinInputConst.PININPUT_CANCEL);
			}
		};

		keyBoardNumber.setKeyBoardListener(listener);
	}
	
	public static PinInputOffLineDialog getInstance(){
		return pinInputOffLine;
	}

}
