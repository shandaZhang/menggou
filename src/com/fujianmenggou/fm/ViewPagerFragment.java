package com.fujianmenggou.fm;

import com.fujianmenggou.util.BaseFragment;

public abstract class ViewPagerFragment extends BaseFragment {
	protected boolean isVisible;

	/**
	 * 在这里实现Fragment数据的缓加载.
	 * 
	 * @param isVisibleToUser
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}

	}

	private void onVisible() {
		layzyLoad();
	};

	protected abstract void layzyLoad();

	protected void onInvisible() {
	}

}
