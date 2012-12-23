package com.eclipse.web.kit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StringUtil {
	public static String[] splitStringByCommas(String str) {
		String[] result=str.split(",");
		for (int i=0; i<result.length; i++) {
			result[i]=result[i].trim();
		}
		
		return result;
	}
	
	public static List<String> splitString(String text, String delimiter) {
		ArrayList<String> result=new ArrayList<String>();
		
		int index;
		do {
			index=text.indexOf(delimiter);
			if (index>=0) {
				result.add(text.substring(0,index));
				text=text.substring(index+delimiter.length());
			} else {
				result.add(text);
			}
		} while (index>=0);
		
		return result;
	}
	
	/**
	 * Replace all occurences of a 'pattern' to 'replacement' in a string 'text' 
	 * @param text
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static String stringReplaceAll(String text, String pattern, String replacement) {
		String result="";
		List<String> elements=splitString(text, pattern);
		result=elements.get(0);
		for (int i=1; i<elements.size(); i++) {
			result+=replacement+elements.get(i);
		}
		
		return result;
	}
	
	/**
	 * Replaces all patterns in map of parameters in string 'pattern'
	 * @param pattern
	 * @param params
	 * @return
	 */
	public static String stringReplaceMap(String pattern, Map<String,String> params) {
		String result=pattern;
		for (Entry<String, String> e:params.entrySet()) {
			result=stringReplaceAll(result, "${"+e.getKey()+"}", e.getValue());
		}
		
		return result;
	}
}
