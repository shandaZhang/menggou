package com.fujianmenggou.fm;

import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class AbstractKeyBoardListener implements IKeyBoardListener {
	private final static String TAG = "AbstractKeyBoardListener";
	private String value = "";
	private int maxLength = 999;
	private View targetView;

	public AbstractKeyBoardListener() {
	}

	/**
	 * 按下时的事件
	 * 
	 * @param code
	 *            ascill码的值
	 * @return true-自己完成事件，不再执行默认事件 false-自己未完成事件，执行默认事件
	 */
	public void onClick(int code) {
		if (value.length() < maxLength) {
			
			if (targetView instanceof EditText && targetView.isFocused()) {
				int index = ((EditText)targetView).getSelectionStart();

				value = value.substring(0, index) + (char) code + value.substring(index);
				
			} else {
				value += (char) code;
			}
			
		}
		
		onKeyClick(code);
		onChangeText(value);
	}

	/**
	 * 退格事件
	 * 
	 * @return 自己完成事件，不再执行默认事件 false-自己未完成事件，执行默认事件
	 */
	public boolean onBackspeace() {

		int index = value.length();
		if (value.length() > 0) {
			if ( targetView instanceof EditText) {
				EditText editText = (EditText)targetView;
				Log.i(TAG,"isFocusable:"+editText.isFocusable()
						+",isFocused:"+editText.isFocused());
				if (!editText.isFocused()) {
					//无焦点输入框
					value = value.substring(0, value.length() - 1);
				} else {
					index = editText.getSelectionStart();
					String temp = index > 0 ? value.substring(0, index - 1) : "";
					temp += value.substring(index);
					value = temp;
				}
			} else {
				value = value.substring(0, index - 1);
			}
			
		} else {
			value = "";
		}
		
		if (targetView instanceof EditText) {
			Log.i(TAG,"value:" + ((EditText)targetView).getText().toString() + ",getSelectionStart"
					+ ((EditText) targetView).getSelectionStart()
					+ ",getSelectionEnd: "
					+ ((EditText) targetView).getSelectionEnd());
		}
		onKeyClick(KeyboardNumber.K_BACKSPACE);
		onChangeText(value);
		return false;
	}

	/**
	 * 清除事件
	 * 
	 */
	public void onClear() {
		value = "";
		onKeyClick(KeyboardNumber.K_CLEAN);
		onChangeText(value);
	}
	
	public String getValue() {
		return value;
	}
	/**
	 * 取消事件
	 * 
	 */
	public void onCancel() {
	}

	/**
	 * 设置键盘的值
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
		Log.i(TAG,"keyboard value:" + value);
	}

	/**
	 * 设置最大长度
	 * @param maxLength
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	
	/**
	 * 当前输入的所有文本
	 * 
	 * @param text
	 */
	public void onChangeText(String text){
		refreshEditText();
	}
	
	/**
	 * 当前输入的单个字符
	 * @param key
	 */
	public void onKeyClick(int key) {
		
	}

	/**
	 * 确认事件
	 */
	public void onEnter(){
		onKeyClick(KeyboardNumber.K_ENTER);
	}
	
	private void refreshEditText() {
		
		if (targetView != null && targetView instanceof EditText) {
			EditText editText = (EditText)targetView;
			int index = editText.getSelectionStart();
			int count = editText.getText().length();
			editText.setTextKeepState(value);
			Log.i(TAG,"refreshEditText -> value:"+value+",index:"+index+",count:"+count);
			if (value.equals("")) {
			} else if(value.length() < count) {
				editText.setSelection(index - 1);
			} else if(value.length() > count) {
				editText.setSelection(index + 1);
			} else {
				editText.setSelection(index);
			}
		}
	}

}
