package com.eclipse.web.kit.util;

public class HtmlSimpleText implements HtmlSimpleElement {
	private String text;
	
	public HtmlSimpleText(String text) {
		this.text=text;
	}
	
	public String getText() {
		return text;
	}
}
