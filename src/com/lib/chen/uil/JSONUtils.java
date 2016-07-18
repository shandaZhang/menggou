package com.lib.chen.uil;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public final class JSONUtils {
	public static <T> T parseJSON(String jsonStr, Class<T> t) {
		Gson gson = new Gson();
		T bean = gson.fromJson(jsonStr, t);
		return bean;
	}

	/**
	 * 
	 * @param response
	 * @param type
	 *            ���磺 Type type = new TypeToken&ltArrayList&ltAnimeInfo>>() {
	 *            }.getType();
	 * @return
	 */
	public static <T> T parseJSONArray(String response, Type type) {
		Gson gson = new Gson();
		T data = gson.fromJson(response, type);
		return data;
	}

	private JSONUtils() {
	}
}
