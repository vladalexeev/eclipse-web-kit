package com.eclipse.web.kit.views.util;

import java.util.Map;
import java.util.Map.Entry;

public class TemplateUtil {

	public static String applyParametersToString(String templateString, Map<String,String> parameters) {
		String result=templateString;
		for (Entry<String,String> e:parameters.entrySet()) {
			String fullParam="{"+e.getKey()+"}";
			while (result.indexOf(fullParam)>=0) {
				int index=result.indexOf(fullParam);
				result=result.substring(0,index)+e.getValue()+result.substring(index+fullParam.length());
			}
		}
		
		return result;
	}
}
