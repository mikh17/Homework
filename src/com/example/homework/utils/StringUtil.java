package com.example.homework.utils;

public final class StringUtil {

	public static boolean isNullOrEmpty(String str) {

		if (str == null || str.length() == 0 || "null".equals(str))
			return true;
		return false;
	}
}
