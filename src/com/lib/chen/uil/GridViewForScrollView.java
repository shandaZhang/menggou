package com.lib.chen.uil;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

public class GridViewForScrollView extends GridView {
	public GridViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * ��д�÷������ﵽʹListView��ӦScrollView��Ч��
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec
			(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
