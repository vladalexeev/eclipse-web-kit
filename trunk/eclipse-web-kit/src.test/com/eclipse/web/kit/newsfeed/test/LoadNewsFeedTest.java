package com.eclipse.web.kit.newsfeed.test;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;

import com.eclipse.web.kit.newsfeed.FeedFile;
import com.eclipse.web.kit.newsfeed.NewsFeed;

import static org.junit.Assert.*;

public class LoadNewsFeedTest {

	@Test
	public void testLoadNewsFeed() throws Exception {
		InputStream is=LoadNewsFeedTest.class.getResourceAsStream("news-feed-example.xml");
		Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		
		NewsFeed newsFeed=new NewsFeed();
		newsFeed.load(doc.getDocumentElement());
		
		assertEquals("Test feed",newsFeed.getName());
		assertEquals("rootFolder/subFolder", newsFeed.getDefaultFolder());
		assertEquals("Default Author", newsFeed.getDefaultAuthor());
		assertEquals(3, newsFeed.getFeedFiles().size());
		
		FeedFile file1=newsFeed.getFeedFiles().get(0);
		assertEquals(FeedFile.FeedFileType.RSS, file1.getType());
		assertEquals("english/rss1.xml", file1.getFilePath());
		assertEquals("${message}", file1.getPattern());
		assertEquals(10, file1.getMaxNews());
		
		FeedFile file2=newsFeed.getFeedFiles().get(1);
		assertEquals(FeedFile.FeedFileType.HTML, file2.getType());
		assertEquals("english/index.html", file2.getFilePath());
		assertEquals("<!-- feedSection -->", file2.getHtmlFeedSectionStart());
		assertEquals("<!-- /feedSection -->", file2.getHtmlFeedSectionEnd());
		assertEquals("<!-- feedDelimiter -->", file2.getHtmlFeedDelimiter());
		assertNotNull(file2.getPattern());
		assertNotNull(file2.getPatternIcon());
		assertEquals(4, file2.getMaxNews());

		FeedFile file3=newsFeed.getFeedFiles().get(2);
		assertEquals(FeedFile.FeedFileType.HTML, file3.getType());
		assertEquals("english/news/index.html", file3.getFilePath());
		assertEquals("<!-- feedSection -->", file3.getHtmlFeedSectionStart());
		assertEquals("<!-- /feedSection -->", file3.getHtmlFeedSectionEnd());
		assertEquals("<!-- feedDelimiter -->", file3.getHtmlFeedDelimiter());
		assertNotNull(file3.getPattern());
		assertNotNull(file3.getPatternIcon());
		assertEquals(-1, file3.getMaxNews());		
	}
}
