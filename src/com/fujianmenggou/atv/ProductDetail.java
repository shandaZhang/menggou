package com.fujianmenggou.atv;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.Tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetail extends BaseActivity {
	private EditText edittext_suan;
	private ImageView iv_pic,imageView_jian,imageView_jia;
	private TextView jisuan_button_textview,tv_stock,tv_content,tv_name,tv_price;
	private String id,name,pic,stock,price,content;
	//private Double _price;
	//private int _stock;
	private int counts = 0, all = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productdetail);
		
		initFakeTitle();
		
		edittext_suan = (EditText) findViewById(R.id.editText_suan);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		imageView_jian = (ImageView) findViewById(R.id.imageView_jian);
		imageView_jia = (ImageView) findViewById(R.id.imageView_jia);
		jisuan_button_textview = (TextView) findViewById(R.id.jishu_button_textview);
		tv_stock = (TextView) findViewById(R.id.tv_stock);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_price = (TextView) findViewById(R.id.tv_price);
		
		id=getIntent().getStringExtra("id");
		name=getIntent().getStringExtra("name");
		pic=getIntent().getStringExtra("pic");
		stock=getIntent().getStringExtra("stock");
		price=getIntent().getStringExtra("price");
		content=getIntent().getStringExtra("content");
		
		//_stock = Integer.parseInt(stock);
		//_price = Double.parseDouble(price);
		
		imageView_jia.setOnClickListener(this);
		imageView_jian.setOnClickListener(this);
		jisuan_button_textview.setOnClickListener(this);
		
		bmp.display(iv_pic, pic);
		tv_name.setText(name);
		tv_price.setText("活动价：￥"+price);
		tv_stock.setText("库存"+stock+"件");
		tv_content.setText(content);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		counts = Integer.parseInt(edittext_suan.getText().toString().trim());
		all = Integer.parseInt(stock);
		super.onClick(v);
		switch (v.getId()) {
		case R.id.imageView_jia:
			if(counts<all)
				counts++;
			else{
				Tools.showTextToast(context, "数量不能高于库存");
			}
			edittext_suan.setText(counts+"");
			break;

		case R.id.imageView_jian:
			if(counts>1)
				counts--;
			else{
				Tools.showTextToast(context, "数量不能少于1");
			}
			edittext_suan.setText(counts+"");
			break;

		case R.id.jishu_button_textview:
			startActivity(new Intent(context, JiesuanActicity.class)
			.putExtra("counts", counts)
			.putExtra("id", id)
			.putExtra("name", name)
			.putExtra("pic", pic)
			.putExtra("stock", stock)
			.putExtra("price", price)
			.putExtra("content", content)
			);
			
			/*
			 startActivity(new Intent(getActivity(), ProductDetail.class)
			.putExtra("id", mData.get(cPosition).get("id"))
			.putExtra("name", mData.get(cPosition).get("name"))
			.putExtra("pic", mData.get(cPosition).get("pic"))
			.putExtra("stock", mData.get(cPosition).get("stock"))
			.putExtra("price", mData.get(cPosition).get("price"))
			.putExtra("content", mData.get(cPosition).get("content"))
					); 
			 */
			break;

		default:
			break;
		}
	}
}
