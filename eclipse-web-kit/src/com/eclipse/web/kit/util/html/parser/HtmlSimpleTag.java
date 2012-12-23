package com.eclipse.web.kit.util.html.parser;

import java.util.Map;

public class HtmlSimpleTag implements HtmlSimpleElement {
	private String tagName;
	private boolean autoClose=false;
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
	
	public String getAttribute(String name) {
		if (attributes==null) {
			return null;
		}
		
		return attributes.get(name);
	}

	public boolean isAutoClose() {
		return autoClose;
	}

	public void setAutoClose(boolean autoClose) {
		this.autoClose = autoClose;
	}
}
