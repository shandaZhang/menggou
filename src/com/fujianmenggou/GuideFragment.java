package com.fujianmenggou;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class GuideFragment extends Fragment {

	private int i;

	public GuideFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View inflate = inflater.inflate(R.layout.fragment_guide, container, false);
		ImageView mImageView = (ImageView) inflate.findViewById(R.id.fragment_image);
		mImageView.setImageResource(i);
		return inflate;
	}

	public void setdraw(int i) {
		this.i=i;
		
	}

}
