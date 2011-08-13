package com.eclipse.web.kit.util.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.eclipse.web.kit.util.LinkExtractor;
import com.eclipse.web.kit.util.LinkInfo;
import com.eclipse.web.kit.util.LinkPattern;

public class TestLinkExtractor {

	@Test
	public void testExtractLink_BySinglePattern() {
		LinkPattern p=new LinkPattern("href=\"", "\"");
		LinkPattern[] patterns=new LinkPattern[]{p};
		
		List<LinkInfo> list=LinkExtractor.extractLinks("<a href=\"http://127.0.0.1\">test</a>", patterns);
		
		assertNotNull(list);
		assertEquals(1, list.size());
		
		LinkInfo test=list.get(0);
		assertEquals("http://127.0.0.1", test.getLinkFile());
		assertEquals(9, test.getBeginIndex());
		assertEquals(25, test.getEndIndex());
	}
	
	@Test
	public void testExtractLink_ByIntersectingPatterns_Small() {
		LinkPattern p1=new LinkPattern("href=\"", "\"");
		LinkPattern p2=new LinkPattern("href=\"javascript:showImage('","'");
		LinkPattern[] patterns=new LinkPattern[]{p1,p2};
		
		List<LinkInfo> list=LinkExtractor.extractLinks("<a href=\"http://127.0.0.1\">test</a>", patterns);
		
		assertNotNull(list);
		assertEquals(1, list.size());
		
		LinkInfo test=list.get(0);
		assertEquals("http://127.0.0.1", test.getLinkFile());
		assertEquals(9, test.getBeginIndex());
		assertEquals(25, test.getEndIndex());
	}
	
	@Test
	public void testExtractLink_ByIntersectingPatterns_Large() {
		LinkPattern p1=new LinkPattern("href=\"", "\"");
		LinkPattern p2=new LinkPattern("href=\"javascript:showImage('","'");
		LinkPattern[] patterns=new LinkPattern[]{p1,p2};
		
		List<LinkInfo> list=LinkExtractor.extractLinks("<a href=\"javascript:showImage('http://127.0.0.1')\">test</a>", patterns);
		
		assertNotNull(list);
		assertEquals(1, list.size());
		
		LinkInfo test=list.get(0);
		assertEquals("http://127.0.0.1", test.getLinkFile());
		assertEquals(31, test.getBeginIndex());
		assertEquals(47, test.getEndIndex());
	}
	
	@Test
	public void testExtractLinks_ByMultiplePatterns() {
		LinkPattern p1=new LinkPattern("href=\"", "\"");
		LinkPattern p2=new LinkPattern("src=\"","\"");
		LinkPattern[] patterns=new LinkPattern[]{p1,p2};
		
		List<LinkInfo> list=LinkExtractor.extractLinks("<a href=\"http://127.0.0.1\"><img src=\"image.jpg\" /></a>", patterns);
		
		assertNotNull(list);
		assertEquals(2, list.size());
		
		LinkInfo test1=list.get(0);
		assertEquals("http://127.0.0.1", test1.getLinkFile());
		
		LinkInfo test2=list.get(1);
		assertEquals("image.jpg", test2.getLinkFile());
	}

	@Test
	public void testExtractLinks_NoLinks() {
		LinkPattern p1=new LinkPattern("href=\"", "\"");
		LinkPattern p2=new LinkPattern("src=\"","\"");
		LinkPattern[] patterns=new LinkPattern[]{p1,p2};
		
		List<LinkInfo> list=LinkExtractor.extractLinks("11111111111111111111111111111", patterns);
		
		assertNotNull(list);
		assertEquals(0, list.size());
	}
}
