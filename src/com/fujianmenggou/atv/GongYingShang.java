package com.fujianmenggou.atv;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;

import android.os.Bundle;
import dujc.dtools.ViewUtils;

/**
 * Created by Du on 2014/12/31.
 */
public class GongYingShang extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gys);
        ViewUtils.inject(this);
        initFakeTitle();
        setTitle("供应商");
    }
}
