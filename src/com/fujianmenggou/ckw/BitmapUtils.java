package com.fujianmenggou.ckw;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public final class BitmapUtils {
	
	public static Bitmap compressImageFromFile(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        newOpts.inJustDecodeBounds = true;//只锟斤拷锟斤拷,锟斤拷锟斤拷锟斤拷锟斤拷  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
  
        newOpts.inJustDecodeBounds = false;  
        int srcWidth = newOpts.outWidth;  
        int srcHeight = newOpts.outHeight;  
        float distWidth = 60f;// 压锟斤拷锟斤拷锟酵计拷锟斤拷  
        float distHeight = 60f;// 压锟斤拷锟斤拷锟酵计拷叨锟�  
        int be = 1;  
        if (srcWidth > srcHeight && srcWidth > distWidth) {  
            be = (int) (srcWidth / distWidth);  
        } else if (srcWidth < srcHeight && srcHeight > distHeight) {  
            be = (int) (srcHeight / distHeight);  
        }  
        if (be <= 0) { 
        	be = 1;  
        }
        newOpts.inSampleSize = be;//锟斤拷锟矫诧拷锟斤拷锟斤拷  
          
        newOpts.inPreferredConfig = Config.ARGB_8888;//锟斤拷模式锟斤拷默锟较碉拷,锟缴诧拷锟斤拷  
        newOpts.inPurgeable = true;// 同时锟斤拷锟矫才伙拷锟斤拷效  
        newOpts.inInputShareable = true;//锟斤拷锟斤拷系统锟节存不锟斤拷时锟斤拷图片锟皆讹拷锟斤拷锟斤拷锟斤拷  
          
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
//      return compressBmpFromBmp(bitmap);//原锟斤拷锟侥凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟酵硷拷锟斤拷卸锟斤拷锟窖癸拷锟�  
                                    //锟斤拷实锟斤拷锟斤拷效锟斤拷,锟斤拷揖锟斤拷艹锟斤拷锟�  
        return bitmap;  
    }  
	public static Bitmap compressBmpFromBmp(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > 100) {
			baos.reset();
			options -= 10;
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bmp = BitmapFactory.decodeStream(isBm, null, null);
		return bmp;
	}
	
	private BitmapUtils(){}
}
