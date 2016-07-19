package com.fujianmenggou.atv;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fujianmenggou.R;
import com.fujianmenggou.util.Base64Coder;
import com.fujianmenggou.util.BaseActivity;
import com.fujianmenggou.util.GlobalVars;
import com.fujianmenggou.util.Tools;

import dujc.dtools.afinal.http.AjaxCallBack;
import dujc.dtools.afinal.http.AjaxParams;
import dujc.dtools.xutils.util.LogUtils;

/**
 * Created by Du on 2015/2/10.
 */
public class Zhengjian extends BaseActivity {

    private ImageView iv_image1,iv_image2,iv_image3,iv_image4;
    private ImageView lastImageView;
//    private UserInfo userInfo;
    private int side = 100;
    private static String url1,url2,url3,url4;
    private String oldurl1;
	private String oldurl2;
	private String oldurl3;
	private String oldurl4;

    private Uri imageUri;//to store the big bitmap
    private final int TAKE_PICTURE = 0x111, CHOOSE_PICTURE = 0x222, CROP_PICTURE = 0x333;
	private boolean isEdit=true;
    private static final String IMAGE_FILE_LOCATION = "file://"+ Tools.getSDPath()+"/.bsetpay/tmpImage.jpg";
    private final static String[] pickWays = {"从图库选择","拍照"};
    private Bitmap bit1, bit2, bit3, bit4;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhengjian);
        initFakeTitle();
        setTitle("证件认证");

        imageUri = Uri.parse(IMAGE_FILE_LOCATION);
        bmp.configDefaultLoadFailedImage(R.drawable.yuanxingjiahao);
        bmp.configDefaultLoadingImage(R.drawable.yuanxingjiahao);
        
        initView();
        initData();
    }
    /** 控件初始化 */
    private void initView() {
    	DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        side = dm.widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((side-40)/2,(side-40)/2);
        params.setMargins(10,0,10,0);

        findViewById(R.id.btn_sure).setOnClickListener(this);
        findViewById(R.id.tv_back).setOnClickListener(this);
        iv_image1 = (ImageView) findViewById(R.id.iv_image1);
        iv_image2 = (ImageView) findViewById(R.id.iv_image2);
        iv_image3 = (ImageView) findViewById(R.id.iv_image3);
        iv_image4 = (ImageView) findViewById(R.id.iv_image4);

        iv_image1.setLayoutParams(params);
        iv_image2.setLayoutParams(params);
        iv_image3.setLayoutParams(params);
        iv_image4.setLayoutParams(params);
        
        iv_image1.setOnClickListener(this);
        iv_image2.setOnClickListener(this);
        iv_image3.setOnClickListener(this);
        iv_image4.setOnClickListener(this);
    }
    private void initData() {
        oldurl1 = userInfoPreferences.getString("id_card_heads","");
        oldurl2 = userInfoPreferences.getString("idCard_tails","");
        oldurl3 = userInfoPreferences.getString("bank_card_heads","");
        oldurl4 = userInfoPreferences.getString("id_card_hand","");
		bmp.display(iv_image1,oldurl1);
        bmp.display(iv_image2,oldurl2);
        bmp.display(iv_image3,oldurl3);
        bmp.display(iv_image4,oldurl4);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_image1:
                lastImageView = iv_image1;
                pickImageDialog();
                break;
            case R.id.iv_image2:
                lastImageView = iv_image2;
                pickImageDialog();
                break;
            case R.id.iv_image3:
                lastImageView = iv_image3;
                pickImageDialog();
                break;
            case R.id.iv_image4:
                lastImageView = iv_image4;
                pickImageDialog();
                break;
            case R.id.btn_sure:
                if (TextUtils.isEmpty(url1)){
                    Tools.showTextToast(context, "四张都要上传哦");
                    return;
                }
                if (TextUtils.isEmpty(url2)){
                    Tools.showTextToast(context,"四张都要上传哦");
                    return;
                }
                if (TextUtils.isEmpty(url3)){
                    Tools.showTextToast(context,"四张都要上传哦");
                    return;
                }
                if (TextUtils.isEmpty(url4)){
                    Tools.showTextToast(context,"四张都要上传哦");
                    return;
                }
                submit();
                break;
            case R.id.tv_back:
            	back();
            	break;
            default:break;
        }
    }
    private void back() {
		if ("".equals(oldurl1)||"".equals(oldurl2)||"".equals(oldurl3)||"".equals(oldurl4))
		{	
			isEdit=false;
		}
		if(!isEdit){
			new AlertDialog.Builder(context)
			.setMessage("您资料已提交成功，谢谢！请退出，待审核结束公司会短信通知您。")
			.setNegativeButton("直接退出", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).create()
			.show();
			return ;
		}else{
			finish();
		}
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if ("".equals(oldurl1)||"".equals(oldurl2)||"".equals(oldurl3)||"".equals(oldurl4))
			{	
				isEdit=false;
			}
			if(!isEdit){
				new AlertDialog.Builder(context)
				.setMessage("您资料已提交成功，谢谢！请退出，待审核结束公司会短信通知您。")
				.setNegativeButton("直接退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).create()
				.show();
				return false;
			}else{
				finish();
			}
			break;

		default:
			break;
		}
    	return super.onKeyDown(keyCode, event);
    }
    private void submit() {//?op=Authentication&IDCardHand=1&IDCardHeads=2&IDCardTails=3&bankCardHeads=4&user_id=63
        Tools.ShowLoadingActivity(context);
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("op","IosAuthentication");
        ajaxParams.put("IDCardHand",url1);
        ajaxParams.put("IDCardHeads",url2);
        ajaxParams.put("IDCardTails",url3);
        ajaxParams.put("bankCardHeads",url4);
        ajaxParams.put("user_id",GlobalVars.getUid(context));
        http.post(GlobalVars.url, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject object = new JSONObject(s);
                    LogUtils.i(s);
                    if (object.getInt("result")==1){
                    	isEdit=true;
                    	Editor info = userInfoPreferences.edit();
                    	info.putString("id_card_heads", GlobalVars.baseUrl+url1);
						info.putString("idCard_tails", GlobalVars.baseUrl+url2);
						info.putString("bank_card_heads", GlobalVars.baseUrl+url3);
						info.putString("id_card_hand", GlobalVars.baseUrl+url4);
						info.commit();
//						iv_image1.setImageBitmap(bit1);
//						iv_image2.setImageBitmap(bit2);
//						iv_image3.setImageBitmap(bit3);
//						iv_image4.setImageBitmap(bit4);
                        Tools.showTextToast(context,"修改成功");
                    }else {
                        Tools.showTextToast(context,object.getString("msg")+"");
                    }
                } catch (JSONException e) {
                	Tools.showTextToast(context,e.getMessage());
                    e.printStackTrace();
                }
                Tools.DismissLoadingActivity(context);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            	Tools.showTextToast(context,"提交失败，网络有问题哦");
                Tools.DismissLoadingActivity(context);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){//result is not correct
            LogUtils.e("requestCode = " + requestCode);
            LogUtils.e("resultCode = " + resultCode);
            LogUtils.e("data = " + data);
            return;
        }else{
            switch (requestCode) {
                case TAKE_PICTURE:
                    LogUtils.d("TAKE_BIG_PICTURE: data = " + data);//it seems to be null
                    cropImageUri(imageUri, 640, 480, CROP_PICTURE);
                    break;
                case CHOOSE_PICTURE:
                    LogUtils.d("CHOOSE_BIG_PICTURE: data = " + data);//it seems to be null
                    ContentResolver resolver = context.getContentResolver();
                    Uri uri = data.getData();
                    try {
                        Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, uri);
                        upLoadPhoto(Tools.zoomBitmap(bm,640,480));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case CROP_PICTURE://from crop_big_picture
                    LogUtils.d("CROP_BIG_PICTURE: data = " + data);//it seems to be null
                    if(imageUri != null){
                        Bitmap bitmap = decodeUriAsBitmap(imageUri);
                        upLoadPhoto(bitmap);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void upLoadPhoto(final Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String strFile = new String(Base64Coder.encodeLines(byteArray));

        Tools.ShowLoadingActivity(context);

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("op", "SingleFile");
        ajaxParams.put("UpFilePath", strFile);
        http.post(GlobalVars.url, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo,
                                  String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
                Tools.DismissLoadingActivity(context);
                Tools.showTextToast(context,
                        "上传失败，可能是网络问题");
            }
            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                Tools.DismissLoadingActivity(context);
                try {
                    JSONObject obj = new JSONObject(t.toString());
                    int result = obj.getInt("result");
                    if(result==1){
                        Tools.showTextToast(context, "上传成功");
                        switch (lastImageView.getId()) {
                            case R.id.iv_image1:
                                url1 = obj.getString("url");
                                bit1 = photo;
                                break;
                            case R.id.iv_image2:
                                url2 = obj.getString("url");
                                bit2 = photo;
                                break;
                            case R.id.iv_image3:
                                url3 = obj.getString("url");
                                bit3 = photo;
                                break;
                            case R.id.iv_image4:
                                url4 = obj.getString("url");
                                bit4 = photo;
                                break;
                        }
                        lastImageView.setImageBitmap(photo);
                    }else{
                        Tools.showTextToast(context,
                                "上传失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Tools.showTextToast(context,
                            "JSONException");
                }
            }
        });
    }

    int flag = -1;
	
    private void pickImageDialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(context)
                .setTitle("选择图片");
        ListView lv=new ListView(context);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context
                , android.R.layout.simple_list_item_single_choice,pickWays);
        lv.setAdapter(adapter);
        lv.setItemsCanFocus(false);//设为可选中1
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//设为可选中2
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                flag=position;//
            }
        });
        dialog.setView(lv);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch (flag) {
                    case 0:
                        startActivityForResult(Tools.doPickPhotoFromGallery(),CHOOSE_PICTURE);
                        break;

                    case 1:
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
                        intent2.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent2, TAKE_PICTURE);
                        break;

                    default:
                        Tools.showTextToast(context, "请先选择图片选取方式");
                        break;
                }
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }
    private Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }
}