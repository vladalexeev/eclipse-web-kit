package com.eclipse.web.kit.newsfeed;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.eclipse.web.kit.util.XmlUtil;

public class FeedFile {
	public enum FeedFileType {
		RSS,
		HTML
	}
	
	private FeedFileType type;
	private String filePath;
	private int maxNews=-1;
	private String pattern;
	private String patternIcon;
	
	private String htmlFeedSectionStart;
	private String htmlFeedSectionEnd;
	private String htmlFeedDelimiter;
	
	public FeedFile() {
		
	}

	public FeedFileType getType() {
		return type;
	}

	public void setType(FeedFileType type) {
		this.type = type;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getMaxNews() {
		return maxNews;
	}

	public void setMaxNews(int maxNews) {
		this.maxNews = maxNews;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getPatternIcon() {
		return patternIcon;
	}

	public void setPatternIcon(String patternIcon) {
		this.patternIcon = patternIcon;
	}

	public String getHtmlFeedSectionStart() {
		return htmlFeedSectionStart;
	}

	public void setHtmlFeedSectionStart(String htmlFeedSectionStart) {
		this.htmlFeedSectionStart = htmlFeedSectionStart;
	}

	public String getHtmlFeedSectionEnd() {
		return htmlFeedSectionEnd;
	}

	public void setHtmlFeedSectionEnd(String htmlFeedSectionEnd) {
		this.htmlFeedSectionEnd = htmlFeedSectionEnd;
	}

	public String getHtmlFeedDelimiter() {
		return htmlFeedDelimiter;
	}

	public void setHtmlFeedDelimiter(String htmlFeedDelimiter) {
		this.htmlFeedDelimiter = htmlFeedDelimiter;
	}
	
	public void load(Element root) {
		Node sibling=root.getFirstChild();
		boolean hasType=false;
		while (sibling!=null) {
			if (sibling.getNodeType()==Node.ELEMENT_NODE) {
				Element elem=(Element) sibling;
				String name=elem.getTagName();
				if (name.equals("type")) {
					String typeValue=XmlUtil.getElementText(elem);
					if (typeValue.equalsIgnoreCase("html")) {
						type=FeedFileType.HTML;
						hasType=true;
					} else if (typeValue.equalsIgnoreCase("rss")) {
						type=FeedFileType.RSS;
						hasType=true;
					} else {
						throw new RuntimeException("Unsupported feed file type "+typeValue);
					}
				} else if (name.equals("path")) {
					filePath=XmlUtil.getElementText(elem);
				} else if (name.equals("max")) {
					maxNews=XmlUtil.getElementNumeric(elem, -1);
				} else if (name.equals("pattern")) {
					pattern=XmlUtil.getElementText(elem);
				} else if (name.equals("patternIcon")) {
					patternIcon=XmlUtil.getElementText(elem);
				} else if (name.equals("htmlFeedSectionStart")) {
					htmlFeedSectionStart=XmlUtil.getElementText(elem);
				} else if (name.equals("htmlFeedSectionEnd")) {
					htmlFeedSectionEnd=XmlUtil.getElementText(elem);
				} else if (name.equals("htmlFeedDelimiter")) {
					htmlFeedDelimiter=XmlUtil.getElementText(elem);
				}
			}
			
			sibling=sibling.getNextSibling();
		}
		
		if (!hasType) {
			throw new RuntimeException("Feed file type not found");
		}
	}
}
