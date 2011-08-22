package com.eclipse.web.kit.util;

public class StringUtil {
	public static String[] splitStringByCommas(String str) {
		String[] result=str.split(",");
		for (int i=0; i<result.length; i++) {
			result[i]=result[i].trim();
		}
		
		return result;
	}
}
