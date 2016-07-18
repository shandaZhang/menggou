package com.fujianmenggou.fm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fujianmenggou.R;

import dujc.dtools.ViewUtils;

/**
 * 数字键盘输入器
 * 
 * @author CB
 * @time 2015-4-20 上午9:10:06
 */
public class KeyboardNumber extends FrameLayout {
	public static final int K_0 = 0x30;
	public static final int K_1 = 0x31;
	public static final int K_2 = 0x32;
	public static final int K_3 = 0x33;
	public static final int K_4 = 0x34;
	public static final int K_5 = 0x35;
	public static final int K_6 = 0x36;
	public static final int K_7 = 0x37;
	public static final int K_8 = 0x38;
	public static final int K_9 = 0x39;
	
	/**
	 * ‘*’号键
	 */
	public static final int K_STAR = 0x2A;
	
	/**
	 * '#'号键
	 */
	public static final int K_WELL = 0x23;
	/**
	 * ‘取消’键
	 */
	public static final int K_CANCEL = 0x14;
	/**
	 * ‘清除’键
	 */
	public static final int K_CLEAN = 0x00;
	
	/**
	 * ‘回退’键
	 */
	public static final int K_BACKSPACE = 0x08;

	/**
	 * ‘回车’键
	 */
	public static final int K_ENTER = 0x0D;
	/**
	 * ‘点’键
	 */
	public static final int K_DOT = 0x2E;
	
	
	/** 确认按钮 */
	ImageView ivEnter;
	/** 取消 */
	ImageView ivCancel;
	/** 退格 */
	ImageView ivBackspeace;
	/** 0 */
	ImageView iv0;
	/** 1 */
	ImageView iv1;
	/** 2 */
	ImageView iv2;
	/** 3 */
	ImageView iv3;
	/** 4 */
	ImageView iv4;
	/** 5 */
	ImageView iv5;
	/** 6 */
	ImageView iv6;
	/** 7 */
	ImageView iv7;
	/** 8 */
	ImageView iv8;
	/** 9 */
	ImageView iv9;
	/** * */
	ImageView ivStar;
	/** # */
	ImageView ivWell;
	/** . */
	ImageView ivDot;

	private Context context;
	private boolean isEnterGone;
	private View[] buttons;
	private ImageView[] numButtons;

	/** 临时View */
	private View view;;
	
	private IKeyBoardListener keyBoardListener;

	public KeyboardNumber(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public KeyboardNumber(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public KeyboardNumber(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		initData();
		initEvent();
	}

	private void initView() {
		/** 确认按钮 */
		ivEnter = (ImageView) findViewById(R.id.iv_enter);
		/** 取消 */
		ivCancel = (ImageView) findViewById(R.id.iv_cancel);
		/** 退格 */
		ivBackspeace = (ImageView) findViewById(R.id.iv_backspeace);
		/** 0 */
		iv0 = (ImageView) findViewById(R.id.iv_0);
		/** 1 */
		iv1 = (ImageView) findViewById(R.id.iv_1);
		/** 2 */
		iv2 = (ImageView) findViewById(R.id.iv_2);
		/** 3 */
		iv3 = (ImageView) findViewById(R.id.iv_3);
		/** 4 */
		iv4 = (ImageView) findViewById(R.id.iv_4);
		/** 5 */
		iv5 = (ImageView) findViewById(R.id.iv_5);
		/** 6 */
		iv6 = (ImageView) findViewById(R.id.iv_6);
		/** 7 */
		iv7 = (ImageView) findViewById(R.id.iv_7);
		/** 8 */
		iv8 = (ImageView) findViewById(R.id.iv_8);
		/** 9 */
		iv9 = (ImageView) findViewById(R.id.iv_9);
		/** * */
		ivStar = (ImageView) findViewById(R.id.iv_star);
		/** # */
		ivWell = (ImageView) findViewById(R.id.iv_well);
		/** . */
		ivDot = (ImageView) findViewById(R.id.iv_dot);
	}
	
	private void initData() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.keyboard_number, this, true);
		ViewUtils.inject(this, view);
		initView();
		buttons = new View[] {iv0, iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, 
				 ivStar, ivWell, ivEnter, ivCancel, ivDot, ivBackspeace};
		numButtons = new ImageView[] {iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9,iv0};
		
		//默认隐藏*#
		setTelephoneKeyDisplay(true);
		//默认按下回车按钮隐藏键盘
		isEnterGone = true;
		
		
	}

	private void initEvent() {
		ivBackspeace.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (keyBoardListener != null) {
					keyBoardListener.onClear();
				}
				return false;
			}
		});
	}
	
	/**
	 * 设置键盘监听器
	 */
	public void setKeyBoardListener(IKeyBoardListener iKeyBoardListener) {
		this.keyBoardListener = iKeyBoardListener;
		if (keyBoardListener != null) {
			for (int i = 0; i < buttons.length; i++) {
				
				view = buttons[i];
				
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.iv_cancel:
							keyBoardListener.onCancel();
							break;
						case R.id.iv_backspeace:
							keyBoardListener.onBackspeace();
							break;
						case R.id.iv_enter:
							if (isEnterGone) {
								setVisibility(View.GONE);
							}
							keyBoardListener.onEnter();
							break;

						default:
							keyBoardListener.onClick(getCodeByView(v));
							break;
						}
					}
				});
				
			}
			
		}
	}
	
	private int getCodeByView(View v) {
		
		int code = -1;
		
		switch (v.getId()) {
		case R.id.iv_1:
			code = K_1;
			break;
		case R.id.iv_2:
			code = K_2;
			break;
		case R.id.iv_3:
			code = K_3;
			break;
		case R.id.iv_4:
			code = K_4;
			break;
		case R.id.iv_5:
			code = K_5;
			break;
		case R.id.iv_6:
			code = K_6;
			break;
		case R.id.iv_7:
			code = K_7;
			break;
		case R.id.iv_8:
			code = K_8;
			break;
		case R.id.iv_9:
			code = K_9;
			break;
		case R.id.iv_0:
			code = K_0;
			break;
		case R.id.iv_star:
			code = K_STAR;
			break;
		case R.id.iv_well:
			code = K_WELL;
			break;
		case R.id.iv_dot:
			code = K_DOT;
			break;
		}
		return code;
	}
	
	

	public IKeyBoardListener getKeyBoardListener() {
		return keyBoardListener;
	}
	
	/**
	 * 按下回车是否隐藏键盘 
	 */
	public void setEnterNotGone() {
		isEnterGone = false;
	}
	
	/**
	 * 设置随机数字键盘
	 */
	public void setRandomNumber() {
		int[] nunKeyboards = getRandomNumber();
		
		for (int i = 0; i < nunKeyboards.length; i++) {

			switch (nunKeyboards[i]) {
			case 1:
				numButtons[i].setId(R.id.iv_1);
				numButtons[i].setImageResource(R.drawable.keyboard_1);
				break;
			case 2:
				numButtons[i].setId(R.id.iv_2);
				numButtons[i].setImageResource(R.drawable.keyboard_2);
				break;
			case 3:
				numButtons[i].setId(R.id.iv_3);
				numButtons[i].setImageResource(R.drawable.keyboard_3);
				break;
			case 4:
				numButtons[i].setId(R.id.iv_4);
				numButtons[i].setImageResource(R.drawable.keyboard_4);
				break;
			case 5:
				numButtons[i].setId(R.id.iv_5);
				numButtons[i].setImageResource(R.drawable.keyboard_5);
				break;
			case 6:
				numButtons[i].setId(R.id.iv_6);
				numButtons[i].setImageResource(R.drawable.keyboard_6);
				break;
			case 7:
				numButtons[i].setId(R.id.iv_7);
				numButtons[i].setImageResource(R.drawable.keyboard_7);
				break;
			case 8:
				numButtons[i].setId(R.id.iv_8);
				numButtons[i].setImageResource(R.drawable.keyboard_8);
				break;
			case 9:
				numButtons[i].setId(R.id.iv_9);
				numButtons[i].setImageResource(R.drawable.keyboard_9);
				break;
			case 0:
				numButtons[i].setId(R.id.iv_0);
				numButtons[i].setImageResource(R.drawable.keyboard_0);
				break;
			}
		}
	
		
	}
	
	/**
	 * 获取随机数组
	 * @return
	 */
	private int[] getRandomNumber() {
		List<Integer> numbers = new ArrayList<Integer>();
		int[] buttons = new int[10];
		for(int i=0; i<10; i++) {
			numbers.add(i);
		}
		int random;
		for(int i=0; i<10; i++) {
			random = new Random().nextInt(numbers.size());
			buttons[i] = numbers.get(random);
			numbers.remove(random);
		}
		
		return buttons;
	}
	
	/**
	 * 设置IP键盘样式
	 */
	public void setIpStyle() {
		ivWell.setVisibility(View.GONE);
		ivDot.setVisibility(View.VISIBLE);
		ivStar.setEnabled(false);
		ivStar.setImageResource(R.drawable.keyboard_null_left);
	}

	/**
	 * 显示隐藏*#按钮
	 * @param isDisplay ture-隐藏，false-显示
	 */
	public void setTelephoneKeyDisplay(boolean isDisplay) {
		
		if (isDisplay) {
			
			ivStar.setEnabled(false);
			ivWell.setEnabled(false);
			ivStar.setImageResource(R.drawable.keyboard_null_left);
			ivWell.setImageResource(R.drawable.keyboard_null_right);
			
		} else {

			ivStar.setEnabled(true);
			ivWell.setEnabled(true);
			ivStar.setImageResource(R.drawable.keyboard_star);
			ivWell.setImageResource(R.drawable.keyboard_well);
		}

		ivWell.setVisibility(View.VISIBLE);
		ivDot.setVisibility(View.GONE);
	}
}
