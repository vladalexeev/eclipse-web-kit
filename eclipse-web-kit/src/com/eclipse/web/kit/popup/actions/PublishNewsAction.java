package com.eclipse.web.kit.popup.actions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.newsfeed.FeedFile;
import com.eclipse.web.kit.newsfeed.NewsFeed;
import com.eclipse.web.kit.newsfeed.FeedFile.FeedFileType;
import com.eclipse.web.kit.overlay.ProjectPropertyStore;
import com.eclipse.web.kit.preferences.PreferenceConstants;
import com.eclipse.web.kit.util.DialogUtil;
import com.eclipse.web.kit.util.FileLoader;
import com.eclipse.web.kit.util.StringUtil;
import com.eclipse.web.kit.util.SwtUtil;
import com.eclipse.web.kit.views.PublishNewsDialog;

public class PublishNewsAction extends FilePopupAction {

	private NewsFeed publishFeed;
	private String publishAnchor;
	private String publishTitle;
	private String publishCategory;
	private String publishText;
	
	private String siteAddress;
	
	public PublishNewsAction() {
	}
	
	private void appendRssItem(Document doc, FeedFile feedFile) {
		Element root=doc.getDocumentElement();
		NodeList nodeList=root.getElementsByTagName("channel");
		if (nodeList.getLength()==0) {
			throw new RuntimeException("No tag channel in RSS file "+feedFile.getFilePath());
		}
		
		Element elemChannel=(Element)nodeList.item(0);
		
		Element elemItem=doc.createElement("item");
		elemChannel.appendChild(elemItem);
		
		Element elemTitle=doc.createElement("title");
		elemTitle.appendChild(doc.createTextNode(publishTitle));
		elemItem.appendChild(elemTitle);
		
		String link=siteAddress;
		if (!link.endsWith("/")) {
			link+="/";
		}
		
		link+=file.getProjectRelativePath().toPortableString();
		
		if (publishAnchor!=null && publishAnchor.length()>0) {
			link+="#"+publishAnchor;
		}
		
		Element elemLink=doc.createElement("link");
		elemLink.appendChild(doc.createTextNode(link));
		elemItem.appendChild(elemLink);
		
		Element elemDescription=doc.createElement("description");
		elemDescription.appendChild(doc.createTextNode(publishText));
		elemItem.appendChild(elemDescription);

		if (publishFeed.getDefaultAuthor()!=null && publishFeed.getDefaultAuthor().length()>0) {
			Element elemAuthor=doc.createElement("author");
			elemAuthor.appendChild(doc.createTextNode(publishFeed.getDefaultAuthor()));
			elemItem.appendChild(elemAuthor);
		}

		if (publishCategory!=null && publishCategory.length()>0) {
			Element elemCategory=doc.createElement("category");
			elemCategory.appendChild(doc.createTextNode(publishCategory));
			elemItem.appendChild(elemCategory);
		}
		
		Element elemGuid=doc.createElement("guid");
		elemGuid.appendChild(doc.createTextNode(UUID.randomUUID().toString()));
		elemItem.appendChild(elemGuid);
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String publishDate=dateFormat.format(new Date());
		
		Element elemPubDate=doc.createElement("pubDate");
		elemPubDate.appendChild(doc.createTextNode(publishDate));
		elemItem.appendChild(elemPubDate);
		
		
		NodeList listLastBuildDate=root.getElementsByTagName("lastBuildDate");
		if (listLastBuildDate.getLength()>0) {
			Element elemLastBuildDate=(Element)listLastBuildDate.item(0);
			elemLastBuildDate.removeChild(elemLastBuildDate.getFirstChild());
			elemLastBuildDate.appendChild(doc.createTextNode(publishDate));
		}		
	}
	
	private void checkRssMaxItems(Document doc, FeedFile feedFile) {
		if (feedFile.getMaxNews()<=0) {
			return;
		}
		
		Element root=doc.getDocumentElement();
		NodeList nodeList=root.getElementsByTagName("channel");
		if (nodeList.getLength()==0) {
			throw new RuntimeException("No tag channel in RSS file "+feedFile.getFilePath());
		}
		
		Element elemChannel=(Element)nodeList.item(0);
		
		
		
		NodeList list=elemChannel.getElementsByTagName("item");
		if (list.getLength()>feedFile.getMaxNews()) {
			while (list.getLength()>feedFile.getMaxNews()) {
				elemChannel.removeChild(list.item(0));
			}
		}
	}
	
	private void publishRSS(FeedFile feedFile) {
		IFile rssFile=file.getProject().getFile(feedFile.getFilePath());
		Document doc;
		try {
			doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(rssFile.getLocation().toFile());
		} catch (Throwable t) {
			throw new RuntimeException("Error loading file "+rssFile.getLocation().toFile(),t);
		}
		
		appendRssItem(doc,feedFile);
		checkRssMaxItems(doc, feedFile);
		
		try {
			Transformer t=TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			t.transform(new DOMSource(doc), new StreamResult(rssFile.getLocation().toFile()));
			rssFile.refreshLocal(IResource.DEPTH_ZERO, null);
			
		} catch (Throwable t) {
			throw new RuntimeException("Error loading file "+rssFile.getLocation().toFile(),t);
		}
		
	}
	
	/**
	 * Create feed item for HTML feed format
	 * @param feedFile
	 * @return
	 */
	private String createHtmlFeedItem(FeedFile feedFile) {
		HashMap<String, String> params=new HashMap<String, String>();
		params.put("title", publishTitle);
		params.put("category", publishCategory);
		params.put("message", publishText);
		
		params.put("patternIcon", "");
		
		String link=file.getProjectRelativePath().toPortableString();
		
		if (publishAnchor!=null && publishAnchor.length()>0) {
			link+="#"+publishAnchor;
		}
		params.put("link", link);
		
		SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy");
		params.put("date", sdf.format(new Date()));
		
		String result=StringUtil.stringReplaceMap(feedFile.getPattern(), params);
		return result;
	}
	
	private void publishHTML(FeedFile feedFile) {
		if (feedFile.getHtmlFeedSectionStart()==null || feedFile.getHtmlFeedSectionStart().length()==0) {
			throw new RuntimeException("No 'htmlFeedSectionStart' defined in feed XML-file "+publishFeed.getFeedFileName());
		}
		
		if (feedFile.getHtmlFeedSectionEnd()==null || feedFile.getHtmlFeedSectionEnd().length()==0) {
			throw new RuntimeException("No 'htmlFeedSectionEnd' defined in feed XML-file "+publishFeed.getFeedFileName());
		}
		
		if (feedFile.getHtmlFeedDelimiter()==null || feedFile.getHtmlFeedDelimiter().length()==0) {
			throw new RuntimeException("No 'htmlFeedDelimiter' defined in feed XML-file "+publishFeed.getFeedFileName());
		}
		
		IFile feedFileHtml=file.getProject().getFile(feedFile.getFilePath());
		
		String fileCharset;
		try {
			fileCharset=feedFileHtml.getCharset();
		} catch (Exception e) {
			throw new RuntimeException("Error getting charset of file "+file, e);
		}
		
		String fileContent;
		try {
			fileContent=FileLoader.loadFile(feedFileHtml.getLocation().toOSString(), fileCharset);
		} catch (Throwable t) {
			throw new RuntimeException("Error reading file "+file, t);
		}
		
		int startFeedIndex=fileContent.indexOf(feedFile.getHtmlFeedSectionStart());
		if (startFeedIndex<0) {
			throw new RuntimeException("Feed start '"+feedFile.getHtmlFeedSectionStart()+"' not found in file "+feedFile.getFilePath());
		}
		
		int endFeedIndex=fileContent.indexOf(feedFile.getHtmlFeedSectionEnd());
		if (endFeedIndex<0) {
			throw new RuntimeException("Feed end '"+feedFile.getHtmlFeedSectionStart()+"' not found in file "+feedFile.getFilePath());
		}
		
		String fileStart=fileContent.substring(0,startFeedIndex);
		String fileEnd=fileContent.substring(endFeedIndex+feedFile.getHtmlFeedSectionEnd().length());
		String feedContent=fileContent.substring(startFeedIndex+feedFile.getHtmlFeedSectionStart().length(), endFeedIndex);
		List<String> feedItems=StringUtil.splitString(feedContent, feedFile.getHtmlFeedDelimiter());
		feedItems.add(0,createHtmlFeedItem(feedFile));
		
		if (feedFile.getMaxNews()>0) {
			while (feedItems.size()>feedFile.getMaxNews()) {
				feedItems.remove(feedItems.size()-1);
			}
		}
		
		String newFileContent=fileStart+feedFile.getHtmlFeedSectionStart();
		for (int i=0; i<feedItems.size(); i++) {
			if (i>0) {
				newFileContent+=feedFile.getHtmlFeedDelimiter();
			}
			newFileContent+=feedItems.get(i);
		}
		newFileContent+=feedFile.getHtmlFeedSectionEnd()+fileEnd;
		
		try {
			FileLoader.saveFile(feedFileHtml.getLocation().toOSString(), newFileContent, fileCharset);
			feedFileHtml.refreshLocal(IResource.DEPTH_ZERO, null);
		} catch (IOException e) {
			throw new RuntimeException("Error saving file "+feedFile.getFilePath(), e);
		} catch (CoreException e) {
			throw new RuntimeException("Error updating file "+feedFile.getFilePath(), e);
		}
	}
	
	private NewsFeed loadNewsFeed(String fileName) throws SAXException, IOException, ParserConfigurationException {
		File file=new File(this.file.getProject().getLocation().toFile(),fileName);
		
		Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		NewsFeed result=new NewsFeed();
		result.setFeedFileName(fileName);
		result.load(doc.getDocumentElement());
		
		return result;
	}

	@Override
	public void run(IAction action) {
		ProjectPropertyStore store=new ProjectPropertyStore(file.getProject(), Activator.getDefault().getPreferenceStore(), 
				PreferenceConstants.PAGE_ID_NEWS_FEEDS);
		
		String strNewsFeeds=store.getString(PreferenceConstants.P_NEWS_FEEDS_DESCRIPTIONS);
		ArrayList<NewsFeed> feeds=new ArrayList<NewsFeed>();
		if (strNewsFeeds!=null) {
			String[] newsFeeds=strNewsFeeds.split("\0");
			for (String newsFeed:newsFeeds) {
				if (newsFeed.length()==0) {
					continue;
				}
				
				try {
					feeds.add(loadNewsFeed(newsFeed));
				} catch (Throwable t) {
					DialogUtil.showError("Error loading feed file "+newsFeed, t);
					return;
				}
			}
		}
		
		if (feeds.size()==0) {
			DialogUtil.showWarning("No feed files defined. Go to project properties and add them at 'News feeds' section");
			return;
		}
		
		PublishNewsDialog dialog=new PublishNewsDialog(SwtUtil.getActiveWorkbenchWindow().getShell(), store, file, feeds);
		dialog.open();

		if (dialog.hasResult()) {
			for (NewsFeed f:feeds) {
				if (f.getFeedFileName().equals(dialog.getResultNewsFeed())) {
					publishFeed=f;
				}
			}
			
			siteAddress=Activator.getOverlayedPreferenceValue(file.getProject(), PreferenceConstants.Q_SITEMAP_BASE_URL);
			if (siteAddress==null || siteAddress.length()==0) {
				throw new RuntimeException("No site address defined in the project properties");
			}
			
			publishAnchor=dialog.getResultAnchor();
			publishTitle=dialog.getResultTitle();
			publishCategory=dialog.getResultCategory();
			publishText=dialog.getResultText();
			
			try {
				for (FeedFile feedFile:publishFeed.getFeedFiles()) {
					if (feedFile.getType()==FeedFileType.RSS) {
						publishRSS(feedFile);
					} else if (feedFile.getType()==FeedFileType.HTML) {
						publishHTML(feedFile);
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
				DialogUtil.showError(t);
			}
		}
	}

}
