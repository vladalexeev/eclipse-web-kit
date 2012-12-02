package com.eclipse.web.kit.util.html.parser;

public class HtmlSimpleComment implements HtmlSimpleElement {
	private String comment;
	
	public HtmlSimpleComment(String comment) {
		this.comment=comment;
	}
	
	public String getComment() {
		return comment;
	}
}
