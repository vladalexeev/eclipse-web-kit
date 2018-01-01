package com.eclipse.web.kit.newsfeed.test;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;

import com.eclipse.web.kit.newsfeed.DefaultCategory;
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
		
		assertNotNull(newsFeed.getDefaultCategories());
		List<DefaultCategory> defCategories=newsFeed.getDefaultCategories();
		assertEquals(2, defCategories.size());
		DefaultCategory c1=defCategories.get(0);
		assertEquals("english/anim", c1.getPath());
		assertEquals("Animation", c1.getCategoryName());
		DefaultCategory c2=defCategories.get(1);
		assertEquals("english/library", c2.getPath());
		assertEquals("Figures library", c2.getCategoryName());

		
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
		assertEquals(4, file2.getMaxNews());

		FeedFile file3=newsFeed.getFeedFiles().get(2);
		assertEquals(FeedFile.FeedFileType.HTML, file3.getType());
		assertEquals("english/news/index.html", file3.getFilePath());
		assertEquals("<!-- feedSection -->", file3.getHtmlFeedSectionStart());
		assertEquals("<!-- /feedSection -->", file3.getHtmlFeedSectionEnd());
		assertEquals("<!-- feedDelimiter -->", file3.getHtmlFeedDelimiter());
		assertNotNull(file3.getPattern());
		assertEquals(-1, file3.getMaxNews());		
	}
}
