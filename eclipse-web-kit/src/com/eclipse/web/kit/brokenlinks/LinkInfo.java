package com.eclipse.web.kit.brokenlinks;

public class LinkInfo {
	private String link;
	private int beginIndex;
	private int endIndex;
	
	public LinkInfo() {
		
	}
	
	public LinkInfo(String link, int beginIndex, int endIndex) {
		this.link=link;
		this.beginIndex=beginIndex;
		this.endIndex=endIndex;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
}
