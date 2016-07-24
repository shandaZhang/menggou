package com.fujianmenggou;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.fujianmenggou.ckw.MyApp;
import com.fujianmenggou.ckw.SupplierFragment;
import com.fujianmenggou.fm.IndexFragment;
import com.fujianmenggou.fm.MyShopFragment;
import com.fujianmenggou.fm.MyShopFragment2;
import com.fujianmenggou.fm.ShoppingMallFragment;
import com.fujianmenggou.fm.UserFragment;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.Tools;

public class MainActivity extends BaseActivity implements OnPageChangeListener,
		OnClickListener {

	private ViewPager viewPager;
	private ImageView tab_icon1;
	private ImageView tab_icon2;
	private ImageView tab_icon3;
	private ImageView tab_icon4;
	private static final Fragment[] fragments = { new IndexFragment(),
			new MyShopFragment(), new SupplierFragment(), new UserFragment() };
	private int lastI = 0;
	private long exitTime;// 用于两次单击返回键退出

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApp.getInstance().addtoStack(this);

		viewPager = (ViewPager) findViewById(R.id.viewpager);

		tab_icon1 = (ImageView) findViewById(R.id.tab_icon1);
		tab_icon2 = (ImageView) findViewById(R.id.tab_icon2);
		tab_icon3 = (ImageView) findViewById(R.id.tab_icon3);
		tab_icon4 = (ImageView) findViewById(R.id.tab_icon4);
		tab_icon1.setOnClickListener(this);
		tab_icon2.setOnClickListener(this);
		tab_icon3.setOnClickListener(this);
		tab_icon4.setOnClickListener(this);

		viewPager.setAdapter(new FragmentPagerAdapter(
				getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int i) {
				switch (i) {
				case 0:
					IndexFragment fragment1 = new IndexFragment();
					return fragment1;
				case 1:
					ShoppingMallFragment fragment2 = new ShoppingMallFragment();
					return fragment2;

				case 2:
					MyShopFragment2 fragment3 = new MyShopFragment2();
					return fragment3;

				case 3:
					UserFragment fragment4 = new UserFragment();
					return fragment4;

				default:
					break;
				}
				IndexFragment fragment = new IndexFragment();
				return fragment;
			}

			@Override
			public int getCount() {
				return fragments.length;
			}
		});
		// viewPager.
		viewPager.setOnPageChangeListener(this);
		//setSelect(0, true);
		setSelect(1, true);
	}

	@Override
	public void onPageScrolled(int i, float v, int i1) {

	}

	@Override
	public void onPageSelected(int i) {
		setSelect(lastI, false);// 取消上一个选中状态
		setSelect(lastI = viewPager.getCurrentItem(), true);// 设置当前选中状态
	}

	@Override
	public void onPageScrollStateChanged(int i) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_icon1:
			viewPager.setCurrentItem(0);
			break;
		case R.id.tab_icon2:
			viewPager.setCurrentItem(1);
			break;
		case R.id.tab_icon3:
			viewPager.setCurrentItem(2);
			break;
		case R.id.tab_icon4:
			viewPager.setCurrentItem(3);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Tools.showTextToast(getApplicationContext(), "再按一次退出程序");
				exitTime = System.currentTimeMillis();
			} else {
				MainActivity.this.finish();
				MyApp.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 设置选中状态
	 * 
	 * @param item
	 * @param able
	 */
	private void setSelect(int item, boolean able) {
		switch (item) {
		case 0:
			tab_icon1.setSelected(able);
			break;
		case 1:
			tab_icon2.setSelected(able);
			break;
		case 2:
			tab_icon3.setSelected(able);
			break;
		case 3:
			tab_icon4.setSelected(able);
			break;
		default:
			break;
		}
	}

}
