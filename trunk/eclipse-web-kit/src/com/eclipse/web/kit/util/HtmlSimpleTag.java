package com.eclipse.web.kit.util;

import java.util.Map;

public class HtmlSimpleTag implements HtmlSimpleElement {
	private String tagName;
	private Map<String,String> attributes;
	
	public HtmlSimpleTag() {
		
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
}
