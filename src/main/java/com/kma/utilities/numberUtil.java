package com.kma.utilities;

public class numberUtil {
	public static boolean isNumber(String value) {
		try {
			Long.parseLong(value);
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
}
