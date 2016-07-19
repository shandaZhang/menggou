package com.fujianmenggou.ckw;

import com.fujianmenggou.R;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class TcDialog extends Dialog {
	private Context context;
	/**
	 * 关闭按钮
	 */
	private ImageView close;
	private TextView tc_info_show;
	private View mView;

	public TcDialog(Context context) {
		super(context, R.style.Dialog_Fullscreen);
		this.context =context;
		setTitle(null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setBackgroundDrawableResource(android.R.color.white);
		setCancelable(false);
		setCanceledOnTouchOutside(false);
		setTcDialog();
	}

	private void setTcDialog() {
		mView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_tc, null);
		super.setContentView(mView);
		
		close = (ImageView) mView.findViewById(R.id.im_close);
		tc_info_show = (TextView) mView.findViewById(R.id.tc_info_show);
		close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		setAttributesX(0.7f);
		setAttributesY(0.8f);
	}
	
	public void setAttributesY(float sy) {
		DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); 
		p.height = (int) (dm2.heightPixels * sy);
		dialogWindow.setAttributes(p);
	}
	
	public void setAttributesX(float sx) {
		DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); 
		p.width = (int) (dm2.widthPixels * sx);
		dialogWindow.setAttributes(p);
	}
	
	
	public void showTcInfo(String params){
		tc_info_show.setText(Html.fromHtml(params));
		show();
	}
	
	 @Override
	public void setContentView(int layoutResID) {
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
	}

	@Override
	public void setContentView(View view) {
	}
}
