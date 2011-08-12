package com.eclipse.web.kit.brokenlinks;

import java.util.ArrayList;
import java.util.List;

public class LinkExtractor {
	
	private static boolean matchPatternPrefix(String str, int startIndex, LinkPattern pattern) {
		String prefix=pattern.getPrefixText();
		
		return str.substring(startIndex, startIndex+prefix.length()).equals(prefix);
	}
	
	public static List<LinkInfo> extractLinks(String str, LinkPattern[] patterns) {
		int index=0;
		ArrayList<LinkInfo> result=new ArrayList<LinkInfo>();

		while (index<str.length()) {
			char c=str.charAt(index);
			LinkPattern selectedPattern=null;
			
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
				result.add(new LinkInfo(linkStr, linkStartIndex, linkEndIndex));
				index=linkEndIndex+selectedPattern.getPostfixText().length();
			}
		}
		
		return result;
	}
}
