package com.eclipse.web.kit.popup.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.action.IAction;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.newsfeed.NewsFeed;
import com.eclipse.web.kit.overlay.ProjectPropertyStore;
import com.eclipse.web.kit.preferences.PreferenceConstants;
import com.eclipse.web.kit.util.DialogUtil;
import com.eclipse.web.kit.util.SwtUtil;
import com.eclipse.web.kit.views.PublishNewsDialog;

public class PublishNewsAction extends FilePopupAction {

	public PublishNewsAction() {
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

	}

}
