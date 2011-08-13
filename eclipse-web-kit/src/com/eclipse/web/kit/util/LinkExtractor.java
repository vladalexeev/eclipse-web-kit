package com.eclipse.web.kit.util;

import java.util.ArrayList;
import java.util.List;


public class LinkExtractor {
	
	private static boolean matchPatternPrefix(String str, int startIndex, LinkPattern pattern) {
		String prefix=pattern.getPrefixText();
		
		try {
			if (startIndex+prefix.length() > str.length()) {
				return false;
			} else {		
				return str.substring(startIndex, startIndex+prefix.length()).equals(prefix);
			}
		} catch (Throwable t) {
			System.out.println("str="+str);
			System.out.println("str.length="+str.length());
			System.out.println("prefix='"+prefix+"'");
			System.out.println("startIndex="+startIndex);
			throw new RuntimeException(t);
		}
	}
	
	public static List<LinkInfo> extractLinks(String str, LinkPattern[] patterns) {
		int index=0;
		ArrayList<LinkInfo> result=new ArrayList<LinkInfo>();
		int lineNumber=1;

		while (index<str.length()) {
			char c=str.charAt(index);
			LinkPattern selectedPattern=null;
			
			if (c=='\n') {
				lineNumber++;
			}
			
			for (LinkPattern p:patterns) {
				if (p.getPrefixText().charAt(0)==c) {
					if (matchPatternPrefix(str, index, p)) {
						if (selectedPattern==null) {
							selectedPattern=p;
						} else {
							if (p.getPrefixText().length()>selectedPattern.getPrefixText().length()) {
								selectedPattern=p;
							}
						}
					}
				} 
			}
			
			if (selectedPattern==null) {			
				index++;
			} else {
				int linkStartIndex=index+selectedPattern.getPrefixText().length();
				int linkEndIndex=str.indexOf(selectedPattern.getPostfixText(), linkStartIndex);
				String linkStr=str.substring(linkStartIndex, linkEndIndex);
				String linkFile;
				String linkAnchor;
				int anchorIndex=linkStr.indexOf('#');
				if (anchorIndex==0) {
					linkFile=null;
					linkAnchor=linkStr.substring(1);
				} else if (anchorIndex<0) {
					linkFile=linkStr;
					linkAnchor=null;
				} else {
					linkFile=linkStr.substring(0, anchorIndex);
					linkAnchor=linkStr.substring(anchorIndex+1);
				}
				result.add(new LinkInfo(linkFile, linkAnchor, linkStartIndex, linkEndIndex, lineNumber));
				index=linkEndIndex+selectedPattern.getPostfixText().length();
			}
		}
		
		return result;
	}
}
