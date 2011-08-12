package com.eclipse.web.kit.brokenlinks;

public class LinkPattern {
	String prefixText;
	String postfixText;
	
	public LinkPattern() {
		
	}
	
	public LinkPattern(String prefixText, String postfixText) {
		this.prefixText=prefixText;
		this.postfixText=postfixText;
	}

	public String getPrefixText() {
		return prefixText;
	}

	public void setPrefixText(String prefixText) {
		this.prefixText = prefixText;
	}

	public String getPostfixText() {
		return postfixText;
	}

	public void setPostfixText(String postfixText) {
		this.postfixText = postfixText;
	}
}
