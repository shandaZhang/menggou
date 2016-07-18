package com.lib.chen.volley;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public final class HTTPUtils {

	public static String post(String url, Map<String, String> map) {
		// �������
		HttpClient client = new DefaultHttpClient();
		// ������ַ
		HttpPost post = new HttpPost(url);
		Set<String> set = map.keySet();
		Iterator<String> itr = set.iterator();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		while (itr.hasNext()){
			String key = itr.next();
			String value = map.get(key);
			NameValuePair pair1 = new 
				BasicNameValuePair(key, value);
			params.add(pair1);
		}
			
		HttpEntity reqEntity;
		try {
			reqEntity = new UrlEncodedFormEntity(params, "utf-8");
			post.setEntity(reqEntity);
			// �س�
			// �������ʾ����
			HttpResponse response = client.execute(post);
			HttpEntity resEntity = response.getEntity();
			String content = EntityUtils.toString(resEntity);
			return content;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;

	}
	
	public static void getVolley(Context context, String url,
			final VolleyListener listener) {
		RequestQueue queue = MyVolley.getRequestQueue(context);
		StringRequest myReq = new UTF8StringRequest(Method.GET, url,
				new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				listener.onResponse(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				listener.onErrorResponse(error);
			}
		});
		queue.add(myReq);
	}

	public static void postVolley(Context context, String url,
			final Map<String, String> map, final VolleyListener listener) {
		RequestQueue queue = MyVolley.getRequestQueue(context);
		Response.Listener<String> SuccListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				listener.onResponse(response);
			}
		};
		Response.ErrorListener errorListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				listener.onErrorResponse(error);
			}
		};
		StringRequest myReq = new UTF8StringRequest(Method.POST, url,
				SuccListener, errorListener){
			@Override
			protected Map<String, String> getParams()
					throws AuthFailureError {
				return map;
			}
		};
		queue.add(myReq);
	}

	private HTTPUtils() {
	}

}
