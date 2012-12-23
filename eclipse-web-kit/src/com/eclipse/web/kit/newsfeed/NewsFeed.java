package com.eclipse.web.kit.newsfeed;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eclipse.web.kit.util.XmlUtil;

public class NewsFeed {
	private String feedFileName;
	private String name;
	private String defaultFolder;
	private String defaultAuthor;
	
	private List<DefaultCategory> defaultCategories=new ArrayList<DefaultCategory>();
	
	private List<FeedFile> feedFiles=new ArrayList<FeedFile>();
	
	public NewsFeed() {
		
	}

	public String getFeedFileName() {
		return feedFileName;
	}

	public void setFeedFileName(String feedFileName) {
		this.feedFileName = feedFileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultAuthor() {
		return defaultAuthor;
	}

	public void setDefaultAuthor(String defaultAuthor) {
		this.defaultAuthor = defaultAuthor;
	}

	public String getDefaultFolder() {
		return defaultFolder;
	}

	public void setDefaultFolder(String defaultFolder) {
		this.defaultFolder = defaultFolder;
	}

	public List<FeedFile> getFeedFiles() {
		return feedFiles;
	}

	public void setFeedFiles(List<FeedFile> feedFiles) {
		this.feedFiles = feedFiles;
	}
	
	public List<DefaultCategory> getDefaultCategories() {
		return defaultCategories;
	}

	public void load(Element root) {
		Node sibling=root.getFirstChild();
		while (sibling!=null) {
			if (sibling.getNodeType()==Node.ELEMENT_NODE) {
				Element elem=(Element) sibling;
				String name=elem.getNodeName();
				if (name.equals("feedName")) {
					this.name=XmlUtil.getElementText(elem);
				} else if (name.equals("defaultFolder")) {
					defaultFolder=XmlUtil.getElementText(elem);
				} else if (name.equals("defaultAuthor")) {
					defaultAuthor=XmlUtil.getElementText(elem);
				} else if (name.equals("files")) {
					loadFeedFiles(elem);
				} else if (name.equals("defaultCategories")) {
					loadDefaultCategories(elem);
				}
			}
			
			sibling=sibling.getNextSibling();
		}
	}
	
	private void loadFeedFiles(Element rootFiles) {
		NodeList list=rootFiles.getElementsByTagName("file");
		for (int i=0; i<list.getLength(); i++) {
			FeedFile feedFile=new FeedFile();
			feedFile.load((Element) list.item(i));
			feedFiles.add(feedFile);
		}
	}
	
	private void loadDefaultCategories(Element rootDefaultCategories) {
		NodeList list=rootDefaultCategories.getElementsByTagName("category");
		for (int i=0; i<list.getLength(); i++) {
			Element e=(Element) list.item(i);
			String path=XmlUtil.findElementValue(e, "path");
			String name=XmlUtil.findElementValue(e, "name");
			if (path!=null && name!=null) {
				defaultCategories.add(new DefaultCategory(path, name));
			}
		}
	}
}
