package com.kma.utilities;

import java.util.Map;

public class mapUtil {
	public static <T> T getObject(Map<String,Object> params, String key, Class<T> tClass) {
		Object obj = params.getOrDefault(key, null);
		if(obj!=null) {
			if(tClass.getTypeName().equals("java.lang.Integer")) {
				obj = obj != null? Integer.valueOf(obj.toString()) : null;
			}
			else if(tClass.getTypeName().equals("java.lang.String")) {
				obj = obj != null? String.valueOf(obj.toString()) : null;
			}
			return tClass.cast(obj);
		}
		return null;
	}
}
