package com.eclipse.web.kit.util;

public class LinkInfo {
	private String linkFile;
	private String linkAnchor;
	private int beginIndex;
	private int endIndex;
	private int lineNumber;
	
	public LinkInfo() {
		
	}
	
	public LinkInfo(String linkFile, int beginIndex, int endIndex) {
		this.linkFile=linkFile;
		this.beginIndex=beginIndex;
		this.endIndex=endIndex;
	}
	
	public LinkInfo(String linkFile, String linkAnchor, int beginIndex, int endIndex, int lineNumber) {
		this.linkFile=linkFile;
		this.linkAnchor=linkAnchor;
		this.beginIndex=beginIndex;
		this.endIndex=endIndex;
		this.lineNumber=lineNumber;
	}

	public String getLinkFile() {
		return linkFile;
	}

	public void setLinkFile(String linkFile) {
		this.linkFile = linkFile;
	}
	
	public String getLinkAnchor() {
		return linkAnchor;
	}

	public void setLinkAnchor(String linkAnchor) {
		this.linkAnchor = linkAnchor;
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

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

}
