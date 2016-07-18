package com.fujianmenggou.ckw;

import java.util.ArrayList;

import com.fujianmenggou.GuideFragment;
import com.fujianmenggou.LoginActivity;
import com.fujianmenggou.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class GuideActivity extends FragmentActivity implements OnClickListener {
	private int[] mGuideRes ={
			R.drawable.yindao1,
			R.drawable.yindao2,
			R.drawable.yindao3,
			R.drawable.yindao4,
			R.drawable.yindao5};
	private int[] intIds={
		R.id.int0,	
		R.id.int1,	
		R.id.int2,	
		R.id.int3,	
		R.id.int4};
	private ArrayList<ImageView> intList=new ArrayList<ImageView>();
	private View guideText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		for (int i = 0; i < intIds.length; i++) {
			intList.add((ImageView) findViewById(intIds[i]));
		}
		guideText = findViewById(R.id.guide_text);
		guideText.setOnClickListener(this);
		ViewPager mPager = (ViewPager) findViewById(R.id.guide_viewpager);
		FragmentManager fm=getSupportFragmentManager();
		FragmentPagerAdapter adapter=new FragmentPagerAdapter(fm){

			@Override
			public Fragment getItem(int position) {
				// TODO Auto-generated method stub
				GuideFragment fragment = new GuideFragment();
				fragment.setdraw(mGuideRes[position]);
				return fragment;
			}
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mGuideRes.length;
			}
		};
		mPager.setAdapter(adapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				for (int i = 0; i < intList.size(); i++) {
					if(i==pos){
						intList.get(i).setImageResource(R.drawable.page_control_n);
					}else{
						intList.get(i).setImageResource(R.drawable.page_control_h);
					}
				}
				if(pos==(intList.size()-1)){
					guideText.setVisibility(View.VISIBLE);
				}else{
					guideText.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.guide, menu);
		return true;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			return false;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}

}
