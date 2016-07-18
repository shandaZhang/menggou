package com.fujianmenggou.atv;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class UserCenter extends BaseActivity {
	
	private TextView tv_text1,tv_text2,tv_text3,tv_text4,tv_text5,tv_text6,tv_text7;
	private ImageView iv_image1,iv_image2,iv_image3,iv_image4,iv_image5;//images[] = new ImageView[5];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usercenter);
		initFakeTitle();
		
		setTitle("个人中心");
		tv_text1 = (TextView) findViewById(R.id.tv_text1);
		tv_text2 = (TextView) findViewById(R.id.tv_text2);
		tv_text3 = (TextView) findViewById(R.id.tv_text3);
		tv_text4 = (TextView) findViewById(R.id.tv_text4);
		tv_text5 = (TextView) findViewById(R.id.tv_text5);
		tv_text6 = (TextView) findViewById(R.id.tv_text6);
		tv_text7 = (TextView) findViewById(R.id.tv_text7);
		
		iv_image1 = (ImageView) findViewById(R.id.iv_image1);
		iv_image2 = (ImageView) findViewById(R.id.iv_image2);
		iv_image3 = (ImageView) findViewById(R.id.iv_image3);
		iv_image4 = (ImageView) findViewById(R.id.iv_image4);
		iv_image5 = (ImageView) findViewById(R.id.iv_image5);
		
//		List<UserInfo> userinfo = db.findAllByWhere(UserInfo.class, "uid='"+GlobalVars.getUid(context)+"'");
//		if(userinfo.size()==0)
//			return;
		tv_text1.setText("账号： "+userInfoPreferences.getString("user_name",""));
		tv_text2.setText("姓名： "+userInfoPreferences.getString("nick_name",""));
		tv_text3.setText("身份证： "+userInfoPreferences.getString("idCard",""));
		tv_text4.setText("银行卡： "+userInfoPreferences.getString("card_number",""));
		tv_text5.setText("邀请码： "+userInfoPreferences.getString("salt",""));
		tv_text6.setText("状态： "+userInfoPreferences.getString("status",""));
		tv_text7.setText("费率： "+userInfoPreferences.getString("rate","")+"‰");
		
		bmp.display(iv_image1, userInfoPreferences.getString("bank_card_heads","")
				);
		bmp.display(iv_image2, userInfoPreferences.getString("bank_card_tails","")
				);
		bmp.display(iv_image3, userInfoPreferences.getString("id_card_hand","")
				);
		bmp.display(iv_image4, userInfoPreferences.getString("id_card_heads","")
				);
		bmp.display(iv_image5, userInfoPreferences.getString("idCard_tails","")
				);
		//LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		
//		for (int i = 0; i < images.length; i++) {
//			images[i] = new ImageView(this);
//			images[i].setScaleType(ScaleType.CENTER_CROP);
//			bmp.display(images[i], userinfo.get(0).getBank_card_heads());
//			ll_images.addView(images[i], layoutParams);
//		}
//		bmp.display(images[0], userinfo.get(0).getBank_card_heads());
//		bmp.display(images[1], userinfo.get(0).getBank_card_tails());
//		bmp.display(images[2], userinfo.get(0).getId_card_hand());
//		bmp.display(images[3], userinfo.get(0).getId_card_heads());
//		bmp.display(images[4], userinfo.get(0).getIdCard_tails());
	}
}
