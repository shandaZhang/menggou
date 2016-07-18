package com.fujianmenggou.atv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.*;

import com.fujianmenggou.R;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeiDian extends BaseActivity {

	private ImageView iv_add = null;
	private ImageView imageView_touxiang;
	private LinearLayout ll_1;
	private PopupWindow pop;
	private TextView textView4,tv_shanchu,tv_bianji,tv_chakan,tv_fenxiang;
	private GridView mGV;
	private List<HashMap<String, String>> mData = new ArrayList<HashMap<String,String>>();
	private int pageIndex = 1 ,totalcount =0,/**当前点中的位置*/cPosition =0;
	private String name,pic,wechat;
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_weidian);

			initFakeTitle();
			setTitle("我的微店");
			
			ll_1 = (LinearLayout) findViewById(R.id.ll_1);
			mGV = (GridView) findViewById(R.id.gridView1);
			textView4 = (TextView) findViewById(R.id.textView4);
			imageView_touxiang = (ImageView) findViewById(R.id.imageView_touxiang);
			iv_add = (ImageView) findViewById(R.id.iv_actionbar_add);

			View popView = inflater.inflate(R.layout.pop_product, null);
			tv_shanchu = (TextView) popView.findViewById(R.id.tv_shanchu);
			tv_bianji = (TextView) popView.findViewById(R.id.tv_bianji);
			tv_chakan = (TextView) popView.findViewById(R.id.tv_chakan);
			tv_fenxiang = (TextView) popView.findViewById(R.id.tv_fenxiang);

			pop = initPop(popView);

			initShopInfo();
			initData();
			
			iv_add.setVisibility(View.VISIBLE);
			
			iv_add.setOnClickListener(this);
			tv_bianji.setOnClickListener(this);
			tv_chakan.setOnClickListener(this);
			tv_fenxiang.setOnClickListener(this);
			tv_shanchu.setOnClickListener(this);
			imageView_touxiang.setOnClickListener(this);
			textView4.setOnClickListener(this);

			mGV.setAdapter(adapter);
			mGV.setOnItemClickListener(this);
		
	}

	private void initShopInfo() {
		// TODO Auto-generated method stub
		AjaxParams params = new AjaxParams();
		params.put("op", "GetUserShop");
		params.put("userID", GlobalVars.getUid(context));
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				try {
					JSONObject json=new JSONObject(t);
					if(json.getInt("result")==1){
						JSONArray array = json.getJSONArray("list");
						//for (int i = 0; i < array.length(); i++) {
						name=array.getJSONObject(0).getString("name");
						pic=array.getJSONObject(0).getString("pic");
						wechat=array.getJSONObject(0).getString("wechat");
						textView4.setText(name);
						bmp.display(imageView_touxiang, pic);
						//}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		//mData = new ArrayList<HashMap<String,Object>>();
		AjaxParams params = new AjaxParams();
		params.put("op", "GetUserProduct");
		params.put("userID", GlobalVars.getUid(context));
		params.put("pageSize", "25");
		params.put("pageIndex", pageIndex +"");
		Tools.ShowLoadingActivity(WeiDian.this);
		http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				Tools.DismissLoadingActivity(WeiDian.this);
				try {
					JSONObject jsonObject = new JSONObject(t);
					totalcount=jsonObject.getInt("totalcount");
					JSONArray array = jsonObject.getJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("id", array.getJSONObject(i).getString("id"));
						map.put("name", array.getJSONObject(i).getString("name"));
						map.put("content", array.getJSONObject(i).getString("content"));
						map.put("price", array.getJSONObject(i).getString("price"));
						map.put("stock", array.getJSONObject(i).getString("stock"));
						map.put("pic", array.getJSONObject(i).getString("pic"));
						mData.add(map);
						//addView(map);
					}
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Tools.DismissLoadingActivity(WeiDian.this);
			}
		});
	}

	private PopupWindow initPop(View view){
		PopupWindow pop = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		//设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);
		//设置动画，可以不设置，但是为了更加美观，还是加上
		pop.setAnimationStyle(R.style.popupAnimation);
		return pop;
	}

	BaseAdapter adapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(convertView==null){
				convertView= inflater.inflate(R.layout.item_gridview_2, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.price = (TextView) convertView.findViewById(R.id.tv_price);
				holder.pic = (ImageView) convertView.findViewById(R.id.iv_pic);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.name.setText(mData.get(position).get("name"));
			holder.price.setText(mData.get(position).get("price"));
			bmp.display(holder.pic, (String)mData.get(position).get("pic"));
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mData.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}
	};

	class ViewHolder{
		ImageView pic;
		TextView name,price;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//iv_add.setVisibility(View.GONE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==123&&resultCode==321){
			Bundle bd = data.getBundleExtra("newItem");
			String id=bd.getString("id");
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", id);
			map.put("name", bd.getString("name"));
			map.put("content", bd.getString("content"));
			map.put("price", bd.getString("price"));
			map.put("stock", bd.getString("stock"));
			map.put("pic", GlobalVars.baseUrl+bd.getString("pic"));
			if(mData.size()>0 && mData.get(cPosition).get("id").equals(id)){//存在此id即是更新
				mData.set(cPosition, map);
			}else{
				mData.add(map);
			}
			
			adapter.notifyDataSetChanged();
		}
		if(requestCode==123&&resultCode==32){
			bmp.display(imageView_touxiang, data.getStringExtra("pic"));
			textView4.setText(data.getStringExtra("name"));
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_actionbar_add:
			startActivityForResult((new Intent(WeiDian.this, AddProductActivity.class))
					.putExtra("isAdd", true)
					, 123);
			break;
		case R.id.textView4://修改微店
		case R.id.imageView_touxiang://修改微店
			startActivityForResult((new Intent(WeiDian.this, ModifyShopInfoActivity.class))
					.putExtra("name", name).putExtra("pic", pic).putExtra("wechat", wechat)
					, 123);
			break;
		case R.id.tv_bianji://编辑
			startActivityForResult((new Intent(WeiDian.this, AddProductActivity.class))
					.putExtra("isAdd", false).putExtra("id", mData.get(cPosition).get("id"))
					.putExtra("name", mData.get(cPosition).get("name"))
					.putExtra("pic", mData.get(cPosition).get("pic"))
					.putExtra("stock", mData.get(cPosition).get("stock"))
					.putExtra("price", mData.get(cPosition).get("price"))
					.putExtra("content", mData.get(cPosition).get("content"))
					, 123);
			break;
		case R.id.tv_fenxiang://分享
			/*Uri smsToUri = Uri.parse("smsto:");  
			Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);  
			intent.putExtra("sms_body", "哎呦，这家店不错哦，赶紧来看看吧。"
					+ "\n 地址 http://mgo.fjscan.com/web/Default.aspx?uid="+GlobalVars.getUid(context));  
			startActivity(intent);  */
			// 设置分享内容
			addQQPlatform();
			addWXPlatform();
			addQQZonePlatform();
			mController.setShareContent("哎呦，这家店不错哦，赶紧来看看吧。"
					+ "\n 地址 http://103.27.7.116:83/web/Default.aspx?uid="+GlobalVars.getUid(context));
			// 设置分享图片, 参数2为图片的url地址
			mController.setShareMedia(new UMImage(WeiDian.this, mData.get(cPosition).get("pic")));
			mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
			mController.openShare(WeiDian.this, false);
			break;
		case R.id.tv_chakan://查看
			startActivity(new Intent(WeiDian.this, ProductDetail.class)
			.putExtra("id", mData.get(cPosition).get("id"))
			.putExtra("name", mData.get(cPosition).get("name"))
			.putExtra("pic", mData.get(cPosition).get("pic"))
			.putExtra("stock", mData.get(cPosition).get("stock"))
			.putExtra("price", mData.get(cPosition).get("price"))
			.putExtra("content", mData.get(cPosition).get("content"))
					);
			break;
		case R.id.tv_shanchu://删除
			AlertDialog.Builder dialog = new AlertDialog.Builder(WeiDian.this)
			.setTitle("删除商品").setMessage("确定删除吗？");
			dialog.setNegativeButton("取消", null);
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Tools.ShowLoadingActivity(WeiDian.this);
					AjaxParams params = new AjaxParams();
					params.put("op", "DeleteUserProduct");
					params.put("ID", mData.get(cPosition).get("id"));
					http.get(GlobalVars.url, params, new AjaxCallBack<String>() {
						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							// TODO Auto-generated method stub
							super.onFailure(t, errorNo, strMsg);
							Tools.DismissLoadingActivity(WeiDian.this);
						}
						@Override
						public void onSuccess(String t) {
							// TODO Auto-generated method stub
							super.onSuccess(t);
							Tools.DismissLoadingActivity(WeiDian.this);
							try {
								JSONObject json = new JSONObject(t);
								if(json.getInt("result")==1){
									mData.remove(cPosition);
									adapter.notifyDataSetChanged();
								}else{
									Tools.showTextToast(WeiDian.this, "删除失败，请稍后再试");
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
			});
			dialog.show();
			break;
		default:
			break;
		}
		if(pop.isShowing())
			pop.dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.gridView1:
			if(!pop.isShowing()){
				cPosition=position;
				pop.showAtLocation(ll_1, Gravity.BOTTOM, 0, 0);
			}else{
				cPosition=0;
				pop.dismiss();
			}
			break;

		default:
			break;
		}
	}

	
    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform() {

        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx93f085b6981f9a12"
        		,secret = "00c12ba480e86f4066008c3d098de0e2";//待修改
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(WeiDian.this, appId, secret);
        		//new UMWXHandler(WeiDian.this, appId);
        wxHandler.setTitle("哎呦，这家店不错哦，赶紧来看看吧。");
        wxHandler.setTargetUrl("http://103.27.7.116:83/web/Default.aspx?uid="+GlobalVars.getUid(context));
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(WeiDian.this, appId, secret);
        		//new UMWXHandler(WeiDian.this, appId);
        wxCircleHandler.setTitle("哎呦，这家店不错哦，赶紧来看看吧。");
        wxCircleHandler.setTargetUrl("http://103.27.7.116:83/web/Default.aspx?uid="+GlobalVars.getUid(context));
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        //mController.openShare(WeiDian.this, false);
    }

    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private void addQQPlatform() {
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(WeiDian.this,
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.setTargetUrl("http://103.27.7.116:83/web/Default.aspx?uid="+GlobalVars.getUid(context));
        qqSsoHandler.addToSocialSDK();
        //mController.openShare(WeiDian.this, false);
    }
    
    private void addQQZonePlatform() {
    	//参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
    	QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(WeiDian.this, "100424468",
    	                "c7394704798a158208a74ab60104f0ba");
    	qZoneSsoHandler.addToSocialSDK();
    }
}
