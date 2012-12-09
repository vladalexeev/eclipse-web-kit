package com.eclipse.web.kit.util;

import org.w3c.dom.Element;

public class XmlUtil {
	public static String getElementText(Element elem) {
		if (elem.getFirstChild()==null) {
			return null;
		} else {
			return elem.getFirstChild().getNodeValue();
		}
	}
	
	public static int getElementNumeric(Element elem, int defaultValue) {
		String strValue=getElementText(elem);
		if (strValue==null || strValue.length()==0) {
			return defaultValue;
		} else {
			return Integer.parseInt(strValue);
		}
	}
}
